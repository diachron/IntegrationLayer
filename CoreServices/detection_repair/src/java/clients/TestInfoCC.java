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
 * @author rous
 */
public class TestInfoCC {
    
    public static void main(String[] args) {
        Client c = Client.create();
        System.out.println("Testing Fetch CC Service...");
        String url = "http://139.91.183.48:8181/diachron/complex_change";
        WebResource r = c.resource(url);
        String ccName = "Mark_as_Obsolete_v2";
        ClientResponse response = r.path(ccName).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        System.out.println(response.getEntity(String.class));
        System.out.println(response.getStatus());
        System.out.println("-----\n");
    }
   
}
