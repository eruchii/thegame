package mrmathami.thegame;

public final class Config {
	/**
	 * Game name. Change it if you want.
	 */
	public static final String GAME_NAME = "Tower Defense";
	/**
	 * Ticks per second
	 */
	public static final long GAME_TPS = 20;
	/**
	 * Nanoseconds per tick
	 */
	public static final long GAME_NSPT = Math.round(1000000000.0 / GAME_TPS);

	/**
	 * Size of the tile, in pixel.
	 * 1.0 field unit == TILE_SIZE pixel on the screen.
	 * Change it base on your texture size.
	 */
	public static final long TILE_SIZE = 35;
	/**
	 * Num of tiles the screen can display if fieldZoom is TILE_SIZE,
	 * in other words, the texture will be display as it without scaling.
	 */
	public static final long TILE_HORIZONTAL = 30;
	/**
	 * Num of tiles the screen can display if fieldZoom is TILE_SIZE,
	 * in other words, the texture will be display as it without scaling.
	 */
	public static final long TILE_VERTICAL = 20;
	/**
	 * An arbitrary number just to make some code run a little faster.
	 * Do not touch.
	 */
	public static final int _TILE_MAP_COUNT = (int) (TILE_HORIZONTAL * TILE_VERTICAL);


	/**
	 * Size of the screen.
	 */
	public static final long SCREEN_WIDTH = TILE_SIZE * TILE_HORIZONTAL;
	/**
	 * Size of the screen.
	 */
	public static final long SCREEN_HEIGHT = TILE_SIZE * TILE_VERTICAL;


	//Other config related to other entities in the game.

	//region Bullet
	public static final long NORMAL_BULLET_TTL = 30;
	public static final long NORMAL_BULLET_STRENGTH = 5;
	public static final double NORMAL_BULLET_SPEED = 0.55;
	public static final long NORMAL_BULLET_COUNT = 1;

	public static final long MACHINE_GUN_BULLET_TTL = 15;
	public static final long MACHINE_GUN_BULLET_STRENGTH = 50;
	public static final double MACHINE_GUN_BULLET_SPEED = 0.55;
	public static final long MACHINE_GUN_BULLET_COUNT = 5;

	public static final long SNIPER_BULLET_TTL = 60;
	public static final long SNIPER_BULLET_STRENGTH = 300;
	public static final double SNIPER_BULLET_SPEED = 0.55;
	public static final long SNIPER_BULLET_COUNT = 1;
	//endregion

	//region Tower
	public static final long NORMAL_TOWER_SPEED = 60;
	public static final double NORMAL_TOWER_RANGE = 5.0;
	public static final long NORMAL_TOWER_COST = 10;

	public static final long MACHINE_GUN_TOWER_SPEED = 15;
	public static final double MACHINE_GUN_TOWER_RANGE = 4.0;
	public static final long MACHINE_GUN_TOWER_COST = 20;

	public static final long SNIPER_TOWER_SPEED = 80;
	public static final double SNIPER_TOWER_RANGE = 8.0;
	public static final long SNIPER_GUN_TOWER_COST = 30;
	//endregion

	//region Enemy
	public static final double NORMAL_ENEMY_SIZE = 2;
	public static final long NORMAL_ENEMY_HEALTH = 1000;
	public static final long NORMAL_ENEMY_ARMOR = 3;
	public static final double NORMAL_ENEMY_SPEED = 0.2;
	public static final long NORMAL_ENEMY_REWARD = 1;

	public static final double SMALLER_ENEMY_SIZE = 2;
	public static final long SMALLER_ENEMY_HEALTH = 500;
	public static final long SMALLER_ENEMY_ARMOR = 0;
	public static final double SMALLER_ENEMY_SPEED = 0.25;
	public static final long SMALLER_ENEMY_REWARD = 2;

	public static final double TANKER_ENEMY_SIZE = 2;
	public static final long TANKER_ENEMY_HEALTH = 3000;
	public static final long TANKER_ENEMY_ARMOR = 5;
	public static final double TANKER_ENEMY_SPEED = 0.2;
	public static final long TANKER_ENEMY_REWARD = 3;

	public static final double BOSS_ENEMY_SIZE = 2;
	public static final long BOSS_ENEMY_HEALTH = 5000;
	public static final long BOSS_ENEMY_ARMOR = 8;
	public static final double BOSS_ENEMY_SPEED = 0.15;
	public static final long BOSS_ENEMY_REWARD = 10;
	//endregion

	public static final long NORMAL_SPAWNER_INTERVAL = 15;
	public static final long NORMAL_SPAWNER_DELAY = 0;
	public static final long NORMAL_SPAWNER_NUM = 3;

	public static final long SMALLER_SPAWNER_INTERVAL = 15;
	public static final long SMALLER_SPAWNER_DELAY = 0;
	public static final long SMALLER_SPAWNER_NUM = 3;

	public static final long TANKER_SPAWNER_INTERVAL = 30;
	public static final long TANKER_SPAWNER_DELAY = 0;
	public static final long TANKER_SPAWNER_NUM = 1;

	public static final long BOSS_SPAWNER_INTERVAL = 100;
	public static final long BOSS_SPAWNER_DELAY = 0;
	public static final long BOSS_SPAWNER_NUM = 1;

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 80;
	public static final String PROTOCOL = "http";

	private Config() {
	}


}
