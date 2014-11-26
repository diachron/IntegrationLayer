package com.diachron.integration.diachronintegrationlayer.services.complexchanges;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import management.configuration.PropertiesManager;

/**
 *
 * @author panos
 */
public class ComplexChangeCoreManager
{
    public ClientResponse initChangeDetection(String input)
    {   
        Client client = Client.create();
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        
        WebResource r = client.resource(propertiesManager.getPropertyValue("ChangeDetectionResourceAddress"));
        
        /*
        String input = "{ \"V1\" : \"" + v1 + "\", "
                        + "\"V2\" : \"" + v2 + "\", "
                        + "\"ingest\" : " + ingest + ", "
                        + "\"CCs\" : [\"" + complexChangeNameList + "\"] }";
        */        
        //Logger.getLogger(ComplexChangeCoreManager.class.getName()).log(Level.SEVERE, null, input);        
        
        ClientResponse response = r.type(MediaType.APPLICATION_JSON).
                                    accept(MediaType.APPLICATION_JSON).
                                    post(ClientResponse.class, input);

        return response;
    }

    public ClientResponse sendQuery(String input)
    {   
        Client client = Client.create();
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        
        String url = propertiesManager.getPropertyValue("ChangeDetectionQueryInterface");
        WebResource r = client.resource(url);
        ClientResponse response = r.type(MediaType.APPLICATION_JSON).
                                    accept(MediaType.APPLICATION_JSON).
                                    post(ClientResponse.class, input);
        
        return response;
    }    
    
}
