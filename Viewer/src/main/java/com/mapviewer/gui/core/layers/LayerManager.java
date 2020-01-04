package com.mapviewer.gui.core.layers;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.UUID;

@Component
public class LayerManager {

    private LayerGroup root;

    public AbstractLayer getLayerByName(String name) {
        return getLayerByValue(name, Comparator.comparing(AbstractLayer::getName));
    }

    public AbstractLayer getLayerById(UUID uuid) {
        return getLayerByValue(uuid, Comparator.comparing(AbstractLayer::getUuid));
    }

    public AbstractLayer getLayerByValue(Object objectToSearch, Comparator comparator) {
        return getLayerByValue(getRoot(), objectToSearch, comparator);
    }

    private AbstractLayer getLayerByValue(AbstractLayer startRoot, Object obj, Comparator comparator) {
        if (startRoot instanceof LayerGroup) {
            for (AbstractLayer layer : ((LayerGroup) startRoot).getChildren()) {
                AbstractLayer res = getLayerByValue(layer, obj, comparator);
                if (res != null)
                    return res;
            }
            return null;
        }
        if (comparator.compare(startRoot, obj) == 0)
            return startRoot;

        return null;
    }

    public LayerGroup getRoot() {
        return root;
    }

    public void setRoot(LayerGroup layerGroup) {
        root = layerGroup;
    }

    public void flush() {
        root = null;
    }
}
