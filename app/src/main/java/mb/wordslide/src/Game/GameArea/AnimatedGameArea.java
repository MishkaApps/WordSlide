package mb.wordslide.src.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;

import mb.wordslide.R;
import mb.wordslide.src.Game.Field.BorderField;
import mb.wordslide.src.Game.Field.FieldView1;
import mb.wordslide.src.Vibrator;

/**
 * Created by mbolg on 26.07.2017.
 */
public class AnimatedGameArea extends StaticGameArea implements View.OnTouchListener, OnClearWordListener {
    private FingerPosition currentFingerPosition;
    private FingerPosition fingerDownPosition;
    private boolean inSwipe;
    private SwipeDirection swipeDirection;
    private ArrayList<FieldView1> activeFields;
    private int activeRow, activeCol;
    private BorderField borderField;
    private FieldView1 touchedField;
    private Word word;
    private boolean fingerDownImitated;
    private Vibrator vibrator;

    public AnimatedGameArea(GridLayout gameAreaGrid,
                            LayoutInflater inflater, Context context) {
        super(6, inflater, gameAreaGrid);

        swipeDirection = new SwipeDirection();
        activeFields = new ArrayList<>();
        word = new Word();
        vibrator = new Vibrator(context);
        setOnTouchListenersForFieldViews();
    }


    private void setOnTouchListenersForFieldViews() {
        for (FieldView1 field : getFieldsArrayList())
            field.setOnTouchListener(this);
    }

    public void onFingerDown() {
        saveFingerDownPosition();
    }

    private void saveFingerDownPosition() {
        fingerDownPosition = currentFingerPosition.copy();
    }

    public void onFingerUp() {
        if (!isFingerGetOutOfRange() && !fingerDownImitated)
            onFieldClick();

        resetGameAreaState();
    }

    private void onFieldClick() {
        word.add(touchedField);
        updateSelectedFieldBackground();
        notifyWordChanged();
    }

    private void notifyWordChanged() {
        for (OnWordChangedListener onWordChangedListener : wordChangedListener)
            onWordChangedListener.wordChanged(word);
    }

    private void updateSelectedFieldBackground() {
        for (FieldView1 field : getFieldsArrayList()) {
            if (word.contains(field))
                field.setSelectedBackground();
            else field.setDefaultBackground();
        }
    }

    public void onFingerMove() {
        setSwipeDirection();
        if (!inSwipe)
            if (isFingerGetOutOfRange()) {
                setActiveFields();
                setActiveRowOrCol();
                setDistancesToFingerForActiveFields();
                createBorderField();
                inSwipe = true;
            } else return;

        continueSwipe();
    }

    private void createBorderField() {
        int borderFieldRow = getBorderFieldRow();
        int borderFieldCol = getBorderFieldCol();
        inflateBorderField(borderFieldRow, borderFieldCol);
        borderField.setPosition(borderFieldRow, borderFieldCol);
        borderField.setLetter(getBorderFieldLetter());
        saveInitialBorderFieldPosition();
//        borderField.setStartPositionBeforeAnimateLeft();    todo: Эта хуета вообще что то делает?
        borderField.setGameAreaDimension(gameAreaDimension);
        activeFields.add(borderField);
        gameAreaGrid.addView(borderField);
    }

    private char getBorderFieldLetter() {
        if (swipeDirection.direction == SwipeDirection.Direction.LEFT)
            return activeFields.get(0).getLetter();
        else if (swipeDirection.direction == SwipeDirection.Direction.RIGHT)
            return activeFields.get(gameAreaDimension - 1).getLetter();
        else if (swipeDirection.direction == SwipeDirection.Direction.UP)
            return activeFields.get(0).getLetter();
        else if (swipeDirection.direction == SwipeDirection.Direction.DOWN)
            return activeFields.get(gameAreaDimension - 1).getLetter();
        return 0;
    }

