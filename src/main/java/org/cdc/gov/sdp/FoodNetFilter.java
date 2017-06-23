package org.cdc.gov.sdp;

import org.apache.camel.Exchange;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Type;

public class FoodNetFilter {

	private static final String TARGET_HEADER = "MSH";
	private static final int TARGET_FIELD = 21;
	private static final String TARGET_VALUE = "FDD_MMG_V1.0";

	public boolean filter(Exchange exchange) {
		boolean result = false;
		Message in = exchange.getIn().getBody(Message.class);

		try {
			Segment msh_structure = (Segment) in.get(TARGET_HEADER);
			Type[] types = msh_structure.getField(TARGET_FIELD);

			for (Type t : types) {
				if (t.encode().startsWith(TARGET_VALUE))
					result = true;
			}

		} catch (HL7Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
