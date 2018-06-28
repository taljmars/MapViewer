package com.gui.core.layers;

public interface LayerWithNameAndPayload {

    void setName(String name);

    String getName();

    Object getPayload();

    void setPayload(Object payload);

    String getCaption();
}
