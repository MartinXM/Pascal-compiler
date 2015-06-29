package ylex;

//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "tiny.y"
/*#define YYPARSER*/
import java.io.*; 

import codegeneration.CodeGenerator;
import tree.*; 

//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short TOKEN_PROGRAM=257;
public final static short TOKEN_FUNCTION=258;
public final static short TOKEN_PROCEDURE=259;
public final static short TOKEN_CONST=260;
public final static short TOKEN_TYPE=261;
public final static short TOKEN_VAR=262;
public final static short TOKEN_IF=263;
public final static short TOKEN_THEN=264;
public final static short TOKEN_ELSE=265;
public final static short TOKEN_REPEAT=266;
public final static short TOKEN_UNTIL=267;
public final static short TOKEN_WHILE=268;
public final static short TOKEN_DO=269;
public final static short TOKEN_CASE=270;
public final static short TOKEN_TO=271;
public final static short TOKEN_DOWNTO=272;
public final static short TOKEN_FOR=273;
public final static short TOKEN_EQUAL=274;
public final static short TOKEN_UNEQUAL=275;
public final static short TOKEN_GE=276;
public final static short TOKEN_GT=277;
public final static short TOKEN_LE=278;
public final static short TOKEN_LT=279;
public final static short TOKEN_ASSIGN=280;
public final static short TOKEN_PLUS=281;
public final static short TOKEN_MINUS=282;
public final static short TOKEN_MUL=283;
public final static short TOKEN_DIV=284;
public final static short TOKEN_OR=285;
public final static short TOKEN_AND=286;
public final static short TOKEN_NOT=287;
public final static short TOKEN_MOD=288;
public final static short TOKEN_READ=289;
public final static short TOKEN_WRITE=290;
public final static short TOKEN_WRITELN=291;
public final static short TOKEN_LB=292;
public final static short TOKEN_RB=293;
public final static short TOKEN_SEMI=294;
public final static short TOKEN_DOT=295;
public final static short TOKEN_DOTDOT=296;
public final static short TOKEN_LP=297;
public final static short TOKEN_RP=298;
public final static short TOKEN_COMMA=299;
public final static short TOKEN_COLON=300;
public final static short TOKEN_INTEGER_TYPE=301;
public final static short TOKEN_BOOLEAN_TYPE=302;
public final static short TOKEN_CHAR_TYPE=303;
public final static short TOKEN_REAL_TYPE=304;
public final static short TOKEN_TRUE=305;
public final static short TOKEN_FALSE=306;
public final static short TOKEN_MAXINT=307;
public final static short TOKEN_ARRAY=308;
public final static short TOKEN_OF=309;
public final static short TOKEN_RECORD=310;
public final static short TOKEN_BEGIN=311;
public final static short TOKEN_END=312;
public final static short TOKEN_GOTO=313;
public final static short TOKEN_ID=314;
public final static short TOKEN_CHAR=315;
public final static short TOKEN_STRING=316;
public final static short TOKEN_INT=317;
public final static short TOKEN_REAL=318;
public final static short ERROR=319;
public final static short TOKEN_ABS=320;
public final static short TOKEN_CHR=321;
public final static short TOKEN_ODD=322;
public final static short TOKEN_ORD=323;
public final static short TOKEN_PRED=324;
public final static short TOKEN_SQR=325;
public final static short TOKEN_SQRT=326;
public final static short TOKEN_SUCC=327;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    2,    3,    3,    3,    3,    3,    4,    5,
    6,    6,    7,    7,    8,    8,    9,   10,   11,   11,
   12,   12,   13,   14,   14,   15,   15,   16,   17,   17,
   17,   17,   17,   17,   17,   18,   18,   19,   19,   20,
   21,   21,   21,   22,   23,   23,   24,   25,   26,   26,
   26,   26,   26,   26,   26,   26,   26,   26,   27,   27,
   28,   29,   30,   31,   31,   49,   49,   32,   32,   32,
   32,   32,   32,   32,   32,   32,   33,   33,   33,   34,
   35,   36,   36,   37,   37,   47,   38,   39,   39,   40,
   40,   41,   41,   42,   42,   42,   42,   42,   42,   43,
   43,   48,   48,   48,   48,   48,   48,   48,   44,   44,
   44,   44,   45,   45,   45,   45,   45,   46,   46,   46,
   46,   46,   46,   46,   46,   46,   46,   46,   46,   46,
   46,   46,   46,
};
final static short yylen[] = {                            2,
    5,    2,    4,    0,    2,    2,    1,    1,    4,    5,
    0,    3,    3,    1,    4,    3,    4,    3,    0,    2,
    2,    1,    4,    0,    2,    2,    1,    4,    1,    1,
    1,    1,    1,    1,    1,    0,    2,    2,    1,    4,
    1,    1,    1,    3,    2,    1,    4,    6,    1,    3,
    3,    4,    5,    3,    1,    1,    1,    1,    3,    1,
    1,    1,    3,    0,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    6,    5,    2,
    5,    0,    2,    4,    0,    4,    5,    2,    1,    4,
    4,    8,    8,    1,    4,    4,    4,    1,    4,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    3,    1,    3,    3,    3,    3,    1,    1,    4,    1,
    3,    2,    2,    4,    3,    4,    4,    4,    4,    4,
    4,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,    0,
   27,    0,    1,   64,    2,   62,    0,    0,   26,    0,
    0,    0,   39,    0,    0,    0,   33,   34,   35,   31,
   32,   29,   30,    0,    0,   64,    0,    0,    0,    0,
    0,    0,   63,    0,    0,    0,   69,   67,   68,   70,
   71,   72,   74,   75,   76,   73,    0,   38,    0,    0,
   22,    0,   60,    0,    0,    0,    7,    0,    8,    0,
   28,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  120,    0,    0,    0,  117,    0,    0,    0,
    0,    0,    0,    0,    0,   80,    0,    0,    0,    0,
    0,   65,    0,    0,   55,   56,   58,   57,    0,    0,
    0,    0,   43,   42,   41,    0,   21,    0,    0,    0,
    0,    5,    6,    0,    0,  123,  122,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   66,    0,    0,    0,    0,    0,    0,    0,    0,
   46,    0,    0,   40,    0,   59,    0,    0,    0,   18,
    0,    0,  121,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  125,    0,    0,    0,    0,  113,  114,  116,
  115,    0,    0,    0,    0,    0,    0,    0,    0,   86,
    0,    0,    0,   89,    0,   96,   97,    0,   99,    0,
    0,   95,    0,   50,    0,   44,   45,    0,   51,   54,
   23,    0,    0,   14,    0,    0,    9,   17,  126,  127,
  128,  129,  130,  131,  132,  133,  124,  119,    0,   81,
    0,    0,   87,   88,    0,    0,    0,    0,    0,    0,
   52,    0,    0,    0,    0,   12,    0,   10,   83,    0,
    0,    0,    0,    0,   53,    0,   47,    0,   13,   16,
   90,   91,    0,    0,   48,   15,   92,   93,
};
final static short yydgoto[] = {                          2,
    6,    7,   66,   67,   68,  179,  233,  234,   69,   70,
   26,   60,   61,    8,   10,   11,   83,   18,   22,   23,
  112,  113,  170,  171,  114,  115,   62,   84,   15,   47,
   21,   48,   49,   50,   51,  250,   52,   53,  213,  214,
   54,   55,  159,   85,   86,   87,   56,  160,   57,
};
final static short yysindex[] = {                      -253,
 -299,    0, -263, -215, -257, -223, -213, -185,    0, -257,
    0, -195,    0,    0,    0,    0, -257, -174,    0,  523,
  -76, -257,    0, -144, -257, -140,    0,    0,    0,    0,
    0,    0,    0, -198,   70,    0,   70,   70, -257, -197,
 -152, -149,    0, -164, -157, -148,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -137,    0,  503, -257,
    0, -128,    0, -145, -119, -140,    0,  -87,    0,  -72,
    0,   70,   70,   70,  -85,  -79,  -71,  -67,  -63,  -52,
  -48,  -45,    0, -134, -154,  -95,    0,  104, -220,  289,
   -9, -120,   70,   70,   70,    0,   14,   70,   70, -257,
   70,    0,  523, -257,    0,    0,    0,    0,  -38, -257,
  -24,  -11,    0,    0,    0,   -8,    0, -257,  503,   -2,
   -2,    0,    0, -215, -215,    0,    0,   15,   70,   70,
   70,   70,   70,   70,   70,   70,   70, -257,   70,   70,
   70,   70,   70,   70,   70,   70,  -15,   70,   70,   70,
   70,   70,   70,   70,  -15,   55,   70,   -1, -116,  182,
 -113,    0,  182,   33,    1, -100,    5,  -98,  530, -202,
    0,  -96,  523,    0, -257,    0,   12, -233,   24,    0,
   41,   44,    0,  -89,  -82,  -78,  -74,  -70,  -66,  -42,
  -35,   40,    0,  -13,  -95,  -95,  -95,    0,    0,    0,
    0,   80, -154, -154, -154, -154, -154, -154,  182,    0,
   89,  100,  544,    0,  146,    0,    0,   70,    0,   76,
   70,    0, -181,    0,  108,    0,    0,  503,    0,    0,
    0, -257, -232,    0,   21,  530,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -15,    0,
  -15,  -15,    0,    0,   70,   70,  182,   70,  182,  523,
    0,   93,  110,   23, -233,    0,  530,    0,    0,  113,
  120,  325,  361,  182,    0,  503,    0,  530,    0,    0,
    0,    0,  -15,  -15,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0, -248,    0,    0,    0, -221,    0, -234,
    0,    0,    0,    0,    0,    0,    0, -250,    0,    0,
  132, -194,    0,    0,    0,  118,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -242,    0,    0,    0, -235,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -226,
    0,    0,    0,    0,    0,  119,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   65,  278,  134,    0,    0,  132,    0,
    0,    0,    0,    0,    0,    0, -216,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -254,    0,    0,    0,  131,
  142,    0,    0, -248, -248,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -216,    0,    0,    0,
    0,    0,    0,    0, -216,    0,    0,    0,    0,   56,
    0,    0, -214,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  170,  206,  242,    0,    0,    0,
    0,  143,  314,  350,  386,  422,  458,  494, -205,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -216,    0,
  132,  132,    0,    0,    0,    0,   67,    0, -191,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -178,    0,    0,    0,    0,    0,    0,
    0,    0, -216, -216,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  207,    0,    0,  372,    0,  319,    0,  185,  387,    0,
    0,    0,  394,    0,    0,  452,  -17,    0,    0,  443,
 -117,    0,    0,  296,    0, -163, -103,   -5,    0,  460,
  436,  376,    0,    0,    0,    0,    0,    0,    0,  261,
    0,    0,   45,  344,   26,  -37,    0,  -16, -129,
};
final static int YYTABLESIZE=862;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
  168,  177,   34,    1,   12,  225,  172,   19,   19,   24,
   24,   24,   24,   24,    3,   46,   24,  202,   88,   63,
   90,   91,   98,   25,   25,  210,   25,   25,  232,   94,
    4,   20,   20,   92,  126,  127,   36,   36,   49,   49,
   36,  111,   35,   49,    5,   36,  154,   37,   85,   38,
   77,   98,   39,  116,   63,  158,    9,  128,   94,   84,
   19,  265,   24,   37,   37,  266,  172,   37,   40,   41,
   42,   13,  268,   79,  235,   17,   25,   85,   20,   77,
    9,  163,  164,   46,   20,  167,   78,   25,   84,   36,
   14,   46,   44,    9,  165,   71,   45,   14,   63,   93,
  260,  111,   79,  280,   63,  198,  199,  200,  201,  226,
  263,    9,  176,  116,  286,   78,   37,   64,   65,  269,
  192,  270,  271,   27,   28,   29,  140,  141,  264,   59,
  142,   98,  193,   30,   31,   32,   33,  209,  211,  161,
  215,   46,   97,   99,   94,  166,  100,   95,  101,   46,
  212,  111,   96,  287,  288,  229,  102,  137,  285,  157,
  138,  235,  139,  116,   63,  195,  196,  197,  120,  230,
  118,  119,   63,  184,  185,  186,  187,  188,  189,  190,
  191,  217,  218,  194,  219,  218,   35,  143,  144,   36,
  145,   37,  146,   38,  121,  211,   39,  222,  218,  224,
  118,  257,  118,  228,  259,  261,  124,  212,  239,  218,
  111,  129,   40,   41,   42,  240,  218,  130,  111,  241,
  218,  125,  116,  242,  218,  131,   63,  243,  218,  132,
  116,  244,  218,  133,   14,   43,   44,    9,  272,  273,
   45,  274,  275,   46,  134,   46,   46,   35,  135,  111,
   36,  136,   37,  169,   38,  245,  218,   39,  111,   63,
  111,  116,  246,  218,  148,  149,  150,  151,  152,  153,
  116,  173,  116,   40,   41,   42,   35,   46,   46,   36,
  221,   37,  174,   38,  248,  218,   39,  175,  148,  149,
  150,  151,  152,  153,  178,   14,  216,   44,    9,  156,
  223,   45,   40,   41,   42,  231,  148,  149,  150,  151,
  152,  153,  183,  148,  149,  150,  151,  152,  153,  118,
  267,  118,  278,  236,   14,  220,   44,    9,  118,  118,
  181,  182,  247,  118,  237,  118,  118,  238,  118,  118,
  118,  118,  118,  118,  249,  118,  118,  118,  118,  118,
  118,   72,  118,  101,  101,  258,   73,  118,  118,   27,
   28,   29,  118,  118,  100,  100,   74,  147,    9,   30,
   31,   32,   33,  118,   27,   28,   29,  148,  149,  150,
  151,  152,  153,    9,   30,   31,   32,   33,  251,   75,
   76,   77,   78,   79,   80,   81,   82,  112,  112,  252,
  262,  276,  112,  277,  112,  112,  281,  112,  112,  112,
  112,  112,  112,  282,  112,  112,  255,  256,  112,  148,
  149,  150,  151,  152,  153,   85,  112,  112,    4,    3,
   11,  112,  112,  109,  109,   11,   82,  122,  109,  180,
  109,  109,  112,  109,  109,  109,  109,  109,  109,  279,
  109,  109,  123,  117,  109,  148,  149,  150,  151,  152,
  153,   19,  109,  109,   58,  227,   16,  109,  109,  110,
  110,   89,  162,  254,  110,    0,  110,  110,  109,  110,
  110,  110,  110,  110,  110,    0,  110,  110,    0,    0,
  110,  203,  204,  205,  206,  207,  208,    0,  110,  110,
    0,    0,    0,  110,  110,  111,  111,    0,    0,    0,
  111,    0,  111,  111,  110,  111,  111,  111,  111,  111,
  111,    0,  111,  111,    0,    0,  111,    0,    0,    0,
    0,    0,    0,    0,  111,  111,    0,    0,    0,  111,
  111,  108,  108,    0,    0,    0,  108,    0,  108,  108,
  111,  108,  108,  108,  108,  108,  108,  155,    0,    0,
    0,    0,  148,  149,  150,  151,  152,  153,    0,    0,
  108,  108,    0,    0,    0,  108,  108,  106,  106,    0,
    0,    0,  106,    0,  106,  106,  108,  106,  106,  106,
  106,  106,  106,  283,    0,    0,    0,    0,  148,  149,
  150,  151,  152,  153,    0,    0,  106,  106,    0,    0,
    0,  106,  106,  107,  107,    0,    0,    0,  107,    0,
  107,  107,  106,  107,  107,  107,  107,  107,  107,  284,
    0,    0,    0,    0,  148,  149,  150,  151,  152,  153,
    0,    0,  107,  107,    0,    0,    0,  107,  107,  102,
  102,    0,    0,    0,  102,    0,  102,  102,  107,  102,
  102,  102,  102,  102,  102,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  102,  102,
    0,    0,    0,  102,  102,  103,  103,    0,    0,    0,
  103,    0,  103,  103,  102,  103,  103,  103,  103,  103,
  103,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  103,  103,    0,    0,    0,  103,
  103,  104,  104,    0,    0,    0,  104,    0,  104,  104,
  103,  104,  104,  104,  104,  104,  104,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  104,  104,    0,    0,    0,  104,  104,  105,  105,    0,
    0,    0,  105,    0,  105,  105,  104,  105,  105,  105,
  105,  105,  105,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  103,    0,  105,  105,    0,    0,
    0,  105,  105,    0,    0,    0,    0,    0,    0,  104,
    0,    0,  105,  105,  106,  107,  108,   27,   28,   29,
  109,  103,  110,    0,    0,    0,    9,   30,   31,   32,
   33,    0,    0,    0,    0,    0,  104,   27,   28,   29,
  105,  106,  107,  108,   27,   28,   29,   30,   31,   32,
   33,    0,    0,    9,   30,   31,   32,   33,   27,   28,
   29,    0,    0,    0,    0,  253,    0,    9,   30,   31,
   32,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
  104,  119,   20,  257,   10,  169,  110,  258,  259,  258,
  259,   17,  261,  262,  314,   21,   22,  147,   35,   25,
   37,   38,  265,  258,  259,  155,  261,  262,  262,  265,
  294,  258,  259,   39,   72,   73,  258,  259,  293,  294,
  262,   59,  263,  298,  260,  266,  267,  268,  265,  270,
  265,  294,  273,   59,   60,   93,  314,   74,  294,  265,
  311,  294,  311,  258,  259,  298,  170,  262,  289,  290,
  291,  295,  236,  265,  178,  261,  311,  294,  274,  294,
  314,   98,   99,   89,  311,  103,  265,  262,  294,  311,
  311,   97,  313,  314,  100,  294,  317,  311,  104,  297,
  282,  119,  294,  267,  110,  143,  144,  145,  146,  312,
  228,  314,  118,  119,  278,  294,  311,  258,  259,  249,
  137,  251,  252,  305,  306,  307,  281,  282,  232,  274,
  285,  280,  138,  315,  316,  317,  318,  154,  156,   95,
  157,  147,  300,  292,  297,  101,  295,  297,  297,  155,
  156,  169,  317,  283,  284,  173,  294,  292,  276,  280,
  295,  265,  297,  169,  170,  140,  141,  142,  314,  175,
  299,  300,  178,  129,  130,  131,  132,  133,  134,  135,
  136,  298,  299,  139,  298,  299,  263,  283,  284,  266,
  286,  268,  288,  270,  314,  213,  273,  298,  299,  298,
  299,  218,  299,  300,  221,  223,  294,  213,  298,  299,
  228,  297,  289,  290,  291,  298,  299,  297,  236,  298,
  299,  294,  228,  298,  299,  297,  232,  298,  299,  297,
  236,  298,  299,  297,  311,  312,  313,  314,  255,  256,
  317,  258,  260,  249,  297,  251,  252,  263,  297,  267,
  266,  297,  268,  292,  270,  298,  299,  273,  276,  265,
  278,  267,  298,  299,  274,  275,  276,  277,  278,  279,
  276,  296,  278,  289,  290,  291,  263,  283,  284,  266,
  280,  268,  294,  270,  298,  299,  273,  296,  274,  275,
  276,  277,  278,  279,  297,  311,  298,  313,  314,  309,
  296,  317,  289,  290,  291,  294,  274,  275,  276,  277,
  278,  279,  298,  274,  275,  276,  277,  278,  279,  299,
  300,  299,  300,  300,  311,  293,  313,  314,  264,  265,
  124,  125,  293,  269,  294,  271,  272,  294,  274,  275,
  276,  277,  278,  279,  265,  281,  282,  283,  284,  285,
  286,  282,  288,  298,  299,  280,  287,  293,  294,  305,
  306,  307,  298,  299,  298,  299,  297,  264,  314,  315,
  316,  317,  318,  309,  305,  306,  307,  274,  275,  276,
  277,  278,  279,  314,  315,  316,  317,  318,  300,  320,
  321,  322,  323,  324,  325,  326,  327,  264,  265,  300,
  293,  309,  269,  294,  271,  272,  294,  274,  275,  276,
  277,  278,  279,  294,  281,  282,  271,  272,  285,  274,
  275,  276,  277,  278,  279,  294,  293,  294,  311,  311,
  300,  298,  299,  264,  265,  294,  294,   66,  269,  121,
  271,  272,  309,  274,  275,  276,  277,  278,  279,  265,
  281,  282,   66,   60,  285,  274,  275,  276,  277,  278,
  279,   10,  293,  294,   22,  170,    7,  298,  299,  264,
  265,   36,   97,  213,  269,   -1,  271,  272,  309,  274,
  275,  276,  277,  278,  279,   -1,  281,  282,   -1,   -1,
  285,  148,  149,  150,  151,  152,  153,   -1,  293,  294,
   -1,   -1,   -1,  298,  299,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,  272,  309,  274,  275,  276,  277,  278,
  279,   -1,  281,  282,   -1,   -1,  285,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  293,  294,   -1,   -1,   -1,  298,
  299,  264,  265,   -1,   -1,   -1,  269,   -1,  271,  272,
  309,  274,  275,  276,  277,  278,  279,  269,   -1,   -1,
   -1,   -1,  274,  275,  276,  277,  278,  279,   -1,   -1,
  293,  294,   -1,   -1,   -1,  298,  299,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,  272,  309,  274,  275,  276,
  277,  278,  279,  269,   -1,   -1,   -1,   -1,  274,  275,
  276,  277,  278,  279,   -1,   -1,  293,  294,   -1,   -1,
   -1,  298,  299,  264,  265,   -1,   -1,   -1,  269,   -1,
  271,  272,  309,  274,  275,  276,  277,  278,  279,  269,
   -1,   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,
   -1,   -1,  293,  294,   -1,   -1,   -1,  298,  299,  264,
  265,   -1,   -1,   -1,  269,   -1,  271,  272,  309,  274,
  275,  276,  277,  278,  279,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  293,  294,
   -1,   -1,   -1,  298,  299,  264,  265,   -1,   -1,   -1,
  269,   -1,  271,  272,  309,  274,  275,  276,  277,  278,
  279,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  293,  294,   -1,   -1,   -1,  298,
  299,  264,  265,   -1,   -1,   -1,  269,   -1,  271,  272,
  309,  274,  275,  276,  277,  278,  279,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  293,  294,   -1,   -1,   -1,  298,  299,  264,  265,   -1,
   -1,   -1,  269,   -1,  271,  272,  309,  274,  275,  276,
  277,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  282,   -1,  293,  294,   -1,   -1,
   -1,  298,  299,   -1,   -1,   -1,   -1,   -1,   -1,  297,
   -1,   -1,  309,  301,  302,  303,  304,  305,  306,  307,
  308,  282,  310,   -1,   -1,   -1,  314,  315,  316,  317,
  318,   -1,   -1,   -1,   -1,   -1,  297,  305,  306,  307,
  301,  302,  303,  304,  305,  306,  307,  315,  316,  317,
  318,   -1,   -1,  314,  315,  316,  317,  318,  305,  306,
  307,   -1,   -1,   -1,   -1,  312,   -1,  314,  315,  316,
  317,  318,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=327;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"TOKEN_PROGRAM","TOKEN_FUNCTION","TOKEN_PROCEDURE","TOKEN_CONST",
