package gov.cdc.sdp.hl7v2.filter;

public class Time implements Comparable {

  int hour = -1;
  int minute = -1;
  int second = -1;
  int millisecond = -1;
  int timezoneOffset = -1;

  public boolean canCompare(Object obj) {
    return (obj instanceof Time);
  }

  public boolean gt(Time other) {
    boolean value = true;
    value = value && !(hour < other.getHour());
    value = value && !(minute < other.getMinute());
    value = value && !(second < other.getSecond());
    value = value && !(millisecond < other.getMillisecond());
    return value;
  }

  public boolean eq(Time other) {
    boolean value = true;
    value = value && hour == other.getHour();
    value = value && minute == other.getMinute();
    value = value && second == other.getSecond();
    value = value && millisecond == other.getMillisecond();
    return value;
  }

  public int compareTo(Object other) {
    Time odt = (Time) other;
    if (eq(odt)) {
      return 0;
    } else if (gt(odt)) {
      return 1;
    }
    return -1;
  }

  public int getTimezoneOffset() {
    return timezoneOffset;
  }

  public void setTimezoneOffset(int timezoneOffset) {
    this.timezoneOffset = timezoneOffset;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public int getSecond() {
    return second;
  }

  public void setSecond(int second) {
    this.second = second;
  }

  public int getMillisecond() {
    return millisecond;
  }

  public void setMillisecond(int millisecond) {
    this.millisecond = millisecond;
  }
}
