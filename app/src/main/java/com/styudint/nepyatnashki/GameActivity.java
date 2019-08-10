package com.styudint.nepyatnashki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.styudint.nepyatnashki.data.GameInfo;
import com.styudint.nepyatnashki.data.GameStartStateGenerator;
import com.styudint.nepyatnashki.data.GameState;
import com.styudint.nepyatnashki.data.GameStateListener;
import com.styudint.nepyatnashki.data.repositories.StatisticsRepository;
import com.styudint.nepyatnashki.settings.ControlMode;
import com.styudint.nepyatnashki.settings.SettingsManager;
import com.styudint.nepyatnashki.settings.SettingsManagerListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class GameActivity extends AppCompatActivity implements GameStateListener, SettingsManagerListener {
    static float swipeThreshold = 120f;

    TextView stepsCounterTextView;
    TextView timerTextView;
    ArrayList<ImageButton> buttons = new ArrayList<>();
    Bitmap background;
    int size;

    GestureDetectorCompat gestureDetector;

    @Inject
    StatisticsRepository statsRepo;

    @Inject
    GameStartStateGenerator generator;

    @Inject
    SettingsManager settingsManager;

    GameState currentGameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((NePyatnashkiApp) getApplication()).getAppComponent().inject(this);

        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

        background = BitmapFactory.decodeResource(getResources(), R.drawable.test_misha);
        background = Bitmap.createScaledBitmap(background, background.getWidth(), background.getHeight(), false);
        size = background.getHeight();

        buttons.add((ImageButton) findViewById(R.id.button1));
        buttons.add((ImageButton) findViewById(R.id.button2));
        buttons.add((ImageButton) findViewById(R.id.button3));
        buttons.add((ImageButton) findViewById(R.id.button4));
        buttons.add((ImageButton) findViewById(R.id.button5));
        buttons.add((ImageButton) findViewById(R.id.button6));
        buttons.add((ImageButton) findViewById(R.id.button7));
        buttons.add((ImageButton) findViewById(R.id.button8));
        buttons.add((ImageButton) findViewById(R.id.button9));
        buttons.add((ImageButton) findViewById(R.id.button10));
        buttons.add((ImageButton) findViewById(R.id.button11));
        buttons.add((ImageButton) findViewById(R.id.button12));
        buttons.add((ImageButton) findViewById(R.id.button13));
        buttons.add((ImageButton) findViewById(R.id.button14));
        buttons.add((ImageButton) findViewById(R.id.button15));
        buttons.add((ImageButton) findViewById(R.id.button16));

        stepsCounterTextView = findViewById(R.id.stepsCounterTextView);
        timerTextView = findViewById(R.id.timerTextView);

        generator.generate().observe(this, new Observer<GameState>() {
            @Override
            public void onChanged(GameState gameState) {
                currentGameState = gameState;
                startGame(gameState);
            }
        });

        settingsManager.subscribe(this);
    }

    private void startGame(GameState gameState) {
        gameState.start();
        gameState.subscribe(this);
        gameState.moves().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                stepsCounterTextView.setText(integer.toString());
            }
        });
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

    private void applyGameState(GameState gameState) {
        for (int i = 0; i < 16; i++) {
            int id = gameState.permutation().get(i);
            buttons.get(i).setImageBitmap(Bitmap.createBitmap(background, (id % 4) * size / 4, (id / 4) * size / 4, size / 4, size / 4));
            if (id == 15) {
                buttons.get(i).setVisibility(View.INVISIBLE);
            } else {
                buttons.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void gameStateChanged() {
        applyGameState(currentGameState);
    }

    @Override
    public void solved() {
        Toast.makeText(getApplicationContext(), "Legendary!", Toast.LENGTH_LONG).show();
        statsRepo.saveGame(new GameInfo(currentGameState.startTime(), currentGameState.gameTime(), true));
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
        if (!currentGameState.isSolved()) {
            statsRepo.saveGame(new GameInfo(currentGameState.startTime(), currentGameState.gameTime(), false));
        }
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
            for (ImageButton btn : buttons) {
                btn.setClickable(true);
            }
        } else if (settingsManager.controlMode() == ControlMode.SWIPES) {
            for (ImageButton btn : buttons) {
                btn.setClickable(false);
            }
        }
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
