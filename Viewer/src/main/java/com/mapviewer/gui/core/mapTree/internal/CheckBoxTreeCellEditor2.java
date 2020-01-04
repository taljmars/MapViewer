package com.mapviewer.gui.core.mapTree.internal;

import com.mapviewer.gui.core.layers.AbstractLayer;
import com.mapviewer.gui.core.layers.LayerWithNameAndPayload;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CheckBoxTreeCellEditor2 extends CheckBoxTreeCell<AbstractLayer> {

	private TextField textField;

	private Node originalGraphic;

	private TreeCellEditorConvertor convertor;

	public CheckBoxTreeCellEditor2(TreeCellEditorConvertor treeCellEditorConvertor ) {
		this.convertor = treeCellEditorConvertor;
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null)
			createTextField();

		backupItem();
		System.out.println("First text " + textBeforeEdit);

		setText(null);
		setGraphic(textField);
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		restoreItem();
	}

	@Override
	public void commitEdit(AbstractLayer item) {
		System.out.println("Layer Name changed from '" + textBeforeEdit + "' to '" + item.toString() + "'");
		super.commitEdit(item);
		item.setWasEdited(true);

		if (item.isWasEdited()) {
			updateItemAsModified(item);
		}

		setText(item.toString());

		((ViewTreeImpl<AbstractLayer>) getTreeView()).updateTreeItemName(textBeforeEdit, getTreeItem());

		dropItemBackup();
	}

	@Override
	public void updateItem(AbstractLayer item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			// Same logic as parent

		}
		else {
			if (getGraphic() instanceof HBox)
				originalGraphic = ((HBox) getGraphic()).getChildren().get(0);
			else
				originalGraphic = getGraphic();

			if (item.isWasEdited()) {
				updateItemAsModified(item);
			}
			else {
				Node graphic;
				Text caption = null;
				if (item instanceof LayerWithNameAndPayload) {
					String captionText = item.getCaption();
					if (captionText != null && !captionText.isEmpty()) {
						caption = new Text("[" + captionText + "]");
					}
				}
				if (caption != null)
					graphic = new HBox(4, originalGraphic, caption);
				else
					graphic = new HBox(4, originalGraphic);
				setGraphic(graphic);
			}

			if (isEditing()) {
				// Nothing at the moment
			}
			else {
				ContextMenu menu = ((ViewTreeImpl<AbstractLayer>) getTreeView()).getPopupMenu(getTreeItem());
				setContextMenu(menu);
			}
		}
	}

	private void updateItemAsModified(AbstractLayer item) {
		Image delayed_img = new Image(this.getClass().getResource("/com/mapviewer/treeImages/pencil.png").toString());
		ImageView imgView = new ImageView();
		imgView.setImage( delayed_img );
		imgView.setFitWidth(12);
		imgView.setFitHeight(12);

		Node graphic;
		Text caption = null;
		if (item instanceof LayerWithNameAndPayload) {
			String captionText = item.getCaption();
			if (captionText != null && !captionText.isEmpty()) {
				caption = new Text("[" + captionText + "]");
			}
		}
		if (caption != null)
			graphic = new HBox(4, originalGraphic, imgView,caption);
		else
			graphic = new HBox(4, originalGraphic, imgView);
		setGraphic(graphic);
	}

	boolean modified = false;
	private void createTextField() {
		textField = new TextField(getString());
		textField.setOnKeyReleased( t -> {
			if (t.getCode() == KeyCode.ENTER) {
				modified = true;
				commitEdit((AbstractLayer) convertor.fromString(getTreeItem(), textField.getText()));
			} else if (t.getCode() == KeyCode.ESCAPE) {
				modified = false;
				cancelEdit();
			}
		});
	}

	Node graphicBeforeEdit = null;
	String styleBeforeEdit = null;
	String textBeforeEdit = null;

	private void backupItem() {
		graphicBeforeEdit = getGraphic();
		styleBeforeEdit = getStyle();
		textBeforeEdit = getText();
	}

	private void dropItemBackup() {
		graphicBeforeEdit = null;
		styleBeforeEdit = null;
		textBeforeEdit = null;

		textField = null;
		modified = false;
	}

	private void restoreItem() {
		setGraphic(graphicBeforeEdit);
		setStyle(styleBeforeEdit);
		setText(textBeforeEdit);

		dropItemBackup();
	}

	private String getString() {
		return getItem() == null ? "Null" : getItem().toString();
	}
}