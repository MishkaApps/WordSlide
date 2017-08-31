package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;

import mb.wordslide.src.L;

/**
 * Created by mbolg on 30.07.2017.
 */
public class BorderField extends FieldView1 {

    public BorderField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void animateLeft(float progress, float fingerPositionX) {
        float rotationAdjust = (float) Math.pow(progress, 3);
        float startRotation = 90f;
        float rotationMultiplier = -1f;

        animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

        float origin = getInitialPositionX();
        float offsetMultiplier = (1 - progress);
        float offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().x(offset).setDuration(0).start();
    }

    @Override
    public void animateRight(float progress, float fingerPositionX) {
        float rotationAdjust = (float) Math.pow(progress, 3);
        float startRotation = -90f;
        float rotationMultiplier = 1f;

        animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

        float origin = getInitialPositionX();
        float offsetMultiplier = (progress - 1);
        float offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().x(offset).setDuration(0).start();
    }

    @Override
    public void animateUp(float progress, float fingerPositionY) {
        float rotationAdjust = (float) Math.pow(progress, 3);
        float startRotation = -90f;
        float rotationMultiplier = 1f;

        animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

        float origin = getInitialPositionY();
        float offsetMultiplier = (1 - progress);
        float offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().y(offset).setDuration(0).start();

    }

    @Override
    public void animateDown(float progress, float fingerPositionY) {

        float rotationAdjust = (float) Math.pow(progress, 3);
        float startRotation = 90f;
        float rotationMultiplier = -1f;

        animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

        float origin = getInitialPositionY();
        float offsetMultiplier = (progress - 1);
        float offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().y(offset).setDuration(0).start();
    }

    public void setStartPositionBeforeAnimateLeft() {
//        setX(getX() + getWidth() / 2);
//        setRotationX(0);
//        setRotationY(90);
    }
}
