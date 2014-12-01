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
public class TestQuery {

    public static void main(String[] args) {
        Client c = Client.create();
        System.out.println("Testing Query Service...");

        String query = "select ?sc ?param_name ?param_value ?v1 ?v2 where { "
                + "?sc a co:Add_Label; "
                + "?param ?param_value; "
                + "co:old_version ?v1; "
                + "co:new_version ?v2. "
                + "?param co:name ?param_name."
                + "} limit 100";
        String format = "xml";
        //
//        String url = "http://localhost:8181/diachron/change_detection";
//        WebResource r = c.resource(url);
//        MultivaluedMap formData = new MultivaluedMapImpl();
//        formData.add("query", query);
//        formData.add("format", format);
//        ClientResponse response = r.queryParams(formData).get(ClientResponse.class);
        //////// 
        String input = "{ "
                + "\"Query\" : \"" + query + "\", "
                + "\"Format\" : \"" + format + "\""
                + " }";
        String url = "http://139.91.183.48:8181/diachron/change_detection/query";
        WebResource r = c.resource(url);
        ClientResponse response = r.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);

        System.out.println(response.getEntity(String.class));
        System.out.println(response.getStatus());
        System.out.println("-----\n");
    }
}
