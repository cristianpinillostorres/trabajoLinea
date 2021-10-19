package co.com.eww.trabajowebwar.service;

import co.com.eww.trabajowebwar.DTO.EstudianteDTO;
import co.com.eww.trabajowebwar.exception.BadRequestEx;
import co.com.eww.trabajowebwar.exception.BussinessException;
import co.com.eww.trabajowebwar.exception.ConflictException;
import co.com.eww.trabajowebwar.exception.NotFoundEx;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.BadRequestException;

public class EstudianteService {

    static final String RUTA_MAL = "C:/Users/cristian/Desktop/UDEC/Linea II/Trabajos/TrabajoWebWar/Lista estudiantes.txt";
    static final String RUTA = "C:/Users/cristian/Documents/UDEC/Linea II/Trabajos/TrabajoWebWar/Lista estudiantes.txt";
    static final String RUTA_TEMPORAL = "C:/Users/cristian/Documents/UDEC/Linea II/Trabajos/TrabajoWebWar/Lista estudiantes.tmp";

    public void guardar(EstudianteDTO estudiante, String ruta) throws IOException {

        try {
            //Se declara un objeto de tipo FileOutputStream pasandole como referencia la ruta del archivo
            FileOutputStream salida = new FileOutputStream(ruta, true);
            //Se declara un objeto de tipo ObjectOutputStream y se envia por parámetro el objeto de tipo FileOutputStream
            ObjectOutputStream escribir = new ObjectOutputStream(salida);
            //Se escriben los objetos en el archivo.
            escribir.writeObject(estudiante);
            escribir.close();
            salida.close();
        } catch (IOException ex) {
            throw new IOException();
        }
    }

    public void guardarEstudiante(EstudianteDTO estudiante, boolean guardar) throws IOException, ConflictException, ClassNotFoundException, BadRequestEx {

        try {
            if ((tieneLetras(estudiante.getCedula()) == true)) {
                throw new BadRequestEx("La cedula no puede contener letras");
            }
            if ((validarCedula(estudiante.getCedula()) == true)) {
                throw new ConflictException("La cedula ya esta registrada");
            }
            if (estudiante.getCedula().length() != 10) {
                throw new BadRequestEx("La cedula no tiene el tamaño requerido");
            }
            if ((validarEdad(estudiante.getEdad()) == true)) {
                throw new BadRequestEx("Ingrese una edad entre 18 y 100 ");
            }
            if (tieneLetras(estudiante.getEdad().toString()) == true) {
                throw new BadRequestEx("Ingrese una edad en valores numericos");
            }
            if ((validarCorreo(estudiante.getCorreo()) == false)) {
                throw new BadRequestEx("Ingrese un correo valido");
            } else {
                guardar(estudiante, RUTA);
            }

        } catch (IOException ex) {
            throw new IOException("Archivo no encontrado");
        }
    }

