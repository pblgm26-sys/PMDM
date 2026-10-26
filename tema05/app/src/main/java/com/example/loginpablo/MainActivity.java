package com.example.loginpablo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    private static final String CORREO_VALIDO = "pablo@correo.com";
    private static final String CLAVE_VALIDA = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etClave = findViewById(R.id.etClave);
        MaterialSwitch swRecordar = findViewById(R.id.swRecordar);
        Button btnContinuar = findViewById(R.id.btnContinuar);
        TextView tvMensaje = findViewById(R.id.tvMensaje);

        btnContinuar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String clave = etClave.getText().toString();

            if (correo.isEmpty()) {
                etCorreo.setError(getString(R.string.falta_correo));
                return;
            }
            if (clave.isEmpty()) {
                etClave.setError(getString(R.string.falta_clave));
                return;
            }
            if (correo.equals(CORREO_VALIDO) && clave.equals(CLAVE_VALIDA)) {
                if (swRecordar.isChecked()) {
                    tvMensaje.setText(R.string.acceso_recordado);
                } else {
                    tvMensaje.setText(R.string.acceso_correcto);
                }
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.correcto));
            } else {
                tvMensaje.setText(R.string.acceso_incorrecto);
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.incorrecto));
            }
        });
    }
}