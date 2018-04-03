package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.util.SegmentFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PathExpression implements Expression {

  List<GroupPath> groupPaths;
  SegmentPath segPath;
  String start = "";

  public PathExpression(List<GroupPath> groups, SegmentPath segPath) {
    this.groupPaths = groups;
    this.segPath = segPath;
  }

  public PathExpression(List<GroupPath> groups, SegmentPath segPath, String start) {
    this.groupPaths = groups;
    this.segPath = segPath;
    this.start = start;
  }

  public void setStartPath(String start) {
    this.start = start;
  }

  public String getStartPath() {
    return start;
  }

  public String executeGroupExpression() {
    return null;
  }

  public Object evaluate(Context ctx) {
    Group message = (Group) ctx.get("MESSAGE");
    List<Group> list = new ArrayList();
    list.add(message);
    return evaluateSegment(evaluateGroups(list, ctx), ctx);
  }

  protected List<Group> evaluateGroups(List groups, Context parent) {
    List<Group> list = new ArrayList<>();
    return evaluateGroupPath(0, groups, parent);
  }

  protected List<Group> evaluateGroupPath(int index, List<Group> groups, Context parent) {
    if (groupPaths == null || groupPaths.size() < index + 1) {
      return groups;
    }
    GroupPath gp = groupPaths.get(index);
    List<Group> list = new ArrayList<>();
    for (Iterator<Group> iter = groups.iterator(); iter.hasNext();) {
      SegmentFinder finder = new SegmentFinder(iter.next());
      Context ctx = new Context(parent);
      ctx.set("FINDER", finder);
      List<Group> vals = (List<Group>) gp.evaluate(ctx);
      if (vals != null) {
        list.addAll(vals);
      }
    }
    return evaluateGroupPath(index + 1, list, parent);
  }

  protected List evaluateSegment(List<Group> groups, Context parent) {
    if (this.segPath == null) {
      return groups;
    }
    List list = new ArrayList<>();
    for (Iterator<Group> iter = groups.iterator(); iter.hasNext();) {
      SegmentFinder finder = new SegmentFinder(iter.next());
      Context ctx = new Context(parent);
      ctx.set("FINDER", finder);
      List vals = (List) segPath.evaluate(ctx);
      if (vals != null) {
        list.addAll(vals);
      }
    }
    return list;
  }
  //
  //
  // public Segment getSegment(String segSpec) throws HL7Exception {
  // Segment seg = null;
  //
  // if (segSpec.startsWith("/")) {
  // getFinder().reset();
  // }
  //
  // StringTokenizer tok = new StringTokenizer(segSpec, "/", false);
  // SegmentFinder finder = getFinder();
  // while (tok.hasMoreTokens()) {
  // String pathSpec = tok.nextToken();
  // Terser.PathSpec ps = parsePathSpec(pathSpec);
  // ps.isGroup = tok.hasMoreTokens();
  // if (ps.isGroup) {
  // Group g = ps.find ?
  // finder.findGroup(ps.pattern, ps.rep) :
  // finder.getGroup(ps.pattern, ps.rep);
  // finder = new SegmentFinder(g);
  // } else {
  // seg = ps.find ?
  // finder.findSegment(ps.pattern, ps.rep) :
  // finder.getSegment(ps.pattern, ps.rep);
  // }
  // }
  //
  // return seg;
  // }
}
