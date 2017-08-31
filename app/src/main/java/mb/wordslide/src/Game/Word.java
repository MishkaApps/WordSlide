package mb.wordslide.src.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mb.wordslide.src.Game.Field.FieldView1;

/**
 * Created by mbolg on 10.08.2017.
 */

public class Word extends ArrayList<FieldView1>{
    @Override
    public boolean add(FieldView1 field) {
        if (size() == 0) {
            return super.add(field);
        } else if (contains(field)) {
            if (field == get(size() - 1)) {
                remove(get(size() - 1));
                return true;
            } else return false;
        } else {
            FieldView1 lastField = get(size() - 1);

            if (field.getCol() == lastField.getCol()
                    && field.getRow() == lastField.getRow()) {
                return false;
            }

            if ((field.getCol() == get(size() - 1).getCol() - 1 && field.getRow() == get(size() - 1).getRow())
                    || (field.getCol() == get(size() - 1).getCol() + 1 && field.getRow() == get(size() - 1).getRow())
                    || (field.getCol() == get(size() - 1).getCol() && field.getRow() == get(size() - 1).getRow() - 1)
                    || (field.getCol() == get(size() - 1).getCol() && field.getRow() == get(size() - 1).getRow() + 1)) {
                return super.add(field);
            } else
                return false;
        }
    }

    public String getWord() {
        String temp = "";
        for (FieldView1 field : this)
            temp += field.getLetter();
        return temp;
    }

    public int getScore() {
        return size();
    }
}
