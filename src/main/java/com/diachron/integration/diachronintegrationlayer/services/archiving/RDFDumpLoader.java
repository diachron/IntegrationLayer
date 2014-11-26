package com.diachron.integration.diachronintegrationlayer.services.archiving;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * @author panos
 */
public class RDFDumpLoader
extends ExternalResourcesLoader
{
    private String rdfTriples = null;
    
    @Override
    public void initConnection()
    {
        Client client = Client.create();
        WebResource webResource = client.resource(this.getUrl());	 
        ClientResponse response = webResource.get(ClientResponse.class);    
        this.rdfTriples = response.getEntity(String.class);
    }

    @Override
    public String loadData()
    {
        return this.rdfTriples;
    }

    @Override
    public void clearConnection()
    {
        this.rdfTriples = null;
    }
    
}
