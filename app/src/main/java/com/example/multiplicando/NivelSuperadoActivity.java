package com.example.multiplicando;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.ArrayList;

public class NivelSuperadoActivity extends AppCompatActivity {
    private LottieAnimationView lottieBravo;
    private Button botonContinuar;
    private TextView etiquetaEnhorabuena;
    private TextView explicacionSiguienteNivel;
    private int nivelActual; // Nivel actual
    private int aciertosAcumulados;
    private int fallosAcumulados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nivel_superado);
        // Reproducir música de nivel superado
        MusicPlayer.startMusic(this, R.raw.nivelsuperado);
        // Recibir los datos acumulados de aciertos y fallos
        aciertosAcumulados = getIntent().getIntExtra("aciertos", 0);
        fallosAcumulados = getIntent().getIntExtra("fallos", 0);
        nivelActual = getIntent().getIntExtra("nivelActual", 1);

        // Recibir el nivel actual desde el Intent
        nivelActual = getIntent().getIntExtra("nivelActual", 1);

        // Inicializar la animación Lottie
        lottieBravo = findViewById(R.id.lottieAnimationView);
        lottieBravo.setAnimation("bravo.json");
        lottieBravo.setVisibility(View.VISIBLE);
        lottieBravo.setRepeatCount(LottieDrawable.INFINITE); // Repetir indefinidamente
        lottieBravo.playAnimation();


        // Inicializar vistas
        botonContinuar = findViewById(R.id.botonContinuar);
        etiquetaEnhorabuena = findViewById(R.id.etiquetaEnhorabuena);
        explicacionSiguienteNivel = findViewById(R.id.explicacionSiguienteNivel);

        // Configurar mensajes
        etiquetaEnhorabuena.setText("¡Enhorabuena. Vamos al siguiente nivel !");
        if (nivelActual == 4) {
            explicacionSiguienteNivel.setText("¡Ahora acierta todas las que puedas en un minuto!");
        } else {
            explicacionSiguienteNivel.setText("¡Acierta cinco preguntas y pasa al siguiente nivel!");
        }

        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottieBravo.cancelAnimation(); // Detener la animación
                // Recibir tablasSeleccionadas desde el Intent actual
                ArrayList<Integer> tablasSeleccionadas = getIntent().getIntegerArrayListExtra("tablasSeleccionadas");

                // Validar que no sean nulas o vacías
                if (tablasSeleccionadas == null || tablasSeleccionadas.isEmpty()) {
                    tablasSeleccionadas = new ArrayList<>();
                    for (int i = 1; i <= 10; i++) {
                        tablasSeleccionadas.add(i); // Por defecto, añadir todas las tablas
                    }
                }

                // Iniciar el siguiente nivel en JuegoActivity
                Intent intent = new Intent(NivelSuperadoActivity.this, JuegoActivity.class);
                intent.putExtra("nivelActual", nivelActual);
                intent.putExtra("aciertos", aciertosAcumulados); // Pasar aciertos acumulados
                intent.putExtra("fallos", fallosAcumulados);     // Pasar fallos acumulados
                intent.putIntegerArrayListExtra("tablasSeleccionadas", tablasSeleccionadas); // Pasar las tablas seleccionadas
                startActivity(intent);
                finish();
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la música al salir de esta actividad
        MusicPlayer.stopMusic();
    }
}
