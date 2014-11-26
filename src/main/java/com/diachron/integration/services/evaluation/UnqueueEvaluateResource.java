package com.diachron.integration.services.evaluation;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import management.configuration.PropertiesManager;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.BlobMessage;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("/evaluateUnqueue")
public class UnqueueEvaluateResource
{
    @Context
    private UriInfo context;
    
    @Context
    private HttpServletResponse serv;    
    
    public UnqueueEvaluateResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)    
    public String dequeue()
    {
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        String queueName      = propertiesManager.getPropertyValue("QualityAssessmentMonoQueue");
        long   defaultWaiting = Integer.parseInt(propertiesManager.getPropertyValue("UnqueueDefaultWaiting"));
        String brokerURL = propertiesManager.getPropertyValue("DefaultBrokerURL");
        
        try
        {            
            ConnectionFactory connectionFactory
                = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive(defaultWaiting);

            if (message instanceof TextMessage)
            {
                try
                {
                    TextMessage textMessage = (TextMessage) message;                    
                    PrintWriter out = serv.getWriter();
                    String text = textMessage.getText();
                    out.println(text);
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    System.out.println("IOEX");
                }
            }
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(UnqueueEvaluateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return "{\"queue_name\":\"" + queueName  + "\", \"Status\":\"Empty\"}";
    }

}
