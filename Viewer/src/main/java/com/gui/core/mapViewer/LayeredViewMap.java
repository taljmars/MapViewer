package com.gui.core.mapViewer;

import com.gui.core.layers.AbstractLayer;
import com.gui.core.mapTree.LayeredViewTree;

import java.util.List;

public interface LayeredViewMap extends ViewMap {

    void setLayeredViewTree(LayeredViewTree layeredViewTree);

    void showLayer(AbstractLayer layer);

    void showLayers(List<AbstractLayer> layers);

    void hideLayer(AbstractLayer layer);

    void hideLayers(List<AbstractLayer> layers);

    void removeLayers(List<AbstractLayer> layers);


    void layerEditorDone();

    void setEditedLayer(AbstractLayer layer);

    AbstractLayer unsetEditedLayer();

    boolean isEditing();
}
