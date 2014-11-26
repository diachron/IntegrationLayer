package com.diachron.integration.services.evaluation;

import com.diachron.integration.diachronintegrationlayer.services.quality.QualityAssessentModuleBase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import io.FileManagement;
import java.io.InputStream;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import management.configuration.PropertiesManager;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("/evaluateEnqueue")
public class EvaluateResource
{
    @Context
    private UriInfo context;

    public EvaluateResource()
    {
    }

    /**
     * Retrieves representation of an instance of com.test.activemqwebserv.GenericResource
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/{id}")
    @Consumes(MediaType.WILDCARD)
    public String inputQueue(@PathParam("id")String id, String inputMessage)
    {
        try
        {
            JSONObject jsonInputMessage = new JSONObject(inputMessage);
            
            String filename = null;
            PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
            String queueName = propertiesManager.getPropertyValue("QualityAssessmentMonoQueue");
            String brokerURL = propertiesManager.getPropertyValue("DefaultBrokerURL");
            
            FileManagement fileManagement = new FileManagement();
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            ActiveMQSession session = (ActiveMQSession) connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

            Destination destination  = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);

            filename = propertiesManager.getPropertyValue("QueuesTempFolder") + "/" + id + ".dat";
            String url = (String)jsonInputMessage.get("Dataset");
            String qualityReportRequired = (String)jsonInputMessage.get("QualityReportRequired");
            String metrics = (String)jsonInputMessage.get("MetricsConfiguration");
            
            //InputStream is = this.retrieveQualityMetrics("http://aksw.org/model/export/?m=http://aksw.org/&f=turtle").getEntityInputStream();
            QualityAssessentModuleBase qualityAssessentModuleBase = new QualityAssessentModuleBase();            
            InputStream is = qualityAssessentModuleBase.retrieveQualityMetrics(url, qualityReportRequired, metrics).getEntityInputStream();
            JSONObject json = new JSONObject(IOUtils.toString(is));
            System.out.println(" ------------ " + json.getJSONObject("QualityMetadata"));
            
            String rtn = store(json.getJSONObject("QualityMetadata").toString());
            
            TextMessage message = session.createTextMessage("{\"id\":\"" + id +"\", \"payload\":" + rtn + "}");

            producer.send(message);
            connection.close();
            
        } catch (Throwable ex)
        {
            ex.printStackTrace();
        }       
        
        return "";        
    }
    
    public String store(String s)
    {
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        JSONObject jsonOutputMessage = null;

        String s2 = new String("http://www.diachron-fp7.eu/resource/dataset/efo/2.34/15CC3C652BCE1A071F614235E0F59C73");
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        FormDataContentDisposition dispo = FormDataContentDisposition
        .name("DatasetURI")
        .size(s2.getBytes().length)
        .build();

        FormDataBodyPart bodyPart2 = new FormDataBodyPart(dispo, s2);        

        FormDataContentDisposition dispo1 = FormDataContentDisposition
        .name("DataFile")
        .fileName("json.2json")
        .size(s.getBytes().length)
        .build();        
        
        FormDataBodyPart bodyPart1 = new FormDataBodyPart(dispo1,s);             

        MultiPart m = formDataMultiPart.bodyPart(bodyPart2).bodyPart(bodyPart1);
        Client c = Client.create();        
        WebResource r = c.resource("http://127.0.0.1:7090/archive-web-services/archive/dataset/version/metadata");

        ClientResponse response = r.type(MediaType.MULTIPART_FORM_DATA)
                                             .post(ClientResponse.class, m);
        jsonOutputMessage = response.getEntity(JSONObject.class);

        return(jsonOutputMessage.toString()); 
    }
}
