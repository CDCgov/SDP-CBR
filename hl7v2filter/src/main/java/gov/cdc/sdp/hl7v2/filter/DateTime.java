package gov.cdc.sdp.hl7v2.filter;

public class DateTime implements Comparable {

  int year = -1;
  int month = -1;
  int day = -1;
  int hour = -1;
  int minute = -1;
  int second = -1;
  int millisecond = -1;
  int timezoneOffset = -1;

  public DateTime() {

  }

  public DateTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    this.millisecond = millisecond;

  }

  public boolean canCompare(Object obj) {
    return (obj instanceof DateTime);
  }

  public boolean gt(DateTime other) {
    boolean value = true;
    value = value && !(year < other.getYear());
    value = value && !(month < other.getMonth());
    value = value && !(day < other.getDay());
    value = value && !(hour < other.getHour());
    value = value && !(minute < other.getMinute());
    value = value && !(second < other.getSecond());
    value = value && !(millisecond < other.getMillisecond());
    return value;
  }

  public boolean eq(DateTime other) {
    boolean value = true;
    value = value && year == other.getYear();
    value = value && month == other.getMonth();
    value = value && day == other.getDay();
    value = value && hour == other.getHour();
    value = value && minute == other.getMinute();
    value = value && second == other.getSecond();
    value = value && millisecond == other.getMillisecond();
    return value;
  }

  public int compareTo(Object other) {
    DateTime odt = (DateTime) other;
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

  public void setTimezoneOffset(int timezonOffset) {
    this.timezoneOffset = timezonOffset;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
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
