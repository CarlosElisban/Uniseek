package com.ucsm.uniseek.appuniseek;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ucsm.uniseek.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchObjectActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private LostItemAdapter adapter;
    private List<LostItem> lostItemList;
    private EditText filterColor, filterAdicional;
    private TextView filterFecha, filterHora;
    private Calendar calendar;
    private Button toggleFiltersButton,volver;
    private boolean filtersVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_object);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lostItemList = new ArrayList<>();
        adapter = new LostItemAdapter(lostItemList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.search_view);
        filterColor = findViewById(R.id.filter_color);
        filterFecha = findViewById(R.id.filter_fecha);
        filterHora = findViewById(R.id.filter_hora);
        filterAdicional = findViewById(R.id.filter_adicional);
        toggleFiltersButton = findViewById(R.id.toggle_filters_button);
        volver = findViewById(R.id.volver);

        filterColor.setVisibility(View.GONE);
        filterFecha.setVisibility(View.GONE);
        filterHora.setVisibility(View.GONE);
        filterAdicional.setVisibility(View.GONE);

        calendar = Calendar.getInstance();

        filterFecha.setOnClickListener(v -> showDatePickerDialog());
        filterHora.setOnClickListener(v -> showTimePickerDialog());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLostItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    lostItemList.clear();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        toggleFiltersButton.setOnClickListener(v -> toggleFiltersVisibility());
        volver.setOnClickListener(v -> {
            Intent intent = new Intent(SearchObjectActivity.this, FirstUniseekActivity.class);
            startActivity(intent);
        });
    }
    private void toggleFiltersVisibility() {
        if (filtersVisible) {
            filterColor.setVisibility(View.VISIBLE);
            filterFecha.setVisibility(View.VISIBLE);
            filterHora.setVisibility(View.VISIBLE);
            filterAdicional.setVisibility(View.VISIBLE);
            toggleFiltersButton.setText("Ocultar Filtros");

        } else {
            filterColor.setVisibility(View.GONE);
            filterFecha.setVisibility(View.GONE);
            filterHora.setVisibility(View.GONE);
            filterAdicional.setVisibility(View.GONE);
            toggleFiltersButton.setText("Mostrar Filtros");
        }
        filtersVisible = !filtersVisible;
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDateLabel() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        filterFecha.setText(day + "/" + month + "/" + year);
    }

    private void updateTimeLabel() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        filterHora.setText(String.format("%02d:%02d", hour, minute));
    }

    private void searchLostItems(String query) {
        String color = filterColor.getText().toString().trim();
        String fecha = filterFecha.getText().toString().trim();
        String hora = filterHora.getText().toString().trim();
        String adicional = filterAdicional.getText().toString().trim();

        db.collection("Objetos perdidos")
                .whereEqualTo("Objeto", query)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lostItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean matches = true;
                            if (!color.isEmpty() && !color.equals(document.getString("Color"))) matches = false;
                            if (!fecha.isEmpty() && !fecha.equals(document.getString("Fecha"))) matches = false;
                            if (!hora.isEmpty() && !hora.equals(document.getString("Hora"))) matches = false;
                            if (!adicional.isEmpty() && !adicional.equals(document.getString("Adicional"))) matches = false;

                            if (matches) {
                                LostItem item = new LostItem();
                                item.setAdicional(document.getString("Adicional"));
                                item.setColor(document.getString("Color"));
                                item.setFecha(document.getString("Fecha"));
                                item.setHora(document.getString("Hora"));
                                item.setImagenURL(document.getString("ImagenURL"));
                                item.setObjeto(document.getString("Objeto"));
                                item.setReporte(document.getString("Reporte"));
                                lostItemList.add(item);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Manejar error
                    }
                });
    }

    public class LostItem {
        private String adicional;
        private String color;
        private String fecha;
        private String hora;
        private String imagenURL;
        private String objeto;
        private String reporte;

        // Constructor vacío necesario para Firestore
        public LostItem() {}

        // Getters y setters
        public String getAdicional() { return adicional; }
        public void setAdicional(String adicional) { this.adicional = adicional; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public String getHora() { return hora; }
        public void setHora(String hora) { this.hora = hora; }
        public String getImagenURL() { return imagenURL; }
        public void setImagenURL(String imagenURL) { this.imagenURL = imagenURL; }
        public String getObjeto() { return objeto; }
        public void setObjeto(String objeto) { this.objeto = objeto; }
        public String getReporte() { return reporte; }
        public void setReporte(String reporte) { this.reporte = reporte; }
    }

    public class LostItemAdapter extends RecyclerView.Adapter<LostItemAdapter.LostItemViewHolder> {
        private final List<LostItem> lostItemList;

        public LostItemAdapter(List<LostItem> lostItemList) {
            this.lostItemList = lostItemList;
        }

        @NonNull
        @Override
        public LostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost, parent, false);
            return new LostItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LostItemViewHolder holder, int position) {
            LostItem item = lostItemList.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return lostItemList.size();
        }

        public class LostItemViewHolder extends RecyclerView.ViewHolder {
            private final ImageView itemImage;
            private final TextView itemColor, itemFecha, itemHora, itemAdicional, itemReporte, itemObjeto;

            public LostItemViewHolder(@NonNull View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.item_image);
                itemColor = itemView.findViewById(R.id.item_color);
                itemFecha = itemView.findViewById(R.id.item_fecha);
                itemHora = itemView.findViewById(R.id.item_hora);
                itemAdicional = itemView.findViewById(R.id.item_adicional);
                itemReporte = itemView.findViewById(R.id.item_reporte);
                itemObjeto = itemView.findViewById(R.id.item_objeto);

                itemImage.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        LostItem clickedItem = lostItemList.get(position);
                        Intent intent = new Intent(itemView.getContext(), FullscreenImageActivity.class);
                        intent.putExtra("image_url", clickedItem.getImagenURL());
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

            public void bind(LostItem item) {
                itemColor.setText(item.getColor());
                itemFecha.setText(item.getFecha());
                itemHora.setText(item.getHora());
                itemAdicional.setText(item.getAdicional());
                itemReporte.setText(item.getReporte());
                itemObjeto.setText(item.getObjeto());

                // Cargar imagen desde la URL
                Glide.with(itemView.getContext())
                        .load(item.getImagenURL())
                        .into(itemImage);
            }
        }
    }

}



