package mrmathami.thegame.drawer;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.LivingEntity;
import mrmathami.thegame.entity.enemy.AbstractEnemy;
import mrmathami.thegame.entity.enemy.SmallerEnemy;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class SmallerEnemyDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount,
					 @Nonnull GraphicsContext graphicsContext,
					 @Nonnull GameEntity entity,
					 double screenPosX,
					 double screenPosY,
					 double screenWidth,
					 double screenHeight,
					 double zoom) {
		double percent = 1.0 * ((AbstractEnemy) entity).getHealth() / ((AbstractEnemy) entity).getMaxHealth();


		graphicsContext.setFill(Color.RED);
		graphicsContext.fillRect(screenPosX - 5, screenPosY - 8, (screenWidth + 10)*percent, 5);
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.strokeRect(screenPosX - 5, screenPosY - 8, screenWidth + 10, 5);

//		graphicsContext.setFill(Color.MAGENTA);
//		graphicsContext.fillRoundRect(screenPosX, screenPosY, screenWidth, screenHeight, 4, 4);
		List<String> animated = new ArrayList<>();
		animated.add("/PNG/Skeleton/1.png");
		animated.add("/PNG/Skeleton/1.png");
		animated.add("/PNG/Skeleton/2.png");
		animated.add("/PNG/Skeleton/2.png");
		animated.add("/PNG/Skeleton/3.png");
		animated.add("/PNG/Skeleton/3.png");
		animated.add("/PNG/Skeleton/4.png");
		animated.add("/PNG/Skeleton/4.png");

		String url = animated.get(((AbstractEnemy) entity).getLT() % animated.size());
		String path = String.valueOf(this.getClass().getResource(url));
		double newW = Config.SMALLER_ENEMY_SIZE * Config.TILE_SIZE ;
		double newH = Config.SMALLER_ENEMY_SIZE * Config.TILE_SIZE ;
		double outputW = newW;
		double outputH = newH;
		double outX = screenPosX;
		double outY = screenPosY;
		double directionX = ((AbstractEnemy) entity).getDirectionX();
		double directionY = ((AbstractEnemy) entity).getDirectionY();
		double SQRT_2 = Math.sqrt(2.0) / 2.0;

		ImageView iv = new ImageView(new Image(path, newW, newH, false, false));
		if((directionX == 1.0 && directionY == 0.0) ||
				(directionX == SQRT_2 && directionY == SQRT_2)) iv.setRotate(0);
		else if((directionX == -1.0 && directionY == 0.0) ||
				(directionX == -SQRT_2 && directionY == SQRT_2)){
			outputW = -newW;
			outX += newW;
		}
		else if((directionX == 0.0 && directionY == 1.0) ||
				directionX == -SQRT_2 && directionY == -SQRT_2) iv.setRotate(90);
		else if((directionX == 0.0 && directionY == -1.0) ||
				directionX == SQRT_2 && directionY == -SQRT_2){
			outputH = -newH;
			outY += newH;
		}


		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		Image content = iv.snapshot(params, null);

		graphicsContext.drawImage(content, outX, outY, outputW, outputH );
	}
}
