package mb.wordslide.src.Game;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import mb.wordslide.R;
import mb.wordslide.src.Game.GameControl.GameController;
import mb.wordslide.src.Game.GameControl.Timer;
/**
 * Created by mbolg on 01.09.2017.
 */

public class TimeGameActivity extends GameActivity {
    private Button pause, resume;
    private Timer timer;
    private ViewGroup gameAreaHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout pauseButtonContainer = (FrameLayout) findViewById(R.id.pause_button_container);
        pauseButtonContainer.setVisibility(View.VISIBLE);

        gameAreaHider = (FrameLayout) findViewById(R.id.game_area_hider);

        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });
        resume = (Button) findViewById(R.id.button_resume);
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame();
            }
        });
    }

    private void pauseGame() {
        timer.pause();
        hideGameArea();
        pause.setVisibility(View.INVISIBLE);
    }

    private void hideGameArea() {
        showGameAreaHider();
        animatedGameArea.hideAllFields();
    }

    private void resumeGame() {
        timer.start();
        showGameArea();
        pause.setVisibility(View.VISIBLE);
    }

    private void showGameArea() {
        hideGameAreaHider();
        animatedGameArea.showAllFields();
    }

    private void showGameAreaHider() {
        gameAreaHider.setVisibility(View.VISIBLE);
    }

    private void hideGameAreaHider() {
        gameAreaHider.setVisibility(View.GONE);
    }

    @Override
    protected GameController getConcreteGameOverController() {
        timer = new Timer();
        return timer;
    }

}
