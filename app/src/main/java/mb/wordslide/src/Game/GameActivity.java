package mb.wordslide.src.Game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mb.wordslide.R;
import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.GameArea.AnimatedGameArea;
import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;
import mb.wordslide.src.Game.GameControl.GameController;
import mb.wordslide.src.Game.GameControl.GameOverListener;
import mb.wordslide.src.Game.GameControl.GameProgressBar;
import mb.wordslide.src.Vibrator;
import mb.wordslide.src.Vocabulary.Vocabulary;

public abstract class GameActivity extends AppCompatActivity implements View.OnClickListener, OnWordChangedListener, GameOverListener {
    private DisplayFragment display;
    private Button btnOk;
    private ScoreDisplay score;
    private Word word;
    private Vocabulary vocabulary;
    protected AnimatedGameArea gameArea;
    protected GameController gameController;
    protected GameBlueprint gameBlueprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        AnimatedGameAreaProvider gameAreaProvider = (AnimatedGameAreaProvider) getSupportFragmentManager().findFragmentById(R.id.game_area_fragment);
        gameArea = gameAreaProvider.getAnimatedGameArea();

        display = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.display);
        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(this);
        hideOkButton();

        score = (ScoreDisplay) findViewById(R.id.score);

        vocabulary = new Vocabulary(getResources());

        setGameAreaWordChangedListeners();
        setGameAreaAsWordClearListener();

        tryRestoreGame();

        GameProgressBar gameProgressBar = (GameProgressBar)findViewById(R.id.game_controller);
        gameController = getConcreteGameOverController();
        gameProgressBar.setConcreteController(gameController);

    }

    private void tryRestoreGame() {
        if(GameBlueprint.isGameSaved(this)) {
            gameBlueprint = GameBlueprint.retrieveSavedGame(this, getConcreteGameBlueprintClass());
        } else {
            gameBlueprint = getNewConcreteGameBlueprint();
        }
        gameArea.setGameBundle(gameBlueprint);
        score.setScore(gameBlueprint.getScore());
    }

    protected abstract GameBlueprint getNewConcreteGameBlueprint();
    protected abstract Class<? extends GameBlueprint> getConcreteGameBlueprintClass();

    protected abstract GameController getConcreteGameOverController();

    private void setGameAreaWordChangedListeners() {
        gameArea.addWordUpdatedListener(display);
        gameArea.addWordUpdatedListener(this);
    }

    private void setGameAreaAsWordClearListener() {
        display.setOnClearWordListener(gameArea);
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

        score.addScore(word.getScore());
        gameBlueprint.addScore(word.getScore());
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
        Toast.makeText(this, "GameBlueprint ends", Toast.LENGTH_SHORT).show();
    }
}
