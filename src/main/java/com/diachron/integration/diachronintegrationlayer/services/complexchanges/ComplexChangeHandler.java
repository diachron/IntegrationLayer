package com.diachron.integration.diachronintegrationlayer.services.complexchanges;

import com.sun.jersey.api.client.ClientResponse;
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
import management.configuration.PropertiesManager;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("/complexChangeHandler")
public class ComplexChangeHandler
{
    @Context
    private UriInfo context;

    public ComplexChangeHandler()
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
            ComplexChangeCoreManager complexChangeManager = new ComplexChangeCoreManager();
            PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
            String queueName = propertiesManager.getPropertyValue("ComplexDetectionDispatcherQueue");
            String brokerURL = propertiesManager.getPropertyValue("DefaultBrokerURL");
            
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            ActiveMQSession session = (ActiveMQSession) connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

            Destination destination  = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);

            ClientResponse clientResponse = complexChangeManager.initChangeDetection(inputMessage);
            
            TextMessage message = session.createTextMessage(clientResponse.getEntity(String.class));
            
            producer.send(message);
            connection.close();
            
        } catch (Throwable ex)
        {
            ex.printStackTrace();
        }       
        
        return "OK";        
    }
}
