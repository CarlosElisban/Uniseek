package com.ucsm.uniseek.appuniseek;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucsm.uniseek.R;

import java.util.Calendar;

public class PrincipalUniseekActivity extends AppCompatActivity implements View.OnClickListener {
    Button bfecha,bhora;
    EditText efecha,ehora;
    private int dia,mes,ano,hora,minutos;

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
        
        bfecha = (Button) findViewById(R.id.seleccionarFechaButton);
        bhora = (Button) findViewById(R.id.seleccionarHoraButton);
        efecha = (EditText) findViewById(R.id.fechaEditText);
        ehora = (EditText) findViewById(R.id.horaEditText);

        Calendar calendario = Calendar.getInstance();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        ano = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);

        // Establecer la fecha y hora actual en los EditText
        efecha.setText(dia + "/" + (mes + 1) + "/" + ano);
        ehora.setText(hora + ":" + minutos);
        
        bfecha.setOnClickListener(this);
        bhora.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==bfecha){
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            },dia,mes,ano);
            datePickerDialog.show();
        }
        if(v==bhora){
            final Calendar c = Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();
        }
    }
}