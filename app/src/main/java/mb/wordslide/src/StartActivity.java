package mb.wordslide.src;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;

import mb.wordslide.R;
import mb.wordslide.src.Game.GameBlueprint.GameBlueprint;
import mb.wordslide.src.Game.ConcreteGameActivities.MoveGameActivity;
import mb.wordslide.src.Game.ConcreteGameActivities.TimeGameActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button movesGame, timeGame, btnLeaderboard, btnResume;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addConnectionCallbacks(this)
                .build();

        movesGame = (Button) findViewById(R.id.btn_moves_game);
        movesGame.setOnClickListener(this);

        timeGame = (Button) findViewById(R.id.btn_time_game);
        timeGame.setOnClickListener(this);

        btnLeaderboard = (Button) findViewById(R.id.btn_leaderboard);
        btnLeaderboard.setOnClickListener(this);

        btnResume = (Button) findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(this);

        (findViewById(R.id.btn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameBlueprint.deleteGame(StartActivity.this);
            }
        });

        if (!GameBlueprint.isGameSaved(this)) {
            btnResume.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_resume:
                if (GameBlueprint.getGameTypeOfSavedGame(this) == GameBlueprint.GameType.MOVES)
                    intent = new Intent(this, MoveGameActivity.class);
                else if (GameBlueprint.getGameTypeOfSavedGame(this) == GameBlueprint.GameType.TIME)
                    intent = new Intent(this, TimeGameActivity.class);
                break;
            case R.id.btn_moves_game:
                GameBlueprint.deleteGame(this);
                intent = new Intent(this, MoveGameActivity.class);
                break;
            case R.id.btn_time_game:
                GameBlueprint.deleteGame(this);
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

//        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//        L.l("is sign in success: " + signInResult.isSuccess());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        L.l("Google Games Api connected");

        if (googleApiClient.hasConnectedApi(Games.API)) {

            Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    L.l("Silent sign in callback");
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        googleApiClient.connect();
    }
}
