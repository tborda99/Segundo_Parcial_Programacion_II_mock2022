import Entities.*;
import Exceptions.CompraRechazada;
import Exceptions.EntidadNoExiste;
import Exceptions.EquiposCompletos;
import Exceptions.InformacionInvalida;
import uy.edu.um.adt.binarytree.MySearchBinaryTree;
import uy.edu.um.adt.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.adt.hash.MyHashImpl;
import uy.edu.um.adt.heap.MyHeap;
import uy.edu.um.adt.heap.MyHeapImpl;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;

public class ControladorMundialImp implements ControladorMundial{

    private MyHashImpl<String, Aficionado> aficionados = new MyHashImpl<>();
    private MyHashImpl<String, Estadio> estadios = new MyHashImpl<>();
    private MyLinkedListImpl<Pais> paises = new MyLinkedListImpl<>();


    @Override
    public void registrarPartido(LocalDate fecha, String nombreEstadio, int capacidadHabilitada, String equipoPaisA, String equipoPaisB) throws InformacionInvalida, EquiposCompletos {
        //Si informacion invalida tira informacion invalida
        if (fecha.isAfter(LocalDate.now()) || capacidadHabilitada<0){ throw new InformacionInvalida();}

        //Todos los strings a minusucula
        equipoPaisA = equipoPaisA.toLowerCase();
        equipoPaisB = equipoPaisB.toLowerCase();
        nombreEstadio= nombreEstadio.toLowerCase();

        //Cambio la fecha a chronolocaldate para que sea comparable
        ChronoLocalDate fechaC = LocalAChrono(fecha);

        //Armo los dos paises y me fijo que existan en la lista.
        //Si no estan y hay lugar, los agrego, sino tiro exception.
        Pais paisA = new Pais(equipoPaisA);
        Pais paisB = new Pais(equipoPaisB);

        if(paises != null){
            if(!paises.contains(paisA) && paises.size() < 32){
                //Si no lo contiene, y no estan todas las selcciones cargadas lo agrego.
                paises.add(paisA);
            } else if (!paises.contains(paisA) && paises.size() >= 32) {
                throw new EquiposCompletos();
            }

            if(!paises.contains(paisB) && paises.size() < 32){
                //Si no lo contiene, y no estan todas las selcciones cargadas lo agrego.
                paises.add(paisB);
            } else if (!paises.contains(paisB) && paises.size() >= 32) {
                throw new EquiposCompletos();
            }
        }else{
            paises.add(paisA);
            paises.add(paisB);
        }


        //Creo el partido con su Hash de Entradas
        MyHashImpl<String, Entrada> entradas_nuevo = new MyHashImpl<>();
        Partido partido_nuevo = new Partido(fechaC,capacidadHabilitada, entradas_nuevo,paisA,paisB);

        //Si no existe el estadio lo crea.
        if (!estadios.contains(nombreEstadio)){
            //Creo el arbol de partidos para el estadio y agrego el partido
            MySearchBinaryTreeImpl<ChronoLocalDate, Partido> BST_partidos_nuevo = new MySearchBinaryTreeImpl<>();
            BST_partidos_nuevo.add(fechaC,partido_nuevo);

            //Creo el nuevo estadio  y agrego a lista de estadios
            Estadio estadio_nuevo = new Estadio(nombreEstadio, BST_partidos_nuevo);
            estadios.put(nombreEstadio,estadio_nuevo);
        }else if(!estadios.get(nombreEstadio).getPartidos().contains(fechaC)){
            //Si el estadio esta creado, pero no tiene partido para esa fecha, lo agrego.
            estadios.get(nombreEstadio).getPartidos().add(fechaC,partido_nuevo);


        }else{
            //El estadio existe, y hay un partido para esa fecha tiro informacion Invalida
            throw new InformacionInvalida();
        }

    }

