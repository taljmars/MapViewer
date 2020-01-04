package com.mapviewer.gui.core.mapViewer.internal;

import com.mapviewer.gui.core.mapTree.LayeredViewTree;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LayerEditorView extends Pane implements Initializable {

    @Autowired
    public LayeredViewTree layeredViewTree;

    @Autowired
    public LayeredViewMapImpl layeredViewMap;

    @FXML private Button btnEditDoneMode;
    @FXML private Pane root;

    private static int called;

    @PostConstruct
    private void init() {
        Assert.isTrue(++called == 1, "Not a Singleton");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TALMA Layer Editor Box created");
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        layeredViewMap.getIsMapLayerEditing().addListener((observable, oldValue, newValue) -> {
            System.out.println("TALMA -> Layer Editor box should be shown ?" + newValue);
            root.setVisible(newValue);
        });
    }

    @FXML
    public void layerEditorDone(MouseEvent mouseEvent) {
        layeredViewTree.getTree().refresh();
        layeredViewTree.stopEditing();
        layeredViewMap.layerEditorDone();
    }
}
