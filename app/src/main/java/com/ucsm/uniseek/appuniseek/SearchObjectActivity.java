package com.ucsm.uniseek.appuniseek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import com.ucsm.uniseek.R;

public class SearchObjectActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private LostItemAdapter adapter;
    private List<LostItem> lostItemList;

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
    }

    private void searchLostItems(String query) {
        db.collection("Objetos perdidos")
                .whereEqualTo("Objeto", query)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lostItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
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

