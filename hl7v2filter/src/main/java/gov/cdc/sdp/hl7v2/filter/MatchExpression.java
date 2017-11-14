package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Type;

import java.util.Iterator;
import java.util.List;

public class MatchExpression  extends   ExpressionEvaluator{

    public MatchExpression(Expression left, Expression right){
        super(left,right);
    }

    public Object evaluateValue(Object left , Object right){
        String lstring = objectValue(left);
        String rstring = objectValue(right);
        if(lstring == null || rstring == null){
            return false;
        }
        return lstring.matches(rstring);
    }
}
