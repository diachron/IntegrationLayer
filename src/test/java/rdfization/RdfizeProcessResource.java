/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfization;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import management.configuration.PropertiesManager;
import org.athena.imis.diachron.archive.datamapping.MultidimensionalConverter;
import org.athena.imis.diachron.archive.datamapping.OntologyConverter;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("rdfizeProcess")
public class RdfizeProcessResource {

    @Context
    private UriInfo context;
    public RdfizeProcessResource() {
    }

    @GET
    @Produces("text/plain")
    public void getText()
    {
        try {
            /*
            String dir = "/home/panos/Downloads/test/";
            File[] files = new File(dir).listFiles();
            
            for(File file : files)
            {
            System.out.println("Converting file " + file.getName());
            File inputFile = new File(dir+file.getName());
            FileInputStream fis = null;
            File outputFile = new File(dir+"_diachron_"+file.getName());
            
            try {
            fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OntologyConverter converter = new OntologyConverter();
            converter.convert(fis, fos, "test");
            
            fis.close();
            fos.close();
            } catch (IOException e) {
            e.printStackTrace();
            }
            }
            JSONObject jsonOutputMessage = null;
            String s = "{}";

            String s2 = new String("http://www.diachron-fp7.eu/resource/diachronicDataset/TESTEST/2961D3C31FBD6D0ABC36FA53D3565915");
            FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
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
            MultiPart m = formDataMultiPart.bodyPart(bodyPart2).bodyPart(bodyPart1);
            
            Client c = Client.create();        
            WebResource r = c.resource("http://127.0.0.1:8080/archive-web-services/archive/dataset/version");

            ClientResponse response = r.type(MediaType.MULTIPART_FORM_DATA)
            .post(ClientResponse.class, m);
            
            jsonOutputMessage = response.getEntity(JSONObject.class);
            System.out.println(jsonOutputMessage);
            */
            
            FormDataMultiPart form = new FormDataMultiPart();
             

            String s2 = new String("http://www.ebi.ac.uk/efo");
            FormDataContentDisposition dispo = FormDataContentDisposition
                .name("diachronicDatasetURI")
                .size(s2.getBytes().length)
                .build();            
            FormDataBodyPart bodyPart2 = new FormDataBodyPart(dispo, s2);        
            
            FormDataBodyPart fdp = new FormDataBodyPart("dataFile",
                    new FileInputStream("/home/panos/Downloads/test/small_efo.rdf"),
                    MediaType.APPLICATION_OCTET_STREAM_TYPE);
            form.bodyPart(bodyPart2).bodyPart(fdp);
            
            WebResource resource = Client.create().resource("http://192.168.10.151:8080/archive-web-services/archive/dataset/version");
            resource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(String.class, form);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RdfizeProcessResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
