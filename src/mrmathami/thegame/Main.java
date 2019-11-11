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
import javafx.scene.input.MouseEvent;
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
		final Canvas canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		final GameController gameController = new GameController(graphicsContext);

		// Setting up the Playscreen include Shop Pane
		Group group = new Group();
		canvas.setFocusTraversable(true);
		graphicsContext.setFontSmoothingType(FontSmoothingType.LCD);
		// keyboard and mouse events to catch. Add if you need more//

		//TESTING AREAAAAA

		// Creating the shop menu
		Pane shopPane = new Pane();
		// Init the shop
		final Shop shop = new Shop(shopPane,gameController);

		// Save Button
		Button saveButton = new Button("Save");
		saveButton.setLayoutX(Config.SCREEN_WIDTH-40);
		saveButton.setLayoutY(Config.SCREEN_HEIGHT-80);
		saveButton.setOnMouseClicked(gameController::SaveButtonClicked);

		//Load Button
		Button loadButton = new Button("Load");
		loadButton.setLayoutX(Config.SCREEN_WIDTH-40);
		loadButton.setLayoutY(Config.SCREEN_HEIGHT-50);
		loadButton.setOnMouseClicked(gameController::LoadButtonClicked);

		// Tower Placed Action
		group.setOnMouseReleased(gameController::mouseUpHandler);

		//	Adding all the element
		group.getChildren().addAll(canvas,shopPane,saveButton,loadButton);

		//	Screen Setting
		primaryStage.setMinWidth(Config.SCREEN_WIDTH);
		primaryStage.setResizable(true);
		primaryStage.setTitle(Config.GAME_NAME);
		primaryStage.setOnCloseRequest(gameController::closeRequestHandler);
		primaryStage.setScene(new Scene(group,Config.SCREEN_WIDTH,Config.SCREEN_HEIGHT));
		primaryStage.show();

		// Start the Game
		gameController.start();
	}
}