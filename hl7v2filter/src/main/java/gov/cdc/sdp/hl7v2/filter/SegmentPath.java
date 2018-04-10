package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.GenericComposite;
import ca.uhn.hl7v2.model.GenericPrimitive;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.primitive.DT;
import ca.uhn.hl7v2.model.primitive.TM;
import ca.uhn.hl7v2.model.primitive.TSComponentOne;
import ca.uhn.hl7v2.util.SegmentFinder;

import java.util.ArrayList;
import java.util.List;

public class SegmentPath implements Expression {

  String name;
  String rep;
  String fieldRep;
  int field;
  int component;
  int subcomponent;

  String segment;
  boolean find = false;

  public SegmentPath(String name, String rep, String fieldRep, int field,
      int component, int subcomponent) {
    this.name = name;
    this.rep = rep == null ? "0" : rep;
    this.fieldRep = fieldRep == null ? "0" : fieldRep;
    this.field = field;
    this.segment = name;
    this.component = component;
    this.subcomponent = subcomponent;
    this.find = true;
    if (this.segment.contains("*") || this.segment.contains("?")) {
      this.find = true;
    }
  }

  public Object evaluate(Context ctx) {
    List lst = new ArrayList();
    SegmentFinder finder = (SegmentFinder) ctx.get("FINDER");
    if (this.rep != null && this.rep.equals("*")) {
      int index = 0;
      List obj = null;
      while ((obj = evaluateRepition(index, finder)) != null) {
        lst.addAll(obj);
        index++;
      }
    } else {
      lst.addAll(evaluateRepition(0, finder));
    }
    return lst;
  }

  private List evaluateRepition(int segmentIndex, SegmentFinder finder) {
    finder.reset();
    List vals = new ArrayList();
    try {
      Segment seg = this.find ? finder.findSegment(segment, segmentIndex)
          : finder.getSegment(segment, segmentIndex);

      if (seg == null || seg.isEmpty()) {
        return null;
      }
      if (field == -1) {
        vals.add(seg);
        return vals;
      }

      if (fieldRep != null && fieldRep.equals("*")) {
        Primitive object = null;
        int index = 0;

        while ((object = getPrimitive(seg, field, index++, component, subcomponent)) != null 
            && object.getValue() != null && !object.isEmpty()) {
          vals.add(convert(object));
        }
      } else {
        Primitive object = getPrimitive(seg, field, Integer.parseInt(fieldRep),
            component, subcomponent);
        if (object != null && !object.isEmpty()) {
          vals.add(convert(object));
        }
      }

      return vals;
    } catch (HL7Exception exception) {
      // Exception currently not handled
    }
    return null;
  }

  private Object convert(Object object) throws DataTypeException {
    if (object instanceof TSComponentOne) {
      object = parseTS((TSComponentOne) object);
    } else if (object instanceof DT) {
      object = parseDT((DT) object);
    } else if (object instanceof TM) {
      object = parseTM((TM) object);
    } else {
      object = object.toString();
    }
    return object;
  }

  private DateTime parseTS(TSComponentOne timestamp) throws DataTypeException {
    DateTime dateTime = new DateTime();
    dateTime.setYear(timestamp.getYear());
    dateTime.setMonth(timestamp.getMonth());
    dateTime.setDay(timestamp.getDay());
    dateTime.setHour(timestamp.getHour());
    dateTime.setMinute(timestamp.getMinute());
    dateTime.setSecond(timestamp.getSecond());
    dateTime.setMillisecond((int) (timestamp.getFractSecond() / .000000001));
    return dateTime;
  }

  private DateTime parseDT(DT value) throws DataTypeException {
    DateTime dateTime = new DateTime();
    dateTime.setYear(value.getYear());
    dateTime.setMonth(value.getMonth());
    dateTime.setDay(value.getDay());
    return dateTime;
  }

  private Time parseTM(TM value) throws DataTypeException {
    Time time = new Time();
    time.setHour(value.getHour());
    time.setMinute(value.getMinute());
    time.setSecond(value.getSecond());
    time.setMillisecond((int) (value.getFractSecond() / .000000001));
    return time;

  }

  protected String get(Segment segment, int field, int rep, int component, int subcomponent)
      throws HL7Exception {
    if (segment == null) {
      throw new NullPointerException("segment may not be null");
    }
    if (rep < 0) {
      throw new IllegalArgumentException("rep must not be negative");
    }
    if (component < 1) {
      throw new IllegalArgumentException(
          "component must not be 1 or more (note that this parameter is 1-indexed, not 0-indexed)");
    }
    if (subcomponent < 1) {
      throw new IllegalArgumentException(
          "subcomponent must not be 1 or more (note that this parameter is 1-indexed, not 0-indexed)");
    }

    Primitive prim = getPrimitive(segment, field, rep, component, subcomponent);
    return prim.getValue();
  }

