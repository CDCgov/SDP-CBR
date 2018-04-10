package gov.cdc.sdp.hl7v2.filter;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterBuilder extends Hl7V2FilterBaseVisitor {

  public FilterBuilder() {

  }

  public Filter buildFilter(String in) {
    return new Filter(build(in));
  }

  public Expression build(String in) {
    Hl7V2FilterLexer lexer = new Hl7V2FilterLexer(new ANTLRInputStream(in));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    Hl7V2FilterParser parser = new Hl7V2FilterParser(tokens);
    Expression expression = (Expression) this.visit(parser.expression());
    return expression;
  }

  @Override
  public Object visitPathTerm(Hl7V2FilterParser.PathTermContext ctx) {

    return visit(ctx.path_expression());
  }

  @Override
  public Object visitPath_expression(Hl7V2FilterParser.Path_expressionContext ctx) {
    PathExpression expression = (PathExpression) visit(ctx.segment_path_spec());
    if (ctx.SLASH() != null) {
      expression.setStartPath("/");
    } else if (ctx.DOT() != null) {
      expression.setStartPath(".");
    }
    return expression;
  }

  @Override
  public Object visitSegment_path_spec(Hl7V2FilterParser.Segment_path_specContext ctx) {
    List<Hl7V2FilterParser.Group_repContext> list = ctx.group_rep();
    List<GroupPath> groups = new ArrayList();
    for (Iterator<Hl7V2FilterParser.Group_repContext> iter = list.iterator(); iter.hasNext();) {
      groups.add((GroupPath) visit(iter.next()));
    }
    SegmentPath path = null;
    if (ctx.segment_rep() != null) {
      path = (SegmentPath) visit(ctx.segment_rep());
    }
    PathExpression pe = new PathExpression(groups, path);
    return pe;
  }

  @Override
  public Object visitSegment_rep(Hl7V2FilterParser.Segment_repContext ctx) {
    String name = ctx.name_pattern().getText();

    String field = "-1";
    String rep = "*";
    String component = "1";
    String subcomponent = "1";
    String fieldRep = "*";
    if (ctx.field_spec() != null) {
      field = parseString(ctx.field_spec().field(), "-1");
      fieldRep = parseString(ctx.field_spec().rep(), "0");
      component = parseString(ctx.field_spec().component(), "1");
      subcomponent = parseString(ctx.field_spec().subcomponent(), "1");

    }

    if (ctx.rep() != null) {
      rep = (String) visit(ctx.rep());
    }

    SegmentPath path = new SegmentPath(name, rep, fieldRep, Integer.parseInt(field), Integer.parseInt(component),
        Integer.parseInt(subcomponent));
    return path;
  }

  @Override
  public Object visitRep(Hl7V2FilterParser.RepContext ctx) {
    return ctx.getText();
  }

  @Override
  public Object visitName_pattern(Hl7V2FilterParser.Name_patternContext ctx) {
    return ctx.getText();
  }

  @Override
  public Object visitGroup_rep(Hl7V2FilterParser.Group_repContext ctx) {
    Expression con = parseExpression(ctx.constraint());
    String name = (String) visit(ctx.name_pattern());
    String rep = null;
    if (ctx.rep() != null) {
      rep = ctx.rep().getText();
    }
    GroupPath path = new GroupPath(name, rep, con);
    return path;
  }

  @Override
  public Object visitConstraint(Hl7V2FilterParser.ConstraintContext ctx) {
    return parseExpression(ctx.expression());
  }

  @Override
  public Object visitGroupTerm(Hl7V2FilterParser.GroupTermContext ctx) {
    return parseExpression(ctx.expression());
  }

  @Override
  public Object visitMatchExpression(Hl7V2FilterParser.MatchExpressionContext ctx) {
    Expression expression = parseExpression(ctx.expression(0));
    Expression match = parseExpression(ctx.expression(1));
    return new MatchExpression(expression, match);
  }

  @Override
  public Object visitContainsExpression(Hl7V2FilterParser.ContainsExpressionContext ctx) {
    Expression expression = parseExpression(ctx.expression(0));
    Expression match = parseExpression(ctx.expression(1));
    return new ContainsExpression(expression, match);
  }

  @Override
  public Object visitInequalityExpression(Hl7V2FilterParser.InequalityExpressionContext ctx) {
    Expression left = parseExpression(ctx.expression(0));
    Expression right = parseExpression(ctx.expression(1));
    String operator = (String) visit(ctx.getChild(1));
    return new InEqualityExpression(left, right, operator);
  }

  @Override
  public Object visitEqualityExpression(Hl7V2FilterParser.EqualityExpressionContext ctx) {
    Expression left = parseExpression(ctx.expression(0));
    Expression right = parseExpression(ctx.expression(1));
    String operator = (String) visit(ctx.getChild(1));
    return new EqualityExpression(left, right, (operator.equals("!=")));
  }

  @Override
  public Object visitAndExpression(Hl7V2FilterParser.AndExpressionContext ctx) {
    Expression left = parseExpression(ctx.expression(0));
    Expression right = parseExpression(ctx.expression(1));
    return new AndExpression(left, right);
  }

  @Override
  public Object visitOrExpression(Hl7V2FilterParser.OrExpressionContext ctx) {
    Expression left = parseExpression(ctx.expression(0));
    Expression right = parseExpression(ctx.expression(1));
    return new OrExpression(left, right);
  }

  @Override
  public Object visitLiteralTerm(Hl7V2FilterParser.LiteralTermContext ctx) {
    return new LiteralExpression(visit(ctx.literal()));
  }

  @Override
  public Object visitBooleanLiteral(Hl7V2FilterParser.BooleanLiteralContext ctx) {
    Boolean value = ctx.getText().equals("true");
    return value;
  }

  @Override
  public Object visitNullLiteral(Hl7V2FilterParser.NullLiteralContext ctx) {
    return null;
  }

  @Override
  public Object visitNumberLiteral(Hl7V2FilterParser.NumberLiteralContext ctx) {
    return Double.parseDouble(ctx.NUMBER().getText());
  }

  @Override
  public Object visitStringLiteral(Hl7V2FilterParser.StringLiteralContext ctx) {
    String st = (String) visit(ctx.STRING());
    return st;
  }

  @Override
  public Object visitDateTimeLiteral(Hl7V2FilterParser.DateTimeLiteralContext ctx) {
    String input = ctx.getText();
    if (input.startsWith("@")) {
      input = input.substring(1);
    }

    Pattern dateTimePattern = Pattern.compile(
        "(\\d{4})(-(\\d{2}))?(-(\\d{2}))?((Z)|(T((\\d{2})(\\:(\\d{2})(\\:(\\d{2})(\\.(\\d+))?)?)?)?((Z)|(([+-])(\\d{2})(\\:?(\\d{2}))?))?))?");
    // 1-------2-3---------4-5---------67---8-91-------1---1-------1---1-------1---1-------------11---12-----2-------2----2---------------
    // ----------------------------------------0-------1---2-------3---4-------5---6-------------78---90-----1-------2----3---------------

    Matcher matcher = dateTimePattern.matcher(input);
    if (matcher.matches()) {
      try {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        DateTime result = new DateTime();
        int year = Integer.parseInt(matcher.group(1));
        int month = -1;
        int day = -1;
        int hour = -1;
        int minute = -1;
        int second = -1;
        int millisecond = -1;
        result.setYear(year);
        if (matcher.group(3) != null) {
          month = Integer.parseInt(matcher.group(3));
          if (month < 0 || month > 12) {
            throw new IllegalArgumentException(String.format("Invalid month in date/time literal (%s).", input));
          }
          result.setMonth(month);
        }

        if (matcher.group(5) != null) {
          day = Integer.parseInt(matcher.group(5));
          int maxDay = 31;
          switch (month) {
            case 2:
              maxDay = calendar.isLeapYear(year) ? 29 : 28;
              break;
            case 4:
            case 6:
            case 9:
            case 11:
              maxDay = 30;
              break;
            default:
              break;
          }

          if (day < 0 || day > maxDay) {
            throw new IllegalArgumentException(String.format("Invalid day in date/time literal (%s).", input));
          }

          result.setDay(day);
        }

        if (matcher.group(10) != null) {
          hour = Integer.parseInt(matcher.group(10));
          if (hour < 0 || hour > 24) {
            throw new IllegalArgumentException(String.format("Invalid hour in date/time literal (%s).", input));
          }
          result.setHour(hour);
        }

        if (matcher.group(12) != null) {
          minute = Integer.parseInt(matcher.group(12));
          if (minute < 0 || minute >= 60 || (hour == 24 && minute > 0)) {
            throw new IllegalArgumentException(String.format("Invalid minute in date/time literal (%s).", input));
          }
          result.setMinute(minute);
        }

        if (matcher.group(14) != null) {
          second = Integer.parseInt(matcher.group(14));
          if (second < 0 || second >= 60 || (hour == 24 && second > 0)) {
            throw new IllegalArgumentException(String.format("Invalid second in date/time literal (%s).", input));
          }
          result.setSecond(second);
        }

        if (matcher.group(16) != null) {
          millisecond = Integer.parseInt(matcher.group(16));
          if (millisecond < 0 || (hour == 24 && millisecond > 0)) {
            throw new IllegalArgumentException(String.format("Invalid millisecond in date/time literal (%s).", input));
          }
          result.setMillisecond(millisecond);
        }

        if ((matcher.group(7) != null && matcher.group(7).equals("Z"))
            || ((matcher.group(18) != null) && matcher.group(18).equals("Z"))) {
          result.setTimezoneOffset(0);
        }

        if (matcher.group(20) != null) {
          int offsetPolarity = matcher.group(20).equals("+") ? 1 : 0;

          if (matcher.group(23) != null) {
            int hourOffset = Integer.parseInt(matcher.group(21));
            if (hourOffset < 0 || hourOffset > 14) {
              throw new IllegalArgumentException(
                  String.format("Timezone hour offset is out of range in date/time literal (%s).", input));
            }

            int minuteOffset = Integer.parseInt(matcher.group(23));
            if (minuteOffset < 0 || minuteOffset >= 60 || (hourOffset == 14 && minuteOffset > 0)) {
              throw new IllegalArgumentException(
                  String.format("Timezone minute offset is out of range in date/time literal (%s).", input));
            }

            result.setTimezoneOffset((hourOffset + (minuteOffset / 60)) * offsetPolarity);
          } else {
            if (matcher.group(21) != null) {
              int hourOffset = Integer.parseInt(matcher.group(21));
              if (hourOffset < 0 || hourOffset > 14) {
                throw new IllegalArgumentException(
                    String.format("Timezone hour offset is out of range in date/time literal (%s).", input));
              }

              result.setTimezoneOffset(hourOffset * offsetPolarity);
            }
          }
        }
        return result;
      } catch (RuntimeException re) {
        throw new IllegalArgumentException(String.format(
            "Invalid date-time input (%s). Use ISO 8601 date time representation (yyyy-MM-ddThh:mm:ss.mmmmZhh:mm).",
            input), re);
      }
    } else {
      throw new IllegalArgumentException(String.format(
          "Invalid date-time input (%s). Use ISO 8601 date time representation (yyyy-MM-ddThh:mm:ss.mmmmZhh:mm).",
          input));
    }
  }

  @Override
  public Object visitTimeLiteral(Hl7V2FilterParser.TimeLiteralContext ctx) {
    String input = ctx.getText();
    if (input.startsWith("@")) {
      input = input.substring(1);
    }

    Pattern dateTimePattern = Pattern
        .compile("T((\\d{2})(\\:(\\d{2})(\\:(\\d{2})(\\.(\\d+))?)?)?)?((Z)|(([+-])(\\d{2})(\\:?(\\d{2}))?))?");
    // -12-------3---4-------5---6-------7---8-------------91---11-----1-------1----1------------
    // -----------------------------------------------------0---12-----3-------4----5------------

    Matcher matcher = dateTimePattern.matcher(input);
    if (matcher.matches()) {
      try {
        Time result = new Time();
        int hour = Integer.parseInt(matcher.group(2));
        int minute = -1;
        int second = -1;
        int millisecond = -1;
        if (hour < 0 || hour > 24) {
          throw new IllegalArgumentException(String.format("Invalid hour in time literal (%s).", input));
        }
        result.setHour(hour);

        if (matcher.group(4) != null) {
          minute = Integer.parseInt(matcher.group(4));
          if (minute < 0 || minute >= 60 || (hour == 24 && minute > 0)) {
            throw new IllegalArgumentException(String.format("Invalid minute in time literal (%s).", input));
          }
          result.setMinute(minute);
        }

        if (matcher.group(6) != null) {
          second = Integer.parseInt(matcher.group(6));
          if (second < 0 || second >= 60 || (hour == 24 && second > 0)) {
            throw new IllegalArgumentException(String.format("Invalid second in time literal (%s).", input));
          }
          result.setSecond(second);
        }

        if (matcher.group(8) != null) {
          millisecond = Integer.parseInt(matcher.group(8));
          if (millisecond < 0 || (hour == 24 && millisecond > 0)) {
            throw new IllegalArgumentException(String.format("Invalid millisecond in time literal (%s).", input));
          }
          result.setMillisecond(millisecond);
        }

        if (matcher.group(10) != null && matcher.group(10).equals("Z")) {
          result.setTimezoneOffset(0);
        }

        if (matcher.group(12) != null) {
          int offsetPolarity = matcher.group(12).equals("+") ? 1 : 0;

          if (matcher.group(15) != null) {
            int hourOffset = Integer.parseInt(matcher.group(13));
            if (hourOffset < 0 || hourOffset > 14) {
              throw new IllegalArgumentException(
                  String.format("Timezone hour offset out of range in time literal (%s).", input));
            }

            int minuteOffset = Integer.parseInt(matcher.group(15));
            if (minuteOffset < 0 || minuteOffset >= 60 || (hourOffset == 14 && minuteOffset > 0)) {
              throw new IllegalArgumentException(
                  String.format("Timezone minute offset out of range in time literal (%s).", input));
            }
            result.setTimezoneOffset((hourOffset + (minuteOffset / 60)) * offsetPolarity);
          } else {
            if (matcher.group(13) != null) {
              int hourOffset = Integer.parseInt(matcher.group(13));
              if (hourOffset < 0 || hourOffset > 14) {
                throw new IllegalArgumentException(
                    String.format("Timezone hour offset out of range in time literal (%s).", input));
              }
              result.setTimezoneOffset((hourOffset * offsetPolarity));
            }
          }
        }
        return result;
      } catch (RuntimeException re) {
        throw new IllegalArgumentException(String.format(
            "Invalid date-time input (%s). Use ISO 8601 date time representation (yyyy-MM-ddThh:mm:ss.mmmmZhh:mm).",
            input), re);
      }
    } else {
      throw new IllegalArgumentException(String.format(
          "Invalid date-time input (%s). Use ISO 8601 date time representation (yyyy-MM-ddThh:mm:ss.mmmmZhh:mm).",
          input));
    }
  }

  private Expression parseExpression(ParseTree pt) {
    return pt == null ? null : (Expression) visit(pt);
  }

  @Override
  public Object visitTerminal(TerminalNode node) {
    String text = node.getText();
    int tokenType = node.getSymbol().getType();
    if (Hl7V2FilterLexer.STRING == tokenType) {
      // chop off leading and trailing ' or "
      text = text.substring(1, text.length() - 1);
      text = text.replace("''", "'");
    }

    return text;
  }

  public String parseString(ParseTree pt, String defaultValue) {
    if (pt == null) {
      return defaultValue;
    }
    Object object = visit(pt);
    return object == null ? defaultValue : object.toString();
  }

}
