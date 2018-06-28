package com.gui.core.mapTree;

import com.gui.core.layers.AbstractLayer;
import com.gui.core.layers.LayerGroup;
import javafx.scene.control.TreeItem;

public interface LayeredViewTree extends ViewTree<AbstractLayer> {

//    void removeTreeItemByName(String name);

    void reloadData();

    <P extends TreeItem<AbstractLayer>> P addLayer(AbstractLayer layer, LayerGroup parent);

    <P extends TreeItem<AbstractLayer>> P editLayer(AbstractLayer layer);

    <P extends TreeItem<AbstractLayer>> P removeLayer(AbstractLayer layer);

    String dumpTree();

    <P extends TreeItem<AbstractLayer>> P createTreeItem(AbstractLayer layer);

    <P extends TreeItem<AbstractLayer>> void stopEditing();

    AbstractLayer getModifiedLayer();
}
