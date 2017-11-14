// define a grammar called Hello
grammar Hl7V2Filter;


expression
        : term                                                      #termExpression
        | expression ('<=' | '<' | '>' | '>=') expression           #inequalityExpression
        | expression ('=' |  '!=' ) expression                      #equalityExpression
        | expression 'contains' expression                              #containsExpression
        | expression 'matches' expression                               #matchExpression
        | expression AND  expression                                #andExpression
        | expression OR expression                                  #orExpression
        ;

term    : path_expression                                           #pathTerm
        | literal                                                   #literalTerm
        | '(' expression ')'                                        #groupTerm
        ;

literal
        :'null'                                                     #nullLiteral
        | ('true' | 'false')                                        #booleanLiteral
        | STRING                                                    #stringLiteral
        | NUMBER                                                    #numberLiteral
        | DATETIME                                                  #dateTimeLiteral
        | TIME                                                      #timeLiteral
        ;


path_expression
        : DOT? SLASH? segment_path_spec
        ;

segment_path_spec
        : group_rep*  segment_rep?
        ;

field_spec
        : '-' field('(' rep ')')?  ( '-' component ('-' subcomponent )? )?
        ;

group_rep
        : DOT? name_pattern ('(' rep ')')?  constraint? '/'
        ;

segment_rep
        : DOT? name_pattern ('(' rep ')')?  field_spec?
        ;

constraint
        : '[' expression ']'
        ;

name_pattern
        : (IDENTIFIER |'*' | '?')+
        ;

field
        :  INTEGER
        ;

component
        : INTEGER
        ;

subcomponent
        : INTEGER
        ;

rep
        :  INTEGER | '*'
        ;

comparator
        : IDENTIFIER
        ;


AND
        : 'and' | '&&'
        ;
OR
        : 'or' | '||'
        ;
NOT
        : 'not' | '!'
        ;
DOT
        : '.'
        ;
SLASH
        : '/'
        ;
DATETIME
        : '@'
            [0-9][0-9][0-9][0-9] // year
            (
                '-'[0-9][0-9] // month
                (
                    '-'[0-9][0-9] // day
                    (
                        'T' TIMEFORMAT
                    )?
                 )?
             )?
             'Z'? // UTC specifier
        ;

TIME
        : '@' 'T' TIMEFORMAT
        ;

INTEGER
        : [0-9]+
        ;

NUMBER
        : [0-9]+('.' [0-9]+)?
        ;

IDENTIFIER
        : ([A-Za-z])([A-Za-z0-9] | '_')*
        ;

STRING
        : '\'' (ESC | .)*? '\''
        ;

WS
        : [ \r\n\t]+ ->skip;

fragment ESC
        : '\\' (["'\\/fnrt] | UNICODE)    // allow \", \', \\, \/, \f, etc. and \uXXX
        ;

fragment UNICODE
        : 'u' HEX HEX HEX HEX
        ;
fragment HEX
        : [0-9a-fA-F]
        ;

fragment TIMEFORMAT
        :
            [0-9][0-9] (':'[0-9][0-9] (':'[0-9][0-9] ('.'[0-9]+)?)?)?
            ('Z' | ('+' | '-') [0-9][0-9]':'[0-9][0-9])? // timezone
        ;