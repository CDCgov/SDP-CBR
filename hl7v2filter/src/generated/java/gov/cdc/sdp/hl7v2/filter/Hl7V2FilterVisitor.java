// Generated from /Users/bobd/projects/hl7v2filter/Hl7V2Filter.g4 by ANTLR 4.7
package gov.cdc.sdp.hl7v2.filter;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Hl7V2FilterParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Hl7V2FilterVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code matchExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchExpression(Hl7V2FilterParser.MatchExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(Hl7V2FilterParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(Hl7V2FilterParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inequalityExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInequalityExpression(Hl7V2FilterParser.InequalityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code containsExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainsExpression(Hl7V2FilterParser.ContainsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equalityExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(Hl7V2FilterParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link Hl7V2FilterParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermExpression(Hl7V2FilterParser.TermExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pathTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathTerm(Hl7V2FilterParser.PathTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralTerm(Hl7V2FilterParser.LiteralTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code groupTerm}
	 * labeled alternative in {@link Hl7V2FilterParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupTerm(Hl7V2FilterParser.GroupTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(Hl7V2FilterParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(Hl7V2FilterParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(Hl7V2FilterParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLiteral(Hl7V2FilterParser.NumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dateTimeLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimeLiteral(Hl7V2FilterParser.DateTimeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code timeLiteral}
	 * labeled alternative in {@link Hl7V2FilterParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeLiteral(Hl7V2FilterParser.TimeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#path_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath_expression(Hl7V2FilterParser.Path_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#segment_path_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_path_spec(Hl7V2FilterParser.Segment_path_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#field_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_spec(Hl7V2FilterParser.Field_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#group_rep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_rep(Hl7V2FilterParser.Group_repContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#segment_rep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment_rep(Hl7V2FilterParser.Segment_repContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint(Hl7V2FilterParser.ConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#name_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName_pattern(Hl7V2FilterParser.Name_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(Hl7V2FilterParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#component}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponent(Hl7V2FilterParser.ComponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#subcomponent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubcomponent(Hl7V2FilterParser.SubcomponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#rep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRep(Hl7V2FilterParser.RepContext ctx);
	/**
	 * Visit a parse tree produced by {@link Hl7V2FilterParser#comparator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparator(Hl7V2FilterParser.ComparatorContext ctx);
}