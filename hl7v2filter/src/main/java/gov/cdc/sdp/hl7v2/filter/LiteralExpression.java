package gov.cdc.sdp.hl7v2.filter;

public class LiteralExpression implements Expression {

  Object value;

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public LiteralExpression(Object value) {
    this.value = value;
  }

  public Object evaluate(Context ctx) {
    return value;
  }
}
