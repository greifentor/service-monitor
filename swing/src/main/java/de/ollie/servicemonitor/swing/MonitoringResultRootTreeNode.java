package de.ollie.servicemonitor.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.TreeNode;

public class MonitoringResultRootTreeNode implements TreeNode {

	private List<MonitoringResultGroupTreeNode> children = new ArrayList<>();
	private String name;

	public MonitoringResultRootTreeNode(String name) {
		super();
		this.name = name;
	}

	public void addNode(MonitoringResultGroupTreeNode node) {
		node.setParent(this);
		children.add(node);
	}

	public List<MonitoringResultGroupTreeNode> getGroups() {
		return children.stream().collect(Collectors.toList());
	}

	public String getName() {
		return name;
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
		return null;
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
