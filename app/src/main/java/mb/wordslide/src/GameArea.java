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
import mb.wordslide.src.Activities.GameActivity;

public class GameArea extends Fragment implements View.OnTouchListener {
    private float downX, downY;
    private boolean inSwipe, swipeEnds;
    private SwipeDirection swipeAxis;
    private GridLayout gameGrid;
    private int fieldWidth;
    private int[] gameGridPos;
    private Vibrator vibrator;
    private float gap;
    private int FIELDS_IN_ROW = 6;
    private FieldsHandler fieldsHandler;
    private CellSelectionListener cellSelectionListener;
    private Word word;
    private GameStateMachine gameStateMachine;

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
        fieldsHandler = new FieldsHandler();
        fields = new ArrayList<>();
        secondaryFields = new ArrayList<>();
        primaryFields = new ArrayList<>();
        gameGrid = (GridLayout) rootView.findViewById(R.id.game_grid);
        fieldWidth = -1;
        ImmutableTable.Builder<Integer, Integer, Field> primaryFieldsBuilder = new ImmutableTable.Builder<>();
        ImmutableTable.Builder<Integer, Integer, Field> secondaryFieldsBuilder = new ImmutableTable.Builder<>();
        gameStateMachine = new GameStateMachine();

        LayoutInflater gameAreaInflater = getActivity().getLayoutInflater();
        Field newField;
        GridLayout.LayoutParams layoutParams;
        for (int row = 0; row < FIELDS_IN_ROW; ++row)
            for (int col = 0; col < FIELDS_IN_ROW; ++col) {
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
                newField.setType(Field.BorderType.PRIMARY, FIELDS_IN_ROW);
                primaryFields.add(newField);
                primaryFieldsBuilder.put(newField.getRow(), newField.getCol(), newField);
                newField.setOnTouchListener(this);
            }

        for (int row = 0; row < FIELDS_IN_ROW; ++row)
            for (int col = 0; col < FIELDS_IN_ROW; ++col) {
                if (row > 0 && row < FIELDS_IN_ROW - 1 && col > 0 && col < FIELDS_IN_ROW - 1)
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

                newField.setType(Field.BorderType.SECONDARY, FIELDS_IN_ROW);
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

        gameGrid.setOnTouchListener(this);

        inSwipe = false;
        swipeEnds = false;
        swipeDirection = SwipeDirection.NONE;
        swipeAxis = SwipeDirection.NONE;
        vibrator = new Vibrator(getActivity());
        fieldsHandler.clean();
        fieldsIsSet = false;


        Random random = new Random();
        int counter = 0;
        for (Field field : fields)  //
            field.setLetter((char) (65 + counter++));
//                field.setLetter((char) (65 + random.nextInt(25)));

        return rootView;
    }

    private ArrayList<Field> fields;
    private ArrayList<Field> primaryFields;
    private ArrayList<Field> secondaryFields;
    private ImmutableTable<Integer, Integer, Field> _primaryFields, _secondaryFields;

    public void addOnCellSelectedListener(CellSelectionListener cellSelectionListener, Word word) {
        this.cellSelectionListener = cellSelectionListener;
        this.word = word;
    }

    private class FieldsHandler {
        private ArrayList<Field> activeFields;
        private Field activeSecondaryField;
        private ArrayList<Field> activePrimaryFields;

        public ArrayList<Field> getPrimaryFields() {
            return primaryFields;
        }

        public void clean() {
            activePrimaryFields = null;
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
                    activeSecondaryField = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
                case RIGHT:
                    tempField = activeFields.get(0);
                    activeSecondaryField = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
                case UP:
                    tempField = activeFields.get(activeFields.size() - 1);
                    activeSecondaryField = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
                case DOWN:
                    tempField = activeFields.get(0);
                    activeSecondaryField = _secondaryFields.get(tempField.getRow(), tempField.getCol());
                    break;
            }
            activeFields.add(activeSecondaryField);
        }

        public ArrayList<Field> getActiveFields() {
            return activeFields;
        }

        public boolean isActiveFieldsSet() {
            return activeFields != null;
        }

        public Field getActiveSecondaryField() {
            return activeSecondaryField;
        }

