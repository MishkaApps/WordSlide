package mb.wordslide.src.Game;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;

import mb.wordslide.R;
import mb.wordslide.src.Game.Field.FieldView1;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 26.07.2017.
 */
public class GameAreaAnimator implements View.OnTouchListener {
    private FingerPosition currentFingerPosition;
    private FingerPosition fingerDownPosition;
    private boolean inSwipe;
    private SwipeDirection swipeDirection;
    private GameArea gameArea;
    private ArrayList<FieldView1> activeFields;
    private float fieldWidth;
    private int activeRow, activeCol;
    private LayoutInflater fieldInflater;
    private GridLayout gameAreaView;


    public GameAreaAnimator(GameArea gameArea) {
        this.gameArea = gameArea;
        this.fieldWidth = 150;
        this.gapBetweenFields = 170;
        this.gameAreaOffsetX = 30;
        this.gameAreaOffsetY = 591;
        swipeDirection = new SwipeDirection();
        activeFields = new ArrayList<>();
    }

    public void onFingerDown() {
        saveFingerDownPosition();
    }

    private void saveFingerDownPosition() {
        fingerDownPosition = currentFingerPosition.copy();
    }

    public void onFingerUp() {
        resetGameAreaState();
    }

    public void onFingerMove() {
        if (!inSwipe)
            if (checkIfFingerGetOutOfRange()) {
                setSwipeDirection();
                setActiveFields();
                setDistancesToFingerForActiveFields();
//                setBorderFieldPosition();
//                createBorderField();
                inSwipe = true;
            } else return;

        continueSwipe();
    }

    private int borderFieldRow, borderFieldCol;

    private void setBorderFieldPosition() {
//        if (swipeDirection.direction == SwipeDirection.Direction.LEFT) {
//            borderFieldRow = activeRow;
//            borderFieldCol = gameArea.getGameAreaDimension() - 1;
//        } else if (swipeDirection.direction == SwipeDirection.Direction.RIGHT) {
//
//        } else if (swipeDirection.direction == SwipeDirection.Direction.UP) {
//
//        } else if (swipeDirection.direction == SwipeDirection.Direction.DOWN) {
//
//        }
    }

    private void createBorderField() {
//        GridLayout.LayoutParams layoutParams;
//        FieldView tempFieldView;
//        tempFieldView = (FieldView) fieldInflater.inflate(R.layout.field_view_template, null, false);
//        layoutParams = new GridLayout.LayoutParams();
//        layoutParams.rowSpec = GridLayout.spec(borderFieldRow);
//        layoutParams.columnSpec = GridLayout.spec(borderFieldCol);
//        layoutParams.setMargins(10, 10, 10, 10);
//        layoutParams.height = 150;
//        layoutParams.width = 150;
//        tempFieldView.setLayoutParams(layoutParams);
//        gameAreaView.addView(tempFieldView);
//        tempFieldView.setBackgroundResource(R.color.colorPrimaryDark);

    }

    private void resetGameAreaState() {
        inSwipe = false;
        swipeDirection.direction = SwipeDirection.Direction.NONE;
        resetActiveFieldsToItsInitialPositions();
    }

    private void setDistancesToFingerForActiveFields() {
        for (FieldView1 field : activeFields)
            field.setDistanceToFinger(currentFingerPosition.x - field.getPositionX(), currentFingerPosition.y - field.getPositionY());
    }

    private ArrayList<FieldView1> getAllFieldViewsList() {
        ArrayList<FieldView1> tempArrayList = new ArrayList<>();
        for (FieldView1 field : gameArea.getFieldsArrayList())
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
                activeCol = activeFields.get(0).getCol();
            }

