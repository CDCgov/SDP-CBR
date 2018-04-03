package gov.cdc.sdp.hl7v2.filter;

public interface Expression {
  Object evaluate(Context ctx);
}
