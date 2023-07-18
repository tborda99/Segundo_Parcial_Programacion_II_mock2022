import Entities.*;
import Exceptions.CompraRechazada;
import Exceptions.EntidadNoExiste;
import Exceptions.EquiposCompletos;
import Exceptions.InformacionInvalida;
import uy.edu.um.adt.binarytree.MySearchBinaryTree;
import uy.edu.um.adt.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;

import static org.junit.jupiter.api.Assertions.*;

class ControladorMundialImpTest {

    private ControladorMundialImp controlador;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        controlador = new ControladorMundialImp();
    }

    @org.junit.jupiter.api.Test
    void registrarPartido() throws InformacionInvalida, EquiposCompletos {
        LocalDate fecha = LocalDate.of(2022, 6, 24);

        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fecha.toEpochDay());

        controlador.registrarPartido(fecha,"Janoub",20000,"Uruguay","Qatar");

        assertTrue(controlador.getEstadios().size() == 1);
        assertEquals(controlador.getEstadios().get("janoub").getPartidos().find(fechaC).getCapacidad(),20000);

    }

    @org.junit.jupiter.api.Test
    void venderEntrada() throws InformacionInvalida, EquiposCompletos, EntidadNoExiste, CompraRechazada {
        //Registrar el Partido
        LocalDate fecha = LocalDate.of(2022, 6, 24);
        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fecha.toEpochDay());
        controlador.registrarPartido(fecha,"Janoub",20000,"Uruguay","Qatar");

        Pais uruguay = controlador.getPaises().get(0);
        Aficionado aficionado = new Aficionado("5143402",uruguay);

        controlador.venderEntrada("5143402","Uruguay",fecha,"Janoub");

        assertEquals(controlador.getEstadios().get("janoub").getPartidos().find(fechaC).getCapacidad(),19999);
        assertEquals(controlador.getEstadios().get("janoub").getPartidos().find(fechaC).getEntradas().keys().get(0),"5143402|20220624|JANOUB");
        assertEquals(controlador.getAficionados().get("5143402").getMis_entradas().get(0).fechaCodigo(),fecha);


    }

    @org.junit.jupiter.api.Test
    void traspasoEntrada() throws InformacionInvalida, EquiposCompletos, EntidadNoExiste, CompraRechazada {
        LocalDate fecha = LocalDate.of(2022, 6, 24);
        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fecha.toEpochDay());
        controlador.registrarPartido(fecha,"Janoub",20000,"Uruguay","Qatar");

        Pais uruguay = controlador.getPaises().get(0);
        Aficionado aficionado1 = new Aficionado("5143402",uruguay);

        controlador.venderEntrada("5143402","Uruguay",fecha,"Janoub");

        Aficionado aficionado2 = new Aficionado("4143403",uruguay);

        controlador.traspasoEntrada(aficionado1.getPasaporte(),aficionado2.getPasaporte(),aficionado2.getPaisOrigen().getNombre(),"5143402|20220624|JANOUB");

        assertEquals(controlador.getAficionados().get(aficionado1.getPasaporte()).getCantidadTraspasos()
                +controlador.getAficionados().get(aficionado1.getPasaporte()).getCantidadTraspasos(), 2);
        assertEquals(controlador.getEstadios().get("janoub").getPartidos().find(fechaC).getCapacidad(),19999);
        assertEquals(controlador.getEstadios().get("janoub").getPartidos().find(fechaC).getEntradas().size(),1);


    }

    @org.junit.jupiter.api.Test
    void buscarAficionadosSospechosos() throws InformacionInvalida, EquiposCompletos, EntidadNoExiste, CompraRechazada {
        LocalDate fecha0 = LocalDate.of(2022, 6, 23);
        LocalDate fecha1 = LocalDate.of(2022, 6, 24);
        LocalDate fecha2 = LocalDate.of(2022, 6, 25);
        LocalDate fecha3 = LocalDate.of(2022, 6, 26);
        LocalDate fecha4 = LocalDate.of(2022, 6, 27);
        LocalDate fecha5 = LocalDate.of(2022, 6, 28);
        LocalDate fecha6 = LocalDate.of(2022, 6, 29);

        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC0 = desiredChronology.dateEpochDay(fecha0.toEpochDay());
        ChronoLocalDate fechaC1 = desiredChronology.dateEpochDay(fecha1.toEpochDay());
        ChronoLocalDate fechaC2 = desiredChronology.dateEpochDay(fecha2.toEpochDay());
        ChronoLocalDate fechaC3 = desiredChronology.dateEpochDay(fecha3.toEpochDay());
        ChronoLocalDate fechaC4 = desiredChronology.dateEpochDay(fecha4.toEpochDay());
        ChronoLocalDate fechaC5 = desiredChronology.dateEpochDay(fecha5.toEpochDay());
        ChronoLocalDate fechaC6 = desiredChronology.dateEpochDay(fecha6.toEpochDay());


        controlador.registrarPartido(fecha0,"bombonera",20000,"Uruguay","Brasil");
        controlador.registrarPartido(fecha1,"Janoub",20000,"Inglaterra","Qatar");
        controlador.registrarPartido(fecha2,"centenario",20000,"Francia","Alemania");
        controlador.registrarPartido(fecha3,"campeondelsiglo",20000,"Australia","Japon");
        controlador.registrarPartido(fecha4,"Janoub",20000,"Francia","Japon");
        controlador.registrarPartido(fecha5,"centenario",20000,"Qatar","Alemania");
        controlador.registrarPartido(fecha6,"campeondelsiglo",20000,"Argentina","Brasil");

        Pais uruguay = controlador.getPaises().get(0);
        Aficionado aficionado1 = new Aficionado("5143402",uruguay);
        Aficionado aficionado2 = new Aficionado("4143403",uruguay);

        controlador.venderEntrada("5143402","Uruguay",fecha1,"Janoub");
        controlador.venderEntrada("5143402","Uruguay",fecha2,"centenario");
        controlador.venderEntrada("5143402","Uruguay",fecha3,"campeondelsiglo");
        controlador.venderEntrada("5143402","Uruguay",fecha4,"Janoub");
        controlador.venderEntrada("5143402","Uruguay",fecha5,"centenario");
        controlador.venderEntrada("5143402","Uruguay",fecha6,"campeondelsiglo");


        MyList<Aficionado> sospechosos = controlador.buscarAficionadosSospechosos();
        assertEquals(sospechosos.size(),1);
        assertTrue(sospechosos.get(0).equals(aficionado1));

        controlador.venderEntrada("4143403","Uruguay",fecha1,"Janoub");
        controlador.venderEntrada("4143403","Uruguay",fecha2,"centenario");
        controlador.venderEntrada("4143403","Uruguay",fecha3,"campeondelsiglo");
        controlador.venderEntrada("4143403","Uruguay",fecha4,"Janoub");
        controlador.venderEntrada("4143403","Uruguay",fecha5,"centenario");
        controlador.venderEntrada("4143403","Uruguay",fecha6,"campeondelsiglo");

        sospechosos = controlador.buscarAficionadosSospechosos();
        assertEquals(sospechosos.size(),2);
        assertTrue(sospechosos.get(0).equals(aficionado2));
        assertTrue(sospechosos.get(1).equals(aficionado1));


    }

    @org.junit.jupiter.api.Test
    void codigoEntrada(){
        Pais paisA = new Pais("Uruguay");
        Pais paisB = new Pais("Argentina");
        MyList<Entrada> entradas = new MyLinkedListImpl<>();
        Aficionado afi = new Aficionado("51434026",paisA,entradas);
        MySearchBinaryTree<ChronoLocalDate, Partido> partidos = new MySearchBinaryTreeImpl<>();
        Estadio estadio = new Estadio("Campeon del Siglo");

        LocalDate fecha = LocalDate.of(2022,12,12);

        String codigo = controlador.GenerarCodigo(afi,fecha,estadio);
        assertEquals(codigo,"51434026|20221212|CAMPEONDELSIGLO");


    }
}