package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import mb.wordslide.src.Game.GameArea.AnimatedGameArea;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 30.07.2017.
 */
public class GameField extends Field {
    public GameField(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRandomLetter();
    }

    // todo: пиздос криво, необходимо объектно оториентировать эту хуйню
    @Override
    public void animateLeft(float progress, float fingerPositionX) {
        resetStartDelayToZero();
        if (col == 0) {
            if (progress > 0)
                moveAndRotateLeft(progress);
            else
                moveLeft(fingerPositionX);
        } else {
            if (col == gameAreaDimension - 1 && (getRotationY() > 0)) {
                moveAndRotateRight(-progress);
            } else {
                if (col == gameAreaDimension - 1)
                    setRotationY(0);
                moveLeft(fingerPositionX);
            }
        }
    }

    @Override
    public void animateRight(float progress, float fingerPositionX) {
        resetStartDelayToZero();

        if (col == gameAreaDimension - 1) {
            if (progress > 0)
                moveAndRotateRight(progress);
            else
                moveRight(fingerPositionX);
        } else {
            if (col == 0 && (getRotationY() < 0)) {
                moveAndRotateLeft(-progress);
            } else {
                if (col == 0)
                    setRotationY(0);
                moveRight(fingerPositionX);
            }
        }
    }


    @Override
    public void animateUp(float progress, float fingerPositionY) {
        resetStartDelayToZero();
        if (row == 0) {
            if (progress > 0)
                moveAndRotateUp(progress);
            else
                moveUp(fingerPositionY);
        } else {
            if (row == gameAreaDimension - 1 && (getRotationX() < 0)) {
                moveAndRotateDown(-progress);
            } else {
                if (row == gameAreaDimension - 1)
                    setRotationX(0);
                moveUp(fingerPositionY);
            }
        }
    }

    @Override
    public void animateDown(float progress, float fingerPositionY) {
        resetStartDelayToZero();
        if (row == gameAreaDimension - 1) {
            if (progress > 0)
                moveAndRotateDown(progress);
            else
                moveDown(fingerPositionY);
        } else {
            if (row == 0 && (getRotationX() > 0)) {
                moveAndRotateUp(-progress);
            } else {
                if (row == 0)
                    setRotationX(0);
                moveDown(fingerPositionY);
            }
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
        animate().x(offset- gameAreaOffsetX).setDuration(0).start();
    }

    // todo: у некоторых методов нет start(), у некоторых есть. Хуета выходит
    private void moveLeft(float fingerPositionX) {
        animate().x(fingerPositionX - distanceToFingerX - gameAreaOffsetX).setDuration(0);
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
        animate().x(offset- gameAreaOffsetX).setDuration(0).start();
    }

    private void moveRight(float fingerPositionX) {
        animate().x(fingerPositionX - distanceToFingerX - gameAreaOffsetX).setDuration(0).start();
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
        animate().y(offset - gameAreaOffsetY).setDuration(0).start();
    }

    // todo: у некоторых методов нет start(), у некоторых есть. Хуета выходит
    private void moveUp(float fingerPositionY) {
        animate().y(fingerPositionY - distanceToFingerY - gameAreaOffsetY).setDuration(0);
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
        animate().y(offset - gameAreaOffsetY).setDuration(0).start();
    }

    private void moveDown(float fingerPositionY) {
        animate().y(fingerPositionY - distanceToFingerY - gameAreaOffsetY).setDuration(0).start();
    }


    public void animatePositionToInitial() {
        animate().y(getInitialPositionY() - gameAreaOffsetY).setDuration(RETURN_ANIMATION_DURATION).start();
        animate().x(getInitialPositionX() - gameAreaOffsetX).setDuration(RETURN_ANIMATION_DURATION).start();
    }

    public void resetPositionToInitialInstantly() {
        animate().y(getInitialPositionY() - gameAreaOffsetY).setDuration(0).start();
        animate().x(getInitialPositionX() - gameAreaOffsetX).setDuration(0).start();
    }

    public void animateRotationToInitial() {
        animate().rotationX(0).setDuration(RETURN_ANIMATION_DURATION).start();
        animate().rotationY(0).setDuration(RETURN_ANIMATION_DURATION).start();
    }

    public void resetRotationToInitialInstantly() {
        animate().rotationX(0).setDuration(0).start();
        animate().rotationY(0).setDuration(0).start();
    }

}
