package Entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Entrada {
    private String codigo;
    private Aficionado propietario;

    public Entrada() {
    }

    public Entrada(String codigo, Aficionado propietario) {
        this.codigo = codigo;
        this.propietario = propietario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Aficionado getPropietario() {
        return propietario;
    }

    public void setPropietario(Aficionado propietario) {
        this.propietario = propietario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrada entrada = (Entrada) o;
        return Objects.equals(codigo, entrada.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    public LocalDate fechaCodigo(){
        String[] partes = codigo.split("\\|");

        String fechaString = partes[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate fechaLocalDate = LocalDate.parse(fechaString, formatter);

        return fechaLocalDate;

    }

    public String estadioCodigo(){
        String[] partes = codigo.split("\\|");

        String EstadioString = partes[2];
        EstadioString = EstadioString.toLowerCase();

        return EstadioString;

    }
}
