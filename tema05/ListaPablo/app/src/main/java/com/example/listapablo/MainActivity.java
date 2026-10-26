package com.example.listapablo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        ArrayList<Videojuego> videojuegos = new ArrayList<>();
        videojuegos.add(new Videojuego("Zelda: Breath of the Wild", "Nintendo Switch", R.drawable.icono1));
        videojuegos.add(new Videojuego("Minecraft", "PC", R.drawable.icono3));
        videojuegos.add(new Videojuego("Clash Royale", "Móvil", R.drawable.icono2));
        videojuegos.add(new Videojuego("Eafc 27", "PlayStation 5", R.drawable.icono4));

        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setAdapter(new AdaptadorVideojuegos(videojuegos));
    }
}