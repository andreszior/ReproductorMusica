package com.example.reproductormusica;

import android.app.Service;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;

public class ObtenerCancion extends Service {
    private static final String MUSIC_DIRECTORY_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();;
    private FileObserver fileObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        startFileObserver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopFileObserver();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startFileObserver() {
        fileObserver = new FileObserver(MUSIC_DIRECTORY_PATH) {
            @Override
            public void onEvent(int event, String path) {
                // Este método se llama cuando hay cambios en el directorio de música
                if (event == FileObserver.CREATE) {
                    // Se ha creado un nuevo archivo
                    // Obten la ruta completa del nuevo archivo
                    String nuevoArchivo = MUSIC_DIRECTORY_PATH + File.separator + path;
                    // Realiza el escaneo de la MediaStore
                    scanMediaStore(nuevoArchivo);
                }
            }
        };
        fileObserver.startWatching();
    }

    private void stopFileObserver() {
        if (fileObserver != null) {
            fileObserver.stopWatching();
        }
    }

    private void scanMediaStore(String nuevoArchivo) {
        MediaScannerConnection.scanFile(this,
                new String[] { nuevoArchivo }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }
}