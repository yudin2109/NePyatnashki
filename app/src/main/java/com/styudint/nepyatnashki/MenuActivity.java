package com.styudint.nepyatnashki;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.styudint.nepyatnashki.data.GameRequisitesHolder;

import javax.inject.Inject;

public class MenuActivity extends AppCompatActivity {

    static int OPEN_IMAGE = 1;

    @Inject
    GameRequisitesHolder requisitesHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ((NePyatnashkiApp) getApplication()).getAppComponent().inject(this);

        final Activity thisActivity = this;

        requisitesHolder.bitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                ContentLoadingProgressBar progressBar = findViewById(R.id.loadingPlaceHolder);
                progressBar.hide();

                AppCompatImageView imageView = findViewById(R.id.mainImage);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ActivityCompat.checkSelfPermission(
                                getApplicationContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, OPEN_IMAGE);
                        } else {
                            ActivityCompat.requestPermissions(thisActivity,
                                new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                0);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == OPEN_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) {
                requisitesHolder.loadImage(uri);
            }
        }
    }


    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GalleryPage.class);
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
