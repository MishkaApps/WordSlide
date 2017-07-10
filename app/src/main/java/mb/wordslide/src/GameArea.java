package mb.wordslide.src;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import java.util.ArrayList;
import java.util.Random;

import mb.wordslide.R;

public class GameArea extends Fragment implements View.OnTouchListener, GameStateMachine.StateListener {
    private float downX, downY, startX, startY;
    private boolean inSwipe, swipeEnds;
    private SwipeDirection swipeAxis;
    private GridLayout gameGrid;
    private int fieldWidth;
    private int[] gameGridPos;
    private Vibrator vibrator;
    private float gap;
    private int dimension = 6;
    private FieldsHandler fieldsHandler;
    private WordUpdateListener wordUpdateListener;
    private Word word;
    private GameStateMachine gameStateMachine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_area, container);
        fieldsHandler = new FieldsHandler();
        fields = new ArrayList<>();
        secondaryFields = new ArrayList<>();
        primaryFields = new ArrayList<>();
        gameGrid = (GridLayout) rootView.findViewById(R.id.game_grid);
        fieldWidth = -1;
        ImmutableTable.Builder<Integer, Integer, Field> primaryFieldsBuilder = new ImmutableTable.Builder<>();
        ImmutableTable.Builder<Integer, Integer, Field> secondaryFieldsBuilder = new ImmutableTable.Builder<>();
        gameStateMachine = new GameStateMachine(this);
        LayoutInflater gameAreaInflater = getActivity().getLayoutInflater();
        Field newField;
        GridLayout.LayoutParams layoutParams;
        for (int row = 0; row < dimension; ++row)
            for (int col = 0; col < dimension; ++col) {
                newField = (Field) gameAreaInflater.inflate(R.layout.field_template, null, false);
                layoutParams = new GridLayout.LayoutParams();
                layoutParams.rowSpec = GridLayout.spec(row);
                layoutParams.columnSpec = GridLayout.spec(col);
                layoutParams.setMargins(10, 10, 10, 10);
                layoutParams.height = 150;
                layoutParams.width = 150;
                newField.setLayoutParams(layoutParams);
                gameGrid.addView(newField);
                newField.setPosition(row, col);

                fields.add(newField);
                newField.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        ((Field) v).saveOrigin();
                        fieldWidth = v.getWidth();
                        gap = primaryFields.get(1).getPosX() - primaryFields.get(0).getPosX();
                    }
                });
                newField.setType(Field.BorderType.PRIMARY, dimension);
                primaryFields.add(newField);
                primaryFieldsBuilder.put(newField.getRow(), newField.getCol(), newField);
                newField.setOnTouchListener(this);
            }

        for (int row = 0; row < dimension; ++row)
            for (int col = 0; col < dimension; ++col) {
                if (row > 0 && row < dimension - 1 && col > 0 && col < dimension - 1)
                    continue;

                newField = (Field) gameAreaInflater.inflate(R.layout.field_template, null, false);
                layoutParams = new GridLayout.LayoutParams();
                layoutParams.rowSpec = GridLayout.spec(row);
                layoutParams.columnSpec = GridLayout.spec(col);
                layoutParams.setMargins(10, 10, 10, 10);
                layoutParams.height = 150;
                layoutParams.width = 150;
                newField.setLayoutParams(layoutParams);
                gameGrid.addView(newField);
                newField.setPosition(row, col);

                fields.add(newField);

                newField.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        ((Field) v).saveOrigin();
                    }
                });

                newField.setType(Field.BorderType.SECONDARY, dimension);
                newField.hide();
                secondaryFields.add(newField);
                secondaryFieldsBuilder.put(newField.getRow(), newField.getCol(), newField);
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

//        gameGrid.setOnTouchListener(this);

        inSwipe = false;
        swipeEnds = false;
        swipeDirection = SwipeDirection.NONE;
        swipeAxis = SwipeDirection.NONE;
        vibrator = new Vibrator(getActivity());
        fieldsHandler.clean();


        Random random = new Random();
        int counter = 0;
        for (Field field : fields)
            field.setLetter((char) (65 + counter++));
