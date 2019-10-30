package mrmathami.thegame.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.bullet.AbstractBullet;
import mrmathami.thegame.entity.bullet.NormalBullet;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

import javax.annotation.Nonnull;

public final class NormalBulletDrawer implements EntityDrawer {
	private final RadialGradient gradient = new RadialGradient(
			0.0,
			0.0,
			0.5,
			0.5,
			1.0,
			true,
			CycleMethod.NO_CYCLE,
			new Stop(0.0, Color.YELLOW),
			new Stop(0.5, Color.RED),
			new Stop(1.0, Color.WHITE)
	);

	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {
		graphicsContext.setFill(gradient);
		graphicsContext.fillOval(screenPosX, screenPosY, screenWidth, screenHeight);
//		AbstractBullet bullet = (AbstractBullet) entity;
//		AbstractTower tower = bullet.getTower();
//		double startX = (tower.getPosX() + tower.getWidth())*Config.TILE_SIZE;
//		double startY = (tower.getPosY() + tower.getHeight())*Config.TILE_SIZE;
//		double endX = (bullet.getPosX() + bullet.getWidth())*Config.TILE_SIZE;
//		double endY = (bullet.getPosY() + bullet.getHeight())*Config.TILE_SIZE;
//		graphicsContext.setStroke(Color.BLUE);
//		graphicsContext.setLineWidth(2);
//		graphicsContext.strokeLine(startX, startY, endX, endY);
	}
}
