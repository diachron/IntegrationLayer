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
public class TestDefineCC {
    
    public static void main(String[] args) {
        Client c = Client.create();
        System.out.println("Testing Define CC Service...");
        String url = "http://139.91.183.48:8181/diachron/complex_change";
        WebResource r = c.resource(url);
        String input = "{ "
                + "\"Complex_Change\" : \"Mark_as_Obsolete_v2\", "
                + "\"Priority\" : 1.0, "
                + "\"Complex_Change_Parameters\": ["
                + "{ \"obs_class\" : \"sc1:-subclass\" }, "
                + "{ \"obs_reason\" : \"sc2:-object\" }"
                + "],"
                + "\"Simple_Changes\": ["
                + "{"
                + "\"Simple_Change\" : \"ADD_SUPERCLASS\", "
                + "\"Simple_Change_Uri\" : \"sc1\", "
                + "\"Is_Optional\" : false, "
                + "\"Selection_Filter\" : \"sc1:-superclass = <http://www.geneontology.org/formats/oboInOwl#ObsoleteClass>\", "
                + "\"Mapping_Filter\" : \"\", "
                + "\"Join_Filter\" : \"\", "
                + "\"Version_Filter\" : \"\" "
                + "}, "
                + "{"
                + "\"Simple_Change\" : \"ADD_PROPERTY_INSTANCE\", "
                + "\"Simple_Change_Uri\" : \"sc2\", "
                + "\"Is_Optional\" : true, "
                + "\"Selection_Filter\" : \"sc2:-property = efo:reason_for_obsolescence\", "
                + "\"Mapping_Filter\" : \"\", "
                + "\"Join_Filter\" : \"sc1:-subclass = sc2:-subject\", "
                + "\"Version_Filter\" : \"\" }"
                + "]"
                + "}";
        
        ClientResponse response = r.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
        System.out.println(response.getEntity(String.class));
        System.out.println(response.getStatus());
        System.out.println("-----\n");
    }
}
