//==============================================================================
//   JFXMapPane is a Java library for parsing raw weather data
//   Copyright (C) 2012 Jeffrey L Smith
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//    
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//    
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//    
//  For more information, please email jsmith.carlsbad@gmail.com
//    
//==============================================================================
package main.java.gui.core.mapViewer;

import java.awt.Point;
import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import main.java.gui.core.mapTree.CheckBoxViewTree;
import main.java.gui.core.mapTreeObjects.Layer;
import main.java.gui.core.mapTreeObjects.LayerGroup;
import main.java.gui.core.mapViewerObjects.MapMarkerDot;
import main.java.gui.is.events.GuiEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author taljmars
 */
@ComponentScan("main.java.gui.core.mapTree")
@Component("map")
public class LayeredViewMap extends ViewMap {
	
	@Resource(name = "tree")
	private CheckBoxViewTree tree;
    
    protected Button btnEditSaveMode;
    protected Button btnEditCancelMode;
    
    private LayerGroup root;
    
    private Layer modifiedLayer;
    
    private VBox editorVbox;
    
    public LayeredViewMap() {
    	super();
    	initLayerEditor();
    	
    	System.out.println("In LayedViewMap Cont");
    }
    
    private void initLayerEditor() {
    	btnEditSaveMode = new Button("Save");
    	btnEditCancelMode = new Button("Cancel");
    	
    	btnEditSaveMode.setOnAction( e -> LayerEditorSave());
    	btnEditCancelMode.setOnAction( e -> LayerEditorCancel());
    	
    	editorVbox = new VBox();
    	editorVbox.setPadding(new Insets(5));
    	editorVbox.getChildren().add(btnEditSaveMode);
    	editorVbox.getChildren().add(btnEditCancelMode);
    	editorVbox.setLayoutX(50);
    	editorVbox.setLayoutY(10);
    	
    	editorVbox.setVisible(false);
    	
    	getChildren().add(editorVbox);
	}
    
    public void setRootLayer(LayerGroup rootLayer) {
    	setRoot(rootLayer);
	}	

	protected void startModifiedLayerMode(Layer modifiedLayer) {
    	this.modifiedLayer = modifiedLayer;
    	editorVbox.setVisible(true);
    }
    
    protected Layer finishModifiedLayerMode() {
    	editorVbox.setVisible(false);
    	Layer modifiedLayer = this.modifiedLayer;
    	this.modifiedLayer = null;
    	return modifiedLayer;
    }
    
    public void addLayer(Layer layer, LayerGroup parentGroup) {
		layer.setParent(parentGroup);
		parentGroup.addChildren(layer);
	}
    
    public void removeLayer(Layer layer) {
    	removeLayersRecursive(layer);
		
		if (layer.getParent() != null) {
			LayerGroup lg = (LayerGroup) layer.getParent();
			lg.removeChildren(layer);
		}
    }
    
    private void removeLayersRecursive(Layer layer) {
		if (layer instanceof LayerGroup) {
			LayerGroup lg = (LayerGroup) layer;
			Iterator<Layer> it = lg.getChildens().iterator();
			while (it.hasNext()) {
				removeLayersRecursive(it.next());
				it.remove();
			}
		}
		else
			layer.removeAllMapObjects();
	}
    
    public void setLayerVisibie(Layer layer, boolean show) {
    	if (show) {
    		System.err.println("Show show all elements of " + layer);
    		layer.regenerateMapObjects();
    	}
    	else {
    		System.err.println("Show hide all elements of " + layer);
    		layer.removeAllMapObjects();
    	}
    }
    
	public void hideLayer(Layer layer) {
		if (layer instanceof LayerGroup) {
			LayerGroup lg = (LayerGroup) layer;
			Iterator<Layer> it = lg.getChildens().iterator();
			while (it.hasNext()) {
				hideLayer(it.next());
			}
		}
		else
			layer.removeAllMapObjects();
	}
	
	public void showLayer(Layer layer) {
		if (layer instanceof LayerGroup) {
			LayerGroup lg = (LayerGroup) layer;
			Iterator<Layer> it = lg.getChildens().iterator();
			while (it.hasNext()) {
				showLayer(it.next());
			}
		}
		else
			layer.regenerateMapObjects();
	}
	
	private ContextMenu popup;
	
	private ContextMenu buildPopup(Point point) {
		ContextMenu popup = new ContextMenu();		
		
		MenuItem menuItemAddMarker = new MenuItem("Add Marker");
		popup.getItems().add(menuItemAddMarker);
		
		menuItemAddMarker.setOnAction( arg -> this.addMapMarker(new MapMarkerDot(getPosition(point))));

		return popup;
	}
	
	@Override
	protected void HandleMouseClick(MouseEvent me) {
		if (popup != null)
			popup.hide();
		
		if (!me.isPopupTrigger())
			return;
		
		Point point = new Point((int) me.getX(), (int) me.getY());
		popup = buildPopup(point);
		popup.show(this, me.getScreenX(), me.getScreenY());
	}
	
	@SuppressWarnings("unused")
	private void EditModeOff() {
		System.out.println("Edit mode is off");
	}

	private void EditModeOn() {
		System.out.println("Edit mode is on");
	}
	
	@EventListener
	public void onApplicationEvent(GuiEvent command) {
		switch (command.getCommand()) {
		case EDITMODE_EXISTING_LAYER_START:
			EditModeOn();
			Layer layer = (Layer) command.getSource();
			HandlEditModeForExistingLayer(layer);
			break;
		}
	}
	
	public void HandlEditModeForExistingLayer(Layer layer) {
		EditModeOn();
	}

	public void LayerEditorCancel() {
		tree.refresh();
	}

	public void LayerEditorSave() {
		tree.refresh();
	}



	/**
	 * @return the root
	 */
	public LayerGroup getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(LayerGroup root) {
		this.root = root;
	}

}
