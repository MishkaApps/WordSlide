package mb.wordslide.src;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.Leaderboards;

import java.util.ArrayList;
import java.util.List;

import mb.wordslide.R;

import static com.google.android.gms.games.leaderboard.LeaderboardVariant.COLLECTION_PUBLIC;
import static com.google.android.gms.games.leaderboard.LeaderboardVariant.COLLECTION_SOCIAL;
import static com.google.android.gms.games.leaderboard.LeaderboardVariant.TIME_SPAN_ALL_TIME;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        findViewById(R.id.btn_get_scores).setOnClickListener(this);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addConnectionCallbacks(this)
                .build();
    }


    private void getTopScore(GoogleApiClient apiClient, String leaderboardId) {
        PendingResult<Leaderboards.LoadScoresResult> pendingResult = Games.Leaderboards
                .loadTopScores(apiClient, leaderboardId, TIME_SPAN_ALL_TIME, COLLECTION_PUBLIC, 10, true);
        pendingResult.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            @Override
            public void onResult(@NonNull Leaderboards.LoadScoresResult loadScoresResult) {
                L.l("on leaderboard csores result");
                List<LeaderboardScore> scores = extractScores(loadScoresResult);
                for (LeaderboardScore score : scores) {
                    L.l("Score: " + score.getDisplayScore());
                }
            }
        });
    }

    private List<LeaderboardScore> extractScores(Leaderboards.LoadScoresResult loadScoresResult) {
        LeaderboardScoreBuffer buffer = loadScoresResult.getScores();
        ((TextView)findViewById(R.id.tv_score_view)).setText(Integer.toString(buffer.getCount()));
        List<LeaderboardScore> result = new ArrayList<>();
        for (int counter = 0; counter < buffer.getCount(); ++counter) {
            result.add(buffer.get(counter));
        }
        return result;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        L.l("LeaderboardActivity: Google Games Api connected");

        if (googleApiClient.hasConnectedApi(Games.API)) {

            Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    L.l("LeaderboardActivity: Silent sign in callback");
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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
        if (v.getId() == R.id.btn_get_scores)
            getTopScore(googleApiClient, "CggIpumhxDoQAhAB");
    }
}
