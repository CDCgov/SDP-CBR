package gov.cdc.sdp.cbr.restapi;

import java.sql.SQLException;
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

import gov.cdc.sdp.cbr.trace.TraceService;
import gov.cdc.sdp.cbr.trace.model.TraceStatus;

@Controller
public class InputController {

    @Autowired
    protected CamelContext camelContext;
    
    @Autowired
    private TraceService traceService;
    
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
        
        try {
			traceService.addTraceMessage(cbrId, source, TraceStatus.INFO, "Message from id " + id + " received");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
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
            
        return cbrId + "/data:" + mapMetadata.toString();
    }
    
    @ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY,
                    reason="Could not parse JSON")  // 422
    @ExceptionHandler(JsonSyntaxException.class)
    public void handleException() {
    	try {
			traceService.addTraceMessage("Error 422", "", TraceStatus.ERROR, "Could not parse JSON");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
}
