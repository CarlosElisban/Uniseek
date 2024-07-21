package com.ucsm.uniseek.appuniseek;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucsm.uniseek.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrincipalUniseekActivity extends AppCompatActivity implements View.OnClickListener {
    Button bfecha, bhora;
    ImageButton refreshButton;
    EditText efecha, ehora, colorEditText, objetoEditText, adicionalEditText, nombreEditText;
    ImageView imageView;
    Spinner spinner;
    Switch reportSwitch;  // Añade una variable para el Switch
    private int dia, mes, ano, hora, minutos;

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

        // Obtener referencia de los elementos de la UI
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(PrincipalUniseekActivity.this, FirstUniseekActivity.class);
            startActivity(intent);
        });

        colorEditText = findViewById(R.id.color);
        objetoEditText = findViewById(R.id.objeto);
        imageView = findViewById(R.id.imagephoto);
        spinner = findViewById(R.id.marca_modelo);
        adicionalEditText = findViewById(R.id.adicional); // Agregar referencia al EditText adicional
        nombreEditText = findViewById(R.id.nombre);
        reportSwitch = findViewById(R.id.sreport); // Inicializa el Switch

        // Configurar botón de actualización (refresh)
        refreshButton = findViewById(R.id.refresh);
        refreshButton.setOnClickListener(v -> {
            // Obtener el texto del EditText objeto
            String objeto = objetoEditText.getText().toString().trim();
            // Actualizar las opciones del Spinner
            updateSpinnerOptions(objeto);
        });

        // Recibir datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String objeto = intent.getStringExtra("objeto");
            String color = intent.getStringExtra("color");
            byte[] byteArray = intent.getByteArrayExtra("imagen");

            objetoEditText.setText(objeto);
            colorEditText.setText(color);
            if (byteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageView.setImageBitmap(bitmap);
            }

            // Actualizar opciones del Spinner según el color recibido
            updateSpinnerOptions(objeto);
        }

        // Configurar el OnItemSelectedListener para el Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("Otro")) {
                    adicionalEditText.setEnabled(true);
                } else {
                    adicionalEditText.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adicionalEditText.setEnabled(false);
            }
        });

        bfecha = findViewById(R.id.seleccionarFechaButton);
        bhora = findViewById(R.id.seleccionarHoraButton);
        efecha = findViewById(R.id.fechaEditText);
        ehora = findViewById(R.id.horaEditText);

        Calendar calendario = Calendar.getInstance();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        ano = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);

        efecha.setText(dia + "/" + (mes + 1) + "/" + ano);
        ehora.setText(hora + ":" + minutos);

        bfecha.setOnClickListener(this);
        bhora.setOnClickListener(this);

        // Configurar el OnCheckedChangeListener para el Switch
        reportSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                nombreEditText.setVisibility(View.GONE);
            } else {
                nombreEditText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == bfecha) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, dia, mes, ano);
            datePickerDialog.show();
        }
        if (v == bhora) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
    }

    private void updateSpinnerOptions(String objeto) {
        List<String> options = new ArrayList<>();
        switch (objeto.toLowerCase()) {
            case "billetera":
                options.add("Levi's");
                options.add("Gucci");
                options.add("Fossil");
                options.add("Calvin Klein");
                options.add("Nike");
                options.add("Adidas");
                options.add("Puma");
                options.add("Vans");
                options.add("Rip Curl");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "calculadora":
                options.add("Texas Instruments");
                options.add("Hewlett-Packard(HP)");
                options.add("Casio");
                options.add("Canon");
                options.add("Sharp");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "cargador":
                options.add("Dell-Laptop");
                options.add("HP-Laptop");
                options.add("Lenovo-Laptop");
                options.add("Apple-Laptop");
                options.add("Asus-Laptop");
                options.add("Anker-Smartphone");
                options.add("Aukey-Smartphone");
                options.add("Romax-Smartphone");
                options.add("Samsung-Smartphone");
                options.add("Apple-Smartphone");
                options.add("Huawei-Smartphone");
                options.add("Xiaomi-Smartphone");
                options.add("Oppo-Smartphone");
                options.add("Motorola-Smartphone");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "cartuchera":
                options.add("Parker");
                options.add("Sheaffer");
                options.add("BIC");
                options.add("Pilot");
                options.add("Uni-ball");
                options.add("Faber-Castell");
                options.add("Pentel");
                options.add("Zebra");
                options.add("Lamy");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "smartphone":
                options.add("iPhone");
                options.add("Galaxy");
                options.add("Huawei");
                options.add("Xiaomi");
                options.add("Motorola");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "dni":
                options.add("Masculino");
                options.add("Femenino");
                break;
            case "laptop":
                options.add("Dell");
                options.add("HP");
                options.add("Lenovo");
                options.add("Apple");
                options.add("Apple (Mac)");
                options.add("Acer");
                options.add("Asus");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "libro":
                options.add("Personal");
                options.add("De la universidad");
                break;
            case "mochila":
                options.add("JanSport");
                options.add("Vans");
                options.add("Nike");
                options.add("Adidas");
                options.add("L.L.Bean");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "reloj":
                options.add("Casio");
                options.add("Rolex");
                options.add("Seiko");
                options.add("Skagen");
                options.add("Swatch");
                options.add("Tissot");
                options.add("Omega");
                options.add("Tag Heuer");
                options.add("Otro");
                options.add("No especifica");
                break;
            case "tablet":
                options.add("iPad");
                options.add("Samsung Galaxy Tab");
                options.add("Microsoft Surface");
                options.add("Amazon Fire");
                options.add("Lenovo Yoga");
                options.add("Otro");
                options.add("No especifica");
                break;
            default:
                options.add("Grande");
                options.add("Mediano");
                options.add("Pequeño");
                break;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}



