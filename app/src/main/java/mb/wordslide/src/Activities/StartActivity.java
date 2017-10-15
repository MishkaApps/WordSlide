package mb.wordslide.src.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mb.wordslide.R;
import mb.wordslide.src.Configurations;
import mb.wordslide.src.Game.MoveGameActivity;
import mb.wordslide.src.Game.TimeGameActivity;
import mb.wordslide.src.L;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button movesGame, timeGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


//        Intent intent = new Intent(this, Configurations.GAME_CLASS);
//        startActivity(intent);

        movesGame = (Button) findViewById(R.id.moves_game);
        movesGame.setOnClickListener(this);

        timeGame = (Button) findViewById(R.id.time_game);
        timeGame.setOnClickListener(this);

//        movesGame.callOnClick();
//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Class gameActivityClass = null;
        if (v == movesGame)
            gameActivityClass = MoveGameActivity.class;
        else if (v == timeGame)
            gameActivityClass = TimeGameActivity.class;
        Intent intent = new Intent(this, gameActivityClass);
        startActivity(intent);
    }
}