//                field.setLetter((char) (65 + random.nextInt(25)));

        downField = null;
        word = new Word();

        return rootView;
    }

    private ArrayList<Field> fields;
    private ArrayList<Field> primaryFields;
    private ArrayList<Field> secondaryFields;
    private ImmutableTable<Integer, Integer, Field> _primaryFields, _secondaryFields;

    public void addOnWordUpdateListener(WordUpdateListener wordUpdateListener) {
        this.wordUpdateListener = wordUpdateListener;
    }

    @Override
    public void notice() {
        GameStateMachine.State newState = gameStateMachine.getState();
        switch (newState) {
            case A:
                onUp();
                break;
            case B:
                onDown();
                break;
            case C:
                startShift();
                break;
        }
    }

    private boolean directionCanBeDefined() {
        return (Math.sqrt(Math.abs(currentX - downX) + Math.abs(currentY - downY)) > SWIPE_DETECT_RADIUS);
    }

    private void startShift() {
        if (Math.abs(currentX - downX) >= Math.abs(currentY - downY)) {
            swipeAxis = SwipeDirection.X;

            if (swipeDirection == SwipeDirection.NONE)
                if (Math.round(currentX) < previousTouchX)
                    swipeDirection = SwipeDirection.LEFT;
                else swipeDirection = SwipeDirection.RIGHT;

            for (Field field : fieldsHandler.getPrimaryFields())
                if (downY > field.getPosY() && downY < (field.getPosY() + fieldWidth)) {
                    fieldsHandler.addActiveFields(field.getRow(), field.getCol());
                    break;
                }
        } else {
            swipeAxis = SwipeDirection.Y;

            if (swipeDirection == SwipeDirection.NONE)
                if (Math.round(currentY) < previousTouchY)
                    swipeDirection = SwipeDirection.UP;
                else swipeDirection = SwipeDirection.DOWN;

            for (Field field : fieldsHandler.getPrimaryFields())
                if (downX > field.getPosX() && downX < (field.getPosX() + fieldWidth)) {
                    fieldsHandler.addActiveFields(field.getRow(), field.getCol());
                    break;
                }
        }

        for (Field field : fieldsHandler.getActiveFields())
            field.setToFinger(currentX - field.getPosX(), currentY - field.getPosY());

        inSwipe = true;
        downX = currentX;
        downY = currentY;
        prepareFields();
    }

    public void clearWord() {
        word.clear();
        wordUpdateListener.wordUpdated(word);
        for (Field field : fieldsHandler.getPrimaryFields())
            field.deselect();
    }

    private class FieldsHandler {
        private ArrayList<Field> activeFields;
        private Field activeSecondaryFields;
        private ArrayList<Field> activePrimaryFields;
        private boolean set;

        public ArrayList<Field> getPrimaryFields() {
            return primaryFields;
        }

        public FieldsHandler() {
            clean();
        }

        public void clean() {
            activePrimaryFields = null;
            activeSecondaryFields = null;
            activeFields = null;

            set = false;
        }

        public void addActiveFields(int row, int col) {
            ImmutableMap temp = (swipeAxis == SwipeDirection.X) ?
                    _primaryFields.row(row)
                    : _primaryFields.column(col);

            activeFields = new ArrayList<>(temp.values());
            activePrimaryFields = new ArrayList<>(activeFields);
            Field tempField;
            switch (swipeDirection) {
                case LEFT:
                    tempField = activeFields.get(activeFields.size() - 1);
                    activeSecondaryFields = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
                case RIGHT:
                    tempField = activeFields.get(0);
                    activeSecondaryFields = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
                case UP:
                    tempField = activeFields.get(activeFields.size() - 1);
                    activeSecondaryFields = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
                case DOWN:
                    tempField = activeFields.get(0);
                    activeSecondaryFields = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
            }
            activeFields.add(activeSecondaryFields);
            set = true;
        }

        public ArrayList<Field> getActiveFields() {
            return activeFields;
        }

        public boolean isSet() {
            return set;
        }

        public Field getActiveSecondaryFields() {
            return activeSecondaryFields;
        }

        public ArrayList<Field> getActivePrimaryFields() {
            return activePrimaryFields;
        }
    }

    float currentX, currentY;
    float offsetX, offsetY;
    private Field downField;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        currentX = event.getRawX();
        currentY = event.getRawY();

        if (v.getClass() == Field.class)
            downField = (Field) v;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gameStateMachine.touch();
                break;
            case MotionEvent.ACTION_MOVE:
                onMove();
                break;
            case MotionEvent.ACTION_UP:
                gameStateMachine.touchEnds();
                break;
        }

        previousTouchX = currentX;
        previousTouchY = currentY;
        return true;
    }

    private void onDown() {

        startX = currentX;
        startY = currentY;

        downX = startX;
        downY = startY;

        reset();
    }

    private final float SWIPE_DETECT_RADIUS = 10f;
    private final float MAX_MOVE_DISTANCE = 10f;

    private void onMove() {
        float deltaX = currentX - previousTouchX;
        float deltaY = currentY - previousTouchY;

        if (Math.abs(deltaX) - offsetX > MAX_MOVE_DISTANCE) {
            offsetX = MAX_MOVE_DISTANCE * ((deltaX > 0) ? 1f : -1f);
            currentX = previousTouchX + offsetX;
        }

        if (Math.abs(deltaY) - offsetY > MAX_MOVE_DISTANCE) {
            offsetY = MAX_MOVE_DISTANCE * ((deltaY > 0) ? 1f : -1f);
            currentY = previousTouchY + offsetY;
        }

        if (inSwipe)
            continueSwipe();
        else if (directionCanBeDefined())
            if (!fieldsHandler.isSet())
                gameStateMachine.swipeGetOutOfRange();
    }

    private void prepareFields() {
        Field field = fieldsHandler.getActiveSecondaryFields();
        field.prepareToFlip(swipeDirection);

        switch (swipeDirection) {
            case LEFT:
                field.setLetter(_primaryFields.get(field.getRow(), 0).getLetter());
                break;
            case RIGHT:
                field.setLetter(_primaryFields.get(field.getRow(), dimension - 1).getLetter());
                break;
            case UP:
                field.setLetter(_primaryFields.get(0, field.getCol()).getLetter());
                break;
            case DOWN:
                field.setLetter(_primaryFields.get(dimension - 1, field.getCol()).getLetter());
                break;
        }
    }

    private void onUp() {
        offsetX = 0f;
        offsetY = 0f;

        if (!directionCanBeDefined())
            clickOnCell(downField);

        reset();
    }

    private void clickOnCell(Field field) {
        if (word.add(field)) {
            for (Field f : fieldsHandler.getPrimaryFields())
                f.deselect();
            for (Field f : word)
                f.select();
            wordUpdateListener.wordUpdated(word);
        }
    }

    private void reset() {
        resetFieldsToOrigin();
        fieldsHandler.clean();
        swipeDirection = SwipeDirection.NONE;
        swipeAxis = SwipeDirection.NONE;
        swipeEnds = false;
        inSwipe = false;
    }

    private void resetFieldsToOrigin() {
        if (fieldsHandler.isSet())
            for (Field field : fieldsHandler.getActiveFields())
                field.resetPositionToOrigin();
    }


    private float previousTouchX, previousTouchY;
    private SwipeDirection swipeDirection;

    private void continueSwipe() {
        if (swipeAxis == SwipeDirection.X) {
            float progress = Math.abs((currentX - downX) / gap);
            for (Field field : fieldsHandler.getActiveFields())
                field.animate(swipeDirection, progress, currentX - gameGridPos[0]);
        } else {
            float progress = Math.abs((currentY - downY) / gap);
            for (Field field : fieldsHandler.getActiveFields())
                field.animate(swipeDirection, progress, currentY - gameGridPos[1]);
        }
        checkOriginMatches();
    }

    private void checkOriginMatches() {
        ArrayList<Field> primaryFields = fieldsHandler.getActivePrimaryFields();
        Field checkField = primaryFields.get(1);
        Field neighboringField;
        boolean match = false;
        switch (swipeDirection) {
            case LEFT:
                neighboringField = primaryFields.get(0);
                if (checkField.getX() < neighboringField.getOriginX())
                    match = true;
                break;
            case RIGHT:
                neighboringField = primaryFields.get(2);
                if (checkField.getX() > neighboringField.getOriginX())
                    match = true;
                break;
            case UP:
                neighboringField = primaryFields.get(0);
                if (checkField.getY() < neighboringField.getOriginY())
                    match = true;
                break;
            case DOWN:
                neighboringField = primaryFields.get(2);
                if (checkField.getY() > neighboringField.getOriginY())
                    match = true;
                break;
        }

        if (match)
            originsMatch();
    }

    private void originsMatch() {
        vibrator.vibrate();
        shift();
        gameStateMachine.shifted();
    }

    private void shift() {
        char tempChar;
        ArrayList<Field> primaryFields = fieldsHandler.getActivePrimaryFields();
        switch (swipeDirection) {
            case LEFT:
                tempChar = primaryFields.get(0).getLetter();
                for (int index = 0; index < primaryFields.size() - 1; index++) {
                    primaryFields.get(index).setLetter(primaryFields.get(index + 1).getLetter());
                }
                primaryFields.get(primaryFields.size() - 1).setLetter(tempChar);
                break;
            case RIGHT:
                tempChar = primaryFields.get(dimension - 1).getLetter();
                for (int index = dimension - 1; index > 0; index--) {
                    primaryFields.get(index).setLetter(primaryFields.get(index - 1).getLetter());
                }
                primaryFields.get(0).setLetter(tempChar);
                break;
            case UP:
                tempChar = primaryFields.get(0).getLetter();
                for (int index = 0; index < primaryFields.size() - 1; index++) {
                    primaryFields.get(index).setLetter(primaryFields.get(index + 1).getLetter());
                }
                primaryFields.get(primaryFields.size() - 1).setLetter(tempChar);
                break;
            case DOWN:
                tempChar = primaryFields.get(dimension - 1).getLetter();
                for (int index = dimension - 1; index > 0; index--) {
                    primaryFields.get(index).setLetter(primaryFields.get(index - 1).getLetter());
                }
                primaryFields.get(0).setLetter(tempChar);
                break;
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
