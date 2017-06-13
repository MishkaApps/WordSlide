package mb.wordslide.src;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import mb.wordslide.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        LayoutInflater ltInflater = getLayoutInflater();
        View view = ltInflater.inflate(R.layout.field_template, null, false);
        View view1 = ltInflater.inflate(R.layout.field_template, null, false);
        View view2 = ltInflater.inflate(R.layout.field_template, null, false);


//        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
//        linLayout.addView(view);

        GridLayout gridLayout = (GridLayout)findViewById(R.id.test_grid);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.columnSpec = GridLayout.spec(3);
        lp.rowSpec = GridLayout.spec(3);
        view1.setLayoutParams(lp);

        gridLayout.addView(view);
        gridLayout.addView(view1);
        gridLayout.addView(view2);
    }
}
