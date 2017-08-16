/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.assignments.crud.gradebook.restcl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author laveeshrohra
 */
public class GradeBook_CRUD_cl {
    private static final Logger LOG = LoggerFactory.getLogger(GradeBook_CRUD_cl.class);
    
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD-GradeBook-lrohraSrv/webresources";

    public GradeBook_CRUD_cl() {        
        LOG.info("Creating a GradeBook_CRUD REST client");

        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("GradeBook");
        LOG.debug("webResource = {}", webResource.getURI());
    }
    
    public ClientResponse createGradebookEntry(Object requestEntity) throws UniformInterfaceException {
        LOG.info("Initiating a Create request");
        LOG.debug("JSON String = {}", (String) requestEntity);
        
        return webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestEntity);
    }

    public ClientResponse deleteEntry(String id, String studentId) throws UniformInterfaceException {
        String path = studentId + "/" + id;
        
        LOG.info("Initiating a Delete request");
        LOG.debug("Path = {}", path);
        
        return webResource.path(path).delete(ClientResponse.class);
    }

    public ClientResponse updateEntry(Object requestEntity, String id, String studentId) throws UniformInterfaceException {
        String path = studentId + "/" + id;
        
        LOG.info("Initiating an Update request");
        LOG.debug("JSON String = {}", (String) requestEntity);
        LOG.debug("Path = {}", path);
        
        return webResource.path(path).type(MediaType.APPLICATION_JSON).put(ClientResponse.class, requestEntity);
    }

    public <T> T retrieveEntry(Class<T> responseType, String id, String studentId) throws UniformInterfaceException {
        String path = studentId + "/" + id;
        
        LOG.info("Initiating a Retrieve request");
        LOG.debug("responseType = {}", responseType.getClass());
        LOG.debug("Id = {}", path);
        
        //WebResource resource = webResource;
        //resource = resource.path(id);
        
        return webResource.path(path).accept(MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        LOG.info("Closing the REST Client");
        
        client.destroy();
    }
}
