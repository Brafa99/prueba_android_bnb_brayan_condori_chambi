package com.example.prueba_tecnica_bnb_brayan_condori;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AutenticacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        ImageView btnBack = findViewById(R.id.btnBackAuth);
        Button btnSiguiente = findViewById(R.id.btnSiguienteAuth);

        btnBack.setOnClickListener(v -> finish());

        btnSiguiente.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando prueba de autenticación...", Toast.LENGTH_SHORT).show();
        });
    }
}