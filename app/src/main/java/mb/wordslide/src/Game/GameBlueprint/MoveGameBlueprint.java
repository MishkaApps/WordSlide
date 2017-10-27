package mb.wordslide.src.Game.GameBlueprint;

import android.content.Context;

import mb.wordslide.src.Configurations;

/**
 * Created by mbolg on 27.10.2017.
 */

public class MoveGameBlueprint extends GameBlueprint  {
    public MoveGameBlueprint(Context context) {
        super(context);
    }

    @Override
    protected int getStartProgress() {
        return Configurations.START_MOVES_NUMBER;
    }

    @Override
    protected GameBlueprint.GameType getGameType() {
        return GameType.MOVES;
    }
}
