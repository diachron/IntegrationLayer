/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diachron.integration.diachronintegrationlayer.services.crawling;

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
@Path("/qualityUnqueue")
public class UnqueueResource
{
    @Context
    private UriInfo context;
    
    @Context
    private HttpServletResponse serv;    
    
    public UnqueueResource() {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)    
    public String dequeue()
    {
        try
        {
            PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
            String queueName      = propertiesManager.getPropertyValue("QualityAssessmentMonoQueue");
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

            if (message instanceof BlobMessage)
            {
                InputStream in = null;

                try
                {
                    BlobMessage blobMessage = (BlobMessage) message;
                    in = blobMessage.getInputStream();
                    int d = 0;
                    PrintWriter out = serv.getWriter();

                    while ((d=in.read())!=-1)
                    {
                       out.write(d);
                       serv.flushBuffer();
                    }
                    
                    in.close();
                    out.close();
                } catch (IOException ex) {
                    System.out.println("IOEX");
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        System.out.println("IOEX");                        
                    }
                }
                }        connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(UnqueueResource.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return "OK";
    }

}
