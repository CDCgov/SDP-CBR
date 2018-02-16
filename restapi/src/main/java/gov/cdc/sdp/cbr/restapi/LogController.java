package gov.cdc.sdp.cbr.restapi;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.trace.TraceService;

@Controller
public class LogController {
	
    
    @Autowired
    private TraceService traceService;
    
    @RequestMapping(value="/cbr/log/{cbrId}", method = RequestMethod.GET)
    @ResponseBody
    public String getTraceLog(
            @PathVariable("cbrId") String cbrId) throws InvalidObjectException, SQLException  {

    	Gson gson = new Gson();
        return gson.toJson(traceService.getTrace(cbrId));
    }
    
    @RequestMapping(value="/cbr/log", method = RequestMethod.GET)
    @ResponseBody
    public String getTraceLogByIdAndSource(
            @RequestParam("id") String id,
            @RequestParam("source") String source) throws InvalidObjectException, SQLException  {
        
    	//String cbrId = "CBR_" + source + "_" + id;
    
        Gson gson = new Gson();
        return gson.toJson(traceService.getTrace(id, source));
    }
    
    @RequestMapping(value="/cbr/batchlog/{batchId}", method = RequestMethod.GET)
    @ResponseBody
    public String getBatchTraceLog(
            @PathVariable("batchId") String batchId) throws InvalidObjectException, SQLException  {

    	Gson gson = new Gson();
        return gson.toJson(traceService.getTrace(batchId));
    }
    
    @RequestMapping(value="/cbr/batchlog", method = RequestMethod.GET)
    @ResponseBody
    public String getBatchTraceLogByIdAndSource(
            @RequestParam("batchId") String batchId,
            @RequestParam("source") String source) throws InvalidObjectException, SQLException  {

    	Gson gson = new Gson();
        return gson.toJson(traceService.getTrace(batchId, source));
    }

    @RequestMapping(value="/cbr/status/{cbrId}", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageStatus(
            @PathVariable("cbrId") String cbrId) throws InvalidObjectException, SQLException  {

    	Gson gson = new Gson();
        return gson.toJson(traceService.getStatus(cbrId));
    }
    
    @RequestMapping(value="/cbr/batchstatus/{batchId}", method = RequestMethod.GET)
    @ResponseBody
    public String getBatchMessageStatus(
            @PathVariable("batchId") String batchId) throws SQLException, IOException  {

    	Gson gson = new Gson();
        return gson.toJson(traceService.getStatus(batchId));
    }
    
    
    // NOTE: this message uses the source and source id for the lookup, instead of the generated CBR_ID
    // TODO: Finish implementing
    @RequestMapping(value="/cbr/status", method = RequestMethod.GET)
    @ResponseBody
    public String getMessageStatus(
            @RequestParam("id") String id,
            @RequestParam("source") String source) throws InvalidObjectException, SQLException  {
        
    	//String cbrId = "CBR_" + source + "_" + id;
    
        Gson gson = new Gson();
        return gson.toJson(traceService.getStatus(id, source));
    }
    
    @RequestMapping(value="/cbr/batchstatus", method = RequestMethod.GET)
    @ResponseBody
    public String getBatchStatus(
            @RequestParam("id") String id,
            @RequestParam("source") String source) throws InvalidObjectException, SQLException  {
        
    
        Gson gson = new Gson();
        return gson.toJson(traceService.getStatus(id, source));
    }
    

}
