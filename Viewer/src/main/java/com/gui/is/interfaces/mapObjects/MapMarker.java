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
package com.gui.is.interfaces.mapObjects;

import java.awt.Point;

import com.gui.core.mapViewer.internal.MapViewerSettings;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import com.geo_tools.Coordinate;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public abstract class MapMarker implements MapObject {

    public abstract double getLat();

    public abstract double getLon();
    
    public abstract Coordinate getCoordinate();

    public abstract void Render(Group g, Point position, Double integer);

	public abstract double getRadius();

	public abstract String getText();

    public abstract Color getColor();

    public abstract MapMarker cloneMe();

    protected Text createText() {
        Text text = new Text(getText());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(getColor());
        text.setStyle(
                "-fx-font-family: \"Times New Roman\";" +
                        "-fx-font-style: bold;" +
                        "-fx-stroke: black;" +
                        "-fx-stroke-width: 0.3;" +
                        "-fx-font-size: " + MapViewerSettings.getMarkersFontSize() + "px;"
        );

        return text;
    }

    protected Bounds getBound() {
        Text text = createText();
        new Scene(new Group());
        text.applyCss();
        return text.getLayoutBounds();
    }
}
