package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.model.Message;

import java.util.Iterator;
import java.util.List;

public class Filter {

  Expression expression;

  public Filter(Expression expression) {
    this.expression = expression;
  }

  public boolean evaluate(Message msg) {
    Context ctx = new Context();
    ctx.set("MESSAGE", msg);
    return isTrue(expression.evaluate(ctx));
  }

  protected boolean isTrue(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof Boolean) {
      return (Boolean) obj;
    }
    if (obj instanceof List) {
      // if anything in the list evals to true return true.
      boolean ret = false;
      for (Iterator it = ((List) obj).iterator(); it.hasNext();) {
        ret = isTrue(it.next());
        if (ret) {
          break;
        }
      }
      return ret;
    }
    // basically a non null object means we found something that the filter picked
    // up
    // so its true.
    return true;
  }
}
