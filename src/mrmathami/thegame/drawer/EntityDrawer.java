package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import mrmathami.thegame.entity.GameEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;

public interface EntityDrawer {
	void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity,
			  double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom, HashMap<String, Image> cacheImg);
}
