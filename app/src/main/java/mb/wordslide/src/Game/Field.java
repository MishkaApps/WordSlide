package mb.wordslide.src.Game;

import android.widget.TextView;

/**
 * Created by mbolg on 26.07.2017.
 */
public class Field {
    private TextView fieldView;
    private int row, col;
    private char letter;

    public Field(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public char getLetter() {
        return letter;
    }


    float getX() {
        int[] pos = {0, 0};
        fieldView.getLocationOnScreen(pos);
        return (float) pos[0];
    }

    float getY(){
        int[] pos = {0, 0};
        fieldView.getLocationOnScreen(pos);
        return pos[1];
    }

    public void moveLeft(float progress, int gameAreaOffset) {

    }

    public void moveRight(float progress, int gameAreaOffset) {

    }

    public void moveUp(float progress, int gameAreaOffset) {

    }

    public void moveDown(float progress, int gameAreaOffset) {

    }
}
