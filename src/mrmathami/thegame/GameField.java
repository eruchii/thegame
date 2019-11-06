package mrmathami.thegame;


import mrmathami.thegame.entity.*;
import mrmathami.thegame.entity.tile.Target;
import mrmathami.thegame.entity.tile.tower.AbstractTower;
import mrmathami.thegame.entity.tile.tower.MachineGunTower;
import mrmathami.thegame.entity.tile.tower.NormalTower;
import mrmathami.thegame.entity.tile.tower.SniperTower;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Game Field. Created from GameMap for each new stage. Represent the currently playing game.
 */
public final class GameField {
	@Nonnull private final Set<GameEntity> entities = new LinkedHashSet<>(Config._TILE_MAP_COUNT);
	@Nonnull private final Collection<GameEntity> unmodifiableEntities = Collections.unmodifiableCollection(entities);
	@Nonnull private final List<GameEntity> spawnEntities = new ArrayList<>(Config._TILE_MAP_COUNT);
	/**
	 * Field width
	 */
	private final double width;
	/**
	 * Field height
	 */
	private final double height;
	/**
	 * Field tick count
	 */
	private long tickCount;
	/**
	 * Target
	 */
	private Target Target;

	/**
     * Money
     */
	private long money = 0;

	public GameField(@Nonnull GameStage gameStage) {
		this.width = gameStage.getWidth();
		this.height = gameStage.getHeight();
		this.tickCount = 0;
		entities.addAll(gameStage.getEntities());
		for(GameEntity entity: entities){
			if(entity instanceof Target) this.Target = (Target) entity;
		}
	}

	public final double getWidth() {
		return width;
	}

	public final double getHeight() {
		return height;
	}

	public final long getTickCount() {
		return tickCount;
	}

	public final Target getTarget(){
	    return  this.Target;
    }

    public void getReward(long reward){
        money += reward;
    }

    public long getMoney(){
        return money;
    }
	/**
	 * @return entities on the field. Read-only list.
	 */
	@Nonnull
	public final Collection<GameEntity> getEntities() {
		return unmodifiableEntities;
	}

	/**
	 * Add an Entity to spawn list. Entity will be spawned at the end of this tick.
	 *
	 * @param entity Entity to spawn
	 */
	public final void doSpawn(@Nonnull GameEntity entity) {
		if (entity.isBeingOverlapped(0.0, 0.0, width, height)) spawnEntities.add(entity);
	}

	/**
	 * Do a tick, in other words, update the field after a fixed period of time.
	 * Current update sequence:
	 * 1. Update Entity:
	 * 1.1. UpdatableEntity update itself, including moving.
	 * 1.2. EffectEntity check collision to affect LivingEntity.
	 * 1.3. DestroyableEntity check and react if it is going to be destroyed.
	 * 2. Destroy Entity:
	 * 2.1. Destroy entities that are marked to be destroyed.
	 * 2.2. Destroy entities that are outside the field.
	 * 3. Spawn Entity: Add entities that are marked to be spawned.
	 */
	public final void tick() {
		this.tickCount += 1;
		// 1.1. Update UpdatableEntity
		for (final GameEntity entity : entities) {
			if (entity instanceof UpdatableEntity) ((UpdatableEntity) entity).onUpdate(this);
		}

		// 1.2. Update EffectEntity & LivingEntity
		for (final GameEntity entity : entities) {
			if (entity instanceof EffectEntity) {
				final EffectEntity effectEntity = (EffectEntity) entity;
				final Collection<LivingEntity> livingEntities = GameEntities.getAffectedEntities(entities,
						effectEntity.getClass(), entity.getPosX(), entity.getPosY(), entity.getWidth(), entity.getHeight());
				for (final LivingEntity livingEntity : livingEntities) {
					if (!effectEntity.onEffect(this, livingEntity)) break;
				}
			}
		}
		// 1.3. Update DestroyableEntity
		final List<GameEntity> destroyedEntities = new ArrayList<>(Config._TILE_MAP_COUNT);
		for (final GameEntity entity : entities) {
			if (entity instanceof DestroyableEntity && ((DestroyableEntity) entity).isDestroyed()) {
				if (entity instanceof DestroyListener) ((DestroyListener) entity).onDestroy(this);
				destroyedEntities.add(entity);
			}
		}

		// 2.1. Destroy entities
		entities.removeAll(destroyedEntities);

		// 2.2. Destroy entities
		entities.removeIf(entity -> !entity.isBeingOverlapped(0.0, 0.0, width, height));

		// 3. Spawn entities
		for (GameEntity entity : spawnEntities) {
			entities.add(entity);
			if (entity instanceof SpawnListener) ((SpawnListener) entity).onSpawn(this);
		}
		spawnEntities.clear();
		if(getTickCount() == 50) this.load("2019-10-30 17-48-23.txt");
	}

    /**
     *  Save game field
     */
	public final void save(String name)  {
	    String filename = new String();
	    if(name == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            filename = sdf.format(new Date()) + ".txt";
        } else filename = name;
		System.out.println(filename);
		try(final PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
			if (writer == null) throw new IOException("Resource not found!");
			ArrayList<String> file = new ArrayList<>();
			file.add(String.format("%s %d\n","Money", this.money));
			file.add(String.format("%s %d\n", "TargetHP", Target.getHealth()));
			for (GameEntity entity : entities) {
			    if(entity instanceof AbstractTower) {
                    String[] classname = entity.getClass().toString().split("class ")[1].split("[. ]+");
                    String entityName = classname[classname.length - 1];
                    file.add(String.format("%s %f %f\n", entityName, entity.getPosX(), entity.getPosY()));
                }
			}
			System.out.println(file.size());
			writer.println(file.size());
			for(String s: file){
				writer.write(s);
			}
			System.out.println("Saved!");
		} catch (IOException e) {
			System.out.println("MonkaS");
		}
	}

	/**
	 * Load
	 */
	public final void load(@Nonnull String name){
		try (final InputStream stream = new FileInputStream(name)) {
            if (stream == null) throw new IOException("Resource not found! Resource name: " + name);
            final Scanner scanner = new Scanner(stream);
            try {
                final int n = scanner.nextInt();
                for (int i = 0; i < n; i++) {
                    String type = scanner.next();
                    if ("Money".equals(type)) {
                        int m = scanner.nextInt();
                        this.money = m;
                    } else if ("TargetHP".equals(type)) {
                        int hp = scanner.nextInt();
                        this.Target.setHealth(hp);
                    } else if ("NormalTower".equals(type)) {
                        double x = scanner.nextDouble();
                        double y = scanner.nextDouble();
                        entities.add(new NormalTower(0, (long) x, (long) y));
                    } else if ("MachineGunTower".equals(type)) {
                        double x = scanner.nextDouble();
                        double y = scanner.nextDouble();
                        entities.add(new MachineGunTower(0, (long) x, (long) y));
                    } else if ("SniperTower".equals(type)) {
                        double x = scanner.nextDouble();
                        double y = scanner.nextDouble();
                        entities.add(new SniperTower(0, (long) x, (long) y));
                    }
                }
                System.out.println("Loaded");
            } catch (NoSuchElementException e) {
                throw new IOException("Resource invalid! Resource name: " + name, e);
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

}