    private void saveInitialBorderFieldPosition() {
        borderField.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ((FieldView1) v).saveInitialPosition();
            }
        });
    }

    private void inflateBorderField(int row, int col) {
        GridLayout.LayoutParams layoutParams;
        borderField = (BorderField) inflater.inflate(R.layout.border_field_view_template, null, false);
        layoutParams = new GridLayout.LayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.height = 150;
        layoutParams.width = 150;
        borderField.setLayoutParams(layoutParams);
    }

    private void resetGameAreaState() {
        fingerDownImitated = false;
        inSwipe = false;
        swipeDirection.direction = SwipeDirection.Direction.NONE;
        resetActiveFieldsToItsInitialState();
        removeBorderFieldFromGameArea();
    }

    private void removeBorderFieldFromGameArea() {
        gameAreaGrid.removeView(borderField);
    }

    private void setDistancesToFingerForActiveFields() {
        for (FieldView1 field : activeFields)
            field.setDistanceToFinger(currentFingerPosition.x - field.getPositionX(), currentFingerPosition.y - field.getPositionY());
    }

    private ArrayList<FieldView1> getAllFieldViewsList() {
        ArrayList<FieldView1> tempArrayList = new ArrayList<>();
        for (FieldView1 field : getFieldsArrayList())
            tempArrayList.add(field);
        return tempArrayList;
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
        activeFields = new ArrayList<>();

        for (FieldView1 fieldView : getAllFieldViewsList())
            if (swipeDirection.isHorizontal()) {
                if (isFieldInRow(fieldView))
                    activeFields.add(fieldView);
            } else {
                if (isFieldInCol(fieldView))
                    activeFields.add(fieldView);
            }
    }

    private void setActiveRowOrCol() {
        if (swipeDirection.isHorizontal())
            activeRow = activeFields.get(0).getRow();
        else
            activeCol = activeFields.get(0).getCol();
    }


    private void continueSwipe() {
        float progress = 0;
        if (swipeDirection.isHorizontal()) {
            if (swipeDirection.direction == SwipeDirection.Direction.LEFT)
                progress = calculateProgressWhileMovingLeft();
            else if (swipeDirection.direction == SwipeDirection.Direction.RIGHT)
                progress = calculateProgressWhileMovingRight();

            for (FieldView1 field : activeFields) {
                float relativeFingerPositionX = currentFingerPosition.x - gameAreaOffsetX;
                if (swipeDirection.direction == SwipeDirection.Direction.LEFT) {
                    field.animateLeft(progress, relativeFingerPositionX);
                    if (isActiveFieldsShiftedLeft()) {
                        shiftRowLeft(activeRow);
                        onShiftComplete();
                        break;
                    }
                } else if (swipeDirection.direction == SwipeDirection.Direction.RIGHT) {
                    field.animateRight(progress, relativeFingerPositionX);
                    if (isActiveFieldsShiftedRight()) {
                        shiftRowRight(activeRow);
                        onShiftComplete();
                        break;
                    }
                }
            }
        } else {
            if (swipeDirection.direction == SwipeDirection.Direction.UP)
                progress = calculateProgressWhileMovingUp();
            else if (swipeDirection.direction == SwipeDirection.Direction.DOWN)
                progress = calculateProgressWhileMovingDown();

            for (FieldView1 field : activeFields) {
                float relativeFingerPositionY = currentFingerPosition.y - gameAreaOffsetY;
                if (swipeDirection.direction == SwipeDirection.Direction.UP) {
                    field.animateUp(progress, relativeFingerPositionY);
                    if (isActiveFieldsShiftedUp()) {
                        shiftColUp(activeCol);
                        onShiftComplete();
                        break;
                    }
                } else if (swipeDirection.direction == SwipeDirection.Direction.DOWN) {
                    field.animateDown(progress, relativeFingerPositionY);
                    if (isActiveFieldsShiftedDown()) {
                        shiftColDown(activeCol);
                        onShiftComplete();
                        break;
                    }
                }
            }
        }

    }

    private void onShiftComplete(){
        resetGameAreaState();
        imitateFingerDown();
        vibrate();
        shiftListener.shifted();
    }

    private void vibrate() {
        vibrator.vibrate();
    }

    private float calculateProgressWhileMovingLeft() {
        FieldView1 testField1 = activeFields.get(1);
        FieldView1 testField2 = activeFields.get(0);
        float progress = 1f - Math.abs((testField1.getAbsolutePositionX() - testField2.getInitialPositionX()) / (testField1.getInitialPositionX() - testField2.getInitialPositionX()));
        return progress;
    }

    private float calculateProgressWhileMovingRight() {
        FieldView1 testField1 = activeFields.get(0);
        FieldView1 testField2 = activeFields.get(1);
        float progress = 1f - Math.abs((testField1.getAbsolutePositionX() - testField2.getInitialPositionX()) / (testField1.getInitialPositionX() - testField2.getInitialPositionX()));
        return progress;
    }

    private float calculateProgressWhileMovingUp() {
        FieldView1 testField1 = activeFields.get(1);
        FieldView1 testField2 = activeFields.get(0);
        float progress = 1f - Math.abs((testField1.getAbsolutePositionY() - testField2.getInitialPositionY()) / (testField1.getInitialPositionY() - testField2.getInitialPositionY()));
        return progress;
    }

    private float calculateProgressWhileMovingDown() {
        FieldView1 testField1 = activeFields.get(0);
        FieldView1 testField2 = activeFields.get(1);
        float progress = 1f - Math.abs((testField1.getAbsolutePositionY() - testField2.getInitialPositionY()) / (testField1.getInitialPositionY() - testField2.getInitialPositionY()));
        return progress;
    }
