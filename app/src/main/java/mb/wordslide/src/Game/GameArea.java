package mb.wordslide.src.Game;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import mb.wordslide.src.L;

/**
 * Created by mbolg on 26.07.2017.
 */
public class GameArea {
    private ImmutableTable<Integer, Integer, Field> fields;
    private int areaDimension;

    public GameArea(int areaDimension) {
        ImmutableTable.Builder<Integer, Integer, Field> fieldsBuilder = new ImmutableTable.Builder<>();
        for (int row = 0; row < areaDimension; ++row)
            for (int col = 0; col < areaDimension; ++col) {
                fieldsBuilder.put(row, col, new Field(row, col));
            }
        fields = fieldsBuilder.build();
        this.areaDimension = areaDimension;
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
        for (Field field : fields.values()) {
            field.setLetter((char) (80 + field.getRow() + field.getCol()));
        }
    }

    public void shiftRowLeft(int row) {
        char tempChar = fields.get(row, 0).getLetter();
        for (int col = 0; col < areaDimension - 1; col++) {
            fields.get(row, col).setLetter(fields.get(row, col + 1).getLetter());
        }
        fields.get(row, areaDimension - 1).setLetter(tempChar);
        printGameArea();
    }

    public void shiftRowRight(int row) {
        char tempChar = fields.get(row, areaDimension - 1).getLetter();
        for (int col = areaDimension - 1; col > 0; col--) {
            fields.get(row, col).setLetter(fields.get(row, col - 1).getLetter());
        }
        fields.get(row, 0).setLetter(tempChar);
        printGameArea();
    }

    public void shiftColUp(int col) {
        char tempChar = fields.get(0, col).getLetter();
        for (int row = 0; row < areaDimension - 1; row++) {
            fields.get(row, col).setLetter(fields.get(row + 1, col).getLetter());
        }
        fields.get(areaDimension - 1, col).setLetter(tempChar);
        printGameArea();
    }

    public void shiftColDown(int col) {
        char tempChar = fields.get(areaDimension - 1, col).getLetter();
        for (int row = areaDimension - 1; row > 0; row--) {
            fields.get(row, col).setLetter(fields.get(row - 1, col).getLetter());
        }
        fields.get(0, col).setLetter(tempChar);
        printGameArea();
    }

    public ArrayList<Field> getFieldsArrayList() {
        return new ArrayList<>(fields.values());
    }

}
