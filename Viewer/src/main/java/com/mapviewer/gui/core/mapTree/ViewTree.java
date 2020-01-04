package com.mapviewer.gui.core.mapTree;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public interface ViewTree<TI> {

    <TREEVIEW extends TreeView> TREEVIEW getTree();

    void regenerateTree();

    void reloadData();

    <P extends TreeItem<TI>> void handleTreeItemClick(P item);

    <P extends TreeItem<TI>> P findTreeItemByValue(TI obj);

    void addTreeItemAction(TreeItem<TI> newItem, TreeItem<TI> parentOfNewItem);

    void removeTreeItemAction(TreeItem<TI> item);

    void editTreeItemAction(TreeItem<TI> item);

    String dumpTree();
}
