package mb.wordslide.src.Game;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import com.google.common.collect.ImmutableTable;

import java.util.ArrayList;
import java.util.Random;

import mb.wordslide.R;
import mb.wordslide.src.Game.Field.Field;
import mb.wordslide.src.Game.Field.FieldView1;

/**
 * Created by mbolg on 26.07.2017.
 */
public class GameArea {
    protected GridLayout gameAreaGrid;
    protected static final int FIELD_SIZE = 150;
    private ImmutableTable<Integer, Integer, FieldView1> fields;
    protected int gameAreaDimension;
    protected LayoutInflater inflater;
    protected int gameAreaOffsetX, gameAreaOffsetY;
    protected ArrayList<OnWordChangedListener> wordChangedListener;

    public GameArea(int areaDimension, LayoutInflater inflater, GridLayout gameAreaGrid) {
        this.gameAreaDimension = areaDimension;
        this.inflater = inflater;
        this.gameAreaGrid = gameAreaGrid;
        wordChangedListener = new ArrayList<>();

        ImmutableTable.Builder<Integer, Integer, FieldView1> fieldsBuilder = new ImmutableTable.Builder<>();
        ArrayList<FieldView1> inflatedFields = inflateFields();
        for (FieldView1 field : inflatedFields)
            fieldsBuilder.put(field.getRow(), field.getCol(), field);
        this.fields = fieldsBuilder.build();


        for (FieldView1 field : getFieldsArrayList())
            gameAreaGrid.addView(field);
        saveInitialFieldsPositions();
        saveGameAreaOffset();
        fillAreaWithLetters();
    }

    private void saveGameAreaOffset() {
        gameAreaGrid.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int[] offset = new int[2];
                gameAreaGrid.getLocationOnScreen(offset);
                gameAreaOffsetX = offset[0];
                gameAreaOffsetY = offset[1];
            }
        });
    }

    private ArrayList<FieldView1> inflateFields() {
        ArrayList<FieldView1> fields = new ArrayList<>();
        FieldView1 tempFieldView;
        for (int row = 0; row < gameAreaDimension; ++row)
            for (int col = 0; col < gameAreaDimension; ++col) {
                tempFieldView = inflateField(row, col);
                tempFieldView.setPosition(row, col);
                tempFieldView.setGameAreaDimension(gameAreaDimension);
                fields.add(tempFieldView);
            }
        return fields;
    }

    private FieldView1 inflateField(int row, int col) {
        GridLayout.LayoutParams layoutParams;
        FieldView1 tempFieldView;
        tempFieldView = (FieldView1) inflater.inflate(R.layout.field_view_template, null, false);
        layoutParams = new GridLayout.LayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.height = FIELD_SIZE;
        layoutParams.width = FIELD_SIZE;
        tempFieldView.setLayoutParams(layoutParams);
        return tempFieldView;
    }


    private void saveInitialFieldsPositions() {
        for (FieldView1 field : getFieldsArrayList()) {
            field.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    ((FieldView1) v).saveInitialPosition();
                }
            });
        }
    }

    private void fillAreaWithLetters() {

        for (FieldView1 field : fields.values()) {
            field.setLetter(getRandomLetter());
        }
    }

    private char getRandomLetter(){
        return (char) (1040 + (new Random()).nextInt(32));
    }

    protected void shiftRowLeft(int row) {
        char tempChar = fields.get(row, 0).getLetter();
        for (int col = 0; col < gameAreaDimension - 1; col++) {
            fields.get(row, col).setLetter(fields.get(row, col + 1).getLetter());
        }
        fields.get(row, gameAreaDimension - 1).setLetter(tempChar);
    }

    protected void shiftRowRight(int row) {
        char tempChar = fields.get(row, gameAreaDimension - 1).getLetter();
        for (int col = gameAreaDimension - 1; col > 0; col--) {
            fields.get(row, col).setLetter(fields.get(row, col - 1).getLetter());
        }
        fields.get(row, 0).setLetter(tempChar);
    }

    protected void shiftColUp(int col) {
        char tempChar = fields.get(0, col).getLetter();
        for (int row = 0; row < gameAreaDimension - 1; row++) {
            fields.get(row, col).setLetter(fields.get(row + 1, col).getLetter());
        }
        fields.get(gameAreaDimension - 1, col).setLetter(tempChar);
    }

    protected void shiftColDown(int col) {
        char tempChar = fields.get(gameAreaDimension - 1, col).getLetter();
        for (int row = gameAreaDimension - 1; row > 0; row--) {
            fields.get(row, col).setLetter(fields.get(row - 1, col).getLetter());
        }
        fields.get(0, col).setLetter(tempChar);
    }

    public ArrayList<FieldView1> getFieldsArrayList() {
        return new ArrayList<>(fields.values());
    }
    public void addWordUpdatedListener(OnWordChangedListener wordUpdatedListener) {
        this.wordChangedListener.add(wordUpdatedListener);
    }


    protected void setNewLetter(FieldView1 field) {
        field.setLetter(getRandomLetter());
    }
}
