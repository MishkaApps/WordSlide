package mb.wordslide.src.Game;


import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mb.wordslide.R;
import mb.wordslide.src.Vibrator;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment implements OnWordChangedListener {
    private TextView display;
    private Button clear;
    private OnClearWordListener onClearWordListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_display, container, false);
        display = (TextView) fragment.findViewById(R.id.display);
        clear = (Button) fragment.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearWordClick();
            }
        });
        hideClearButton();
        return fragment;
    }

    private void onClearWordClick() {
        onClearWordListener.notifyWordCleared();
        clearDisplay();
        hideClearButton();
    }

    private void clearDisplay() {
        display.setText("");
    }

    private void hideClearButton() {
        clear.setVisibility(View.INVISIBLE);
    }

    @Override
    public void wordChanged(Word word) {
        display.setText(word.getWord());
        if (word.size() > 0) {
            showClearButton();
        } else {
            hideClearButton();
        }
    }


    private void showClearButton() {
        clear.setVisibility(View.VISIBLE);
    }

    public void setOnClearWordListener(OnClearWordListener onClearWordListener) {
        this.onClearWordListener = onClearWordListener;
    }

    public void clearUsedWord() {
        onClearWordListener.notifyWordUsed();
        clearDisplay();
        hideClearButton();
    }

    public void notifyUserWordIncorrect() {
        shakeDisplay();
        vibrate();
    }

    // Пиздос костыль, но работает охуенно!!!
    private final long ANIMATION_PART_DURATION = 50;
    private boolean isShaking;

    private void shakeDisplay() {
        if(isShaking)
            return;
        Shaker shaker = new Shaker();
        display.animate().xBy(50).setDuration(ANIMATION_PART_DURATION).setListener(shaker).start();
    }

    private class Shaker implements Animator.AnimatorListener {
        private int count;

        public Shaker() {
            count = 0;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            isShaking = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ++count;
            switch (count) {
                case 1:
                    display.animate().xBy(-100).setDuration(ANIMATION_PART_DURATION).setListener(this).start();
                    break;
                case 2:
                    display.animate().xBy(80).setDuration(ANIMATION_PART_DURATION).setListener(this).start();
                    break;
                case 3:
                    display.animate().xBy(-60).setDuration(ANIMATION_PART_DURATION).setListener(this).start();
                    break;
                case 4:
                    display.animate().xBy(40).setDuration(ANIMATION_PART_DURATION).setListener(this).start();
                    break;
                case 5:
                    display.animate().xBy(-20).setDuration(ANIMATION_PART_DURATION).setListener(this).start();
                    break;
                case 6:
                    display.animate().xBy(10).setDuration(ANIMATION_PART_DURATION).setListener(this).start();
                    break;
                case 7:
                    isShaking = false;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private void vibrate() {
        (new Vibrator(getActivity())).vibrate();
    }
}
