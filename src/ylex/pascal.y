%{
//#define YYPARSER
import java.io.*; 
import tree.*; 

%}
%token TOKEN_PROGRAM TOKEN_FUNCTION TOKEN_PROCEDURE TOKEN_CONST TOKEN_TYPE TOKEN_VAR
%token TOKEN_IF TOKEN_THEN TOKEN_ELSE TOKEN_REPEAT TOKEN_UNTIL TOKEN_WHILE TOKEN_DO TOKEN_CASE TOKEN_TO TOKEN_DOWNTO TOKEN_FOR
%token TOKEN_EQUAL TOKEN_UNEQUAL TOKEN_GE TOKEN_GT TOKEN_LE TOKEN_LT TOKEN_ASSIGN TOKEN_PLUS TOKEN_MINUS TOKEN_MUL TOKEN_DIV TOKEN_OR TOKEN_AND TOKEN_NOT TOKEN_MOD TOKEN_READ TOKEN_WRITE TOKEN_WRITELN
%token TOKEN_LB TOKEN_RB TOKEN_SEMI TOKEN_DOT TOKEN_DOTDOT TOKEN_LP TOKEN_RP TOKEN_COMMA TOKEN_COLON 
%token TOKEN_INTEGER_TYPE TOKEN_BOOLEAN_TYPE TOKEN_CHAR_TYPE TOKEN_REAL_TYPE 
%token TOKEN_TRUE TOKEN_FALSE TOKEN_MAXINT
%token TOKEN_ARRAY TOKEN_OF TOKEN_RECORD TOKEN_BEGIN TOKEN_END TOKEN_GOTO
%token <sval> TOKEN_ID TOKEN_CHAR TOKEN_STRING
%token <ival> TOKEN_INT
%token <dval> TOKEN_REAL
%token ERROR
%token TOKEN_ABS TOKEN_CHR TOKEN_ODD TOKEN_ORD TOKEN_PRED TOKEN_SQR TOKEN_SQRT TOKEN_SUCC

%type <node> program routine routine_head routine_part function_decl function_head parameters para_decl_list para_type_list
%type <node> procedure_decl procedure_head var_part var_decl_list var_decl const_part const_expr_list const_expr const_value
%type <node> type_part  type_decl_list type_definition type_decl record_type_decl field_decl_list field_decl array_type_decl
%type <node> simple_type_decl name_list ID routine_body compound_stmt stmt_list no_label_stmt assign_stmt goto_stmt if_stmt
%type <node> else_clause repeat_stmt case_stmt case_expr_list case_expr for_stmt proc_stmt args_list expr
%type <node> term factor
%type <node> while_stmt expression stmt
%%

program             :   TOKEN_PROGRAM TOKEN_ID TOKEN_SEMI routine TOKEN_DOT
                        {   $$ = $4;
                            $$.setAttribute($2);
                            savedTree = $$;
                        };
routine             :   routine_head routine_body
                        {
                            $$ =$1;
                            $$.setSibling($2);
                        };
routine_head        :   const_part type_part var_part routine_part
                        {
                            $$ = new TreeNode(DeclKind.ROUTINEHEAD,yyline);
                            $$.addChild($1);
                            $$.addChild($2);
                            $$.addChild($3);
                            $$.addChild($4);
                        };
routine_part        :
                        {   $$= null;}
                    |   routine_part function_decl
                        {   TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }   
                    |   routine_part procedure_decl
                        {   TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }
                    |   function_decl   {$$=$1;}
                    |   procedure_decl  {$$=$1;}
                    ;
