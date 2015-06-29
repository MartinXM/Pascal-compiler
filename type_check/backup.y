var_part        :
                    {   $$=NULL;}
                |
                    TOKEN_VAR var_decl_list
                    {   $$=$2;}
                ;
var_decl_list   :   var_decl_list var_decl
                    {   
                        YYSTYPE t=$1;
                        if(t!=NULL){
                            while(t->sibling!=NULL)
                              t=t->sibling;
                            t->sibling=$2;
                            $$=$1;
                        }
                        else
                          $$=$1;
                    }
                |   var_decl
                    {   $$=$1;}
var_decl        :   
                ;

