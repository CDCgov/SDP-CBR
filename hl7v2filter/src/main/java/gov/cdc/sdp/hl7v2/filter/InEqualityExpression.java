package gov.cdc.sdp.hl7v2.filter;

import java.util.Iterator;
import java.util.List;

public class InEqualityExpression extends ExpressionEvaluator {
  String operator;

  public InEqualityExpression(Expression left, Expression right, String operator) {
    super(left, right);
    this.operator = operator;
  }

  public Object evaluate(Context ctx) {
    List list = (List) super.evaluate(ctx);
    if (list == null) {
      return false;
    }
    boolean ret = false;
    for (Iterator iter = list.iterator(); iter.hasNext();) {
      Object object = iter.next();
      if (object != null && object instanceof Boolean && !(Boolean) object) {
        continue;
      }
      ret = true;
    }
    return ret;
  }

  protected Boolean evaluateValue(Object lv, Object rv) {
    if (lv == null || rv == null) {
      return compareNULL(lv, rv);
    } else if (lv instanceof Boolean || rv instanceof Boolean) {
      return compareBoolean(lv, rv);
    } else if (lv instanceof DateTime && rv instanceof DateTime) {
      return compare((DateTime) lv, (DateTime) rv);
    } else if (lv instanceof Time && rv instanceof Time) {
      return compare((Time) lv, (Time) rv);
    } else if (lv instanceof Number || rv instanceof Number) {
      return compare(parseNumber(lv), parseNumber(rv));
    }
    return compare(lv.toString(), rv.toString());
  }

  private Boolean compare(Comparable left, Comparable right) {
    if (left == null || right == null) {
      return false;
    }
    int comp = left.compareTo(right);
    return compareResult(comp);
  }

  protected Boolean compareNULL(Object lv, Object rv) {
    return false;
  }

  protected Boolean compareBoolean(Object lv, Object rv) {
    return false;
  }

  private Boolean compareResult(int comp) {
    Boolean value = false;
    switch (operator) {
      case ">":
        value = comp > 0;
        break;
      case "<":
        value = comp < 0;
        break;
      case "<=":
        value = comp <= 0;
        break;
      case ">=":
        value = comp >= 0;
        break;
      case "=":
        value = comp == 0;
        break;
      case "!=":
        value = comp != 0;
        break;
      default:
        value = false;
    }
    return value;
  }

  private Double parseNumber(Object obj) {
    if (obj instanceof Number) {
      return ((Number) obj).doubleValue();
    }
    try {
      return Double.parseDouble(obj.toString());
    } catch (NumberFormatException nfe) {
      // Exception not currently handled
    }
    return null;
  }

}
