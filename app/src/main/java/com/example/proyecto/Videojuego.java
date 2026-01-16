package com.example.proyecto;

public class Videojuego {

    public int idJuego;
    public String titulo;
    public String plataforma;
    public String estado;
    public int portada;

    public Videojuego(int idJuego, String titulo, String plataforma, String estado, int portada) {
        this.idJuego = idJuego;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.estado = estado;
        this.portada = portada;
    }
}
