package gov.cdc.sdp.cbr.restapi;

import java.util.HashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@Api(value="input", description="Operations that allow input of messages into CBR")
@RequestMapping(value="/cbr")     
public class InputController {

    @Autowired
    protected CamelContext camelContext;
    
    @Value("${input.post.endpoint}")
    public String endpoint;
    
    @RequestMapping(value="/input", method = RequestMethod.POST)   
    @ApiOperation(value = "Add a single message to the phinms queue")
    public @ResponseBody String processInputFile (
                @ApiParam(name="id", value="A unique id for the message", required=true) @RequestParam("id") String id,
                @ApiParam(name="source", value="The source sending the message", required=true) @RequestParam("source") String source,
                // TODO: Add createdAt, change createdAt in transformer to receivedAt?
                @ApiParam(name="metadata", value="A map of String key-value metadata records", required=true) @RequestParam("metadata") String jsonMetadata,
                @ApiParam(name="file", value="The payload of the message: may be a single HL7 message or a batch message", required=true) @RequestParam("file") MultipartFile file)  {
        
        String cbrId = "CBR_" + source + "_" + id;
        
        // TODO: Log message in
        // DO log message receipt even if it's a duplicate, but log that it is a duplicate
        
        // Send message to endpoint
        ProducerTemplate template = camelContext.createProducerTemplate();
        
        // send with a body and header 
            Gson gson = new Gson();
            
            @SuppressWarnings("rawtypes")
            HashMap mapMetadata = gson.fromJson(jsonMetadata, HashMap.class);
        
            Exchange ex = new ExchangeBuilder(camelContext)
                    .withBody(file)
                    .withHeader("CBR_ID", cbrId)
                    .withHeader("ORIGINATING_CBR_ID", cbrId)
                    .withHeader("sourceId", id)
                    .withHeader("source", source)
                    .withHeader("METADATA", mapMetadata).build();
           
            template.send(endpoint, ex);
                
            // TODO: Return more data in the response.
            return cbrId;
    }
    
    @ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY,
                    reason="Could not parse JSON")  // 422
    @ExceptionHandler(JsonSyntaxException.class)
    public void handleException() {
       // TODO: LOG SOMETHING
    }
    
}
