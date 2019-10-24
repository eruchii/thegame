package mrmathami.thegame.entity.bullet;

import mrmathami.thegame.Config;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

public final class SniperBullet extends AbstractBullet {
	public SniperBullet(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		super(createdTick, posX, posY, deltaX, deltaY, Config.SNIPER_BULLET_SPEED, Config.SNIPER_BULLET_STRENGTH, Config.SNIPER_BULLET_TTL);
	}
	public SniperBullet(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		super(createdTick, tower, enemy, Config.SNIPER_BULLET_SPEED, Config.SNIPER_BULLET_STRENGTH, Config.SNIPER_BULLET_TTL);
	}
}
