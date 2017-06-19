package mb.wordslide.src;

import java.util.ArrayList;

/**
 * Created by mbolg on 19.06.2017.
 */
public class Word extends ArrayList<Field> {
    @Override
    public boolean add(Field field) {
        if (size() == 0)
            return super.add(field);
        else {
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
}
