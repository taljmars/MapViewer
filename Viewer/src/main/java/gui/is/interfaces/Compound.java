package main.java.gui.is.interfaces;

import tools.geoTools.Coordinate;

public interface Compound {
	
	public boolean isContained(Coordinate position);
	
	public Coordinate getClosestPointOnEdge(Coordinate coord);
	
}
