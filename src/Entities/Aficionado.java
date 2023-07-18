package Entities;

import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

public class Aficionado implements Comparable<Aficionado> {

    private String pasaporte;
    private Pais paisOrigen;
    private MyList<Entrada> mis_entradas;
    private int cantidadTraspasos;

    public Aficionado() {
    }

    public Aficionado(String pasaporte, Pais paisOrigen, MyList<Entrada> mis_entradas) {
        this.pasaporte = pasaporte;
        this.paisOrigen = paisOrigen;
        this.mis_entradas = mis_entradas;
        this.cantidadTraspasos = 0;
    }

    public Aficionado(String pasaporte, Pais paisOrigen) {
        this.pasaporte = pasaporte;
        this.paisOrigen = paisOrigen;
        this.mis_entradas = new MyLinkedListImpl<>();
        this.cantidadTraspasos = 0;
    }

    public String getPasaporte() {
        return pasaporte;
    }

    public void setPasaporte(String pasaporte) {
        this.pasaporte = pasaporte;
    }

    public Pais getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(Pais paisOrigen) {
        this.paisOrigen = paisOrigen;
    }


    public MyList<Entrada> getMis_entradas() {
        return mis_entradas;
    }

    public void setMis_entradas(MyList<Entrada> mis_entradas) {
        this.mis_entradas = mis_entradas;
    }

    public int getCantidadTraspasos() {
        return cantidadTraspasos;
    }

    public void setCantidadTraspasos(int cantidadTraspasos) {
        this.cantidadTraspasos = cantidadTraspasos;
    }

    public void aumentarTraspasos(){
        this.cantidadTraspasos = this.cantidadTraspasos+1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aficionado that = (Aficionado) o;
        return Objects.equals(pasaporte, that.pasaporte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pasaporte);
    }

    /*
    * Devuelve TRUE si YA HAY PARTIDO PARA ESA FECHA
    * */
    public boolean TieneEntradaFecha(ChronoLocalDate fecha){
        boolean flag = false;
        for (int i = 0; i < mis_entradas.size(); i++) {
            if (mis_entradas.get(i).fechaCodigo().isEqual(fecha)){
                flag = true;
            }
        }
        return flag;

    }

    public int compareTo(Aficionado o) {
        return o.getPasaporte().compareTo(this.pasaporte);
    }
}
