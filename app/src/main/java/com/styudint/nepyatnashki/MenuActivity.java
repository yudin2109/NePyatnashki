package com.styudint.nepyatnashki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
}
