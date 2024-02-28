package dungeonmania.entities.playerState;

import dungeonmania.entities.Buffing;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.*;

public abstract class PlayerState implements Buffing {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void transition(Potion potion) {
        if (potion == null) {
            player.changeState(new BaseState(player));
        } else if (potion instanceof InvincibilityPotion) {
            player.changeState(new InvincibleState(player));
        } else if (potion instanceof InvisibilityPotion) {
            player.changeState(new InvisibleState(player));
        }
    }
}
