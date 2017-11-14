// Generated from /Users/bobd/projects/hl7v2filter/Hl7V2Filter.g4 by ANTLR 4.7
package gov.cdc.sdp.hl7v2.filter;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class Hl7V2FilterParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, AND=19, OR=20, NOT=21, DOT=22, SLASH=23, DATETIME=24, TIME=25, 
		INTEGER=26, NUMBER=27, IDENTIFIER=28, STRING=29, WS=30;
	public static final int
		RULE_expression = 0, RULE_term = 1, RULE_literal = 2, RULE_path_expression = 3, 
		RULE_segment_path_spec = 4, RULE_field_spec = 5, RULE_group_rep = 6, RULE_segment_rep = 7, 
		RULE_constraint = 8, RULE_name_pattern = 9, RULE_field = 10, RULE_component = 11, 
		RULE_subcomponent = 12, RULE_rep = 13, RULE_comparator = 14;
	public static final String[] ruleNames = {
		"expression", "term", "literal", "path_expression", "segment_path_spec", 
		"field_spec", "group_rep", "segment_rep", "constraint", "name_pattern", 
		"field", "component", "subcomponent", "rep", "comparator"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'<='", "'<'", "'>'", "'>='", "'='", "'!='", "'contains'", "'matches'", 
		"'('", "')'", "'null'", "'true'", "'false'", "'-'", "'['", "']'", "'*'", 
		"'?'", null, null, null, "'.'", "'/'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "AND", "OR", "NOT", "DOT", "SLASH", 
		"DATETIME", "TIME", "INTEGER", "NUMBER", "IDENTIFIER", "STRING", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Hl7V2Filter.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public Hl7V2FilterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MatchExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MatchExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterMatchExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitMatchExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitMatchExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OR() { return getToken(Hl7V2FilterParser.OR, 0); }
		public OrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(Hl7V2FilterParser.AND, 0); }
		public AndExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InequalityExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InequalityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterInequalityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitInequalityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitInequalityExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContainsExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ContainsExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterContainsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitContainsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitContainsExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqualityExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EqualityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitEqualityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitEqualityExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermExpressionContext extends ExpressionContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterTermExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitTermExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitTermExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new TermExpressionContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(31);
			term();
			}
			_ctx.stop = _input.LT(-1);
			setState(53);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(51);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						_localctx = new InequalityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(33);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(34);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(35);
						expression(7);
						}
						break;
					case 2:
						{
						_localctx = new EqualityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(36);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(37);
						_la = _input.LA(1);
						if ( !(_la==T__4 || _la==T__5) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(38);
						expression(6);
						}
						break;
					case 3:
						{
						_localctx = new ContainsExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(39);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(40);
						match(T__6);
						setState(41);
						expression(5);
						}
						break;
					case 4:
						{
						_localctx = new MatchExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(42);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(43);
						match(T__7);
						setState(44);
						expression(4);
						}
						break;
					case 5:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(45);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(46);
						match(AND);
						setState(47);
						expression(3);
						}
						break;
					case 6:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(48);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(49);
						match(OR);
						setState(50);
						expression(2);
						}
						break;
					}
					} 
				}
				setState(55);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	 
		public TermContext() { }
		public void copyFrom(TermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class GroupTermContext extends TermContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public GroupTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterGroupTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitGroupTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitGroupTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PathTermContext extends TermContext {
		public Path_expressionContext path_expression() {
			return getRuleContext(Path_expressionContext.class,0);
		}
		public PathTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterPathTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitPathTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitPathTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralTermContext extends TermContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterLiteralTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitLiteralTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitLiteralTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_term);
		try {
			setState(62);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new PathTermContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				path_expression();
				}
				break;
			case 2:
				_localctx = new LiteralTermContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				literal();
				}
				break;
			case 3:
				_localctx = new GroupTermContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(58);
				match(T__8);
				setState(59);
				expression(0);
				setState(60);
				match(T__9);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TimeLiteralContext extends LiteralContext {
		public TerminalNode TIME() { return getToken(Hl7V2FilterParser.TIME, 0); }
		public TimeLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterTimeLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitTimeLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitTimeLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullLiteralContext extends LiteralContext {
		public NullLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterNullLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitNullLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DateTimeLiteralContext extends LiteralContext {
		public TerminalNode DATETIME() { return getToken(Hl7V2FilterParser.DATETIME, 0); }
		public DateTimeLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterDateTimeLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitDateTimeLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitDateTimeLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode STRING() { return getToken(Hl7V2FilterParser.STRING, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanLiteralContext extends LiteralContext {
		public BooleanLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterBooleanLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitBooleanLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberLiteralContext extends LiteralContext {
		public TerminalNode NUMBER() { return getToken(Hl7V2FilterParser.NUMBER, 0); }
		public NumberLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_literal);
		int _la;
		try {
			setState(70);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				match(T__10);
				}
				break;
			case T__11:
			case T__12:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				_la = _input.LA(1);
				if ( !(_la==T__11 || _la==T__12) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(66);
				match(STRING);
				}
				break;
			case NUMBER:
				_localctx = new NumberLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(67);
				match(NUMBER);
				}
				break;
			case DATETIME:
				_localctx = new DateTimeLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(68);
				match(DATETIME);
				}
				break;
			case TIME:
				_localctx = new TimeLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(69);
				match(TIME);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Path_expressionContext extends ParserRuleContext {
		public Segment_path_specContext segment_path_spec() {
			return getRuleContext(Segment_path_specContext.class,0);
		}
		public TerminalNode DOT() { return getToken(Hl7V2FilterParser.DOT, 0); }
		public TerminalNode SLASH() { return getToken(Hl7V2FilterParser.SLASH, 0); }
		public Path_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterPath_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitPath_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitPath_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Path_expressionContext path_expression() throws RecognitionException {
		Path_expressionContext _localctx = new Path_expressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_path_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(72);
				match(DOT);
				}
				break;
			}
			setState(76);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(75);
				match(SLASH);
				}
				break;
			}
			setState(78);
			segment_path_spec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Segment_path_specContext extends ParserRuleContext {
		public List<Group_repContext> group_rep() {
			return getRuleContexts(Group_repContext.class);
		}
		public Group_repContext group_rep(int i) {
			return getRuleContext(Group_repContext.class,i);
		}
		public Segment_repContext segment_rep() {
			return getRuleContext(Segment_repContext.class,0);
		}
		public Segment_path_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_segment_path_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterSegment_path_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitSegment_path_spec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitSegment_path_spec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Segment_path_specContext segment_path_spec() throws RecognitionException {
		Segment_path_specContext _localctx = new Segment_path_specContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_segment_path_spec);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(80);
					group_rep();
					}
					} 
				}
				setState(85);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(86);
				segment_rep();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Field_specContext extends ParserRuleContext {
		public FieldContext field() {
			return getRuleContext(FieldContext.class,0);
		}
		public RepContext rep() {
			return getRuleContext(RepContext.class,0);
		}
		public ComponentContext component() {
			return getRuleContext(ComponentContext.class,0);
		}
		public SubcomponentContext subcomponent() {
			return getRuleContext(SubcomponentContext.class,0);
		}
		public Field_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterField_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitField_spec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitField_spec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Field_specContext field_spec() throws RecognitionException {
		Field_specContext _localctx = new Field_specContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_field_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(T__13);
			setState(90);
			field();
			setState(95);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(91);
				match(T__8);
				setState(92);
				rep();
				setState(93);
				match(T__9);
				}
				break;
			}
			setState(103);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(97);
				match(T__13);
				setState(98);
				component();
				setState(101);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(99);
					match(T__13);
					setState(100);
					subcomponent();
					}
					break;
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Group_repContext extends ParserRuleContext {
		public Name_patternContext name_pattern() {
			return getRuleContext(Name_patternContext.class,0);
		}
		public TerminalNode DOT() { return getToken(Hl7V2FilterParser.DOT, 0); }
		public RepContext rep() {
			return getRuleContext(RepContext.class,0);
		}
		public ConstraintContext constraint() {
			return getRuleContext(ConstraintContext.class,0);
		}
		public Group_repContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_group_rep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterGroup_rep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitGroup_rep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitGroup_rep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Group_repContext group_rep() throws RecognitionException {
		Group_repContext _localctx = new Group_repContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_group_rep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(105);
				match(DOT);
				}
			}

			setState(108);
			name_pattern();
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(109);
				match(T__8);
				setState(110);
				rep();
				setState(111);
				match(T__9);
				}
			}

			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(115);
				constraint();
				}
			}

			setState(118);
			match(SLASH);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Segment_repContext extends ParserRuleContext {
		public Name_patternContext name_pattern() {
			return getRuleContext(Name_patternContext.class,0);
		}
		public TerminalNode DOT() { return getToken(Hl7V2FilterParser.DOT, 0); }
		public RepContext rep() {
			return getRuleContext(RepContext.class,0);
		}
		public Field_specContext field_spec() {
			return getRuleContext(Field_specContext.class,0);
		}
		public Segment_repContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_segment_rep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterSegment_rep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitSegment_rep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitSegment_rep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Segment_repContext segment_rep() throws RecognitionException {
		Segment_repContext _localctx = new Segment_repContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_segment_rep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(120);
				match(DOT);
				}
			}

			setState(123);
			name_pattern();
			setState(128);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(124);
				match(T__8);
				setState(125);
				rep();
				setState(126);
				match(T__9);
				}
				break;
			}
			setState(131);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(130);
				field_spec();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstraintContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintContext constraint() throws RecognitionException {
		ConstraintContext _localctx = new ConstraintContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_constraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__14);
			setState(134);
			expression(0);
			setState(135);
			match(T__15);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Name_patternContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(Hl7V2FilterParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(Hl7V2FilterParser.IDENTIFIER, i);
		}
		public Name_patternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterName_pattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitName_pattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitName_pattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Name_patternContext name_pattern() throws RecognitionException {
		Name_patternContext _localctx = new Name_patternContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_name_pattern);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(138); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(137);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << IDENTIFIER))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(140); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(Hl7V2FilterParser.INTEGER, 0); }
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_field);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComponentContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(Hl7V2FilterParser.INTEGER, 0); }
		public ComponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterComponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitComponent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitComponent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComponentContext component() throws RecognitionException {
		ComponentContext _localctx = new ComponentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_component);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubcomponentContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(Hl7V2FilterParser.INTEGER, 0); }
		public SubcomponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subcomponent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterSubcomponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitSubcomponent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitSubcomponent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubcomponentContext subcomponent() throws RecognitionException {
		SubcomponentContext _localctx = new SubcomponentContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_subcomponent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RepContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(Hl7V2FilterParser.INTEGER, 0); }
		public RepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterRep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitRep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitRep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RepContext rep() throws RecognitionException {
		RepContext _localctx = new RepContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_rep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			_la = _input.LA(1);
			if ( !(_la==T__16 || _la==INTEGER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparatorContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(Hl7V2FilterParser.IDENTIFIER, 0); }
		public ComparatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).enterComparator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Hl7V2FilterListener ) ((Hl7V2FilterListener)listener).exitComparator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Hl7V2FilterVisitor ) return ((Hl7V2FilterVisitor<? extends T>)visitor).visitComparator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparatorContext comparator() throws RecognitionException {
		ComparatorContext _localctx = new ComparatorContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_comparator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 6);
		case 1:
			return precpred(_ctx, 5);
		case 2:
			return precpred(_ctx, 4);
		case 3:
			return precpred(_ctx, 3);
		case 4:
			return precpred(_ctx, 2);
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3 \u009b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\66"+
		"\n\2\f\2\16\29\13\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3A\n\3\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\5\4I\n\4\3\5\5\5L\n\5\3\5\5\5O\n\5\3\5\3\5\3\6\7\6T\n\6\f\6\16"+
		"\6W\13\6\3\6\5\6Z\n\6\3\7\3\7\3\7\3\7\3\7\3\7\5\7b\n\7\3\7\3\7\3\7\3\7"+
		"\5\7h\n\7\5\7j\n\7\3\b\5\bm\n\b\3\b\3\b\3\b\3\b\3\b\5\bt\n\b\3\b\5\bw"+
		"\n\b\3\b\3\b\3\t\5\t|\n\t\3\t\3\t\3\t\3\t\3\t\5\t\u0083\n\t\3\t\5\t\u0086"+
		"\n\t\3\n\3\n\3\n\3\n\3\13\6\13\u008d\n\13\r\13\16\13\u008e\3\f\3\f\3\r"+
		"\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\2\3\2\21\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36\2\7\3\2\3\6\3\2\7\b\3\2\16\17\4\2\23\24\36\36\4\2\23"+
		"\23\34\34\2\u00a6\2 \3\2\2\2\4@\3\2\2\2\6H\3\2\2\2\bK\3\2\2\2\nU\3\2\2"+
		"\2\f[\3\2\2\2\16l\3\2\2\2\20{\3\2\2\2\22\u0087\3\2\2\2\24\u008c\3\2\2"+
		"\2\26\u0090\3\2\2\2\30\u0092\3\2\2\2\32\u0094\3\2\2\2\34\u0096\3\2\2\2"+
		"\36\u0098\3\2\2\2 !\b\2\1\2!\"\5\4\3\2\"\67\3\2\2\2#$\f\b\2\2$%\t\2\2"+
		"\2%\66\5\2\2\t&\'\f\7\2\2\'(\t\3\2\2(\66\5\2\2\b)*\f\6\2\2*+\7\t\2\2+"+
		"\66\5\2\2\7,-\f\5\2\2-.\7\n\2\2.\66\5\2\2\6/\60\f\4\2\2\60\61\7\25\2\2"+
		"\61\66\5\2\2\5\62\63\f\3\2\2\63\64\7\26\2\2\64\66\5\2\2\4\65#\3\2\2\2"+
		"\65&\3\2\2\2\65)\3\2\2\2\65,\3\2\2\2\65/\3\2\2\2\65\62\3\2\2\2\669\3\2"+
		"\2\2\67\65\3\2\2\2\678\3\2\2\28\3\3\2\2\29\67\3\2\2\2:A\5\b\5\2;A\5\6"+
		"\4\2<=\7\13\2\2=>\5\2\2\2>?\7\f\2\2?A\3\2\2\2@:\3\2\2\2@;\3\2\2\2@<\3"+
		"\2\2\2A\5\3\2\2\2BI\7\r\2\2CI\t\4\2\2DI\7\37\2\2EI\7\35\2\2FI\7\32\2\2"+
		"GI\7\33\2\2HB\3\2\2\2HC\3\2\2\2HD\3\2\2\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2"+
		"\2I\7\3\2\2\2JL\7\30\2\2KJ\3\2\2\2KL\3\2\2\2LN\3\2\2\2MO\7\31\2\2NM\3"+
		"\2\2\2NO\3\2\2\2OP\3\2\2\2PQ\5\n\6\2Q\t\3\2\2\2RT\5\16\b\2SR\3\2\2\2T"+
		"W\3\2\2\2US\3\2\2\2UV\3\2\2\2VY\3\2\2\2WU\3\2\2\2XZ\5\20\t\2YX\3\2\2\2"+
		"YZ\3\2\2\2Z\13\3\2\2\2[\\\7\20\2\2\\a\5\26\f\2]^\7\13\2\2^_\5\34\17\2"+
		"_`\7\f\2\2`b\3\2\2\2a]\3\2\2\2ab\3\2\2\2bi\3\2\2\2cd\7\20\2\2dg\5\30\r"+
		"\2ef\7\20\2\2fh\5\32\16\2ge\3\2\2\2gh\3\2\2\2hj\3\2\2\2ic\3\2\2\2ij\3"+
		"\2\2\2j\r\3\2\2\2km\7\30\2\2lk\3\2\2\2lm\3\2\2\2mn\3\2\2\2ns\5\24\13\2"+
		"op\7\13\2\2pq\5\34\17\2qr\7\f\2\2rt\3\2\2\2so\3\2\2\2st\3\2\2\2tv\3\2"+
		"\2\2uw\5\22\n\2vu\3\2\2\2vw\3\2\2\2wx\3\2\2\2xy\7\31\2\2y\17\3\2\2\2z"+
		"|\7\30\2\2{z\3\2\2\2{|\3\2\2\2|}\3\2\2\2}\u0082\5\24\13\2~\177\7\13\2"+
		"\2\177\u0080\5\34\17\2\u0080\u0081\7\f\2\2\u0081\u0083\3\2\2\2\u0082~"+
		"\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0086\5\f\7\2\u0085"+
		"\u0084\3\2\2\2\u0085\u0086\3\2\2\2\u0086\21\3\2\2\2\u0087\u0088\7\21\2"+
		"\2\u0088\u0089\5\2\2\2\u0089\u008a\7\22\2\2\u008a\23\3\2\2\2\u008b\u008d"+
		"\t\5\2\2\u008c\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\25\3\2\2\2\u0090\u0091\7\34\2\2\u0091\27\3\2\2\2"+
		"\u0092\u0093\7\34\2\2\u0093\31\3\2\2\2\u0094\u0095\7\34\2\2\u0095\33\3"+
		"\2\2\2\u0096\u0097\t\6\2\2\u0097\35\3\2\2\2\u0098\u0099\7\36\2\2\u0099"+
		"\37\3\2\2\2\24\65\67@HKNUYagilsv{\u0082\u0085\u008e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}