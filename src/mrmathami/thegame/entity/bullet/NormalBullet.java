package mrmathami.thegame.entity.bullet;

import mrmathami.thegame.Config;
import mrmathami.thegame.GameField;
import mrmathami.thegame.entity.LivingEntity;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

import javax.annotation.Nonnull;

public final class NormalBullet extends AbstractBullet {
	private final long time = 10;
	private final double eff = 0.5;
	public NormalBullet(long createdTick, double posX, double posY, double deltaX, double deltaY) {
		super(createdTick, posX, posY, deltaX, deltaY, Config.NORMAL_BULLET_SPEED, Config.NORMAL_BULLET_STRENGTH, Config.NORMAL_BULLET_TTL);
		super.effectOnHit(this.time, this.eff);
	}
	public NormalBullet(long createdTick, AbstractTower tower, AbstractEnemy enemy) {
		super(createdTick, tower, enemy, Config.NORMAL_BULLET_SPEED, Config.NORMAL_BULLET_STRENGTH, Config.NORMAL_BULLET_TTL);
		super.effectOnHit(this.time, this.eff);
	}
}
