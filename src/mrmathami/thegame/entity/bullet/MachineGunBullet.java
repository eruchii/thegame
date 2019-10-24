package mrmathami.thegame.entity.bullet;

import mrmathami.thegame.Config;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

public final class MachineGunBullet extends AbstractBullet {
	public MachineGunBullet(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		super(createdTick, posX, posY, deltaX, deltaY, Config.MACHINE_GUN_BULLET_SPEED, Config.MACHINE_GUN_BULLET_STRENGTH, Config.MACHINE_GUN_BULLET_TTL);
	}
	public MachineGunBullet(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		super(createdTick, tower, enemy, Config.MACHINE_GUN_BULLET_SPEED, Config.MACHINE_GUN_BULLET_STRENGTH, Config.MACHINE_GUN_BULLET_TTL);
	}
}
