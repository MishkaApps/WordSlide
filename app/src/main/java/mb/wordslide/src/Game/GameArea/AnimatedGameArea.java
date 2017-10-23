package mb.wordslide.src.Game.GameArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;

import mb.wordslide.R;
import mb.wordslide.src.Game.Field.GameField;
import mb.wordslide.src.Game.FieldAnimationListener;
import mb.wordslide.src.Game.Field.BorderField;
import mb.wordslide.src.Game.Field.Field;
import mb.wordslide.src.Game.FingerPosition;
import mb.wordslide.src.Game.GameControl.ShiftListener;
import mb.wordslide.src.Game.NoActiveFieldsSetException;

/**
 * Created by mbolg on 26.07.2017.
 */
public class AnimatedGameArea extends ClickableGameArea implements View.OnTouchListener {
    private FingerPosition currentFingerPosition;
    private FingerPosition originFingerPosition;
    private Direction swipeDirection;
    private ArrayList<Field> activeFields;
    private int activeFieldsIndex;
    private BorderField startBorderField, endBorderField;
    private ShiftListener shiftListener;

    private boolean fingerDownImitated;
    private boolean inSwipe;
    private boolean isSwipeAxisDetermined;
    private boolean isActiveFieldsSet;
    private boolean isFingerActuallyDown;

    public AnimatedGameArea(GridLayout gameAreaGrid,
                            LayoutInflater inflater, Context context) {
        super(6, inflater, gameAreaGrid, context);
        setFieldsTouchListener();
        currentFingerPosition = new FingerPosition();
        previousFingerPosition = new FingerPosition();
        originFingerPosition = new FingerPosition();
        swipeDirection = Direction.NONE;
        gameAreaGrid.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int motionEvent = event.getAction();

        if (motionEvent != MotionEvent.ACTION_UP)
            if (isDistanceBetweenCurrentAndPreviousEventsLiesInAllowableRange(event))
                return true;

        if (motionEvent == MotionEvent.ACTION_MOVE)
            if (isUserInputWord())
                return true;

        saveTouchedViewIfItsField(v);
        setFingerPosition(event.getRawX(), event.getRawY());

        switch (motionEvent) {
            case MotionEvent.ACTION_DOWN:
                onFingerDown();
                break;
            case MotionEvent.ACTION_MOVE:
                onFingerMove();
                break;
            case MotionEvent.ACTION_UP:
                onFingerUp();
                break;
        }

        return true;
    }

    private void onFingerDown() {
        fingerDownImitated = false;
        inSwipe = false;
        swipeDirection = Direction.NONE;
        isFingerActuallyDown = true;
        clearActiveFields();
        isSwipeAxisDetermined = false;
        saveCurrentFingerPositionAsOriginPosition();
    }

    private void imitateFingerDown() {
        fingerDownImitated = true;
        saveCurrentFingerPositionAsOriginPosition();
    }

    private void onFingerMove() {
        setSwipeDirection();

        if (!inSwipe)
            if (isFingerGetOutOfRange()) {
                if (!isActiveFieldsSet) {
                   try {
                       setActiveFields();
                   } catch (NoActiveFieldsSetException e){
                       onFingerUp(); // todo:  Норм не норм?
                       return;
                   }
                }
                setActiveFieldsIndex();
                setDistancesToFingerForActiveFields();
                prepareBorderFields();
                inSwipe = true;
                isSwipeAxisDetermined = true;
            } else return;

        previousFingerPosition.setPosition(currentFingerPosition.getX(), currentFingerPosition.getY());
        continueSwipe();
    }

    private void prepareBorderFields() {
        addStartBorderField();
        addEndBorderField();
        startBorderField.setGameAreaOffset(gameAreaOffsetX, gameAreaOffsetY);
        endBorderField.setGameAreaOffset(gameAreaOffsetX, gameAreaOffsetY);
        startBorderField.setAnimationListener(new FieldAnimationListener(gameAreaGrid, startBorderField));
        hideBorderFields();
    }

    private void onFingerUp() {
        isFingerActuallyDown = false;
        if (!isFingerGetOutOfRange() && !fingerDownImitated)
            if (touchedField != null)
                onFieldClick();
        returnActiveFieldsRotationToInitial();
        returnActiveFieldsPositionToInitial();
    }

    private void saveTouchedViewIfItsField(View v) {
        if (v.getClass() == GameField.class)
            touchedField = (Field) v;
    }

