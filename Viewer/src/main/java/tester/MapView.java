package main.java.tester;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.java.gui.core.mapTree.CheckBoxViewTree;
import main.java.gui.core.mapViewer.LayeredViewMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

@Component("MapView")
public class MapView extends Pane implements ChangeListener<Number>, Initializable {

	@Autowired
	private CheckBoxViewTree tree;
	
	@Autowired
	private LayeredViewMap map;
	
	@FXML private SplitPane splitPane;
	@FXML private Pane left;
	@FXML private Pane right;
	
	private static int called;
	@PostConstruct
	private void init() {
		if (called++ > 1)
			throw new RuntimeException("Not a Singletone");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		left.getChildren().add(tree);
		right.getChildren().add(map);
		if (splitPane.getDividers().size() == 1)
			splitPane.getDividers().get(0).positionProperty().addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends Number> property, Number fromPrecentage, Number toPrecentage) {
		map.setMapBounds(0, 0, (int) (splitPane.getWidth() - splitPane.getWidth() * toPrecentage.doubleValue()), (int) splitPane.getHeight());
		tree.setTreeBound(0, 0, (int) (splitPane.getWidth() * toPrecentage.doubleValue()), (int) splitPane.getHeight());
	}
}
