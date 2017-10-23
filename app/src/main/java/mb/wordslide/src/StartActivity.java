package mb.wordslide.src;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import mb.wordslide.R;
import mb.wordslide.src.Game.MoveGameActivity;
import mb.wordslide.src.Game.TimeGameActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button movesGame, timeGame, btnLeaderboard;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        movesGame = (Button) findViewById(R.id.btn_moves_game);
        movesGame.setOnClickListener(this);

        timeGame = (Button) findViewById(R.id.btn_time_game);
        timeGame.setOnClickListener(this);

        btnLeaderboard = (Button) findViewById(R.id.btn_leaderboard);
        btnLeaderboard.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {

//            Games.Leaderboards.submitScore(googleApiClient, "CggIpumhxDoQAhAB", 56);
//            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(googleApiClient,
//                    "CggIpumhxDoQAhAB"), 228);
//        }

        Intent intent = null;
        switch(v.getId()){
            case R.id.btn_moves_game:
                intent = new Intent(this, MoveGameActivity.class);
                break;
            case R.id.btn_time_game:
                intent = new Intent(this, TimeGameActivity.class);
                break;
            case R.id.btn_leaderboard:
                intent = new Intent(this, LeaderboardActivity.class);
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        L.l("activity result: " + requestCode);

//        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//        L.l("is sign in success: " + signInResult.isSuccess());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        L.l("Google Games Api connected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        L.l("Google Games Api connection failed: " + connectionResult);
    }

    @Override
    public void onConnectionSuspended(int i) {
        L.l("Google Games Api suspended");
        // Attempt to reconnect
        googleApiClient.connect();
    }
}
