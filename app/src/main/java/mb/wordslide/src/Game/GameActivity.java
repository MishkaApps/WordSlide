package mb.wordslide.src.Game;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mb.wordslide.R;
import mb.wordslide.src.L;
import mb.wordslide.src.Vocabulary.Vocabulary;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, OnWordChangedListener, OnGameEndsListener {
    private DisplayFragment display;
    private Button btnOk;
    private Score score;
    private Word word;
    private ShiftCounter shiftCounter;
    private Vocabulary vocabulary;
    private GameAreaFragment gameAreaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        gameAreaFragment = (GameAreaFragment) fragmentManager.findFragmentById(R.id.game_area_fragment);

        display = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.display);
        shiftCounter = (ShiftCounter) findViewById(R.id.shift_counter);
        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(this);
        hideOkButton();

        score = (Score) findViewById(R.id.score);

        vocabulary = new Vocabulary(getResources());
        setGameAreaWordChangedListeners();
        setGameAreaAsWordClearListener();
        setShiftCounterAsShiftListener();
        shiftCounter.addOnGameEndsListener(this);
    }


    private void setGameAreaWordChangedListeners() {
        gameAreaFragment.addOnWordUpdatedListener(display);
        gameAreaFragment.addOnWordUpdatedListener(this);
    }

    private void setGameAreaAsWordClearListener() {
        display.setOnClearWordListener(gameAreaFragment.getGameArea());
    }

    private void setShiftCounterAsShiftListener() {
        gameAreaFragment.getGameArea().setOnShiftListener(shiftCounter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
                addWordToScore();
        }
    }

    private void notifyUserWordDoesNotExist() {
        Toast.makeText(this, "word does not exist", Toast.LENGTH_SHORT).show();
    }

    private void addWordToScore() {
        score.addWord(word);
        display.clearUsedWord();
    }

    private void clearWordAndHideButtons() {
        hideOkButton();
    }

    private void hideOkButton() {
        btnOk.setVisibility(View.INVISIBLE);
    }

    private void showOkButton() {
        btnOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void wordChanged(Word word) {
        this.word = word;
        if (word.size() >= 1)
            showOkButton();
        else hideOkButton();
    }

    @Override
    public void notifyGameEnds() {
        Toast.makeText(this, "Game ends", Toast.LENGTH_SHORT).show();
    }
}
