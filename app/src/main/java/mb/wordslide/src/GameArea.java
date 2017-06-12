package mb.wordslide.src;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.google.common.collect.ImmutableTable;

import java.util.ArrayList;
import java.util.Random;

import mb.wordslide.R;

// TODO: Добавить анимацию возврата при отпускании пальца от экрана
public class GameArea extends Fragment implements View.OnTouchListener {
    private ArrayList<Field> fields;
    private float downX, downY;
    private boolean inSwipe, swipeEnds;
    private ArrayList<Field> activeFields;
    private ArrayList<Field> activePrimaryFields;
    private ArrayList<Field> primaryFields;
    private ImmutableTable<Integer, Integer, Field> _primaryFields, _secondaryFields;
    private ArrayList<Field> secondaryFields;
    private SwipeDirection swipeAxis;
    private ViewGroup gameGrid;
    private int fieldWidth;
    private int[] gameGridPos;
    private Vibrator vibrator;
    private Kernel kernel;
    private float gap;

    /**
     * Наполнение фрагмента с игровым полем
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_area, container);
        fields = new ArrayList<>();
        secondaryFields = new ArrayList<>();
        primaryFields = new ArrayList<>();
        gameGrid = (ViewGroup) rootView.findViewById(R.id.game_grid);
        fieldWidth = -1;
        ImmutableTable.Builder<Integer, Integer, Field> primaryFieldsBuilder = new ImmutableTable.Builder<>();
        ImmutableTable.Builder<Integer, Integer, Field> secondaryFieldsBuilder = new ImmutableTable.Builder<>();
        /**
         * Перебор всех дочерних элементов, для наполнения массивов
         * c ячейками
         */
        for (int index = 0; index < gameGrid.getChildCount(); ++index) {
            Field newField = (Field) gameGrid.getChildAt(index);
            fields.add(newField);
            newField.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    ((Field) v).saveOrigin();
                    if (fieldWidth == -1)
                        fieldWidth = v.getWidth();
                    float tempGap = primaryFields.get(1).getPosX() - primaryFields.get(0).getPosX();
                    if (tempGap > 0) {
                        gap = tempGap;
                        for (Field field : fields) {
                            field.setGap(gap);
                        }
                    }
                }
            });
            String borderTag = getActivity().getResources().getString(R.string.border_tag);
            if (borderTag.equals(newField.getTag())) {
                newField.setType(Field.BorderType.SECONDARY);
                newField.hide();
                secondaryFields.add(newField);
                secondaryFieldsBuilder.put(newField.getRow(), newField.getCol(), newField);
            } else {
                newField.setType(Field.BorderType.PRIMARY);
                primaryFields.add(newField);
                primaryFieldsBuilder.put(newField.getRow(), newField.getCol(), newField);
            }
            newField.setOnTouchListener(this);
        }

        _primaryFields = primaryFieldsBuilder.build();
        _secondaryFields = secondaryFieldsBuilder.build();


        gameGrid.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                gameGridPos = new int[2];
                gameGrid.getLocationOnScreen(gameGridPos);
            }
        });

        inSwipe = false;
        swipeEnds = false;
        swipeDirection = SwipeDirection.NONE;
        swipeAxis = SwipeDirection.NONE;
        vibrator = new Vibrator(getActivity());
        kernel = new Kernel();

        return rootView;
    }

    float currentX;
    float currentY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        currentX = event.getRawX();
        currentY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onDown();
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onUp();
                break;
        }
        return true;
    }

    private void onDown() {
        downX = currentX;
        downY = currentY;
        reset();
    }

    final float SWIPE_DETECT_RADIUS = 5;

    private void onMove(MotionEvent event) {
        if (swipeEnds)
            return;

        if (inSwipe) {
            continueSwipe(event);
        } else {
            if (Math.sqrt(Math.abs(currentX - downX) + Math.abs(currentY - downY)) > SWIPE_DETECT_RADIUS) {
                activeFields = new ArrayList<>();
                activePrimaryFields = new ArrayList<>();

                if (Math.abs(currentX - downX) >= Math.abs(currentY - downY)) {
                    swipeAxis = SwipeDirection.X;
                    for (Field field : fields)
                        if (downY > field.getPosY() && downY < (field.getPosY() + fieldWidth)) {
                            if (field.isSecondary())
                                if (field.getBorderPosition() == Field.BorderPosition.T
                                        || field.getBorderPosition() == Field.BorderPosition.B)
                                    continue;
                            field.setToFinger(event.getRawX() - field.getPosX(), event.getRawY() - field.getPosY());
                            activeFields.add(field);
                            if (!field.isSecondary())
                                activePrimaryFields.add(field);
                        }

                    if (swipeDirection == SwipeDirection.NONE)
                        if (Math.round(event.getRawX()) < previousTouchX)
                            swipeDirection = SwipeDirection.LEFT;
                        else swipeDirection = SwipeDirection.RIGHT;

                } else {
                    swipeAxis = SwipeDirection.Y;
                    for (Field field : fields)
                        if (downX > field.getPosX() && downX < (field.getPosX() + fieldWidth)) {
                            if (field.isSecondary())
                                if (field.getBorderPosition() == Field.BorderPosition.L
                                        || field.getBorderPosition() == Field.BorderPosition.R)
                                    continue;
                            field.setToFinger(event.getRawX() - field.getPosX(), event.getRawY() - field.getPosY());
                            activeFields.add(field);
                            if (!field.isSecondary())
                                activePrimaryFields.add(field);
                        }

                    if (swipeDirection == SwipeDirection.NONE)
                        if (Math.round(event.getRawY()) < previousTouchY)
                            swipeDirection = SwipeDirection.UP;
                        else swipeDirection = SwipeDirection.DOWN;
                }
                inSwipe = true;
                downX = currentX;
                downY = currentY;
                prepareFields();
            }
        }

        previousTouchX = currentX;
        previousTouchY = currentY;
    }

    private void prepareFields() {
        for (Field field : activeFields) {
            if (field.isSecondary()) {
                field.prepareToFlip(swipeDirection);
            }
        }
    }

    private void onUp() {
        swipeEnds = false;
        reset();
    }

    private void reset() {
        if (activeFields != null)
            for (Field field : activeFields) {
                field.resetPositionToOrigin();
            }
        swipeDirection = SwipeDirection.NONE;
        swipeAxis = SwipeDirection.NONE;
        inSwipe = false;
    }


    private float previousTouchX, previousTouchY;
    private SwipeDirection swipeDirection;

    private void continueSwipe(MotionEvent event) {
        if (swipeAxis == SwipeDirection.X) {
            float progress = Math.abs((event.getRawX() - downX) / gap);
            for (Field field : activeFields)
                field.animate(swipeDirection, progress, event.getRawX() - gameGridPos[0]);
        } else {
            float progress = Math.abs((event.getRawY() - downY) / gap);
            for (Field field : activeFields)
                field.animate(swipeDirection, progress, event.getRawY() - gameGridPos[0]);
        }

        checkOriginMatches();
    }

    private void checkOriginMatches() {
        Field checkField = activePrimaryFields.get(1);
        Field neighboringField;
        boolean match = false;
        switch (swipeDirection) {
            case LEFT:
                neighboringField = activePrimaryFields.get(0);
                if (checkField.getX() < neighboringField.getOriginX())
                    match = true;
                break;
            case RIGHT:
                neighboringField = activePrimaryFields.get(2);
                if (checkField.getX() > neighboringField.getOriginX())
                    match = true;
                break;
            case UP:
                neighboringField = activeFields.get(0);
                if (checkField.getY() < neighboringField.getOriginY())
                    match = true;
                break;
            case DOWN:
                neighboringField = activeFields.get(2);
                if (checkField.getY() > neighboringField.getOriginY())
                    match = true;
                break;
        }

        if (match)
            originsMatch();
    }

    private void originsMatch() {
        vibrator.vibrate();
        kernel.shift();
        swipeEnds = true;
        onUp();
        onDown();
    }

    private final int FIELDS_IN_ROW = 4;
    private class Kernel {

        public Kernel() {
            Random random = new Random();
            for (Field field : fields)
                field.setLetter((char) (65 + random.nextInt(25)));
            updateSecondaryFields();
        }

        public void updateSecondaryFields() {
            for (Field field : secondaryFields) {
                switch (field.getBorderPosition()) {
                    case RT:
                    case R:
                    case RB:
                        field.setLetter(_primaryFields.get(field.getRow(), 0).getLetter());
                        break;
                    case LT:
                    case L:
                    case LB:
                        field.setLetter(_primaryFields.get(field.getRow(), FIELDS_IN_ROW - 1).getLetter());
                        break;
                }
            }
        }

        public void shift() {
            char tempChar;
            switch (swipeDirection) {
                case LEFT:
                    tempChar = activePrimaryFields.get(0).getLetter();
                    for (int index = 0; index < activePrimaryFields.size() - 1; index++) {
                        activePrimaryFields.get(index).setLetter(activePrimaryFields.get(index + 1).getLetter());
                    }
                    activePrimaryFields.get(activePrimaryFields.size() - 1).setLetter(tempChar);
                    break;
                case RIGHT:
                    tempChar = activePrimaryFields.get(FIELDS_IN_ROW - 1).getLetter();
                    for (int index = FIELDS_IN_ROW - 1; index > 0; index--) {
                        activePrimaryFields.get(index).setLetter(activePrimaryFields.get(index - 1).getLetter());
                    }
                    activePrimaryFields.get(0).setLetter(tempChar);
                    break;
            }
            updateSecondaryFields();
        }
    }

    private class Vibrator {
        private android.os.Vibrator vibrator;

        public Vibrator(Context context) {
            vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        public void vibrate() {
            vibrator.vibrate(10);
        }
    }
}
