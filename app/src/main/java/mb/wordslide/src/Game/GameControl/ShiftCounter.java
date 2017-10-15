package mb.wordslide.src.Game.GameControl;

import android.widget.ProgressBar;

import mb.wordslide.src.Configurations;

/**
 * Created by mbolg on 31.08.2017.
 */

public class ShiftCounter implements GameController, ShiftListener {
    private int remainingShifts;
    private GameOverListener gameOverListener;
    private ProgressBar progressBar;

    public ShiftCounter() {
        remainingShifts = Configurations.START_MOVES_NUMBER;
    }

    @Override
    public void shifted() {
        decreaseRemainingShifts();
        updateProgressBar();
        checkForGameOver();
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
}
