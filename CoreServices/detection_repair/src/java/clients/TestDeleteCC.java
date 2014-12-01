/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author rousakis
 */
public class TestDeleteCC {

    public static void main(String[] args) {
        Client c = Client.create();

        // test delete cc service
        System.out.println("Testing Delete Service...");
        String url = "http://139.91.183.48:8181/diachron/complex_change";
        String ccName = "Mark_as_Obsolete_v2";
        WebResource r = c.resource(url);
        ClientResponse response = r.path(ccName).accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        System.out.println(response.getEntity(String.class));
        System.out.println(response.getStatus());
        System.out.println("-----\n");
        ////
    }
}
