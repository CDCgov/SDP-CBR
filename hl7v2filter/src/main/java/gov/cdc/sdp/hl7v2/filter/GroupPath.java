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

public class GroupPath implements Expression{
    String name;
    String rep;
    Expression constraint;
    public GroupPath(String name, String rep, Expression constraint){
        this.name=name;
        this.rep=rep;
        this.constraint = constraint;
    }

    public Object evaluate(Context ctx){
        List<Group> list = new ArrayList();
        SegmentFinder finder = (SegmentFinder)ctx.get("FINDER");
        if(rep == null || rep.equals("*")){
            int index = 0;
            Group g = null;

            while(( g = evaluateRepition(index++,finder)) !=null){
               list.add(g);
            }
        }else if(rep != null){
            try{
                Integer index = Integer.parseInt(rep);
                Group g = evaluateRepition(index, finder);
                list.add(g);
            }catch(NumberFormatException e){

            }
        }else {
            Group g = evaluateRepition(0, finder);
            list.add(g);
        }


        return evaluateConstraint(list, ctx);
    }

    private List evaluateConstraint(List<Group> groups, Context parent){
        if(constraint == null) return groups;
        List<Group> list = new ArrayList();
        for(Iterator<Group> iter = groups.iterator(); iter.hasNext();){
            Group g = iter.next();
            SegmentFinder f = new SegmentFinder(g);
            Context ctx = new Context(parent);
            ctx.set("MESSAGE", g);
            ctx.set("FINDER", f);
            Object o = constraint.evaluate(ctx);
            if(o == null){
                continue;
            }
            if(o instanceof Boolean){
                if((Boolean)o){
                    list.add(g);
                }
            }else if(o instanceof List){
                if(((List)o).size() >0){
                    list.add(g);
                }
            }else if(o != null){
                list.add(g);
            }
        }
        return list;
    }

    private Group evaluateRepition(int i, SegmentFinder finder){
        try {
            finder.reset();
            Group g = finder.findGroup(name, i);

            return  g.isEmpty()? null : g;
        }catch(HL7Exception e){

        }
        return null;
    }

}
