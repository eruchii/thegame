package mrmathami.thegame.drawer;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.enemy.AbstractEnemy;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class NormalEnemyDrawer implements EntityDrawer {
	@Override
	public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {
        // HP bar
		double percent = 1.0 * ((AbstractEnemy) entity).getHealth() / ((AbstractEnemy) entity).getMaxHealth();
		graphicsContext.setFill(Color.RED);
		graphicsContext.fillRect(screenPosX - 5, screenPosY - 8, (screenWidth + 10)*percent, 5);
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.strokeRect(screenPosX - 5, screenPosY - 8, screenWidth + 10, 5);

//		Color c = animated.get(((AbstractEnemy) entity).getLT() % animated.size());
//		graphicsContext.setFill(c);
//		graphicsContext.fillRoundRect(screenPosX, screenPosY, screenWidth, screenHeight, 4, 4);

		List<String> animated = new ArrayList<>();
		if(((AbstractEnemy)entity).isSlow()) {
			animated.add("/PNG/Levynade/0.png");
			animated.add("/PNG/Levynade/1.png");
			animated.add("/PNG/Levynade/2.png");
			animated.add("/PNG/Levynade/3.png");
			animated.add("/PNG/Levynade/4.png");
			animated.add("/PNG/Levynade/5.png");
			animated.add("/PNG/Levynade/6.png");
			animated.add("/PNG/Levynade/7.png");
		} else {
			animated.add("/PNG/Levynade/8.png");
			animated.add("/PNG/Levynade/9.png");
			animated.add("/PNG/Levynade/10.png");
			animated.add("/PNG/Levynade/11.png");
			animated.add("/PNG/Levynade/11.png");
			animated.add("/PNG/Levynade/12.png");
			animated.add("/PNG/Levynade/13.png");
			animated.add("/PNG/Levynade/14.png");
			animated.add("/PNG/Levynade/15.png");
		}

		String url = animated.get(((AbstractEnemy) entity).getLT() % animated.size());
		String path = String.valueOf(this.getClass().getResource(url));
		double newW = Config.NORMAL_ENEMY_SIZE * Config.TILE_SIZE ;
		double newH = Config.NORMAL_ENEMY_SIZE * Config.TILE_SIZE ;
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
