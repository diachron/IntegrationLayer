package com.diachron.integration.diachronintegrationlayer.services.archiving;

import caching.BasicCache;
import caching.GenericCacheInterface;
import com.diachron.integration.dataaccesmodule.DataAccessModuleBase;
import com.diachron.integration.diachronintegrationlayer.messages.archiving.ArchiveQueryInputMessage;
import com.diachron.integration.diachronintegrationlayer.messages.generic.ErrorMessageBase;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Path("archivingService")
public class ArchivingService {

    @Context
    private UriInfo context;

    public ArchivingService() {
    }

    @POST
    @Path("query")
    @Consumes(MediaType.APPLICATION_JSON)    
    public Response processQuery(ArchiveQueryInputMessage inputMessage)
    {
        JSONObject jsonOutputMessage = null;
        GenericCacheInterface cache = BasicCache.getBasicCache();
        JSONObject jsonInputMessage;
        String query = null;
        
        query = inputMessage.getQuery();
        if((jsonOutputMessage = (JSONObject)cache.get(query))==null)
        {
            DataAccessModuleBase dataAccessModuleBase = new DataAccessModuleBase();
            jsonOutputMessage = dataAccessModuleBase.executeSPARQLArchiveCall(query,
                                                                                  "SELECT",
                                                                                  MediaType.APPLICATION_JSON)
                                                                                  .getEntity(JSONObject.class);
            System.out.println(jsonOutputMessage);
                
            cache.put(query, jsonOutputMessage);
        }
            
        return Response.status(Response.Status.OK).entity(jsonOutputMessage).build();
    }
      
    private static void convert(String json)
    {
        InputStream is;        
        try {
            is = new ByteArrayInputStream( json.getBytes( "UTF-8" ) );
            ResultSet rs = ResultSetFactory.fromXML(is);
            //System.out.println(" --- "+ ResultSetFormatter.asXMLString(rs));
            System.out.println(rs.getResultVars());
            ResultSetFormatter.toModel(rs).write(System.out,"Turtle");            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ArchivingService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