        if (swipeDirection.isHorizontal())
            activeRow = activeFields.get(0).getRow();
        else
            activeRow = activeFields.get(0).getCol();
    }

    private float gapBetweenFields;
    private int gameAreaOffsetX, gameAreaOffsetY;

    private void continueSwipe() {
        if (swipeDirection.isHorizontal()) {
            float progress = Math.abs((currentFingerPosition.x - fingerDownPosition.x) / gapBetweenFields);
            for (FieldView1 field : activeFields) {
                float relativeFingerPositionX = currentFingerPosition.x - gameAreaOffsetX;
                if (swipeDirection.direction == SwipeDirection.Direction.LEFT) {
                    field.moveLeft(progress, relativeFingerPositionX);
                    if (isActiveFieldsShiftedLeft()) {
                        gameArea.shiftRowLeft(activeRow);
//                        updateFields();
                        resetGameAreaState();
                        imitateFingerDown();
                    }
                } else {
//                    field.moveRight(progress, relativeFingerPositionX);
//                    if (isActiveFieldsShiftedRight()) {
//
//                    }
                }
            }
        } else {
//            float progress = Math.abs((currentFingerPosition.y - fingerDownPosition.y) / gapBetweenFields);
//            for (FieldView field : activeFields) {
//                float relativeFingerPositionY = currentFingerPosition.y - gameAreaOffsetY;
//                if (swipeDirection.direction == SwipeDirection.Direction.UP) {
//                    field.moveUp(progress, relativeFingerPositionY);
//                    if (isActiveFieldsShiftedUp()) {
//
//                    }
//                } else {
//                    field.moveDown(progress, relativeFingerPositionY);
//                    if (isActiveFieldsShiftedDown()) {
//
//                    }
//                }
//            }
        }

    }

//    public void updateFields() {
//        for (FieldView1 fieldView : getAllFieldViewsList())
//            fieldView.setText(fieldView.getLetterAsString());
//    }


    private void imitateFingerDown() {
        onFingerDown();
    }


    private void resetActiveFieldsToItsInitialPositions() {
        for (FieldView1 field : activeFields) {
            field.resetPositionToInitial();
        }
    }

    private boolean isActiveFieldsShiftedLeft() {
        FieldView1 testField1 = activeFields.get(1);
        FieldView1 testField2 = activeFields.get(0);
        return testField1.getAbsolutePositionX() < testField2.getInitialPositionX();
    }

    private boolean isActiveFieldsShiftedRight() {
        return false;
    }

    private boolean isActiveFieldsShiftedUp() {
        return false;
    }

    private boolean isActiveFieldsShiftedDown() {
        return false;
    }

    private boolean isFieldInRow(FieldView1 field) {
        return fingerDownPosition.y > field.getPositionY() && fingerDownPosition.y < (field.getPositionY() + fieldWidth);
    }

    private boolean isFieldInCol(FieldView1 field) {
        return fingerDownPosition.x > field.getPositionX() && fingerDownPosition.x < (field.getPositionX() + fieldWidth);
    }


    private boolean checkIfFingerGetOutOfRange() {
        final float SWIPE_DETECT_RADIUS = 5f;
        return (Math.sqrt(Math.abs(currentFingerPosition.x - fingerDownPosition.x) + Math.abs(currentFingerPosition.y - fingerDownPosition.y)) > SWIPE_DETECT_RADIUS);

    }

    public void setFingerPosition(FingerPosition fingerPosition) {
        this.currentFingerPosition = fingerPosition;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.setFingerPosition(new FingerPosition(event.getRawX(), event.getRawY()));
        int motionEvent = event.getAction();
        if (motionEvent == MotionEvent.ACTION_DOWN) {
            this.onFingerDown();
        } else if (motionEvent == MotionEvent.ACTION_MOVE) {
            this.onFingerMove();
        } else if (motionEvent == MotionEvent.ACTION_UP) {
            this.onFingerUp();
        }
        return true;
    }

    public void setFieldInflater(LayoutInflater fieldsInflater) {
        this.fieldInflater = fieldsInflater;
    }

    public void setGameAreaView(GridLayout gameAreaView) {
        this.gameAreaView = gameAreaView;
    }
}