    private boolean isDistanceBetweenCurrentAndPreviousEventsLiesInAllowableRange(MotionEvent event) {
        final float MAX_ALLOWED_TOUCH_OFFSET = 100;
        if (isFingerActuallyDown)
            return getDistanceBetweenCurrentAndPreviousTouches(event.getRawX(), event.getRawY()) > MAX_ALLOWED_TOUCH_OFFSET;
        return false;
    }


    private void setFieldsTouchListener() {
        for (Field field : getFieldsArrayList())
            field.setOnTouchListener(this);
    }


    private void clearActiveFields() {
        activeFields = new ArrayList<>();
        isActiveFieldsSet = false;
    }

    private void saveCurrentFingerPositionAsOriginPosition() {
        originFingerPosition.setPosition(currentFingerPosition.getX(), currentFingerPosition.getY());
    }

    private FingerPosition previousFingerPosition;


    private void hideBorderFields() {
        startBorderField.setVisibility(View.INVISIBLE);
        endBorderField.setVisibility(View.INVISIBLE);
    }


    private void addStartBorderField() {
        int borderFieldRow = getStartBorderFieldRow();
        int borderFieldCol = getStartBorderFieldCol();
        startBorderField = inflateBorderField(borderFieldRow, borderFieldCol);
        startBorderField.setPosition(borderFieldRow, borderFieldCol);
        startBorderField.setLetter(getBorderFieldLetter());
        startBorderField.addListenerToSavePosition();
        startBorderField.setGameAreaDimension(gameAreaDimension);
        activeFields.add(startBorderField);
        gameAreaGrid.addView(startBorderField);
    }

    private void addEndBorderField() {
        int borderFieldRow = getEndBorderFieldRow();
        int borderFieldCol = getEndBorderFieldCol();
        endBorderField = inflateBorderField(borderFieldRow, borderFieldCol);
        endBorderField.setPosition(borderFieldRow, borderFieldCol);
        endBorderField.setLetter(getBorderFieldLetter());
        endBorderField.addListenerToSavePosition();
        endBorderField.setGameAreaDimension(gameAreaDimension);
        activeFields.add(endBorderField);
        gameAreaGrid.addView(endBorderField);
    }

    private int getStartBorderFieldRow() {
        if (swipeDirection.isHorizontal())
            return activeFieldsIndex;
        else
            return 0;
    }

    private int getStartBorderFieldCol() {
        if (swipeDirection.isHorizontal())
            return 0;
        else return activeFieldsIndex;
    }

    private int getEndBorderFieldRow() {
        if (swipeDirection.isHorizontal())
            return activeFieldsIndex;
        else
            return gameAreaDimension - 1;
    }

    private int getEndBorderFieldCol() {
        if (swipeDirection.isHorizontal())
            return gameAreaDimension - 1;
        else
            return activeFieldsIndex;
    }


    private void setSwipeDirection() {
        Direction newDirection;

        if (isFingerXOffsetGreaterThanYOffset()) {
            if (currentFingerPosition.getX() < previousFingerPosition.getX())
                newDirection = Direction.LEFT;
            else newDirection = Direction.RIGHT;
        } else {
            if (currentFingerPosition.getY() < previousFingerPosition.getY())
                newDirection = Direction.UP;
            else newDirection = Direction.DOWN;
        }

        if (swipeDirection.isSameAxis(newDirection) || swipeDirection.isNone())
            swipeDirection = newDirection;
        else if (!isSwipeAxisDetermined)
            swipeDirection = newDirection;
    }

    private boolean isFingerGetOutOfRange() {
        final float SWIPE_DETECT_RADIUS = 5f;
        return (Math.sqrt(Math.abs(currentFingerPosition.getX() - originFingerPosition.getX()) + Math.abs(currentFingerPosition.getY() - originFingerPosition.getY())) > SWIPE_DETECT_RADIUS);
    }


    private char getBorderFieldLetter() {
        if (swipeDirection == Direction.LEFT)
            return activeFields.get(0).getLetter();
        else if (swipeDirection == Direction.RIGHT)
            return activeFields.get(gameAreaDimension - 1).getLetter();
        else if (swipeDirection == Direction.UP)
            return activeFields.get(0).getLetter();
        else if (swipeDirection == Direction.DOWN)
            return activeFields.get(gameAreaDimension - 1).getLetter();
        return 0;
    }


