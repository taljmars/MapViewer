package com.gui.core.layers;

import java.util.Vector;

public class LayerGroup extends AbstractLayer {

	private Vector<AbstractLayer> children;
	
	public LayerGroup(String name) {
		super(name);
		children = new Vector<>();
	}

	public LayerGroup(LayerGroup layerGroup) {
		super(layerGroup);
		children = new Vector<>();
		for (AbstractLayer layer : layerGroup.getChildren())
			children.add(layer.cloneMe());
	}

	@Override
	public AbstractLayer cloneMe() {
		return new LayerGroup(this);
	}

	public void unlink() {
		if (this.getParent() != null) {
			((LayerGroup) this.getParent()).removeChildren(this);

		}

		for (AbstractLayer layer : children) {
			((LayerGroup) this.getParent()).addChildren(layer);
		}

		this.setParent(null);
	}

	public void unlinkGroup() {
		if (this.getParent() != null) {
			((LayerGroup) this.getParent()).removeChildren(this);

		}

		this.setParent(null);
	}

	public void addChildren(AbstractLayer layer) {
		layer.setParent(this);
		this.children.addElement(layer);
	}
	
	public Vector<AbstractLayer> getChildren() {
		return children;
	}

	public void removeChildren(AbstractLayer layer) {
		children.remove(layer);
		layer.setParent(null);
	}

	@Override
	public void hideAllMapObjects() {
		for (AbstractLayer child : children)
			child.hideAllMapObjects();
	}

	@Override
	public void showAllMapObjects() {
		for (AbstractLayer child : children)
			child.showAllMapObjects();
	}

	@Override
	public void regenerateMapObjects() {
		for (AbstractLayer child : children)
			child.regenerateMapObjects();
	}

	@Override
	public void removeAllMapObjects() {
		for (AbstractLayer child : children)
			child.removeAllMapObjects();
	}

	@Override
	public String getCaption() {
		return "";
	}
}
