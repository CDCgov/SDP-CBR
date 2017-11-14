package gov.cdc.sdp.hl7v2.filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.*;

import java.util.Iterator;
import java.util.List;

public class ContainsExpression extends ExpressionEvaluator{

    public ContainsExpression(Expression left, Expression right){
        super(left,right);
    }

    public Object evaluateValue(Object left , Object right){
        String lstring = objectValue(left);
        String rstring = objectValue(right);
        if(lstring == null || rstring == null){
            return false;
        }
        return lstring.contains(rstring);
    }


}

