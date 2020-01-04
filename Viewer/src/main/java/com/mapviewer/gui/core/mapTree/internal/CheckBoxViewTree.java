package com.mapviewer.gui.core.mapTree.internal;

import com.mapviewer.gui.core.layers.AbstractLayer;
import com.mapviewer.gui.core.layers.LayerGroup;
import com.mapviewer.gui.core.layers.LayerManager;
import com.mapviewer.gui.core.mapViewer.LayeredViewMap;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Arrays;

//@ComponentScan("com.gui.core.layers")
@Component
public class CheckBoxViewTree extends LayeredViewTreeImpl<CheckBoxTreeItem<AbstractLayer>> {

	@Autowired
	protected LayerManager layerManager;

	private LayeredViewMap layeredViewMap;

	LayerGroup generalGroup;

	public CheckBoxViewTree() {
		super();
		TreeCellEditorConvertor<AbstractLayer> convertor = new LayeredCheckBoxTreeCellEditorConvertor();
		setCellFactory(tree -> new CheckBoxTreeCellEditor2(convertor));
	}

	public CheckBoxViewTree(TreeCellEditorConvertor<AbstractLayer> convertor) {
		super();
		setCellFactory(tree -> new CheckBoxTreeCellEditor2(convertor));
	}

	private static int called;
	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void init() {
		Assert.isTrue(++called == 1, "Not a Singleton");

		LayerGroup root = new LayerGroup("Layers");
		generalGroup = new LayerGroup("General");
		root.addChildren(generalGroup);

		layerManager.setRoot(root);

		CheckBoxTreeItem<AbstractLayer> rootItem = loadTree(root);

		rootItem.setExpanded(true);

		setRoot(rootItem);

		
		System.out.println("Checkbox Tree is ready");
	}

	@Override
	public LayeredViewMap getLayeredViewMap() {
		return layeredViewMap;
	}

	@Autowired
	public void setLayeredViewMap(LayeredViewMap layeredViewMap) {
		this.layeredViewMap = layeredViewMap;
		this.layeredViewMap.setLayeredViewTree(this);
	}

	@Override
	protected void addSelectionHandler(CheckBoxTreeItem<AbstractLayer> cBox) {
		System.out.println("Adding selection handler");
		cBox.addEventHandler(CheckBoxTreeItem.<AbstractLayer>checkBoxSelectionChangedEvent(),
			(event) -> {
				CheckBoxTreeItem<AbstractLayer> cbItem = event.getTreeItem();
				if (!event.getTreeItem().equals(cBox))
					return;
				System.out.println("Event ! - " + event);
				if (!cbItem.isIndeterminate()) {
					if (cbItem.isSelected())
						getLayeredViewMap().showLayers(Arrays.asList(cbItem.getValue()));
					else
						getLayeredViewMap().hideLayers(Arrays.asList(cbItem.getValue()));
				}
			}
		);
	}

	@Override
	public void addTreeItemAction(TreeItem<AbstractLayer> newItem, TreeItem<AbstractLayer> parentOfNewItem) {
		super.addTreeItemAction(newItem, parentOfNewItem);
		addSelectionHandler((CheckBoxTreeItem<AbstractLayer>) newItem);
	}

	@Override
	public void editTreeItemAction(TreeItem<AbstractLayer> item) {
		((CheckBoxTreeItem<AbstractLayer>) item).setSelected(true);
		super.editTreeItemAction(item);
	}

	@Override
	public CheckBoxTreeItem<AbstractLayer> createTreeItem(AbstractLayer layer) {
		CheckBoxTreeItem item = new CheckBoxTreeItem(layer);
		if (layer instanceof LayerGroup)
			item.setExpanded(true);
		return item;
	}

}
