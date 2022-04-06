package de.ollie.servicemonitor.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.TreeNode;

import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.CheckResult.Status;

public class MonitoringResultGroupTreeNode implements TreeNode {

	private List<SingleMonitoringResultTreeNode> children = new ArrayList<>();
	private String name;
	private MonitoringResultRootTreeNode parent;

	public MonitoringResultGroupTreeNode(String name) {
		super();
		this.name = name;
	}

	public void addNode(SingleMonitoringResultTreeNode node) {
		node.setParent(this);
		children.add(node);
	}

	public List<CheckResult> getCheckResults() {
		return children.stream().map(treeNode -> treeNode.getCheckResult()).collect(Collectors.toList());
	}

	public Status getStatus() {
		return getCheckResults()
		        .stream()
		        .map(CheckResult::getStatus)
		        .reduce((st0, st1) -> st0.max(st1))
		        .orElse(Status.OK);
	}

	public String getName() {
		return name;
	}

	public void setParent(MonitoringResultRootTreeNode parent) {
		this.parent = parent;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(children);
	}

}