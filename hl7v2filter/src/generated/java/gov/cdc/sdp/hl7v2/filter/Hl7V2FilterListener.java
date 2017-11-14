// Generated from /Users/bobd/projects/hl7v2filter/Hl7V2Filter.g4 by ANTLR 4.7
package gov.cdc.sdp.hl7v2.filter;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Hl7V2FilterParser}.
 */
public interface Hl7V2FilterListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code matchExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMatchExpression(Hl7V2FilterParser.MatchExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code matchExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMatchExpression(Hl7V2FilterParser.MatchExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(Hl7V2FilterParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(Hl7V2FilterParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(Hl7V2FilterParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(Hl7V2FilterParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inequalityExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInequalityExpression(Hl7V2FilterParser.InequalityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inequalityExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInequalityExpression(Hl7V2FilterParser.InequalityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code containsExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterContainsExpression(Hl7V2FilterParser.ContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code containsExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitContainsExpression(Hl7V2FilterParser.ContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code equalityExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(Hl7V2FilterParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code equalityExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(Hl7V2FilterParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTermExpression(Hl7V2FilterParser.TermExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTermExpression(Hl7V2FilterParser.TermExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pathTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 */
	void enterPathTerm(Hl7V2FilterParser.PathTermContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pathTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 */
	void exitPathTerm(Hl7V2FilterParser.PathTermContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 */
	void enterLiteralTerm(Hl7V2FilterParser.LiteralTermContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 */
	void exitLiteralTerm(Hl7V2FilterParser.LiteralTermContext ctx);
	/**
	 * Enter a parse tree produced by the {@code groupTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 */
	void enterGroupTerm(Hl7V2FilterParser.GroupTermContext ctx);
	/**
	 * Exit a parse tree produced by the {@code groupTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 */
	void exitGroupTerm(Hl7V2FilterParser.GroupTermContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(Hl7V2FilterParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(Hl7V2FilterParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(Hl7V2FilterParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(Hl7V2FilterParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(Hl7V2FilterParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(Hl7V2FilterParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNumberLiteral(Hl7V2FilterParser.NumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNumberLiteral(Hl7V2FilterParser.NumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dateTimeLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeLiteral(Hl7V2FilterParser.DateTimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dateTimeLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeLiteral(Hl7V2FilterParser.DateTimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timeLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterTimeLiteral(Hl7V2FilterParser.TimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timeLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitTimeLiteral(Hl7V2FilterParser.TimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#path_expression}.
	 * @param ctx the parse tree
	 */
	void enterPath_expression(Hl7V2FilterParser.Path_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#path_expression}.
	 * @param ctx the parse tree
	 */
	void exitPath_expression(Hl7V2FilterParser.Path_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#segment_path_spec}.
	 * @param ctx the parse tree
	 */
	void enterSegment_path_spec(Hl7V2FilterParser.Segment_path_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#segment_path_spec}.
	 * @param ctx the parse tree
	 */
	void exitSegment_path_spec(Hl7V2FilterParser.Segment_path_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#field_spec}.
	 * @param ctx the parse tree
	 */
	void enterField_spec(Hl7V2FilterParser.Field_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#field_spec}.
	 * @param ctx the parse tree
	 */
	void exitField_spec(Hl7V2FilterParser.Field_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#group_rep}.
	 * @param ctx the parse tree
	 */
	void enterGroup_rep(Hl7V2FilterParser.Group_repContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#group_rep}.
	 * @param ctx the parse tree
	 */
	void exitGroup_rep(Hl7V2FilterParser.Group_repContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#segment_rep}.
	 * @param ctx the parse tree
	 */
	void enterSegment_rep(Hl7V2FilterParser.Segment_repContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#segment_rep}.
	 * @param ctx the parse tree
	 */
	void exitSegment_rep(Hl7V2FilterParser.Segment_repContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(Hl7V2FilterParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(Hl7V2FilterParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#name_pattern}.
	 * @param ctx the parse tree
	 */
	void enterName_pattern(Hl7V2FilterParser.Name_patternContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#name_pattern}.
	 * @param ctx the parse tree
	 */
	void exitName_pattern(Hl7V2FilterParser.Name_patternContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(Hl7V2FilterParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(Hl7V2FilterParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#component}.
	 * @param ctx the parse tree
	 */
	void enterComponent(Hl7V2FilterParser.ComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#component}.
	 * @param ctx the parse tree
	 */
	void exitComponent(Hl7V2FilterParser.ComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#subcomponent}.
	 * @param ctx the parse tree
	 */
	void enterSubcomponent(Hl7V2FilterParser.SubcomponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#subcomponent}.
	 * @param ctx the parse tree
	 */
	void exitSubcomponent(Hl7V2FilterParser.SubcomponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#rep}.
	 * @param ctx the parse tree
	 */
	void enterRep(Hl7V2FilterParser.RepContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#rep}.
	 * @param ctx the parse tree
	 */
	void exitRep(Hl7V2FilterParser.RepContext ctx);
	/**
	 * Enter a parse tree produced by {@link Hl7V2FilterParser#comparator}.
	 * @param ctx the parse tree
	 */
	void enterComparator(Hl7V2FilterParser.ComparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Hl7V2FilterParser#comparator}.
	 * @param ctx the parse tree
	 */
	void exitComparator(Hl7V2FilterParser.ComparatorContext ctx);
}