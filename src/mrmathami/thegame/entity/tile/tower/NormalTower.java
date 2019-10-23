package mrmathami.thegame.entity.tile.tower;

import mrmathami.thegame.Config;
import mrmathami.thegame.entity.bullet.NormalBullet;
import mrmathami.thegame.entity.enemy.AbstractEnemy;

import javax.annotation.Nonnull;

public final class NormalTower extends AbstractTower<NormalBullet> {
	public NormalTower(long createdTick, long posX, long posY) {
		super(createdTick, posX, posY, Config.NORMAL_TOWER_RANGE, Config.NORMAL_TOWER_SPEED);
	}

	@Nonnull
	@Override
	protected final NormalBullet doSpawn(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		return new NormalBullet(createdTick, posX, posY, deltaX, deltaY);
	}
	@Nonnull
	@Override
	protected final NormalBullet doSpawn(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		return new NormalBullet(createdTick, tower, enemy);
	}
}
