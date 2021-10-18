package co.com.eww.trabajowebwar.service;

import co.com.eww.trabajowebwar.DTO.EstudianteDTO;
import co.com.eww.trabajowebwar.exception.BussinessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstudianteService {

    static final String RUTA_MAL = "C:/Users/cristian/Desktop/UDEC/Linea II/Trabajos/TrabajoWebWar/Lista estudiantes.txt";
    static final String RUTA = "C:/Users/cristian/Documents/UDEC/Linea II/Trabajos/TrabajoWebWar/Lista estudiantes.txt";
    static final String RUTA_TEMPORAL = "C:/Users/cristian/Documents/UDEC/Linea II/Trabajos/TrabajoWebWar/Lista estudiantes.tmp";

    public void guardarEstudiante(EstudianteDTO estudiante, boolean guardar) throws FileNotFoundException, IOException {

        String ruta = "";
        if (guardar == true) {
            ruta = RUTA_MAL;
        } else {
            ruta = RUTA_TEMPORAL;
        }
        try {
            System.out.println("ruta" + ruta);
            //Se declara un objeto de tipo FileOutputStream pasandole como referencia la ruta del archivo
            FileOutputStream salida = new FileOutputStream(ruta, true);
            //Se declara un objeto de tipo ObjectOutputStream y se envia por parámetro el objeto de tipo FileOutputStream
            ObjectOutputStream escribir = new ObjectOutputStream(salida);
            //Se escriben los objetos en el archivo.
            escribir.writeObject(estudiante);
            escribir.close();
            salida.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("");
        } catch (IOException ex) {
            throw new IOException();
        }
    }

    public List<EstudianteDTO> listarEstudiantes() throws ClassNotFoundException, FileNotFoundException, IOException {

        List<EstudianteDTO> listaEstudiantes = new ArrayList<>();
        try {
            //Se declara un objeto de tipo FileOutputStream pasandole como referencia la ruta del archivo
            FileInputStream entrada = new FileInputStream(RUTA);
            //Se declara un objeto de tipo ObjectOutputStream 
            ObjectInputStream leer;
            // Creamos un bucle para leer la información -- Mientras haya bytes en el archivo --
            while (entrada.available() > 0) {
                //Se inicializa el ObjectOutputStream enviando por parámetro el objeto de tipo FileOutputStream
                leer = new ObjectInputStream(entrada);
                // Se lee un objeto del archivo
                EstudianteDTO estudianteLeido = (EstudianteDTO) leer.readObject();
                //se almacena en la lista
                listaEstudiantes.add(estudianteLeido);
            }
            entrada.close();
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("");
        } catch (IOException ex) {
            throw new IOException();
        }
        return listaEstudiantes;
    }

    public EstudianteDTO obtenerEstudiante(String cedula) throws ClassNotFoundException, IOException, BussinessException {

        EstudianteDTO estudiante = null;
        try {
            Boolean validar = tieneLetras(cedula);
            if (cedula.length() != 10 || validar == true) {
                throw new BussinessException(" Error en el formato de la cedula");
            }else{
                //llena la lista con todos los estudiantes registrados
                List<EstudianteDTO> listaEstudiantes = listarEstudiantes();
                //recorre la lista buscando el objeto a consultar
                for (int i = 0; i < listaEstudiantes.size(); i++) {
                    if (cedula.equals(listaEstudiantes.get(i).getCedula())) {
                        estudiante = listaEstudiantes.get(i);
                        System.out.println("Estudiante encontrado !!");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("");
        } catch (IOException ex) {
            throw new IOException();
        }
        return estudiante;
    }

    public Boolean modificarEstudiante(EstudianteDTO estudianteModificar) throws ClassNotFoundException, FileNotFoundException, IOException {

        Boolean existe = false;
        try {
            //llena la lista con todos los estudiantes registrados
            List<EstudianteDTO> listaEstudiantes = listarEstudiantes();
            //recorre la lista buscando el objeto a modificar
            for (int i = 0; i < listaEstudiantes.size(); i++) {
                if (estudianteModificar.getCedula().equals(listaEstudiantes.get(i).getCedula())) {
                    //modifica el estudiante
                    guardarEstudiante(estudianteModificar, false);
                    existe = true;
                } else {
                    //guarda los registros que no se modificaran en un nuevo archivo
                    guardarEstudiante(listaEstudiantes.get(i), false);
                }
                //elimina el archivo original que contiene todos los registros
                File archivo = new File(RUTA);
                archivo.delete();
                //instancia del archivo temporal llenado para posteriormente renombrarlo con el nombre del original
                File archivoTemporal = new File(RUTA_TEMPORAL);
                archivoTemporal.renameTo(archivo);
            }
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("");
        } catch (IOException ex) {
            throw new IOException();
        }
        return existe;
    }

    public Boolean eliminarEstudiante(String cedula) throws BussinessException {

        Boolean existe = false;
        try {
            Boolean validar = tieneLetras(cedula);
            if (cedula.length() != 10 || validar == true) {
                throw new BussinessException(" Error en el formato de la cedula");
            }else{
                //llena la lista con todos los estudiantes registrados
                List<EstudianteDTO> listaEstudiantes = listarEstudiantes();
                //recorre la lista buscando el objeto a eliminar
                for (int i = 0; i < listaEstudiantes.size(); i++) {
                    if (!cedula.equals(listaEstudiantes.get(i).getCedula())) {
                        guardarEstudiante(listaEstudiantes.get(i), false);
                        existe = false;
                    } else {
                        existe = true;
                    }
                }
            }
            //elimina el archivo original que contiene todos los registros
            File archivo = new File(RUTA);
            archivo.delete();
            //instancia del archivo temporal llenado para posteriormente renombrarlo con el nombre del original
            File archivoTemporal = new File(RUTA_TEMPORAL);
            archivoTemporal.renameTo(archivo);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EstudianteService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EstudianteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existe;
    }
    
    //metodos para validaciones 
    
   public Boolean tieneLetras(String texto){
    
    String letras="abcdefghyjklmnñopqrstuvwxyz";
    texto = texto.toLowerCase();
    for(int i=0; i<texto.length(); i++){
       if (letras.indexOf(texto.charAt(i),0)!=-1){
          return true;
       }
    }
   return false;
    }
   
}
