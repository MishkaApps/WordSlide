package mb.wordslide.src.Game;

import android.animation.Animator;
import android.util.Pair;
import android.view.animation.Animation;
import android.widget.GridLayout;

import mb.wordslide.src.Game.Field.BorderField;
import mb.wordslide.src.Game.Field.Field;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 07.10.2017.
 */

public class FieldAnimationListener implements Animator.AnimatorListener {
    private GridLayout gameArea;
    private Field field;
    private boolean inAnimation;

    public FieldAnimationListener(GridLayout gameAreaGrid, Field field) {
        this.gameArea = gameAreaGrid;
        this.field = field;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        inAnimation = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        inAnimation = false;
        if (field.getClass() == BorderField.class)
            gameArea.removeView(field);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
