package mrmathami.thegame;

import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.tile.tower.AbstractTower;
import mrmathami.thegame.entity.tile.tower.MachineGunTower;
import mrmathami.thegame.entity.tile.tower.NormalTower;
import mrmathami.thegame.entity.tile.tower.SniperTower;

import javax.annotation.Nonnull;

public class Shop {
    @Nonnull GameField field;

    public Shop(@Nonnull GameField field){
        this.field = field;
    }

    public final void buyTower(AbstractTower tower){
        if(field.getMoney() - tower.getCost() < 0) return;
        field.getReward(-tower.getCost());
        field.addEntities(tower);
    }


}
