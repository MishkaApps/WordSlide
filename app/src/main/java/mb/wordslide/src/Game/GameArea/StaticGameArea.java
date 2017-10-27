package mb.wordslide.src.Game.GameArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import java.util.ArrayList;

import mb.wordslide.R;
import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.Field.Field;
import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;

/**
 * Created by mbolg on 26.07.2017.
 */
public class StaticGameArea {
    protected GridLayout gameAreaGrid;
    protected static final int FIELD_SIZE = Configurations.FIELDS_SIZE;
    private ImmutableTable<Integer, Integer, Field> fields;
    protected int gameAreaDimension;
    protected LayoutInflater inflater;
    protected int gameAreaOffsetX, gameAreaOffsetY;
    private GameBlueprint gameBundle;
    private Context context;

    public StaticGameArea(int areaDimension, LayoutInflater inflater, GridLayout gameAreaGrid, Context context) {
        this.gameAreaDimension = areaDimension;
        this.inflater = inflater;
        this.gameAreaGrid = gameAreaGrid;
        this.context = context;

        ImmutableTable.Builder<Integer, Integer, Field> fieldsBuilder = new ImmutableTable.Builder<>();
        ArrayList<Field> inflatedFields = inflateFields();
        for (Field field : inflatedFields)
            fieldsBuilder.put(field.getRow(), field.getCol(), field);
        this.fields = fieldsBuilder.build();

        for (Field field : getFieldsArrayList())
            gameAreaGrid.addView(field);
        saveInitialFieldsPositions();
        saveGameAreaOffset();
    }

    private void saveGameAreaOffset() {
        gameAreaGrid.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int[] offset = new int[2];
                gameAreaGrid.getLocationOnScreen(offset);
                gameAreaOffsetX = offset[0];
                gameAreaOffsetY = offset[1];
                setGameAreaOffsetForFields();
            }
        });
    }

    private void setGameAreaOffsetForFields() {
        for (Field field : getFieldsArrayList())
            field.setGameAreaOffset(gameAreaOffsetX, gameAreaOffsetY);
    }

    private ArrayList<Field> inflateFields() {
        ArrayList<Field> fields = new ArrayList<>();
        Field tempFieldView;
        for (int row = 0; row < gameAreaDimension; ++row)
            for (int col = 0; col < gameAreaDimension; ++col) {
                tempFieldView = inflateField(row, col);
                tempFieldView.setPosition(row, col);
                tempFieldView.setGameAreaDimension(gameAreaDimension);
                fields.add(tempFieldView);
            }
        return fields;
    }

    private Field inflateField(int row, int col) {
        GridLayout.LayoutParams layoutParams;
        Field tempFieldView;
        tempFieldView = (Field) inflater.inflate(R.layout.field_view_template, null, false);
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
        for (Field field : getFieldsArrayList()) {
            field.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    ((Field) v).saveInitialPosition();
                }
            });
        }
    }

    protected void shiftRowLeft(int row) {
        char tempChar = fields.get(row, 0).getLetter();
        for (int col = 0; col < gameAreaDimension - 1; col++) {
            fields.get(row, col).setLetter(fields.get(row, col + 1).getLetter());
        }
        fields.get(row, gameAreaDimension - 1).setLetter(tempChar);
        updateGameBundle();
    }


    protected void shiftRowRight(int row) {
        char tempChar = fields.get(row, gameAreaDimension - 1).getLetter();
        for (int col = gameAreaDimension - 1; col > 0; col--) {
            fields.get(row, col).setLetter(fields.get(row, col - 1).getLetter());
        }
        fields.get(row, 0).setLetter(tempChar);
        updateGameBundle();
    }

    protected void shiftColUp(int col) {
        char tempChar = fields.get(0, col).getLetter();
        for (int row = 0; row < gameAreaDimension - 1; row++) {
            fields.get(row, col).setLetter(fields.get(row + 1, col).getLetter());
        }
        fields.get(gameAreaDimension - 1, col).setLetter(tempChar);
        updateGameBundle();
    }

    protected void shiftColDown(int col) {
        char tempChar = fields.get(gameAreaDimension - 1, col).getLetter();
        for (int row = gameAreaDimension - 1; row > 0; row--) {
            fields.get(row, col).setLetter(fields.get(row - 1, col).getLetter());
        }
        fields.get(0, col).setLetter(tempChar);
        updateGameBundle();
    }

    protected void updateGameBundle() {
        Table<Integer, Integer, Character> gameArea = HashBasedTable.create();
        for (Field field : getFieldsArrayList()) {
            gameArea.put(field.getRow(), field.getCol(), field.getLetter());
        }
        gameBundle.updateGameAreaAndSave(gameArea);
    }

    public ArrayList<Field> getFieldsArrayList() {
        return new ArrayList<>(fields.values());
    }

    public void setGameBundle(GameBlueprint gameBundle) {
        this.gameBundle = gameBundle;
        restoreGameArea();
    }

    private void restoreGameArea() {
        for (Field field : getFieldsArrayList())
            field.setLetter(gameBundle.getLetterInField(field.getRow(), field.getCol()));
    }
}
