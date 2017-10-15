package mb.wordslide.src.Game;

import java.util.ArrayList;

import mb.wordslide.src.Game.Field.Field;

/**
 * Created by mbolg on 10.08.2017.
 */

public class Word extends ArrayList<Field>{
    @Override
    public boolean add(Field field) {
        if (size() == 0) {
            return super.add(field);
        } else if (contains(field)) {
            if (field == get(size() - 1)) {
                remove(get(size() - 1));
                return true;
            } else return false;
        } else {
            Field lastField = get(size() - 1);

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
        for (Field field : this)
            temp += field.getLetter();
        return temp;
    }

    public int getScore() {
        return size();
    }

    public int getCost() {
        return size();
    }
}
