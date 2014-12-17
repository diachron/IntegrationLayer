package com.diachron.integration.diachronintegrationlayer.services.quality;

import caching.BasicCache;
import caching.GenericCacheInterface;
import com.diachron.integration.diachronintegrationlayer.messages.generic.ErrorMessageBase;
import com.diachron.integration.diachronintegrationlayer.services.archiving.ArchivingService;
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

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("qualityService")
public class QualityServiceResource {

    @Context
    private UriInfo context;

    public QualityServiceResource() {
    }

    @POST
    @Consumes(MediaType.WILDCARD)    
    public Response retrieveQualityMetrics(String inputMessage)
    {
        JSONObject jsonOutputMessage = null;
        GenericCacheInterface cache = BasicCache.getBasicCache();
        String query = "null";
        
        if((jsonOutputMessage = (JSONObject)cache.get(query))==null)
        {
                try {
                    JSONObject jsonInputMessage = new JSONObject(inputMessage);                    
                    String url = (String)jsonInputMessage.get("Dataset");
                    String qualityReportRequired = (String)jsonInputMessage.get("QualityReportRequired");
                    String metrics = (String)jsonInputMessage.get("MetricsConfiguration");                
                    QualityAssessentModuleBase qualityAssessentModuleBase = new QualityAssessentModuleBase();
                    jsonOutputMessage = qualityAssessentModuleBase.retrieveQualityMetrics(url, qualityReportRequired, metrics).getEntity(JSONObject.class);
                    System.out.println(jsonOutputMessage);

                } catch (JSONException ex) {
                    Logger.getLogger(QualityServiceResource.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
            
        return Response.status(Response.Status.OK).entity(jsonOutputMessage).build();        
    }
}
