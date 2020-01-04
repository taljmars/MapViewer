package com.mapviewer.gui.core.mapViewer;

import com.mapviewer.gui.is.interfaces.mapObjects.MapLine;
import com.mapviewer.gui.is.interfaces.mapObjects.MapMarker;
import com.mapviewer.gui.is.interfaces.mapObjects.MapObject;
import com.mapviewer.gui.is.interfaces.mapObjects.MapPolygon;

import java.util.Map;

public interface ViewMap {

    void addMapMarker(MapMarker marker);

    void removeMapMarker(MapMarker marker);

    void addMapLine(MapLine line);

    void removeMapLine(MapLine line);

    void addMapPolygon(MapPolygon polygon);

    void removeMapPolygon(MapPolygon polygon);

    Map<Integer, MapObject> getMapObjectMap();

    void flush();
}
