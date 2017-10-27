package mb.wordslide.src.Game;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mbolg on 31.08.2017.
 */

public class ScoreDisplay extends android.support.v7.widget.AppCompatTextView {
    private int score;

    public ScoreDisplay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        score = 0;
    }

    public void addScore(int score){
        this.score += score;
        updateScoreView();
    }

    private void updateScoreView() {
        setText(Integer.toString(score));
    }

    public void setScore(int score) {
        this.score = score;
        updateScoreView();
    }
}
