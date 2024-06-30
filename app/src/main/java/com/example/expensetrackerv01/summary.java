package com.example.expensetrackerv01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class summary extends ComponentActivity {

    private RecyclerView mRecyclerView;
    private GastoAdapter mAdapter;
    private List<Gasto> mGastos;
    private DataBaseHelper_Java dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Inicializar RecyclerView y variables
        mRecyclerView = findViewById(R.id.recycler_view);
        mGastos = new ArrayList<>();
        dbHelper = new DataBaseHelper_Java(this);

        // Configurar RecyclerView y adaptador
        mAdapter = new GastoAdapter(this, mGastos, dbHelper);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        // Cargar gastos desde la base de datos
        cargarGastos();

        // Botón para volver a MainActivity
        Button btnVolverSu = findViewById(R.id.btn_volverSum);
        btnVolverSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(summary.this, MainActivity_Java.class);
                startActivity(intent);
            }
        });
    }

    private void cargarGastos() {
        mGastos.clear();
        mGastos.addAll(dbHelper.obtenerListaGastos());
        mAdapter.notifyDataSetChanged();
    }
}
