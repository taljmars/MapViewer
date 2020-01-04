package com.mapviewer.gui.core.layers;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractLayer implements LayerWithNameAndPayload {

	private UUID randomId;
	private AbstractLayer parent;
	private String name;
	private Object payload;
	private boolean wasEdited = false;
	
	public AbstractLayer(String name) {
		this.randomId = UUID.randomUUID();
		this.name = name;
	}
	
	public AbstractLayer(AbstractLayer layer) {
		this(layer.getName());
		this.parent = layer.parent;
		this.payload = layer.payload;
	}

	public UUID getUuid() {
		return randomId;
	}

	public void setUuid(UUID uuid) {
		this.randomId = uuid;
	}

	public AbstractLayer getParent() {
		return parent;
	}

	public void setParent(AbstractLayer parent) {
		this.parent = parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getPayload() {
		return payload;
	}

	@Override
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public boolean isWasEdited() {
		return wasEdited;
	}

	public void setWasEdited(boolean wasEdited) {
		this.wasEdited = wasEdited;
	}

	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractLayer layer = (AbstractLayer) o;
		return Objects.equals(randomId, layer.randomId) &&
				Objects.equals(parent, layer.parent) &&
				Objects.equals(name, layer.name) &&
				Objects.equals(payload, layer.payload);
	}

	@Override
	public int hashCode() {

		return Objects.hash(randomId, parent, name, payload);
	}

	/**
	 * This function will be called during layer editing.
	 * The cloned layer will be the one the is actually being edited.
	 * During "save" action the cloned layer replace the original one and the original one is being removed.
	 * During "cancel" the cloned layer is simply being deleted (including all it map object)
	 *
	 * @return the cloned layer
	 */
	public abstract AbstractLayer cloneMe();

	public abstract void hideAllMapObjects();

	public abstract void showAllMapObjects();

	public abstract void regenerateMapObjects();

	public abstract void removeAllMapObjects();

	public void loadMapObjects() {
		regenerateMapObjects();
		hideAllMapObjects();
	}
}
