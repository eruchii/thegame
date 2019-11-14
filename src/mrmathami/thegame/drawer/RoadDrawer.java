package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;

public final class RoadDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom, HashMap<String, Image> cacheImg) {
//		graphicsContext.setFill(Color.LIGHTGREEN);
//		graphicsContext.fillRect(screenPosX, screenPosY, screenWidth, screenHeight);
		//		// Path
		Image content = cacheImg.get("/PNG/Grass.png");
		if(content == null) {
			String path = String.valueOf(this.getClass().getResource("/PNG/Grass.png"));
			double newW = Config.TILE_SIZE;
			double newH = Config.TILE_SIZE;
			content = new Image(path, newW, newH, false, true);
			cacheImg.put("/PNG/Grass.png", content);
		}
		graphicsContext.drawImage(content, screenPosX, screenPosY);
	}
}
