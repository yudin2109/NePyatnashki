package com.styudint.nepyatnashki;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.styudint.nepyatnashki.account.AccountManager;
import com.styudint.nepyatnashki.data.AndroidGameState;
import com.styudint.nepyatnashki.data.BitmapCache;
import com.styudint.nepyatnashki.data.GameInfo;
import com.styudint.nepyatnashki.data.GameRequisitesHolder;
import com.styudint.nepyatnashki.data.GameStartStateGenerator;
import com.styudint.nepyatnashki.data.AndroidGameStateListener;
import com.styudint.nepyatnashki.data.GameStartStateGeneratorImpl;
import com.styudint.nepyatnashki.data.repositories.StatisticsRepository;
import com.styudint.nepyatnashki.gameviews.GameView;
import com.styudint.nepyatnashki.settings.ControlMode;
import com.styudint.nepyatnashki.settings.SettingsManager;
import com.styudint.nepyatnashki.settings.SettingsManagerListener;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import kotlin.Pair;

public class GameActivity extends AppCompatActivity implements AndroidGameStateListener, SettingsManagerListener {
    static float swipeThreshold = 120f;

    TextView stepsCounterTextView;
    TextView timerTextView;
    GameView gameView;

    GestureDetectorCompat gestureDetector;

    @Inject
    StatisticsRepository statsRepo;

    @Inject
    GameStartStateGenerator generator;

    @Inject
    SettingsManager settingsManager;

    @Inject
    AccountManager accountManager;

    @Inject
    BitmapCache bitmapCache;

    @Inject
    GameRequisitesHolder requisitesHolder;

    AndroidGameState currentGameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((NePyatnashkiApp) getApplication()).getAppComponent().inject(this);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        final AppCompatActivity thisActivity = this;

        requisitesHolder.bitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap background) {
                int size = Math.min(background.getWidth(), background.getHeight());

                bitmapCache.initialize(background);
                bitmapCache.setupSizes(requisitesHolder.getWidth(), requisitesHolder.getHeight());
                bitmapCache.setupSizeBounds(0, 0, size, size);


                stepsCounterTextView = findViewById(R.id.stepsCounterTextView);
                timerTextView = findViewById(R.id.timerTextView);
                gameView = findViewById(R.id.gameView);

                generator.changeSizes(requisitesHolder.getWidth(), requisitesHolder.getHeight());
                generator.generate().observe(thisActivity, new Observer<AndroidGameState>() {
                    @Override
                    public void onChanged(AndroidGameState gameState) {
                        if (gameState == null)
                            return;
                        if (currentGameState != null)
                            throw new IllegalStateException("Game state has been already initialized");
                        currentGameState = gameState;

                        startGame(gameState);
                    }
                });
            }
        });

        settingsManager.subscribe(this);
    }

    private void startGame(AndroidGameState gameState) {
        gameState.start();
        gameState.subscribe(this);
        gameState.stopWatch().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long timeValue) {
                timerTextView.setText(formatTime(timeValue));
            }
        });

        applyGameState(gameState);
    }

    private String formatTime(long time) {
        time /= 10;
        return String.format("%d:%02d:%02d", time / 6000, (time / 100) % 60, time % 100);
    }

    private void applyGameState(AndroidGameState gameState) {
        stepsCounterTextView.setText(Integer.valueOf(gameState.moves()).toString());
    }

    @Override
    public void gameStateChanged() {
        applyGameState(currentGameState);
    }

    @Override
    public void solved() {
        Toast.makeText(getApplicationContext(), "Legendary!", Toast.LENGTH_LONG).show();
        FirebaseUser user = accountManager.currentUser();
        statsRepo.saveGame(new GameInfo(
            currentGameState.startTime(),
            currentGameState.gameTime(),
            currentGameState.moveLog(),
            true,
            user == null ? null : user.getUid()));
    }

    public void onClick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (settingsManager.controlMode() == ControlMode.CLICKS) {
            currentGameState.handleTap(tag);
        }
    }

    @Override
    public void onBackPressed() {
        currentGameState.stop();
        FirebaseUser user = accountManager.currentUser();
        if (!currentGameState.isSolved()) {
            /*statsRepo.saveGame(new GameInfo(
                currentGameState.startTime(),
                currentGameState.gameTime(),
                currentGameState.moveLog(),
                false,
                user == null ? null : user.getUid()));*/
        }
        settingsManager.unsubscribe(this);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void settingsChanged() {
        if (settingsManager.controlMode() == ControlMode.CLICKS) {

        } else if (settingsManager.controlMode() == ControlMode.SWIPES) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        settingsManager.unsubscribe(this);
        gameView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        settingsManager.subscribe(this);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float dX = e2.getX() - e1.getX();
            float dY = e2.getY() - e1.getY();

            if (settingsManager.controlMode() == ControlMode.SWIPES) {
                if (Math.abs(dX) > Math.abs(dY)) {
                    if (Math.abs(dX) > swipeThreshold) {
                        if (dX < 0) {
                            currentGameState.moveLeft();
                        } else {
                            currentGameState.moveRight();
                        }
                    }
                } else {
                    if (Math.abs(dY) > swipeThreshold) {
                        if (dY < 0) {
                            currentGameState.moveUp();
                        } else {
                            currentGameState.moveDown();
                        }
                    }
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
