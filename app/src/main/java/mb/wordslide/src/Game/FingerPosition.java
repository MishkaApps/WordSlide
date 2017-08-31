package mb.wordslide.src.Game;

/**
 * Created by mbolg on 27.07.2017.
 */
public class FingerPosition {
    float x;
    float y;

    public FingerPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FingerPosition copy() {
        return new FingerPosition(x, y);
    }
}
