package com.vdocipher.sampleapp.castdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.vdocipher.aegis.player.VdoPlayer;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    public static String PACKAGE_NAME;

    private TextView loadInfo;
    private Button playerButton;
    private VdoPlayer.VdoInitParams initParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        CastContext.getSharedInstance(this);

        setContentView(getResources().getIdentifier("activity_main","layout",MainActivity.PACKAGE_NAME));

        loadInfo = findViewById(getResources().getIdentifier("params_loader_info","id",MainActivity.PACKAGE_NAME));
        loadInfo.setText(getResources().getIdentifier("load_active_msg","string",MainActivity.PACKAGE_NAME));
        playerButton = findViewById(getResources().getIdentifier("player_btn","id",MainActivity.PACKAGE_NAME));

        obtainOtpAndPlaybackInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    /**
     * Fetch (otp + playbackInfo) and initialize VdoPlayer
     * here we're fetching a sample (otp + playbackInfo)
     * TODO you need to generate/fetch (otp + playbackInfo) OR (signature + playbackInfo) for the
     * video you wish to play
     */
    private void obtainOtpAndPlaybackInfo() {
        // todo use asynctask
        Log.i(TAG, "fetching params...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Pair<String, String> pair = Utils.getSampleOtpAndPlaybackInfo();
                    String otp = pair.first;
                    String playbackInfo = pair.second;
                    initParams = new VdoPlayer.VdoInitParams.Builder()
                            .setOtp(otp)
                            .setPlaybackInfo(playbackInfo)
                            .setPreferredCaptionsLanguage("en")
                            .build();

                    Log.i(TAG, "obtained new otp and playbackInfo");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadInfo.setText(getResources().getIdentifier("load_done_msg","string",MainActivity.PACKAGE_NAME));
                            playerButton.setEnabled(true);
                            playerButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadPlayerActivity();
                                }
                            });
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "Error fetching otp and playbackInfo: " + e.getClass().getSimpleName(),
                                    Toast.LENGTH_LONG).show();
                            Log.e(TAG, "error fetching otp and playbackInfo");
                            loadInfo.setText(getResources().getIdentifier("load_error_msg","string",MainActivity.PACKAGE_NAME));
                            loadInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    obtainOtpAndPlaybackInfo();
                                    loadInfo.setOnClickListener(null);
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }

    private void loadPlayerActivity() {
        Intent intent = new Intent(this, CastVdoPlayerActivity.class);
        intent.putExtra("initParams", initParams);
        startActivity(intent);
    }
}
