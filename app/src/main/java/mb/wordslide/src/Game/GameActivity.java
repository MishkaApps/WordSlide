package mb.wordslide.src.Game;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mb.wordslide.R;
import mb.wordslide.src.Vocabulary.Vocabulary;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, OnWordChangedListener{
    private DisplayFragment display;
    private Button btnOk;
    private Score score;
    private Word word;
    private Vocabulary vocabulary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        display = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.display);
        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(this);
        hideOkButton();

        score = (Score) findViewById(R.id.score);

        vocabulary = new Vocabulary(getResources());
        setGameAreaWordChangedListeners();
        setGameAreaAsWordClearListener();
    }

    private void setGameAreaWordChangedListeners() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GameAreaFragment gameAreaFragment = (GameAreaFragment) fragmentManager.findFragmentById(R.id.game_area_fragment);
        gameAreaFragment.addOnWordUpdatedListener(display);
        gameAreaFragment.addOnWordUpdatedListener(this);
    }

    private void setGameAreaAsWordClearListener() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GameAreaFragment gameAreaFragment = (GameAreaFragment) fragmentManager.findFragmentById(R.id.game_area_fragment);
        display.setOnClearWordListener(gameAreaFragment.getGameArea());
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
            addWordToScore();
        }
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
        if(word.size() >= 1)
            showOkButton();
        else hideOkButton();
    }
}
