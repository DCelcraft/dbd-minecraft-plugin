package fr.cel.dbdplugin.manager;

import org.bukkit.Bukkit;

import fr.cel.dbdplugin.DBDPlugin;
import fr.cel.dbdplugin.utils.ItemKiller;
import fr.cel.dbdplugin.utils.timer.GameRunnable;
import fr.cel.dbdplugin.utils.timer.LobbyRunnable;
import net.kyori.adventure.text.Component;

public class GameManager {

    private final DBDPlugin main;
    private GameState gameState = GameState.WAITING;

    private final PlayerManager playerManager;
    private final ItemKiller itemKiller;

    private LobbyRunnable lobbyRunnable;
    private GameRunnable gameRunnable;

    public GameManager(DBDPlugin main) {
        this.main = main;
        this.playerManager = new PlayerManager(this);
        this.itemKiller = new ItemKiller();
    }
    
    public void setGameState(GameState gameState) {
        // if (this.gameState == GameState.WAITING && gameState == GameState.STARTING) return;
        // if (this.gameState == gameState) return;

        this.gameState = gameState;

        switch (gameState) {
            case WAITING:
            Bukkit.broadcast(Component.text("Waiting !"));

            this.lobbyRunnable = new LobbyRunnable(main, this);
            this.lobbyRunnable.runTaskTimer(main, 0, 20);
            break;

            case STARTING:
            if (this.lobbyRunnable != null) this.lobbyRunnable.cancel();
            
            Bukkit.broadcast(Component.text("Starting !"));

            getPlayerManager().clearPlayers();
            getPlayerManager().healPlayers();
            getPlayerManager().teleportPlayers();
            // faire apparaître les générateurs
            this.setGameState(GameState.GAME);

            break;

            case GAME:
            Bukkit.broadcast(Component.text("Game !"));
            
            this.gameRunnable = new GameRunnable();
            this.gameRunnable.runTaskTimer(main, 0, 20);
            break;

            case END:
            Bukkit.broadcast(Component.text("End !"));
            break;
        }
    }

    public boolean isGameState(GameState status) {
        return this.gameState == status;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ItemKiller getItemKiller() {
        return itemKiller;
    }

}