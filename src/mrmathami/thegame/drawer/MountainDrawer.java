package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;

public final class MountainDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext,
					 @Nonnull GameEntity entity, double screenPosX, double screenPosY,
					 double screenWidth, double screenHeight, double zoom, HashMap<String, Image> cacheImg) {
//		graphicsContext.setFill(Color.DARKGREEN);
//		graphicsContext.fillRect(screenPosX, screenPosY, screenWidth, screenHeight);
//		// Draw the Road
		Image content = cacheImg.get("/BrickTile.png");
		if(content == null) {
			String path = String.valueOf(this.getClass().getResource("/BrickTile.png"));
			double newW = Config.TILE_SIZE;
			double newH = Config.TILE_SIZE;
			content = new Image(path, newW, newH, false, true);
			cacheImg.put("/BrickTile.png", content);
		}
		graphicsContext.drawImage(content, screenPosX, screenPosY);
	}
}
