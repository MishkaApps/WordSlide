package mb.wordslide.src.Game;

import com.google.common.collect.ImmutableTable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mb.wordslide.src.Game.Field.Field;
import mb.wordslide.src.Game.Field.FieldView1;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 26.07.2017.
 */
public class GameArea {
    private ImmutableTable<Integer, Integer, FieldView1> fields;
    private int gameAreaDimension;

    public int getGameAreaDimension() {
        return gameAreaDimension;
    }

    public GameArea(int areaDimension, ArrayList<FieldView1> fields) {
        ImmutableTable.Builder<Integer, Integer, FieldView1> fieldsBuilder = new ImmutableTable.Builder<>();
        for(FieldView1 field: fields)
                fieldsBuilder.put(field.getRow(), field.getCol(), field);

        this.fields = fieldsBuilder.build();
        this.gameAreaDimension = areaDimension;
        fillAreaWithLetters();
        printGameArea();
    }

    private void printGameArea() {
        L.l("------------------------\n");
        String rowString = "";
        for (int row : fields.rowKeySet()) {
            for (int col : fields.columnKeySet()) {
                rowString += fields.get(row, col).getLetter() + " ";
            }
            L.l("\t\t" + rowString + '\n');
            rowString = "";
        }
        L.l("\n------------------------");
    }

    private void fillAreaWithLetters() {
        for (FieldView1 field : fields.values()) {
            field.setLetter((char) (80 + field.getRow() + field.getCol()));
        }
    }

    public void shiftRowLeft(int row) {
        char tempChar = fields.get(row, 0).getLetter();
        for (int col = 0; col < gameAreaDimension - 1; col++) {
            fields.get(row, col).setLetter(fields.get(row, col + 1).getLetter());
        }
        fields.get(row, gameAreaDimension - 1).setLetter(tempChar);
        printGameArea();
    }

    public void shiftRowRight(int row) {
        char tempChar = fields.get(row, gameAreaDimension - 1).getLetter();
        for (int col = gameAreaDimension - 1; col > 0; col--) {
            fields.get(row, col).setLetter(fields.get(row, col - 1).getLetter());
        }
        fields.get(row, 0).setLetter(tempChar);
        printGameArea();
    }

    public void shiftColUp(int col) {
        char tempChar = fields.get(0, col).getLetter();
        for (int row = 0; row < gameAreaDimension - 1; row++) {
            fields.get(row, col).setLetter(fields.get(row + 1, col).getLetter());
        }
        fields.get(gameAreaDimension - 1, col).setLetter(tempChar);
        printGameArea();
    }

    public void shiftColDown(int col) {
        char tempChar = fields.get(gameAreaDimension - 1, col).getLetter();
        for (int row = gameAreaDimension - 1; row > 0; row--) {
            fields.get(row, col).setLetter(fields.get(row - 1, col).getLetter());
        }
        fields.get(0, col).setLetter(tempChar);
        printGameArea();
    }

    public ArrayList<FieldView1> getFieldsArrayList() {
        return new ArrayList<>(fields.values());
    }

    public void setFields() {

    }
}
