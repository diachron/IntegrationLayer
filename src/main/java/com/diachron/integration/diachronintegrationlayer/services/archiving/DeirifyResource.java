package com.diachron.integration.diachronintegrationlayer.services.archiving;

import caching.BasicCache;
import caching.GenericCacheInterface;
import com.diachron.integration.diachronintegrationlayer.messages.generic.ErrorMessageBase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import management.configuration.PropertiesManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("deirify")
public class DeirifyResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DeirifyResource
     */
    public DeirifyResource() {
    }

    /**
     * Retrieves representation of an instance of com.diachron.integration.diachronintegrationlayer.services.archiving.DeirifyResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Response getJson()
    {
        String state;
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        JSONObject jsonOutputMessage = null;
        GenericCacheInterface cache = BasicCache.getBasicCache();
        
        String query = "SELECT * FROM <http://www.diachron-fp7.eu/resource/recordset/efo/2.45> WHERE {?a ?p ?o} limit 100";
        if((jsonOutputMessage = (JSONObject)cache.get(query))==null)
        {
                Client c = Client.create();
                String queryType = new String("SELECT");

                WebResource r = c.resource(propertiesManager.getPropertyValue("ArchiveResourceAddress") + "?query=" + URLEncoder.encode(query).replaceAll("\\+","%20") + "&queryType=" + queryType);
                System.out.println(query);

                ClientResponse response = r.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
                jsonOutputMessage = response.getEntity(JSONObject.class);

                System.out.println(jsonOutputMessage);
                cache.put(query, jsonOutputMessage);
        }
            
        return Response.status(Response.Status.OK).entity(jsonOutputMessage).build();
    }

}
