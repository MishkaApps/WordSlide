package mb.wordslide.src.Game.Field;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import mb.wordslide.R;
import mb.wordslide.src.L;
import mb.wordslide.src.Vibrator;

/**
 * Created by mbolg on 30.07.2017.
 */
public abstract class Field extends android.support.v7.widget.AppCompatTextView {
    protected float distanceToFingerX, distanceToFingerY;
    protected float initialPositionX, initialPositionY;
    protected int row, col;
    private char letter;
    protected int gameAreaDimension;
    protected int gameAreaOffsetX, gameAreaOffsetY;

    public Field(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGameAreaDimension(int gameAreaDimension) {
        this.gameAreaDimension = gameAreaDimension;
    }

    public void saveInitialPosition() {
        initialPositionX = getPositionX();
        initialPositionY = getPositionY();
        if (getClass() == BorderField.class && row == 0)
            L.l("Saving initial field position: " + getPositionAsString());
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

    public void setDistanceToFinger(float x, float y) {
        distanceToFingerX = x;
        distanceToFingerY = y;
    }

    public float getInitialPositionX() {
        return initialPositionX;
    }

    public float getInitialPositionY() {
        return initialPositionY;
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

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setSelectedBackground() {
        setBackground(getResources().getDrawable(R.drawable.selected_field));
    }

    public void setDefaultBackground() {
        setBackground(getResources().getDrawable(R.drawable.field));
    }

    private final long HIDING_DURATION = 200l;
    private final long SHOWING_DURATION = 200l;
    private final int MAX_ANIMATION_START_DELAY = 150;

    public void hide() {
        Random random = new Random();
        long delay = random.nextInt(MAX_ANIMATION_START_DELAY);
        animate().scaleXBy(-1.0f).setDuration(HIDING_DURATION).setStartDelay(delay).start();
        animate().scaleYBy(-1.0f).setDuration(HIDING_DURATION).setStartDelay(delay).start();
    }

    public void show() {
        Random random = new Random();
        setScaleX(0);
        setScaleY(0);
        long delay = random.nextInt(MAX_ANIMATION_START_DELAY);
        animate().scaleXBy(1.0f).setDuration(SHOWING_DURATION).setStartDelay(delay).start();
        animate().scaleYBy(1.0f).setDuration(SHOWING_DURATION).setStartDelay(delay).start();
    }

    public void setRandomLetter() {
        setLetter(LetterGenerator.getRandomLetter());
    }


    public void addListenerToSavePosition() {
        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                saveInitialPosition();
            }
        });
    }

    // todo: Написать реализацию анимации возврата ячеек к исходным положениям для BorderField
    protected final int RETURN_ANIMATION_DURATION = 200;

    public abstract void animatePositionToInitial();

    public abstract void resetPositionToInitialInstantly();

    public abstract void animateRotationToInitial();

    public abstract void resetRotationToInitialInstantly();

    public void resetStartDelayToZero() {
        animate().setStartDelay(0);
    }

    public void setGameAreaOffset(int gameAreaOffsetX, int gameAreaOffsetY) {
        this.gameAreaOffsetX = gameAreaOffsetX;
        this.gameAreaOffsetY = gameAreaOffsetY;
    }

    protected String getPositionAsString() {
        return "(" + getPositionX() + "; " + getPositionY() + ")";
    }
}
