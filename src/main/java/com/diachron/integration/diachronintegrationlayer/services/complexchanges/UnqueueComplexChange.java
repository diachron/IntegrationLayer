package com.diachron.integration.diachronintegrationlayer.services.complexchanges;

import com.diachron.integration.services.evaluation.UnqueueEvaluateResource;
import java.io.IOException;
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
@Path("/complexChangeUnqueue")
public class UnqueueComplexChange
{
    @Context
    private UriInfo context;
    
    @Context
    private HttpServletResponse serv;    
    
    public UnqueueComplexChange() {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)    
    public String dequeue()
    {
        try
        {
            PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
            String queueName      = propertiesManager.getPropertyValue("ComplexDetectionDispatcherQueue");
            long   defaultWaiting = Integer.parseInt(propertiesManager.getPropertyValue("UnqueueDefaultWaiting"));
            String brokerURL = propertiesManager.getPropertyValue("DefaultBrokerURL");
            
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
                //InputStream in = null;

                try
                {
                    TextMessage textMessage = (TextMessage) message;                    
                    PrintWriter out = serv.getWriter();
                    String text = textMessage.getText();
                    out.println(text);
                    System.out.println(" LOOK AT THIS VALUE: " + text);
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    System.out.println("IOEX");
                }
                }        connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(UnqueueEvaluateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return "OK";
    }

}
