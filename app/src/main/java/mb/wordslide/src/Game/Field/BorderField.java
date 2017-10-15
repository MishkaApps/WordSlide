package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;

import mb.wordslide.src.Game.FieldAnimationListener;
import mb.wordslide.src.Game.GameArea.AnimatedGameArea;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 30.07.2017.
 */
public class BorderField extends Field {

    private AnimatedGameArea.Direction direction;
    private boolean isFieldReadyToAnimate;
    private FieldAnimationListener animationListener;


    public BorderField(Context context, AttributeSet attrs) {
        super(context, attrs);
        isFieldReadyToAnimate = false;
    }

    @Override
    public void saveInitialPosition() {
        super.saveInitialPosition();
        isFieldReadyToAnimate = true;
    }

    @Override
    public void animateLeft(float progress, float fingerPositionX) {
        if (!isFieldReadyToAnimate)
            return;
        resetStartDelayToZero();

        direction = AnimatedGameArea.Direction.LEFT;

        if (isVisibleOnGameArea(progress))
            setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);

        if (col == 0) {
            if (progress < 0)
                animateRight(-progress, fingerPositionX);
        } else {
            float rotationAdjust = (float) Math.pow(progress, 3);
            float startRotation = 90f;
            float rotationMultiplier = -1f;

            animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

            float origin = getInitialPositionX();
            float offsetMultiplier = (1 - progress);
            float offset = origin + (getWidth() / 2) * offsetMultiplier;

            animate().x(offset - gameAreaOffsetX).setDuration(0).start();
        }
    }

    @Override
    public void animateRight(float progress, float fingerPositionX) {
        if (!isFieldReadyToAnimate)
            return;
        resetStartDelayToZero();

        direction = AnimatedGameArea.Direction.RIGHT;

        if (isVisibleOnGameArea(progress))
            setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);

        if (col == gameAreaDimension - 1) {
            if (progress < 0)
                animateLeft(-progress, fingerPositionX);
        } else {
            float rotationAdjust = (float) Math.pow(progress, 3);
            float startRotation = -90f;
            float rotationMultiplier = 1f;

            animate().rotationY(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

            float origin = getInitialPositionX();
            float offsetMultiplier = (progress - 1);
            float offset = origin + (getWidth() / 2) * offsetMultiplier;

            animate().x(offset - gameAreaOffsetX).setDuration(0).start();
        }
    }

    @Override
    public void animateUp(float progress, float fingerPositionY) {
        if (!isFieldReadyToAnimate)
            return;
        resetStartDelayToZero();

        direction = AnimatedGameArea.Direction.UP;

        if (isVisibleOnGameArea(progress))
            setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);

        if (row == 0) {
            if (progress < 0)
                animateDown(-progress, fingerPositionY);
        } else {

            float rotationAdjust = (float) Math.pow(progress, 3);
            float startRotation = -90f;
            float rotationMultiplier = 1f;

            animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

            float origin = getInitialPositionY();
            float offsetMultiplier = (1 - progress);
            float offset = origin + (getWidth() / 2) * offsetMultiplier;

            animate().y(offset - gameAreaOffsetY).setDuration(0).start();
        }
    }

    @Override
    public void animateDown(float progress, float fingerPositionY) {
        if (!isFieldReadyToAnimate)
            return;
        resetStartDelayToZero();
        direction = AnimatedGameArea.Direction.DOWN;

        if (isVisibleOnGameArea(progress))
            setVisibility(VISIBLE);
        else setVisibility(INVISIBLE);

        if (row == gameAreaDimension - 1) {
            if (progress < 0)
                animateUp(-progress, fingerPositionY);
        } else {

            float rotationAdjust = (float) Math.pow(progress, 3);
            float startRotation = 90f;
            float rotationMultiplier = -1f;

            animate().rotationX(startRotation + rotationMultiplier * 90f * rotationAdjust).setDuration(0).start();

            float origin = getInitialPositionY();
            float offsetMultiplier = (progress - 1);
            float offset = origin + (getWidth() / 2) * offsetMultiplier;

            animate().y(offset - gameAreaOffsetY).setDuration(0).start();
        }
    }


    private boolean isVisibleOnGameArea(float progress) {
        switch (direction) {
            case LEFT:
                if (!isInStart(direction)) {
                    return progress >= 0;
                } else {
                    return progress < 0;
                }
            case RIGHT:
                if (!isInStart(direction)) {
                    return progress < 0;
                } else {
                    return progress >= 0;
                }
            case UP:
                if (!isInStart(direction)) {
                    return progress >= 0;
                } else {
                    return progress < 0;
                }
            case DOWN:
                if (!isInStart(direction)) {
                    return progress < 0;
                } else {
                    return progress >= 0;
                }
        }
        return false;
    }

    private boolean isInStart(AnimatedGameArea.Direction direction) {
        if (direction.isHorizontal())
            return col == 0;
        else return row == 0;
    }

    public void animatePositionToInitial() {
        ViewPropertyAnimator xAnimator = null, yAnimator = null;

        if (direction == null) {
            yAnimator = animate();
            xAnimator = animate();
        } else {
            switch (direction) {
                case UP:
                    yAnimator = animate().y(getInitialPositionY() - gameAreaOffsetY + getWidth() / 2).setDuration(RETURN_ANIMATION_DURATION);
                    xAnimator = animate().x(getInitialPositionX() - gameAreaOffsetX).setDuration(RETURN_ANIMATION_DURATION);
                    break;
                case DOWN:
                    yAnimator = animate().y(getInitialPositionY() - gameAreaOffsetY - getWidth() / 2).setDuration(RETURN_ANIMATION_DURATION);
                    xAnimator = animate().x(getInitialPositionX() - gameAreaOffsetX).setDuration(RETURN_ANIMATION_DURATION);
                    break;
                case LEFT:
                    yAnimator = animate().y(getInitialPositionY() - gameAreaOffsetY).setDuration(RETURN_ANIMATION_DURATION);
                    xAnimator = animate().x(getInitialPositionX() - gameAreaOffsetX + getWidth() / 2).setDuration(RETURN_ANIMATION_DURATION);
                    break;
                case RIGHT:
                    yAnimator = animate().y(getInitialPositionY() - gameAreaOffsetY).setDuration(RETURN_ANIMATION_DURATION);
                    xAnimator = animate().x(getInitialPositionX() - gameAreaOffsetX - getWidth() / 2).setDuration(RETURN_ANIMATION_DURATION);
                    break;

            }
        }
        yAnimator.setListener(animationListener).start();
        xAnimator.setListener(animationListener).start();
    }

    public void resetPositionToInitialInstantly() {
//        animate().y(getInitialPositionY() - gameAreaOffsetY).setDuration(0).start();
//        animate().x(getInitialPositionX() - gameAreaOffsetX).setDuration(0).start();
    }

    public void animateRotationToInitial() {
        if (direction == AnimatedGameArea.Direction.UP) {
            animate().rotationX(-90).setDuration(RETURN_ANIMATION_DURATION).start();
            animate().rotationY(0).setDuration(RETURN_ANIMATION_DURATION).start();
        } else if (direction == AnimatedGameArea.Direction.DOWN) {
            animate().rotationX(90).setDuration(RETURN_ANIMATION_DURATION).start();
            animate().rotationY(0).setDuration(RETURN_ANIMATION_DURATION).start();
        } else if (direction == AnimatedGameArea.Direction.LEFT) {
            animate().rotationX(0).setDuration(RETURN_ANIMATION_DURATION).start();
            animate().rotationY(90).setDuration(RETURN_ANIMATION_DURATION).start();
        } else if (direction == AnimatedGameArea.Direction.RIGHT) {
            animate().rotationX(0).setDuration(RETURN_ANIMATION_DURATION).start();
            animate().rotationY(-90).setDuration(RETURN_ANIMATION_DURATION).start();
        }
    }

    public void resetRotationToInitialInstantly() {
//        animate().rotationX(0).setDuration(0).start();
//        animate().rotationY(0).setDuration(0).start();
    }

    public void setAnimationListener(FieldAnimationListener listener) {
        animationListener = listener;
    }
}
