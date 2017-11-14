package gov.cdc.sdp.hl7v2.filter;

public class EqualityExpression extends InEqualityExpression {

    boolean negate;
    Expression left, right;

    public EqualityExpression(Expression left, Expression right, boolean negate){
        super(left,right,"=");
        this.negate=negate;
    }

    public Object evaluate(Context ctx){
        Boolean eq = (Boolean)super.evaluate(ctx);
        return (negate)? !eq : eq;
    }

    protected Boolean compareNULL(Object lv, Object rv){
        return lv == rv;
    }
    protected Boolean compareBoolean(Object lv, Object rv){
        if((lv instanceof Boolean) || (rv instanceof Boolean)){
            return ((Boolean)lv && (Boolean)rv);
        }
        return false;
    }


}
