package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by mbolg on 30.07.2017.
 */
public class Field extends FieldView1 {


    public Field(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // todo: пиздос криво, необходимо объектно оториентировать эту хуйню
    @Override
    public void animateLeft(float progress, float fingerPositionX) {
        if (col == 0) {
            moveAndRotateLeft(progress);
        } else {
            moveLeft(fingerPositionX);
        }
    }

    @Override
    public void animateRight(float progress, float fingerPositionX) {
        if (col == gameAreaDimension - 1) {
            moveAndRotateRight(progress);
        } else {
            moveRight(fingerPositionX);
        }
    }

    private void moveAndRotateLeft(float progress) {
        float rotationAdjust = 1f - (float) Math.pow((1 - progress), 3);
        float startRotation = 0f;
        float rotationMultiplier = 0;

        float offset;
        float offsetMultiplier = 0;
        float origin = 0;

        origin = this.initialPositionX;
        rotationMultiplier = -1f;
        offsetMultiplier = -progress;

        offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();
        animate().x(offset).setDuration(0).start();
    }

    // todo: у некоторых методов нет start(), у некоторых есть. Хуета выходит
    private void moveLeft(float fingerPositionX) {
        animate().x(fingerPositionX - distanceToFingerX).setDuration(0);
    }

    private void moveAndRotateRight(float progress) {
        float rotationAdjust = (1f - (float) Math.pow((1 - progress), 3));
        float startRotation = 0f;
        float rotationMultiplier = 0;

        float offset;
        float offsetMultiplier = 0;
        float origin = 0;

        origin = this.initialPositionX;
        rotationMultiplier = 1f;
        offsetMultiplier = (progress);

        offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();
        animate().x(offset).setDuration(0).start();
    }

    private void moveRight(float fingerPositionX) {
        animate().x(fingerPositionX - distanceToFingerX).setDuration(0).start();
    }


    @Override
    public void animateUp(float progress, float fingerPositionY) {
        if (row == 0) {
            moveAndRotateUp(progress);
        } else {
            moveUp(fingerPositionY);
        }
    }

    @Override
    public void animateDown(float progress, float fingerPositionY) {
        if (row == gameAreaDimension - 1) {
            moveAndRotateDown(progress);
        } else {
            moveDown(fingerPositionY);
        }
    }

    private void moveAndRotateUp(float progress) {
        float rotationAdjust = 1f - (float) Math.pow((1 - progress), 3);
        float startRotation = 0f;
        float rotationMultiplier = 0;

        float offset;
        float offsetMultiplier = 0;
        float origin = 0;

        origin = this.initialPositionY;
        rotationMultiplier = 1f;
        offsetMultiplier = -progress;

        offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();
        animate().y(offset).setDuration(0).start();
    }

    // todo: у некоторых методов нет start(), у некоторых есть. Хуета выходит
    private void moveUp(float fingerPositionY) {
        animate().y(fingerPositionY - distanceToFingerY).setDuration(0);
    }

    private void moveAndRotateDown(float progress) {
        float rotationAdjust = (1f - (float) Math.pow((1 - progress), 3));
        float startRotation = 0f;
        float rotationMultiplier = 0;

        float offset;
        float offsetMultiplier = 0;
        float origin = 0;

        startRotation *= -1f;
        origin = this.initialPositionY;
        rotationMultiplier = -1f;
        offsetMultiplier = (progress);

        offset = origin + (getWidth() / 2) * offsetMultiplier;

        animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();
        animate().y(offset).setDuration(0).start();
    }

    private void moveDown(float fingerPositionY) {
        animate().y(fingerPositionY - distanceToFingerY).setDuration(0).start();
    }

}
