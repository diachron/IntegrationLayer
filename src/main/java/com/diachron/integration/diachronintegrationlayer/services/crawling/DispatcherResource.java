package com.diachron.integration.diachronintegrationlayer.services.crawling;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.UUID;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;


@Path("QualityDispatcher")
public class DispatcherResource
{

    @Context
    private UriInfo context;

    public DispatcherResource() {
    }

    /**
     * Retrieves representation of an instance of com.test.activemqwebserv.DispatcherResource
     * @return an instance of java.lang.String
     */
    @GET
    public String Dispatcher()
    {
        Thread thread = new Thread()
        {
            public void run()
            {
                Client client = Client.create();
                WebResource webResource = client
                    .resource("http://127.0.0.1:9090/DIACHRONIntegrationLayer/webresources/qualityEnqueue/"
                            + ("" + UUID.randomUUID().toString().replaceAll("-", "")));

                ClientResponse response = webResource.get(ClientResponse.class);                
            }
        };
        
        thread.start();
        
        return "Dispatched ";
    }


}
