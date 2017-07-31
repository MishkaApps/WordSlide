package mb.wordslide.src.Game.Field;

/**
 * Created by mbolg on 31.07.2017.
 */
public interface FieldLogic {
    void setLetter(char letter);
    int getCol();
    int getRow();
    char getLetter();
    void setPosition(int row, int col);
}
