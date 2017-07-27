package mb.wordslide.src.Game;

import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mbolg on 26.07.2017.
 */
public class GameFragment extends Fragment implements View.OnTouchListener {
    private GameAreaAnimator gameAreaAnimator;

    public GameFragment() {
        gameAreaAnimator = new GameAreaAnimator();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gameAreaAnimator.setFingerPosition(new FingerPosition(event.getRawX(), event.getRawY()));
        int motionEvent = event.getAction();
        if (motionEvent == MotionEvent.ACTION_DOWN) {
            gameAreaAnimator.onFingerDown();
        } else if (motionEvent == MotionEvent.ACTION_MOVE) {
            gameAreaAnimator.onFingerMove();
        } else if (motionEvent == MotionEvent.ACTION_UP) {
            gameAreaAnimator.onFingerUp();
        }
        return false;
    }
}
