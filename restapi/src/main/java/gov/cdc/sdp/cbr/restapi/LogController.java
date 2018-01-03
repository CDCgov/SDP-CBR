package gov.cdc.sdp.cbr.restapi;

import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.trace.TraceService;
import gov.cdc.sdp.cbr.trace.model.TraceLog;

@Controller
public class LogController {
    
    @Autowired
    private TraceService traceService;
    
    @RequestMapping(value="/cbr/log/{cbrId}", method = RequestMethod.GET)
    @ResponseBody
    public String getTraceLog(
            @PathVariable("cbrId") String cbrId) throws InvalidObjectException, SQLException  {

     // TODO: Find a way to do this with batch messages.
        // TODO: Add a version with source and source id
      Gson gson = new Gson();
        return gson.toJson(traceService.getTrace(cbrId));
    }
    

    // TODO: Finish implementing
    @RequestMapping(value="/cbr/status/{cbrId}", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageStatus(
            @PathVariable("cbrId") String cbrId) throws InvalidObjectException, SQLException  {
        //String cbrId = "CBR_" + source + "_" + id;
     //   return cbrId;
        
        // TODO: TEST
        // TODO: Find a way to do this with batch messages.

      Gson gson = new Gson();
        return gson.toJson(traceService.getStatus(cbrId));
    }
    
    
    // NOTE: this message uses the source and source id for the lookup, instead of the generated CBR_ID
    // TODO: Finish implementing
    @RequestMapping(value="/cbr/status", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageStatus(
            @RequestParam("id") String id,
            @RequestParam("source") String source) throws InvalidObjectException, SQLException  {
        String cbrId = "CBR_" + source + "_" + id;
     //   return cbrId;
        
        // TODO: TEST
        // TODO: Find a way to do this with batch messages.

      Gson gson = new Gson();
        return gson.toJson(traceService.getStatus(cbrId));
    }
    
    

}
