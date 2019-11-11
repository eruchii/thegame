package mrmathami.thegame.entity.shop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MachineGunTowerButton extends AbstractButton {
    public MachineGunTowerButton(ImageView imageView, double screenPosX, double screenPosY,
                             double buttonWidth, double buttonHeight, String string) {
        super(imageView, screenPosX, screenPosY, buttonWidth, buttonHeight, string);
        //this.getButton().setOnMousePressed(gameController::MachineGunTowerClicked);
    }

    @Override
    public Button getButton() {
        return super.getButton();
    }
}
