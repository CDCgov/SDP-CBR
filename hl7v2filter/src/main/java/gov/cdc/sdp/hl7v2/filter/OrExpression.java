package gov.cdc.sdp.hl7v2.filter;

import java.util.Iterator;
import java.util.List;

public class OrExpression extends ExpressionEvaluator {

  Expression left;
  Expression right;

  public OrExpression(Expression left, Expression right) {
    super(left, right);
  }

  protected Object evaluateValue(Object left, Object right) {
    if (left == null && right == null) {
      return false;
    }
    if (left == null || right == null) {
      Object object = (left == null) ? right : left;
      if (object instanceof Boolean) {
        return object;
      } else {
        return true;
      }
    }

    if (left instanceof Boolean && right instanceof Boolean) {
      return (Boolean) left || (Boolean) right;
    }

    return true;
  }

  public Object evaluate(Context ctx) {
    List lst = (List) super.evaluate(ctx);
    if (lst == null) {
      return false;
    }
    for (Iterator iter = lst.iterator(); iter.hasNext();) {
      Object obj = iter.next();
      if (obj instanceof Boolean && (Boolean) obj) {
        return true;
      }
    }
    return false;
  }
}
