package mb.wordslide.src.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mb.wordslide.R;
import mb.wordslide.src.Word;
import mb.wordslide.src.WordUpdateListener;
import mb.wordslide.src.GameArea;

public class GameActivity extends Activity implements WordUpdateListener, View.OnClickListener {
    private GameArea gameArea;
    private TextView display;
    private Button btnClearWord;
    private int dimension = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameArea = (GameArea)getFragmentManager().findFragmentById(R.id.game_fragment);
        gameArea.addOnWordUpdateListener(this);

        display = (TextView)findViewById(R.id.tv_display);
        btnClearWord = (Button)findViewById(R.id.btn_clear_word);
        btnClearWord.setOnClickListener(this);
        btnClearWord.setVisibility(View.INVISIBLE);

    }

    @Override
    public void wordUpdated(Word word) {
        display.setText(word.getWord());
        if(word.size() > 0)
            btnClearWord.setVisibility(View.VISIBLE);
        else btnClearWord.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v == btnClearWord){
            gameArea.clearWord();
        }
    }
}
