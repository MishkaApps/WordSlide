package mb.wordslide.src.Game;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;

import mb.wordslide.R;
import mb.wordslide.src.Game.Field.FieldView1;

public class GameAreaFragment extends Fragment {
    private GameAreaAnimator gameAreaAnimator;
    private GameArea gameArea;
    private final int gameAreaDimension = 6;
    private GridLayout gameAreaView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_game_area2, container);
        gameAreaView = (GridLayout) fragment.findViewById(R.id.game_area_view);

        ArrayList<FieldView1> fields = getCreatedFields();
        for (FieldView1 field : fields)
            gameAreaView.addView(field);
        gameArea = new GameArea(gameAreaDimension, fields);

        gameAreaAnimator = new GameAreaAnimator(gameArea);


//        gameAreaAnimator.setFieldInflater(getActivity().getLayoutInflater());
//        gameAreaAnimator.setGameAreaView(gameAreaView);

        saveInitialFieldsPositions();
        setOnTouchListenersForFieldViews();

        return fragment;
    }

    private void saveInitialFieldsPositions() {
        for (FieldView1 field : gameArea.getFieldsArrayList()) {
            field.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    ((FieldView1) v).saveInitialPosition();
                }
            });
        }
    }

    private ArrayList<FieldView1> getCreatedFields() {
        ArrayList<FieldView1> fields = new ArrayList<>();
        FieldView1 tempFieldView;
        for (int row = 0; row < gameAreaDimension; ++row)
            for (int col = 0; col < gameAreaDimension; ++col) {
                tempFieldView = inflateField(row, col);
                tempFieldView.setPosition(row, col);
                fields.add(tempFieldView);
            }
        return fields;
    }

    private FieldView1 inflateField(int row, int col) {
        LayoutInflater gameAreaInflater = getActivity().getLayoutInflater();
        GridLayout.LayoutParams layoutParams;
        FieldView1 tempFieldView;
        tempFieldView = (FieldView1) gameAreaInflater.inflate(R.layout.field_view_template, null, false);
        layoutParams = new GridLayout.LayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.height = 150;
        layoutParams.width = 150;
        tempFieldView.setLayoutParams(layoutParams);
        return tempFieldView;
    }


//    private void getInflatedFields() {
//        LayoutInflater gameAreaInflater = getActivity().getLayoutInflater();
//        GridLayout.LayoutParams layoutParams;
//        FieldView tempFieldView;
//        for (FieldTemp field : gameArea.getFieldsArrayList()) {
//            tempFieldView = (FieldView) gameAreaInflater.inflate(R.layout.field_view_template, null, false);
//            layoutParams = new GridLayout.LayoutParams();
//            layoutParams.rowSpec = GridLayout.spec(field.getRow());
//            layoutParams.columnSpec = GridLayout.spec(field.getCol());
//            layoutParams.setMargins(10, 10, 10, 10);
//            layoutParams.height = 150;
//            layoutParams.width = 150;
//            tempFieldView.setLayoutParams(layoutParams);
//            gameAreaView.addView(tempFieldView);
//            field.setFieldView(tempFieldView);
//        }
//        gameAreaAnimator.updateFields();
//    }

    private void setOnTouchListenersForFieldViews() {
        for (FieldView1 field : gameArea.getFieldsArrayList())
            field.setOnTouchListener(gameAreaAnimator);
    }
}
