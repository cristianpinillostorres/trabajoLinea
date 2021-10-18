package co.com.eww.trabajowebwar.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception>{

    @Override
    public Response toResponse(Exception ex) {
        
        ex.printStackTrace();
        ExceptionWrapper wrraper;
        if(ex instanceof BussinessException ) {
             wrraper = new ExceptionWrapper("400", "BAD_REQUEST", ex.getMessage(),"/estudiantes/");    
            return Response.status(Response.Status.BAD_REQUEST).entity(wrraper).build();
        } else {
              wrraper = new ExceptionWrapper("500", "INTERNAL_SERVER_ERROR", "","/estudiantes/");    
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(wrraper).build();
        } 
    }
}
