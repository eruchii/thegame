package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.FocusModel;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mrmathami.thegame.Config;
import mrmathami.thegame.GameEntities;
import mrmathami.thegame.GameField;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.bullet.MachineGunBullet;
import mrmathami.thegame.entity.bullet.NormalBullet;
import mrmathami.thegame.entity.bullet.SniperBullet;
import mrmathami.thegame.entity.enemy.BossEnemy;
import mrmathami.thegame.entity.enemy.NormalEnemy;
import mrmathami.thegame.entity.enemy.SmallerEnemy;
import mrmathami.thegame.entity.enemy.TankerEnemy;
import mrmathami.thegame.entity.tile.Mountain;
import mrmathami.thegame.entity.tile.Road;
import mrmathami.thegame.entity.tile.Target;
import mrmathami.thegame.entity.tile.spawner.BossSpawner;
import mrmathami.thegame.entity.tile.spawner.NormalSpawner;
import mrmathami.thegame.entity.tile.spawner.SmallerSpawner;
import mrmathami.thegame.entity.tile.spawner.TankerSpawner;
import mrmathami.thegame.entity.tile.tower.MachineGunTower;
import mrmathami.thegame.entity.tile.tower.NormalTower;
import mrmathami.thegame.entity.tile.tower.SniperTower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameDrawer {
	/**
	 * TODO: This is a list contains Entity type that can be drawn on screen.
	 * Remember to add your own entity class here if it can be drawn.
	 */
	@Nonnull private static final List<Class<?>> ENTITY_DRAWING_ORDER = List.of(
			Road.class,
			Mountain.class,
			NormalTower.class,
			SniperTower.class,
			MachineGunTower.class,
			NormalBullet.class,
			MachineGunBullet.class,
			SniperBullet.class,
			NormalEnemy.class,
			SmallerEnemy.class,
			TankerEnemy.class,
			BossEnemy.class,
			NormalSpawner.class,
			SmallerSpawner.class,
			TankerSpawner.class,
			BossSpawner.class,
			Target.class
	);
	/**
	 * TODO:
	 * This is a map between Entity type and its drawer.
	 * Remember to add your entity drawer here.
	 */
	@Nonnull private static final Map<Class<? extends GameEntity>, EntityDrawer> ENTITY_DRAWER_MAP = new HashMap<>(Map.ofEntries(
			Map.entry(Road.class, new RoadDrawer()),
			Map.entry(Mountain.class, new MountainDrawer()),
			Map.entry(NormalTower.class, new NormalTowerDrawer()),
			Map.entry(SniperTower.class, new SniperTowerDrawer()),
			Map.entry(MachineGunTower.class, new MachineGunTowerDrawer()),
			Map.entry(NormalBullet.class, new NormalBulletDrawer()),
			Map.entry(MachineGunBullet.class, new MachineGunBulletDrawer()),
			Map.entry(SniperBullet.class, new SniperBulletDrawer()),
			Map.entry(NormalEnemy.class, new NormalEnemyDrawer()),
			Map.entry(SmallerEnemy.class, new SmallerEnemyDrawer()),
			Map.entry(TankerEnemy.class, new TankerEnemyDrawer()),
			Map.entry(BossEnemy.class, new BossEnemyDrawer()),
			Map.entry(NormalSpawner.class, new SpawnerDrawer()),
			Map.entry(SmallerSpawner.class, new SpawnerDrawer()),
			Map.entry(TankerSpawner.class, new SpawnerDrawer()),
			Map.entry(BossSpawner.class, new SpawnerDrawer()),
			Map.entry(Target.class, new TargetDrawer())
	));

	@Nonnull private final GraphicsContext graphicsContext;
	@Nonnull private GameField gameField;
	private transient double fieldStartPosX = Float.NaN;
	private transient double fieldStartPosY = Float.NaN;
	private transient double fieldZoom = Float.NaN;
	public HashMap<String, Image> cacheImg = new HashMap<>();

	public GameDrawer(@Nonnull GraphicsContext graphicsContext, @Nonnull GameField gameField) {
		this.graphicsContext = graphicsContext;
		this.gameField = gameField;
	}

	/**
	 * Do not touch me.
	 * This is a drawing order comparator, use to sort the entity list.
	 *
	 * @param entityA entity A
	 * @param entityB entity B
	 * @return order
	 */
	private static int entityDrawingOrderComparator(@Nonnull GameEntity entityA, @Nonnull GameEntity entityB) {
		final int compareOrder = Integer.compare(
				ENTITY_DRAWING_ORDER.indexOf(entityA.getClass()),
				ENTITY_DRAWING_ORDER.indexOf(entityB.getClass())
		);
		if (compareOrder != 0) return compareOrder;
		final int comparePosX = Double.compare(entityA.getPosX(), entityB.getPosX());
		if (comparePosX != 0) return comparePosX;
		final int comparePosY = Double.compare(entityA.getPosY(), entityB.getPosY());
		if (comparePosY != 0) return comparePosY;
		final int compareWidth = Double.compare(entityA.getWidth(), entityB.getWidth());
		if (compareWidth != 0) return compareWidth;
		return Double.compare(entityA.getHeight(), entityB.getHeight());
	}

	/**
	 * @param entity entity
	 * @return the drawer fot that entity, or null if that entity is not drawable.
	 */
	@Nullable
	private static EntityDrawer getEntityDrawer(@Nonnull GameEntity entity) {
		return ENTITY_DRAWER_MAP.get(entity.getClass());
	}

	public final double getFieldStartPosX() {
		return fieldStartPosX;
	}

	public final double getFieldStartPosY() {
		return fieldStartPosY;
	}

	public final double getFieldZoom() {
		return fieldZoom;
	}

	public final void setGameField(@Nonnull GameField gameField) {
		this.gameField = gameField;
	}

	/**
	 * Set the field view region, in other words, set the region of the field that will be drawn on the screen.
	 *
	 * @param fieldStartPosX pos x
	 * @param fieldStartPosY pos y
	 * @param fieldZoom      zoom
	 */
	public final void setFieldViewRegion(double fieldStartPosX, double fieldStartPosY, double fieldZoom) {
		this.fieldStartPosX = fieldStartPosX;
		this.fieldStartPosY = fieldStartPosY;
		this.fieldZoom = fieldZoom;
	}

	/**
	 * Do render. Should not touch.
	 */
	public final void render() {
		final GameField gameField = this.gameField;
		final double fieldStartPosX = this.fieldStartPosX;
		final double fieldStartPosY = this.fieldStartPosY;
		final double fieldZoom = this.fieldZoom;

		final List<GameEntity> entities = new ArrayList<>(GameEntities.getOverlappedEntities(gameField.getEntities(),
				fieldStartPosX, fieldStartPosY, Config.SCREEN_WIDTH / fieldZoom, Config.SCREEN_HEIGHT / fieldZoom));
		entities.sort(GameDrawer::entityDrawingOrderComparator);

		graphicsContext.setFill(Color.BLACK);
		graphicsContext.fillRect(0.0, 0.0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

		GameEntity lastEntity = null;
		for (final GameEntity entity : entities) {
			if (lastEntity != null && entityDrawingOrderComparator(entity, lastEntity) == 0) continue;
			lastEntity = entity;
			final EntityDrawer drawer = getEntityDrawer(entity);
			if (drawer != null) {
					drawer.draw(gameField.getTickCount(), graphicsContext, entity,
							(entity.getPosX() - fieldStartPosX) * fieldZoom,
							(entity.getPosY() - fieldStartPosY) * fieldZoom,
							entity.getWidth() * fieldZoom,
							entity.getHeight() * fieldZoom,
							fieldZoom, cacheImg
					);
			}
		}

// 		display HP and Money
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillText(String.format("Wave: %d", gameField.getWaveCount()+1), 0, Config.SCREEN_HEIGHT - 20);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillText(String.format("HP: %d", gameField.getTarget().getHealth()), 0, Config.SCREEN_HEIGHT - 40);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(String.format("Money: %d", gameField.getMoney()), 0, Config.SCREEN_HEIGHT - 60);
	}
	public void drawNewEntities(GameEntity entity) {
		final EntityDrawer drawer = getEntityDrawer(entity);
		if (drawer != null) {
			drawer.draw(gameField.getTickCount(), graphicsContext, entity,
					(entity.getPosX() - fieldStartPosX) * fieldZoom,
					(entity.getPosY() - fieldStartPosY) * fieldZoom,
					entity.getWidth() * fieldZoom,
					entity.getHeight() * fieldZoom,
					fieldZoom,
					cacheImg);
		}
		System.out.println(drawer);
		System.out.println(entity.getClass());
	}
	/*
	Mouse Hover Drawer
	 */
	public void NormalTowerHover(){
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Cost : " + Config.NORMAL_TOWER_COST + "\nRange : " + Config.NORMAL_TOWER_RANGE + "\nDamage : "
                + Config.NORMAL_BULLET_STRENGTH + "\nSpeed : Normal" , 150, Config.SCREEN_HEIGHT - 165);
    }

    public  void MachineGunTowerHover(){
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Cost : " + Config.MACHINE_GUN_TOWER_COST + "\nRange : " + Config.MACHINE_GUN_TOWER_RANGE + "\nDamage : "
                + Config.MACHINE_GUN_BULLET_STRENGTH + "\nSpeed : Fast" , 650, Config.SCREEN_HEIGHT - 165);
    }

    public void SniperGunTowerHover(){
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Cost : " + Config.SNIPER_GUN_TOWER_COST + "\nRange : " + Config.SNIPER_TOWER_RANGE + "\nDamage : "
                + Config.SNIPER_BULLET_STRENGTH + "\nSpeed : Slow" , 400, Config.SCREEN_HEIGHT - 165);
    }

    /*
    Game Over Screen
     */
	public final double screenToFieldPosX(double screenPosX) {
		return screenPosX * fieldZoom + fieldStartPosX;
	}

	public final double screenToFieldPosY(double screenPosY) {
		return screenPosY * fieldZoom + fieldStartPosY;
	}

	public final double fieldToScreenPosX(double fieldPosX) {
		return (fieldPosX - fieldStartPosX) / fieldZoom;
	}

	public final double fieldToScreenPosY(double fieldPosY) {
		return (fieldPosY - fieldStartPosY) / fieldZoom;
	}
}
