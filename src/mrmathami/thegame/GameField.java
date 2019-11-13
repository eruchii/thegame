package mrmathami.thegame;


import javafx.scene.canvas.GraphicsContext;
import mrmathami.thegame.entity.*;
import mrmathami.thegame.entity.enemy.BossEnemy;
import mrmathami.thegame.entity.enemy.NormalEnemy;
import mrmathami.thegame.entity.enemy.TankerEnemy;
import mrmathami.thegame.entity.tile.Target;
import mrmathami.thegame.entity.tile.spawner.*;
import mrmathami.thegame.entity.tile.tower.AbstractTower;
import mrmathami.thegame.entity.tile.tower.MachineGunTower;
import mrmathami.thegame.entity.tile.tower.NormalTower;
import mrmathami.thegame.entity.tile.tower.SniperTower;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.enemy.SmallerEnemy;
import mrmathami.thegame.entity.bullet.AbstractBullet;


import javax.annotation.Nonnull;
import java.io.*;
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
	private AbstractSpawner spawner = null;

	private int currentWave = 0;
	private int waveCount = 0;
	/**
     * Money
     */
	private long money = 5000;

	private boolean pause = false;

	public GameField(@Nonnull GameStage gameStage) {
		this.width = gameStage.getWidth();
		this.height = gameStage.getHeight();
		this.tickCount = 0;
		entities.addAll(gameStage.getEntities());
		for(GameEntity entity: entities){
			if(entity instanceof Target) this.Target = (Target) entity;
			if(entity instanceof AbstractSpawner) this.spawner = (AbstractSpawner) entity;
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
	public final void updateTarget(Target newTarget) { this.Target = newTarget;}
    public void getReward(long reward){
        money += reward;
    }

    public long getMoney(){
        return money;
    }

    public int getCurrentWave(){
		return this.currentWave;
	}

	public int getWaveCount(){
		return this.waveCount;
	}

    public void pauseGame() {this.pause = true;}
    public void unpauseGame(){ this.pause = false;}
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
		//1.0 Check if the game is over or pausing
		if (Target.isDestroyed() || this.pause){
			return;
		}
		this.tickCount += 1;

		// refill spawner

		// 1.1. Update UpdatableEntity && get spawner

		for (final GameEntity entity : entities) {
			if (entity instanceof UpdatableEntity) ((UpdatableEntity) entity).onUpdate(this);
			if (entity instanceof AbstractSpawner && this.spawner == null) this.spawner = (AbstractSpawner) entity;
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
			if(entity instanceof AbstractSpawner){
				if(((AbstractSpawner)entity).getNumOfSpawn() == 0) destroyedEntities.add(entity);
			}
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
		//4. refill spawner
		boolean hasEnemy = false;
		boolean hasSpawner = false;
		for(final GameEntity entity: entities){
			if(entity instanceof AbstractEnemy){
				hasEnemy = true;
				break;
			}
			if(entity instanceof AbstractSpawner){
				hasSpawner = true;
			}
		}
		if(!hasSpawner && this.spawner.getNumOfSpawn() > 0 && !hasEnemy) this.doSpawn(this.spawner);
		if((this.spawner.getNumOfSpawn() == 0 && !hasEnemy)){
			this.currentWave = (this.currentWave + 1) % 4;
			if(this.currentWave == 1) {
				this.spawner = new NormalSpawner(this.getTickCount(),
						(long)this.spawner.getPosX(), (long)this.spawner.getPosY(), (long)this.spawner.getWidth(),
						(long)this.spawner.getHeight(), Config.NORMAL_SPAWNER_INTERVAL,
						Config.NORMAL_SPAWNER_DELAY, Config.NORMAL_SPAWNER_NUM + waveCount);

			} else if(this.currentWave == 2){
				this.spawner = new SmallerSpawner(this.getTickCount(),
						(long)this.spawner.getPosX(), (long)this.spawner.getPosY(), (long)this.spawner.getWidth(),
						(long)this.spawner.getHeight(), Config.SMALLER_SPAWNER_INTERVAL,
						Config.SMALLER_SPAWNER_DELAY, Config.SMALLER_SPAWNER_NUM + waveCount);
			} else if(this.currentWave == 3){
				this.spawner = new TankerSpawner(this.getTickCount(),
						(long)this.spawner.getPosX(), (long)this.spawner.getPosY(), (long)this.spawner.getWidth(),
						(long)this.spawner.getHeight(), Config.TANKER_SPAWNER_INTERVAL,
						Config.TANKER_SPAWNER_DELAY, Config.TANKER_SPAWNER_NUM + waveCount);
				this.doSpawn(this.spawner);
			} else if(this.currentWave == 0){
				this.spawner = new BossSpawner(this.getTickCount(),
						(long)this.spawner.getPosX(), (long)this.spawner.getPosY(), (long)this.spawner.getWidth(),
						(long)this.spawner.getHeight(), Config.BOSS_SPAWNER_INTERVAL,
						Config.BOSS_SPAWNER_DELAY, Config.BOSS_SPAWNER_NUM + waveCount);
				waveCount++;
			}
			this.doSpawn(this.spawner);
		}
	}
	/**
	 * Game over
	 */
	public boolean GameOver(){
		return Target.isDestroyed();
	}
    /**
     *  Save game field
     */
    public final List<String> createSaveFile(){
		List<String> file = new ArrayList<>();
		file.add(String.format("%s %d\n","Money", this.money));
		file.add(String.format("%s %d\n","Tick", this.getTickCount()));
		file.add(String.format("%s %f %f %f %f %d\n",
				"TargetHP", this.Target.getPosX(), this.Target.getPosY(), this.Target.getWidth(),
				this.Target.getHeight(), this.Target.getHealth())
		);
		file.add(String.format("%s %d\n","CurrentWave",this.currentWave));
		file.add(String.format("%s %d\n","WaveCount", this.waveCount));
		for (GameEntity entity : entities) {
			String[] classname = entity.getClass().toString().split("class ")[1].split("[. ]+");
			String entityName = classname[classname.length - 1];
			if(entity instanceof AbstractTower || entity instanceof AbstractEnemy) {
				file.add(String.format("%s %d %f %f\n", entityName, entity.getCreatedTick(),
						entity.getPosX(), entity.getPosY())
				);
			} else if(entity instanceof AbstractSpawner){
				file.add(String.format("%s %d %f %f %f %f %d %d %d\n",
						entityName, entity.getCreatedTick(), entity.getPosX(), entity.getPosY(), entity.getWidth(), entity.getHeight(),
						((AbstractSpawner) entity).getSpawnInterval(), ((AbstractSpawner) entity).getInitialDelay(),
						((AbstractSpawner) entity).getNumOfSpawn())
				);
			}
		}
		return file;
	}

	public final void saveToFile(String name)  {
	    String filename = new String();
	    if(name == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            filename = sdf.format(new Date()) + ".txt";
        } else filename = name;
		System.out.println(filename);
		try(final PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
			if (writer == null) throw new IOException("Resource not found!");
			List<String> file = createSaveFile();
			writer.println(file.size());
			for(String s: file){
				writer.write(s);
			}
			System.out.println("Saved!");
		} catch (IOException e) {
			System.out.println("MonkaS");
		}
	}

	public final void saveToCloud(){
    	Thread t = new Thread(new HttpMultiThread(this, "POST"));
    	t.start();
	}

	/**
	 * Load
	 */
	public final void load(InputStream stream){
	    Scanner scanner = new Scanner(stream);
		final List<GameEntity> destroyedEntities = new ArrayList<>(Config._TILE_MAP_COUNT);
		for(GameEntity entity: entities){
			if(entity instanceof AbstractTower || entity instanceof AbstractEnemy || entity instanceof AbstractBullet
					|| entity instanceof AbstractSpawner || entity instanceof Target || entity instanceof AbstractSpawner){
				destroyedEntities.add(entity);
			}
		}
		entities.removeAll(destroyedEntities);
		final int n = scanner.nextInt();
		for (int i = 0; i < n; i++) {
			String type = scanner.next();
			try{
			if ("Money".equals(type)) {
				int m = scanner.nextInt();
				this.money = m;
			} else if ("TargetHP".equals(type)) {
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double w = scanner.nextDouble();
				double h = scanner.nextDouble();
				int health = scanner.nextInt();
				Target new_target = new Target(0, (long)x, (long)y, (long)w, (long)h, health);
				entities.add(new_target);
				this.Target = new_target;
			} else if("Tick".equals(type)){
				int tick = scanner.nextInt();
				this.tickCount = tick+1;
			} else if("CurrentWave".equals(type)){
				int wave = scanner.nextInt();
				this.currentWave = wave;
			} else if("WaveCount".equals(type)){
				int count = scanner.nextInt();
				this.waveCount = count;
			} else if ("NormalTower".equals(type)) {
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new NormalTower(tick, (long) x, (long) y));
			} else if ("MachineGunTower".equals(type)) {
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new MachineGunTower(tick, (long) x, (long) y));
			} else if ("SniperTower".equals(type)) {
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new SniperTower(tick, (long) x, (long) y));
			} else if("NormalEnemy".equals(type)){
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new NormalEnemy(tick, x, y));
			} else if("BossEnemy".equals(type)){
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new BossEnemy(tick, x, y));
			} else if("SmallerEnemy".equals(type)){
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new SmallerEnemy(tick, x, y));
			} else if("TankerEnemy".equals(type)){
				long tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				entities.add(new TankerEnemy(tick, x, y));
			} else if("NormalSpawner".equals(type)){
				int tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double w = scanner.nextDouble();
				double h = scanner.nextDouble();
				int spawnInterval = scanner.nextInt();
				int initialDelay = scanner.nextInt();
				int numOfSpawn = scanner.nextInt();
				entities.add(new NormalSpawner(tick, (long)x, (long)y, (long)w, (long)h,
						spawnInterval, initialDelay, numOfSpawn)
				);
			} else if("BossSpawner".equals(type)){
				int tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double w = scanner.nextDouble();
				double h = scanner.nextDouble();
				int spawnInterval = scanner.nextInt();
				int initialDelay = scanner.nextInt();
				int numOfSpawn = scanner.nextInt();
				entities.add(new BossSpawner(tick, (long)x, (long)y, (long)w, (long)h,
						spawnInterval, initialDelay, numOfSpawn)
				);
			} else if("SmallerSpawner".equals(type)){
				int tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double w = scanner.nextDouble();
				double h = scanner.nextDouble();
				int spawnInterval = scanner.nextInt();
				int initialDelay = scanner.nextInt();
				int numOfSpawn = scanner.nextInt();
				entities.add(new SmallerSpawner(tick, (long)x, (long)y, (long)w, (long)h,
						spawnInterval, initialDelay, numOfSpawn)
				);
			} else if("TankerSpawner".equals(type)){
				int tick = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double w = scanner.nextDouble();
				double h = scanner.nextDouble();
				int spawnInterval = scanner.nextInt();
				int initialDelay = scanner.nextInt();
				int numOfSpawn = scanner.nextInt();
				entities.add(new TankerSpawner(tick, (long)x, (long)y, (long)w, (long)h,
						spawnInterval, initialDelay, numOfSpawn)
				);
			}
			} catch (InputMismatchException e){
				System.out.println(type);
			}
		}
		System.out.println("Loaded");
	}

	public final void loadFromFile(@Nonnull String name){
		try (final InputStream stream = this.getClass().getResourceAsStream(name)) {
            if (stream == null) throw new IOException("Resource not found! Resource name: " + name);
            try {
				load(stream);
            } catch (NoSuchElementException e) {
                throw new IOException("Resource invalid! Resource name: " + name, e);
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void loadFromCloud(){
		Thread t = new Thread(new HttpMultiThread(this, "GET"));
		t.start();
	}

}
