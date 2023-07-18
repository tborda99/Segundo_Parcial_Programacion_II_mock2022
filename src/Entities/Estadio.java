package Entities;

import uy.edu.um.adt.binarytree.MySearchBinaryTreeImpl;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

public class Estadio {
    private String nombre;
    private MySearchBinaryTreeImpl<ChronoLocalDate,Partido> partidos;

    public Estadio() {
    }

    public Estadio(String nombre, MySearchBinaryTreeImpl<ChronoLocalDate, Partido> partidos) {
        this.nombre = nombre.toLowerCase();
        this.partidos = partidos;
    }

    public Estadio(String nombre) {
        this.nombre = nombre.toLowerCase();
        this.partidos =  new MySearchBinaryTreeImpl<>();
    }

    public String getNombre() {
        return nombre.toLowerCase();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toLowerCase();
    }

    public MySearchBinaryTreeImpl<ChronoLocalDate, Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(MySearchBinaryTreeImpl<ChronoLocalDate, Partido> partidos) {
        this.partidos = partidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estadio estadio = (Estadio) o;
        return Objects.equals(nombre, estadio.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }


}
