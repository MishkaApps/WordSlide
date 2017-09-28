package mb.wordslide.src.Game;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import java.util.ArrayList;

import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.GameOverController.GameFinishController;

/**
 * Created by mbolg on 01.09.2017.
 */

public class Timer extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener, GameFinishController {
    private CountDownTimer timer;
    private ArrayList<OnGameOverListener> gameOverListeners;
    private int timeToFinish;
    private boolean paused;

    public Timer(Context context) {
        super(context);
        gameOverListeners = new ArrayList<>();
        setOnClickListener(this);
        resetTime();
        updateTimeView();
        start();
    }

    public void createTimer() {
        final int THOUSAND_MILLISECONDS = 1000;

        timer = new CountDownTimer(timeToFinish * THOUSAND_MILLISECONDS, THOUSAND_MILLISECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeToFinish = (int) millisUntilFinished / 1000;
                updateTimeView();
            }

            @Override
            public void onFinish() {
                notifyTimerFinished();
            }
        };
    }

    private void notifyTimerFinished() {
        for(OnGameOverListener gameOverListener: gameOverListeners)
            gameOverListener.gameEnds();
    }

    private void updateTimeView() {
        setText(Integer.toString(timeToFinish));
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

    public void reset() {
        timer.cancel();
        resetTime();
        updateTimeView();
    }

    private void resetTime() {
        timeToFinish = Configurations.GAME_TIME_IN_SECONDS;
    }

    @Override
    public void onClick(View v) {
        if(v == this) {
            if(!paused) {
                pause();
            } else {
                start();
            }
        }
    }

    @Override
    public void shifted() {

    }

    @Override
    public void addBonus(int bonus) {
        addSeconds(bonus);
    }

    @Override
    public void notifyGameOver() {

    }

    @Override
    public void addGameOverListener(OnGameOverListener gameOverListener) {

    }

    public interface TimerEventListener {
        void onTimerFinished();
        void onTimerPaused();
    }

    public void addSeconds(int seconds){
        timer.cancel();
        timeToFinish += seconds;
        createTimer();
        start();
    }
}
