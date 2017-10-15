package mb.wordslide.src.Game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import mb.wordslide.R;
import mb.wordslide.src.Game.GameArea.AnimatedGameArea;

public class GameAreaFragment extends Fragment implements AnimatedGameAreaProvider {
    private AnimatedGameArea animatedGameArea;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_game_area2, container);
        GridLayout gameAreaGrid = (GridLayout) fragment.findViewById(R.id.game_area_view);
        animatedGameArea = new AnimatedGameArea(gameAreaGrid, getActivity().getLayoutInflater(), getActivity());

        return fragment;
    }

    @Override
    public AnimatedGameArea getAnimatedGameArea() {
        return animatedGameArea;
    }
}
