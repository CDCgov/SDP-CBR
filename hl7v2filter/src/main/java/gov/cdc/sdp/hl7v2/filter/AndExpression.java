package gov.cdc.sdp.hl7v2.filter;

import java.util.Iterator;
import java.util.List;

/**
 * Class that represents a logical and expression.
 */

public class AndExpression extends ExpressionEvaluator {

  /**
   * Create a logical and expression.
   * @param left the left expression
   * @param right the right expression
   */
  public AndExpression(final Expression left, final Expression right) {
    super(left, right);
  }

  /**
   * Evaluate the expression.
   */
  protected Object evaluateValue(Object left, Object right) {
    if (left == null || right == null) {
      return false;
    }
    if (left instanceof Boolean && right instanceof Boolean) {
      return (Boolean) left && (Boolean) right;
    }

    if ((left instanceof Boolean && !(Boolean) left) || (right instanceof Boolean
        && !(Boolean) right)) {
      return false;
    }

    return true;
  }

  /**
   * Evaluate the and expression.
   */
  public Object evaluate(final Context ctx) {
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