    @Override
    public void venderEntrada(String pasaporteAficionado, String paisOrigen, LocalDate partidoFecha, String nombreEstadio) throws InformacionInvalida, EntidadNoExiste, CompraRechazada {
        if (pasaporteAficionado == null || paisOrigen == null || partidoFecha == null || nombreEstadio == null) {
            throw new InformacionInvalida();
        }
        //Todos los strings a minuscula
        paisOrigen = paisOrigen.toLowerCase();
        nombreEstadio = nombreEstadio.toLowerCase();


        Estadio estadio = null;
        Partido partido = null;
        Boolean partidoOK = false;
        if(estadios.contains(nombreEstadio)){
            if(estadios.get(nombreEstadio).getPartidos().contains(LocalAChrono(partidoFecha))){
                if(estadios.get(nombreEstadio).getPartidos().find(LocalAChrono(partidoFecha)).getCapacidad()>0){
                    estadio =estadios.get(nombreEstadio);
                    partido = estadio.getPartidos().find(LocalAChrono(partidoFecha));
                    partidoOK = true;
                }
            }else{
                //No tiene el partido
                throw new EntidadNoExiste();
            }
        }

        if (partidoOK){

            //Chequeo el aficionado.
            Aficionado aficionadoAVender = CrearODevolverAficonado(pasaporteAficionado,paisOrigen);

            //Check Numero Traspasos
            if(aficionadoAVender.getCantidadTraspasos() >=5){
                throw new CompraRechazada();
            }
            //Check entradas misma fecha. Con esta condicion tambien se cumple lo de una entrada por partido
            MyList<Entrada> entradasAficionado = aficionadoAVender.getMis_entradas();

            for (int i = 0; i < entradasAficionado.size(); i++) {
                if(entradasAficionado.get(i).fechaCodigo().isEqual(LocalAChrono(partidoFecha))){
                    throw new CompraRechazada();
                }

            }

            //Si Partido OK y Aficonado OK
                //Genero Codigo
            String codigo = GenerarCodigo(aficionadoAVender,partidoFecha,estadio);
                //Genero Entrada
            Entrada entrada = new Entrada(codigo,aficionadoAVender);
                //La agrego al partido
            partido.getEntradas().put(codigo,entrada);
                //Bajo la capacidad del partido
            partido.disminuirCapacidad();
                //Agrego entrada al Aficionado
            aficionadoAVender.getMis_entradas().add(entrada);
                //Agrego el aficionado a la lista de aficionados
            aficionados.put(pasaporteAficionado,aficionadoAVender);


        }

    }

