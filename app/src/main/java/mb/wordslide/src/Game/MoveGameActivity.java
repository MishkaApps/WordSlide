package mb.wordslide.src.Game;

import android.os.Bundle;

import mb.wordslide.src.Game.GameControl.GameController;
import mb.wordslide.src.Game.GameControl.ShiftCounter;
import mb.wordslide.src.Game.GameControl.ShiftListener;

/**
 * Created by mbolg on 01.09.2017.
 */

public class MoveGameActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShiftListener();
    }

    protected void setShiftListener() {
        animatedGameArea.setShiftListener((ShiftListener) gameController);
    }

    @Override
    protected GameController getConcreteGameOverController() {
        return new ShiftCounter();
    }
}
