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
public class TestRepair {

    public static void main(String[] args) {
        Client c = Client.create();
        String dataset = "http://repair/test/copy";
        String ontology = "http://dbpedia.org/ontology/3.6";
        WebResource r = c.resource("http://139.91.183.48:8181/diachron/repair");
        String input = "{ \"Dataset\" : \"" + dataset + "\", "
                + "\"Ontology_w_constraints\" : \"" + ontology + "\", "
                + "\"GetDelta\" : true  }";
        ClientResponse response = r.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
        System.out.println(response.getEntity(String.class));
        System.out.println(response.getStatus());
    }
}
