package org.mycompany;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v251.datatype.EI;
import ca.uhn.hl7v2.model.v251.segment.MSH;

public class ProfileFilter implements Processor {
	public void process(Exchange exchange) throws Exception {
		 	AbstractMessage  msg = (AbstractMessage )exchange.getIn().getBody();
		 	MSH msh = (MSH) msg.get("MSH");
		 	EI[] ei = msh.getMessageProfileIdentifier();
		 	boolean is = false;
		 	for (int i = 0; i < ei.length; i++) {
				System.out.println(ei[i].getEntityIdentifier());
				if(ei[i].getEntityIdentifier().getValue().contains("FDD_MMG")){
					is = true;
				
				}
			}
	        System.out.println("IS FDD "+is);
	    }
}
