package co.com.eww.trabajowebwar.controller;

import co.com.eej.trabajoejbjar.Service.IEstudianteService;
import co.com.eww.trabajowebwar.DTO.EstudianteDTO;
import co.com.eww.trabajowebwar.exception.BadRequestEx;
import co.com.eww.trabajowebwar.exception.BussinessException;
import co.com.eww.trabajowebwar.exception.ConflictException;
import co.com.eww.trabajowebwar.exception.NotFoundEx;
import co.com.eww.trabajowebwar.service.EstudianteService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/estudiantes")
public class EstudianteController {
    
    @EJB
    private IEstudianteService service;
    
    EstudianteService estudianteService = new EstudianteService();
    
    //Servicio para Registrar estudiantes recibe desde el body en formato json
    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarEstudiante(@Valid EstudianteDTO estudiante) throws IOException, FileNotFoundException, ClassNotFoundException, ConflictException, BadRequestEx {
        estudianteService.guardarEstudiante(estudiante, true);
        return Response.status(Response.Status.CREATED).build();
    }

    //Servicio para listar todos los estudiantes retornando cada estudiante en formato Json
    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarEstudiantes() throws ClassNotFoundException, IOException {
        List<EstudianteDTO> listaEstudiantes = estudianteService.listarEstudiantes();          
        if(!listaEstudiantes.isEmpty()){
            return Response.status(Response.Status.OK).entity(listaEstudiantes).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();  
        }
    }

    //Servicio para modificar estudiantes recibe desde el body en fotmato Json
    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificarEstudiante(@Valid EstudianteDTO estudiante) throws ClassNotFoundException, IOException, NotFoundEx {
        estudianteService.modificarEstudiante(estudiante);
        return Response.status(Response.Status.OK).build(); 

    }

    //Servicio para listar un estudiante por cedula retornando el estudiante en formato Json
    @GET
    @Path("/obtenerPorCedula/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEstudiante(@PathParam("cedula") String cedula ) throws ClassNotFoundException, IOException, NotFoundEx, BadRequestEx {
        EstudianteDTO estudiante = estudianteService.obtenerEstudiante(cedula);
        return Response.status(Response.Status.OK).entity(estudiante).build();
    }

    //Servicio para eliminar un estudiante por cedula 
    @DELETE
    @Path("/eliminar/{cedula}")
    public Response eliminarEstudiante(@PathParam("cedula") String cedula) throws ClassNotFoundException, IOException, BadRequestEx, NotFoundEx, ConflictException {
        estudianteService.eliminarEstudiante(cedula);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}

