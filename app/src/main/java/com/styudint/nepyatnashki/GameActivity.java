package com.styudint.nepyatnashki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    boolean isGameOver = false;
    TextView stepsCounterTextView;
    TextView timerTextView;
    ImageButton[] buttons;
    int nSteps = 0;
    Bitmap background;
    int size;
    long startTime;
    int[][] permutation = new int[4][];
    enum Directions {
        UP, DOWN, LEFT, RIGHT, NONE;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Integer> randPerm = new ArrayList<>();
        for (int i = 0; i < 16; i++)
            randPerm.add(i);
        Collections.shuffle(randPerm);
        if (CountInversions(randPerm) % 2 == 1) {
            if (randPerm.get(0) != 15 && randPerm.get(1) != 15) {
                int tmp = randPerm.get(0);
                randPerm.set(0, randPerm.get(1));
                randPerm.set(1, tmp);
            } else {
                int tmp = randPerm.get(2);
                randPerm.set(2, randPerm.get(3));
                randPerm.set(3, tmp);
            }
        }

        for (int i = 0; i < 4; i++) {
            permutation[i] = new int[4];
            for (int j = 0 ; j < 4; j++) {
                permutation[i][j] = randPerm.get(4 * i + j);
            }
        }

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

        for (int i = 0; i < 16; i++) {
            int id = permutation[i / 4][i % 4];
            buttons[i].setImageBitmap(Bitmap.createBitmap(background, (id % 4) * size / 4, (id / 4) * size / 4, size / 4, size / 4));
            if (id == 15)
                buttons[i].setVisibility(View.INVISIBLE);
        }

        stepsCounterTextView = findViewById(R.id.stepsCounterTextView);
        timerTextView = findViewById(R.id.timerTextView);
        startTime = System.currentTimeMillis();

        startStopWatch();
    }


    public void onClick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        int row = tag / 4;
        int col = tag % 4;

        Directions direction = Directions.NONE;
        if (row > 0 && permutation[row - 1][col] == 15)
            direction = Directions.UP;
        else if (row < 3 && permutation[row + 1][col] == 15)
            direction = Directions.DOWN;
        if (col > 0 && permutation[row][col - 1] == 15)
            direction = Directions.LEFT;
        if (col < 3 && permutation[row][col + 1] == 15)
            direction = Directions.RIGHT;

        if (direction != Directions.NONE) {
            SwapStates(row, col, direction);
            ++nSteps;
            stepsCounterTextView.setText(Integer.toString(nSteps));

            boolean isSolved = true;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    isSolved = isSolved & (permutation[i][j] == 4 * i + j);
                }
            }
            if (isSolved) {
                isGameOver = true;
                Toast toast = Toast.makeText(getApplicationContext(), "Legendary!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void SwapStates(int row, int col, Directions direction) {
        int targetRow = row, targerCol = col;
        switch (direction) {
            case UP:
                --targetRow;
                break;
            case DOWN:
                ++targetRow;
                break;
            case LEFT:
                --targerCol;
                break;
            case RIGHT:
                ++targerCol;
        }

        buttons[4 * row + col].setVisibility(View.INVISIBLE);
        buttons[4 * targetRow + targerCol].setVisibility(View.VISIBLE);

        int tmp = permutation[row][col];
        permutation[row][col] = permutation[targetRow][targerCol];
        permutation[targetRow][targerCol] = tmp;

        buttons[4 * targetRow + targerCol].setImageBitmap((Bitmap.createBitmap(background, (tmp % 4) * size / 4, (tmp / 4) * size / 4, size / 4, size / 4)));
    }

    public int CountInversions(ArrayList<Integer> arrayList) {
        int result = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) == 15) {
                result += (i / 4) + 1;
            } else {
                for (int j = i + 1; j < arrayList.size(); j++) {
                    if (arrayList.get(j) < arrayList.get(i))
                        ++result;
                }
            }
        }
        return result;
    }

    public void startStopWatch() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isGameOver) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long currentTime = (System.currentTimeMillis() - startTime) / 10;

                            timerTextView.setText(String.format("%d:%02d:%02d", currentTime / 6000, (currentTime / 100) % 60, currentTime % 100));
                        }
                    });
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
