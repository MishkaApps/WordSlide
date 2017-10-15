package mb.wordslide.src.Game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mb.wordslide.R;
import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.GameArea.AnimatedGameArea;
import mb.wordslide.src.Game.GameControl.GameController;
import mb.wordslide.src.Game.GameControl.GameOverListener;
import mb.wordslide.src.Game.GameControl.GameProgressBar;
import mb.wordslide.src.Vibrator;
import mb.wordslide.src.Vocabulary.Vocabulary;

public abstract class GameActivity extends AppCompatActivity implements View.OnClickListener, OnWordChangedListener, GameOverListener {
    private DisplayFragment display;
    private Button btnOk;
    private Score score;
    private Word word;
    private Vocabulary vocabulary;
    protected AnimatedGameArea animatedGameArea;
    protected GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        AnimatedGameAreaProvider gameAreaProvider = (AnimatedGameAreaProvider) getSupportFragmentManager().findFragmentById(R.id.game_area_fragment);
        animatedGameArea = gameAreaProvider.getAnimatedGameArea();

        display = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.display);
        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(this);
        hideOkButton();

        score = (Score) findViewById(R.id.score);

        vocabulary = new Vocabulary(getResources());

        setGameAreaWordChangedListeners();
        setGameAreaAsWordClearListener();

        GameProgressBar gameProgressBar = (GameProgressBar)findViewById(R.id.game_controller);
        gameController = getConcreteGameOverController();
        gameProgressBar.setConcreteController(gameController);
    }

    protected abstract GameController getConcreteGameOverController();

    private void setGameAreaWordChangedListeners() {
        animatedGameArea.addWordUpdatedListener(display);
        animatedGameArea.addWordUpdatedListener(this);
    }

    private void setGameAreaAsWordClearListener() {
        display.setOnClearWordListener(animatedGameArea);
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
            try {
                addWord();
            } catch (WordDoesNotExist wordDoesNotExist) {
                onWordDoesNotExist();
            }
        }
    }

    private void addWord() throws WordDoesNotExist {
        if(Configurations.CHECK_WORDS)
            if(!checkWord())
                throw new WordDoesNotExist();

        score.addWord(word);
        gameController.addBonus(word.getCost());
        display.clearUsedWord();
    }

    private boolean checkWord(){
        return true || vocabulary.exist(word.getWord());
    }

    private void onWordDoesNotExist() {
        display.notifyUserWordIncorrect();
        new Vibrator(this).vibrate();
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
    public void gameOver() {
        Toast.makeText(this, "Game ends", Toast.LENGTH_SHORT).show();
    }
}
