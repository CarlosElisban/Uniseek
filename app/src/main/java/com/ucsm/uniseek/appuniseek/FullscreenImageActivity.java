package com.ucsm.uniseek.appuniseek;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ucsm.uniseek.R;

public class FullscreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ImageView fullscreenImage = findViewById(R.id.fullscreen_image);

        // Obtener la URL de la imagen del Intent
        String imageUrl = getIntent().getStringExtra("image_url");
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(fullscreenImage);
        }
    }
}

