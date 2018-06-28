package com.viewer_console;

import com.gui.core.mapTree.internal.LayeredViewTreeImpl;
import com.gui.core.mapViewer.LayeredViewMap;
import com.gui.core.mapViewer.internal.LayeredViewMapImpl;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MapView extends Pane implements ChangeListener<Number>, Initializable {

	private LayeredViewTreeImpl tree;
	private LayeredViewMapImpl map;

	public static SimpleIntegerProperty _WIDTH = new SimpleIntegerProperty(540);
	public static SimpleIntegerProperty _HEIGHT = new SimpleIntegerProperty(960);
	private static double lastPosition = 0.1;

	@FXML private SplitPane splitPane;
	@FXML private Pane left;
	@FXML private Pane right;
	
	private static int called;
	@PostConstruct
	private void init() {
		Assert.isTrue(++called == 1, "Not a Singleton");

		_WIDTH.addListener((observable, oldValue, newValue) -> {
			splitPane.setMaxWidth((int) newValue);
			splitPane.setMinWidth((int) newValue);
			precentage(lastPosition);
		});
		_HEIGHT.addListener((observable, oldValue, newValue) -> {
			splitPane.setMaxHeight((int) newValue);
			splitPane.setMinHeight((int) newValue);
			precentage(lastPosition);
		});
	}

	@Autowired
	public void setTree(LayeredViewTreeImpl tree) {
		this.tree = tree;
	}

	@Autowired
	public void setMap(LayeredViewMapImpl map) {
		this.map = map;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		splitPane.setDividerPosition(0,  lastPosition);
		left.getChildren().add(tree);
		right.getChildren().add(map);
		if (splitPane.getDividers().size() == 1)
			splitPane.getDividers().get(0).positionProperty().addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends Number> property, Number fromPrecentage, Number toPrecentage) {
		lastPosition = toPrecentage.doubleValue();
		precentage(lastPosition);
	}

	private void precentage(double toPrecentage) {
		map.setMapBounds(0, 0, (int) (splitPane.getWidth() - splitPane.getWidth() * toPrecentage), (int) splitPane.getHeight());
		tree.setTreeBound(0, 0, (int) (splitPane.getWidth() * toPrecentage), (int) splitPane.getHeight());
	}
}
