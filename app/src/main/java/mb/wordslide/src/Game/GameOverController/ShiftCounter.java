package mb.wordslide.src.Game;

import android.content.Context;

import java.util.ArrayList;

import mb.wordslide.src.Game.GameOverController.GameFinishController;

/**
 * Created by mbolg on 31.08.2017.
 */

public class ShiftCounter extends android.support.v7.widget.AppCompatTextView implements GameFinishController {
    private final int START_ALLOWED_SHIFT_NUMBER = 30;
    private int remainingShifts;
    private ArrayList<OnGameOverListener> gameEndsListeners;

    public ShiftCounter(Context context) {
        super(context);
        remainingShifts = START_ALLOWED_SHIFT_NUMBER;
        gameEndsListeners = new ArrayList<>();
        updateView();
    }

    @Override
    public void shifted() {
        decreaseRemainingShifts();
        updateView();
        checkForEnd();
    }

    @Override
    public void addBonus(int bonus) {
        remainingShifts += bonus;
    }

    @Override
    public void notifyGameOver() {
        for(OnGameOverListener gameEndsListener: gameEndsListeners)
            gameEndsListener.gameEnds();
    }


    private void decreaseRemainingShifts() {
        --remainingShifts;
    }

    private void updateView() {
        setText(Integer.toString(remainingShifts));
    }

    private void checkForEnd() {
        if(remainingShifts == 0)
            notifyGameOver();
    }

    @Override
    public void addGameOverListener(OnGameOverListener gameEndsListener){
        gameEndsListeners.add(gameEndsListener);
    }
}
