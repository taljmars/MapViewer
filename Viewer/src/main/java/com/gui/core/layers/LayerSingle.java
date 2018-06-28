package com.gui.core.layers;

import java.util.Vector;

import com.gui.core.mapViewer.LayeredViewMap;
import com.gui.is.interfaces.mapObjects.MapLine;
import com.gui.is.interfaces.mapObjects.MapMarker;
import com.gui.is.interfaces.mapObjects.MapPolygon;

public class LayerSingle extends AbstractLayer {
	
	private Vector<MapMarker> mapMarkers;
	private Vector<MapLine> mapLines;
	private Vector<MapPolygon> mapPolygons;
	private LayeredViewMap viewMap;
	
	public LayerSingle(String name, LayeredViewMap viewMap) {
		super(name);
		this.viewMap = viewMap;
		initLocals();
	}

	public LayerSingle(LayerSingle layer) {
		super(layer);
		this.viewMap = layer.viewMap;
		initLocals();

		for (MapMarker m : layer.mapMarkers) this.mapMarkers.addElement((MapMarker) m.cloneMe());
		for (MapLine m : layer.mapLines) this.mapLines.addElement((MapLine) m.cloneMe());
		for (MapPolygon m : layer.mapPolygons) this.mapPolygons.addElement((MapPolygon) m.cloneMe());
	}

	@Override
	public AbstractLayer cloneMe() {
		return new LayerSingle(this);
	}

	private void initLocals() {
		mapMarkers = new Vector<>();
		mapPolygons = new Vector<>();
		mapLines = new Vector<>();
	}
	
	protected Vector<MapPolygon> getMapPolygons() {
		return mapPolygons;
	}
		
	public void addMapMarker(MapMarker marker) {
		viewMap.addMapMarker(marker);
		mapMarkers.addElement(marker);
	}

	public void removeMapMarker(MapMarker marker) {
		viewMap.removeMapMarker(marker);
		mapMarkers.removeElement(marker);
	}
	
	protected void showAllMapMarkers() {
		for (MapMarker mapMarker : mapMarkers) {
			viewMap.addMapMarker(mapMarker);
		}
	}

	public void addMapPolygon(MapPolygon polygon) {
		viewMap.addMapPolygon(polygon);
		mapPolygons.addElement(polygon);
	}
	
	protected void showAllMapPolygons() {
		for (MapPolygon polygon : mapPolygons)
			viewMap.addMapPolygon(polygon);
	}

	public void addMapLine(MapLine line) {
		viewMap.addMapLine(line);
		mapLines.addElement(line);
	}
	
	protected void showAllMapLines() {
		for (MapLine mapLine : mapLines)
			viewMap.addMapLine(mapLine);
	}
	
	protected void removeAllMapLines() {
		hideAllMapLines();
		mapLines.removeAllElements();
	}
	
	protected void hideAllMapLines() {
		for (MapLine mapLine : mapLines)
			viewMap.removeMapLine(mapLine);
	}

	protected void removeAllMapMarkers() {
		hideAllMapMarkers();
		mapMarkers.removeAllElements();
	}
	
	protected void hideAllMapMarkers() {
		for (MapMarker mapMarker : mapMarkers)
			viewMap.removeMapMarker(mapMarker);
	}
	
	protected void removeAllMapPolygons() {
		hideAllMapPolygons();
		mapPolygons.removeAllElements();
	}
	
	protected void hideAllMapPolygons() {
		for (MapPolygon polygon : mapPolygons)
			viewMap.removeMapPolygon(polygon);
	}

	@Override
	public void hideAllMapObjects() {
		hideAllMapLines();
		hideAllMapMarkers();
		hideAllMapPolygons();
	}

	@Override
	public void showAllMapObjects() {
		showAllMapLines();
		showAllMapMarkers();
		showAllMapPolygons();
	}

	@Override
	public void regenerateMapObjects() {
		hideAllMapObjects();
		showAllMapObjects();
	}

	@Override
	public void removeAllMapObjects() {
		System.err.println("Remove all map object");
		removeAllMapLines();
		removeAllMapMarkers();
		removeAllMapPolygons();
	}

	@Override
	public String getCaption() {
		return "";
	}
}