        public ArrayList<Field> getActivePrimaryFields() {
            return activePrimaryFields;
        }
    }

    float currentX;
    float currentY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        currentX = event.getRawX();
        currentY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gameStateMachine.touch();
                onDown();
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                break;
            case MotionEvent.ACTION_UP:
                gameStateMachine.touchEnds();
                fieldsIsSet = false;
                fieldsHandler.clean();
                swipeDirection = SwipeDirection.NONE;
                swipeAxis = SwipeDirection.NONE;
                onUp((v.getClass() == Field.class)?(Field)v:null);
                break;
        }
        return true;
    }

    private void onDown() {
        downX = currentX;
        downY = currentY;
        reset();
    }

    private final float SWIPE_DETECT_RADIUS = 5;
    private boolean fieldsIsSet;

    private void onMove(MotionEvent event) {
        if (inSwipe) {
            continueSwipe(event);
        } else {
            if (Math.sqrt(Math.abs(currentX - downX) + Math.abs(currentY - downY)) > SWIPE_DETECT_RADIUS) {
                gameStateMachine.swipeGetOutOfRange();
                if (!fieldsIsSet) {
                    if (Math.abs(currentX - downX) >= Math.abs(currentY - downY)) {
                        swipeAxis = SwipeDirection.X;

                        if (swipeDirection == SwipeDirection.NONE)
                            if (Math.round(event.getRawX()) < previousTouchX)
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
                            if (Math.round(event.getRawY()) < previousTouchY)
                                swipeDirection = SwipeDirection.UP;
                            else swipeDirection = SwipeDirection.DOWN;

                        for (Field field : fieldsHandler.getPrimaryFields())
                            if (downX > field.getPosX() && downX < (field.getPosX() + fieldWidth)) {
                                fieldsHandler.addActiveFields(field.getRow(), field.getCol());
                                break;
                            }
                    }
                    fieldsIsSet = true;
                }

                /**
                 * Логическое условие - для проверки, если касание было между полей
                 */
                if (fieldsHandler.getActivePrimaryFields() != null) {
                    for (Field field : fieldsHandler.getActiveFields())
                        field.setToFinger(currentX - field.getPosX(), currentY - field.getPosY());

                    inSwipe = true;
                    downX = currentX;
                    downY = currentY;
                    prepareFields();
                }
            }
        }

        previousTouchX = currentX;
        previousTouchY = currentY;
    }

    /**
     * Метод prepareFields() разворачивает вторичные активные поля
     * и устанавливает для них соответствующие буквы
     */
    private void prepareFields() {
        Field field = fieldsHandler.getActiveSecondaryField();
        field.prepareToFlip(swipeDirection);

        switch (swipeDirection) {
            case LEFT:
                field.setLetter(_primaryFields.get(field.getRow(), 0).getLetter());
                break;
            case RIGHT:
                field.setLetter(_primaryFields.get(field.getRow(), FIELDS_IN_ROW - 1).getLetter());
                break;
            case UP:
                field.setLetter(_primaryFields.get(0, field.getCol()).getLetter());
                break;
            case DOWN:
                field.setLetter(_primaryFields.get(FIELDS_IN_ROW - 1, field.getCol()).getLetter());
                break;
        }
    }

    private void onUp(Field field) {
        if(field != null && !inSwipe){
            selectCell(field);
        }
        swipeEnds = false;
        reset();
    }

    private void selectCell(Field field) {
        if(word.add(field)) {
            field.setBackgroundResource(R.drawable.selected_field);
            cellSelectionListener.nextLetter();
        }
    }

    private void reset() {
        if (fieldsHandler.isActiveFieldsSet())
            for (Field field : fieldsHandler.getActiveFields()) {
                field.resetPositionToOrigin();
            }
        inSwipe = false;
    }



    private float previousTouchX, previousTouchY;
    private SwipeDirection swipeDirection;

    private void continueSwipe(MotionEvent event) {
        if (swipeAxis == SwipeDirection.X) {
            float progress = Math.abs((event.getRawX() - downX) / gap);
            for (Field field : fieldsHandler.getActiveFields())
                field.animate(swipeDirection, progress, event.getRawX() - gameGridPos[0]);
        } else {
            float progress = Math.abs((event.getRawY() - downY) / gap);
            for (Field field : fieldsHandler.getActiveFields())
                field.animate(swipeDirection, progress, event.getRawY() - gameGridPos[1]);
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
        gameStateMachine.shifted();
        vibrator.vibrate();
        shift();
        swipeEnds = true;
        onUp(null);
        onDown();
    }

    /**
     * Метод shift() вызывается, в момент когда ряд или столбец
     * были сдвинуты на одну ячейку в сторону.
     * <p/>
     * Метод переназначает каждой сдвинутой ячейке новую букву
     * в зависимости от соседней от нее ячейки и
     * направления сдвига
     */
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
                tempChar = primaryFields.get(FIELDS_IN_ROW - 1).getLetter();
                for (int index = FIELDS_IN_ROW - 1; index > 0; index--) {
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
                tempChar = primaryFields.get(FIELDS_IN_ROW - 1).getLetter();
                for (int index = FIELDS_IN_ROW - 1; index > 0; index--) {
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
