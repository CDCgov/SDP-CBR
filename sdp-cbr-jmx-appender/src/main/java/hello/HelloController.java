package hello;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/")
    public String index() {
    	
    	LOG.info("Request received!");
   	
    	return "Greetings from Spring Boot!";
    }
    
}
