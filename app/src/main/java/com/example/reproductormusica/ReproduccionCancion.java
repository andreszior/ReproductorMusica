package com.example.reproductormusica;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReproduccionCancion extends AppCompatActivity {


    File cancion;
    Uri uri = null;
    MediaPlayer mp;
    private MediaObserver observer = null;

    ImageView retroLista;
    ImageView imagenAlbum;
    ProgressBar barraProgreso;
    TextView tempoInicial;
    TextView tempoFinal;
    ImageView retrocedersec;
    ImageView cancionPrevia;
    ImageView play;
    ImageView pausa;
    ImageView stopCircle;
    ImageView cancionSiguiente;
    ImageView adelantarsec;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproduccion_cancion);
        String rutaCancion = getIntent().getStringExtra("cancion_path");
        try{
            iniciarCancion(rutaCancion);
        }catch (Exception e){
            e.printStackTrace();
        }

        cargarComponentes();


    }

    private void cargarComponentes(){

        retroLista = findViewById(R.id.retrocederALista);

        imagenAlbum = findViewById(R.id.imagenAlbum);

        //Acciones de la barra
        barraProgreso = findViewById(R.id.progressBar);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                observer.stop();
                barraProgreso.setProgress(mediaPlayer.getCurrentPosition());
                mp.stop();
                mp.reset();
            }
        });

        tempoInicial = findViewById(R.id.tempoInicio);

        tempoFinal = findViewById(R.id.tempoFinal);

        retrocedersec = findViewById(R.id.replay10);

        cancionPrevia = findViewById(R.id.previousSong);


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
                mp.pause();
                pausa.setVisibility(View.INVISIBLE);
            }
        });

        stopCircle = findViewById(R.id.stopCircle);

        cancionSiguiente = findViewById(R.id.forwardSong);

        adelantarsec = findViewById(R.id.forward10);

        observer = new MediaObserver();
        new Thread(observer).start();

    }

    private void iniciarCancion(String ruta){
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mp.setDataSource(ruta);
            mp.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
        mp.start();
    }

    private void reproducirCancion(){
        //mp = MediaPlayer.create();
        mp.start();
    }
    private void PausarCancion(){

    }

    private class MediaObserver implements Runnable{

        private AtomicBoolean stop = new AtomicBoolean(false);
        public void stop(){
            stop.set(true);
        }
        @Override
        public void run() {
            while(!stop.get()){
                barraProgreso.setProgress((int) ((double) mp.getCurrentPosition() / (double)
                mp.getDuration() * 100));
                try{
                    Thread.sleep(200);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
    }

    private void adelantarCancion(){

    }
}