"TOKEN_TYPE","TOKEN_VAR","TOKEN_IF","TOKEN_THEN","TOKEN_ELSE","TOKEN_REPEAT",
"TOKEN_UNTIL","TOKEN_WHILE","TOKEN_DO","TOKEN_CASE","TOKEN_TO","TOKEN_DOWNTO",
"TOKEN_FOR","TOKEN_EQUAL","TOKEN_UNEQUAL","TOKEN_GE","TOKEN_GT","TOKEN_LE",
"TOKEN_LT","TOKEN_ASSIGN","TOKEN_PLUS","TOKEN_MINUS","TOKEN_MUL","TOKEN_DIV",
"TOKEN_OR","TOKEN_AND","TOKEN_NOT","TOKEN_MOD","TOKEN_READ","TOKEN_WRITE",
"TOKEN_WRITELN","TOKEN_LB","TOKEN_RB","TOKEN_SEMI","TOKEN_DOT","TOKEN_DOTDOT",
"TOKEN_LP","TOKEN_RP","TOKEN_COMMA","TOKEN_COLON","TOKEN_INTEGER_TYPE",
"TOKEN_BOOLEAN_TYPE","TOKEN_CHAR_TYPE","TOKEN_REAL_TYPE","TOKEN_TRUE",
"TOKEN_FALSE","TOKEN_MAXINT","TOKEN_ARRAY","TOKEN_OF","TOKEN_RECORD",
"TOKEN_BEGIN","TOKEN_END","TOKEN_GOTO","TOKEN_ID","TOKEN_CHAR","TOKEN_STRING",
"TOKEN_INT","TOKEN_REAL","ERROR","TOKEN_ABS","TOKEN_CHR","TOKEN_ODD",
"TOKEN_ORD","TOKEN_PRED","TOKEN_SQR","TOKEN_SQRT","TOKEN_SUCC",
};
final static String yyrule[] = {
"$accept : program",
"program : TOKEN_PROGRAM TOKEN_ID TOKEN_SEMI routine TOKEN_DOT",
"routine : routine_head routine_body",
"routine_head : const_part type_part var_part routine_part",
"routine_part :",
"routine_part : routine_part function_decl",
"routine_part : routine_part procedure_decl",
"routine_part : function_decl",
"routine_part : procedure_decl",
"function_decl : function_head TOKEN_SEMI routine TOKEN_SEMI",
"function_head : TOKEN_FUNCTION TOKEN_ID parameters TOKEN_COLON simple_type_decl",
"parameters :",
"parameters : TOKEN_LP para_decl_list TOKEN_RP",
"para_decl_list : para_decl_list TOKEN_SEMI para_type_list",
"para_decl_list : para_type_list",
"para_type_list : TOKEN_VAR name_list TOKEN_COLON simple_type_decl",
"para_type_list : name_list TOKEN_COLON simple_type_decl",
"procedure_decl : procedure_head TOKEN_SEMI routine TOKEN_SEMI",
"procedure_head : TOKEN_PROCEDURE TOKEN_ID parameters",
"var_part :",
"var_part : TOKEN_VAR var_decl_list",
"var_decl_list : var_decl_list var_decl",
"var_decl_list : var_decl",
"var_decl : name_list TOKEN_COLON type_decl TOKEN_SEMI",
"const_part :",
"const_part : TOKEN_CONST const_expr_list",
"const_expr_list : const_expr_list const_expr",
"const_expr_list : const_expr",
"const_expr : ID TOKEN_EQUAL const_value TOKEN_SEMI",
"const_value : TOKEN_INT",
"const_value : TOKEN_REAL",
"const_value : TOKEN_CHAR",
"const_value : TOKEN_STRING",
"const_value : TOKEN_TRUE",
"const_value : TOKEN_FALSE",
"const_value : TOKEN_MAXINT",
"type_part :",
"type_part : TOKEN_TYPE type_decl_list",
"type_decl_list : type_decl_list type_definition",
"type_decl_list : type_definition",
"type_definition : ID TOKEN_EQUAL type_decl TOKEN_SEMI",
"type_decl : simple_type_decl",
"type_decl : array_type_decl",
"type_decl : record_type_decl",
"record_type_decl : TOKEN_RECORD field_decl_list TOKEN_END",
"field_decl_list : field_decl_list field_decl",
"field_decl_list : field_decl",
"field_decl : name_list TOKEN_COLON type_decl TOKEN_SEMI",
"array_type_decl : TOKEN_ARRAY TOKEN_LB simple_type_decl TOKEN_RB TOKEN_OF type_decl",
"simple_type_decl : ID",
"simple_type_decl : TOKEN_LP name_list TOKEN_RP",
"simple_type_decl : const_value TOKEN_DOTDOT const_value",
"simple_type_decl : TOKEN_MINUS const_value TOKEN_DOTDOT const_value",
"simple_type_decl : TOKEN_MINUS const_value TOKEN_DOTDOT TOKEN_MINUS const_value",
"simple_type_decl : ID TOKEN_DOTDOT ID",
"simple_type_decl : TOKEN_INTEGER_TYPE",
"simple_type_decl : TOKEN_BOOLEAN_TYPE",
"simple_type_decl : TOKEN_REAL_TYPE",
"simple_type_decl : TOKEN_CHAR_TYPE",
"name_list : name_list TOKEN_COMMA ID",
"name_list : ID",
"ID : TOKEN_ID",
"routine_body : compound_stmt",
"compound_stmt : TOKEN_BEGIN stmt_list TOKEN_END",
"stmt_list :",
"stmt_list : stmt_list stmt TOKEN_SEMI",
"stmt : TOKEN_INT TOKEN_COLON no_label_stmt",
"stmt : no_label_stmt",
"no_label_stmt : assign_stmt",
"no_label_stmt : compound_stmt",
"no_label_stmt : goto_stmt",
"no_label_stmt : if_stmt",
"no_label_stmt : repeat_stmt",
"no_label_stmt : while_stmt",
"no_label_stmt : case_stmt",
"no_label_stmt : for_stmt",
"no_label_stmt : proc_stmt",
"assign_stmt : ID TOKEN_ASSIGN expression",
"assign_stmt : ID TOKEN_LB expression TOKEN_RB TOKEN_ASSIGN expression",
"assign_stmt : ID TOKEN_DOT ID TOKEN_ASSIGN expression",
"goto_stmt : TOKEN_GOTO TOKEN_INT",
"if_stmt : TOKEN_IF expression TOKEN_THEN stmt else_clause",
"else_clause :",
"else_clause : TOKEN_ELSE stmt",
"repeat_stmt : TOKEN_REPEAT stmt_list TOKEN_UNTIL expression",
"repeat_stmt :",
"while_stmt : TOKEN_WHILE expression TOKEN_DO stmt",
"case_stmt : TOKEN_CASE expression TOKEN_OF case_expr_list TOKEN_END",
"case_expr_list : case_expr_list case_expr",
"case_expr_list : case_expr",
"case_expr : const_value TOKEN_COLON stmt TOKEN_SEMI",
"case_expr : ID TOKEN_COLON stmt TOKEN_SEMI",
"for_stmt : TOKEN_FOR ID TOKEN_ASSIGN expression TOKEN_TO expression TOKEN_DO stmt",
"for_stmt : TOKEN_FOR ID TOKEN_ASSIGN expression TOKEN_DOWNTO expression TOKEN_DO stmt",
"proc_stmt : ID",
"proc_stmt : ID TOKEN_LP args_list TOKEN_RP",
"proc_stmt : TOKEN_READ TOKEN_LP factor TOKEN_RP",
"proc_stmt : TOKEN_WRITE TOKEN_LP args_list TOKEN_RP",
"proc_stmt : TOKEN_WRITELN",
"proc_stmt : TOKEN_WRITELN TOKEN_LP args_list TOKEN_RP",
"args_list : args_list TOKEN_COMMA expression",
"args_list : expression",
"expression : expression TOKEN_GE expr",
"expression : expression TOKEN_GT expr",
"expression : expression TOKEN_LE expr",
"expression : expression TOKEN_LT expr",
"expression : expression TOKEN_EQUAL expr",
"expression : expression TOKEN_UNEQUAL expr",
"expression : expr",
"expr : expr TOKEN_PLUS term",
"expr : expr TOKEN_MINUS term",
"expr : expr TOKEN_OR term",
"expr : term",
"term : term TOKEN_MUL factor",
"term : term TOKEN_DIV factor",
"term : term TOKEN_MOD factor",
"term : term TOKEN_AND factor",
"term : factor",
"factor : ID",
"factor : ID TOKEN_LP args_list TOKEN_RP",
"factor : const_value",
"factor : TOKEN_LP expression TOKEN_RP",
"factor : TOKEN_NOT factor",
"factor : TOKEN_MINUS factor",
"factor : ID TOKEN_LB expression TOKEN_RB",
"factor : ID TOKEN_DOT ID",
"factor : TOKEN_ABS TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_CHR TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_ODD TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_ORD TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_PRED TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_SQR TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_SQRT TOKEN_LP args_list TOKEN_RP",
"factor : TOKEN_SUCC TOKEN_LP args_list TOKEN_RP",
};

