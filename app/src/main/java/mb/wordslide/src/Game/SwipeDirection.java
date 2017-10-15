package mb.wordslide.src.Game;

/**
 * Created by mbolg on 27.07.2017.
 */
public class SwipeDirection {
    private Direction direction;

    public SwipeDirection(){
        direction = Direction.NONE;
    }

    public boolean isHorizontal(){
        if(direction == Direction.LEFT || direction == Direction.RIGHT)
            return true;
        else return false;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }
}
