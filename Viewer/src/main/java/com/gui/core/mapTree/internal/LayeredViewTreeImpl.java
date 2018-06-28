package com.gui.core.mapTree.internal;

import com.gui.core.layers.AbstractLayer;
import com.gui.core.layers.LayerGroup;
import com.gui.core.layers.LayerSingle;
import com.gui.core.mapTree.LayeredViewTree;
import com.gui.core.mapViewer.LayeredViewMap;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.util.Arrays;
import java.util.Date;

public abstract class LayeredViewTreeImpl<S extends TreeItem<AbstractLayer>> extends ViewTreeImpl<AbstractLayer> implements LayeredViewTree {

	public LayeredViewTreeImpl() {
		super();
	}

	public abstract LayeredViewMap getLayeredViewMap();

	protected AbstractLayer modifiedLayer;

	public S loadTree(AbstractLayer root) {
		S parent = createTreeItem(root);
		addSelectionHandler(parent);
		if (root instanceof LayerGroup) {
			for (AbstractLayer l : ((LayerGroup) root).getChildren()) {
				super.addTreeItemAction(loadTree(l), parent);
			}
		}
		return parent;
	}

	@Override
	public AbstractLayer getModifiedLayer() {
		return modifiedLayer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public S addLayer(AbstractLayer layer, LayerGroup layerGroup) {
		System.out.println("Adding Layer");
		S layerGroupTreeItem = findTreeItemByValue(layerGroup);
		if (layerGroupTreeItem == null) {
			System.err.println("Failed to find group");
			return null;
		}
		S layerTreeItem = findTreeItemByValue(layer);
		layerTreeItem = createTreeItem(layer);
		
		addTreeItemAction(layerTreeItem, layerGroupTreeItem);
		return layerTreeItem;
	}

	@Override
	public S editLayer(AbstractLayer layer) {
		S layerTreeItem = findTreeItemByValue(layer);
		if (layerTreeItem == null) {
			System.err.println("Failed to find layer");
			return null;
		}
		editTreeItemAction(layerTreeItem);
		return layerTreeItem;
	}

	@Override
	public S removeLayer(AbstractLayer layer) {
		S layerTreeItem = findTreeItemByValue(layer);
		if (layerTreeItem == null) {
			System.err.println("Failed to find tree item");
			return null;
		}

		removeTreeItemAction(layerTreeItem);
		return layerTreeItem;
	}

	@Override
	public void updateTreeItemName(String fromName, TreeItem<AbstractLayer> treeItem) {
		System.out.println("Tree Item was updated from '" + fromName + "' to '" + treeItem + "'");
	}

	@Override
	public ContextMenu getPopupMenu(TreeItem<AbstractLayer> treeItem) {
		ContextMenu popup = super.getPopupMenu(treeItem);

		if (treeItem.getValue() instanceof LayerGroup) {
			MenuItem menuItemAddLayer = new MenuItem("Add Layer");
			popup.getItems().add(menuItemAddLayer);

			menuItemAddLayer.setOnAction(handler -> {
				AbstractLayer layer = new LayerSingle("Layer" + (new Date().getTime()), getLayeredViewMap());
				layer.setWasEdited(true);
				TreeItem<AbstractLayer> newChild = createTreeItem(layer);
				addTreeItemAction(newChild, treeItem);
			});

			MenuItem menuItemAddLayerGroup = new MenuItem("Add Layer Group");
			popup.getItems().add(menuItemAddLayerGroup);

			menuItemAddLayerGroup.setOnAction(handler -> {
				LayerGroup layer = new LayerGroup("LayerGroup" + (new Date().getTime()));
				TreeItem<AbstractLayer> newChild = createTreeItem(layer);
				addTreeItemAction(newChild, treeItem);
			});
		}

		MenuItem menuItemRenameLayer = new MenuItem("Rename");
		popup.getItems().add(menuItemRenameLayer);

		menuItemRenameLayer.setOnAction(handler -> {});

		if (treeItem.getValue() instanceof LayerSingle) {
			MenuItem menuItemEditLayer = new MenuItem("Edit");
			popup.getItems().add(menuItemEditLayer);

			menuItemEditLayer.setOnAction(handler -> {
				editTreeItemAction(treeItem);
			});
		}

		return popup;
	}

	protected abstract void addSelectionHandler(S item);

	@Override
	public void addTreeItemAction(TreeItem<AbstractLayer> newItem, TreeItem<AbstractLayer> parentOfNewItem) {
		AbstractLayer newLayer = newItem.getValue();
		LayerGroup layerGroup = (LayerGroup) parentOfNewItem.getValue();
		layerGroup.addChildren(newLayer);
		super.addTreeItemAction(newItem, parentOfNewItem);
	}

	@Override
	public void editTreeItemAction(TreeItem<AbstractLayer> item) {
		modifiedLayer = item.getValue();
		getLayeredViewMap().showLayer(modifiedLayer);
		getLayeredViewMap().setEditedLayer(modifiedLayer);
		super.editTreeItemAction(item);
	}

	@Override
	public void removeTreeItemAction(TreeItem<AbstractLayer> treeItem) {
		if (treeItem.getParent() == null)
			return;

		getLayeredViewMap().removeLayers(Arrays.asList(treeItem.getValue()));
		if (treeItem.getValue() instanceof LayerGroup)
			((LayerGroup) treeItem.getValue()).unlinkGroup();

		treeItem.getValue().setParent(null);
		super.removeTreeItemAction(treeItem);
	}

	@Override
	public void stopEditing() {
		modifiedLayer = null;
	}
}
