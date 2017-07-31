package mb.wordslide.src.Game.Field;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by mbolg on 30.07.2017.
 */
public class Field extends FieldView1 {


    public Field(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void moveLeft(float progress, float fingerPositionX) {
        animate().x(fingerPositionX - distanceToFingerX).setDuration(0);
    }

}
