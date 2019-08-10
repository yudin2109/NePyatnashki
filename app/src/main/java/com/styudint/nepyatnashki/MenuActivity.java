package com.styudint.nepyatnashki;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGameActivity(View view) {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        MenuActivity.this.startActivity(intent);
    }

    public void onStatsClicked(View view) {
        Intent intent = new Intent(this, StatisticsPage.class);
        startActivity(intent);
    }

    public void onSettingsClicked(View view) {
        startActivity(new Intent(this, SettingsPage.class));
    }
}