    //todo Не реагировать на касания, если анимация возврата не заверншена

    private BorderField inflateBorderField(int row, int col) {
        GridLayout.LayoutParams layoutParams;
        BorderField inflatedField = (BorderField) inflater.inflate(R.layout.border_field_view_template, null, false);
        layoutParams = new GridLayout.LayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.height = FIELD_SIZE;
        layoutParams.width = FIELD_SIZE;
        inflatedField.setLayoutParams(layoutParams);
        return inflatedField;
    }

    private void resetGameAreaState() {
        fingerDownImitated = false;
        inSwipe = false;
        removeBorderFieldFromGameArea();
        resetActiveFieldsRotationInstantly();
        resetActiveFieldsPositionInstantly();
    }

    private void resetActiveFieldsRotationInstantly() {
        for (Field field : activeFields) {
            field.resetRotationToInitialInstantly();
        }
    }

    private void resetActiveFieldsPositionInstantly() {
        for (Field field : activeFields)
            field.resetPositionToInitialInstantly();
    }

    private void returnActiveFieldsRotationToInitial() {
        for (Field field : activeFields) {
            field.animateRotationToInitial();
        }
    }

    private void returnActiveFieldsPositionToInitial() {
        for (Field field : activeFields)
            field.animatePositionToInitial();
    }

    private void removeBorderFieldFromGameArea() {
        gameAreaGrid.removeView(startBorderField);
        gameAreaGrid.removeView(endBorderField);
    }

    private void setDistancesToFingerForActiveFields() {
        for (Field field : activeFields)
            field.setDistanceToFinger(currentFingerPosition.getX() - field.getPositionX(), currentFingerPosition.getY() - field.getPositionY());
    }


    private boolean isFingerXOffsetGreaterThanYOffset() {
        return Math.abs(currentFingerPosition.getX() - originFingerPosition.getX()) >= Math.abs(currentFingerPosition.getY() - originFingerPosition.getY());
    }

    private void setActiveFields() throws NoActiveFieldsSetException {
        for (Field fieldView : getFieldsArrayList())
            if (swipeDirection.isHorizontal()) {
                if (isFieldInRow(fieldView))
                    activeFields.add(fieldView);
            } else {
                if (isFieldInCol(fieldView))
                    activeFields.add(fieldView);
            }

        if (activeFields.size() == 0)
            throw new NoActiveFieldsSetException();

        isActiveFieldsSet = true;
    }

    private boolean isFieldInRow(Field field) {
        return originFingerPosition.getY() > field.getPositionY() && originFingerPosition.getY() < (field.getPositionY() + FIELD_SIZE);
    }

    private boolean isFieldInCol(Field field) {
        return originFingerPosition.getX() > field.getPositionX() && originFingerPosition.getX() < (field.getPositionX() + FIELD_SIZE);
    }

    private void setActiveFieldsIndex() {
        if (swipeDirection.isHorizontal())
            activeFieldsIndex = activeFields.get(0).getRow();
        else
            activeFieldsIndex = activeFields.get(0).getCol();
    }

    private void continueSwipe() {
        float progress = 0;
        if (swipeDirection.isHorizontal()) {
            if (swipeDirection == Direction.LEFT)
                progress = calculateProgressWhileMovingLeft();
            else if (swipeDirection == Direction.RIGHT)
                progress = calculateProgressWhileMovingRight();


            for (Field field : activeFields) {
                float relativeFingerPositionX = currentFingerPosition.getX();
                if (swipeDirection == Direction.LEFT) {

                    field.animateLeft(progress, relativeFingerPositionX);

                    if (isActiveFieldsShiftedLeft()) {
                        shiftRowLeft(activeFieldsIndex);
                        onShiftComplete();
                        break;
                    }
                } else if (swipeDirection == Direction.RIGHT) {

                    field.animateRight(progress, relativeFingerPositionX);

                    if (isActiveFieldsShiftedRight()) {
                        shiftRowRight(activeFieldsIndex);
                        onShiftComplete();
                        break;
                    }
                }
            }
        } else {
            if (swipeDirection == Direction.UP)
                progress = calculateProgressWhileMovingUp();
            else if (swipeDirection == Direction.DOWN)
                progress = calculateProgressWhileMovingDown();

            for (Field field : activeFields) {
                float relativeFingerPositionY = currentFingerPosition.getY();// - gameAreaOffsetY;
                if (swipeDirection == Direction.UP) {
                    field.animateUp(progress, relativeFingerPositionY);
                    if (isActiveFieldsShiftedUp()) {
                        shiftColUp(activeFieldsIndex);
                        onShiftComplete();
                        break;
                    }
                } else if (swipeDirection == Direction.DOWN) {
                    field.animateDown(progress, relativeFingerPositionY);
                    if (isActiveFieldsShiftedDown()) {
                        shiftColDown(activeFieldsIndex);
                        onShiftComplete();
                        break;
                    }
                }
            }
        }

    }

