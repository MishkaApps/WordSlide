package mb.wordslide.src.Game;

import mb.wordslide.src.L;

/**
 * Created by mbolg on 27.07.2017.
 */
public class FingerPosition {
    private float x;
    private float y;

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y){
        setX(x);
        setY(y);
    }
}
