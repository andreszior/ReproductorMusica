package com.example.reproductormusica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class ListarCanciones extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    File directorio;
    File[] ficheros;
    ClaseCancion[] canciones;
    ListView listaViewCanciones;


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

    private void cargarFicheros() {

        listaViewCanciones = findViewById(R.id.listViewCanciones);

        //Capta los ficheros del directorio
        directorio = new File(Environment.getExternalStorageDirectory().getPath() + "/Download");
        if (directorio.exists() && directorio.isDirectory()) {
            ficheros = directorio.listFiles();

            try {
                canciones = new ClaseCancion[ficheros.length];

                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

                for (int i = 0; i < ficheros.length; i++) {
                    metadataRetriever.setDataSource(ficheros[i].getAbsolutePath());
                    canciones[i] = new ClaseCancion(i , ficheros[i], ficheros[i].getAbsolutePath());
                    rellenarDatosListView(canciones[i], metadataRetriever);
                    }
                try{
                    metadataRetriever.release();
                }catch (Exception e){
                    e.printStackTrace();
                }



            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "La carpeta está vacia", Toast.LENGTH_SHORT).show();
            }

            Adaptador adapter = new Adaptador(canciones, ListarCanciones.this);

            listaViewCanciones.setAdapter(adapter);


            listaViewCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //ClaseCancion cancionClicada = (ClaseCancion) parent.getItemAtPosition(position);

                    Dataholder.getInstance().canciones = canciones;
                    Intent intent = new Intent(getBaseContext(), ReproduccionCancion.class);
                    intent.putExtra("id_cancion", id);
                    intent.putExtra("posicion_cancion", position);
                    //intent.putExtra("cancion_posicion", canciones);
                    //intent.putExtra("cancion_titulo", cancionClicada.getNombre());
                    //intent.putExtra("cancion_artista", cancionClicada.getArtista());

                    startActivity(intent);
                }
            });


        } else {
            Toast.makeText(this, "Fichero vacio", Toast.LENGTH_SHORT).show();
        }
    }

    private void rellenarDatosListView(ClaseCancion canciones, MediaMetadataRetriever metadataRetriever){

        try{
            canciones.setNombre(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        }catch (Exception e) {
            canciones.setNombre("Desconocido");
        }

        try{
            canciones.setArtista(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }catch (Exception e) {
            canciones.setArtista("Desconocido");
        }
        try{
            canciones.setAlbum(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        }catch (Exception e) {
            canciones.setAlbum("Desconocido");
        }
/*
        Bitmap bitmap;
        try{
            canciones.setImagenAlbum(metadataRetriever.getEmbeddedPicture());
            //bitmap = BitmapFactory.decodeByteArray(canciones.imagenAlbum, 0, canciones.imagenAlbum.length);
            //imagenAlbum.setImageBitmap(bitmap);
        }catch (Exception e){
            int imagenDefecto = R.drawable.ic_launcher_foreground;
            bitmap = BitmapFactory.decodeResource(getResources(), imagenDefecto);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            canciones.setImagenAlbum(byteArray);
        }

 */

    }
}