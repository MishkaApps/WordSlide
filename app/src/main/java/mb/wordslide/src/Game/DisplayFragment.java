package mb.wordslide.src.Game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mb.wordslide.R;

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
}
