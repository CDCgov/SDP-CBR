package gov.cdc.sdp.hl7v2.filter;


import java.util.Iterator;
import java.util.List;

public class InEqualityExpression extends ExpressionEvaluator{
    String operator;


    public InEqualityExpression(Expression left, Expression right, String operator){
        super(left,right);
        this.operator=operator;
    }

    public Object evaluate(Context ctx ){
        List list = (List)super.evaluate(ctx);
        if(list == null){
            return false;
        }
        boolean ret = false;
        for(Iterator iter = list.iterator(); iter.hasNext();){
            Object o = iter.next();
            if(o != null && o instanceof Boolean && !(Boolean)o){
                continue;
            }
            ret = true;
        }
        return ret;
    }

    protected Boolean evaluateValue(Object lv, Object rv){
        if(lv == null || rv == null) {
            return compareNULL(lv,rv);
        }
        else if(lv instanceof Boolean || rv instanceof Boolean){
            return compareBoolean(lv,rv);
        }
        else if(lv instanceof DateTime && rv instanceof DateTime ){
            return compare((DateTime)lv,(DateTime)rv);
        }else if(lv instanceof Time && rv instanceof Time ){
            return compare((Time)lv,(Time)rv);
        }else if(lv instanceof Number || rv instanceof Number){
            return compare(parseNumber(lv),parseNumber(rv));
        }
        return compare(lv.toString(),rv.toString());
    }

    private Boolean compare(Comparable left, Comparable right){
        if(left == null || right == null){
            return false;
        }
        int i = left.compareTo(right);
        return compareResult(i);
    }


    protected Boolean compareNULL(Object lv, Object rv){
        return false;
    }
    protected Boolean compareBoolean(Object lv, Object rv){
        return false;
    }

    private Boolean compareResult(int comp){
        Boolean v = false;
        switch(operator){
            case ">" :
                v= comp > 0;
                break;
            case "<":
                v= comp < 0;
                break;
            case "<=":
                v= comp <= 0;
                break;
            case ">=":
                v= comp >= 0;
                break;
            case "=":
                v= comp == 0;
                break;
            case "!=":
                v= comp != 0;
                break;
        }
        return v;
    }

    private Double parseNumber(Object obj){
        if(obj instanceof Number)return ((Number)obj).doubleValue();
        try{
           return Double.parseDouble(obj.toString());
        }catch(NumberFormatException e){

        }
        return null;
    }

}
