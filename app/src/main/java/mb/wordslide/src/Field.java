package mb.wordslide.src;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import mb.wordslide.R;

public class Field extends TextView {
    private Pair<Float, Float> toFinger;
    private Pair<Integer, Integer> origin;
    private int row, col;
    private BorderType borderType;
    private BorderPosition borderPosition;


    public Field(Context context, AttributeSet attrs) {
        super(context, attrs, R.style.FieldStyle);
        origin = null;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isSecondary() {
        if (borderType == BorderType.SECONDARY)
            return true;
        else return false;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setType(BorderType type, int FIELDS_IN_ROW) {
        if (row >= 1 && col >= 1 && row < FIELDS_IN_ROW - 1 && col < FIELDS_IN_ROW - 1) {
            borderPosition = BorderPosition.NONE;
        } else {
            if (row == 0) {
                if (col == 0)
                    borderPosition = BorderPosition.LT;
                else if (col == FIELDS_IN_ROW - 1)
                    borderPosition = BorderPosition.RT;
                else borderPosition = BorderPosition.T;
            } else if (row == FIELDS_IN_ROW - 1) {
                if (col == 0)
                    borderPosition = BorderPosition.LB;
                else if (col == FIELDS_IN_ROW - 1)
                    borderPosition = BorderPosition.RB;
                else borderPosition = BorderPosition.B;
            } else {
                if (col == 0)
                    borderPosition = BorderPosition.L;
                else borderPosition = BorderPosition.R;
            }
        }
        borderType = type;
    }

    public float getPosX() {
        int[] pos = {0, 0};
        getLocationOnScreen(pos);
        return (float) pos[0];
    }

    public int getPosY() {
        int[] pos = {0, 0};
        getLocationOnScreen(pos);
        return pos[1];
    }

    public void setToFinger(float x, float y) {
        toFinger = new Pair<>(x, y);
    }

    public float getToFingerX() {
        return toFinger.first;
    }

    public float getToFingerY() {
        return toFinger.second;
    }

    public void saveOrigin() {
        origin = new Pair<>(Math.round(getX()), Math.round(getY()));
    }

    public boolean isOriginSet(){
        if(origin == null)
            return false;
        else return true;
    }


    public void resetPositionToOrigin() {
        animate().x(origin.first).setDuration(0).start();
        animate().y(origin.second).setDuration(0).start();
        if (isSecondary())
            hide();
        else {
            animate().rotationY(0).setDuration(0).start();
            animate().rotationX(0).setDuration(0).start();
        }
    }

    public int getOriginX() {
        return origin.first;
    }

    public int getOriginY() {
        return origin.second;
    }

    public void select() {
        setBackgroundResource(R.drawable.selected_field);
    }
    public void deselect() {
        setBackgroundResource(R.drawable.field);
    }

    public void setLetter(char letter) {
        setText("" + Character.toUpperCase(letter));
    }

    public char getLetter() {
        return getText().charAt(0);
    }

    public void hide() {
        setVisibility(INVISIBLE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }


    public void animate(SwipeDirection direction, float progress, float touchPos) {
        switch (direction) {
            case LEFT:
                switch (borderPosition) {
                    case R:
                    case RB:
                    case RT:
                        if (borderType == BorderType.SECONDARY)
                            flip(true, progress, direction);
                        else if (borderType == BorderType.PRIMARY)
                            animate().x(touchPos - getToFingerX()).setDuration(0).start();
                        break;
                    case L:
                    case LT:
                    case LB:
                        if (borderType == BorderType.PRIMARY)
                            flip(false, progress, direction);
                        break;
                    case T:
                    case B:
                    case NONE:
                        if (borderType == BorderType.PRIMARY)
                            animate().x(touchPos - getToFingerX()).setDuration(0).start();
                        break;
                }
                break;
            case RIGHT:
                switch (borderPosition) {
                    case R:
                    case RT:
                    case RB:
                        if (borderType == BorderType.PRIMARY)
                            flip(false, progress, direction);
                        break;
                    case L:
                    case LT:
                    case LB:
                        if (borderType == BorderType.SECONDARY)
                            flip(true, progress, direction);
                        else if (borderType == BorderType.PRIMARY)
                            animate().x(touchPos - getToFingerX()).setDuration(0).start();
                        break;
                    case T:
                    case B:
                    case NONE:
                        if (borderType == BorderType.PRIMARY)
                            animate().x(touchPos - getToFingerX()).setDuration(0).start();
                        break;
                }
                break;
            case UP:
                switch (borderPosition) {
                    case B:
                    case RB:
                    case LB:
                        if (borderType == BorderType.SECONDARY)
                            flip(true, progress, direction);
                        else if (borderType == BorderType.PRIMARY)
                            animate().y(touchPos - getToFingerY()).setDuration(0).start();
                        break;
                    case T:
                    case LT:
                    case RT:
                        if (borderType == BorderType.PRIMARY)
                            flip(false, progress, direction);
                        break;
                    case R:
                    case L:
                    case NONE:
                        if (borderType == BorderType.PRIMARY)
                            animate().y(touchPos - getToFingerY()).setDuration(0).start();
                        break;
                }
                break;
            case DOWN:
                switch (borderPosition) {
                    case B:
                    case RB:
                    case LB:
                        if (borderType == BorderType.PRIMARY)
                            flip(false, progress, direction);
                        break;
                    case T:
                    case LT:
                    case RT:
                        if (borderType == BorderType.SECONDARY)
                            flip(true, progress, direction);
                        else if (borderType == BorderType.PRIMARY)
                            animate().y(touchPos - getToFingerY()).setDuration(0).start();
                        break;
                    case R:
                    case L:
                    case NONE:
                        if (borderType == BorderType.PRIMARY)
                            animate().y(touchPos - getToFingerY()).setDuration(0).start();
                        break;
                }
                break;
        }
    }

    private void flip(boolean appearing, float progress, SwipeDirection direction) {
        float rotationAdjust = appearing ? (float) Math.pow(progress, 3) : (1f - (float) Math.pow((1 - progress), 3));
        float startRotation = appearing ? 90f : 0f;
        float rotationMultiplier = 0;

        float offset;
        float offsetMultiplier = 0;
        float origin = 0;

        switch (direction) {
            case LEFT:
                origin = this.origin.first;
                rotationMultiplier = -1f;
                offsetMultiplier = appearing ? (1 - progress) : (-progress);
                break;
            case RIGHT:
                startRotation *= -1f;
                origin = this.origin.first;
                rotationMultiplier = 1f;
                offsetMultiplier = appearing ? (progress - 1) : (progress);
                break;
            case UP:
                startRotation *= -1f;
                origin = this.origin.second;
                rotationMultiplier = 1f;
                offsetMultiplier = appearing ? (1 - progress) : (-progress);
                break;
            case DOWN:
                origin = this.origin.second;
                rotationMultiplier = -1f;
                offsetMultiplier = appearing ? (progress - 1) : (progress);
                break;
        }

        offset = origin + (getWidth() / 2) * offsetMultiplier;

        switch (direction) {
            case RIGHT:
            case LEFT:
                animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();
                animate().x(offset).setDuration(0).start();
                break;
            case UP:
            case DOWN:
                animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();
                animate().y(offset).setDuration(0).start();
                break;
        }

    }

    public void prepareToFlip(SwipeDirection direction) {
        float rotationX = 0, rotationY = 0;
        switch (direction) {
            case LEFT:
                switch (borderPosition) {
                    case R:
                    case RT:
                    case RB:
                        show();
                        setX(getX() + getWidth() / 2);
                        rotationY = 90f;
                        rotationX = 0;
                        break;
                }
                break;
            case RIGHT:
                switch (borderPosition) {
                    case L:
                    case LT:
                    case LB:
                        show();
                        setX(getX() - getWidth() / 2);
                        rotationY = -90f;
                        rotationX = 0;
                        break;
                }
                break;
            case UP:
                switch (borderPosition) {
                    case B:
                    case RB:
                    case LB:
                        show();
                        setY(getY() + getWidth() / 2);
                        rotationY = 0;
                        rotationX = 90f;
                        break;
                }
                break;
            case DOWN:
                switch (borderPosition) {
                    case T:
                    case RT:
                    case LT:
                        show();
                        setY(getY() - getWidth() / 2);
                        rotationY = 0;
                        rotationX = -90;
                        break;
                }
                break;

        }

        setRotationX(rotationX);
        setRotationY(rotationY);
    }

    public BorderPosition getBorderPosition() {
        return borderPosition;
    }

    public enum BorderPosition {
        L, R, B, T, LT, LB, RT, RB, NONE
    }

    public enum BorderType {
        PRIMARY, SECONDARY
    }
}
