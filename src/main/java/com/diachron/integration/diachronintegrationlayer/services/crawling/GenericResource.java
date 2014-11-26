package com.diachron.integration.diachronintegrationlayer.services.crawling;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import io.FileManagement;
import java.io.InputStream;
import java.net.URL;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import management.configuration.PropertiesManager;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("/qualityEnqueue")
public class GenericResource
{
    @Context
    private UriInfo context;

    public GenericResource()
    {
    }

    /**
     * Retrieves representation of an instance of com.test.activemqwebserv.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/{id}")
    public String inputQueue(@PathParam("id")String id)
    {
        try
        {
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
            System.out.println("+++++++++++ " + id);
            //InputStream is = this.retrieveQualityMetrics("http://aksw.org/model/export/?m=http://aksw.org/&f=turtle").getEntityInputStream();
            InputStream is = this.retrieveQualityMetrics("file:///home/panos/NetBeansProjects/diachron/quality-luzzu-integration/examples/diachron-efo-2.34.rdf").getEntityInputStream();
            fileManagement.storeFile(is, filename);
            BlobMessage message = session.createBlobMessage(new URL("file://" + filename));
            producer.send(message);
            connection.close();
            
        } catch (Throwable ex)
        {
            ex.printStackTrace();
        }       
        
        return "OK";        
    }
    
    public ClientResponse retrieveQualityMetrics(String url)
    {        
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();        
        MultivaluedMap<String,String> map = new MultivaluedMapImpl();
        map.add("Dataset", url);
        map.add("QualityReportRequired", "false");
        //map.add("MetricsConfiguration", "{\"@id\":\"_:f4216607408b1\",\"@type\":[\"http://github.com/EIS-Bonn/Luzzu#MetricConfiguration\"],\"http://github.com/EIS-Bonn/Luzzu#metric\":[{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.POBODefinitionUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.HomogeneousDatatypes\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.ObsoleteConceptsInOntology\"},{\"@value\":\"eu.diachron.qualitymetrics.representational.understandability.LowBlankNodeUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.representational.understandability.HumanReadableLabelling\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.SynonymUsage\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.DefinedOntologyAuthor\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.conciseness.OntologyVersioningConciseness\"},{\"@value\":\"eu.diachron.qualitymetrics.accessibility.performance.DataSourceScalability\"},{\"@value\":\"eu.diachron.qualitymetrics.dynamicity.timeliness.TimelinessOfResource\"},{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.consistency.EntitiesAsMembersOfDisjointClasses\"}]}");
        map.add("MetricsConfiguration", "{\"@id\":\"_:f4216607408b1\",\"@type\":[\"http://github.com/EIS-Bonn/Luzzu#MetricConfiguration\"],\"http://github.com/EIS-Bonn/Luzzu#metric\":[{\"@value\":\"eu.diachron.qualitymetrics.intrinsic.accuracy.POBODefinitionUsage\"}]}");
        
        Client client = Client.create();
        WebResource webResource = client
                    .resource(propertiesManager.getPropertyValue("ComputeQualityAssementResourceAddress"));
	 
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED)
                                             .post(ClientResponse.class, map);

        return response;
    }    

}
