package com.diachron.integration.dataaccesmodule;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URLEncoder;
import management.configuration.PropertiesManager;

/**
 *
 * @author panos
 */
public class DataAccessModuleBase
{
    public ClientResponse insertTriplesToNamedGraph(String namedGraphURI, String triples)
    {
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        
        Client client = Client.create();
        String insertSPARQL = new String("INSERT {" + triples + "}");
        WebResource webResource = client
            .resource(propertiesManager.getPropertyValue("DataAccessCoreSPARQlEndpointURL")
                        + "?default-graph-uri=" + URLEncoder.encode(namedGraphURI).replaceAll("\\+","%20")
                        + "&query=" + URLEncoder.encode(insertSPARQL).replaceAll("\\+","%20")
                        + "&format=text%2Fhtml&timeout=0&debug=on");
	 
        ClientResponse response = webResource.get(ClientResponse.class);

        return response;
    }
    
    public ClientResponse deleteNamedGraph(String namedGraphURI)
    {
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        
        Client client = Client.create();
        String deleteSPARQL = new String("CLEAR GRAPH <" + namedGraphURI + ">");
        WebResource webResource = client
            .resource(propertiesManager.getPropertyValue("DataAccessCoreSPARQlEndpointURL")
                        + "?query=" + URLEncoder.encode(deleteSPARQL).replaceAll("\\+","%20")
                        + "&format=text%2Fhtml&timeout=0&debug=on");
	 
        ClientResponse response = webResource.get(ClientResponse.class);

        return response;
    }

    
    public ClientResponse executeSPARQLArchiveCall(String query, String queryType, String mediaType)
    {
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        
        Client c = Client.create();

        WebResource r = c.resource(propertiesManager.getPropertyValue("ArchiveResourceAddress")
                        + "?query=" + URLEncoder.encode(query).replaceAll("\\+","%20")
                        + "&queryType=" + queryType);
        
        ClientResponse response = r.type(mediaType).get(ClientResponse.class);
        
        return response;
    }
            
}
