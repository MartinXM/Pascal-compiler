%%

%class scanner
%column
%line
%byaccj

%{   
    private Parser yyparser;
    public scanner (java.io.Reader r, Parser yyparser) {
      this(r);
      this.yyparser = yyparser;
    }
    public int getLine() {  
        return yyline;  
    } 
    public String getText() {  
        return yytext();  
    }
%}

digit=[0-9]
real={digit}+\.{digit}+
number={digit}+
letter=[a-zA-Z]
identifier={letter}+
newline=\n
whitespace=[ \t]+
char=\'.\'
string=\".*\"

%%
"program"       {return Parser.TOKEN_PROGRAM;}
"if"        {return Parser.TOKEN_IF;}
"then"      {return Parser.TOKEN_THEN;}
"else"      {return Parser.TOKEN_ELSE;}
"repeat"    {return Parser.TOKEN_REPEAT;}
"until"     {return Parser.TOKEN_UNTIL;}
"while"     {return Parser.TOKEN_WHILE;}
"do"        {return Parser.TOKEN_DO;}
"case"      {return Parser.TOKEN_CASE;}
"to"        {return Parser.TOKEN_TO;}
"downto"    {return Parser.TOKEN_DOWNTO;}
"for"       {return Parser.TOKEN_FOR;}
"read"      {return Parser.TOKEN_READ;}
"write"     {return Parser.TOKEN_WRITE;}
"writeln"   {return Parser.TOKEN_WRITELN;}
"abs"       {return Parser.TOKEN_ABS;}
"chr"       {return Parser.TOKEN_CHR;}
"odd"       {return Parser.TOKEN_ODD;}
"ord"       {return Parser.TOKEN_ORD;}
"pred"      {return Parser.TOKEN_PRED;}
"sqr"       {return Parser.TOKEN_SQR;}
"sqrt"      {return Parser.TOKEN_SQRT;}
"succ"      {return Parser.TOKEN_SUCC;}

"["         {return Parser.TOKEN_LB;}
"]"         {return Parser.TOKEN_RB;}
";"         {return Parser.TOKEN_SEMI;}
".."        {return Parser.TOKEN_DOTDOT;}
"."         {return Parser.TOKEN_DOT;}
"("         {return Parser.TOKEN_LP;}
")"         {return Parser.TOKEN_RP;}
","         {return Parser.TOKEN_COMMA;}
":"         {return Parser.TOKEN_COLON;}

":="        {return Parser.TOKEN_ASSIGN;}
"="         {return Parser.TOKEN_EQUAL;} 
"+"         {return Parser.TOKEN_PLUS;}
"-"         {return Parser.TOKEN_MINUS;}
"or"        {return Parser.TOKEN_OR;}
"<>"        {return Parser.TOKEN_UNEQUAL;}
">="        {return Parser.TOKEN_GE;}
">"         {return Parser.TOKEN_GT;}
"<="        {return Parser.TOKEN_LE;}
"<"         {return Parser.TOKEN_LT;}
"*"         {return Parser.TOKEN_MUL;}
"/"        {return Parser.TOKEN_DIV;}
"mod"       {return Parser.TOKEN_MOD;}
"and"       {return Parser.TOKEN_AND;}
"not"       {return Parser.TOKEN_NOT;}

"goto"      {return Parser.TOKEN_GOTO;}
"integer"   {return Parser.TOKEN_INTEGER_TYPE;}
"boolean"   {return Parser.TOKEN_BOOLEAN_TYPE;}
"char"      {return Parser.TOKEN_CHAR_TYPE;}
"real"      {return Parser.TOKEN_REAL_TYPE;}
"true"      {return Parser.TOKEN_TRUE;}
"false"     {return Parser.TOKEN_FALSE;}
"maxint"    {return Parser.TOKEN_MAXINT;}
"array"     {return Parser.TOKEN_ARRAY;}
"of"        {return Parser.TOKEN_OF;}
"record"    {return Parser.TOKEN_RECORD;}
"begin"     {return Parser.TOKEN_BEGIN;}
"end"       {return Parser.TOKEN_END;}
"const"     {return Parser.TOKEN_CONST;}
"type"      {return Parser.TOKEN_TYPE;}
"var"       {return Parser.TOKEN_VAR;}
"function"  {return Parser.TOKEN_FUNCTION;}
"procedure" {return Parser.TOKEN_PROCEDURE;}

{number}    {yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));return Parser.TOKEN_INT;}
{real}      {yyparser.yylval = new ParserVal(Double.parseDouble(yytext()));return Parser.TOKEN_REAL;}
{char}      {yyparser.yylval = new ParserVal(yytext());return Parser.TOKEN_CHAR;}
{string}    {yyparser.yylval = new ParserVal(yytext());return Parser.TOKEN_STRING;}
{identifier} {yyparser.yylval = new ParserVal(yytext());return Parser.TOKEN_ID;}
{newline}   {yyline++;}
{whitespace}    {/*ignore*/}

