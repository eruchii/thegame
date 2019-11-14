package mrmathami.thegame.entity.enemy;

import mrmathami.thegame.GameEntities;
import mrmathami.thegame.GameField;
import mrmathami.thegame.entity.*;
import mrmathami.thegame.entity.tile.Road;

import javax.annotation.Nonnull;
import java.util.Collection;

public abstract class AbstractEnemy extends AbstractEntity implements UpdatableEntity, EffectEntity, LivingEntity, DestroyListener {
	private static final double SQRT_2 = Math.sqrt(2.0) / 2.0;
	private static final double[][] DELTA_DIRECTION_ARRAY = {
			{0.0, -1.0}, {0.0, 1.0}, {-1.0, 0.0}, {1.0, 0.0},
			{-SQRT_2, -SQRT_2}, {SQRT_2, SQRT_2}, {SQRT_2, -SQRT_2}, {-SQRT_2, SQRT_2},
	};

	private long health;
	private long armor;
	private double speed;
	private long reward;
	private long slowTime = 0;
	private double slowEff = 0;
	private long maxHealth;
	private int LT = 0;
	private double directionX = 1.0;
	private double directionY = 0.0;
	private boolean upgraded = false;
	private int waveCount = 0;

	protected AbstractEnemy(long createdTick, double posX, double posY, double size, long health, long armor, double speed, long reward) {
		super(createdTick, posX, posY, size, size);
		this.health = health;
		this.armor = armor;
		this.speed = speed;
		this.reward = reward;
		this.maxHealth = health;
	}

	private static double evaluateDistance(@Nonnull Collection<GameEntity> overlappableEntities,
			@Nonnull GameEntity sourceEntity, double posX, double posY, double width, double height) {
		double distance = 0.0;
		double sumArea = 0.0;
		for (GameEntity entity : GameEntities.getOverlappedEntities(overlappableEntities, posX, posY, width, height)) {
			if (sourceEntity != entity && GameEntities.isCollidable(sourceEntity.getClass(), entity.getClass())) return Double.NaN;
			if (entity instanceof Road) {
				final double entityPosX = entity.getPosX();
				final double entityPosY = entity.getPosY();
				final double area = (Math.min(posX + width, entityPosX + entity.getWidth()) - Math.max(posX, entityPosX))
						* (Math.min(posY + height, entityPosY + entity.getHeight()) - Math.max(posY, entityPosY));
				sumArea += area;
				distance += area * ((Road) entity).getDistance();
			}
		}
		return distance / sumArea;
	}

	@Override
	public final UpdatableEntity onUpdate(@Nonnull GameField field) {
		if(!upgraded && this.maxHealth != this.health){
			this.maxHealth = (long)(maxHealth*Math.pow(1.1, field.getWaveCount()));
			this.health = this.maxHealth;
			upgraded = true;
		}
		this.waveCount = field.getWaveCount();
		final double enemyPosX = getPosX();
		final double enemyPosY = getPosY();
		final double enemyWidth = getWidth();
		final double enemyHeight = getHeight();
		this.LT++;
		double currentSpeed = this.speed;

		if(this.slowTime > 0){
			this.slowTime -= 1;
			currentSpeed = this.speed * ((1 - this.slowEff > 0) ? (1 - this.slowEff) : 0.05);
		}

		final Collection<GameEntity> overlappableEntities = GameEntities.getOverlappedEntities(field.getEntities(),
				getPosX() - currentSpeed, getPosY() - currentSpeed, currentSpeed + getWidth() + currentSpeed, currentSpeed + getHeight() + currentSpeed);
		double minimumDistance = Double.MAX_VALUE;
		double newPosX = enemyPosX;
		double newPosY = enemyPosY;
		for (double realSpeed = currentSpeed * 0.03125; realSpeed <= currentSpeed; realSpeed += realSpeed) {
			for (double[] deltaDirection : DELTA_DIRECTION_ARRAY) {

				final double currentPosX = enemyPosX + deltaDirection[0] * realSpeed;
				final double currentPosY = enemyPosY + deltaDirection[1] * realSpeed;
				final double currentDistance = evaluateDistance(overlappableEntities, this, currentPosX, currentPosY, enemyWidth, enemyHeight);
				if (currentDistance < minimumDistance) {
					minimumDistance = currentDistance;
					newPosX = currentPosX;
					newPosY = currentPosY;
					directionX = deltaDirection[0];
					directionY = deltaDirection[1];
				}
			}
		}
		setPosX(newPosX);
		setPosY(newPosY);
        return null;
    }

	@Override
	public final void onDestroy(@Nonnull GameField field) {
		// TODO: reward
		field.getReward(reward);
	}

	@Override
	public final boolean onEffect(@Nonnull GameField field, @Nonnull LivingEntity livingEntity) {
		// TODO: harm the target
		if(this.isBeingOverlapped(livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getWidth(), livingEntity.getHeight()))
		{
			livingEntity.doEffect(-1);
			this.doDestroy();
			field.getReward(-reward);
		}
		return false;
	}

	@Override
	public final long getHealth() {
		return health;
	}

	public final long getMaxHealth(){ return maxHealth;}

	public int getLT() {return this.LT;}
	public double getDirectionX() {return this.directionX;}
	public double getDirectionY() {return this.directionY;}
	public int getWaveCount(){return this.waveCount;}

	@Override
	public final void doEffect(long value) {
		if (health != Long.MIN_VALUE && (value < -armor || value > 0)) this.health += value;
	}

	public final void reduceSpeed(long time, double x){
		this.slowTime = Math.max(this.slowTime, time);
		this.slowEff = Math.max(this.slowEff, x);
	}

	public final boolean isSlow(){ return this.slowTime > 0;}

	@Override
	public final void doDestroy() {
		this.health = Long.MIN_VALUE;
	}

	@Override
	public final boolean isDestroyed() {
		return health <= 0L;
	}
}
