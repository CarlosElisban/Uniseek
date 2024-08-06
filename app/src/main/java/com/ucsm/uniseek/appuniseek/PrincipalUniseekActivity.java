package com.ucsm.uniseek.appuniseek;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ucsm.uniseek.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipalUniseekActivity extends AppCompatActivity implements View.OnClickListener {
    Button bfecha, bhora, aceptarButton;
    ImageButton refreshButton;
    EditText efecha, ehora, colorEditText, objetoEditText, adicionalEditText, nombreEditText;
    ImageView imageView;
    Spinner spinner;
    Switch reportSwitch;
    private int dia, mes, ano, hora, minutos;
    private Bitmap bitmap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

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
        adicionalEditText = findViewById(R.id.adicional);
        nombreEditText = findViewById(R.id.nombre);
        reportSwitch = findViewById(R.id.sreport);

        // Configurar botón de actualización (refresh)
        refreshButton = findViewById(R.id.refresh);
        refreshButton.setOnClickListener(v -> {
            String objeto = objetoEditText.getText().toString().trim();
            updateSpinnerOptions(objeto);
        });

        // Recibir datos del Intent
        Intent intent2 = getIntent();
        if (intent2 != null) {
            String objeto = intent2.getStringExtra("objeto");
            String color = intent2.getStringExtra("color");
            byte[] byteArray = intent2.getByteArrayExtra("imagen");

            String email = intent2.getStringExtra("email");

            objetoEditText.setText(objeto);
            colorEditText.setText(color);
            if (byteArray != null) {
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageView.setImageBitmap(bitmap);
            }

            if (email != null) {
                nombreEditText.setText(email);
            }

            updateSpinnerOptions(objeto);
        }

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

        aceptarButton = findViewById(R.id.button3);

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
        aceptarButton.setOnClickListener(this);

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

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                efecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }, dia, mes, ano);
            datePickerDialog.show();
        } else if (v == bhora) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                ehora.setText(hourOfDay + ":" + minute);
            }, hora, minutos, false);
            timePickerDialog.show();
        } else if (v == aceptarButton) {
            guardarDatosEnFirestore();
            Intent intent = new Intent(PrincipalUniseekActivity.this, FirstUniseekActivity.class);
            startActivity(intent);
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

    private void guardarDatosEnFirestore() {
        String color = colorEditText.getText().toString();
        String objeto = objetoEditText.getText().toString();
        String fecha = efecha.getText().toString();
        String hora = ehora.getText().toString();
        String adicional = adicionalEditText.getText().toString();
        String marcaModelo = spinner.getSelectedItem().toString();
        String reporte;

        if (reportSwitch.isChecked()) {
            reporte = "Reportado a la oficina de objetos perdidos";
        } else {
            reporte = nombreEditText.getText().toString();
        }

        // Crear un mapa de datos para almacenar en Firestore
        Map<String, Object> objetoPerdido = new HashMap<>();
        objetoPerdido.put("Color", color);
        objetoPerdido.put("Objeto", objeto);
        objetoPerdido.put("Fecha", fecha);
        objetoPerdido.put("Hora", hora);
        objetoPerdido.put("Reporte", reporte);

        if (marcaModelo.equals("Otro")) {
            objetoPerdido.put("Adicional", adicional);
        } else {
            objetoPerdido.put("Adicional", marcaModelo);
        }

        // Subir la imagen a Firebase Storage
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            // Crear una referencia a la carpeta "objetos" en Firebase Storage
            StorageReference storageRef = storage.getReference().child("objetos/" + objeto + "_" + System.currentTimeMillis() + ".jpg");

            UploadTask uploadTask = storageRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Guardar la URL de la imagen en Firestore
                    objetoPerdido.put("ImagenURL", uri.toString());

                    // Almacenar en Firestore
                    db.collection("Objetos perdidos")
                            .add(objetoPerdido)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Datos guardados con éxito", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                            });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Si no hay imagen, simplemente guarda los datos en Firestore
            db.collection("Objetos perdidos")
                    .add(objetoPerdido)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Datos guardados con éxito", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}



