#a.out : tiny.l tiny.y global.h scan.h parse.h util.c main.c
a.out : tiny.l tiny.y global.h scan.h parse.h CG.h analyze.h symtab.h util.c main.c
	flex tiny.l
	yacc -d tiny.y
	gcc -g util.c lex.yy.c y.tab.c main.c CG.c analyze.c symtab.c -L/usr/local/Cellar/flex/2.5.39/lib -lfl -o a.out
	#	gcc util.c lex.yy.c main.c -lfl -o a.out
    
