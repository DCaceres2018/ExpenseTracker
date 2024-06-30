package com.example.expensetrackerv01;

import static com.example.expensetrackerv01.R.layout.activity_add_expense;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.ComponentActivity;

import java.util.Arrays;
import java.util.List;

public class expenses_java extends ComponentActivity {
    static int totalGastos;
    private EditText etImporte;
    private Spinner spinnerCategoria;
    private Spinner metododepago;
    private EditText etSubcategoria;
    private EditText etFecha; // Añade esto
    private Button btnGuardarGasto;
    private DataBaseHelper_Java dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add_expense);

        etImporte = findViewById(R.id.et_amount);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        metododepago = findViewById(R.id.sp_payment_method);
        etSubcategoria = findViewById(R.id.et_subcategory);
        etFecha = findViewById(R.id.et_fecha); // Añade esto
        btnGuardarGasto = findViewById(R.id.btn_save_expense);
        dbHelper = new DataBaseHelper_Java(this);
        totalGastos = dbHelper.contarGastos();

        TextView tvTotalGastos = findViewById(R.id.et_gastos);
        tvTotalGastos.setText("Total de gastos introducidos: " + totalGastos);

        Button btnVolver = findViewById(R.id.btn_volverExp);
        btnVolver.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(com.example.expensetrackerv01.expenses_java.this, MainActivity_Java.class);
                        startActivity(intent);
                    }
                });

        cargarCategoriasEnSpinner();
        cargarMetodosEnSpinner();

        btnGuardarGasto.setOnClickListener(view -> {
            String importe = etImporte.getText().toString();
            String categoria = spinnerCategoria.getSelectedItem().toString();
            String metodoPago = metododepago.getSelectedItem().toString();
            String subcategoria = etSubcategoria.getText().toString();
            String fecha = etFecha.getText().toString(); // Añade esto

            guardarGasto(importe, categoria, metodoPago, subcategoria, fecha); // Añade esto
        });
    }

    private void cargarCategoriasEnSpinner() {
        List<String> categorias = dbHelper.obtenerCategorias();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void cargarMetodosEnSpinner() {
        List<String> subcategorias = Arrays.asList("Efectivo", "Bizum", "Tarjeta");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subcategorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metododepago.setAdapter(adapter);
    }

    private void guardarGasto(String importe, String categoria, String metodoPago, String subcategoria, String fecha) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("importe", importe);
        values.put("categoria", categoria);
        values.put("metodo_pago", metodoPago);
        values.put("subcategoria", subcategoria);
        values.put("fecha", fecha); // Añade esto
        totalGastos = totalGastos + 1;
        TextView tvTotalGastos = findViewById(R.id.et_gastos);
        tvTotalGastos.setText("Total de Gastos: " + totalGastos);

        db.insert("gastos", null, values);
    }
}
