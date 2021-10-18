package co.com.eww.trabajowebwar.DTO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class EstudianteDTO implements Serializable {
    
    private String cedula;
    
    
    private String nombre;
    
    private String apellido;
    
    private Integer edad;
    
    private String correo;
    
    private List<String> listaMaterias;
    
    private int[] numero;

    public EstudianteDTO() {
    }

    public EstudianteDTO(String cedula, String nombre, String apellido, Integer edad, String correo, List<String> listaMaterias, int[] numero) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.correo = correo;
        this.listaMaterias = listaMaterias;
        this.numero = numero;
    }
    
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<String> getListaMaterias() {
        return listaMaterias;
    }

    public void setListaMaterias(List<String> listaMaterias) {
        this.listaMaterias = listaMaterias;
    }

    public int[] getNumero() {
        return numero;
    }

    public void setNumero(int[] numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "cedula=" + cedula + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + ", correo=" + correo + ", listaMateria=" + listaMaterias + ", numero=" + Arrays.toString(numero) + '}';
    }
    
}