//#line 623 "tiny.y"

     private scanner lexer;  
    private TreeNode savedTree;
    private char[] savedName;
    private int savedNum;
    private int yyline;
    /* interface to the lexer */  
    private int yylex() {  
        int retVal = -1;  
        try {  
            retVal = lexer.yylex();
            yyline=lexer.getLine();
        } catch (IOException e) {  
            System.err.println("IO Error:" + e);  
        }  
        return retVal;  
    }  
      
    /* error reporting */  
    /* constructor taking in File Input */  
    public Parser (Reader r) {  
        lexer = new scanner (r, this);  
    }  
    public TreeNode parse(){
        yyparse();
        return this.savedTree;
    }
    static boolean interactive;
    public static void main (String [] args) throws IOException {  
       Parser yyparser;
//      if ( args.length > 0 ) {
            // parse a file
        yyparser = new Parser(new FileReader("C:/Users/mwindson/compiler/Pascal-compiler/test/calculate"));
//      }
//      else {
//          // interactive mode
//          System.out.println("[Quit with CTRL-D]");
//          System.out.print("Expression: ");
//          interactive = true;
//          yyparser = new Parser(new InputStreamReader(System.in));
//      }
        System.err.println("YACC: Parsing...");
        //yyparser.yyparse();
        TreeNode syntaxTree = yyparser.parse();
        System.out.println(syntaxTree);
        System.err.println("YACC: Parsed...");
        syntaxTree.printTree(syntaxTree);
        CodeGenerator.getCodeGenerator().generate(syntaxTree);
        System.err.println("code generation end");
    }  

        /* error reporting */  
    public void yyerror (String error) {  
        System.err.println("Error : " + error + " at line " + lexer.getLine());  
    }  