    @Override
    public void traspasoEntrada(String pasaporteAficionado, String pasaporteAficionadoDestino, String paisOrigenAficionadoDestino, String codigoEntrada) throws EntidadNoExiste, InformacionInvalida, CompraRechazada {

        //Datos null
        if(pasaporteAficionado == null || pasaporteAficionadoDestino == null || paisOrigenAficionadoDestino == null || codigoEntrada == null){
            throw new InformacionInvalida();
        }
        Aficionado aficionadoOrigen;
        //Aficionado original existe en la base
        if(!aficionados.contains(pasaporteAficionado)){
            throw new EntidadNoExiste();
        }else{
            aficionadoOrigen = aficionados.get(pasaporteAficionado);
        }

        //Busco o Creo el aficionado destino
        Aficionado aficionadoDestino = CrearODevolverAficonado(pasaporteAficionadoDestino,paisOrigenAficionadoDestino);

        if(aficionadoDestino.equals(aficionadoOrigen)){
            System.out.println("Aficionado Destino tiene que ser distinto de aficionado de origen");
            throw new CompraRechazada();
        }
        //Numero de Traspasos Realizados. El resto de las condiciones se encarga el vender entrada.
        if(aficionadoDestino.getCantidadTraspasos() >=5 || aficionadoOrigen.getCantidadTraspasos()>= 5){
            throw new CompraRechazada();
        }

        //Chequear que el codigo sea valido
        String[] partes = codigoEntrada.split("\\|");
        String pasaporteCodigo = partes[0];
        String fechaCodigo = partes[1];
        String estadioCodigo = partes[2].toLowerCase();

        if (!pasaporteCodigo.trim().toLowerCase().equals(pasaporteAficionado.trim().toLowerCase())){
            System.out.println("La entrada no pertenece a ese aficionado");
            throw new CompraRechazada();
        }else{
            MyList<Entrada> entradasOrigen = aficionados.get(pasaporteAficionado).getMis_entradas();
            Entrada entradaAux = new Entrada(codigoEntrada,aficionadoOrigen);
            if(!entradasOrigen.contains(entradaAux)){
                System.out.println("La entrada no pertenece a ese aficionado");
                throw new CompraRechazada();
            }
        }

        LocalDate fechaPartido = LocalDate.parse(fechaCodigo, DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (estadios.contains(estadioCodigo)){
            if(estadios.get(estadioCodigo).getPartidos().contains(fechaPartido)){
                estadios.get(estadioCodigo).getPartidos().find(fechaPartido).aumentarCapacidad();
            }
        }

        try {
            venderEntrada(pasaporteAficionadoDestino,paisOrigenAficionadoDestino,fechaPartido,estadioCodigo);
            //Puede tirar: InformacionInvalida, EntidadNoExiste, CompraRechazada
            //Si tira, la capacidad tiene que volver a lo que era antes.
        } catch (InformacionInvalida e1) {
            estadios.get(estadioCodigo).getPartidos().find(fechaPartido).disminuirCapacidad();
            throw new InformacionInvalida();
        } catch (EntidadNoExiste e2) {
            estadios.get(estadioCodigo).getPartidos().find(fechaPartido).disminuirCapacidad();
            throw new EntidadNoExiste();
        } catch(CompraRechazada e3){
            estadios.get(estadioCodigo).getPartidos().find(fechaPartido).disminuirCapacidad();
            throw new CompraRechazada();
        }


        Partido partidoACambiar = estadios.get(estadioCodigo).getPartidos().find(fechaPartido);


        //Le saco entrada al aficionado
        aficionadoOrigen.getMis_entradas().remove(partidoACambiar.getEntradas().get(codigoEntrada));

        //Saco esa entrada de las del partido.
        partidoACambiar.getEntradas().remove(codigoEntrada);

        //Le sumo traspasos
        aficionadoOrigen.aumentarTraspasos();
        aficionadoDestino.aumentarTraspasos();


    }

    @Override
    public MyList<Aficionado> buscarAficionadosSospechosos() {

        MyList<Aficionado> aficionadosList = aficionados.values();
        MyList<Aficionado> sospechosos = new MyLinkedListImpl<>();
        int size = aficionadosList.size();


        for (int i = 0; i < size; i++) {
            Aficionado afiAux = aficionadosList.get(i);
            if(afiAux.getMis_entradas().size() + afiAux.getCantidadTraspasos() >= 5){
                boolean sospechoso = true;
                for (int j = 0; j < afiAux.getMis_entradas().size(); j++) {
                    String estadioAux = afiAux.getMis_entradas().get(j).estadioCodigo();
                    MyList<Pais> paises = paisesEntrada(afiAux.getMis_entradas().get(j));
                    for (int k = 0; k < paises.size(); k++) {
                        if(paises.get(k).equals(afiAux.getPaisOrigen())){
                            sospechoso = false;
                        }
                    }
                }
                if(sospechoso) {
                    sospechosos.add(afiAux);
                }
            }
        }


        //Ordenar de mayor a menor
        MyList<Aficionado> sospechososOrdenados = new MyLinkedListImpl<>();
        MyHeap<Aficionado> sospechososHeap = new MyHeapImpl<>(false);
        for (int i = 0; i < sospechosos.size(); i++) {
            sospechososHeap.insert(sospechosos.get(i));
        }


        int sizeHeap = sospechososHeap.size();
        for (int i = 0; i < sizeHeap; i++) {
            sospechososOrdenados.add(sospechososHeap.delete());
        }

        return sospechososOrdenados;
    }

    public MyHashImpl<String, Aficionado> getAficionados() {
        return aficionados;
    }

    public void setAficionados(MyHashImpl<String, Aficionado> aficionados) {
        this.aficionados = aficionados;
    }

    public MyHashImpl<String, Estadio> getEstadios() {
        return estadios;
    }

    public void setEstadios(MyHashImpl<String, Estadio> estadios) {
        this.estadios = estadios;
    }

    public MyLinkedListImpl<Pais> getPaises() {
        return paises;
    }

    public void setPaises(MyLinkedListImpl<Pais> paises) {
        this.paises = paises;
    }

    //METODOS AUXILIARES
    public ChronoLocalDate LocalAChrono(LocalDate fecha){
        Chronology desiredChronology = IsoChronology.INSTANCE;
        ChronoLocalDate fechaC = desiredChronology.dateEpochDay(fecha.toEpochDay());
        return fechaC;
    }

    public String GenerarCodigo(Aficionado aficionado, LocalDate fecha, Estadio estadio){
        String pasaporte = aficionado.getPasaporte();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fechaS = fecha.format(formatter);

        String nombreEstadio = estadio.getNombre();
        nombreEstadio = nombreEstadio.replace(" ", "").toUpperCase();


        String codigo = pasaporte + "|" + fechaS + "|" +nombreEstadio;
        return codigo;

    }

    public Aficionado CrearODevolverAficonado(String pasaporte,String paisOrigen) throws EntidadNoExiste {

        paisOrigen = paisOrigen.toLowerCase();
        Pais paisAux = null;
        for (int i = 0; i < paises.size(); i++) {
            if (paises.get(i).getNombre().equals(paisOrigen)){
                paisAux = paises.get(i);
            }
        }
        if(paisAux == null){
            System.out.println("Pais no registrado");
            throw new EntidadNoExiste();
        }

        if(aficionados.contains(pasaporte)){
            return aficionados.get(pasaporte);
        }else{
            Aficionado afi = new Aficionado(pasaporte, paisAux);
            aficionados.put(pasaporte,afi);
            return afi;
        }
    }

    public MyList<Pais> paisesEntrada(Entrada entrada){
        //Devuelve los paises que juegan un partido, dado un codigo

        MyList<Pais> paises = new MyLinkedListImpl<>();
        LocalDate fecha = entrada.fechaCodigo();
        String estadio = entrada.estadioCodigo();

        Pais paisA = estadios.get(estadio).getPartidos().find(LocalAChrono(fecha)).getSeleccionA();
        Pais paisB = estadios.get(estadio).getPartidos().find(LocalAChrono(fecha)).getSeleccionB();

        paises.add(paisA);
        paises.add(paisB);

        return paises;

    }



}
