package com.example.reproductormusica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//CLASE QUE SE ENCARGA DEL RECYCLERVIEW
public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {

    List<ClaseCanciones> listaCanciones;
    Context context;


    public Adaptador(List<ClaseCanciones> listaCanciones, Context context){
        this.listaCanciones = listaCanciones;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adaptador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ClaseCanciones cancion = listaCanciones.get(position);
        holder.textViewNombre.setText(cancion.getNombre());
        holder.textViewAutor.setText(cancion.getArtista());
        holder.textViewAlbum.setText(cancion.getAlbum());

        if (cancion.getImagenAlbum() != null) {
            holder.imageViewAlbum.setImageBitmap(BitmapFactory.decodeByteArray(cancion.getImagenAlbum(), 0, cancion.getImagenAlbum().length));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reproducirCancion(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listaCanciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewAutor;
        TextView textViewAlbum;
        ImageView imageViewAlbum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textNombreCancion);
            textViewAutor = itemView.findViewById(R.id.textNombreAutor);
            textViewAlbum = itemView.findViewById(R.id.textNombreAlbum);
            imageViewAlbum = itemView.findViewById(R.id.imagenAlbum);
        }
    }

    private void reproducirCancion(int pos){
        ClaseCanciones cancionEscojida = listaCanciones.get(pos);
        Intent intent = new Intent(context, ReproduccionCancion.class);
        intent.putExtra("cancion_path", cancionEscojida.getCancion().getPath());
        context.startActivity(intent);
    }

/*
    @Override
    public int getCount() {
        return canciones.length;
    }

    @Override
    public Object getItem(int i) {
        return canciones[i];
    }

    @Override
    public long getItemId(int i) {
        return canciones[i].getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") final View elemento = inflater.inflate(R.layout.activity_adaptador,
                viewGroup, false);

        Uri.parse("android.resource://" + context.getPackageName() + "/" + canciones[i].cancion);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context, uri);

        TextView nombreCancion = elemento.findViewById(R.id.textNombreCancion);
        nombreCancion.setText(canciones[i].nombre);

        String nombreAutor = String.valueOf(elemento.findViewById(R.id.textNombreAutor));
        nombreAutor = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        String nombreAlbum = String.valueOf(elemento.findViewById(R.id.textNombreAlbum));
        nombreAlbum = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        elemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReproduccionCancion.class);
                intent.putExtra("cancion", canciones[i].cancion);
                context.startActivity(intent);
            }
        });






    }

 */
}
