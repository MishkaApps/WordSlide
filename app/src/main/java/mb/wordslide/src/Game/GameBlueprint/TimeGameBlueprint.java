package mb.wordslide.src.Game.GameBlueprint;

import android.content.Context;

import mb.wordslide.src.Configurations;

/**
 * Created by mbolg on 27.10.2017.
 */

public class TimeGameBlueprint extends GameBlueprint {
    public TimeGameBlueprint(Context context) {
        super(context);
    }

    @Override
    protected int getStartProgress() {
        return Configurations.START_GAME_TIME_IN_SECONDS;
    }


    @Override
    protected GameBlueprint.GameType getGameType() {
        return GameType.TIME;
    }
}
