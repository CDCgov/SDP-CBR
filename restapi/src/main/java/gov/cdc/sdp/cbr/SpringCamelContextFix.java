package gov.cdc.sdp.cbr;

import javax.inject.Inject;

import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component 
public class SpringCamelContextFix implements ApplicationListener<ApplicationEvent> {

	@Inject
	private SpringCamelContext camelContext;

	public SpringCamelContextFix(SpringCamelContext camelContext) {
		this.camelContext = camelContext;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		camelContext.onApplicationEvent(event);
	}

}

