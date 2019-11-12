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

public final class MouseDrawer implements EntityDrawer {
    @Override
    public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {


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
