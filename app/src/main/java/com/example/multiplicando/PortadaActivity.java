package com.example.multiplicando;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PortadaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portada);

        // Inicia la música
        MusicPlayer.startMusic(this, R.raw.tmportada1);

        // Encuentra el botón
        Button btnJugar = findViewById(R.id.btnJugar);

        // Carga la animación del boton para que aparezca
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.botonanimacion);

        btnJugar.setOnClickListener(v -> {
            Intent intent = new Intent(PortadaActivity.this, SeleccionTablasActivity.class);
            startActivity(intent);
        });
        // Iniciar música al entrar a la portada (solo si no está ya reproduciendo)
        MusicPlayer.startMusic(this, R.raw.tmportada1);

        // Configura el botón como visible justo antes de iniciar la animación
        btnJugar.setVisibility(View.VISIBLE);

        // Inicia la animación
        btnJugar.startAnimation(fadeIn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicPlayer.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicPlayer.resumeMusic();
    }


}