//    public void updateFields() {
//        for (FieldView1 fieldView : getAllFieldViewsList())
//            fieldView.setText(fieldView.getLetterAsString());
//    }

    private void imitateFingerDown() {
        fingerDownImitated = true;
        onFingerDown();
    }

    private void resetActiveFieldsToItsInitialState() {
        for (FieldView1 field : activeFields) {
            field.resetPositionToInitial();
            field.resetRotationToInitial();
        }
    }

    private boolean isActiveFieldsShiftedLeft() {
        FieldView1 testField1 = activeFields.get(1);
        FieldView1 testField2 = activeFields.get(0);
        return testField1.getAbsolutePositionX() < testField2.getInitialPositionX();
    }

    private boolean isActiveFieldsShiftedRight() {
        FieldView1 testField1 = activeFields.get(0);
        FieldView1 testField2 = activeFields.get(1);
        return testField1.getAbsolutePositionX() > testField2.getInitialPositionX();
    }

    private boolean isActiveFieldsShiftedUp() {
        FieldView1 testField1 = activeFields.get(1);
        FieldView1 testField2 = activeFields.get(0);
        return testField1.getAbsolutePositionY() < testField2.getInitialPositionY();
    }

    private boolean isActiveFieldsShiftedDown() {
        FieldView1 testField1 = activeFields.get(0);
        FieldView1 testField2 = activeFields.get(1);
        return testField1.getAbsolutePositionY() > testField2.getInitialPositionY();
    }

    private boolean isFieldInRow(FieldView1 field) {
        return fingerDownPosition.y > field.getPositionY() && fingerDownPosition.y < (field.getPositionY() + FIELD_SIZE);
    }

    private boolean isFieldInCol(FieldView1 field) {
        return fingerDownPosition.x > field.getPositionX() && fingerDownPosition.x < (field.getPositionX() + FIELD_SIZE);
    }


    private boolean isFingerGetOutOfRange() {
        final float SWIPE_DETECT_RADIUS = 5f;
        return (Math.sqrt(Math.abs(currentFingerPosition.x - fingerDownPosition.x) + Math.abs(currentFingerPosition.y - fingerDownPosition.y)) > SWIPE_DETECT_RADIUS);

    }

    public void setFingerPosition(FingerPosition fingerPosition) {
        this.currentFingerPosition = fingerPosition;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchedField = (FieldView1) v;
        this.setFingerPosition(new FingerPosition(event.getRawX(), event.getRawY()));
        int motionEvent = event.getAction();
        if (motionEvent == MotionEvent.ACTION_DOWN) {
            this.onFingerDown();
        } else if (motionEvent == MotionEvent.ACTION_MOVE) {
            if (!isUserSelectWord())
                this.onFingerMove();
        } else if (motionEvent == MotionEvent.ACTION_UP) {
            this.onFingerUp();
        }
        return true;
    }

    private boolean isUserSelectWord() {
        return word.size() > 0;
    }

    public int getBorderFieldRow() {
        if (swipeDirection.isHorizontal()) {
            return this.activeRow;
        } else if (swipeDirection.direction == SwipeDirection.Direction.UP) {
            return gameAreaDimension - 1;
        } else if (swipeDirection.direction == SwipeDirection.Direction.DOWN) {
            return 0;
        }
        return 0;
    }

    public int getBorderFieldCol() {
        if (!swipeDirection.isHorizontal()) {
            return this.activeCol;
        } else if (swipeDirection.direction == SwipeDirection.Direction.LEFT) {
            return gameAreaDimension - 1;
        } else if (swipeDirection.direction == SwipeDirection.Direction.RIGHT) {
            return 0;
        }
        return 0;
    }


    @Override
    public void notifyWordCleared() {
        word.clear();
        updateSelectedFieldBackground();
        notifyWordChanged();
    }

    @Override
    public void notifyWordUsed() {
        setClearedFields();
        word.clear();
        updateSelectedFieldBackground();
        notifyWordChanged();

    }


    private void setClearedFields() {
        for (FieldView1 field : word) {
            hideField(field);
            setNewLetter(field);
            showField(field);
        }
    }

    private void hideField(FieldView1 field) {
        field.hide();
    }

    private void showField(FieldView1 field) {
        field.show();
    }

}
