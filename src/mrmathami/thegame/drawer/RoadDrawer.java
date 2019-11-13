package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.tile.Road;

import javax.annotation.Nonnull;

public final class RoadDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {
		// Path
		String path = String.valueOf(this.getClass().getResource("/grass.png"));
		double scale = 1;
		double newW = Config.TILE_SIZE * scale;
		double newH =  Config.TILE_SIZE * scale;
		Image content = new Image(path, newW, newH, false, false);
		graphicsContext.drawImage(content, screenPosX, screenPosY);
	}
}
