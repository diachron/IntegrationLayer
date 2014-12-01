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
public class TestChangeDetection {

    public static void main(String[] args) {
        Client c = Client.create();
        /*
        String v1 = "http://www.diachron-fp7.eu/resource/recordset/efo/2.34";
        String v2 = "http://www.diachron-fp7.eu/resource/recordset/efo/2.35";
        boolean ingest = false;
        WebResource r = c.resource("http://139.91.183.48:8181/diachron/change_detection");
        String input = "{ \"V1\" : \"" + v1 + "\", "
                + "\"V2\" : \"" + v2 + "\", "
                + "\"ingest\" : " + ingest + ", "
                + "\"CCs\" : [\"\"] }";
                */
              String v1 = "http://www.diachron-fp7.eu/resource/recordset/efo/2.34";
        String v2 = "http://www.diachron-fp7.eu/resource/recordset/efo/2.35";
        boolean ingest = true;
        WebResource r = c.resource("http://139.91.183.48:8181/diachron/change_detection");
        String input = "{ \"V1\" : \"" + v1 + "\", "
                + "\"V2\" : \"" + v2 + "\", "
                + "\"ingest\" : " + ingest + ", "
                + "\"CCs\" : [\"Mark_as_Obsolete_v2\"] }";
        ClientResponse response = r.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
        System.out.println(response.getEntity(String.class));
        System.out.println(response.getStatus());
    }
}
