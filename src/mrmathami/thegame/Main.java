package mrmathami.thegame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mrmathami.thegame.entity.tile.tower.SniperTower;

/**
 * Main class. Entry point of the game.
 */
public final class Main extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		final Canvas canvas = new Canvas(Config.SCREEN_WIDTH+150, Config.SCREEN_HEIGHT);
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		final GameController gameController = new GameController(graphicsContext);

		// Setting up the Playscreen include Shop Pane
		Group group = new Group();
		canvas.setFocusTraversable(true);
		graphicsContext.setFontSmoothingType(FontSmoothingType.LCD);
		// keyboard and mouse events to catch. Add if you need more//

		//TESTING AREAAAAA

		Image image = new Image("http://icons.iconarchive.com/icons/uiconstock/flat-halloween/128/Halloween-Bat-icon.png");
		Pane shopPane = new Pane();
		// Normal Tower Area
		ImageView normalTower = new ImageView (image);
		ImageView machineGunTower = new ImageView(image);
		ImageView sniperTower = new ImageView(image);
		// normal Tower Setting
		normalTower.setFitHeight(150);
		normalTower.setFitWidth(150);
		normalTower.setX(Config.SCREEN_WIDTH);
		normalTower.setY(25);
		// Sniper Tower Settings
		sniperTower.setX(Config.SCREEN_WIDTH);
		sniperTower.setY(395);
		sniperTower.setFitHeight(150);
		sniperTower.setFitWidth(150);
		// Machine gun tower Setting
		machineGunTower.setFitWidth(150);
		machineGunTower.setFitHeight(150);
		machineGunTower.setX(Config.SCREEN_WIDTH);
		machineGunTower.setY(210);

		// Background for Shop
		graphicsContext.setFill(Color.YELLOW);
		graphicsContext.fillRect(Config.SCREEN_WIDTH,0,1200,Config.SCREEN_HEIGHT);

		// Adding all the item
		shopPane.getChildren().addAll(normalTower,machineGunTower,sniperTower);
		// Mouse Listener

		group.setOnMousePressed(gameController::mouseDownHandler);
		group.setOnMouseReleased(gameController::mouseUpHandler);
//		shopPane.setOnMousePressed(gameController::mouseDownHandler);
//		shopPane.setOnMouseReleased(gameController::mouseUpHandler);
//		canvas.setOnMouseReleased(gameController::mouseUpHandler);
//		canvas.setOnMouseClicked(...);
//		canvas.setOnMouseMoved(...);
//		Adding all the element
		group.getChildren().addAll(canvas,shopPane);
//		Screen Setting
		primaryStage.setMinWidth(Config.SCREEN_WIDTH+200);
		primaryStage.setResizable(true);
		primaryStage.setTitle(Config.GAME_NAME);
		primaryStage.setOnCloseRequest(gameController::closeRequestHandler);
		primaryStage.setScene(new Scene(group,Config.SCREEN_WIDTH,Config.SCREEN_HEIGHT));
		primaryStage.show();


		gameController.start();
	}
}