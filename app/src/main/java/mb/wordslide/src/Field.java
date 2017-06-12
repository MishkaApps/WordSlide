package mb.wordslide.src;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;

import mb.wordslide.R;

public class Field extends TextView {
    private Pair<Float, Float> toFinger;
    private Pair<Integer, Integer> origin;
    private int row, col;
    private BorderType borderType;
    private BorderPosition borderPosition;
    private float gap;


    public Field(Context context, AttributeSet attrs) {
        super(context, attrs, R.style.FieldStyle);
        row = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_row"));
        col = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_column"));
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

    private final int FIELDS_IN_ROW = 4;                  //todo: зарефакторить

    public void setType(BorderType type) {
        if (row >= 1 && col >= 1 && row < FIELDS_IN_ROW - 1 && col < FIELDS_IN_ROW - 1) {
            borderPosition = BorderPosition.NONE;
        } else {
            switch (row) {
                case 0:
                    if (col == 0)
                        borderPosition = BorderPosition.LT;
                    else if (col == FIELDS_IN_ROW - 1)
                        borderPosition = BorderPosition.RT;
                    else borderPosition = BorderPosition.T;
                    break;
                case FIELDS_IN_ROW - 1:
                    if (col == 0)
                        borderPosition = BorderPosition.LB;
                    else if (col == FIELDS_IN_ROW - 1)
                        borderPosition = BorderPosition.RB;
                    else borderPosition = BorderPosition.B;
                    break;
                default:
                    if (col == 0)
                        borderPosition = BorderPosition.L;
                    else borderPosition = BorderPosition.R;
                    break;
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

    public void resetPositionToOrigin() {
        animate().x(origin.first).setDuration(0).start();
        animate().y(origin.second).setDuration(0).start();
        if (isSecondary())
            hide();
        else {
            animate().rotationY(0).setDuration(0).start();
            setRotationX(0);
        }
    }

    public int getOriginX() {
        return origin.first;
    }

    public int getOriginY() {
        return origin.second;
    }

    public void select() {
        setBackgroundResource(R.color.selected_field_color);
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
                    case RT:
                    case RB:
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
        }
    }

    private void flip(boolean appear, float progress, SwipeDirection direction) {

        switch (direction) {
            case LEFT:
                if (appear && getRotationY() > 0) {
                    animate().rotationY(90f - 90f * (float) Math.pow(progress, 3)).setDuration(0).start();
                    animate().x(origin.first + (getWidth() / 2) * (1 - progress)).setDuration(0).start();
                }
                if (!appear && getRotationY() > -90) {
                    animate().rotationY(-90f * (1f - (float) Math.pow((1 - progress), 3))).setDuration(0).start();
                    animate().x((origin.first - getWidth() / 2) + getWidth() * (1 - progress) / 2).setDuration(0).start();
                }
                break;
            case RIGHT:
                if (appear && getRotationY() < 0) {
                    animate().rotationY(-90f + 90f * (float) Math.pow(progress, 3)).setDuration(0).start();
                    animate().x((origin.first - getWidth() / 2) + getWidth() * (progress) / 2).setDuration(0).start();
                }
                if (!appear && getRotationY() < 90) {
                    animate().rotationY(90f * (1f - (float) Math.pow((1 - progress), 3))).setDuration(0).start();
                    animate().x(origin.first + getWidth() * (progress) / 2).setDuration(0).start();
                }
                break;
        }
    }

    public void prepareToFlip(SwipeDirection direction) {
        switch (direction) {
            case LEFT:
                switch (borderPosition) {
                    case R:
                    case RT:
                    case RB:
                        show();
                        setX(getX() + getWidth() / 2);
                        setRotationY(90);
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
                        setRotationY(-90);
                        break;
                }
                break;

        }
    }

    public BorderPosition getBorderPosition() {
        return borderPosition;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public enum BorderPosition {
        L, R, B, T, LT, LB, RT, RB, NONE
    }

    public enum BorderType {
        PRIMARY, SECONDARY
    }
}
