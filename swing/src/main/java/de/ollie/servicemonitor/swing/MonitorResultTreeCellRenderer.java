package de.ollie.servicemonitor.swing;

import java.awt.Component;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import de.ollie.servicemonitor.MessageValueReplacer;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.CheckResult.Status;
import de.ollie.servicemonitor.model.OutputAlternative;
import de.ollie.servicemonitor.model.OutputColumn;
import de.ollie.servicemonitor.model.OutputColumn.Alignment;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MonitorResultTreeCellRenderer implements TreeCellRenderer {

	private final int fontSize;
	private final MessageValueReplacer messageValueReplacer;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
	        boolean leaf, int row, boolean hasFocus) {
		String color = "black";
		String message = "-";
		if (value instanceof MonitoringResultRootTreeNode) {
			message = "" + LocalDateTime.now();
		} else if (value instanceof MonitoringResultGroupTreeNode) {
			MonitoringResultGroupTreeNode node = ((MonitoringResultGroupTreeNode) value);
			message = node.getName();
			color = getStatusColor(node.getStatus());
		} else if (value instanceof SingleMonitoringResultTreeNode) {
			CheckResult checkResult = ((SingleMonitoringResultTreeNode) value).getCheckResult();
			CheckRequest checkRequest = checkResult.getCheckRequest();
			message = checkRequest
			        .getGroup()
			        .getOutput()
			        .getColumns()
			        .stream()
			        .map(outputColumn -> convertOutputColumnToString(checkResult, outputColumn, checkRequest))
			        .reduce((s0, s1) -> s0 + " | " + s1)
			        .orElse("n/a")
			        .replace(" ", "&nbsp;");
			color = getStatusColor(checkResult.getStatus());
		}
		String s = "<html><font size=\"" + fontSize
		        + "\" style=\"color:{color};\"><tt>{message}</tt></font></html>"
		        .replace("{color}", color)
		        .replace("{message}", message);
		return new JLabel(s);
	}

	private String getStatusColor(Status status) {
		if (status == Status.OK) {
			return "green";
		} else if (status == Status.WARN) {
			return "#FFD700";
		}
		return "red";
	}

	private String convertOutputColumnToString(CheckResult checkResult, OutputColumn outputColumn,
	        CheckRequest checkRequest) {
		String content = findOutputAlternative(checkRequest, outputColumn.getId())
		        .map(OutputAlternative::getContent)
		        .orElse(outputColumn.getContent());
		String message = messageValueReplacer.getMessageWithReplacesValues(content, checkRequest, checkResult);
		return String.format(getFormatString(outputColumn), message);
	}

	private String getFormatString(OutputColumn outputColumn) {
		return "%" + (outputColumn.getAlign() == Alignment.LEFT ? "-" : "") + getOutputColumnWidth(outputColumn) + "s";
	}

	private String getOutputColumnWidth(OutputColumn outputColumn) {
		return outputColumn.getWidth() > 0 ? "" + outputColumn.getWidth() : "";
	}

	private Optional<OutputAlternative> findOutputAlternative(CheckRequest checkRequest, String id) {
		return checkRequest.getOutputAlternatives() == null
		        ? Optional.empty()
		        : checkRequest
		                .getOutputAlternatives()
		                .stream()
		                .filter(outputAlternative -> outputAlternative.getId().equals(id))
		                .findFirst();
	}

}
