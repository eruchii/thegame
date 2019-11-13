package mrmathami.thegame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mrmathami.thegame.entity.shop.MachineGunTowerButton;
import mrmathami.thegame.entity.shop.NormalTowerButton;
import mrmathami.thegame.entity.shop.SniperTowerButton;
import mrmathami.thegame.entity.tile.tower.AbstractTower;
import mrmathami.thegame.entity.tile.tower.NormalTower;
import mrmathami.thegame.entity.tile.tower.SniperTower;

import javax.annotation.Nonnull;

public class Shop {
    @Nonnull GameField field;
    @Nonnull GameController gameController;
    private double buttonWidth = 200;
    private double buttonHeigth = 95;

    public Shop(@Nonnull GameField field){
        this.field = field;

    }

    public final void buyTower(AbstractTower tower){
        if(field.getTarget().isDestroyed()) return;
        if(field.getMoney() - tower.getCost() < 0) return;
        field.getReward(-tower.getCost());
        field.addEntities(tower);
    }
    public Shop(Pane pane,GameController gameController){

        this.gameController = gameController;

        Image image = new Image("http://icons.iconarchive.com/icons/uiconstock/flat-halloween/128/Halloween-Bat-icon.png",70,130,true,true);
        ImageView normalTower = new ImageView (image);
        ImageView machineGunTower = new ImageView(image);
        ImageView sniperTower = new ImageView(image);

        // Init Button
        NormalTowerButton normalTowerButton = new NormalTowerButton(normalTower,100,Config.SCREEN_HEIGHT-buttonHeigth,buttonWidth,buttonHeigth,"Normal Tower");
        SniperTowerButton sniperTowerButton = new SniperTowerButton(sniperTower,400,Config.SCREEN_HEIGHT-buttonHeigth,buttonWidth,buttonHeigth,"Sniper Tower");
        MachineGunTowerButton machineGunTowerButton = new MachineGunTowerButton(machineGunTower,700,Config.SCREEN_HEIGHT-buttonHeigth,buttonWidth,buttonHeigth,"Machine Gun Tower");

        //Save and Load Button


        // Set Button Pressed
        normalTowerButton.getButton().setOnMousePressed(gameController::NormalTowerClicked);
        sniperTowerButton.getButton().setOnMousePressed(gameController::SniperTowerClicked);
        machineGunTowerButton.getButton().setOnMousePressed(gameController::MachineGunTowerClicked);

        // Adding the Button
        pane.getChildren().addAll(normalTowerButton.getButton(), machineGunTowerButton.getButton(), sniperTowerButton.getButton());
    }
}
