package co.com.eww.trabajowebwar.exception;

import java.io.Serializable;
import java.util.Date;

public class ExceptionWrapper implements Serializable {
    
    private String codigo;
    
    private String error;
    
    private String fecha;
    
    private String mensaje;
    
    private String ulr;

    public ExceptionWrapper() {
    }
    
    public ExceptionWrapper(String codigo, String error, String mensaje, String ulr) {
        this.codigo = codigo;
        this.error = error;
        this.fecha = new Date().toString();
        this.mensaje = mensaje;
        this.ulr = ulr;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUlr() {
        return ulr;
    }

    public void setUlr(String ulr) {
        this.ulr = ulr;
    }
   
}
