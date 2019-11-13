package mrmathami.thegame.entity.shop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import mrmathami.thegame.GameController;
import mrmathami.thegame.entity.tile.tower.NormalTower;

public class NormalTowerButton extends AbstractButton {
    public NormalTowerButton(ImageView imageView, double screenPosX, double screenPosY,
                             double buttonWidth, double buttonHeight, String string, GameController gameController) {
        super(imageView, screenPosX, screenPosY, buttonWidth, buttonHeight, string,gameController);
    }

    @Override
    public Button getButton() {
        return super.getButton();
    }
}