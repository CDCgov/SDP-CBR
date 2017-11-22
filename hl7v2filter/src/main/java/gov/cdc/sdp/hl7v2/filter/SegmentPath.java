package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.*;
import ca.uhn.hl7v2.model.primitive.DT;
import ca.uhn.hl7v2.model.primitive.TM;
import ca.uhn.hl7v2.model.primitive.TSComponentOne;
import ca.uhn.hl7v2.util.SegmentFinder;

import java.util.ArrayList;
import java.util.List;

public class SegmentPath implements Expression{

    String name;
    String rep;
    String fieldRep;
    int field;
    int component;
    int subcomponent;

    String segment;
    boolean find = false;

    public SegmentPath(String name, String rep,String fieldRep, int field, int component, int subcomponent){
        this.name=name;
        this.rep=rep==null? "0":rep;
        this.fieldRep=fieldRep==null? "0":fieldRep;
        this.field=field;
        this.segment=name;
        this.component=component;
        this.subcomponent=subcomponent;
        this.find=true;
        if(this.segment.contains("*") || this.segment.contains("?") ){
            this.find=true;
        }
    }

    public Object evaluate(Context ctx){
        List lst = new ArrayList();
        SegmentFinder finder = (SegmentFinder)ctx.get("FINDER");
        if(this.rep !=null && this.rep.equals("*")){
            int index = 0;
            List obj = null;
            while((obj = evaluateRepition(index,finder)) != null) {
                lst.addAll(obj);
                index++;
            }
        }else{
            lst.addAll(evaluateRepition(0,finder));
        }
        return lst;
    }

    private List evaluateRepition(int i, SegmentFinder finder){
        finder.reset();
        List vals = new ArrayList();
        try {

            Segment seg = this.find? finder.findSegment(segment, i):
                    finder.getSegment(segment, i);

            if(seg == null || seg.isEmpty()){
                return null;
            }
            if(field == -1 ){
                vals.add(seg);
                return vals;
            }

            if(fieldRep!= null && fieldRep.equals("*")){
                Primitive o = null;
                int index = 0;

                while((o = getPrimitive(seg,field,index++,component,subcomponent))!=null &&
                        o.getValue() != null && !o.isEmpty()){
                    vals.add(convert(o));
                }
            }else{
                Primitive o = getPrimitive(seg,field,Integer.parseInt(fieldRep),component,subcomponent);
                if(o!= null && !o.isEmpty()){
                    vals.add(convert(o));
                }
            }

            return vals;
        }catch(HL7Exception e){

        }
        return null;
    }


    private Object convert(Object o)throws DataTypeException{
        if(o instanceof TSComponentOne) {
            o = parseTS((TSComponentOne)o);
        }else if (o instanceof DT) {
            o = parseDT((DT)o);
        }
        else if (o instanceof TM ) {
            o = parseTM((TM)o);
        }else {
            o = o.toString();
        }
        return o;
    }

    private DateTime parseTS(TSComponentOne v) throws DataTypeException {

        DateTime t = new DateTime();
        t.setYear(v.getYear());
        t.setMonth(v.getMonth());
        t.setDay(v.getDay());
        t.setHour(v.getHour());
        t.setMinute(v.getMinute());
        t.setSecond(v.getSecond());
        t.setMillisecond((int)(v.getFractSecond()/.000000001));
        return t;
    }

    private DateTime parseDT(DT v)throws  DataTypeException{

        DateTime t = new DateTime();
        t.setYear(v.getYear());
        t.setMonth(v.getMonth());
        t.setDay(v.getDay());
        return t;
    }
    private Time parseTM(TM v)throws DataTypeException{
        Time t = new Time();
        t.setHour(v.getHour());
        t.setMinute(v.getMinute());
        t.setSecond(v.getSecond());
        t.setMillisecond((int)(v.getFractSecond()/.000000001));
        return t;

    }

    protected String get(Segment segment, int field, int rep, int component, int subcomponent) throws HL7Exception {
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
     * Returns the Primitive object at the given location in the given field. It is intended that
     * the given type be at the field level, although extra components will be added blindly if, for
     * example, you provide a primitive subcomponent instead and specify component or subcomponent >
     * 1
     *
     * @param type the type from which to get the primitive
     * @param component the component number (indexed from 1, use 1 for primitive field)
     * @param subcomponent the subcomponent number (indexed from 1, use 1 for primitive component)
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
                final String message = "Unexpected exception copying data to generic composite. This is probably a bug within HAPI. "
                        + de.getMessage();
                throw new Error(message);
            }
        }
        final Type sub = getComponent(comp, subcomponent);
        return getPrimitive(sub);
    }

    /**
     * Attempts to extract a Primitive from the given type. If it's a composite, drills down through
     * first components until a primitive is reached.
     */
    private static Primitive getPrimitive(Type type) {
        if (type instanceof Primitive) {
            return (Primitive) type;
        }
        if (type instanceof Composite) {
            try {
                return getPrimitive(((Composite) type).getComponent(0));
            } catch (HL7Exception e) {
                throw new RuntimeException("Internal error: HL7Exception thrown on Composite.getComponent(0).");
            }
        }
        return getPrimitive(((Varies) type).getData());
    }

    /**
     * Returns the component (or sub-component, as the case may be) at the given index. If it does
     * not exist, it is added as an "extra component". If comp > 1 is requested from a Varies with
     * GenericPrimitive data, the data is set to GenericComposite (this avoids the creation of a
     * chain of ExtraComponents on GenericPrimitives). Components are numbered from 1.
     */
    private static Type getComponent(Type type, int comp) {

        if (type instanceof Primitive && comp == 1) {
            return type;
        }
        if (type instanceof Composite) {
            if (comp <= numStandardComponents(type) || type instanceof GenericComposite) {
                try {
                    return ((Composite) type).getComponent(comp - 1);
                } catch (DataTypeException e) {
                    throw new RuntimeException(
                            "Internal error: HL7Exception thrown on getComponent(x) where x < # standard components.",
                            e);
                }
            }
        }
        if (Varies.class.isAssignableFrom(type.getClass())) {
            Varies v = (Varies) type;
            try {
                if (comp > 1 && GenericPrimitive.class.isAssignableFrom(v.getData().getClass()))
                    v.setData(new GenericComposite(v.getMessage()));
            } catch (DataTypeException e) {
                throw new RuntimeException("Unexpected exception copying data to generic composite: " + e.getMessage(),
                        e);
            }

            return getComponent(v.getData(), comp);
        }

        return type.getExtraComponents().getComponent(comp - numStandardComponents(type) - 1);
    }

    /**
     * Returns the number of components in the given type, i.e. the number of standard components
     * (e.g. 6 for CE) plus any extra components that have been added at runtime.
     *
     * @param t composite type
     * @return the number of components in the given type
     */
    public static int numComponents(Type t) {
        if (!(t instanceof Varies)) {
            return numStandardComponents(t) + t.getExtraComponents().numComponents();
        }
        return numComponents(((Varies) t).getData());
    }

    private static int numStandardComponents(Type t) {
        if (t instanceof Composite) {
            return ((Composite) t).getComponents().length;
        }
        if (t instanceof Varies) {
            return numStandardComponents(((Varies) t).getData());
        }
        return 1;
    }
}
