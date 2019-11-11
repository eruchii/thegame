package mrmathami.thegame.entity.shop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import mrmathami.thegame.GameController;

public abstract class AbstractButton extends Button{
    protected Button button;
    protected GameController gameController;
    public  AbstractButton(ImageView imageView,double screenPosX,double screenPosY, double buttonWidth, double buttonHeight,String Tower){
        this.button = new Button(Tower,imageView);
        this.button.setLayoutX(screenPosX);
        this.button.setLayoutY(screenPosY);
        this.button.setMinSize(buttonWidth,buttonHeight);
    }
    public AbstractButton(double screenPosX,double screenPosY, double buttonWidth, double buttonHeight,String Tower){
        this.button = new Button(Tower);
        this.button.setLayoutX(screenPosX);
        this.button.setLayoutY(screenPosY);
        this.button.setMinSize(buttonWidth,buttonHeight);
    }

    public Button getButton() {
        return this.button;
    }
}