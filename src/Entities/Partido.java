package Entities;

import uy.edu.um.adt.hash.MyHashImpl;

import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

public class Partido {
    private ChronoLocalDate fecha;
    private int capacidad;
    private MyHashImpl<String,Entrada> entradas;
    private Pais seleccionA;
    private Pais seleccionB;

    public Partido() {
    }

    public Partido(ChronoLocalDate fecha, int capacidad, MyHashImpl<String, Entrada> entradas, Pais seleccionA, Pais seleccionB) {
        this.fecha = fecha;
        this.capacidad = capacidad;
        this.entradas = entradas;
        this.seleccionA = seleccionA;
        this.seleccionB = seleccionB;
    }

    public ChronoLocalDate getFecha() {
        return fecha;
    }

    public void setFecha(ChronoLocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public MyHashImpl<String, Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(MyHashImpl<String, Entrada> entradas) {
        this.entradas = entradas;
    }

    public Pais getSeleccionA() {
        return seleccionA;
    }

    public void setSeleccionA(Pais seleccionA) {
        this.seleccionA = seleccionA;
    }

    public Pais getSeleccionB() {
        return seleccionB;
    }

    public void setSeleccionB(Pais seleccionB) {
        this.seleccionB = seleccionB;
    }

    public void aumentarCapacidad(){
        this.capacidad = this.capacidad +1;
    }

    public void disminuirCapacidad(){
        this.capacidad = this.capacidad -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return Objects.equals(fecha, partido.fecha) && Objects.equals(seleccionA, partido.seleccionA) && Objects.equals(seleccionB, partido.seleccionB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, seleccionA, seleccionB);
    }
}
