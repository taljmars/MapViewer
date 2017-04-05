package com.gui.core.mapTree;

import org.springframework.context.annotation.ComponentScan;

import com.gui.core.mapTreeObjects.Layer;
import com.gui.core.mapTreeObjects.LayerGroup;
import com.gui.core.mapViewer.LayeredViewMap;
import javafx.scene.control.TreeItem;

import javax.annotation.Resource;

@ComponentScan("com.gui.core.mapViewer")
public abstract class LayeredViewTree<S extends TreeItem<Layer>> extends ViewTree<Layer> {
	
	@SuppressWarnings("rawtypes")
	private Class<? extends TreeItem> clazz;

	private LayeredViewMap layeredViewMap;

	public LayeredViewTree(S instance) {
		super();
		this.clazz = instance.getClass();
	}

	public LayeredViewMap getLayeredViewMap() {
		return layeredViewMap;
	}

	@Resource(type = LayeredViewMap.class)
	public void setLayeredViewMap(LayeredViewMap layeredViewMap) {
		this.layeredViewMap = layeredViewMap;
	}

	@SuppressWarnings("unchecked")
	protected S addTreeNode(Layer layer, LayerGroup layerGroup) {
		S layerGroupTreeitem = findCheckBoxTreeItemByLayer(layerGroup);
		if (layerGroupTreeitem == null) {
			System.err.println("Failed to find group");
			return null;
		}
		S newTreeItem;
		try {
			newTreeItem = (S) clazz.newInstance();
			newTreeItem.setValue(layer);
			return (S) super.addTreeNode(newTreeItem, layerGroupTreeitem);
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("Failed to instansiate Tree item");
			return null;
		}
	}
	
	protected void removeFromTreeGroup(Layer layer) {
		S treeitem = findCheckBoxTreeItemByLayer(layer);
		if (treeitem == null) {
			System.err.println("Failed to find layer");
			return;
		}
		
		S parentTreeitem = findCheckBoxTreeItemByLayer(layer.getParent());
		if (parentTreeitem == null) {
			System.err.println("Failed to find layer parent");
			return;
		}
		
		super.removeTreeNode(treeitem, parentTreeitem);
	}
	
	public S findCheckBoxTreeItemByLayer(Layer layer) {
		@SuppressWarnings("unchecked")
		S res = (S) super.findTreeItemByValue(layer, getRoot());
		return res;
	}
	
	@Override
	protected void handleRemoveTreeItem(TreeItem<Layer> treeItem) {
		Layer layer = treeItem.getValue();
		layeredViewMap.removeLayer(layer);
		super.handleRemoveTreeItem(treeItem);
	}

	@Override
	public void updateTreeItemName(String fromName, TreeItem<Layer> treeItem) {
		System.out.println("Tree Item was updated " + fromName + " -> " + treeItem);
	}
}
