package gov.cdc.sdp.hl7v2.filter;

public class NotExpression implements Expression {

  Expression exp;

  public NotExpression(Expression exp) {
    this.exp = exp;
  }

  public Object evaluate(Context ctx) {
    Object value = exp.evaluate(ctx);
    if (value == null) {
      return true;
    } else if (value instanceof Boolean) {
      return !(Boolean) value;
    }
    return false;
  }
}
