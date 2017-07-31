package mb.wordslide.src.Game;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import mb.wordslide.R;

/**
 * Created by mbolg on 28.07.2017.
 */
public class FieldViewRemoved extends TextView {
    private float distanceToFingerX, distanceToFingerY;
    private float initialPositionX, initialPositionY;
    private FieldTemp parentField;

    public FieldViewRemoved(Context context, AttributeSet attrs) {
        super(context, attrs, R.style.FieldStyle);
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

    public void moveLeft(float progress, float fingerPositionX) {
        animate().x(fingerPositionX - distanceToFingerX).setDuration(0);
    }

    public void moveRight(float progress, float fingerPositionX) {

    }

    public void moveUp(float progress, float fingerPositionY) {

    }

    public void moveDown(float progress, float fingerPositionY) {

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

//    public int getRow() {
//        return parentField.getRow();
//    }
//    public int getCol() {
//        return parentField.getCol();
//    }
//
//    public void setParentField(FieldTemp parentField) {
//        this.parentField = parentField;
//    }
//
//    public String getLetterAsString() {
//        return Character.toString(parentField.getLetter());
//    }
}
