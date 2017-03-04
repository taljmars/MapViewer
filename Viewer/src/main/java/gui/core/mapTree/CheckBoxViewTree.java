package main.java.gui.core.mapTree;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import main.java.gui.core.mapTree.internal.CheckBoxTreeCellEditor;
import main.java.gui.core.mapTree.internal.LayeredCheckBoxTreeCellEditorConvertor;
import main.java.gui.core.mapTree.internal.TreeCellEditorConvertor;
import main.java.gui.core.mapTreeObjects.Layer;
import main.java.gui.core.mapTreeObjects.LayerGroup;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;

@Component("tree")
public class CheckBoxViewTree extends LayeredViewTree<CheckBoxTreeItem<Layer>> {
	
	private LayerGroup generalGroup;
	
	public CheckBoxViewTree() {
		super(new CheckBoxTreeItem<Layer>());
		TreeCellEditorConvertor<Layer> convertor = new LayeredCheckBoxTreeCellEditorConvertor();
		setCellFactory(tree -> {return new CheckBoxTreeCellEditor<Layer>(convertor);});
	}
	
	private static int called;
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		if (called++ > 1)
			throw new RuntimeException("Not a Singletone");
		
		LayerGroup rootLayer = new LayerGroup("Layers");
		map.setRootLayer(rootLayer);
		CheckBoxTreeItem<Layer> rootItem = new CheckBoxTreeItem<Layer> (rootLayer);
		rootItem.setExpanded(true);
		
		generalGroup = new LayerGroup("General");
		rootLayer.addChildren(generalGroup);
        CheckBoxTreeItem<Layer> itemGeneral = new CheckBoxTreeItem<Layer> (generalGroup);
        
        rootItem.getChildren().addAll(itemGeneral);        
        setRoot(rootItem);
       
        addSelectionHandler(getRoot());
        
        System.out.println("Checkbox Tree is ready");
	}

	private void addSelectionHandler(TreeItem<Layer> cbox) {
		cbox.addEventHandler(CheckBoxTreeItem.<Layer>checkBoxSelectionChangedEvent(),
				(event) -> {
					CheckBoxTreeItem<Layer> cbItem = (CheckBoxTreeItem<Layer>) event.getTreeItem();
					if (!cbItem.isIndeterminate())
						map.setLayerVisibie(cbItem.getValue(), cbItem.isSelected());
				}
		);
	}

	public void addLayer(Layer layer) {
		CheckBoxTreeItem<Layer> ti = addTreeNode(layer, generalGroup);
		map.addLayer(layer, generalGroup);
		addSelectionHandler(ti);
	}

	public void removeLayer(Layer layer) {
		removeFromTreeGroup(layer);
		map.removeLayer(layer);
	}

	@Override
	public void handleTreeItemClick(TreeItem<Layer> treeItem) {
	}
}
