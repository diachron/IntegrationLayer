package com.diachron.integration.diachronintegrationlayer.services.archiving;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import management.configuration.PropertiesManager;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.core.header.*;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("archiveStore")
public class ArchiveStoreResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ArchiveStoreResource
     */
    public ArchiveStoreResource() {
    }

    /**
     * Retrieves representation of an instance of com.diachron.integration.diachronintegrationlayer.services.archiving.ArchiveStoreResource
     * @return an instance of java.lang.String
     */
    @GET
    public void store()
    {
        PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
        JSONObject jsonOutputMessage = null;
        String s = "{}";
        /*
        MultivaluedMap<String,String> map = new MultivaluedMapImpl();
        map.add("DataFile", "{}");
        map.add("DatasetURI", "http://www.diachron-fp7.eu/resource/dataset/efo/2.34/15CC3C652BCE1A071F614235E0F59C73");
        */
        String s2 = new String("http://www.diachron-fp7.eu/resource/dataset/efo/2.34/15CC3C652BCE1A071F614235E0F59C73");
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        //formDataMultiPart.field("DatasetURI", "http://www.diachron-fp7.eu/resource/dataset/efo/2.34/15CC3C652BCE1A071F614235E0F59C73");

//        FormDataBodyPart bodyPart2 = new FormDataBodyPart("DatasetURI", "http://www.diachron-fp7.eu/resource/dataset/efo/2.34/15CC3C652BCE1A071F614235E0F59C73");
        FormDataContentDisposition dispo = FormDataContentDisposition
        .name("DatasetURI")
        .size(s2.getBytes().length)
        .build();

        FormDataBodyPart bodyPart2 = new FormDataBodyPart(dispo, s2);        

        FormDataContentDisposition dispo1 = FormDataContentDisposition
        .name("DataFile")
        .fileName("json.2json")
        .size(s.getBytes().length)
        .build();        
        
        FormDataBodyPart bodyPart1 = new FormDataBodyPart(dispo1,s);             
        //formDataMultiPart.bodyPart(bodyPart);        
        

        MultiPart m = formDataMultiPart.bodyPart(bodyPart2).bodyPart(bodyPart1);
        System.out.println(" ----- " + dispo1.getName());
        
        //System.out.println("---- " + m.getContentDisposition().toString() );
        //System.out.println(formDataMultiPart.getContentDisposition().toString());
        Client c = Client.create();        
        WebResource r = c.resource("http://127.0.0.1:7090/archive-web-services/archive/dataset/version/metadata");

        ClientResponse response = r.type(MediaType.MULTIPART_FORM_DATA)
                                             .post(ClientResponse.class, m);
        jsonOutputMessage = response.getEntity(JSONObject.class);

        System.out.println(jsonOutputMessage);
        
    }

}
