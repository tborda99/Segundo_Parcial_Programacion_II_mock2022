import Entities.Aficionado;
import Exceptions.CompraRechazada;
import Exceptions.EntidadNoExiste;
import Exceptions.EquiposCompletos;
import Exceptions.InformacionInvalida;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.LocalDate;

public interface ControladorMundial {

    void registrarPartido(LocalDate fecha, String nombreEstadio, int capacidadHabilitada, String equipoPaisA, String equipoPaisB) throws InformacionInvalida, EquiposCompletos;

    void venderEntrada(String pasaporteAficionado, String paisOrigen, LocalDate partidoFecha, String nombreEstadio) throws InformacionInvalida, EntidadNoExiste, CompraRechazada;

    void traspasoEntrada(String pasaporteAficionado, String pasaporteAficionadoDestino, String paisOrigenAficionadoDestino, String codigoEntrada) throws EntidadNoExiste, InformacionInvalida, CompraRechazada;

    MyList<Aficionado> buscarAficionadosSospechosos();
}
