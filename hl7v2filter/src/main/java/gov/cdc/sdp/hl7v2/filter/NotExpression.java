package gov.cdc.sdp.hl7v2.filter;

public class NotExpression implements Expression {

    Expression exp;

    public NotExpression(Expression exp){
        this.exp = exp;
    }

    public Object evaluate(Context ctx){
        Object v = exp.evaluate(ctx);
        if(v == null){
            return true;
        }else if(v instanceof Boolean){
            return !(Boolean)v;
        }
        return false;
    }
}