  /**
   * Returns the Primitive object at the given location.
   */
  private static Primitive getPrimitive(Segment segment, int field, int rep, int component, int subcomponent)
      throws HL7Exception {
    Type type = segment.getField(field, rep);
    return getPrimitive(type, component, subcomponent);
  }

  /**
   * Returns the Primitive object at the given location in the given field. It is
   * intended that the given type be at the field level, although extra components
   * will be added blindly if, for example, you provide a primitive subcomponent
   * instead and specify component or subcomponent > 1
   *
   * @param type
   *          the type from which to get the primitive
   * @param component
   *          the component number (indexed from 1, use 1 for primitive field)
   * @param subcomponent
   *          the subcomponent number (indexed from 1, use 1 for primitive
   *          component)
   * @return the Primitive object at the given location
   */
  public static Primitive getPrimitive(final Type type, final int component, final int subcomponent) {
    if (type == null) {
      throw new NullPointerException("type may not be null");
    }
    if (component < 1) {
      throw new IllegalArgumentException(
          "component must not be 1 or more (note that this parameter is 1-indexed, not 0-indexed)");
    }
    if (subcomponent < 1) {
      throw new IllegalArgumentException(
          "subcomponent must not be 1 or more (note that this parameter is 1-indexed, not 0-indexed)");
    }

    Type comp = getComponent(type, component);
    if (type instanceof Varies && comp instanceof GenericPrimitive && subcomponent > 1) {
      try {
        final Varies varies = (Varies) type;
        final GenericComposite comp2 = new GenericComposite(type.getMessage());
        varies.setData(comp2);
        comp = getComponent(type, component);
      } catch (final DataTypeException de) {
        final String message = "Unexpected exception copying data to generic composite. "
            + "This is probably a bug within HAPI. " + de.getMessage();
        throw new Error(message);
      }
    }
    final Type sub = getComponent(comp, subcomponent);
    return getPrimitive(sub);
  }

  /**
   * Attempts to extract a Primitive from the given type. If it's a composite,
   * drills down through first components until a primitive is reached.
   */
  private static Primitive getPrimitive(Type type) {
    if (type instanceof Primitive) {
      return (Primitive) type;
    }
    if (type instanceof Composite) {
      try {
        return getPrimitive(((Composite) type).getComponent(0));
      } catch (HL7Exception exception) {
        throw new RuntimeException("Internal error: HL7Exception thrown on Composite.getComponent(0).");
      }
    }
    return getPrimitive(((Varies) type).getData());
  }

  /**
   * Returns the component (or sub-component, as the case may be) at the given
   * index. If it does not exist, it is added as an "extra component". If comp > 1
   * is requested from a Varies with GenericPrimitive data, the data is set to
   * GenericComposite (this avoids the creation of a chain of ExtraComponents on
   * GenericPrimitives). Components are numbered from 1.
   */
  private static Type getComponent(Type type, int comp) {

    if (type instanceof Primitive && comp == 1) {
      return type;
    }
    if (type instanceof Composite) {
      if (comp <= numStandardComponents(type) || type instanceof GenericComposite) {
        try {
          return ((Composite) type).getComponent(comp - 1);
        } catch (DataTypeException dte) {
          throw new RuntimeException(
              "Internal error: HL7Exception thrown on getComponent(x) where x < # standard components.", dte);
        }
      }
    }
    if (Varies.class.isAssignableFrom(type.getClass())) {
      Varies varies = (Varies) type;
      try {
        if (comp > 1 && GenericPrimitive.class.isAssignableFrom(varies.getData().getClass())) {
          varies.setData(new GenericComposite(varies.getMessage()));
        }
      } catch (DataTypeException dte) {
        throw new RuntimeException("Unexpected exception copying data to generic composite: " + dte.getMessage(), dte);
      }

      return getComponent(varies.getData(), comp);
    }

    return type.getExtraComponents().getComponent(comp - numStandardComponents(type) - 1);
  }

  /**
   * Returns the number of components in the given type, i.e. the number of
   * standard components (e.g. 6 for CE) plus any extra components that have been
   * added at runtime.
   *
   * @param type
   *          composite type
   * @return the number of components in the given type
   */
  public static int numComponents(Type type) {
    if (!(type instanceof Varies)) {
      return numStandardComponents(type) + type.getExtraComponents().numComponents();
    }
    return numComponents(((Varies) type).getData());
  }

  private static int numStandardComponents(Type type) {
    if (type instanceof Composite) {
      return ((Composite) type).getComponents().length;
    }
    if (type instanceof Varies) {
      return numStandardComponents(((Varies) type).getData());
    }
    return 1;
  }
}
