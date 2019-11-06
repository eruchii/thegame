package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.enemy.AbstractEnemy;

import javax.annotation.Nonnull;

public final class TankerEnemyDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {
		double percent = 1.0 * ((AbstractEnemy) entity).getHealth() / ((AbstractEnemy) entity).getMaxHealth();
		graphicsContext.setFill(Color.RED);
		graphicsContext.fillRect(screenPosX - 5, screenPosY - 8, (screenWidth + 10)*percent, 5);
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.strokeRect(screenPosX - 5, screenPosY - 8, screenWidth + 10, 5);

		graphicsContext.setFill(Color.VIOLET);
		graphicsContext.fillRoundRect(screenPosX, screenPosY, screenWidth, screenHeight, 4, 4);
	}
}
