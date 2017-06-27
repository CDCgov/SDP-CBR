package org.cdc.gov.sdp;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;

public class CamelUtils {
	
	private static final int MAX_REP = 100;

	public boolean filterEquals(Exchange exchange) {
		String filterField = (String)exchange.getIn().getHeader("HL7FilterField");
		String filterValue = (String)exchange.getIn().getHeader("HL7FilterValue");
		return org.apache.camel.component.hl7.HL7.terser(filterField).isEqualTo(filterValue).matches(exchange);
	}

	public boolean filterNotEquals(Exchange exchange) {
		String filterField = (String)exchange.getIn().getHeader("HL7FilterField");
		String filterValue = (String)exchange.getIn().getHeader("HL7FilterValue");
		return org.apache.camel.component.hl7.HL7.terser(filterField).isNotEqualTo(filterValue).matches(exchange);
	}

	public boolean filterContains(Exchange exchange) {
		String filterField = (String)exchange.getIn().getHeader("HL7FilterField");
		String filterValue = (String)exchange.getIn().getHeader("HL7FilterValue");
		return org.apache.camel.component.hl7.HL7.terser(filterField).contains(filterValue).matches(exchange);
	}

	public boolean filterStartsWith(Exchange exchange) {
		String filterField = (String)exchange.getIn().getHeader("HL7FilterField");
		String filterValue = (String)exchange.getIn().getHeader("HL7FilterValue");
		return org.apache.camel.component.hl7.HL7.terser(filterField).startsWith(filterValue).matches(exchange);
	}
	
	public boolean filterEqualsWithRepetitions(Exchange exchange) {
		String filterField = (String)exchange.getIn().getHeader("HL7FilterField");
		String filterValue = (String)exchange.getIn().getHeader("HL7FilterValue");
		try {
			for (int i = 0; i < MAX_REP; i++) {
				String filterFieldWithRepetition = filterField.replace("*", Integer.toString(i));
				if (org.apache.camel.component.hl7.HL7.terser(filterFieldWithRepetition).isEqualTo(filterValue).matches(exchange)) {
					return true;
				}
			}
		} catch (RuntimeCamelException e) {}
		return false;
	}
	
	public boolean filterContainsWithRepetitions(Exchange exchange) {
		String filterField = (String)exchange.getIn().getHeader("HL7FilterField");
		String filterValue = (String)exchange.getIn().getHeader("HL7FilterValue");
		try {
			for (int i = 0; i < MAX_REP; i++) {
				String filterFieldWithRepetition = filterField.replace("*", Integer.toString(i));
				if (org.apache.camel.component.hl7.HL7.terser(filterFieldWithRepetition).contains(filterValue).matches(exchange)) {
					return true;
				}
			}
		} catch (RuntimeCamelException e) {}
		return false;
	}

}
