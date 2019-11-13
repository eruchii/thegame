package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mrmathami.thegame.Config;
import mrmathami.thegame.drawer.EntityDrawer;
import mrmathami.thegame.entity.GameEntity;

import javax.annotation.Nonnull;

public final class MountainDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {

		// Draw the Mountain
		String path = String.valueOf(this.getClass().getResource("/BrickTile.png"));
		double scale = 1;
		double newW = Config.TILE_SIZE * scale;
		double newH =  Config.TILE_SIZE * scale;
		Image content = new Image(path, newW, newH, false, false);
		graphicsContext.drawImage(content, screenPosX, screenPosY);
	}
}