    private void onShiftComplete() {
        resetGameAreaState();
        imitateFingerDown();
        vibrate();
        if (shiftListener != null)
            shiftListener.shifted();
    }

    private void vibrate() {
        vibrator.vibrate();
    }

    private float calculateProgressWhileMovingLeft() {
        Field testField1 = activeFields.get(2);
        Field testField2 = activeFields.get(1);
        float progress = 1f - Math.abs((testField1.getPositionX() - testField2.getInitialPositionX()) / (testField1.getInitialPositionX() - testField2.getInitialPositionX()));
        return progress;
    }

    private float calculateProgressWhileMovingRight() {
        Field testField1 = activeFields.get(1);
        Field testField2 = activeFields.get(2);
        float progress = 1f - Math.abs((testField1.getPositionX() - testField2.getInitialPositionX()) / (testField1.getInitialPositionX() - testField2.getInitialPositionX()));
        return progress;
    }

    private float calculateProgressWhileMovingUp() {
        Field testField1 = activeFields.get(2);
        Field testField2 = activeFields.get(1);
        float progress = 1f - Math.abs((testField1.getPositionY() - testField2.getInitialPositionY()) / (testField1.getInitialPositionY() - testField2.getInitialPositionY()));
        return progress;
    }

    private float calculateProgressWhileMovingDown() {
        Field testField1 = activeFields.get(1);
        Field testField2 = activeFields.get(2);
        float progress = 1f - Math.abs((testField1.getPositionY() - testField2.getInitialPositionY()) / (testField1.getInitialPositionY() - testField2.getInitialPositionY()));
        return progress;
    }


    private boolean isActiveFieldsShiftedLeft() {
        Field testField1 = activeFields.get(2);
        Field testField2 = activeFields.get(1);
        return testField1.getPositionX() < testField2.getInitialPositionX();
    }

    private boolean isActiveFieldsShiftedRight() {
        Field testField1 = activeFields.get(1);
        Field testField2 = activeFields.get(2);
        return testField1.getPositionX() > testField2.getInitialPositionX();
    }

    private boolean isActiveFieldsShiftedUp() {
        Field testField1 = activeFields.get(2);
        Field testField2 = activeFields.get(1);
        return testField1.getPositionY() < testField2.getInitialPositionY();
    }

    private boolean isActiveFieldsShiftedDown() {
        Field testField1 = activeFields.get(1);
        Field testField2 = activeFields.get(2);
        return testField1.getPositionY() > testField2.getInitialPositionY();
    }


    private float getDistanceBetweenCurrentAndPreviousTouches(float x, float y) {
        float res = (float) Math.sqrt(Math.pow(currentFingerPosition.getX() - x, 2)
                + Math.pow(currentFingerPosition.getY() - y, 2));
        return res;
    }


    private void setFingerPosition(float x, float y) {
        currentFingerPosition.setPosition(x, y);
    }

    private boolean isUserInputWord() {
        return word.size() > 0;
    }


    public void setShiftListener(ShiftListener shiftListener) {
        this.shiftListener = shiftListener;
    }

    public void hideAllFields() {
        vibrate();
        for (Field field : getFieldsArrayList()) {
            field.hide();
        }
    }

    public void showAllFields() {
        vibrate();
        for (Field field : getFieldsArrayList()) {
            field.show();
        }
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE;

        public boolean isHorizontal() {
            if (this == LEFT || this == RIGHT)
                return true;
            else return false;
        }

        public boolean isSameAxis(Direction direction) {
            if (this == LEFT || this == RIGHT)
                if (direction == LEFT || direction == RIGHT)
                    return true;
                else return false;
            if (this == UP || this == DOWN)
                if (direction == UP || direction == DOWN)
                    return true;
                else return false;
            return false;
        }

        public boolean isNone() {
            return (this == NONE);
        }
    }

}
