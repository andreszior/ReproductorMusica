package com.example.reproductormusica;

import java.io.File;

public class ClaseCancion {



    long id;

    File cancion;
    String nombre;
    String artista;

    String album;

    byte[] imagenAlbum;


    public ClaseCancion(){}
    public ClaseCancion(long id, File cancion, String nombre){
        this.id = id;
        this.cancion = cancion;
        this.nombre = nombre;
    }

    public ClaseCancion(long id, File cancion, String nombre, String artista, String album, byte[] imagenAlbum) {
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


    public File getCancion() {
        return cancion;
    }

    public void setCancion(File cancion) {
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
