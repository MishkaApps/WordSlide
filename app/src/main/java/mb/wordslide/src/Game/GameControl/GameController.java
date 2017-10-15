package mb.wordslide.src.Game.GameControl;

import android.widget.ProgressBar;

/**
 * Created by mbolg on 14.10.2017.
 */

public interface GameController {
    void setGameOverListener(GameOverListener gameOverListener);
    void notifyGameOverListener();
    void addBonus(int bonus);
    void setProgressBar(ProgressBar progressBar);
    void updateProgressBar();
}
