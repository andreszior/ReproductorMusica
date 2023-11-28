package com.example.reproductormusica;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListarCanciones extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    File directorio;
    List<ClaseCanciones> canciones;
    RecyclerView recyclerView;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ListarCanciones.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            Toast.makeText(this, "Los permisos fueron denegados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Los permisos fueron aceptados", Toast.LENGTH_SHORT).show();
            cargarFicheros();
            //obtenerCanciones();
        }


        //startService(new Intent(this, ObtenerCancion.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // Verificar si se han concedido los permisos
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos, continuar con la lógica de la
                Toast.makeText(this, "Los permisos fueron aceptados", Toast.LENGTH_SHORT).show();
                cargarFicheros();
                //obtenerCanciones();
            } else {
                // Los permisos fueron denegados, mostrar un mensaje o realizar
                Toast.makeText(this, "Los permisos fueron denegados.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
/*
    private void comprobarPedirPermisos() {
        List<String> missingPermissions = new ArrayList<>();
        for (String permission : PERMISSIONS_STORAGE) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            String[] permissionsArray = missingPermissions.toArray(new String[0]);
            requestPermissionLauncher.launch(permissionsArray);
        } else {
            cargarFicheros();
        }
    }

 */

    private void cargarFicheros(){
        recyclerView = findViewById(R.id.listaCanciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Adaptador adapter = new Adaptador(canciones, this);
        //recyclerView.setAdapter(adapter);

    }

    private List<ClaseCanciones> obtenerCanciones(){
        List<ClaseCanciones> lista = new ArrayList<>();
        String carpetaDownloads = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

        Uri uriCarpetaDownloads = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uriCarpetaDownloads = MediaStore.Downloads.getContentUri("external");
        }

        //Obtener los archivos de audio
        String[] infoCancion = {
                MediaStore.Downloads._ID,
                MediaStore.Downloads.TITLE,
                MediaStore.Downloads.ALBUM_ARTIST,
                MediaStore.Downloads.ALBUM,
                //MediaStore.Downloads.
        };

        String selection = MediaStore.Downloads.MIME_TYPE + "=?";

        String[] selectionArgs = {"audio/*"};

        try (Cursor cursor = getContentResolver().query(
                uriCarpetaDownloads,
                infoCancion,
                selection,
                selectionArgs,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndex(MediaStore.Downloads._ID);
                int dataColumn = cursor.getColumnIndex(MediaStore.Downloads.DATA);
                int titleColumn = cursor.getColumnIndex(MediaStore.Downloads.TITLE);
                int artistColumn = cursor.getColumnIndex(MediaStore.Downloads.ARTIST);
                int albumColumn = cursor.getColumnIndex(MediaStore.Downloads.ALBUM);

                do {
                    long id = cursor.getLong(idColumn);
                    String path = cursor.getString(dataColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    String album = cursor.getString(albumColumn);

                    // Obtener metadatos de la canción
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(path);

                    // Obtener imagen del álbum (si está disponible)
                    byte[] imagenAlbum = retriever.getEmbeddedPicture();

                    // Crear objeto ClaseCanciones y agregar a la lista
                    ClaseCanciones cancion = new ClaseCanciones(id, Uri.parse(path), title, artist, album, imagenAlbum);
                    lista.add(cancion);

                    retriever.release();
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Error", "Error al obtener canciones desde Downloads: " + e.getMessage());
        }
        return lista;
     }

/*
    private void cargarFicheros(){
        directorio = new File(Environment.getExternalStorageDirectory().getParent()+"/Download");
        if(directorio.exists()){
            ficheros = directorio.listFiles();
            if(ficheros != null){
                ClaseCanciones[] canciones = new ClaseCanciones[ficheros.length];
                for (int i = 0; i < fields.length; i++){
                    canciones[i] = new ClaseCanciones(i, ficheros[i], " ");
                }
                // pasar al adaptador
                Adaptador adaptador = new Adaptador(canciones, this);
                ListView lista = findViewById(R.id.listaCanciones);
                lista.setAdapter(adaptador);
            }
        }
    }
    package com.example.myreproductor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreproductor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListarCanciones extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    File directorio;
    RecyclerView recyclerView;
    ActivityResultLauncher<String> requestPermissionLauncher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ListarCanciones.this, permissions, REQUEST_EXTERNAL_STORAGE);
            Toast.makeText(this, "Los permisos fueron denegados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Los permisos fueron denegados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // Verificar si se han concedido los permisos
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos, continuar con la lógica de la
                Toast.makeText(this, "Los permisos fueron aceptados", Toast.LENGTH_SHORT).show();
            } else {
                // Los permisos fueron denegados, mostrar un mensaje o realizar
                Toast.makeText(this, "Los permisos fueron denegados.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


 */
}