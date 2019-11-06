package mrmathami.thegame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Stage;

/**
 * Main class. Entry point of the game.
 */
public final class Main extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		final Canvas canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		final GameController gameController = new GameController(graphicsContext);

		canvas.setFocusTraversable(true);
		graphicsContext.setFontSmoothingType(FontSmoothingType.LCD);
		//graphicsContext.drawImage(new Image("flag.png"),3,4);

		// keyboard and mouse events to catch. Add if you need more
		canvas.setOnKeyPressed(gameController::keyDownHandler);
		canvas.setOnKeyReleased(gameController::keyUpHandler);
//		canvas.setOnKeyTyped(...);

<<<<<<< HEAD
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
=======
		canvas.setOnMousePressed(gameController::mouseDownHandler);
		canvas.setOnMouseReleased(gameController::mouseUpHandler);
>>>>>>> parent of 9a142d7... Update the shop and Place tower function
//		canvas.setOnMouseClicked(...);
//		canvas.setOnMouseMoved(...);


		primaryStage.setResizable(false);
		primaryStage.setTitle(Config.GAME_NAME);
		primaryStage.setOnCloseRequest(gameController::closeRequestHandler);
		primaryStage.setScene(new Scene(new StackPane(canvas)));
		primaryStage.show();

		gameController.start();
	}
}