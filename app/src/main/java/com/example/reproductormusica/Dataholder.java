package com.example.reproductormusica;

public class Dataholder {

    private static final Dataholder holder = new Dataholder();
    public ClaseCancion[] canciones;

    public static Dataholder getInstance() { return holder; }
}
