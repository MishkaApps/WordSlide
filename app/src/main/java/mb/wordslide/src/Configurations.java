package mb.wordslide.src;

import mb.wordslide.src.Game.ConcreteGameActivities.TimeGameActivity;

/**
 * Created by mbolg on 21.09.2017.
 */

public class Configurations {
    public static final int START_GAME_TIME_IN_SECONDS = 30;
    public static final int MAX_GAME_TIME_IN_SECONDS = 60;
    public static final int START_MOVES_NUMBER = 30;
    public static final int MAX_MOVES_NUMBER = 60;
    
    public static final int TIME_BONUS_MULTIPLIER = 1;
    public static final int MOVES_BONUS_MULTIPLIER = 1;
    public static final Class<?> GAME_CLASS = TimeGameActivity.class;
//    public static final Class<?> GAME_CLASS = MoveGameActivity.class;
    public static final boolean CHECK_WORDS = true;
    public static final int FIELDS_SIZE = 150;
    public static final int GAME_AREA_DIMENSION = 6;

}
