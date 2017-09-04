package com.gui.core.mapViewerObjects;

import java.awt.Point;

import com.gui.core.mapViewer.MapViewerSettings;
import com.gui.is.interfaces.mapObjects.MapMarker;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import com.geo_tools.Coordinate;
import com.geo_tools.GeoTools;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * A simple implementation of the {@link MapMarker} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 *
 * @author taljmars
 *
 */
public class MapMarkerCircle extends MapMarker {

	private double lat;
	private double lon;
	private Color color;
	private Circle sphere;
	private double radius;
	private String text;
    
	/**
	 * 
	 * @param text name
	 * @param iCoord position
	 * @param radius in meters
	 */
    public MapMarkerCircle(String text, Coordinate iCoord, double radius) {
		this(text, iCoord.getLat(), iCoord.getLon(), radius);
	}
    
    /**
     * 
     * @param iCoordinate position
     * @param radius in meters
     */
    public MapMarkerCircle(Coordinate iCoordinate, double radius) {
        this("", iCoordinate.getLat(), iCoordinate.getLon(), radius);
    }
    
    /**
     * 
     * @param lat
     * @param lon
     * @param radius in meters
     */
    public MapMarkerCircle(String text ,double lat, double lon, double radius) {
        this(text, lat, lon, radius, Color.TRANSPARENT);
    }
    
	public MapMarkerCircle(MapMarkerCircle mapMarkerCircle) {
		this.lat = mapMarkerCircle.lat;
		this.lon = mapMarkerCircle.lon;
		this.color = mapMarkerCircle.color;
		this.sphere = mapMarkerCircle.sphere;
		this.radius = mapMarkerCircle.radius;
		this.text = mapMarkerCircle.text;
	}

	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param radius in meters
	 * @param color
	 */
    private MapMarkerCircle(String text, double lat, double lon, double radius, Color color) {
        super();
        this.text = text;
        this.color = color;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;

        this.sphere = new Circle(radius);
        this.sphere.setStroke(Color.ORANGE);
        this.sphere.setStrokeWidth(1);
        this.sphere.setStrokeType(StrokeType.OUTSIDE);
        this.sphere.setFill(Color.TRANSPARENT);        
    }

	public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void Render(Group g, Point position, Double radius) {
		Text text = createText();
		Bounds bounds = getBound();
		this.sphere.setTranslateX(position.x - radius);
		this.sphere.setTranslateY(position.y - radius);
		this.sphere.setRadius(radius);
		text.setTranslateX(position.x - radius);
		text.setTranslateY(position.y - radius);

        //g.getChildren().add(this.sphere);
		StackPane layout = new StackPane();
		layout.getChildren().addAll(this.sphere, text);
		g.getChildren().addAll(layout);
    }

    @Override
    public String toString() {
        return "MapMarker at " + lat + " " + lon;
    }

	public void setColor(Color clr) {
		color = clr;
	}

	@Override
	public Coordinate getCoordinate() {
		return new Coordinate(lat, lon);
	}
	
	@Override
	public MapMarkerCircle clone() {
		return new MapMarkerCircle(this);
	}

	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public Color getColor() {
		return Color.ORANGE;
	}

	public boolean contains(Coordinate coord) {
		double dist = GeoTools.getDistance(coord, new Coordinate(lat, lon));
		if (dist < radius) 
			return true;
		
		return false;
	}
}
