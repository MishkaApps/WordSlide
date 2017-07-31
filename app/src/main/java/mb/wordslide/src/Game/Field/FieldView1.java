package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mbolg on 30.07.2017.
 */
public abstract class FieldView1 extends TextView implements FieldLogic{
    protected float distanceToFingerX, distanceToFingerY;
    private float initialPositionX, initialPositionY;
    private int row, col;
    private char letter;

    public FieldView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void saveInitialPosition(){
        initialPositionX = getX();
        initialPositionY = getY();
    }

    public float getPositionX() {
        int[] pos = {0, 0};
        getLocationOnScreen(pos);
        return (float) pos[0];
    }

    public float getPositionY() {
        int[] pos = {0, 0};
        getLocationOnScreen(pos);
        return pos[1];
    }

    public float getAbsolutePositionX(){
        return getX();
    }


    public void setDistanceToFinger(float x, float y) {
        this.distanceToFingerX = x;
        this.distanceToFingerY = y;
    }

    public float getInitialPositionX() {
        return initialPositionX;
    }

    public void resetPositionToInitial() {
        animate().x(initialPositionX).setDuration(0);
        animate().y(initialPositionY).setDuration(0);
    }

    public void setLetter(char letter) {
        this.letter = letter;
        setText("" + letter);
    }
    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }
    public char getLetter() {
        return letter;
    }
    abstract public void moveLeft(float progress, float fingerPositionX);
    @Override
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
