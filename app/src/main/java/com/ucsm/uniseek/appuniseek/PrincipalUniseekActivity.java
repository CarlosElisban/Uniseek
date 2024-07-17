package com.ucsm.uniseek.appuniseek;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucsm.uniseek.R;

public class PrincipalUniseekActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal_uniseek);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Obtener referencia del boton de la actividad de lanzamiento
        Button button = findViewById(R.id.button2);

        // Agregar listener al botón
        button.setOnClickListener(view -> {
            // Intent para navegar a OtraActividad
            Intent intent = new Intent(PrincipalUniseekActivity.this, FirstUniseekActivity.class);
            startActivity(intent);
        });

        EditText colorEditText = findViewById(R.id.color);
        EditText objetoEditText = findViewById(R.id.objeto);
        ImageView imageView = findViewById(R.id.imagephoto);

        // Recibir datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String objeto = intent.getStringExtra("objeto");
            String color = intent.getStringExtra("color");

            byte[] byteArray = intent.getByteArrayExtra("imagen");

            // Asignar los datos a los EditText
            objetoEditText.setText(objeto);
            colorEditText.setText(color);
            if (byteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}