package com.example.expensetrackerv01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class statisticsActivity extends ComponentActivity {

    private EditText etFechaInicio;
    private EditText etFechaFin;
    private Spinner spinnerCategoria;
    private Spinner spinnerMetodoPago;
    private Button btnBuscar;
    private Button btnVolverStats;
    private RecyclerView recyclerView;
    private GastoAdapter mAdapter;
    private DataBaseHelper_Java dbHelper;
    private List<Gasto> mGastos;
    private RecyclerView recyclerViewEstadisticas;
    private TextView tvTotalGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        etFechaInicio = findViewById(R.id.et_fecha_inicio);
        etFechaFin = findViewById(R.id.et_fecha_fin);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        spinnerMetodoPago = findViewById(R.id.spinner_metodo_pago);
        btnBuscar = findViewById(R.id.btn_buscar);
        btnVolverStats = findViewById(R.id.btn_volverStats);
        recyclerView = findViewById(R.id.recycler_view_estadisticas);

        tvTotalGastos = findViewById(R.id.tv_total_gastos);

        dbHelper = new DataBaseHelper_Java(this);

        cargarCategoriasEnSpinner();
        cargarMetodosEnSpinner();

        btnBuscar.setOnClickListener(v -> buscarGastos());

        btnVolverStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(statisticsActivity.this, MainActivity_Java.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGastos = dbHelper.obtenerListaGastos();
        mAdapter = new GastoAdapter(this, mGastos, dbHelper);
        recyclerView.setAdapter(mAdapter);
    }

    private void cargarCategoriasEnSpinner() {
        List<String> categorias = dbHelper.obtenerCategorias();
        categorias.add(0, "");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void cargarMetodosEnSpinner() {
        List<String> metodosPago = Arrays.asList("", "Efectivo", "Bizum", "Tarjeta");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metodosPago);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetodoPago.setAdapter(adapter);
    }
    /*@SuppressLint("SetTextI18n")
    private void buscarGastos() {
        String fechaInicio = etFechaInicio.getText().toString();
        String fechaFin = etFechaFin.getText().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String metodoPago = spinnerMetodoPago.getSelectedItem().toString();

        mGastos = dbHelper.obtenerGastosPorParametros(fechaInicio, fechaFin, categoria, metodoPago);
        TextView tvTotalGastos = findViewById(R.id.et_numeroDebug);
        tvTotalGastos.setText("Total de Gastos: "+mGastos.size());
        mAdapter = new GastoAdapter(this, mGastos,dbHelper);
        recyclerView.setAdapter(mAdapter);
    }*/

    @SuppressLint("SetTextI18n")
    private void buscarGastos() {
        mGastos.clear();
        String fechaInicio = etFechaInicio.getText().toString();
        String fechaFin = etFechaFin.getText().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String metodoPago = spinnerMetodoPago.getSelectedItem().toString();

        mGastos.addAll(dbHelper.obtenerGastosPorParametros(fechaInicio, fechaFin, categoria, metodoPago));
        TextView tvTotalGastos2 = findViewById(R.id.et_numeroDebug);
        tvTotalGastos2.setText("Total de Gastos: " + mGastos.size());

        mAdapter = new GastoAdapter(this, mGastos, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        double totalGastos = calcularTotalGastos(mGastos);
        tvTotalGastos.setText("Total de Gastos: " + totalGastos);

        mAdapter.notifyDataSetChanged();

    }

    private double calcularTotalGastos(List<Gasto> gastos) {
        double total = 0.0;
        for (Gasto gasto : gastos) {
            try {
                double importe = Double.parseDouble(gasto.getImporte());
                total += importe;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return total;
    }
}
