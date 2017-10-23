package mb.wordslide.src;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.Leaderboards;

import java.util.ArrayList;
import java.util.List;

import mb.wordslide.R;

public class LeaderboardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }


    private void getTopScore(GoogleApiClient apiClient, String leaderboardId){
        PendingResult<Leaderboards.LoadScoresResult> pendingResult = Games.Leaderboards
                .loadTopScores(apiClient, leaderboardId, 2, 0, 15, true);
        pendingResult.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
            @Override
            public void onResult(@NonNull Leaderboards.LoadScoresResult loadScoresResult) {
                L.l("on leaderboard csores result");
                List<LeaderboardScore> scores = extractScores(loadScoresResult);
                for(LeaderboardScore score: scores){
                    L.l("Score: " + score.getDisplayScore());
                }
            }
        });
    }

    private List<LeaderboardScore> extractScores(Leaderboards.LoadScoresResult loadScoresResult) {
        LeaderboardScoreBuffer buffer = loadScoresResult.getScores();
        L.l("scores count: " + buffer.getCount());
        List<LeaderboardScore> result = new ArrayList<>();
        for(int counter = 0; counter < buffer.getCount(); ++counter){
            result.add(buffer.get(counter));
        }
        return result;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        L.l("Leaderboard activity: connected");
        getTopScore(googleApiClient, "CggIpumhxDoQAhAB");
    }

    @Override
    public void onConnectionSuspended(int i) {

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
}
