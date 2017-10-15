package mb.wordslide.src.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import mb.wordslide.R;

public class TestActivity extends AppCompatActivity {
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        pb = (ProgressBar) findViewById(R.id.progress_bar);

        pb.setProgress(5);

        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        pb.setProgress(pb.getProgress() + (new Random()).nextInt(5));
            }
        });

    }
}
