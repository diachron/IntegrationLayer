package com.diachron.integration.publication;

import caching.BasicCache;
import caching.GenericCacheInterface;
import com.diachron.integration.diachronintegrationlayer.messages.generic.ErrorMessageBase;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.jena.riot.Lang;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("publicationService")
public class PublicationServiceResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PublicationServiceResource
     */
    public PublicationServiceResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response convert(String inputMessage)
    {
        JSONObject jsonOutputMessage = new JSONObject();
        GenericCacheInterface cache = BasicCache.getBasicCache();
        JSONObject jsonInputMessage = new JSONObject();
        String url = null;
        String inputType = null;
        String returnType = null;
        
        try
        {
            jsonInputMessage = new JSONObject(inputMessage);
            url = jsonInputMessage.getString("url");
            inputType = jsonInputMessage.getString("inputType");
            returnType = jsonInputMessage.getString("returnType");
        }
        catch (JSONException ex)
        {
            Logger.getLogger(PublicationServiceResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PublicationManager publicationManager = new PublicationManager();
        publicationManager.setUrl(url);
        publicationManager.setInputMIMEType(Lang.get(inputType));
        publicationManager.setReturnMIMEType(returnType);
        publicationManager.convert();
            
        try {
                jsonOutputMessage.put("Status", "Success");
            } catch (JSONException ex) {
                Logger.getLogger(PublicationServiceResource.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return Response.status(Response.Status.OK).entity(jsonOutputMessage).build();        
    }
}
