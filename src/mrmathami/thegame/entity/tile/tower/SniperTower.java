package mrmathami.thegame.entity.tile.tower;

import mrmathami.thegame.Config;
import mrmathami.thegame.entity.bullet.SniperBullet;
import mrmathami.thegame.entity.enemy.AbstractEnemy;

import javax.annotation.Nonnull;

public final class SniperTower extends AbstractTower<SniperBullet> {
	public SniperTower(long createdTick, long posX, long posY) {
		super(createdTick, posX, posY, Config.SNIPER_TOWER_RANGE, Config.SNIPER_TOWER_SPEED, Config.SNIPER_GUN_TOWER_COST);
		super.setNumTarget(Config.SNIPER_BULLET_COUNT);
	}

	@Nonnull
	@Override
	protected final SniperBullet doSpawn(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		return new SniperBullet(createdTick, posX, posY, deltaX, deltaY);
	}
	@Nonnull
	@Override
	protected final SniperBullet doSpawn(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		return new SniperBullet(createdTick, tower, enemy);
	}
}
