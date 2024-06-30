package com.example.expensetrackerv01;

import static com.example.expensetrackerv01.R.layout.activity_organize_categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import androidx.activity.ComponentActivity;
import java.util.List;

public class organizacion_java extends ComponentActivity {

    Button btnVolver;
    private EditText etNuevaCategoria, etNuevaSubcategoria;
    private Button btnAnadirCategoria, btnAnadirSubcategoria;
    private ListView lvCategorias, lvSubCategorias;
    private DataBaseHelper_Java dbHelper;
    private String categoriaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(activity_organize_categories);

        etNuevaCategoria = findViewById(R.id.et_nueva_categoria);
        btnAnadirCategoria = findViewById(R.id.btn_add_categoria);
        lvCategorias = findViewById(R.id.lv_categorias);
        btnVolver = findViewById(R.id.btn_volver);

        btnVolver.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(com.example.expensetrackerv01.organizacion_java.this, MainActivity_Java.class);
                        startActivity(intent);
                    }
                });

        dbHelper = new DataBaseHelper_Java(this);

        btnAnadirCategoria.setOnClickListener(view -> {
            String nuevaCategoria = etNuevaCategoria.getText().toString();
            dbHelper.añadirCategoria(nuevaCategoria);
            cargarCategorias();
        });


        lvCategorias.setOnItemLongClickListener((parent, view, position, id) -> {
            String categoria = (String) parent.getItemAtPosition(position);
            dbHelper.eliminarCategoria(categoria);
            cargarCategorias();
            return true;
        });


        cargarCategorias();
    }

    private void cargarCategorias() {
        List<String> categorias = dbHelper.obtenerCategorias();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        lvCategorias.setAdapter(adapter);
    }
}
