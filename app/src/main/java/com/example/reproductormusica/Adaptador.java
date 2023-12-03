package com.example.reproductormusica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

//CLASE QUE SE ENCARGA DEL RECYCLERVIEW
public class Adaptador extends BaseAdapter {

    ClaseCancion[] listaCanciones;
    Context context;



    public Adaptador(ClaseCancion[] listaCanciones, Context context){
        this.listaCanciones = listaCanciones;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaCanciones.length;
    }

    @Override
    public Object getItem(int position) {
        return listaCanciones[position];
    }

    @Override
    public long getItemId(int position) {
        return listaCanciones[position].getId();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") final View elementos =
        inflater.inflate(R.layout.activity_adaptador, parent, false);

        TextView nombreCancion = elementos.findViewById(R.id.textNombreCancion);
        TextView nombreAutor = elementos.findViewById(R.id.textNombreAutor);
        TextView nombreAlbum = elementos.findViewById(R.id.textNombreAlbum);
        ImageView imagenAlbum = elementos.findViewById(R.id.imageAlbum);


        ClaseCancion cancion = (ClaseCancion) getItem(position);
        nombreCancion.setText(cancion.getNombre());
        nombreAutor.setText(cancion.getArtista());
        nombreAlbum.setText(cancion.getAlbum());

        //Bitmap bitmap = BitmapFactory.decodeByteArray(cancion.imagenAlbum, 0, cancion.imagenAlbum.length);
        //imagenAlbum.setImageBitmap(bitmap);



        return elementos;


    }

}
