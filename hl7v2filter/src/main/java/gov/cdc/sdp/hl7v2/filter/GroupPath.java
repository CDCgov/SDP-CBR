package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.primitive.DT;
import ca.uhn.hl7v2.model.primitive.TM;
import ca.uhn.hl7v2.model.primitive.TSComponentOne;
import ca.uhn.hl7v2.util.SegmentFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupPath implements Expression {
  String name;
  String rep;
  Expression constraint;

  public GroupPath(String name, String rep, Expression constraint) {
    this.name = name;
    this.rep = rep;
    this.constraint = constraint;
  }

  public Object evaluate(Context ctx) {
    List<Group> list = new ArrayList();
    SegmentFinder finder = (SegmentFinder) ctx.get("FINDER");
    if (rep == null || rep.equals("*")) {
      int index = 0;
      Group group = null;

      while ((group = evaluateRepition(index++, finder)) != null) {
        list.add(group);
      }
    } else if (rep != null) {
      try {
        Integer index = Integer.parseInt(rep);
        Group group = evaluateRepition(index, finder);
        list.add(group);
      } catch (NumberFormatException nfe) {
        // Exception not currently handled
      }
    } else {
      Group group = evaluateRepition(0, finder);
      list.add(group);
    }

    return evaluateConstraint(list, ctx);
  }

  private List evaluateConstraint(List<Group> groups, Context parent) {
    if (constraint == null) {
      return groups;
    }
    List<Group> list = new ArrayList();
    for (Iterator<Group> iter = groups.iterator(); iter.hasNext();) {
      Group group = iter.next();
      SegmentFinder finder = new SegmentFinder(group);
      Context ctx = new Context(parent);
      ctx.set("MESSAGE", group);
      ctx.set("FINDER", finder);
      Object object = constraint.evaluate(ctx);
      if (object == null) {
        continue;
      }
      if (object instanceof Boolean) {
        if ((Boolean) object) {
          list.add(group);
        }
      } else if (object instanceof List) {
        if (((List) object).size() > 0) {
          list.add(group);
        }
      } else if (object != null) {
        list.add(group);
      }
    }
    return list;
  }

  private Group evaluateRepition(int index, SegmentFinder finder) {
    try {
      finder.reset();
      Group group = finder.findGroup(name, index);

      return group.isEmpty() ? null : group;
    } catch (HL7Exception exception) {
      // Exception currently not handled
    }
    return null;
  }

}
