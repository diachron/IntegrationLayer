package com.diachron.integration.diachronintegrationlayer.services.crawling;

import com.diachron.integration.dataaccesmodule.DataAccessModuleBase;
import com.diachron.integration.diachronintegrationlayer.messages.generic.ErrorMessageBase;
import com.diachron.integration.diachronintegrationlayer.services.archiving.ExternalResourcesLoader;
import com.diachron.integration.diachronintegrationlayer.services.archiving.RDFDumpLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
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
@Path("crawlingService")
public class CrawlingServiceResource
{

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CrawlingServiceResource
     */
    public CrawlingServiceResource()
    {
    }

    @POST
    @Produces("application/json")
    @Path("external/dump")
    @Consumes(MediaType.WILDCARD)        
    public Response insertRDFDUmpDataset(String inputMessage)
    {
        JSONObject jsonOutputMessage = new JSONObject();
        JSONObject jsonInputMessage = null;
        String url = null;
        String namedGraph = null;
        String rdfTriples = null;
        
        try
        {
            jsonInputMessage = new JSONObject(inputMessage);
            url = jsonInputMessage.getString("dump_url");
            namedGraph = jsonInputMessage.getString("named_graph");
        }
        catch (JSONException ex)
        {
            Logger.getLogger(CrawlingServiceResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ExternalResourcesLoader loader = new RDFDumpLoader();
        loader.setUrl(url);
        loader.initConnection();
        rdfTriples = loader.loadData();
        DataAccessModuleBase dataAccessModuleBase = new DataAccessModuleBase();
        dataAccessModuleBase.insertTriplesToNamedGraph(namedGraph, rdfTriples);

        try {
                jsonOutputMessage.put("Status", "Success");
            } catch (JSONException ex) {
                Logger.getLogger(CrawlingServiceResource.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return Response.status(Response.Status.OK).entity(jsonOutputMessage).build();
    }


}
