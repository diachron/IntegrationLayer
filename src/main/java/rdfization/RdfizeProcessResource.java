/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import org.athena.imis.diachron.archive.datamapping.OntologyConverter;

/**
 * REST Web Service
 *
 * @author panos
 */
@Path("rdfizeProcess")
public class RdfizeProcessResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RdfizeProcessResource
     */
    public RdfizeProcessResource() {
    }

    /**
     * Retrieves representation of an instance of rdfization.RdfizeProcessResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText() {
        // TODO Auto-generated method stub
        String dir = "/home/panos/Downloads/test/";
        File[] files = new File(dir).listFiles();
        for(File file : files){
        System.out.println("Converting file " + file.getName());
        File inputFile = new File(dir+file.getName());
        FileInputStream fis = null;
        File outputFile = new File(dir+"_diachron_"+file.getName());
        try {
        fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);
        OntologyConverter converter = new OntologyConverter();
        converter.convert(fis, fos);
        fis.close();
        fos.close();
        } catch (IOException e) {
        e.printStackTrace();
        }
        
        }
        
        return "OK";
    }
    

}
