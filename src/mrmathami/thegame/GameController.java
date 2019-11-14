package mrmathami.thegame;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.WindowEvent;
import mrmathami.thegame.drawer.GameDrawer;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.tile.Mountain;
import mrmathami.thegame.entity.tile.tower.MachineGunTower;
import mrmathami.thegame.entity.tile.tower.NormalTower;
import mrmathami.thegame.entity.tile.tower.SniperTower;
import mrmathami.utilities.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A game controller. Everything about the game should be managed in here.
 */
public final class GameController extends AnimationTimer {
	private GameEntity lastEntityToAdd;
	private int currentEntityToAdd = 0;
	private Shop shop;
	private double mouseMovePosX, mouseMovePosY;
	private double gameOverScreenPosX, getGameOverScreenPosY;
	private int mouseHover = 0;
	private int mouseExited;
	/**
	 * Advance stuff. Just don't touch me. Google me if you are curious.
	 */
	private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactoryBuilder()
					.setNamePrefix("Tick")
					.setDaemon(true)
					.setPriority(Thread.NORM_PRIORITY)
					.build()
	);

	/**
	 * The screen to draw on. Just don't touch me. Google me if you are curious.
	 */
	private final GraphicsContext graphicsContext;

	/**
	 * Game field. Contain everything in the current game field.
	 * Responsible to update the field every tick.
	 * Kinda advance, modify if you are sure about your change.
	 */
	private GameField field;
	/**
	 * Game drawer. Responsible to draw the field every tick.
	 * Kinda advance, modify if you are sure about your change.
	 */
	private GameDrawer drawer;

	/**
	 * Beat-keeper Manager. Just don't touch me. Google me if you are curious.
	 */
	private ScheduledFuture<?> scheduledFuture;

	/**
	 * The tick value. Just don't touch me.
	 * Google me if you are curious, especially about volatile.
	 * WARNING: Wall of text.
	 */
	private volatile long tick;

	/**
	 * The constructor.
	 *
	 * @param graphicsContext the screen to draw on
	 */
	public GameController(GraphicsContext graphicsContext) {
		// The screen to draw on
		this.graphicsContext = graphicsContext;
		// Just a few acronyms.
		final long width = Config.TILE_HORIZONTAL;
		final long height = Config.TILE_VERTICAL;
		this.graphicsContext.setFont(new Font(15));
		this.graphicsContext.setFontSmoothingType(FontSmoothingType.LCD);
		// The game field. Please consider create another way to load a game field.
		// TODO: I don't have much time, so, spawn some wall then :)
		this.field = new GameField(GameStage.load("/stage/demo.txt"));

		// The drawer. Nothing fun here.
		this.drawer = new GameDrawer(graphicsContext, field);

		// Field view region is a rectangle region
		// [(posX, posY), (posX + SCREEN_WIDTH / zoom, posY + SCREEN_HEIGHT / zoom)]
		// that the drawer will select and draw everything in it in an self-defined order.
		// Can be modified to support zoom in / zoom out of the map.
		drawer.setFieldViewRegion(0.0, 0.0, Config.TILE_SIZE);
		shop = new Shop(this.field);
	}

	/**
	 * Beat-keeper. Just don't touch me.
	 */
	private void tick() {
		//noinspection NonAtomicOperationOnVolatileField
		this.tick += 1;
	}

	/**
	 * A JavaFX loop.
	 * Kinda advance, modify if you are sure about your change.
	 *
	 * @param now not used. It is a timestamp in nanosecond.
	 */
	@Override
	public void handle(long now) {
		// don't touch me.
		final long currentTick = tick;
		final long startNs = System.nanoTime();

		// do a tick, as fast as possible
		field.tick();

//		// if it's too late to draw a new frame, skip it.
//		// make the game feel really laggy, so...
//		if (currentTick != tick) return;

		// draw a new frame, as fast as possible.
		drawer.render();
		// MSPT display. MSPT stand for Milliseconds Per Tick.
		// It means how many ms your game spend to update and then draw the game once.
		// Draw it out mostly for debug
		final double mspt = (System.nanoTime() - startNs) / 1000000.0;
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.fillText(String.format("MSPT: %3.2f", mspt), 0, 12);

		// Draw the Tower on the mouse after Button Clicked
		if (this.currentEntityToAdd == 1){ // Normal Tower
            graphicsContext.strokeOval(mouseMovePosX-Config.TILE_SIZE*Config.NORMAL_TOWER_RANGE/2, mouseMovePosY-Config.TILE_SIZE*Config.NORMAL_TOWER_RANGE/2,
                    Config.TILE_SIZE*Config.NORMAL_TOWER_RANGE,Config.TILE_SIZE*Config.NORMAL_TOWER_RANGE);
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillOval(mouseMovePosX-Config.TILE_SIZE/2, mouseMovePosY-Config.TILE_SIZE/2, Config.TILE_SIZE, Config.TILE_SIZE);
		}
		else if (this.currentEntityToAdd == 2){ // Machine Gun Tower
			graphicsContext.strokeOval(mouseMovePosX-Config.TILE_SIZE*Config.MACHINE_GUN_TOWER_RANGE/2, mouseMovePosY-Config.TILE_SIZE*Config.MACHINE_GUN_TOWER_RANGE/2,
					Config.TILE_SIZE*Config.MACHINE_GUN_TOWER_RANGE,Config.TILE_SIZE*Config.MACHINE_GUN_TOWER_RANGE);
			graphicsContext.setFill(Color.DARKRED);
			graphicsContext.fillOval(mouseMovePosX-Config.TILE_SIZE/2, mouseMovePosY-Config.TILE_SIZE/2, Config.TILE_SIZE, Config.TILE_SIZE);
		}
		else if (this.currentEntityToAdd == 3){ // Sniper Tower
			graphicsContext.strokeOval(mouseMovePosX-Config.TILE_SIZE*Config.SNIPER_TOWER_RANGE/2, mouseMovePosY-Config.TILE_SIZE*Config.SNIPER_TOWER_RANGE/2,
					Config.TILE_SIZE*Config.SNIPER_TOWER_RANGE,Config.TILE_SIZE*Config.SNIPER_TOWER_RANGE);
			graphicsContext.setFill(Color.MEDIUMVIOLETRED);
			graphicsContext.fillOval(mouseMovePosX-Config.TILE_SIZE/2, mouseMovePosY-Config.TILE_SIZE/2, Config.TILE_SIZE, Config.TILE_SIZE);

		}

		// Draw the information about the tower when hover on the button
		if (mouseHover == 1)
			drawer.NormalTowerHover();
		else if (mouseHover == 2){
			drawer.MachineGunTowerHover();
		}
		else if (mouseHover == 3){
			drawer.SniperGunTowerHover();
		}
		if(field.GameOver()){
			String path = String.valueOf(this.getClass().getResource("/PNG/game_over.png"));
			Image content = new Image(path, 500, 500, false, true);
			graphicsContext.drawImage(content, Config.SCREEN_WIDTH/2-250, Config.SCREEN_HEIGHT/2-250);
		}

		// if we have time to spend, do a spin
		while (currentTick == tick) Thread.onSpinWait();
	}

	/**
	 * Start rendering and ticking. Just don't touch me.
	 * Anything that need to initialize should be in the constructor.
	 */
	@Override
	public void start() {
		// Start the beat-keeper. Start to call this::tick at a fixed rate.
		this.scheduledFuture = SCHEDULER.scheduleAtFixedRate(this::tick, 0, Config.GAME_NSPT, TimeUnit.NANOSECONDS);
		// start the JavaFX loop.
		super.start();
	}

	/**
	 * On window close request.
	 * Kinda advance, modify if you are sure about your change.
	 *
	 * @param windowEvent currently not used
	 */
	final void closeRequestHandler(WindowEvent windowEvent) {
		scheduledFuture.cancel(true);
		stop();
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Key down handler.
	 *
	 * @param keyEvent the key that you press down
	 */
	final void keyDownHandler(KeyEvent keyEvent) {
		final KeyCode keyCode = keyEvent.getCode();
		if (keyCode == KeyCode.W) {
		} else if (keyCode == KeyCode.S) {
		} else if (keyCode == KeyCode.A) {
		} else if (keyCode == KeyCode.D) {
		} else if (keyCode == KeyCode.I) {
		} else if (keyCode == KeyCode.J) {
		} else if (keyCode == KeyCode.K) {
		} else if (keyCode == KeyCode.L) {
		}
	}

	/**
	 * Key up handler.
	 *
	 * @param keyEvent the key that you release up.
	 */
	final void keyUpHandler(KeyEvent keyEvent) {
		final KeyCode keyCode = keyEvent.getCode();
		if (keyCode == KeyCode.W) {
		} else if (keyCode == KeyCode.S) {

		} else if (keyCode == KeyCode.A) {
		} else if (keyCode == KeyCode.D) {
		} else if (keyCode == KeyCode.I) {
		} else if (keyCode == KeyCode.J) {
		} else if (keyCode == KeyCode.K) {
		} else if (keyCode == KeyCode.L) {
		}
	}

	/**
	 * Button Down Handler
	 *
	 * @param mouseEvent
	 */
	public void NormalTowerClicked(MouseEvent mouseEvent){
		this.currentEntityToAdd = 1;
		mouseMovePosY = mouseEvent.getSceneY();
		mouseMovePosX = mouseEvent.getSceneX();
	}

	public void MachineGunTowerClicked(MouseEvent mouseEvent){
		this.currentEntityToAdd = 2;
		mouseMovePosY = mouseEvent.getSceneY();
		mouseMovePosX = mouseEvent.getSceneX();
	}
	public void SniperTowerClicked(MouseEvent mouseEvent){
		this.currentEntityToAdd = 3;
		mouseMovePosY = mouseEvent.getSceneY();
		mouseMovePosX = mouseEvent.getSceneX();
	}

	public void SaveButtonClicked(MouseEvent mouseEvent){
		field.saveToCloud();
	}
	public void LoadButtonClicked(MouseEvent mouseEvent){
		field.loadFromCloud();
	}

	/**
	 * Mouse up handler.
	 *
	 * @param mouseEvent the mouse button you release up.
	 */
	final void mouseUpHandler(MouseEvent mouseEvent) {
		//mouseEvent.getButton(); // which mouse button?

        // Debug Check the class
        // System.out.println(currentEntityToAdd.getClass());
        // Screen coordinate. Remember to convert to field coordinate/
		long posX = (long) this.drawer.screenToFieldPosX(mouseEvent.getX()/1000);
		long posY = (long) this.drawer.screenToFieldPosY(mouseEvent.getY()/1000);
		/*System.out.println(posX);
		System.out.println(posY);*/
		boolean check = true;
		// check if it was mountain or not
		for(GameEntity entity: field.getEntities()){
			if(posX == entity.getPosX() && posY == entity.getPosY()){
				if(! (entity instanceof Mountain)) {
					check = false;
					break;
				}
			}
		}
		// Place that towerrrrrr
		if (check && currentEntityToAdd!=0){
		    // Add And Spawn Specify Tower
			if (this.currentEntityToAdd == 1)
				shop.buyTower(new NormalTower(field.getTickCount(), posX, posY));
			else if (this.currentEntityToAdd == 2)
				shop.buyTower(new MachineGunTower(field.getTickCount(), posX, posY));
			else if (this.currentEntityToAdd == 3)
				shop.buyTower(new SniperTower(field.getTickCount(), posX, posY));

			this.currentEntityToAdd = 0;
		}
	}

	public void mouseMoveHandler(MouseEvent mouseEvent) {
		mouseMovePosY = mouseEvent.getSceneY();
		mouseMovePosX = mouseEvent.getSceneX();
	}

	public void NormalTowerHover(MouseEvent mouseEvent) {
		mouseHover = 1;
	}

	public void NormalTowerExited(MouseEvent mouseEvent) {
		mouseHover = 0;
		System.out.println("hi");
	}

	public void SniperTowerHover(MouseEvent mouseEvent) {
		mouseHover = 3;
	}

	public void MachineGunTowerHover(MouseEvent mouseEvent) {
		mouseHover = 2;
	}

	public void setMouseHover() {
		mouseHover = 0;
	}
}
