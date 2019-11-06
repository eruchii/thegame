package mrmathami.thegame.entity.tile.tower;

import mrmathami.thegame.Config;
import mrmathami.thegame.entity.bullet.MachineGunBullet;
import mrmathami.thegame.entity.enemy.AbstractEnemy;

import javax.annotation.Nonnull;

public final class MachineGunTower extends AbstractTower<MachineGunBullet> {
	public MachineGunTower(long createdTick, long posX, long posY) {
		super(createdTick, posX, posY, Config.MACHINE_GUN_TOWER_RANGE, Config.MACHINE_GUN_TOWER_SPEED);
		super.setNumTarget(Config.MACHINE_GUN_BULLET_COUNT);
	}

	@Nonnull
	@Override
	protected final MachineGunBullet doSpawn(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		return new MachineGunBullet(createdTick, posX, posY, deltaX, deltaY);
	}
	@Nonnull
	@Override
	protected final MachineGunBullet doSpawn(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		return new MachineGunBullet(createdTick, tower, enemy);
	}
}
