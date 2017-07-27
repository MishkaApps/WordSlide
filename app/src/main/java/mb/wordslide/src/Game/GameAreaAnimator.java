package mb.wordslide.src.Game;

import java.util.ArrayList;

/**
 * Created by mbolg on 26.07.2017.
 */
public class GameAreaAnimator {
    private FingerPosition currentFingerPosition;
    private FingerPosition fingerDownPosition;
    private boolean inSwipe;
    private SwipeDirection swipeDirection;
    private GameArea gameArea;
    private ArrayList<Field> activeFields;
    private float fieldWidth;


    public void onFingerDown() {
        saveFingerDownPosition();
    }

    private void saveFingerDownPosition() {
        fingerDownPosition = currentFingerPosition.copy();
    }

    public void onFingerUp() {

    }

    public void onFingerMove() {
        if (!inSwipe)
            if (checkIfFingerGetOutOfRange()) {
                setSwipeDirection();
                setActiveFields();
                inSwipe = true;
            } else return;

        continueSwipe();
    }

    private void setSwipeDirection() {
        if (Math.abs(currentFingerPosition.x - fingerDownPosition.x) >= Math.abs(currentFingerPosition.y - fingerDownPosition.y)) {
            if (currentFingerPosition.x < fingerDownPosition.x)
                swipeDirection.direction = SwipeDirection.Direction.LEFT;
            else swipeDirection.direction = SwipeDirection.Direction.RIGHT;
        } else {
            if (currentFingerPosition.y < fingerDownPosition.y)
                swipeDirection.direction = SwipeDirection.Direction.UP;
            else swipeDirection.direction = SwipeDirection.Direction.DOWN;
        }
    }

    private void setActiveFields() {
        for (Field field : gameArea.getFieldsArrayList()) {
            if (swipeDirection.isHorizontal()) {
                if (isFieldInRow(field)) {
                    activeFields.add(field);
                }
            } else {
                if (isFieldInCol(field)) {
                    activeFields.add(field);
                }
            }
        }
    }

    private float gapBetweenFields;
    private int gameAreaOffsetX, gameAreaOffsetY;

    private void continueSwipe() {
        if (swipeDirection.isHorizontal()) {
            float progress = Math.abs((currentFingerPosition.x - fingerDownPosition.x) / gapBetweenFields);
            for (Field field : activeFields) {
                if (swipeDirection.direction == SwipeDirection.Direction.LEFT)
                    field.moveLeft(progress, gameAreaOffsetX);
                else field.moveRight(progress, gameAreaOffsetX);
            }
        } else {
            float progress = Math.abs((currentFingerPosition.y - fingerDownPosition.y) / gapBetweenFields);
            for (Field field : activeFields) {
                if (swipeDirection.direction == SwipeDirection.Direction.UP)
                    field.moveUp(progress, gameAreaOffsetY);
                else field.moveDown(progress, gameAreaOffsetY);
            }
        }
    }

    private boolean isFieldInRow(Field field) {
        return fingerDownPosition.y > field.getY() && fingerDownPosition.y < (field.getY() + fieldWidth);
    }

    private boolean isFieldInCol(Field field) {
        return fingerDownPosition.x > field.getX() && fingerDownPosition.x < (field.getX() + fieldWidth);
    }

    private final float SWIPE_DETECT_RADIUS = 10f;

    private boolean checkIfFingerGetOutOfRange() {
        return (Math.sqrt(Math.abs(currentFingerPosition.x - fingerDownPosition.x) + Math.abs(currentFingerPosition.y - fingerDownPosition.y)) > SWIPE_DETECT_RADIUS);

    }

    public void setFingerPosition(FingerPosition fingerPosition) {
        this.currentFingerPosition = fingerPosition;
    }

}
