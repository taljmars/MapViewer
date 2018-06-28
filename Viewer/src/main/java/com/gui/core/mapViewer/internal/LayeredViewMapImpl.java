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
package com.gui.core.mapViewer.internal;

import com.gui.core.layers.AbstractLayer;
import com.gui.core.layers.LayerGroup;
import com.gui.core.layers.LayerManager;
import com.gui.core.layers.LayerSingle;
import com.gui.core.mapTree.LayeredViewTree;
import com.gui.core.mapViewer.LayeredViewMap;
import com.gui.core.mapViewerObjects.MapLineImpl;
import com.gui.core.mapViewerObjects.MapMarkerCircle;
import com.gui.core.mapViewerObjects.MapMarkerDot;
import com.gui.is.interfaces.mapObjects.MapLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.List;

/**
 *
 * @author taljmars
 */
//@ComponentScan("com.gui.core.mapTree.internal")
@Component
public class LayeredViewMapImpl extends ViewMapImpl implements LayeredViewMap
{
    @Autowired
    private LayerManager layerManager;

    private LayeredViewTree layeredViewTree;
//    private AbstractLayer modifiedLayer;
    private BooleanProperty isMapLayerEditing;

    private List<AbstractLayer> currentVisibleLayers;
    private List<AbstractLayer> currentHiddenLayers;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public LayeredViewMapImpl() {
        super();
        isMapLayerEditing = new SimpleBooleanProperty( false);
        System.out.println("In LayeredViewMap Constructor");
    }

    private static int called;
    @PostConstruct
    protected void init() {
        Assert.isTrue(++called == 1, "Not a Singleton");
    }

    @Override
    public void setLayeredViewTree(LayeredViewTree layeredViewTree) {
        this.layeredViewTree = layeredViewTree;
    }

    @Override
    public void showLayer(AbstractLayer layer) {
        System.out.println("Going to show: " + layer);
        if (layer instanceof LayerGroup) {
            LayerGroup lg = (LayerGroup) layer;
            showLayers(lg.getChildren());
        } else
            layer.showAllMapObjects();
    }

    @Override
    public void showLayers(List<AbstractLayer> layers) {
        for (AbstractLayer layer : layers) {
            System.out.println("Going to show: " + layer);
            showLayer(layer);
        }
    }

    @Override
    public void hideLayer(AbstractLayer layer) {
        System.out.println("Going to hide: " + layer);
        if (layer instanceof LayerGroup) {
            LayerGroup lg = (LayerGroup) layer;
            hideLayers(lg.getChildren());
        }
        else
            layer.hideAllMapObjects();
    }

    @Override
    public void hideLayers(List<AbstractLayer> layers) {
        for (AbstractLayer layer : layers) {
            hideLayer(layer);
        }
    }

    @Override
    public void removeLayers(List<AbstractLayer> layers) {
        for (AbstractLayer layer : layers) {
            System.out.println("Going to hide: " + layer);
            if (layer instanceof LayerGroup) {
                LayerGroup lg = (LayerGroup) layer;
                removeLayers(lg.getChildren());
            }
            else
                layer.removeAllMapObjects();
        }
    }

    @Override
    public void setEditedLayer(AbstractLayer layer) {
        isMapLayerEditing.setValue(true);
    }

    @Override
    public AbstractLayer unsetEditedLayer() {
        isMapLayerEditing.setValue(false);
        return null;
    }

    @Override
    public boolean isEditing() {
        return isMapLayerEditing.get();
    }
    
    private ContextMenu popup;
    private boolean buildLine = false;
    private MapLine tempMapLine = null;
    
    private ContextMenu buildPopup(Point point) {
        ContextMenu popup = new ContextMenu();

        if (buildLine) {
            MenuItem menuItemAddPoint = new MenuItem("Add Point");
            menuItemAddPoint.setOnAction(arg -> {
                this.removeMapLine(tempMapLine);
                tempMapLine.addCoordinate(getPosition(point));
                this.addMapLine(tempMapLine);
            });
            popup.getItems().add(menuItemAddPoint);

            MenuItem menuItemDone = new MenuItem("Done");
            menuItemDone.setOnAction( arg -> {
                buildLine = false;
                tempMapLine = null;
            });
            popup.getItems().add(menuItemDone);

            return popup;
        }

        if (isEditing()) {
            MenuItem menuItemAddMarker = new MenuItem("Add Marker");
            popup.getItems().add(menuItemAddMarker);

            MenuItem menuItemAddMarkerImage = new MenuItem("Add Marker with Image");
            popup.getItems().add(menuItemAddMarkerImage);

            MenuItem menuItemAddCircle = new MenuItem("Add Circle");
            popup.getItems().add(menuItemAddCircle);

            MenuItem menuItemAddLine = new MenuItem("Add Line");
            popup.getItems().add(menuItemAddLine);

            MenuItem menuItemAddPolygon = new MenuItem("Add Polygon");
            popup.getItems().add(menuItemAddPolygon);


            Image img = new Image(this.getClass().getResource("/com/mapImages/droneConnected.png").toString());
            javafx.scene.image.ImageView iView = new javafx.scene.image.ImageView(img);
            iView.setFitHeight(40);
            iView.setFitWidth(40);

            menuItemAddMarker.setOnAction(arg -> {
                AbstractLayer modifiedLayer = layeredViewTree.getModifiedLayer();
                ((LayerSingle) modifiedLayer).addMapMarker(new MapMarkerDot("12", getPosition(point)));
                modifiedLayer.regenerateMapObjects();
            });
            menuItemAddMarkerImage.setOnAction(arg -> {
                AbstractLayer modifiedLayer = layeredViewTree.getModifiedLayer();
                ((LayerSingle) modifiedLayer).addMapMarker(new MapMarkerDot(iView, 45.0, getPosition(point)));
                modifiedLayer.regenerateMapObjects();
            });
            menuItemAddCircle.setOnAction(arg -> {
                AbstractLayer modifiedLayer = layeredViewTree.getModifiedLayer();
                ((LayerSingle) modifiedLayer).addMapMarker(new MapMarkerCircle("12", getPosition(point), 50000));
                modifiedLayer.regenerateMapObjects();
            });
            menuItemAddLine.setOnAction(arg -> {
                tempMapLine = new MapLineImpl();
                AbstractLayer modifiedLayer = layeredViewTree.getModifiedLayer();
                ((LayerSingle) modifiedLayer).addMapLine(tempMapLine);
                buildLine = true;
            });
        }

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
    protected void EditModeOff() {
        isMapLayerEditing.set(false);
        System.out.println("Edit mode is off");
    }

    protected void EditModeOn() {
        isMapLayerEditing.set(true);
        System.out.println("Edit mode is on");
    }

    public boolean isEditingLayer() {
        return isMapLayerEditing.get();
    }

    @Override
    public void layerEditorDone() {
        unsetEditedLayer();
    }

    public BooleanProperty getIsMapLayerEditing() {
        return isMapLayerEditing;
    }
}