//#line 715 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 30 "tiny.y"
{   yyval.node = val_peek(1).node;
                            yyval.node.setAttribute(val_peek(3).sval);
                            savedTree = yyval.node;
                        }
break;
case 2:
//#line 35 "tiny.y"
{
                            yyval.node =val_peek(1).node;
                            yyval.node.setSibling(val_peek(0).node);
                        }
break;
case 3:
//#line 40 "tiny.y"
{
                            yyval.node = new TreeNode(DeclKind.ROUTINEHEAD,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(1).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 4:
//#line 48 "tiny.y"
{   yyval.node= null;}
break;
case 5:
//#line 50 "tiny.y"
{   TreeNode t=val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 6:
//#line 61 "tiny.y"
{   TreeNode t=val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 7:
//#line 71 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 8:
//#line 72 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 9:
//#line 75 "tiny.y"
{
                            yyval.node=new TreeNode(DeclKind.FUNCTION,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 10:
//#line 82 "tiny.y"
{
                            yyval.node=new TreeNode(DeclKind.FUNCTIONHEAD,yyline);
                            yyval.node.setAttribute(val_peek(3).sval);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 11:
//#line 90 "tiny.y"
{yyval.node=null;}
break;
case 12:
//#line 92 "tiny.y"
{yyval.node=val_peek(1).node;}
break;
case 13:
//#line 95 "tiny.y"
{   TreeNode t=val_peek(2).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(2).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 14:
//#line 106 "tiny.y"
{   yyval.node=val_peek(0).node; }
break;
case 15:
//#line 109 "tiny.y"
{
                            yyval.node=new TreeNode(DeclKind.VAR_PARA,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 16:
//#line 115 "tiny.y"
{
                            yyval.node=new TreeNode(DeclKind.VAL_PARA,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 17:
//#line 122 "tiny.y"
{
                            yyval.node=new TreeNode(DeclKind.PROCEDURE,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 18:
//#line 129 "tiny.y"
{   
                            yyval.node=new TreeNode(DeclKind.PROCEDUREHEAD,yyline);
                            yyval.node.setAttribute(val_peek(1).sval);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 19:
//#line 136 "tiny.y"
{   yyval.node = null;}
break;
case 20:
//#line 138 "tiny.y"
{   yyval.node=val_peek(0).node;}
break;
case 21:
//#line 141 "tiny.y"
{   TreeNode t = val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 22:
//#line 151 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 23:
//#line 154 "tiny.y"
{   yyval.node=new TreeNode(DeclKind.VAR,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 24:
//#line 160 "tiny.y"
{   yyval.node = null; }
break;
case 25:
//#line 162 "tiny.y"
{   yyval.node=val_peek(0).node; }
break;
case 26:
//#line 165 "tiny.y"
{
                            TreeNode t = val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 27:
//#line 178 "tiny.y"
{   yyval.node=val_peek(0).node; }
break;
case 28:
//#line 181 "tiny.y"
{
                            yyval.node=new TreeNode(DeclKind.CONST,yyline);
                            yyval.node.setAttribute(val_peek(3).node.getAttribute());
                            /*freeNode($1);*/
                            yyval.node.addChild(val_peek(1).node);
                            yyval.node.setType(val_peek(1).node.getType());
                        }
break;
case 29:
//#line 190 "tiny.y"
{
                            yyval.node =new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.INT);
                            yyval.node.setAttribute(val_peek(0).ival);
                        }
break;
case 30:
//#line 196 "tiny.y"
{
                            yyval.node = new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.REAL);
                            yyval.node.setAttribute(val_peek(0).dval);
                        }
break;
case 31:
//#line 202 "tiny.y"
{
                            yyval.node = new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.CHAR);
                            yyval.node.setAttribute(val_peek(0).sval.toCharArray());
                        }
break;
case 32:
//#line 208 "tiny.y"
{
                            yyval.node = new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.STRING);
                            yyval.node.setAttribute(val_peek(0).sval);
                        }
break;
case 33:
//#line 214 "tiny.y"
{
                            yyval.node=new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.BOOL);
                            yyval.node.setAttribute(1);
                        }
break;
case 34:
//#line 220 "tiny.y"
{
                            yyval.node=new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.BOOL);
                            yyval.node.setAttribute(0);
                        }
break;
case 35:
//#line 226 "tiny.y"
{
                            yyval.node=new TreeNode(ExpKind.CONST,yyline);
                            yyval.node.setType(ExpType.INT);
                            yyval.node.setAttribute(2147483647);
                        }
break;
case 36:
//#line 233 "tiny.y"
{   yyval.node=null;}
break;
case 37:
//#line 235 "tiny.y"
{   yyval.node=val_peek(0).node;}
break;
case 38:
//#line 238 "tiny.y"
{
                            TreeNode t=val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                              yyval.node=val_peek(0).node;
                        }
break;
case 39:
//#line 250 "tiny.y"
{   yyval.node=val_peek(0).node;}
break;
case 40:
//#line 253 "tiny.y"
{   yyval.node=new TreeNode(DeclKind.TYPE,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 41:
//#line 258 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 42:
//#line 259 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 43:
//#line 260 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 44:
//#line 263 "tiny.y"
{   yyval.node=val_peek(1).node; }
break;
case 45:
//#line 266 "tiny.y"
{
                            TreeNode t=val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 46:
//#line 277 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 47:
//#line 280 "tiny.y"
{
                            yyval.node=new TreeNode(TypeKind.RECORD,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 48:
//#line 287 "tiny.y"
{
                            yyval.node=new TreeNode(TypeKind.ARRAY,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setType(ExpType.ARRAY);
                        }
break;
case 49:
//#line 295 "tiny.y"
{
                            yyval.node=new TreeNode(TypeKind.SIMPLE_ID,yyline);
                            yyval.node.setAttribute(val_peek(0).node.getAttribute());
                            /*free($1);*/
                        }
break;
case 50:
//#line 301 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_ENUM,yyline);
                            yyval.node.addChild(val_peek(1).node);
                            yyval.node.setType(ExpType.SIMPLE_ENUM);
                        }
break;
case 51:
//#line 306 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setType(ExpType.SIMPLE_LIMIT);
                        }
break;
case 52:
//#line 312 "tiny.y"
{
                            yyval.node=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.getChildren().get(0).setAttribute(Integer.parseInt(String.valueOf(yyval.node.getChildren().get(0).getAttribute()))*(-1));
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setType(ExpType.SIMPLE_LIMIT);
                        }
break;
case 53:
//#line 320 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.getChildren().get(0).setAttribute(Integer.parseInt(String.valueOf(yyval.node.getChildren().get(0).getAttribute()))*(-1));
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.getChildren().get(1).setAttribute(Integer.parseInt(String.valueOf(yyval.node.getChildren().get(1).getAttribute()))*(-1));
                            yyval.node.setType(ExpType.SIMPLE_LIMIT);
                        }
break;
case 54:
//#line 328 "tiny.y"
{
                            yyval.node=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setType(ExpType.SIMPLE_LIMIT);
                        }
break;
case 55:
//#line 335 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            yyval.node.setType(ExpType.INT);
                        }
break;
case 56:
//#line 339 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            yyval.node.setType(ExpType.BOOL);
                        }
break;
case 57:
//#line 343 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            yyval.node.setType(ExpType.REAL);
                        }
break;
case 58:
//#line 347 "tiny.y"
{   yyval.node=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            yyval.node.setType(ExpType.CHAR);
                        }
break;
case 59:
//#line 352 "tiny.y"
{
                            TreeNode t=val_peek(2).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(2).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 60:
//#line 363 "tiny.y"
{   yyval.node=val_peek(0).node; }
break;
case 61:
//#line 366 "tiny.y"
{   yyval.node=new TreeNode(ExpKind.ID,yyline);
                            yyval.node.setAttribute(val_peek(0).sval);
                        }
break;
case 62:
//#line 369 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 63:
//#line 370 "tiny.y"
{yyval.node=val_peek(1).node;}
break;
case 64:
//#line 372 "tiny.y"
{yyval.node=null;}
break;
case 65:
//#line 374 "tiny.y"
{
                            TreeNode t=val_peek(2).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(1).node);
                                yyval.node=val_peek(2).node;
                            }
                            else
                                yyval.node=val_peek(1).node;
                        }
break;
case 66:
//#line 387 "tiny.y"
{
                            yyval.node=new TreeNode(StmtKind.LABEL,yyline);
                            yyval.node.setAttribute(val_peek(2).ival);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 67:
//#line 393 "tiny.y"
{   yyval.node=val_peek(0).node;}
break;
case 68:
//#line 395 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 69:
//#line 396 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 70:
//#line 397 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 71:
//#line 398 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 72:
//#line 399 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 73:
//#line 400 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 74:
//#line 401 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 75:
//#line 402 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 76:
//#line 403 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 77:
//#line 405 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.ASSIGN,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setAttribute(OpKind.ID);
                        }
break;
case 78:
//#line 411 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.ASSIGN,yyline);
                            yyval.node.addChild(val_peek(5).node);
                            (yyval.node.getChildren().get(0)).addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setAttribute(OpKind.ARRAY);
                        }
break;
case 79:
//#line 419 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.ASSIGN,yyline);
                            yyval.node.addChild(val_peek(4).node);
                            (yyval.node.getChildren().get(0)).addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setAttribute(OpKind.RECORD);
                        }
break;
case 80:
//#line 427 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.GOTO,yyline);
                            yyval.node.setAttribute(val_peek(0).ival);
                        }
break;
case 81:
//#line 432 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.IF,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 82:
//#line 438 "tiny.y"
{yyval.node=null;}
break;
case 83:
//#line 439 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 84:
//#line 442 "tiny.y"
{
                            yyval.node=new TreeNode(StmtKind.REPEAT,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 86:
//#line 450 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.WHILE,yyline);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                        }
break;
case 87:
//#line 455 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.CASE,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 88:
//#line 460 "tiny.y"
{   TreeNode t=val_peek(1).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(1).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 89:
//#line 470 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 90:
//#line 472 "tiny.y"
{
                            yyval.node=new TreeNode(ExpKind.CASE,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 91:
//#line 478 "tiny.y"
{
                            yyval.node=new TreeNode(ExpKind.CASE,yyline);
                            yyval.node.addChild(val_peek(3).node);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 92:
//#line 484 "tiny.y"
{
                            yyval.node=new TreeNode(StmtKind.FOR,yyline);
                            yyval.node.addChild(val_peek(6).node);
                            yyval.node.addChild(val_peek(4).node);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setAttribute(OpKind.TO);
                        }
break;
case 93:
//#line 493 "tiny.y"
{
                            yyval.node=new TreeNode(StmtKind.FOR,yyline);
                            yyval.node.addChild(val_peek(6).node);
                            yyval.node.addChild(val_peek(4).node);
                            yyval.node.addChild(val_peek(2).node);
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setAttribute(OpKind.DOWNTO);
                        }
break;
case 94:
//#line 502 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.PROC_ID,yyline);
                            yyval.node.setAttribute(val_peek(0).node.getAttribute());
                        }
break;
case 95:
//#line 506 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.PROC_ID,yyline);
                            yyval.node.setAttribute(val_peek(3).node.getAttribute());
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 96:
//#line 511 "tiny.y"
{
                            yyval.node=new TreeNode(StmtKind.PROC_SYS,yyline);
                            yyval.node.setAttribute(OpKind.READ);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 97:
//#line 517 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.PROC_SYS,yyline);
                            yyval.node.setAttribute(OpKind.WRITE);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 98:
//#line 522 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.PROC_SYS,yyline);
                            yyval.node.setAttribute(OpKind.WRITELN);
                        }
break;
case 99:
//#line 526 "tiny.y"
{   yyval.node=new TreeNode(StmtKind.PROC_SYS,yyline);
                            yyval.node.setAttribute(OpKind.WRITELN);
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 100:
//#line 531 "tiny.y"
{   TreeNode t=val_peek(2).node;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling(val_peek(0).node);
                                yyval.node=val_peek(2).node;
                            }
                            else
                                yyval.node=val_peek(0).node;
                        }
break;
case 101:
//#line 541 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 102:
//#line 543 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.GE,yyline); }
break;
case 103:
//#line 544 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.GT,yyline); }
break;
case 104:
//#line 545 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.LE,yyline); }
break;
case 105:
//#line 546 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.LT,yyline); }
break;
case 106:
//#line 547 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.EQUAL,yyline); }
break;
case 107:
//#line 548 "tiny.y"
{  yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.UNEQUAL,yyline); }
break;
case 108:
//#line 549 "tiny.y"
{   yyval.node=val_peek(0).node;}
break;
case 109:
//#line 551 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.PLUS,yyline); }
break;
case 110:
//#line 552 "tiny.y"
{  yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.MINUS,yyline); }
break;
case 111:
//#line 553 "tiny.y"
{     yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.OR,yyline); }
break;
case 112:
//#line 554 "tiny.y"
{   yyval.node=val_peek(0).node;}
break;
case 113:
//#line 556 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.MUL,yyline); }
break;
case 114:
//#line 557 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.DIV,yyline); }
break;
case 115:
//#line 558 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.MOD,yyline); }
break;
case 116:
//#line 559 "tiny.y"
{   yyval.node=new TreeNode(val_peek(2).node,val_peek(0).node,OpKind.AND,yyline); }
break;
case 117:
//#line 560 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 118:
//#line 563 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 119:
//#line 565 "tiny.y"
{ 
                            yyval.node=new TreeNode(ExpKind.FUNC_ID,yyline);
                            yyval.node.setAttribute(val_peek(3).node.getAttribute());
                            yyval.node.addChild(val_peek(1).node);
                        }
break;
case 120:
//#line 570 "tiny.y"
{yyval.node=val_peek(0).node;}
break;
case 121:
//#line 571 "tiny.y"
{yyval.node=val_peek(1).node;}
break;
case 122:
//#line 573 "tiny.y"
{  
                           yyval.node=new TreeNode(val_peek(0).node,null,OpKind.NOT,yyline);
                        }
break;
case 123:
//#line 577 "tiny.y"
{   yyval.node=new TreeNode(val_peek(0).node, null, OpKind.MINUS, yyline);
                        }
break;
case 124:
//#line 580 "tiny.y"
{   yyval.node=val_peek(3).node;
                            yyval.node.addChild(val_peek(1).node);
                            yyval.node.setType(ExpType.ARRAY);
                        }
break;
case 125:
//#line 585 "tiny.y"
{   yyval.node=val_peek(2).node;
                            yyval.node.addChild(val_peek(0).node);
                            yyval.node.setType(ExpType.RECORD);                         
                        }
break;
case 126:
//#line 590 "tiny.y"
{   
                            yyval.node=new TreeNode(OpKind.ABS, val_peek(1).node,yyline);
                        }
break;
case 127:
//#line 594 "tiny.y"
{   
                            yyval.node=new TreeNode(OpKind.CHR, val_peek(1).node,yyline);
                        }
break;
case 128:
//#line 598 "tiny.y"
{   
                            yyval.node=new TreeNode(OpKind.ODD, val_peek(1).node,yyline);
                        }
break;
case 129:
//#line 602 "tiny.y"
{   
                           
                            yyval.node=new TreeNode(OpKind.ORD, val_peek(1).node,yyline);
                        }
break;
case 130:
//#line 607 "tiny.y"
{   
                            yyval.node=new TreeNode(OpKind.PRED, val_peek(1).node,yyline);
                        }
break;
case 131:
//#line 611 "tiny.y"
{ 
                            yyval.node=new TreeNode(OpKind.SQR, val_peek(1).node,yyline);
                        }
break;
case 132:
//#line 615 "tiny.y"
{   
                            yyval.node=new TreeNode(OpKind.SQRT, val_peek(1).node,yyline);
                        }
break;
case 133:
//#line 619 "tiny.y"
{   yyval.node=new TreeNode(OpKind.SUCC, val_peek(1).node,yyline);
                        }
break;
//#line 1722 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
