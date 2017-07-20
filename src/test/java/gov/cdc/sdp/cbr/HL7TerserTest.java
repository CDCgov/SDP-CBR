package gov.cdc.sdp.cbr;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Test;

import gov.cdc.sdp.cbr.HL7Terser;
import gov.cdc.sdp.cbr.common.SDPTestBase;


public class HL7TerserTest extends SDPTestBase {

	@Test
	public void testFilterSingleTermEquals() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "PID-5-1 EQUALS SMITH");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "PID-5-1 EQUALS SMITH2");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "PID-5-1 EQUALS SMI");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterSingleTermContains() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 CONTAINS MI");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertTrue(new HL7Terser().filter(exchange));

		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 CONTAINS JONES");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterSingleTermEqualsWithNot() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 NOT_EQUALS JONES");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertTrue(new HL7Terser().filter(exchange));

		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 NOT_EQUALS SMITH");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterSingleTermContainsWithNot() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 NOT_CONTAINS JJ");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertTrue(new HL7Terser().filter(exchange));

		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 NOT_CONTAINS MI");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertFalse(new HL7Terser().filter(exchange));

		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "PID-5-1 NOT_CONTAINS SMITH");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterAndTwoTermsBothEquals() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS SMITH] AND [PID-5-1 EQUALS SMITH]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS SMITH] AND [PID-5-1 EQUALS JONES]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS JONES] AND [PID-5-1 EQUALS SMITH]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterOrTwoTermsBothEquals() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS SMITH] OR [PID-5-1 EQUALS JONES]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS JONES] OR [PID-5-1 EQUALS SMITH]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS JONES] OR [PID-5-1 EQUALS JONES]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterTwoTermsWithNot() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS SMITH] AND [PID-5-1 NOT_EQUALS JONES]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 NOT_EQUALS JONES] OR [PID-5-1 NOT_EQUALS SMITH]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 NOT_EQUALS SMITH] OR [PID-5-1 CONTAINS MI]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 NOT_EQUALS SMITH] OR [PID-5-1 NOT_CONTAINS MI]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterTwoTermsWithRepetition() throws IOException {
		String sourceFile = "src/test/resources/hl7v2_obx.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "*([OBSERVATION(*)/OBX-3-2 CONTAINS Pregnancy] AND [OBSERVATION(*)/OBX-5-1 EQUALS N])");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "*([OBSERVATION(*)/OBX-3-2 CONTAINS Pregnancy] AND [OBSERVATION(*)/OBX-5-1 EQUALS Y])");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterThreeTerms() throws IOException {
		String sourceFile = "src/test/resources/hl7v2.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS SMITH] AND [[PID-5-2 EQUALS JOHN] OR [PID-5-2 EQUALS JON]]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[PID-5-1 EQUALS SMITH] AND [[PID-5-2 EQUALS JOHN] AND [PID-5-2 EQUALS JON]]");
		msg.setBody(readFile(sourceFile));

		exchange.setIn(msg);
		
		assertFalse(new HL7Terser().filter(exchange));
	}
	
	@Test
	public void testFilterComplex() throws IOException {
		String sourceFile = "src/test/resources/hl7v2_obx.txt";
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange exchange = new DefaultExchange(ctx);
		Message msg = new DefaultMessage();
		
		msg.setHeader("HL7Filter", "[[PID-5-1 EQUALS JONES] AND [PID-5-2 EQUALS MARY]] AND "
				+ "[*([OBSERVATION(*)/OBX-3-2 CONTAINS Pregnancy] AND [OBSERVATION(*)/OBX-5-1 EQUALS N]) OR "
				+ "*([OBSERVATION(*)/OBX-3-2 CONTAINS Patient Age] AND [OBSERVATION(*)/OBX-5-1 MINIMUM 50])]");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertTrue(new HL7Terser().filter(exchange));
		
		exchange = new DefaultExchange(ctx);
		msg = new DefaultMessage();
		msg.setHeader("HL7Filter", "[[PID-5-1 EQUALS JONES] AND [PID-5-2 EQUALS MARY]] AND "
				+ "[*([OBSERVATION(*)/OBX-3-2 CONTAINS Pregnancy] AND [OBSERVATION(*)/OBX-5-1 EQUALS Y]) OR "
				+ "*([OBSERVATION(*)/OBX-3-2 CONTAINS Patient Age] AND [OBSERVATION(*)/OBX-5-1 MINIMUM 50])]");
		msg.setBody(readFile(sourceFile));
		exchange.setIn(msg);
		assertFalse(new HL7Terser().filter(exchange));
		
	}
}

