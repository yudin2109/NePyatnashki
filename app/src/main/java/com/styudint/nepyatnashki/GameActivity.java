package com.styudint.nepyatnashki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.styudint.nepyatnashki.data.GameInfo;
import com.styudint.nepyatnashki.data.GameStartStateGenerator;
import com.styudint.nepyatnashki.data.GameState;
import com.styudint.nepyatnashki.data.GameStateListener;
import com.styudint.nepyatnashki.data.repositories.StatisticsRepository;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class GameActivity extends AppCompatActivity implements GameStateListener {
    boolean isGameOver = false;
    TextView stepsCounterTextView;
    TextView timerTextView;
    ImageButton[] buttons;
    Bitmap background;
    int size;
    long startTime;

    @Inject
    StatisticsRepository statsRepo;

    @Inject
    GameStartStateGenerator generator;

    long timestamp;

    GameState currentGameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((NePyatnashkiApp) getApplication()).getAppComponent().inject(this);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.test_misha);
        background = Bitmap.createScaledBitmap(background, background.getWidth(), background.getHeight(), false);
        size = background.getHeight();

        buttons = new ImageButton[16];
        buttons[0] = (ImageButton) findViewById(R.id.button1);
        buttons[1] = (ImageButton) findViewById(R.id.button2);
        buttons[2] = (ImageButton) findViewById(R.id.button3);
        buttons[3] = (ImageButton) findViewById(R.id.button4);
        buttons[4] = (ImageButton) findViewById(R.id.button5);
        buttons[5] = (ImageButton) findViewById(R.id.button6);
        buttons[6] = (ImageButton) findViewById(R.id.button7);
        buttons[7] = (ImageButton) findViewById(R.id.button8);
        buttons[8] = (ImageButton) findViewById(R.id.button9);
        buttons[9] = (ImageButton) findViewById(R.id.button10);
        buttons[10] = (ImageButton) findViewById(R.id.button11);
        buttons[11] = (ImageButton) findViewById(R.id.button12);
        buttons[12] = (ImageButton) findViewById(R.id.button13);
        buttons[13] = (ImageButton) findViewById(R.id.button14);
        buttons[14] = (ImageButton) findViewById(R.id.button15);
        buttons[15] = (ImageButton) findViewById(R.id.button16);

        stepsCounterTextView = findViewById(R.id.stepsCounterTextView);
        timerTextView = findViewById(R.id.timerTextView);

        generator.generate().observe(this, new Observer<GameState>() {
            @Override
            public void onChanged(GameState gameState) {
                currentGameState = gameState;
                startGame(gameState);
            }
        });
    }

    private void startGame(GameState gameState) {
        startTime = System.currentTimeMillis();
        timestamp = System.currentTimeMillis();

        gameState.subscribe(this);

        applyGameState(gameState);

        startStopWatch();
    }

    private void applyGameState(GameState gameState) {
        for (int i = 0; i < 16; i++) {
            int id = gameState.permutation().get(i);
            buttons[i].setImageBitmap(Bitmap.createBitmap(background, (id % 4) * size / 4, (id / 4) * size / 4, size / 4, size / 4));
            if (id == 15)
                buttons[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void gameStateChanged() {
        applyGameState(currentGameState);
    }

    @Override
    public void solved() {
        Toast.makeText(getApplicationContext(), "Legendary!", Toast.LENGTH_LONG).show();
        long endTime = System.currentTimeMillis();
        long time = (endTime - startTime) / 10;
        statsRepo.saveGame(new GameInfo(startTime, time, true));
        isGameOver = true;
    }

    public void onClick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());

        currentGameState.handleTap(tag);
    }

    @Override
    public void onBackPressed() {
        if (!isGameOver) {
            long endTime = System.currentTimeMillis();
            long time = (endTime - startTime) / 10;
            statsRepo.saveGame(new GameInfo(startTime, time, false));
        }
        finish();
    }

    public void startStopWatch() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isGameOver) {
                        final long currentTime = (System.currentTimeMillis() - startTime) / 10;
                        timerTextView.post(new Runnable() {
                            @Override
                            public void run() {
                            timerTextView.setText(String.format("%d:%02d:%02d", currentTime / 6000, (currentTime / 100) % 60, currentTime % 100));
                            }
                        });

                        Thread.sleep(16);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
