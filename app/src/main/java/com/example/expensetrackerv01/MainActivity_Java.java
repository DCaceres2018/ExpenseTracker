package com.example.expensetrackerv01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.activity.ComponentActivity;

public class MainActivity_Java extends ComponentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnExpenses= findViewById(R.id.btn_add_expense);
        Button btnSummary= findViewById(R.id.btn_summary);
        Button btn = findViewById(R.id.btn_organize);
        Button stats = findViewById(R.id.btn_stats);

        btn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity_Java.this, organizacion_java.class);
            startActivity(intent);
        });
        btnExpenses.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity_Java.this, expenses_java.class);
            startActivity(intent);
        });
        btnSummary.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity_Java.this, summary.class);
            startActivity(intent);
        });
        stats.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity_Java.this, statisticsActivity.class);
            startActivity(intent);
        });
    }


}
