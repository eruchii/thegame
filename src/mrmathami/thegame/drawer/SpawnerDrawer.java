package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mrmathami.thegame.entity.GameEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;

public final class SpawnerDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom, HashMap<String, Image> cacheImg) {
		graphicsContext.setStroke(Color.DARKBLUE);
		graphicsContext.setLineWidth(4);
		graphicsContext.strokeRect(screenPosX, screenPosY, screenWidth, screenHeight);
	}
}
