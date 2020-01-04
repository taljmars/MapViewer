package com.mapviewer.gui.core.mapTree.internal;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.KeyCode;

public class CheckBoxTreeCellEditor<T> extends CheckBoxTreeCell<T> {
	 
	private TextField textField;
	private TreeCellEditorConvertor<T> convertor;
	
	public CheckBoxTreeCellEditor( TreeCellEditorConvertor<T> treeCellEditorConvertor ) {
		this.convertor = treeCellEditorConvertor;
	}


	private String firstText;

	@Override
	public void startEdit() {
		super.startEdit();
		firstText = super.getText();
		System.out.println("First text " + firstText);

		if (textField == null)
			createTextField();

		setText(null);
		setGraphic(textField);
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setText(((T) getItem()).toString());
	}

	@Override
	public void commitEdit(T item) {
		System.out.println("From " + firstText + " to " + item.toString());
		super.commitEdit(item);
		((ViewTreeImpl<T>) getTreeView()).updateTreeItemName(firstText, getTreeItem());
		setStyle("-fx-background-color: red;");
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			ObservableValue<Boolean> selectedState = getSelectedStateCallback().call(this.getTreeItem());
			if (selectedState != null) {
				System.out.println("SEL");
			}

			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);			   
			} 
			else {
				setText(getString());
				
				ContextMenu menu = ((ViewTreeImpl<T>) getTreeView()).getPopupMenu(getTreeItem());
				setContextMenu(menu);
			}
		}		
	}
	
	private void createTextField() {
		textField = new TextField(getString());
		textField.setOnKeyReleased( t -> {
			if (t.getCode() == KeyCode.ENTER) {
				commitEdit(convertor.fromString(getTreeItem(), textField.getText()));
			} else if (t.getCode() == KeyCode.ESCAPE) {
				cancelEdit();
			}
		});  
		
	}

	private String getString() {
		return getItem() == null ? "Null" : getItem().toString();
	}
}