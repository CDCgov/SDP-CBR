package gov.cdc.sdp.cbr;

import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class SpringBootCamelFix implements SmartLifecycle { 

    private static final Logger log = LoggerFactory.getLogger(SpringBootCamelFix.class); 

    @Autowired 
    DefaultCamelContext context; 


    @Override 
    public void start() {} 

    @Override 
    public void stop() { 

//            if (!isRunning()) { 
//                    log.info("Camel context already stopped"); 
//                    return; 
//            } 

            log.info("Stopping camel context. Will wait until it is actually stopped"); 

            try { 
                    context.stop(); 
            } catch (Exception e) { 
                    e.printStackTrace(); 
            } 

            while(context.isStarted()); 
    } 

    @Override 
    public boolean isRunning() { 
            return context.isStarted(); 
    } 

    @Override 
    public int getPhase() { 
            return Integer.MAX_VALUE; 
    } 

    @Override 
    public boolean isAutoStartup() { 
            return false; 
    } 

    @Override 
    public void stop(Runnable callback) { 
            stop(); 
            callback.run(); 
    } 
} 