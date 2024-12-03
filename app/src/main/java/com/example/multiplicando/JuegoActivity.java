package com.example.multiplicando;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    private TextView preguntaTextView, timerTextView, scoreTextView;
    private Button opcion1Button, opcion2Button, opcion3Button, opcion4Button;
    private RelativeLayout juegoLayout;

    private ImageView[] estrellas;
    private int respuestasCorrectas = 0;

    private List<Integer> tablasSeleccionadas;
    private int nivelActual = 1;
    private int respuestasCorrectasConsecutivas = 0;
    private int aciertos = 0;
    private int fallos = 0;
    private int tiempoRestante = 60;
    private TextView nivelTextView; // Declaración global

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_juego);

        // Recibir el nivel actual desde el Intent
        nivelActual = getIntent().getIntExtra("nivelActual", 1);
        aciertos = getIntent().getIntExtra("aciertos", 0);
        fallos = getIntent().getIntExtra("fallos", 0);

        // Recibir las tablas seleccionadas desde la actividad anterior
        tablasSeleccionadas = getIntent().getIntegerArrayListExtra("tablasSeleccionadas");

        // Validar que tablas seleccionadas no sean nulas o vacías
        if (tablasSeleccionadas == null || tablasSeleccionadas.isEmpty()) {
            Toast.makeText(this, "Error: No se seleccionaron tablas para practicar.", Toast.LENGTH_LONG).show();
            finish(); // Finaliza la actividad si no hay tablas seleccionadas
            return;
        }

        // Inicializar vistas
        inicializarVistas();

        // Iniciar el juego
        iniciarNivel();
    }

    private void inicializarVistas() {
        juegoLayout = findViewById(R.id.juegoLayout);
        preguntaTextView = findViewById(R.id.preguntaTextView);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        opcion1Button = findViewById(R.id.opcion1Button);
        opcion2Button = findViewById(R.id.opcion2Button);
        opcion3Button = findViewById(R.id.opcion3Button);
        opcion4Button = findViewById(R.id.opcion4Button);


        // Inicializar el TextView del nivel actual
        nivelTextView = findViewById(R.id.nivelTextView);
        nivelTextView.setText(String.format("Nivel: %d", nivelActual));



        // Inicializar las estrellas
        estrellas = new ImageView[5];
        estrellas[0] = findViewById(R.id.estrella1);
        estrellas[1] = findViewById(R.id.estrella2);
        estrellas[2] = findViewById(R.id.estrella3);
        estrellas[3] = findViewById(R.id.estrella4);
        estrellas[4] = findViewById(R.id.estrella5);
    }
    //iniciamos nivel cada vez que lo llamemos
    private void iniciarNivel() {
        if (nivelActual >= 5) {
            terminarJuego();
            return;
        }
        reiniciarEstrellas();
        cambiarFondoNivel();
        // Ocultar contenedor de estrellas en nivel 4
        View progresoLayout = findViewById(R.id.progresoLayout);
        if (nivelActual == 4) {
            progresoLayout.setVisibility(View.GONE); // Ocultar en nivel 4
        } else {
            progresoLayout.setVisibility(View.VISIBLE); // Mostrar en otros niveles
        }

        configurarPreguntas();
        //configuramos el tiempo según los niveles
        int tiempo = nivelActual == 4 ? 30 : 30;
        iniciarTemporizador(tiempo);
    }


    private void cambiarFondoNivel() {
        int fondoDrawable;
        switch (nivelActual) {
            case 1:
                fondoDrawable = R.drawable.f1;
                break;
            case 2:
                fondoDrawable = R.drawable.f2;
                break;
            case 3:
                fondoDrawable = R.drawable.f3;
                break;
            case 4:
                fondoDrawable = R.drawable.f4;
                break;
            default:
                fondoDrawable = R.drawable.f5;
                break;
        }
        juegoLayout.setBackgroundResource(fondoDrawable);
    }

    private void configurarPreguntas() {

        Random random = new Random();
        if (tablasSeleccionadas == null || tablasSeleccionadas.isEmpty()) {
            Toast.makeText(this, "Error: No se seleccionaron tablas para practicar.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Generar los números para la pregunta
        int numero1 = tablasSeleccionadas.get(random.nextInt(tablasSeleccionadas.size()));
        int numero2 = random.nextInt(10) + 1;
        final int respuestaCorrecta = numero1 * numero2;

        // Crear las opciones de respuesta
        List<Integer> opciones = new ArrayList<>();
        opciones.add(respuestaCorrecta);
        //establece el numero fijo de 4 opciones para nivel 4 y creciente para el resto
        int numOpciones = nivelActual == 4 ? 4 : nivelActual + 1;

        // Generar respuestas incorrectas sin repetir la respuesta correcta
        while (opciones.size() < numOpciones) {
            int incorrecta = generarRespuestaIncorrecta(random, respuestaCorrecta, numero1);
        //descartamos opciones incorrectas repetidas
            if (!opciones.contains(incorrecta)) {
                opciones.add(incorrecta);
            }
        }

        // Mezclar opciones
        Collections.shuffle(opciones);

        // Validar que la respuesta correcta esté incluida
        if (!opciones.contains(respuestaCorrecta)) {
            opciones.set(random.nextInt(opciones.size()), respuestaCorrecta);
        }

        // Mostrar la pregunta
        preguntaTextView.setText(String.format("%d x %d = ?", numero1, numero2));

        // Asignar las opciones a los botones
        asignarOpcionesABotones(opciones);

        // Guardar la respuesta correcta como una etiqueta para la verificación
        preguntaTextView.setTag(respuestaCorrecta);
    }

    private int generarRespuestaIncorrecta(Random random, int respuestaCorrecta, int numero1) {
        // Generar una respuesta incorrecta razonable
        int incorrecta;
        do {
            incorrecta = random.nextInt(10 * numero1) + 1;
        } while (incorrecta == respuestaCorrecta); // Asegurarse de que no sea igual a la correcta
        return incorrecta;
    }
    private void asignarOpcionesABotones(List<Integer> opciones) {

        restaurarFondoBoton(opcion1Button);
        restaurarFondoBoton(opcion2Button);
        restaurarFondoBoton(opcion3Button);
        restaurarFondoBoton(opcion4Button);

        opcion1Button.setText(String.valueOf(opciones.get(0)));
        opcion1Button.setVisibility(View.VISIBLE);
        opcion2Button.setText(String.valueOf(opciones.get(1)));
        opcion2Button.setVisibility(View.VISIBLE);

        if (opciones.size() > 2) {
            opcion3Button.setText(String.valueOf(opciones.get(2)));
            opcion3Button.setVisibility(View.VISIBLE);
        } else {
            opcion3Button.setVisibility(View.GONE);
        }

        if (opciones.size() > 3) {
            opcion4Button.setText(String.valueOf(opciones.get(3)));
            opcion4Button.setVisibility(View.VISIBLE);
        } else {
            opcion4Button.setVisibility(View.GONE);
        }

        // Configurar el evento de clic para los botones
        View.OnClickListener clickListener = v -> verificarRespuesta(
                ((Button) v).getText().toString(),
                Integer.parseInt(preguntaTextView.getTag().toString())
        );
        opcion1Button.setOnClickListener(clickListener);
        opcion2Button.setOnClickListener(clickListener);
        if (opciones.size() > 2) opcion3Button.setOnClickListener(clickListener);
        if (opciones.size() > 3) opcion4Button.setOnClickListener(clickListener);
    }




    private void verificarRespuesta(String seleccion, int respuestaCorrecta) {
        int respuestaSeleccionada = Integer.parseInt(seleccion);

        if (respuestaSeleccionada == respuestaCorrecta) {
            aciertos++;
            scoreTextView.setText(String.format("Aciertos: %d | Fallos: %d", aciertos, fallos));

            MusicPlayer.startMusic(this, R.raw.si);

            if (nivelActual < 4) {
                respuestasCorrectas++;
                if (respuestasCorrectas <= 5) {
                    estrellas[respuestasCorrectas - 1].setImageResource(R.drawable.estrella);
                }

                if (respuestasCorrectas == 5) {
                    nivelActual++;
                    respuestasCorrectas = 0;

                    Intent intent = new Intent(JuegoActivity.this, NivelSuperadoActivity.class);
                    intent.putExtra("nivelActual", nivelActual);
                    intent.putExtra("aciertos", aciertos);
                    intent.putExtra("fallos", fallos);
                    intent.putIntegerArrayListExtra("tablasSeleccionadas", new ArrayList<>(tablasSeleccionadas));
                    startActivity(intent);
                    finish();


            } else {
                    configurarPreguntas();
                }
            } else {
                // En el nivel 4, seguimos mostrando preguntas hasta que el temporizador termine.
                configurarPreguntas();
            }
        } else {
            fallos++;
            respuestasCorrectasConsecutivas = 0;
            scoreTextView.setText(String.format("Aciertos: %d | Fallos: %d", aciertos, fallos));

            MusicPlayer.startMusic(this, R.raw.no);

            iluminarRespuestaCorrecta(respuestaCorrecta);

            new Handler(Looper.getMainLooper()).postDelayed(this::configurarPreguntas, 1000);
        }
    }


    private void reiniciarEstrellas() {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setImageResource(R.drawable.estrellasin);
        }
        respuestasCorrectas = 0;
    }

    private void iluminarRespuestaCorrecta(int respuestaCorrecta) {
        Button botonCorrecto;

        // Identificar el botón correspondiente a la respuesta correcta
        if (opcion1Button.getText().toString().equals(String.valueOf(respuestaCorrecta))) {
            botonCorrecto = opcion1Button;
        } else if (opcion2Button.getText().toString().equals(String.valueOf(respuestaCorrecta))) {
            botonCorrecto = opcion2Button;
        } else if (opcion3Button.getVisibility() == View.VISIBLE &&
                opcion3Button.getText().toString().equals(String.valueOf(respuestaCorrecta))) {
            botonCorrecto = opcion3Button;
        } else if (opcion4Button.getVisibility() == View.VISIBLE &&
                opcion4Button.getText().toString().equals(String.valueOf(respuestaCorrecta))) {
            botonCorrecto = opcion4Button;
        } else {
            botonCorrecto = null;
        }

        if (botonCorrecto != null) {
            // Guardar el fondo y el tinte originales si aún no se han guardado
            if (botonCorrecto.getTag(R.id.boton_fondo_original) == null) {
                botonCorrecto.setTag(R.id.boton_fondo_original, botonCorrecto.getBackground());
            }
            if (botonCorrecto.getTag(R.id.boton_tinte_original) == null) {
                botonCorrecto.setTag(R.id.boton_tinte_original, botonCorrecto.getBackgroundTintList());
            }

            // Cambiar el fondo del botón a verde usando ColorStateList
            botonCorrecto.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light, getTheme()));

            // Agrandar el botón (efecto de resalte)
            botonCorrecto.animate().scaleX(1.2f).scaleY(1.2f).setDuration(600).withEndAction(() -> {
                // Restaurar el tamaño, el fondo y el tinte originales
                botonCorrecto.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).withEndAction(() -> {
                    // Restaurar el fondo original
                    Drawable fondoOriginal = (Drawable) botonCorrecto.getTag(R.id.boton_fondo_original);
                    if (fondoOriginal != null) {
                        botonCorrecto.setBackground(fondoOriginal);
                    }
                    // Restaurar el tinte original
                    ColorStateList tinteOriginal = (ColorStateList) botonCorrecto.getTag(R.id.boton_tinte_original);
                    if (tinteOriginal != null) {
                        botonCorrecto.setBackgroundTintList(tinteOriginal);
                    }
                });
            });
        }
    }


    private void restaurarFondoBoton(Button boton) {
        if (boton.getTag(R.id.boton_fondo_original) != null) {
            Drawable fondoOriginal = (Drawable) boton.getTag(R.id.boton_fondo_original);
            boton.setBackground(fondoOriginal);
        } else {
            // Si no se ha guardado un fondo original, usa un fondo predeterminado
            boton.setBackgroundResource(android.R.drawable.btn_default);
        }
    }




    private void iniciarTemporizador(int segundos) {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(segundos * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = (int) (millisUntilFinished / 1000);
                timerTextView.setText(String.format("Tiempo: %ds", tiempoRestante));
            }

            @Override
            public void onFinish() {
                terminarJuego(); // Finaliza el juego y redirige al reporte
            }
        };
        timer.start();
    }



    private void terminarJuego() {
        if (timer != null) timer.cancel();
        Intent intent = new Intent(JuegoActivity.this, ReporteActivity.class);
        intent.putExtra("aciertos", aciertos);
        intent.putExtra("fallos", fallos);
        intent.putExtra("nivelMaximo", nivelActual);
        startActivity(intent);
        finish();
    }

    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}