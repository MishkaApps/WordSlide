package mb.wordslide.src.Game.GameControl;

import android.widget.ProgressBar;

import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;

/**
 * Created by mbolg on 14.10.2017.
 */

public abstract class GameController {
    protected GameBlueprint gameBlueprint;

    public GameController(GameBlueprint gameBlueprint) {
        this.gameBlueprint = gameBlueprint;
        setProgressFromGameBlueprint(gameBlueprint.getProgress());
    }

    abstract void setGameOverListener(GameOverListener gameOverListener);
    abstract void notifyGameOverListener();
    public abstract void addBonus(int bonus);
    abstract void setProgressBar(ProgressBar progressBar);
    abstract void updateProgressBar();
    abstract void setGameBlueprint(GameBlueprint gameBlueprint);
    abstract void updateGameBlueprintProgress();
    abstract void setProgressFromGameBlueprint(int progress);
}
