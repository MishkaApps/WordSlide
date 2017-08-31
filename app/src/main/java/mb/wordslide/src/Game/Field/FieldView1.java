package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import mb.wordslide.R;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 30.07.2017.
 */
public abstract class FieldView1 extends android.support.v7.widget.AppCompatTextView implements FieldLogic {
    protected float distanceToFingerX, distanceToFingerY;
    protected float initialPositionX, initialPositionY;
    protected int row, col;
    private char letter;
    protected int gameAreaDimension;

    public FieldView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGameAreaDimension(int gameAreaDimension) {
        this.gameAreaDimension = gameAreaDimension;
    }

    public void saveInitialPosition() {
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

    public float getAbsolutePositionX() {
        return getX();
    }

    public float getAbsolutePositionY() {
        return getY();
    }

    public void setDistanceToFinger(float x, float y) {
        this.distanceToFingerX = x;
        this.distanceToFingerY = y;
    }

    public float getInitialPositionX() {
        return initialPositionX;
    }

    public float getInitialPositionY() {
        return initialPositionY;
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

    abstract public void animateLeft(float progress, float fingerPositionX);
    abstract public void animateRight(float progress, float fingerPositionX);
    abstract public void animateUp(float progress, float fingerPositionY);
    abstract public void animateDown(float progress, float fingerPositionY);

    @Override
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void resetRotationToInitial() {
        animate().rotationX(0).setDuration(0).start();
        animate().rotationY(0).setDuration(0).start();
    }

    public void setSelectedBackground() {
        setBackground(getResources().getDrawable(R.drawable.selected_field));
    }

    public void setDefaultBackground() {
        setBackground(getResources().getDrawable(R.drawable.field));
    }

    private final long HIDING_DURATION = 200l;
    private final long SHOWING_DURATION = 200l;
    public void hide(){
        animate().scaleXBy(-1.0f).setDuration(HIDING_DURATION);
        animate().scaleYBy(-1.0f).setDuration(HIDING_DURATION);
    }

    public void show(){
        setScaleX(0);
        setScaleY(0);
        animate().scaleXBy(1.0f).setDuration(SHOWING_DURATION);
        animate().scaleYBy(1.0f).setDuration(SHOWING_DURATION);
    }

}
