package mb.wordslide.src.Game.ConcreteGameActivities;

import android.os.Bundle;

import mb.wordslide.src.Game.GameActivity;
import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;
import mb.wordslide.src.Game.GameBlueprint.MoveGameBlueprint;
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



    @Override
    protected GameBlueprint getNewConcreteGameBlueprint() {
        return new MoveGameBlueprint(this);
    }

    @Override
    protected Class<? extends GameBlueprint> getConcreteGameBlueprintClass() {
        return MoveGameBlueprint.class;
    }

    protected void setShiftListener() {
        gameArea.setShiftListener((ShiftListener) gameController);
    }

    @Override
    protected GameController getConcreteGameOverController() {
        return new ShiftCounter(gameBlueprint);
    }
}
