package mb.wordslide.src.Game.GameControl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import mb.wordslide.R;

/**
 * Created by mbolg on 11.10.2017.
 */

public class GameProgressBar extends ProgressBar {
    private GameController concreteController;

    public GameProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setConcreteController(GameController concreteController) {
        this.concreteController = concreteController;
        concreteController.setProgressBar(this);
    }
    public void addBonus(int bonus) {
        concreteController.addBonus(bonus);
    }
    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        paintProgressBar(progress);
    }
    private void paintProgressBar(int progress) {
        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.progress_drawable);
        int newColor = Color.rgb(255,
                100 + progress * 2,
                100 + progress * 2);

        ClipDrawable progressDrawable = (ClipDrawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);
        GradientDrawable progressOutline = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.progress_outline);

        progressDrawable.setDrawable(new ColorDrawable(newColor));
        progressOutline.setStroke(10, newColor);

        setProgressDrawable(layerDrawable);
    }
}
