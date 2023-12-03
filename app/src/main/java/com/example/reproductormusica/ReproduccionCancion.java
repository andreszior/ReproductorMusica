package com.example.reproductormusica;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReproduccionCancion extends AppCompatActivity {


    MediaPlayer mp;
    private MediaObserver observer = null;

    ImageView retroLista;
    ImageView imagenAlbum;
    SeekBar barraProgreso;
    TextView tempoInicial;
    TextView tempoFinal;
    ImageView retrocedersec;
    ImageView cancionPrevia;
    ImageView play;
    ImageView pausa;
    ImageView stopCircle;
    ImageView cancionSiguiente;
    ImageView adelantarsec;

    Handler handler;
    Runnable runnable;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproduccion_cancion);

        long idCancion = getIntent().getLongExtra("id_cancion", 1);
        ClaseCancion[] lista = Dataholder.getInstance().canciones;
        int cancionPosicion = getIntent().getIntExtra("posicion_cancion", 1);
        observer = new MediaObserver();

        cargarComponentes(lista, cancionPosicion);
        handler = new Handler();

        try{
            iniciarCancion(lista, cancionPosicion);
            new Thread(observer).start();
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    private void cargarComponentes(ClaseCancion[] lista, int pos){

        retroLista = findViewById(R.id.retrocederALista);

        imagenAlbum = findViewById(R.id.imagenAlbum);



        //Acciones de la barra
        barraProgreso = findViewById(R.id.progressBar);

        if(mp != null){
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    observer.stop();
                    //barraProgreso.setProgress(mediaPlayer.getCurrentPosition());
                    mp.stop();
                    mp.reset();
                }
            });
        }

        tempoInicial = findViewById(R.id.tempoInicio);

        tempoFinal = findViewById(R.id.tempoFinal);

        retrocedersec = findViewById(R.id.replay10);
        retrocedersec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retroceder();
            }
        });


        cancionPrevia = findViewById(R.id.previousSong);
        cancionPrevia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                anteriorCancion(lista, pos);
            }
        });


        //Acciones del boton Play
        play = findViewById(R.id.playCircle);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausa.setVisibility(View.VISIBLE);
                mp.start();
                play.setVisibility(View.INVISIBLE);
            }
        });

        //Acciones del boton Pausa
        pausa = findViewById(R.id.pauseCircle);
        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.VISIBLE);
                pausarCancion();
                pausa.setVisibility(View.INVISIBLE);
            }
        });

        stopCircle = findViewById(R.id.stopCircle);
        stopCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                    if (mp != null) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                    if (observer != null) {
                        observer.stop();
                    }
                }
        });

        cancionSiguiente = findViewById(R.id.forwardSong);
        cancionSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                siguienteCancion(lista, pos);
            }
        });

        adelantarsec = findViewById(R.id.forward10);
        adelantarsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adelantar();
            }
        });

    }

    private void iniciarCancion(ClaseCancion[] lista, int pos){
        mp = new MediaPlayer();
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mp.setDataSource(lista[pos].cancion.getAbsolutePath());
            mp.prepare();
            barraProgreso.setMax(mp.getDuration());
        }catch (IOException e){
            e.printStackTrace();
        }
        mp.start();
        updateSeekbar();
    }

    private void pausarCancion(){
        if(mp.isPlaying()){
            mp.pause();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
    }

    private void siguienteCancion(ClaseCancion[] lista, int posicion){
        iniciarCancion(lista, posicion+1);
    }

    private void anteriorCancion(ClaseCancion[] lista, int posicion){
        iniciarCancion(lista, posicion-1);
    }

    private void adelantar() {
        int posicion = mp.getCurrentPosition();
        posicion = Math.min(posicion + 10000, mp.getDuration()); // Adelanta 10 segundos
        mp.seekTo(posicion);
    }

    private void retroceder() {
        int posicion = mp.getCurrentPosition();
        posicion = Math.max(posicion - 10000, 0); // Retrocede 10 segundos
        mp.seekTo(posicion);
    }


    private class MediaObserver implements Runnable{

        private AtomicBoolean stop = new AtomicBoolean(false);
        public void stop(){
            stop.set(true);
        }
        @Override
        public void run() {
            while (!stop.get()) {
                if (mp != null) {
                    final int currentPosition = mp.getCurrentPosition();
                    final int duration = mp.getDuration();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updateSeekbar();
                            // Actualiza tempoInicial
                            @SuppressLint("DefaultLocale") String tiempoActual = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                                    TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))
                            );
                            tempoInicial.setText(tiempoActual);

                            // Actualiza tempoFinal
                            @SuppressLint("DefaultLocale") String tiempoFinal = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(duration),
                                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                            );
                            tempoFinal.setText(tiempoFinal);
                        }
                    });
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateSeekbar(){
        int currPos = mp.getCurrentPosition();
        barraProgreso.setProgress(currPos);
        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeekbar();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

}