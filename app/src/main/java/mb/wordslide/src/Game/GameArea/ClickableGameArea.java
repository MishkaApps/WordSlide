package mb.wordslide.src.Game.GameArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import java.util.ArrayList;

import mb.wordslide.src.Game.Field.Field;
import mb.wordslide.src.Game.FingerPosition;
import mb.wordslide.src.Game.OnClearWordListener;
import mb.wordslide.src.Game.OnWordChangedListener;
import mb.wordslide.src.Game.Word;
import mb.wordslide.src.Vibrator;

/**
 * Created by mbolg on 01.09.2017.
 */

public class ClickableGameArea extends StaticGameArea implements OnClearWordListener{
    private ArrayList<OnWordChangedListener> wordChangedListener;
    protected Vibrator vibrator;
    protected Word word;
    protected Field touchedField;

    public ClickableGameArea(int areaDimension, LayoutInflater inflater, GridLayout gameAreaGrid, Context context) {
        super(areaDimension, inflater, gameAreaGrid, context);
        vibrator = new Vibrator(context);
        word = new Word();
        wordChangedListener = new ArrayList<>();
    }


    public void addWordUpdatedListener(OnWordChangedListener wordUpdatedListener) {
        this.wordChangedListener.add(wordUpdatedListener);
    }


    protected void onFieldClick() {
        word.add(touchedField);
        syncFieldsBackgroundWithWord();
        notifyWordChanged();
    }


    private void notifyWordChanged() {
        for (OnWordChangedListener onWordChangedListener : wordChangedListener)
            onWordChangedListener.wordChanged(word);
    }

    private void syncFieldsBackgroundWithWord(){
        updateSelectedFieldBackground();
        resetNotSelectedFieldsBackground();
    }

    private void updateSelectedFieldBackground() {
        for (Field field : getFieldsArrayList())
            if (word.contains(field))
                field.setSelectedBackground();

    }

    private void resetNotSelectedFieldsBackground() {
        for (Field field : getFieldsArrayList())
            if (!word.contains(field))
                field.setDefaultBackground();

    }

    @Override
    public void notifyWordCleared() {
        word.clear();
        syncFieldsBackgroundWithWord();
        notifyWordChanged();
    }

    @Override
    public void notifyWordUsed() {
        setUsedFieldsLetters();
        word.clear();
        syncFieldsBackgroundWithWord();
        notifyWordChanged();
    }


    private void setUsedFieldsLetters() {
        for (Field field : word) {
            field.hide();
            field.setRandomLetter();
            field.show();
        }
        updateGameBundle();
    }

}
