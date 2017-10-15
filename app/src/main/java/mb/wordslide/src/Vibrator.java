package mb.wordslide.src;

import android.content.Context;

/**
 * Created by mbolg on 31.08.2017.
 */


public class Vibrator {
    private android.os.Vibrator vibrator;

    public Vibrator(Context context) {
        vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate() {
        vibrator.vibrate(10);
    }
}
