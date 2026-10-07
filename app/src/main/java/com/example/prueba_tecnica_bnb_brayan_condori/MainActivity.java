package com.example.prueba_tecnica_bnb_brayan_condori;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private EditText etCelular, etCarnet, etComplemento;
    private TextView tvErrorCelular, tvErrorCarnet, tvErrorComplemento;
    private CheckBox cbTieneComplemento;
    private View layoutComplemento;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etCelular = findViewById(R.id.etCelular);
        etCarnet = findViewById(R.id.etCarnet);
        etComplemento = findViewById(R.id.etComplemento);

        tvErrorCelular = findViewById(R.id.tvErrorCelular);
        tvErrorCarnet = findViewById(R.id.tvErrorCarnet);
        tvErrorComplemento = findViewById(R.id.tvErrorComplemento);

        cbTieneComplemento = findViewById(R.id.cbTieneComplemento);
        layoutComplemento = findViewById(R.id.layoutComplemento);
        btnSiguiente = findViewById(R.id.btnSiguiente);
    }

    private void setupListeners() {
        cbTieneComplemento.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutComplemento.setVisibility(View.VISIBLE);
            } else {
                layoutComplemento.setVisibility(View.GONE);
                etComplemento.setText("");
                tvErrorComplemento.setVisibility(View.GONE);
            }
        });

        etCelular.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvErrorCelular.setVisibility(View.GONE);
            }
        });

        etCarnet.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvErrorCarnet.setVisibility(View.GONE);
            }
        });

        etComplemento.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvErrorComplemento.setVisibility(View.GONE);
            }
        });

        btnSiguiente.setOnClickListener(v -> validarYContinuar());
    }

    private void validarYContinuar() {
        boolean esValido = true;

        String celular = etCelular.getText().toString().trim();
        String carnet = etCarnet.getText().toString().trim();
        String complemento = etComplemento.getText().toString().trim();

        // 1. Validar Celular (Máximo 8 caracteres numéricos)
        if (celular.isEmpty()) {
            tvErrorCelular.setText("El número de celular es requerido");
            tvErrorCelular.setVisibility(View.VISIBLE);
            esValido = false;
        } else if (!celular.matches("^[0-9]{1,8}$")) {
            tvErrorCelular.setText("Debe contener hasta 8 dígitos numéricos");
            tvErrorCelular.setVisibility(View.VISIBLE);
            esValido = false;
        }

        // 2. Validar Carnet (Máximo 10 caracteres numéricos)
        if (carnet.isEmpty()) {
            tvErrorCarnet.setText("El número de carnet es requerido");
            tvErrorCarnet.setVisibility(View.VISIBLE);
            esValido = false;
        } else if (!carnet.matches("^[0-9]{1,10}$")) {
            tvErrorCarnet.setText("Debe contener hasta 10 dígitos numéricos");
            tvErrorCarnet.setVisibility(View.VISIBLE);
            esValido = false;
        }

        // 3. Validar Complemento si está activo (2 caracteres letras y números)
        if (cbTieneComplemento.isChecked()) {
            if (complemento.isEmpty()) {
                tvErrorComplemento.setText("Ingresa el complemento");
                tvErrorComplemento.setVisibility(View.VISIBLE);
                esValido = false;
            } else if (!complemento.matches("^[a-zA-Z0-9]{1,2}$")) {
                tvErrorComplemento.setText("Máximo 2 caracteres (letras y números sin especiales)");
                tvErrorComplemento.setVisibility(View.VISIBLE);
                esValido = false;
            }
        }

        if (esValido) {
            verificarPermisoUbicacion();
        }
    }

    private void verificarPermisoUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            verificarGpsHabilitado();
        } else {
            mostrarDialogoActivaUbicacion();
        }
    }

    private void mostrarDialogoActivaUbicacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_location_prompt, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        Button btnActivar = dialogView.findViewById(R.id.btnActivarUbicacion);
        TextView btnCancelar = dialogView.findViewById(R.id.btnCancelarUbicacion);

        btnActivar.setOnClickListener(v -> {
            dialog.dismiss();
            solicitarPermisoUbicacion();
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void solicitarPermisoUbicacion() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
                verificarGpsHabilitado();
            } else {
                Toast.makeText(this, "Es necesario habilitar la ubicación para continuar", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void verificarGpsHabilitado() {
        consumirServicioYContinuar();
    }

    private void consumirServicioYContinuar() {
        Toast.makeText(this, "Validación exitosa. Consumiendo servicio...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, AutenticacionActivity.class);
        startActivity(intent);
    }

    abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    }
}