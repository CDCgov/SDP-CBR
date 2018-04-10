package gov.cdc.sdp.cbr.trace.model;

public enum TraceStatus {
  // Gaps allow for the addition of new statuses without db migration
  ERROR(20), WARN(15), INFO(10);

  private final int level;

  TraceStatus(int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }

  public static TraceStatus getStatus(int statusCode) {
    switch (statusCode) {
      case 10:
        return INFO;
      case 15:
        return WARN;
      case 20:
        return ERROR;
      default:
        return null;
    }
  }
}