package com.gui.core.mapTree.internal;

import com.gui.core.mapTree.ViewTree;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Collections;

public abstract class ViewTreeImpl<T> extends TreeView<T> implements ViewTree<T> {

	public ViewTreeImpl() {
		getSelectionModel().selectedItemProperty().addListener(listener -> handleTreeItemClick(getSelectionModel().getSelectedItem()));
		setEditable(true);
	}
	
	public void setTreeBound(int x, int y, int width, int height) {
		this.setPrefWidth(width);
		this.setPrefHeight(height);
		this.setWidth(width);
		this.setHeight(height);
	}

	public ContextMenu getPopupMenu(TreeItem<T> treeItem) {
		
		ContextMenu popup = new ContextMenu();		
		
		MenuItem menuItemDelete = new MenuItem("Delete");
		menuItemDelete.setOnAction(handler -> {
			removeTreeItemAction(treeItem);
		});
		
		popup.getItems().add(menuItemDelete);
		
		return popup;
	}

	@Override
	public <P extends TreeItem<T>> P findTreeItemByValue(T value) {
		return findTreeItemByValue(value, getRoot());
	}

	private <P extends TreeItem<T>> P findTreeItemByValue(T value, TreeItem<T> node) {
		if (node.getValue() != null && node.getValue().equals(value))
			return (P) node;

		for (TreeItem<T> child : node.getChildren()) {
			TreeItem<T> res = findTreeItemByValue(value, child);
			if (res != null && res.getValue() != null && res.getValue().equals(value))
				return (P) res;
		}

		return null;
	}

	@Override
	public <P extends TreeItem<T>> void handleTreeItemClick(P item) {}
	
	public String dumpTree() {
		return dumpTree(getRoot(), 0);
	}
	
	private String dumpTree(TreeItem<T> item, int depth) {
		String ans = String.join("", Collections.nCopies(depth, " ")) + item.toString() + "\n"; 
		for (TreeItem<T> child : item.getChildren()) {
			ans += dumpTree(child, depth + 1);
		}
		return ans;
	}

	public abstract void updateTreeItemName(String fromName, TreeItem<T> treeItem);

	@Override
	public <T extends TreeView> T getTree() {
		return (T) this;
	}

	@Override
	public void regenerateTree() {}

	@Override
	public void reloadData() {}

	/*
	 * This method start the remove of a tree item, it should be the only one who actually interact with the map
	 * to prevent loops
	 */
	@Override
	public void addTreeItemAction(TreeItem<T> newItem, TreeItem<T> parentOfNewItem) {
		if (!parentOfNewItem.getChildren().contains(newItem))
			parentOfNewItem.getChildren().add(newItem);
	}

	@Override
	public void removeTreeItemAction(TreeItem<T> item) {
			TreeItem parent = item.getParent();
			if (parent != null)
				parent.getChildren().remove(item);
	}

	@Override
	public void editTreeItemAction(TreeItem<T> item) {

	}
}
