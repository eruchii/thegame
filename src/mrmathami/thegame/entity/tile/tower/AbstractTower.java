package mrmathami.thegame.entity.tile.tower;

import mrmathami.thegame.GameEntities;
import mrmathami.thegame.GameField;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.UpdatableEntity;
import mrmathami.thegame.entity.bullet.AbstractBullet;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.tile.AbstractTile;

import javax.annotation.Nonnull;
import java.util.Collection;

public abstract class AbstractTower<E extends AbstractBullet> extends AbstractTile implements UpdatableEntity {
	private final double range;
	private final long speed;
	private long numTarget = 1;
	private long tickDown;
	private long cost;

	protected AbstractTower(long createdTick, long posX, long posY, double range, long speed, long cost) {
		super(createdTick, posX, posY, 1L, 1L);
		this.range = range;
		this.speed = speed;
		this.tickDown = 0;
		this.cost = cost;
	}

	@Override
	public final UpdatableEntity onUpdate(@Nonnull GameField field) {
		this.tickDown -= 1;
		if (tickDown <= 0) {
			// TODO: Find a target and spawn a bullet to that direction.
			// Use GameEntities.getFilteredOverlappedEntities to find target in range
			// Remember to set this.tickDown back to this.speed after shooting something.
			Collection<GameEntity> entities = GameEntities.getFilteredOverlappedEntities(
			        field.getEntities(), GameEntity.class ,
                    this.getPosX() - this.range, this.getPosY() - this.range,
                    this.range*2.0, this.range*2.0);
			long bulletCount = this.numTarget;
			if(entities.size() < bulletCount) bulletCount = entities.size();
            for(GameEntity entity: entities){
            	if(bulletCount == 0){
            		this.tickDown = speed;
                    return null;
				}
				if(entity instanceof AbstractEnemy){
				    AbstractBullet bullet = this.doSpawn(field.getTickCount(),this, (AbstractEnemy) entity);
				    field.doSpawn(bullet);
				    bulletCount--;
				}
			}
		}
        return null;
    }
	// Tower can shoot more than one enemy
	public final void setNumTarget(long numTarget){ this.numTarget = numTarget;}
	public final long getCost(){
		return this.cost;
	}
	/**
	 * Create a new bullet. Each tower spawn different type of bullet.
	 * Override this method and return the type of bullet that your tower shot out.
	 * See NormalTower for example.
	 *
	 * @param createdTick createdTick
	 * @param posX posX
	 * @param posY posY
	 * @param deltaX deltaX
	 * @param deltaY deltaY
	 * @return the bullet entity
	 */
	@Nonnull
	protected abstract E doSpawn(long createdTick, double posX, double posY, double deltaX, double deltaY);
    @Nonnull
    protected abstract E doSpawn(long createdTick, AbstractTower tower, AbstractEnemy enemy);
}
