package mrmathami.thegame.entity;

import mrmathami.thegame.GameField;

import javax.annotation.Nonnull;

public interface UpdatableEntity extends GameEntity {
	UpdatableEntity onUpdate(@Nonnull GameField field);
}
