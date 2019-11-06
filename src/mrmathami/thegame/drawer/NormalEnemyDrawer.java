package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.enemy.AbstractEnemy;

import javax.annotation.Nonnull;

public final class NormalEnemyDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {
		double percent = 1.0 * ((AbstractEnemy) entity).getHealth() / ((AbstractEnemy) entity).getMaxHealth();
		graphicsContext.setFill(Color.RED);
		graphicsContext.fillRect(screenPosX - 5, screenPosY - 8, (screenWidth + 10)*percent, 5);
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.strokeRect(screenPosX - 5, screenPosY - 8, screenWidth + 10, 5);

		graphicsContext.setFill(Color.DARKMAGENTA);
		graphicsContext.fillRoundRect(screenPosX, screenPosY, screenWidth, screenHeight, 4, 4);

//		String path = String.valueOf(this.getClass().getResource("/PNG/towerDefense_tile245.png"));
//		double scale = 1;
//		double newW = Config.NORMAL_ENEMY_SIZE * Config.TILE_SIZE * scale;
//		double newH = Config.NORMAL_ENEMY_SIZE * Config.TILE_SIZE * scale;
//		Image content = new Image(path, newW, newH, false, false);
//		graphicsContext.drawImage(content, screenPosX, screenPosY);
	}
}
