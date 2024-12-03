package com.example.multiplicando;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity {
    private LottieAnimationView lottieBravo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        // Reproducir música de nivel superado
        MusicPlayer.startMusic(this, R.raw.genio);
        // Obtener referencias a las vistas
        TextView totalAciertosTextView = findViewById(R.id.aciertosTextView);
        TextView totalFallosTextView = findViewById(R.id.fallosTextView);
        TextView diferenciaTextView = findViewById(R.id.diferenciaTextView);
        TextView nivelAlcanzadoTextView = findViewById(R.id.nivelAlcanzadoTextView);
        Button btnReiniciar = findViewById(R.id.btnReiniciar);
        Button btnSalir = findViewById(R.id.btnSalir);

        // Obtener los datos pasados desde `JuegoActivity`
        ArrayList<Integer> aciertosPorNivel = getIntent().getIntegerArrayListExtra("aciertosPorNivel");
        ArrayList<Integer> fallosPorNivel = getIntent().getIntegerArrayListExtra("fallosPorNivel");
        int nivelMaximo = getIntent().getIntExtra("nivelMaximo", 1);

        // Calcular totales y diferencia
        int totalAciertos = getIntent().getIntExtra("aciertos", 0);
        int totalFallos = getIntent().getIntExtra("fallos", 0);
        int diferencia = totalAciertos - totalFallos;

        // Mostrar los resultados en las vistas
        totalAciertosTextView.setText(String.format("Total de Aciertos: %d", totalAciertos));
        totalFallosTextView.setText(String.format("Total de Fallos: %d", totalFallos));
        diferenciaTextView.setText(String.format("Diferencia: %d", diferencia));
        nivelAlcanzadoTextView.setText(String.format("Nivel alcanzado: %d", nivelMaximo));

        // Llamar al método para mostrar el ranking
        mostrarRanking(totalAciertos);

        // Inicializar la animación Lottie
        lottieBravo = findViewById(R.id.lottieAnimationView);
        lottieBravo.setAnimation("fin.json");
        lottieBravo.setVisibility(View.VISIBLE);
        lottieBravo.setRepeatCount(LottieDrawable.INFINITE);
        lottieBravo.playAnimation();


        // Configurar el botón "Reiniciar"
        btnReiniciar.setOnClickListener(v -> {
            lottieBravo.cancelAnimation();
            // Cambiar a la actividad de portada
            Intent intent = new Intent(ReporteActivity.this, PortadaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        // Configurar el botón "Salir"
        lottieBravo.cancelAnimation();
        btnSalir.setOnClickListener(v -> finishAffinity()); // Cierra toda la aplicación
    }


    // Método mostrarRanking
    private void mostrarRanking(int aciertos) {
        ImageView[] estrellas = {
                findViewById(R.id.estrella1), findViewById(R.id.estrella2),
                findViewById(R.id.estrella3), findViewById(R.id.estrella4),
                findViewById(R.id.estrella5), findViewById(R.id.estrella6),
                findViewById(R.id.estrella7), findViewById(R.id.estrella8),
                findViewById(R.id.estrella9), findViewById(R.id.estrella10)
        };
        TextView mensajeTextView = findViewById(R.id.mensajeTextView);

        int estrellasIluminadas;
        String mensaje;

        // Nueva lógica de clasificación
        if (aciertos < 7) {
            estrellasIluminadas = 2;
            mensaje = "¡Puedes hacerlo mucho mejor, prueba otra vez!";
        } else if (aciertos >= 7 && aciertos <= 12) {
            estrellasIluminadas = 3;
            mensaje = "¡No está mal! Pero puedes mejorar, sigue intentándolo.";
        } else if (aciertos >= 13 && aciertos <= 19) {
            estrellasIluminadas = 4;
            mensaje = "¡Vas por buen camino! Sigue así y lo lograrás.";
        } else if (aciertos >= 20 && aciertos <= 24) {
            estrellasIluminadas = 5;
            mensaje = "¡Muy bien! Tu esfuerzo está dando frutos.";
        } else if (aciertos >= 25 && aciertos <= 27) {
            estrellasIluminadas = 7;
            mensaje = "¡Increíble! ¡Lo estás haciendo genial, casi perfecto!";
        } else if (aciertos >= 28 && aciertos <= 31) {
            estrellasIluminadas = 9;
            mensaje = "¡Impresionante! Estás dominando las tablas de multiplicar.";
        } else if (aciertos >= 32) {
            estrellasIluminadas = 10;
            mensaje = "¡Perfecto! Eres una estrella de las matemáticas.";
        } else {
            estrellasIluminadas = 0;
            mensaje = "¡Inténtalo de nuevo! Puedes mejorar."; // Caso por si los aciertos no encajan
        }

        // Asegurarse de que todas las estrellas estén vacías al principio
        for (ImageView estrella : estrellas) {
            estrella.setImageResource(R.drawable.estrellasin); // Comienzan vacías
        }

        // Animar las estrellas
        animarEstrellas(estrellas, estrellasIluminadas);

        // Actualizar mensaje
        mensajeTextView.setText(mensaje);
    }


    // Animar las estrellas progresivamente
    private void animarEstrellas(ImageView[] estrellas, int estrellasIluminadas) {
        Handler handler = new Handler();

        // Recorremos cada estrella que debe iluminarse
        for (int i = 0; i < estrellasIluminadas; i++) {
            final int index = i;

            handler.postDelayed(() -> {
                estrellas[index].setImageResource(R.drawable.estrella); // Cambiar a iluminada
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.si);
                mediaPlayer.start();
            }, index * 500); // El retraso aumenta con cada estrella
        }

        // Reproducir la animación Lottie después de animar todas las estrellas
        handler.postDelayed(() -> {
            if (lottieBravo != null) {
                lottieBravo.playAnimation();
            }
        }, estrellasIluminadas * 500); // Retraso acumulado basado en las estrellas
    }

    protected void onDestroy() {
        super.onDestroy();
        // Detener la música al salir de esta actividad
        MusicPlayer.stopMusic();
    }
}