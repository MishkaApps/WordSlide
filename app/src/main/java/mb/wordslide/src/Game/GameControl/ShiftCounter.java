package mb.wordslide.src.Game.GameControl;

import android.widget.ProgressBar;

import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;

/**
 * Created by mbolg on 31.08.2017.
 */

public class ShiftCounter extends GameController implements ShiftListener {
    private int remainingShifts;
    private GameOverListener gameOverListener;
    private ProgressBar progressBar;

    public ShiftCounter(GameBlueprint gameBlueprint) {
        super(gameBlueprint);
    }

    @Override
    public void shifted() {
        decreaseRemainingShifts();
        updateProgressBar();
        checkForGameOver();
        updateGameBlueprintProgress();
    }

    @Override
    public void addBonus(int bonus) {
        bonus *= Configurations.MOVES_BONUS_MULTIPLIER;
        remainingShifts += bonus;
        updateProgressBar();
    }


    @Override
    public void notifyGameOverListener() {
        gameOverListener.gameOver();
    }

    private void decreaseRemainingShifts() {
        --remainingShifts;
    }


    private void checkForGameOver() {
        if (remainingShifts == 0)
            notifyGameOverListener();
    }

    @Override
    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    @Override
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setProgress(Configurations.START_MOVES_NUMBER);
        progressBar.setMax(Configurations.MAX_MOVES_NUMBER);
        updateProgressBar();
    }

    @Override
    public void updateProgressBar() {
        progressBar.setProgress(remainingShifts);
    }

    @Override
    public void setGameBlueprint(GameBlueprint gameBlueprint) {
        this.gameBlueprint = gameBlueprint;
    }

    @Override
    public void updateGameBlueprintProgress() {
        gameBlueprint.setProgress(remainingShifts);
    }

    @Override
    void setProgressFromGameBlueprint(int progress) {
        remainingShifts = progress;
    }

}
