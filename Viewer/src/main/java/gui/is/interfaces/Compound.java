package gui.is.interfaces;

import geoTools.Coordinate;

public interface Compound {
	
	public boolean isContained(Coordinate position);
	
	public Coordinate getClosestPointOnEdge(Coordinate coord);
	
}
