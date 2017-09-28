package mb.wordslide.src.Game;

/**
 * Created by mbolg on 01.09.2017.
 */

public interface GameFinishController {
    void shifted();
    void addBonus(int bonus);
    void notifyGameOver();
    void addGameOverListener(OnGameOverListener gameOverListener);
}
