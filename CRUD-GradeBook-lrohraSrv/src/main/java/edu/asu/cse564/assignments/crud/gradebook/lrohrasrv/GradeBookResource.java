/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.assignments.crud.gradebook.lrohrasrv;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.asu.cse564.assignments.crud.gradebook.lrohrasrv.model.GradeBook;
import edu.asu.cse564.assignments.crud.gradebook.utils.CheckMultipleEntries;
import edu.asu.cse564.assignments.crud.gradebook.utils.Converter;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Random;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author laveeshrohra
 */

@Path("GradeBook")
public class GradeBookResource {
    private static final Logger LOG = LoggerFactory.getLogger(GradeBookResource.class);
    
    private static HashMap<Integer, GradeBook> bookEntries;
    private static Random rand;
    private static final int MAX = 999;

    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of GradeBookResource
     */
    public GradeBookResource(){
        LOG.info("Creating Gradebook resource instance");
//        bookEntries = new HashMap<>();
        rand = new Random();
        
    }
    
    /**
     * POST method for creating a new entry in GradeBook
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTaskEntry(String content) {
        LOG.info("Creating a new instance of Gradebook Resource");
        LOG.debug("POST request");
        LOG.debug("Request Content = {}", content);
        
        Response response;
        
        if(bookEntries == null){
            LOG.debug("Initialising map");
            bookEntries = new HashMap<>();
        }
            try {
                GradeBook newEntry = (GradeBook) Converter.JSONToObject(content, GradeBook.class);
                
                if(newEntry.getWorkTask().isEmpty() || newEntry.getStudentId().isEmpty()){
                    response = Response.status(Response.Status.BAD_REQUEST).entity("Student ID and Work Task required!").build();
                }
                else{
                    int entryExists = CheckMultipleEntries.checkEntryExists(bookEntries, newEntry);
                    if(entryExists == -1){
                        LOG.info("Creating new Entry: {}", newEntry);
                        int id = -1;
                        do {
                            LOG.info("ID: {}", id);
                            id = rand.nextInt(MAX);
                        }while(bookEntries.containsKey(id));   //To make sure new id is unique

                        newEntry.setTaskId(id);
                        bookEntries.put(id, newEntry);
                        String jsonResp = Converter.ObjectToJSON(newEntry);

                        URI locationURI = URI.create(context.getAbsolutePath() + "/" + newEntry.getStudentId() + "/" + Integer.toString(id));
                        response = Response.status(Response.Status.CREATED).location(locationURI).entity(jsonResp).build();
                    }
                    else{
                        LOG.debug("Entry already exists: {}", entryExists);
                        String resp = Converter.ObjectToJSON(bookEntries.get(entryExists));
                        response = Response.status(Response.Status.CONFLICT).entity(resp).build();
                    }
                }
            }
            catch (IOException ex) {
                LOG.debug("JSON Parsing error for string- {}", content);
                response = Response.status(Response.Status.BAD_REQUEST).entity(content).build();
            }
            catch (RuntimeException e) {
                LOG.debug("Catch All exception");
                LOG.info("Creating a {} {} Status Response", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());     
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(content).build();
            }

            LOG.debug("Generated response {}", response);
        
        return response;
    }
    
    
    /**
     * Retrieves representation of an instance of GradeBookResource
     * @param studentId
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{studentId}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntry(@PathParam("studentId") String studentId, @PathParam("id") String id) {
        LOG.info("Retrieving the Gradebook Entry wiht Student ID: {} and ID: {}", studentId, id);
        LOG.debug("GET request");
        
        Response response;
        
        
        if (bookEntries == null){
            LOG.info("Creating a {} {} Status Response", Response.Status.GONE.getStatusCode(), Response.Status.GONE.getReasonPhrase());
            LOG.debug("No Gradebook Entries to return");
            
            response = Response.status(Response.Status.GONE).entity("No Gradebook Entry to return").build();
        } 
        else {
            int taskId = Integer.parseInt(id);
            if (bookEntries.containsKey(taskId) && bookEntries.get(taskId).getStudentId().equals(studentId)){
                LOG.info("Creating a {} {} Status Response", Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase());
                LOG.debug("Retrieving the Gradebook Entry {}", bookEntries.get(taskId));
                
                try {
                    String jsonResp = Converter.ObjectToJSON(bookEntries.get(taskId));
                    response = Response.status(Response.Status.OK).entity(jsonResp).build();
                } 
                catch (JsonProcessingException ex) {
                    LOG.debug("Conversion from Object to JSON String error");
                    response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to return response").build();
                }
            } 
            else {
                LOG.info("Creating a {} {} Status Response", Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.getReasonPhrase());
                response = Response.status(Response.Status.NOT_FOUND).entity("No Gradebook entry under Student ID: " + studentId + " with ID : "+ id +" to return").build();
            }
        }        
        
        LOG.debug("Returning the value {}", response);
        return response;
    }
    
    
    /**
     * PUT method for updating an instance of GradeBookResource
     * @param studentId
     * @param id
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("{studentId}/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGradeBook(@PathParam("studentId") String studentId, @PathParam("id") String id, String content) {
        LOG.info("Updating the Gradebook Entry {} ", content);
        LOG.debug("PUT request");
        LOG.debug("PathParam studentId = {} and id = {}", studentId, id);
        LOG.debug("Request Content = {}", content);
        
        Response response;
        int taskId = Integer.parseInt(id);
        if (bookEntries != null && bookEntries.containsKey(taskId) && bookEntries.get(taskId).getStudentId().equals(studentId)){
            LOG.debug("Attempting to update the Gradebook Entry with ID {}", id);
  
            try {
                GradeBook updateEntry = (GradeBook) Converter.JSONToObject(content, GradeBook.class);

                LOG.debug("The JSON {} was converted to the object {}", content, updateEntry);         
                LOG.debug("Updated Gradebook Entry is: {}", updateEntry);

                if(updateEntry.getWorkTask().isEmpty() && !updateEntry.getAppeal()){
                    response = Response.status(Response.Status.BAD_REQUEST).entity("Work Task required!").build();
                }
                else{
                    LOG.info("Creating a {} {} Status Response", Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase());

                    if(updateEntry.getAppeal()){
                        updateEntry = bookEntries.get(taskId);
                        updateEntry.setAppeal(true);
                    }
                    else if(!updateEntry.getAppeal()){
                        GradeBook oldEntry = bookEntries.get(taskId);
                        if(oldEntry.getAppeal()){
                            updateEntry.setAppeal(true);
                        }
                    }
                    
                    bookEntries.put(taskId, updateEntry);
                    String jsonResp = Converter.ObjectToJSON(updateEntry);

                    response = Response.status(Response.Status.OK).entity(jsonResp).build();
                }
            } catch (IOException e) {
                LOG.info("Creating a {} {} Status Response", Response.Status.BAD_REQUEST.getStatusCode(), Response.Status.BAD_REQUEST.getReasonPhrase());
                LOG.debug("JSON {} is incompatible with Gradebook Resource", content);

                response = Response.status(Response.Status.BAD_REQUEST).entity(content).build();
            } catch (RuntimeException e) {
                LOG.debug("Catch All exception");
                LOG.info("Creating a {} {} Status Response", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());

                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(content).build();
            }
        } 
        else {
            LOG.info("Creating a {} {} Status Response", Response.Status.CONFLICT.getStatusCode(), Response.Status.CONFLICT.getReasonPhrase());
            LOG.debug("Cannot update Gradebook Entry with Student ID : {} and  ID : {} as it doesn't exist", studentId, id);
                      
            response = Response.status(Response.Status.CONFLICT).entity(content).build();
        }

        LOG.debug("Generated response {}", response);
        return response;
    }
    
    
    /**
     * Delete Gradebook Entry
     * @param studentId
     * @param id
     * @return an instance of java.lang.String
     */
    @DELETE
    @Path("{studentId}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGradebookEntry(@PathParam("studentId") String studentId, @PathParam("id") String id) {
        LOG.info("Removing the Gradebook Entry with id: {}", id);
        LOG.debug("DELETE request");
        
        Response response;
        
        int taskId = Integer.parseInt(id);
        if (bookEntries == null){
            LOG.info("Creating a {} {} Status Response", Response.Status.GONE.getStatusCode(), Response.Status.GONE.getReasonPhrase());
            LOG.debug("No Appointment Resource to delete");
            
            response = Response.status(Response.Status.GONE).entity("No Gradebook Entry to delete").build();
        } 
        else {
            if (bookEntries.containsKey(taskId) && bookEntries.get(taskId).getStudentId().equals(studentId)){
                LOG.info("Creating a {} {} Status Response", Response.Status.NO_CONTENT.getStatusCode(), Response.Status.NO_CONTENT.getReasonPhrase());
                LOG.debug("Deleting the Gradebook Entry {}", bookEntries.get(taskId));
                
                bookEntries.remove(taskId);
                
                response = Response.status(Response.Status.NO_CONTENT).build();
            } 
            else {
                LOG.info("Creating a {} {} Status Response", Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.getReasonPhrase());
                
                response = Response.status(Response.Status.NOT_FOUND)
                    .entity("No Gradebook Entry found with Student ID : "+studentId+" and ID: "+taskId+" to delete").build();
            }
        }        
        
        LOG.debug("Generated response {}", response);
        
        return response;
    }
}
