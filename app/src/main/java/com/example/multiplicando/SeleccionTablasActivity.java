package com.example.multiplicando;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;


import java.util.ArrayList;
import java.util.List;

public class SeleccionTablasActivity extends AppCompatActivity {

    private CheckBox checkSeleccionarTodas;
    private CheckBox checkTabla1, checkTabla2, checkTabla3, checkTabla4, checkTabla5;
    private CheckBox checkTabla6, checkTabla7, checkTabla8, checkTabla9, checkTabla10;
    private LottieAnimationView lottieConfeti;
    private List<String> tablasSeleccionadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_tabla);



        // Inicializar los CheckBoxes
        checkSeleccionarTodas = findViewById(R.id.checkSeleccionarTodas);
        checkTabla1 = findViewById(R.id.checkTabla1);
        checkTabla2 = findViewById(R.id.checkTabla2);
        checkTabla3 = findViewById(R.id.checkTabla3);
        checkTabla4 = findViewById(R.id.checkTabla4);
        checkTabla5 = findViewById(R.id.checkTabla5);
        checkTabla6 = findViewById(R.id.checkTabla6);
        checkTabla7 = findViewById(R.id.checkTabla7);
        checkTabla8 = findViewById(R.id.checkTabla8);
        checkTabla9 = findViewById(R.id.checkTabla9);
        checkTabla10 = findViewById(R.id.checkTabla10);

        // Inicializar la animación Lottie
        lottieConfeti = findViewById(R.id.confetiContainer);
        lottieConfeti.setAnimation("confeti.json");
        lottieConfeti.setVisibility(View.GONE);


        // Agrega un listener para el CheckBox "Seleccionar todas"
        checkSeleccionarTodas.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkTabla1.setChecked(isChecked);
            checkTabla2.setChecked(isChecked);
            checkTabla3.setChecked(isChecked);
            checkTabla4.setChecked(isChecked);
            checkTabla5.setChecked(isChecked);
            checkTabla6.setChecked(isChecked);
            checkTabla7.setChecked(isChecked);
            checkTabla8.setChecked(isChecked);
            checkTabla9.setChecked(isChecked);
            checkTabla10.setChecked(isChecked);
        });
    }

    public void onSeleccionarClicked(View view) {
        tablasSeleccionadas = new ArrayList<>();

        if (checkTabla1.isChecked()) tablasSeleccionadas.add("1");
        if (checkTabla2.isChecked()) tablasSeleccionadas.add("2");
        if (checkTabla3.isChecked()) tablasSeleccionadas.add("3");
        if (checkTabla4.isChecked()) tablasSeleccionadas.add("4");
        if (checkTabla5.isChecked()) tablasSeleccionadas.add("5");
        if (checkTabla6.isChecked()) tablasSeleccionadas.add("6");
        if (checkTabla7.isChecked()) tablasSeleccionadas.add("7");
        if (checkTabla8.isChecked()) tablasSeleccionadas.add("8");
        if (checkTabla9.isChecked()) tablasSeleccionadas.add("9");
        if (checkTabla10.isChecked()) tablasSeleccionadas.add("10");

        if (!tablasSeleccionadas.isEmpty()) {
            lottieConfeti.setVisibility(View.VISIBLE);
            lottieConfeti.playAnimation();

            lottieConfeti.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Convertir las tablas seleccionadas a enteros y pasarlas
                    ArrayList<Integer> tablasEnteros = new ArrayList<>();
                    for (String tabla : tablasSeleccionadas) {
                        tablasEnteros.add(Integer.parseInt(tabla));
                    }
                    // Detén la música antes de pasar a la siguiente actividad
                    MusicPlayer.stopMusic();

                    Intent intent = new Intent(SeleccionTablasActivity.this, JuegoActivity.class);
                    intent.putIntegerArrayListExtra("tablasSeleccionadas", tablasEnteros);
                    startActivity(intent);
                    finish(); // Cerrar actividad actual
                }
            });
        } else {
            Toast.makeText(this, "Por favor selecciona al menos una tabla", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausa la música cuando la actividad se pausa
        MusicPlayer.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reanuda la música si estaba pausada
        MusicPlayer.resumeMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene la música cuando la actividad se destruye
        MusicPlayer.stopMusic();
    }
}
