package gov.cdc.sdp.hl7v2.filter;

public class MatchExpression extends ExpressionEvaluator {

  public MatchExpression(Expression left, Expression right) {
    super(left, right);
  }

  public Object evaluateValue(Object left, Object right) {
    String lstring = objectValue(left);
    String rstring = objectValue(right);
    if (lstring == null || rstring == null) {
      return false;
    }
    return lstring.matches(rstring);
  }
}