function_decl       :   function_head TOKEN_SEMI routine TOKEN_SEMI
                        {
                            $$=new TreeNode(DeclKind.FUNCTION,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    ;
function_head       :   TOKEN_FUNCTION TOKEN_ID parameters  TOKEN_COLON simple_type_decl
                        {
                            $$=new TreeNode(DeclKind.FUNCTIONHEAD,yyline);
                            $$.setAttribute($2);
                            $$.addChild($3);
                            $$.addChild($5);
                        }
                    ;
parameters          :   
                        {$$=null;}
                    |   TOKEN_LP para_decl_list TOKEN_RP
                        {$$=$2;}
                    ;
para_decl_list      :   para_decl_list  TOKEN_SEMI  para_type_list
                        {   TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($3);
                                $$=$1;
                            }
                            else
                                $$=$3;
                        }
                    |   para_type_list
                        {   $$=$1; }
                    ;
para_type_list      :   TOKEN_VAR name_list TOKEN_COLON simple_type_decl
                        {
                            $$=new TreeNode(DeclKind.VAR_PARA,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                        }
                    |   name_list TOKEN_COLON simple_type_decl
                        {
                            $$=new TreeNode(DeclKind.VAL_PARA,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    ;
procedure_decl      :   procedure_head TOKEN_SEMI routine TOKEN_SEMI
                        {
                            $$=new TreeNode(DeclKind.PROCEDURE,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    ;
procedure_head      :   TOKEN_PROCEDURE TOKEN_ID parameters
                        {   
                            $$=new TreeNode(DeclKind.PROCEDUREHEAD,yyline);
                            $$.setAttribute($2);
                            $$.addChild($3);
                        }
                    ;
var_part            :
                        {   $$ = null;}
                    |   TOKEN_VAR var_decl_list
                        {   $$=$2;}
                    ;
var_decl_list       :   var_decl_list var_decl
                        {   TreeNode t = $1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }
                    |   var_decl    {$$=$1;}
                    ;
var_decl            :   name_list TOKEN_COLON type_decl TOKEN_SEMI
                        {   $$=new TreeNode(DeclKind.VAR,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    ;
const_part          :
                        {   $$ = null; }
                    |   TOKEN_CONST const_expr_list
                        {   $$=$2; }
                    ;
const_expr_list     :   const_expr_list const_expr
                        {
                            TreeNode t = $1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }
                        
                    |   const_expr
                        {   $$=$1; }
                    ;
const_expr          :    ID TOKEN_EQUAL const_value TOKEN_SEMI
                        {
                            $$=new TreeNode(DeclKind.CONST,yyline);
                            $$.setAttribute($1.getAttribute());
                            $$.addChild($3);
                            $$.setType($3.getType());
                        }
                    ;
const_value         :   TOKEN_INT
                        {
                            $$ =new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.INT);
                            $$.setAttribute($1);
                        }
                    |   TOKEN_REAL
                        {
                            $$ = new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.REAL);
                            $$.setAttribute($1);
                        }
                    |   TOKEN_CHAR
                        {
                            $$ = new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.CHAR);
                            $$.setAttribute($1.toCharArray());
                        }
                    |   TOKEN_STRING
                        {
                            $$ = new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.STRING);
                            $$.setAttribute($1);
                        }
                    |   TOKEN_TRUE
                        {
                            $$=new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.BOOL);
                            $$.setAttribute(1);
                        }
                    |   TOKEN_FALSE
                        {
                            $$=new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.BOOL);
                            $$.setAttribute(0);
                        }
                    |   TOKEN_MAXINT
                        {
                            $$=new TreeNode(ExpKind.CONST,yyline);
                            $$.setType(ExpType.INT);
                            $$.setAttribute(2147483647);
                        }
                    ;
type_part           :
                        {   $$=null;}
                    |   TOKEN_TYPE type_decl_list
                        {   $$=$2;}
                    ;
type_decl_list      :   type_decl_list  type_definition
                        {
                            TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                              $$=$2;
                        }
                    |   type_definition
                        {   $$=$1;}
                    ;
type_definition     :   ID TOKEN_EQUAL type_decl TOKEN_SEMI
                        {   $$=new TreeNode(DeclKind.TYPE,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    ;
type_decl           :   simple_type_decl    {$$=$1;}
                    |   array_type_decl     {$$=$1;}
                    |   record_type_decl    {$$=$1;}
                    ;
record_type_decl    :   TOKEN_RECORD field_decl_list TOKEN_END
                        {   $$=$2; }
                    ;
field_decl_list     :   field_decl_list field_decl
                        {
                            TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }
                    |   field_decl {$$=$1;}
                    ;
field_decl          :   name_list TOKEN_COLON type_decl TOKEN_SEMI
                        {
                            $$=new TreeNode(TypeKind.RECORD,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    ;
array_type_decl     :   TOKEN_ARRAY TOKEN_LB simple_type_decl TOKEN_RB TOKEN_OF type_decl
                        {
                            $$=new TreeNode(TypeKind.ARRAY,yyline);
                            $$.addChild($3);
                            $$.addChild($6);
                            $$.setType(ExpType.ARRAY);
                        }
                    ;
simple_type_decl    :   ID
                        {
                            $$=new TreeNode(TypeKind.SIMPLE_ID,yyline);
                            $$.setAttribute($1.getAttribute());
                            //free($1);
                        }
                    |   TOKEN_LP name_list TOKEN_RP
                        {   $$=new TreeNode(TypeKind.SIMPLE_ENUM,yyline);
                            $$.addChild($2);
                            $$.setType(ExpType.SIMPLE_ENUM);
                        }
                    |   const_value TOKEN_DOTDOT const_value
                        {   $$=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                            $$.setType(ExpType.SIMPLE_LIMIT);
                        }
                    |   TOKEN_MINUS const_value TOKEN_DOTDOT const_value
                        {
                            $$=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            $$.addChild($2);
                            $$.getChildren().get(0).setAttribute(Integer.parseInt(String.valueOf($$.getChildren().get(0).getAttribute()))*(-1));
                            $$.addChild($4);
                            $$.setType(ExpType.SIMPLE_LIMIT);
                        }
                    |   TOKEN_MINUS const_value TOKEN_DOTDOT TOKEN_MINUS const_value
                        {   $$=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            $$.addChild($2);
                            $$.getChildren().get(0).setAttribute(Integer.parseInt(String.valueOf($$.getChildren().get(0).getAttribute()))*(-1));
                            $$.addChild($5);
                            $$.getChildren().get(1).setAttribute(Integer.parseInt(String.valueOf($$.getChildren().get(1).getAttribute()))*(-1));
                            $$.setType(ExpType.SIMPLE_LIMIT);
                        }
                    |   ID TOKEN_DOTDOT ID
                        {
                            $$=new TreeNode(TypeKind.SIMPLE_LIMIT,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                            $$.setType(ExpType.SIMPLE_LIMIT);
                        }
                    |   TOKEN_INTEGER_TYPE  
                        {   $$=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            $$.setType(ExpType.INT);
                        }
                    |   TOKEN_BOOLEAN_TYPE
                        {   $$=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            $$.setType(ExpType.BOOL);
                        }
                    |   TOKEN_REAL_TYPE
                        {   $$=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            $$.setType(ExpType.REAL);
                        }
                    |   TOKEN_CHAR_TYPE
                        {   $$=new TreeNode(TypeKind.SIMPLE_SYS,yyline);
                            $$.setType(ExpType.CHAR);
                        }
                    ;
name_list           :   name_list TOKEN_COMMA ID
                        {
                            TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($3);
                                $$=$1;
                            }
                            else
                                $$=$3;
                        }
                    |   ID {   $$=$1; }
                    ;
ID                  :   TOKEN_ID
                        {   $$=new TreeNode(ExpKind.ID,yyline);
                            $$.setAttribute($1);
                        } ;
routine_body        :   compound_stmt   {$$=$1;} ; 
compound_stmt       :   TOKEN_BEGIN stmt_list TOKEN_END {$$=$2;} ;
stmt_list           :   
                        {$$=null;}
                    |   stmt_list stmt TOKEN_SEMI
                        {
                            TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }
                    ;
stmt                :   TOKEN_INT TOKEN_COLON no_label_stmt
                        {
                            $$=new TreeNode(StmtKind.LABEL,yyline);
                            $$.setAttribute($1);
                            $$.addChild($3);
                        }
                    |   no_label_stmt
                        {   $$=$1;}
                    ;
no_label_stmt       :   assign_stmt {$$=$1;}
                    |   compound_stmt   {$$=$1;}
                    |   goto_stmt   {$$=$1;}
                    |   if_stmt     {$$=$1;}
                    |   repeat_stmt {$$=$1;}
                    |   while_stmt  {$$=$1;}
                    |   case_stmt   {$$=$1;}
                    |   for_stmt    {$$=$1;}
                    |   proc_stmt   {$$=$1;};
assign_stmt         :   ID TOKEN_ASSIGN expression
                        {   $$=new TreeNode(StmtKind.ASSIGN,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                            $$.setAttribute(OpKind.ID);
                        }
                    |   ID TOKEN_LB expression TOKEN_RB TOKEN_ASSIGN expression
                        {   $$=new TreeNode(StmtKind.ASSIGN,yyline);
                            $$.addChild($1);
                            ($$.getChildren().get(0)).addChild($3);
                            $$.addChild($6);
                            $$.setAttribute(OpKind.ARRAY);
                        }
                    |
                        ID TOKEN_DOT ID TOKEN_ASSIGN expression
                        {   $$=new TreeNode(StmtKind.ASSIGN,yyline);
                            $$.addChild($1);
                            ($$.getChildren().get(0)).addChild($3);
                            $$.addChild($5);
                            $$.setAttribute(OpKind.RECORD);
                        }
                    ;
goto_stmt           :   TOKEN_GOTO  TOKEN_INT
                        {   $$=new TreeNode(StmtKind.GOTO,yyline);
                            $$.setAttribute($2);
                        }
                    ;
if_stmt             :   TOKEN_IF expression TOKEN_THEN stmt  else_clause
                        {   $$=new TreeNode(StmtKind.IF,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                            $$.addChild($5);
                        }
                    ;
else_clause         :   {$$=null;}
                    |   TOKEN_ELSE  stmt    {$$=$2;}
                    ;
repeat_stmt         :   TOKEN_REPEAT stmt_list TOKEN_UNTIL expression
                        {
                            $$=new TreeNode(StmtKind.REPEAT,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                        }
                    |
                    ;
while_stmt          :   TOKEN_WHILE expression TOKEN_DO stmt
                        {   $$=new TreeNode(StmtKind.WHILE,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                        };
case_stmt           :   TOKEN_CASE expression TOKEN_OF case_expr_list TOKEN_END
                        {   $$=new TreeNode(StmtKind.CASE,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                        };
case_expr_list      :   case_expr_list  case_expr
                        {   TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($2);
                                $$=$1;
                            }
                            else
                                $$=$2;
                        }
                    |   case_expr   {$$=$1;};
case_expr           :   const_value TOKEN_COLON stmt TOKEN_SEMI
                        {
                            $$=new TreeNode(ExpKind.CASE,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        }
                    |   ID  TOKEN_COLON stmt TOKEN_SEMI
                        {
                            $$=new TreeNode(ExpKind.CASE,yyline);
                            $$.addChild($1);
                            $$.addChild($3);
                        };
for_stmt            :   TOKEN_FOR ID TOKEN_ASSIGN expression TOKEN_TO expression TOKEN_DO stmt
                        {
                            $$=new TreeNode(StmtKind.FOR,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                            $$.addChild($6);
                            $$.addChild($8);
                            $$.setAttribute(OpKind.TO);
                        }
                    |   TOKEN_FOR ID TOKEN_ASSIGN expression TOKEN_DOWNTO expression TOKEN_DO stmt
                        {
                            $$=new TreeNode(StmtKind.FOR,yyline);
                            $$.addChild($2);
                            $$.addChild($4);
                            $$.addChild($6);
                            $$.addChild($8);
                            $$.setAttribute(OpKind.DOWNTO);
                        };
proc_stmt           :   ID 
                        {   $$=new TreeNode(StmtKind.PROC_ID,yyline);
                            $$.setAttribute($1.getAttribute());
                        }
                    |   ID TOKEN_LP args_list TOKEN_RP
                        {   $$=new TreeNode(StmtKind.PROC_ID,yyline);
                            $$.setAttribute($1.getAttribute());
                            $$.addChild($3);
                        }
                    |   TOKEN_READ TOKEN_LP factor TOKEN_RP
                        {
                            $$=new TreeNode(StmtKind.PROC_SYS,yyline);
                            $$.setAttribute(OpKind.READ);
                            $$.addChild($3);
                        }
                    |   TOKEN_WRITE TOKEN_LP args_list TOKEN_RP
                        {   $$=new TreeNode(StmtKind.PROC_SYS,yyline);
                            $$.setAttribute(OpKind.WRITE);
                            $$.addChild($3);
                        }
                    |   TOKEN_WRITELN
                        {   $$=new TreeNode(StmtKind.PROC_SYS,yyline);
                            $$.setAttribute(OpKind.WRITELN);
                        }
                    |   TOKEN_WRITELN TOKEN_LP args_list TOKEN_RP
                        {   $$=new TreeNode(StmtKind.PROC_SYS,yyline);
                            $$.setAttribute(OpKind.WRITELN);
                            $$.addChild($3);
                        };
args_list           :   args_list TOKEN_COMMA expression
                        {   TreeNode t=$1;
                            if(t!=null){
                                while(t.getSibling()!=null)
                                  t=t.getSibling();
                                t.setSibling($3);
                                $$=$1;
                            }
                            else
                                $$=$3;
                        }
                    |   expression  {$$=$1;};
                            
expression          :   expression TOKEN_GE expr {   $$=new TreeNode($1,$3,OpKind.GE,yyline); }
                    |   expression TOKEN_GT expr {   $$=new TreeNode($1,$3,OpKind.GT,yyline); }
                    |   expression TOKEN_LE expr {   $$=new TreeNode($1,$3,OpKind.LE,yyline); }
                    |   expression TOKEN_LT expr {   $$=new TreeNode($1,$3,OpKind.LT,yyline); }
                    |   expression TOKEN_EQUAL expr {   $$=new TreeNode($1,$3,OpKind.EQUAL,yyline); }
                    |   expression TOKEN_UNEQUAL expr {  $$=new TreeNode($1,$3,OpKind.UNEQUAL,yyline); }
                    |   expr {   $$=$1;} ;

expr                :   expr TOKEN_PLUS term  {   $$=new TreeNode($1,$3,OpKind.PLUS,yyline); }
                    |   expr TOKEN_MINUS term  {  $$=new TreeNode($1,$3,OpKind.MINUS,yyline); }
                    |   expr TOKEN_OR term  {     $$=new TreeNode($1,$3,OpKind.OR,yyline); }
                    |   term {   $$=$1;} ;

term                :   term TOKEN_MUL factor {   $$=new TreeNode($1,$3,OpKind.MUL,yyline); }
                    |   term TOKEN_DIV factor {   $$=new TreeNode($1,$3,OpKind.DIV,yyline); }
                    |   term TOKEN_MOD factor {   $$=new TreeNode($1,$3,OpKind.MOD,yyline); }
                    |   term TOKEN_AND factor {   $$=new TreeNode($1,$3,OpKind.AND,yyline); }
                    |   factor {$$=$1;} ;

factor              :   ID
                        {$$=$1;}
                    |   ID TOKEN_LP args_list TOKEN_RP
                        { 
                            $$=new TreeNode(ExpKind.FUNC_ID,yyline);
                            $$.setAttribute($1.getAttribute());
                            $$.addChild($3);
                        }
                    |   const_value {$$=$1;}
                    |   TOKEN_LP expression TOKEN_RP    {$$=$2;}
                    |   TOKEN_NOT factor    
                        {  
                           $$=new TreeNode($2,null,OpKind.NOT,yyline);
                        }
                    |   TOKEN_MINUS factor
                        {   $$=new TreeNode($2, null, OpKind.MINUS, yyline);
                        }
                    |   ID TOKEN_LB expression TOKEN_RB
                        {   $$=$1;
                            $$.addChild($3);
                            $$.setType(ExpType.ARRAY);
                        }
                    |   ID TOKEN_DOT ID 
                        {   $$=$1;
                            $$.addChild($3);
                            $$.setType(ExpType.RECORD);                         
                        }
                    |   TOKEN_ABS TOKEN_LP args_list TOKEN_RP
                        {   
                            $$=new TreeNode(OpKind.ABS, $3,yyline);
                        }
                    |   TOKEN_CHR TOKEN_LP args_list TOKEN_RP
                        {   
                            $$=new TreeNode(OpKind.CHR, $3,yyline);
                        }
                    |   TOKEN_ODD TOKEN_LP args_list TOKEN_RP
                        {   
                            $$=new TreeNode(OpKind.ODD, $3,yyline);
                        }
                    |   TOKEN_ORD TOKEN_LP args_list TOKEN_RP
                        {   
                           
                            $$=new TreeNode(OpKind.ORD, $3,yyline);
                        }
                    |   TOKEN_PRED TOKEN_LP args_list TOKEN_RP
                        {   
                            $$=new TreeNode(OpKind.PRED, $3,yyline);
                        }
                    |   TOKEN_SQR TOKEN_LP args_list TOKEN_RP
                        { 
                            $$=new TreeNode(OpKind.SQR, $3,yyline);
                        }
                    |   TOKEN_SQRT TOKEN_LP args_list TOKEN_RP
                        {   
                            $$=new TreeNode(OpKind.SQRT, $3,yyline);
                        }
                    |   TOKEN_SUCC TOKEN_LP args_list TOKEN_RP
                        {   $$=new TreeNode(OpKind.SUCC, $3,yyline);
                        }
                    ;
%%

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
        yyparser = new Parser(new FileReader("/Users/kehanyang/Documents/Documents/Courses/Computer Courses/Compiler Design/project/Pascal-compiler/test/calculate"));
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