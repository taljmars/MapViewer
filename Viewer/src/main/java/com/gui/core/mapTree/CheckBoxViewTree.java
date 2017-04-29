package com.gui.core.mapTree;

import javax.annotation.PostConstruct;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import org.springframework.stereotype.Component;

import com.gui.core.mapTree.internal.CheckBoxTreeCellEditor;
import com.gui.core.mapTree.internal.LayeredCheckBoxTreeCellEditorConvertor;
import com.gui.core.mapTree.internal.TreeCellEditorConvertor;
import com.gui.core.mapTreeObjects.Layer;
import com.gui.core.mapTreeObjects.LayerGroup;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;

@Component
public class CheckBoxViewTree extends LayeredViewTree<CheckBoxTreeItem<Layer>>{
	
	private LayerGroup generalGroup;

	public CheckBoxViewTree() {
		super();
		TreeCellEditorConvertor<Layer> convertor = new LayeredCheckBoxTreeCellEditorConvertor();
		setCellFactory(tree -> {return new CheckBoxTreeCellEditor<>(convertor);});
	}
	
	private static int called;
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		if (called++ > 1)
			throw new RuntimeException("Not a Singleton");
		
		LayerGroup rootLayer = new LayerGroup("Layers");
		getLayeredViewMap().setRootLayer(rootLayer);
		CheckBoxTreeItem<Layer> rootItem =  createTreeItem(rootLayer);
		rootItem.setExpanded(true);
		rootItem.setSelected(true);
		
		generalGroup = new LayerGroup("General");
		rootLayer.addChildren(generalGroup);
        CheckBoxTreeItem<Layer> itemGeneral = createTreeItem(generalGroup);
		addSelectionHandler(itemGeneral);
        
        rootItem.getChildren().addAll(itemGeneral);        
        setRoot(rootItem);
       
        addSelectionHandler((CheckBoxTreeItem<Layer>) getRoot());
        
        System.out.println("Checkbox Tree is ready");
	}

	private void addSelectionHandler(CheckBoxTreeItem<Layer> cbox) {
		cbox.addEventHandler(CheckBoxTreeItem.<Layer>checkBoxSelectionChangedEvent(),
			(event) -> {
				System.out.println("TALMA");
				CheckBoxTreeItem<Layer> cbItem = (CheckBoxTreeItem<Layer>) event.getTreeItem();
				if (!cbItem.isIndeterminate())
					getLayeredViewMap().setLayerVisibie(cbItem.getValue(), cbItem.isSelected());
			}
		);
	}

	public void addLayer(Layer layer) {
		CheckBoxTreeItem<Layer> ti = addTreeNode(layer, generalGroup);
		getLayeredViewMap().addLayer(layer, generalGroup);
		addSelectionHandler(ti);
	}

	public void removeLayer(Layer layer) {
		removeFromTreeGroup(layer);
		getLayeredViewMap().removeLayer(layer);
	}

	@Override
	public void handleTreeItemClick(TreeItem<Layer> treeItem) {
	}

	public void handleTreeItemMark(CheckBoxTreeItem<Layer> treeItem, Boolean aBoolean) {
		System.out.println("\n\nvalue " + treeItem.getValue().getName() + " is marked: " + aBoolean);

		((CheckBoxTreeItem)treeItem).selectedProperty().setValue(aBoolean);
		ObservableList<TreeItem<Layer>> lst = treeItem.<CheckBoxTreeItem>getChildren();
		for (TreeItem<Layer> child : lst) {
			CheckBoxTreeItem<Layer> item = (CheckBoxTreeItem<Layer>) child;
			handleTreeItemMark(item, aBoolean);
			System.out.println("try to update " + item.getValue().getName());
		}

		refresh();
	}

	@Override
	protected CheckBoxTreeItem<Layer> createTreeItem(Layer layer) {
		return new CheckBoxTreeItem<>(layer);
	}
}