    public List<EstudianteDTO> listarEstudiantes() throws ClassNotFoundException, IOException {

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
            throw new ClassNotFoundException("Error en la escritura al archivo");
        } catch (IOException ex) {
            throw new IOException("Archivo no encontrado");
        }
        return listaEstudiantes;
    }

    public EstudianteDTO obtenerEstudiante(String cedula) throws ClassNotFoundException, IOException, NotFoundEx, BadRequestEx {

        EstudianteDTO estudiante = null;

        try {
            byte contador = 0;
            Boolean validarLetras = tieneLetras(cedula);
            if ((tieneLetras(cedula) == true)) {
                throw new BadRequestEx("La cedula no puede contener letras");
            }
            if (cedula.length() != 10) {
                throw new BadRequestEx("La cedula no tiene el tamaño requerido");
            } else {
                //llena la lista con todos los estudiantes registrados
                List<EstudianteDTO> listaEstudiantes = listarEstudiantes();
                //recorre la lista buscando el objeto a consultar
                for (EstudianteDTO listaEstudiante : listaEstudiantes) {
                    if (cedula.equals(listaEstudiante.getCedula())) {
                        contador++;
                    }
                }
                if (contador == 0) {
                    throw new NotFoundEx("La cédula no está registrada");
                } else {
                    for (int i = 0; i < listaEstudiantes.size(); i++) {
                        if (cedula.equals(listaEstudiantes.get(i).getCedula())) {
                            estudiante = listaEstudiantes.get(i);
                        } else {

                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Error en la escritura al archivo");
        } catch (IOException ex) {
            throw new IOException("Archivo no encontrado");
        }
        return estudiante;
    }

    public void modificarEstudiante(EstudianteDTO estudianteModificar) throws ClassNotFoundException, IOException, NotFoundEx {

        //Validar la misma cedula
        //validar que no ingrese otra cedula 
        //
        try {
            byte contador = 0;
            //llena la lista con todos los estudiantes registrados
            List<EstudianteDTO> listaEstudiantes = listarEstudiantes();

            for (EstudianteDTO listaEstudiante : listaEstudiantes) {
                if (estudianteModificar.getCedula().equals(listaEstudiante.getCedula())) {
                    contador++;
                }
            }
            if (contador == 0) {
                throw new NotFoundEx("La cédula no está registrada");
            } else {
                //recorre la lista buscando el objeto a modificar
                for (int i = 0; i < listaEstudiantes.size(); i++) {
                    if (estudianteModificar.getCedula().equals(listaEstudiantes.get(i).getCedula())) {
                        //modifica el estudiante
                        guardar(estudianteModificar, RUTA_TEMPORAL);

                    } else {
                        //guarda los registros que no se modificaran en un nuevo archivo
                        guardar(listaEstudiantes.get(i), RUTA_TEMPORAL);
                    }
                    //elimina el archivo original que contiene todos los registros
                    File archivo = new File(RUTA);
                    archivo.delete();
                    //instancia del archivo temporal llenado para posteriormente renombrarlo con el nombre del original
                    File archivoTemporal = new File(RUTA_TEMPORAL);
                    archivoTemporal.renameTo(archivo);
                }
                }
            }catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Error en la escritura al archivo");
        }catch (IOException ex) {
            throw new IOException("Archivo no encontrado");
        }
        }

    

    public void eliminarEstudiante(String cedula) throws ClassNotFoundException, IOException, BadRequestEx, NotFoundEx, ConflictException {

        try {
            byte contador = 0;
            if ((tieneLetras(cedula) == true)) {
                throw new BadRequestEx("La cedula no puede contener letras");
            }
            if (cedula.length() != 10) {
                throw new BadRequestEx("La cedula no tiene el tamaño requerido");
            } else {
                //llena la lista con todos los estudiantes registrados
                List<EstudianteDTO> listaEstudiantes = listarEstudiantes();
                //recorre la lista buscando el objeto a eliminar

                for (EstudianteDTO listaEstudiante : listaEstudiantes) {
                    if (cedula.equals(listaEstudiante.getCedula())) {
                        contador++;
                    }
                }
                if (contador == 0) {
                    throw new NotFoundEx("La cédula no está registrada");
                } else {
                    for (int i = 0; i < listaEstudiantes.size(); i++) {
                        if (!cedula.equals(listaEstudiantes.get(i).getCedula())) {
                            guardar(listaEstudiantes.get(i), RUTA_TEMPORAL);
                        } else {

                        }
                    }
                }
            }
            //elimina el archivo original que contiene todos los registros
            File archivo = new File(RUTA);
            archivo.delete();
            //instancia del archivo temporal llenado para posteriormente renombrarlo con el nombre del original
            File archivoTemporal = new File(RUTA_TEMPORAL);
            archivoTemporal.renameTo(archivo);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Error en la escritura al archivo");
        } catch (IOException ex) {
            throw new IOException("Archivo no encontrado");
        }
    }

    //metodos para validaciones 
    public Boolean tieneLetras(String texto) {

        String letras = "abcdefghyjklmnñopqrstuvwxyz";
        texto = texto.toLowerCase();
        for (int i = 0; i < texto.length(); i++) {
            if (letras.indexOf(texto.charAt(i), 0) != -1) {
                return true;
            }
        }
        return false;
    }

    public Boolean validarCedula(String cedula) throws ClassNotFoundException, IOException {

        List<EstudianteDTO> listaEstudiantes = listarEstudiantes();
        //recorre la lista buscando si la cedula ya existe
        for (int i = 0; i < listaEstudiantes.size(); i++) {
            if (cedula.equals(listaEstudiantes.get(i).getCedula())) {
                return true;
            }
        }
        return false;
    }

    public Boolean validarEdad(Integer edad) {

        return (edad < 18) || (edad > 100);
    }

    public Boolean validarCorreo(String correo) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(correo);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

}
