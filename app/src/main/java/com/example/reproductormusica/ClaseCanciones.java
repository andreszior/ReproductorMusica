package com.example.reproductormusica;

import android.media.Image;
import android.net.Uri;

import java.io.File;

public class ClaseCanciones {



    long id;

    Uri cancion;
    String nombre;
    String artista;

    String album;

    byte[] imagenAlbum;


    public ClaseCanciones(){}
    public ClaseCanciones(long id, Uri cancion, String nombre){
        this.id = id;
        this.cancion = cancion;
        this.nombre = nombre;
    }

    public ClaseCanciones(long id, Uri cancion, String nombre, String artista, String album, byte[] imagenAlbum) {
        this.id = id;
        this.cancion = cancion;
        this.nombre = nombre;
        this.artista = artista;
        this.album = album;
        this.imagenAlbum = imagenAlbum;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Uri getCancion() {
        return cancion;
    }

    public void setCancion(Uri cancion) {
        this.cancion = cancion;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public byte[] getImagenAlbum() {
        return imagenAlbum;
    }

    public void setImagenAlbum(byte[] imagenAlbum) {
        this.imagenAlbum = imagenAlbum;
    }
}
