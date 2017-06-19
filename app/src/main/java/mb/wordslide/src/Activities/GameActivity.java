package mb.wordslide.src.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import mb.wordslide.R;
import mb.wordslide.src.CellSelectionListener;
import mb.wordslide.src.GameArea;
import mb.wordslide.src.Word;

public class GameActivity extends Activity implements CellSelectionListener {
    private GameArea gameArea;
    private TextView display;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        word = new Word();

        GameArea gameArea = (GameArea)getFragmentManager().findFragmentById(R.id.game_fragment);
        gameArea.addOnCellSelectedListener(this, word);

        display = (TextView)findViewById(R.id.tv_display);
    }

    @Override
    public void nextLetter() {
        display.setText(word.getWord());
    }

    @Override
    public void deleteLetter(char ch) {

    }
}
