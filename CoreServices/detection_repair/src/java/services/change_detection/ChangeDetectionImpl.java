/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.change_detection;

import change_detection_utils.ChangesDetector;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.query.resultio.text.csv.SPARQLResultsCSVWriter;
import org.openrdf.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.openrdf.repository.RepositoryException;
import repositories.SesameVirtRep;

/**
 * This class contains the services which are related with the change detection process along with the 
 * discovery and search of any detected changes. 
 * @author rousakis
 */
@Path("change_detection")
public class ChangeDetectionImpl {

    private static String propFile = "/home/panos/NetBeansProjects/diachron/detection_repair/Config Files/config.properties";

    public ChangeDetectionImpl() {
    }

    /**
     * <b>POST</b> method which is responsible for the change detection process among two dataset versions. 
     * This method detects any existing simple and complex changes and updates the ontology of changes accordingly. <br>
     * <b>URL:</b> /diachron/change_detection
     * @param <b>inputMessage</b> : A JSON-encoded string which has the following form: <br>
     * { <br>
     * "Old_Version" : "V1", <br>
     * "New_Version" : "V2", <br>
     * "ingest" : true, <br>
     * "CCs" : ["Label_Obsolete", ...] <br>
     * } <br>
     * where
     * <ul>
     * <li>Old_Version - The old version URI of a DIACHRON entity.<br>
     * <li>New_Version - The old version URI of a DIACHRON entity.<br>
     * <li>ingest - A flag which denotes whether the service is called due to a new dataset ingestion or not.
     * <li>CCs - The set of complex change types which will be considered. If the set is empty, then all 
     * the defined complex changes in the ontology of changes will be considered.
     * </ul>
     * @return A Response instance which has a JSON-encoded entity content depending on the input 
     * parameter of the method. We discriminate the following cases: <br>
     * <ul>
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message should have 
     * exactly 4 arguments." } if the input parameter has more than four JSON parameters. 
     * <li> Error code: <b>200</b> and entity content: { "Success" : true, "Message" : "Change detection among versions 
     * Old_Version, New_Version was executed." } if the input parameter has the correct form. 
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message could not be parsed." } 
     * if the input parameter has not the correct form. 
     * </ul>
     * 
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeDetectJSON(String inputMessage) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputMessage);
            if (jsonObject.size() != 4) {
                String message = "JSON input message should have exactly 4 arguments.";
                String json = "{ \"Success\" : false, "
                        + "\"Message\" : \"" + message + "\" }";
                return Response.status(400).entity(json).build();
            } else {
                String oldVersion = (String) jsonObject.get("V1");
                String newVersion = (String) jsonObject.get("V2");
                boolean ingest = (Boolean) jsonObject.get("ingest");
                JSONArray ccs = (JSONArray) jsonObject.get("CCs");
                if (oldVersion == null || newVersion == null || ccs == null) {
                    throw new ParseException(-1);
                }
                ///
                Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(propFile));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ///
                ChangesDetector detector = new ChangesDetector(properties);
                if (ingest) {
                    detector.detectSimpleChanges(oldVersion, newVersion);
                }
                String[] cChanges = {};
                if (ccs.size() > 0) {
                    cChanges = new String[ccs.size()];
                    for (int i = 0; i < ccs.size(); i++) {
                        cChanges[i] = (String) ccs.get(i);
                    }
                }
                detector.detectComplexChanges(oldVersion, newVersion, cChanges);
                String json = "{ \"Success\" : true, "
                        + "\"Message\" : \"Change detection among versions <" + oldVersion + ">, <" + newVersion + "> was executed. \" }";
                return Response.status(200).entity(json).build();
            }
        } catch (ParseException ex) {
            String message = "JSON input message could not be parsed.";
            String json = "{ \"Success\" : false, "
                    + "\"Message\" : \"" + message + "\" }";
            return Response.status(400).entity(json).build();
        }
    }

    /**
     * <b>GET</b> method which applies SPARQL queries on the ontology of changes and returns the results in various formats. <br>
     * <b>URL:</b> /diachron/change_detection?query={query1}&format={format1}
     * @param <b>query</b> Query parameter which has a string value representing the requested SPARQL query.
     * @param <b>format</b> Query parameter which refers on the requested format of the results. The formats which are 
     * supported are: <b>xml</b>, <b>csv</b>, <b>tsv</b>, <b>json.</b>
     * @return A Response instance which has a JSON-encoded entity content with the query results in the requested format. 
     * We discriminate the following cases: <br>
     * <ul>
     * <li> Error code: <b>400</b> if the given SPARQL query contains syntax errors or there was an internal server issue. In any case, 
     * the entity content explains the problem's category.
     * <li> Error code: <b>200</b> and entity content with the query results with the requested format.
     * <li> Error code: <b>406</b> and entity content "Invalid results format given." if the requested query results format is not 
     * recognized. 
     * </ul>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryChangesOntologyGet(@QueryParam("query") String query,
            @QueryParam("format") String format) {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            prop.load(new FileInputStream(propFile));
            String ip = prop.getProperty("Repository_IP");
            String username = prop.getProperty("Repository_Username");
            String password = prop.getProperty("Repository_Password");
            String changesOntol = prop.getProperty("Changes_Ontology");
            int port = Integer.parseInt(prop.getProperty("Repository_Port"));
            SesameVirtRep sesame = new SesameVirtRep(ip, port, username, password);
            query = query.replace(" where ", " from <" + changesOntol + "> ");
            TupleQuery tupleQuery = sesame.getCon().prepareTupleQuery(QueryLanguage.SPARQL, query);
            output = new OutputStream() {

                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            TupleQueryResultHandler writer;
            switch (format) {
                case "xml":
                    writer = new SPARQLResultsXMLWriter(output);
                    break;
                case "csv":
                    writer = new SPARQLResultsCSVWriter(output);
                    break;
                case "tsv":
                    writer = new SPARQLResultsTSVWriter(output);
                    break;
                case "json":
                    writer = new SPARQLResultsJSONWriter(output);
                    break;
                default:
                    String result = "Invalid results format given.";
                    return Response.status(406).entity(result).build();
            }
            tupleQuery.evaluate(writer);
        } catch (MalformedQueryException | QueryEvaluationException | TupleQueryResultHandlerException | RepositoryException ex) {
            ex.printStackTrace();
            return Response.status(400).entity(ex.toString()).build();
        } catch (IOException ex) {
            ex.printStackTrace();
            return Response.status(400).entity(ex.toString()).build();
        }
        return Response.status(200).entity(output.toString()).build();
    }

    /**
     * <b>POST</b> method which applies SPARQL queries on the ontology of changes and returns the results in various formats. <br>
     * <b>URL (partial):</b> /diachron/change_detection/query
     * @param <b>inputMessage</b> : A JSON-encoded string which has the following form: <br>
     * { <br>
     * "Query" : "select ?s ?p ...", <br>
     * "Format" : "json", <br>
     * } <br>
     * where
     * <ul>
     * <li>Query - A string which represents the SPARQL query which will be applied.<br>
     * <li>Format - The format(MIME type) of the query results. The formats which are 
     * supported are: <b>xml</b>, <b>csv</b>, <b>tsv</b>, <b>json.</b>
     * </ul>
     * @return A Response instance which has a JSON-encoded entity content with the query results in the requested format. 
     * We discriminate the following cases: <br>
     * <ul>
     * <li> Error code: <b>400</b> if the given SPARQL query contains syntax errors or there was an internal server issue. In any case, 
     * the entity content explains the problem's category.
     * <li> Error code: <b>200</b> and entity content with the query results with the requested format.
     * <li> Error code: <b>406</b> and entity content "Invalid results format given." if the requested query results format is not 
     * recognized. 
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message should have 
     * exactly 2 arguments." } if the input parameter has more than two JSON parameters. 
     * <li> Error code: <b>400</b> and entity content: { "Success" : false, "Message" : "JSON input message could not be parsed." } 
     * if the input parameter has not the correct form. 
     * </ul>
     */
    @POST
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response queryChangesOntologyPost(String inputMessage) {
        JSONParser jsonParser = new JSONParser();
        OutputStream output = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputMessage);
            if (jsonObject.size() != 2) {
                String message = "JSON input message should have exactly 2 arguments.";
                String json = "{ \"Success\" : false, "
                        + "\"Message\" : \"" + message + "\" }";
                return Response.status(400).entity(json).build();
            } else {
                String query = (String) jsonObject.get("Query");
                String format = (String) jsonObject.get("Format");
                if (format == null || query == null) {
                    throw new ParseException(-1);
                }
                ///
                Properties prop = new Properties();
                prop.load(new FileInputStream(propFile));
                String ip = prop.getProperty("Repository_IP");
                String username = prop.getProperty("Repository_Username");
                String password = prop.getProperty("Repository_Password");
                String changesOntol = prop.getProperty("Changes_Ontology");
                int port = Integer.parseInt(prop.getProperty("Repository_Port"));
                SesameVirtRep sesame = new SesameVirtRep(ip, port, username, password);
                query = query.replace(" where ", " from <" + changesOntol + "> ");
                TupleQuery tupleQuery = sesame.getCon().prepareTupleQuery(QueryLanguage.SPARQL, query);
                output = new OutputStream() {

                    private StringBuilder string = new StringBuilder();

                    @Override
                    public void write(int b) throws IOException {
                        this.string.append((char) b);
                    }

                    public String toString() {
                        return this.string.toString();
                    }
                };
                ///
                TupleQueryResultHandler writer;
                switch (format) {
                    case "xml":
                        writer = new SPARQLResultsXMLWriter(output);
                        break;
                    case "csv":
                        writer = new SPARQLResultsCSVWriter(output);
                        break;
                    case "tsv":
                        writer = new SPARQLResultsTSVWriter(output);
                        break;
                    case "json":
                        writer = new SPARQLResultsJSONWriter(output);
                        break;
                    default:
                        String result = "Invalid results format given.";
                        return Response.status(406).entity(result).build();
                }
                tupleQuery.evaluate(writer);
            }
        } catch (MalformedQueryException | QueryEvaluationException | TupleQueryResultHandlerException | RepositoryException ex) {
            ex.printStackTrace();
            return Response.status(400).entity(ex.toString()).build();
        } catch (IOException ex) {
            ex.printStackTrace();
            return Response.status(400).entity(ex.toString()).build();
        } catch (ParseException ex) {
            String message = "JSON input message could not be parsed.";
            String json = "{ \"Success\" : false, "
                    + "\"Message\" : \"" + message + "\" }";
            return Response.status(400).entity(json).build();
        }
        return Response.status(200).entity(output.toString()).build();
    }
}
