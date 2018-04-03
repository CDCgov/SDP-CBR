package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ExpressionEvaluator implements Expression {
  Expression leftExpression;
  Expression rightExpression;

  public ExpressionEvaluator(Expression left, Expression right) {
    this.leftExpression = left;
    this.rightExpression = right;
  }

  @Override
  public Object evaluate(Context ctx) {
    Object left = leftExpression.evaluate(ctx);
    Object right = rightExpression.evaluate(ctx);
    List values = evaluate(toList(left), toList(right));
    return values;
  }

  protected List evaluate(List left, List right) {
    List values = new ArrayList();
    if (left == null && right == null) {
      values.add(evaluateValue(left, right));
      return values;
    }

    if (left == null || (left instanceof List && ((List) left).size() == 0)) {
      if (right == null) {
        values.add(evaluateValue(null, null));
      } else {
        for (Iterator iter = right.iterator(); iter.hasNext();) {
          Object value = evaluateValue(null, iter.next());
          if (value != null) {
            values.add(value);
          }
        }
      }
    } else if (right == null || (right instanceof List && ((List) right).size() == 0)) {
      if (left == null) {
        values.add(evaluateValue(null, null));
      } else {
        for (Iterator iter = left.iterator(); iter.hasNext();) {
          Object value = evaluateValue(iter.next(), null);
          if (value != null) {
            values.add(value);
          }
        }
      }
    } else {
      for (Iterator leftIter = left.iterator(); leftIter.hasNext();) {
        for (Iterator rightIter = right.iterator(); rightIter.hasNext();) {
          Object value = evaluateValue(leftIter.next(), rightIter.next());
          if (value != null) {
            values.add(value);
          }
        }
      }
    }
    return values;
  }

  protected abstract Object evaluateValue(Object left, Object right);

  protected List toList(Object obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof List) {
      return (List) obj;
    }
    List lst = new ArrayList();
    lst.add(obj);
    return lst;
  }

  protected String objectValue(Object obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof Primitive) {
      return ((Primitive) obj).getValue();
    } else if (obj instanceof Type) {
      try {
        return ((Type) obj).encode();
      } catch (HL7Exception exception) {
        // Exception not currently handled
      }
    } else if (obj instanceof Segment) {
      try {
        return ((Segment) obj).encode();
      } catch (HL7Exception exception) {
        // Exception not currently handled
      }
    } else if (obj instanceof Message) {
      try {
        return ((Message) obj).encode();
      } catch (HL7Exception exception) {
        // Exception not currently handled
      }
    }
    return obj.toString();
  }

}
