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

@Controller
public class InputController {

    @Autowired
    protected CamelContext camelContext;
    
    @Value("${input.post.endpoint}")
    public String endpoint;
    
    @RequestMapping(value="/cbr/input", method = RequestMethod.POST)
    
    public @ResponseBody String processInputFile (
                @RequestParam("id") String id,
                @RequestParam("source") String source,
                // TODO: Add createdAt, change createdAt in transformer to receivedAt?
                @RequestParam("metadata") String jsonMetadata,
                @RequestParam("file") MultipartFile file)  {
        
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
