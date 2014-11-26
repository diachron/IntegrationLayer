package com.diachron.integration.diachronintegrationlayer.services.validation;

import com.diachron.integration.dataaccesmodule.DataAccessModuleBase;
import com.diachron.integration.diachronintegrationlayer.messages.generic.ErrorMessageBase;
import com.diachron.integration.diachronintegrationlayer.messages.valildation.DeleteValidationMessage;
import com.diachron.integration.diachronintegrationlayer.messages.valildation.InsertValidationMessage;
import com.diachron.integration.diachronintegrationlayer.services.archiving.ArchivingService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("rulesTemplateService")
public class RulesTemplateServiceResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RulesTemplateServiceResource
     */
    public RulesTemplateServiceResource()
    {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response importRules(InsertValidationMessage inputMessage)
    {
        Subject currentUser = SecurityUtils.getSubject();   
        JSONObject jsonOutputMessage = new JSONObject();
        JSONObject jsonInputMessage;
        String triples = null;
        String namedGraph = null;

        triples    = inputMessage.getValidationTriples();
        namedGraph = inputMessage.getNamedGraph();
        
        if(currentUser.isAuthenticated())
        {
            DataAccessModuleBase dataAccessModuleBase = new DataAccessModuleBase();
            dataAccessModuleBase.insertTriplesToNamedGraph(namedGraph, triples);
            
            return Response.status(Response.Status.OK).build();
        }
        else
        {            
            ErrorMessageBase errorMessage = new ErrorMessageBase();
            errorMessage.setDatasetInformation("generic");
            errorMessage.setErrorMessage("AuthenticationProblem");
            errorMessage.setOriginProblematicModule("Apache Shiro");
            JSONObject jsonErrorMessage = null;
            
            try {
                jsonErrorMessage = errorMessage.serializeMessageToJSON();
            } catch (JSONException ex) {
                Logger.getLogger(ArchivingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonErrorMessage).build();
        }        
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response deleteRules(DeleteValidationMessage inputMessage)
    {
        Subject currentUser = SecurityUtils.getSubject();   
        JSONObject jsonOutputMessage = new JSONObject();
        JSONObject jsonInputMessage;
        String triples = null;
        String namedGraph = null;

        namedGraph = inputMessage.getNamedGraph();
        
        if(currentUser.isAuthenticated())
        {
            DataAccessModuleBase dataAccessModuleBase = new DataAccessModuleBase();
            dataAccessModuleBase.deleteNamedGraph(namedGraph);
            
            return Response.status(Response.Status.OK).build();
        }
        else
        {            
            ErrorMessageBase errorMessage = new ErrorMessageBase();
            errorMessage.setDatasetInformation("generic");
            errorMessage.setErrorMessage("AuthenticationProblem");
            errorMessage.setOriginProblematicModule("Apache Shiro");
            JSONObject jsonErrorMessage = null;
            
            try {
                jsonErrorMessage = errorMessage.serializeMessageToJSON();
            } catch (JSONException ex) {
                Logger.getLogger(ArchivingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonErrorMessage).build();
        }        
    }
    
}
