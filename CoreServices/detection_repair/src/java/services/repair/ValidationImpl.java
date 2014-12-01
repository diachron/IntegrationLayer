/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.repair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.diachron.repair.master.Results;
import org.diachron.repair.master.Validation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author rousakis
 */
@Path("validation")
public class ValidationImpl {

    private static String propFile = "/home/panos/NetBeansProjects/diachron/detection_repair/Config Files/config.properties";

    /** Creates a new instance of ValidationImpl */
    public ValidationImpl() {
    }

    /**
     * <b>POST</b> method which is responsible for the diagnosis of invalidities between an instance of a 
     * DIACHRON dataset and the provided integrity constraints.<br>
     * <b>URL:</b> /diachron/validation
     * @param <b>inputMessage</b> : A JSON-encoded string which has the following form: <br>
     * { <br>
     * "Dataset" : "Dataset1", <br>
     * "Ontology_w_constraints" : “Ontology1”, <br>
     * "GetInvalidities” : true, <br>
     * } <br>
     * <ul>
     * <li>Dataset - The URI of a DIACHRON entity to be validated. <br>
     * <li>Ontology_w_constraints - The  URI of an ontology describing the dataset, which should also contain the integrity 
     * constraints that will be checked over the dataset, in OWL syntax.<br>
     * <li>GetInvalidities - A flag which denotes if we want the method to return the set of invalidities or just 
     * a true/false value if the dataset is valid/invalid.
     * </ul>
     * @return A Response instance which has a JSON-encoded entity content depending on the input 
     * parameter of the method. We discriminate the following cases: <br>
     * <ul>
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message should have 
     * exactly 3 arguments." } if the input parameter has not three JSON parameters. 
     * <li> Error code: <b>200</b> and entity content: { "Success" : true, "Result" : true, "Invalidities" : [...]  } if 
     * the input parameter has the correct form where: 
     * <ul>
     * <li> Result is a flag which denotes whether the dataset is valid (true) or invalid (false).
     * <li> Invalidities is an array of triple pairs and each triple pair causes an invalidity in the dataset. 
     * </ul>
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message could not be parsed." } 
     * if the input parameter has not the correct form. 
     * </ul>
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validationJSON(String inputMessage) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputMessage);
            if (jsonObject.size() != 3) {
                String message = "JSON input message should have exactly 3 arguments.";
                String json = "{ \"Success\" : false, "
                        + "\"Message\" : \"" + message + "\" }";
                return Response.status(400).entity(json).build();
            } else {
                String dataset = (String) jsonObject.get("Dataset");
                String constOntology = (String) jsonObject.get("Ontology_w_constraints");
                boolean getInvalidities = (Boolean) jsonObject.get("GetInvalidities");
                ///
                Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(propFile));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String virtuoso = properties.getProperty("Repository_IP");
                String username = properties.getProperty("Repository_Username");
                String password = properties.getProperty("Repository_Password");
                String port = properties.getProperty("Repository_Port");
                Validation validation = new Validation();
                String[] arguments = {virtuoso, port, username, password, dataset, constOntology, Boolean.toString(getInvalidities)};
                Results result = validation.run(arguments);
                String json = "{ \"Success\" = true, \"Result\" : " + result.getType() + ", \"Invalidities\" : [" + result.getStringBuilder() + "] }";
                return Response.status(200).entity(json).build();
            }
        } catch (ParseException ex) {
            String message = "JSON input message could not be parsed.";
            String json = "{ \"Success\" : false, "
                    + "\"Message\" : \"" + message + "\" }";
            return Response.status(400).entity(json).build();
        }
    }
}
