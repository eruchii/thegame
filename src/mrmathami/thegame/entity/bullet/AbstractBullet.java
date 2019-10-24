package mrmathami.thegame.entity.bullet;

import mrmathami.thegame.GameField;
import mrmathami.thegame.entity.*;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

import javax.annotation.Nonnull;

public abstract class AbstractBullet extends AbstractEntity implements UpdatableEntity, EffectEntity, DestroyableEntity {
	private double deltaX;
	private double deltaY;
	private final long strength;
	private long tickDown;
	private AbstractEnemy enemy = null;
	private double speed;
	private double slowEff = 0;
	private long slowTime = 0;

	protected AbstractBullet(long createdTick, double posX, double posY, double deltaX, double deltaY, double speed, long strength, long timeToLive) {
		super(createdTick, posX, posY, 0.2, 0.2);
		final double normalize = speed / Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		this.deltaX = deltaX * normalize;
		this.deltaY = deltaY * normalize;
		this.strength = strength;
		this.tickDown = timeToLive;
	}

	protected AbstractBullet(long createdTick, AbstractTower tower, AbstractEnemy enemy, double speed, long strength, long timeToLive){
		super(createdTick, tower.getPosX() + tower.getWidth()/2.0, tower.getPosY() + tower.getHeight()/2.0, 0.2, 0.2);
		this.calcNewDelta();
		this.strength = strength;
		this.tickDown = timeToLive;
		this.enemy = enemy;
		this.speed = speed;
	}

	protected void calcNewDelta(){
		if(this.enemy != null) {
			double diffX = enemy.getPosX() + enemy.getWidth() / 2.0 - this.getPosX();
			double diffY = enemy.getPosY() + enemy.getHeight() / 2.0 - this.getPosY();
			double s = Math.sqrt(diffX * diffX + diffY * diffY);
			double normalize = this.speed *1.5 / s;
			this.deltaX = diffX * normalize;
			this.deltaY = diffY * normalize;
		}
	}

	public void effectOnHit(long time, double eff){
		this.slowTime = time;
		this.slowEff = eff;
	}

	@Override
	public final void onUpdate(@Nonnull GameField field) {
		if(this.enemy != null && this.enemy.isDestroyed()){
			this.doDestroy();
			return;
		}
		this.tickDown -= 1;
		setPosX(getPosX() + deltaX);
		setPosY(getPosY() + deltaY);
		this.calcNewDelta();
	}

	@Override
	public final boolean onEffect(@Nonnull GameField field, @Nonnull LivingEntity livingEntity) {
		livingEntity.doEffect(-strength);
		if(livingEntity instanceof AbstractEnemy){
			((AbstractEnemy) livingEntity).reduceSpeed(slowTime, slowEff);
		}
		this.tickDown = 0;
		return false;
	}

	@Override
	public final void doDestroy() {
		this.tickDown = 0;
	}

	@Override
	public final boolean isDestroyed() {
		return tickDown <= -0;
	}
}
