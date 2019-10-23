package mrmathami.thegame.entity.bullet;

import mrmathami.thegame.Config;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

public final class NormalBullet extends AbstractBullet {
	public NormalBullet(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		super(createdTick, posX, posY, deltaX, deltaY, Config.NORMAL_BULLET_SPEED, Config.NORMAL_BULLET_STRENGTH, Config.NORMAL_BULLET_TTL);
	}
	public NormalBullet(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		super(createdTick, tower, enemy, Config.NORMAL_BULLET_SPEED, Config.NORMAL_BULLET_STRENGTH, Config.NORMAL_BULLET_TTL);
	}
}
