package com.mapviewer.gui.core.mapViewerObjects;

import java.awt.Point;

import com.mapviewer.gui.is.interfaces.mapObjects.MapMarker;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import com.geo_tools.Coordinate;
import javafx.scene.text.Text;

/**
 * A simple implementation of the {@link MapMarker} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 *
 * @author Jan Peter Stotz
 *
 */
@SuppressWarnings("deprecation")
public class MapMarkerDot extends MapMarker {

	private final static int DEF_RADIUS = 10;
	
	double lat;
	double lon;
	Color color;
	Circle sphere;
	int radius;
	String text;
	ImageView imageView;
	Double rotate;

	public MapMarkerDot(String text, Color color, Coordinate coordinate) {
		this(text, DEF_RADIUS, color, coordinate.getLat(), coordinate.getLon());
	}

	public MapMarkerDot(ImageView imageView, Double rotate, Coordinate coordinate) {
		this("", DEF_RADIUS, null, coordinate.getLat(), coordinate.getLon());
		this.imageView = imageView;
		this.rotate = rotate;
	}
	
	public MapMarkerDot(String text, Coordinate coordinate) {
		this(text, coordinate.getLat(), coordinate.getLon());
	}
	
	public MapMarkerDot(MapMarkerDot mapMarkerDot) {
		this.lat = mapMarkerDot.lat;
		this.lon = mapMarkerDot.lon;
		this.color = mapMarkerDot.color;
		this.sphere = mapMarkerDot.sphere;
		this.radius = mapMarkerDot.radius;
		this.text = mapMarkerDot.text;
		this.imageView = mapMarkerDot.imageView;
		this.rotate = mapMarkerDot.rotate;
	}
	
	public MapMarkerDot(String text, double lat, double lon) {
		this(text, DEF_RADIUS, Color.YELLOW, lat, lon);
	}

	private MapMarkerDot(String text, int radius, Color color, double lat, double lon) {
		this(text, radius, color, lat, lon, null, null);
	}

	private MapMarkerDot(String text, int radius, Color color, double lat, double lon, ImageView imageView, Double rotate) {
		super();
		this.color = color;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
		this.text = text;
		this.imageView = imageView;
		this.rotate = rotate;

		if (!this.text.isEmpty()) {
			Bounds bounds = getBound();
			this.radius = (int) Math.ceil((Math.max(bounds.getHeight(), bounds.getWidth()) / 2) * 1.8);
		}

		if (imageView != null)
			return;

		this.sphere = new Circle(this.radius, this.radius, this.radius);
//		this.sphere.cacheProperty().setValue(true);

		RadialGradient rgrad = new RadialGradient(0,0,
				sphere.getCenterX() - sphere.getRadius() / 3,
				sphere.getCenterY() - sphere.getRadius() / 3,
				sphere.getRadius(),
				false,
				null,
				new Stop(0.0, color),
				new Stop(1.0, Color.BLACK)
				);
		
		this.sphere.setFill(rgrad);		
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getText() {
		return text;
	}

	public void Render(Group g, Point position, Double radius) {
		StackPane layout = new StackPane();
		this.sphere.setTranslateX(position.x - this.radius);
		this.sphere.setTranslateY(position.y - this.radius);
		if (imageView != null) {
			this.imageView.setTranslateX(position.x - this.imageView.getFitWidth()/2);
			this.imageView.setTranslateY(position.y - this.imageView.getFitHeight()/2);
			this.imageView.setRotate(this.rotate);
			layout.getChildren().addAll(this.imageView);
		}
		else {
			layout.getChildren().addAll(this.sphere);
		}
		if (!text.isEmpty()) {
			Text text = createText();
			text.setTranslateX(position.x - this.radius);
			text.setTranslateY(position.y - this.radius);
			layout.getChildren().addAll(text);
		}
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
	public MapMarkerDot cloneMe() {
		return new MapMarkerDot(this);
	}

	public Color getColor() {
		return color;
	}

	@Override
	public double getRadius() {
		return radius;
	}
}
