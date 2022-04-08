package de.ollie.servicemonitor.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import de.ollie.servicemonitor.model.CheckResult;

public class SingleMonitoringResultTreeNode implements TreeNode {

	private CheckResult checkResult;
	private MonitoringResultGroupTreeNode parent;

	public SingleMonitoringResultTreeNode(CheckResult checkResult) {
		this.checkResult = checkResult;
	}

	public CheckResult getCheckResult() {
		return checkResult;
	}

	public void setParent(MonitoringResultGroupTreeNode parent) {
		this.parent = parent;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration(new ArrayList<TreeNode>());
	}

}
