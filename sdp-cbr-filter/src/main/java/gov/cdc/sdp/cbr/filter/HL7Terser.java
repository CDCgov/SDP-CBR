package gov.cdc.sdp.cbr.filter;

import gov.cdc.sdp.cbr.CBR;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.ValueBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7Terser {
  private static final Logger LOG = LoggerFactory.getLogger(HL7Terser.class);

  private enum HL7Operation {
    EQUALS, CONTAINS, STARTS_WITH, MINIMUM, MAXIMUM

  }

  private enum LogicalOperation {
    AND, OR
  }

  private static final int MAX_REP = 100;

  public boolean filter(Exchange exchange) {
    String filter = (String) exchange.getIn().getHeader("HL7Filter");
    boolean passed = processTerm(filter.trim(), exchange);

    LOG.info("Exchange " + exchange.getIn().getHeader(CBR.ID) + " " + (passed ? "PASSED" : "FAILED") + " the filter:"
        + filter);

    return passed;
  }

  private boolean applyLogicalOperation(String logicalOperator, String term1, String term2, Exchange ex) {
    LogicalOperation op = LogicalOperation.valueOf(logicalOperator);
    switch (op) {
      case AND:
        return processTerm(term1, ex) && processTerm(term2, ex);
      case OR:
        return processTerm(term1, ex) || processTerm(term2, ex);
      default:
        return false;
    }
  }

  public boolean processTerm(String filter, Exchange exchange) {
    if (filter.startsWith("*(")) {
      assert (filter.charAt(filter.length() - 1) == ')');
      String term = filter.substring(2, filter.length() - 1);
      return processRepetitions(term, exchange);
    }
    if (filter.startsWith("[")) {
      return processComplexTerm(filter, exchange);
    }
    boolean inverse = false;
    String[] terms = filter.split(" ", 3);
    if (terms.length != 3) {
      throw new IllegalArgumentException("Filter terms must be in the form [PID-1 EQUALS VALUE].");
    }

    String expression = terms[0].startsWith("/.") ? terms[0] : "/." + terms[0];
    String operationStr = terms[1];
    String value = terms[2];

    ValueBuilder terserExpression = org.apache.camel.component.hl7.HL7.terser(expression);
    if (operationStr.startsWith("NOT_")) {
      inverse = true;
      operationStr = operationStr.substring(4);
    }
    HL7Operation operation = HL7Operation.valueOf(operationStr);
    Predicate matcher = evaluateOperation(terserExpression, operation, value);

    boolean match = matcher.matches(exchange);
    return inverse ? !match : match;
  }

  private boolean processComplexTerm(String filter, Exchange exchange) {
    int endOfFirstTerm = -1;
    int startOfSecondTerm = -1;
    int endOfSecondTerm = -1;

    int openBracketCount = 0;
    int closeBracketCount = 0;
    for (int i = 0; i < filter.length(); i++) {
      char character = filter.charAt(i);
      if (character == '[') {
        openBracketCount++;
      } else if (character == ']') {
        closeBracketCount++;
      }

      if (openBracketCount == closeBracketCount) {
        endOfFirstTerm = i;
        break;
      }
    }

    openBracketCount = 0;
    closeBracketCount = 0;
    for (int index = endOfFirstTerm + 2; index < filter.length(); index++) {
      char character = filter.charAt(index);
      if (character == '[') {
        if (startOfSecondTerm == -1) {
          startOfSecondTerm = index + 1;
        }
        openBracketCount++;
      } else if (character == ']') {
        closeBracketCount++;
      }

      if (openBracketCount == closeBracketCount && openBracketCount != 0) {
        endOfSecondTerm = index;
        break;
      }
    }

    String term1 = filter.substring(1, endOfFirstTerm);
    String logicalOperator = filter.substring(endOfFirstTerm + 1, startOfSecondTerm - 1).trim();
    String term2 = filter.substring(startOfSecondTerm, endOfSecondTerm);
    return applyLogicalOperation(logicalOperator, term1, term2, exchange);
  }

  private boolean processRepetitions(String term, Exchange exchange) {
    try {
      for (int i = 0; i < MAX_REP; i++) {
        String filterFieldWithRepetition = term.replace("*", Integer.toString(i));
        if (processTerm(filterFieldWithRepetition, exchange)) {
          return true;
        }
      }
    } catch (RuntimeCamelException exception) {
      // Exception is currently not handled
    }
    return false;
  }

  private Predicate evaluateOperation(ValueBuilder terserExpression, HL7Operation operation, String value) {
    switch (operation) {
      case EQUALS:
        return terserExpression.isEqualTo(value);
      case CONTAINS:
        return terserExpression.contains(value);
      case STARTS_WITH:
        return terserExpression.startsWith(value);
      case MINIMUM:
        return terserExpression.isGreaterThanOrEqualTo(value);
      case MAXIMUM:
        return terserExpression.isLessThanOrEqualTo(value);
      default:
        throw new IllegalArgumentException("Cannot process operation: " + operation);
    }
  }
}
