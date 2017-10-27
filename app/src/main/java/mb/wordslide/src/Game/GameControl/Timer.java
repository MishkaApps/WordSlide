package mb.wordslide.src.Game.GameControl;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 01.09.2017.
 */

public class Timer extends GameController {
    private final int THOUSAND_MILLISECONDS = 1000;
    private CountDownTimer timer;
    private GameOverListener gameOverListener;
    private long timeToFinishInMillis;
    private boolean paused;
    private ProgressBar progressBar;

    public Timer(GameBlueprint gameBlueprint) {
        super(gameBlueprint);
        start();
    }


    public void createTimer() {

        timer = new CountDownTimer(timeToFinishInMillis, THOUSAND_MILLISECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeToFinishInMillis = millisUntilFinished;
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                notifyGameOverListener();
            }
        };
    }

    public void start() {
        createTimer();
        timer.start();
        paused = false;
    }

    public void pause() {
        timer.cancel();
        paused = true;
    }

    private void resetTime() {
        timeToFinishInMillis = THOUSAND_MILLISECONDS * Configurations.START_GAME_TIME_IN_SECONDS;
    }

    @Override
    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    @Override
    public void notifyGameOverListener() {
        gameOverListener.gameOver();
    }

    @Override
    public void addBonus(int bonus) {
        addSeconds(bonus);
    }

    @Override
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setProgress(Configurations.START_GAME_TIME_IN_SECONDS);
        progressBar.setMax(Configurations.MAX_GAME_TIME_IN_SECONDS);
        updateProgressBar();
    }

    @Override
    public void updateProgressBar() {
        L.l("saved time: " + getTimeToFinishInSeconds());
        progressBar.setProgress(getTimeToFinishInSeconds());
        updateGameBlueprintProgress();
    }

    private int getTimeToFinishInSeconds() {
        return (int) (timeToFinishInMillis / THOUSAND_MILLISECONDS);
    }

    public void resume() {
        timer.start();
        paused = false;
    }

    public void addSeconds(int seconds) {
        timer.cancel();
        seconds *= Configurations.TIME_BONUS_MULTIPLIER;
        timeToFinishInMillis += THOUSAND_MILLISECONDS * seconds;
        createTimer();
        start();
    }

    @Override
    public void setGameBlueprint(GameBlueprint gameBlueprint) {
        this.gameBlueprint = gameBlueprint;

    }

    @Override
    public void updateGameBlueprintProgress() {
        gameBlueprint.setProgress(getTimeToFinishInSeconds());
    }

    @Override
    void setProgressFromGameBlueprint(int progress) {
        timeToFinishInMillis = THOUSAND_MILLISECONDS * progress;
    }
}
