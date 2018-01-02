package gov.cdc.sdp.cbr.restapi;

import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public String doSomething(
            @PathVariable("cbrId") String cbrId) throws InvalidObjectException, SQLException  {
        //String cbrId = "CBR_" + source + "_" + id;
     //   return cbrId;

      Gson gson = new Gson();
        return gson.toJson(traceService.getTrace(cbrId));
    }

}
