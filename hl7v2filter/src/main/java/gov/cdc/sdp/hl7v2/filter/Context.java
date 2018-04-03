package gov.cdc.sdp.hl7v2.filter;

import java.util.HashMap;
import java.util.Map;

public class Context {

  Map<String, Object> contextParameters;
  Context parent;

  public Context() {
    this(null, new HashMap<String, Object>());
  }

  public Context(Map<String, Object> params) {
    this(null, params);
  }

  public Context(Context parent) {
    this(parent, new HashMap<String, Object>());
  }

  public Context(Context parent, Map<String, Object> params) {
    this.parent = parent;
    this.contextParameters = params;
  }

  public void set(String key, Object value) {
    this.contextParameters.put(key, value);
  }

  public Object get(String key) {
    Object object = contextParameters.get(key);
    if (object == null && parent != null) {
      object = parent.get(key);
    }
    return object;
  }
}
