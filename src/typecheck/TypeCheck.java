package typecheck;

import java.util.LinkedList;

import codegeneration.CodeGenerator;
import symboltable.*;
import tree.DeclKind;
import tree.ExpKind;
import tree.ExpType;
import tree.StmtKind;
import tree.TreeNode;


public class TypeCheck {
	/*counter for variable memory location*/
	static int base = 0;
	static int offset = 0;

	/*trace*/
	static int TraceAnalyze = 1;
	
	/*error*/
	static int Error = 0;
	
	/*typecheck*/
	static int inTypeCheck = 0;
	protected static TypeCheck instance;
	public TypeCheck(){
		
	}
    public static TypeCheck getTypeCheck() {  // Singleton pattern
        if (instance == null) {
            instance = new TypeCheck();
        }
        return instance;
    }

	public void typeCheck(TreeNode syntaxTree){
		inTypeCheck = 1;
		SymbolTable.initScope();
		checkNode(syntaxTree);
		SymbolTable.leaveScope();
		inTypeCheck = 0;
	}

	private boolean compoundTypeEqual(TreeNode t1,TreeNode t2) {
		VariableDef l1,l2;
		l1 = SymbolTable.lookupVar((String)t1.getAttribute());
		l2 = SymbolTable.lookupVar((String)t1.getAttribute());
		if(l1 == null){
			System.out.println("Line "+t1.getLineNumber()+", Error: "+(String)(t1.getChildren().get(0)).getAttribute()+" is not declare.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
		}
		if(l2 == null){
			System.out.println("Line "+t2.getLineNumber()+", Error: "+(String)(t2.getChildren().get(0)).getAttribute()+" is not declare.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
		}
		return (l1.pAttr == l2.pAttr);
	}
	private boolean nodeTypeEqual(TreeNode t1,TreeNode t2) {
		if(t1.getRunningType() == t2.getRunningType()){
			if(t1.getRunningType() == ExpType.VOID ||
			   t1.getRunningType() == ExpType.INT || 
			   t1.getRunningType() == ExpType.REAL || 
			   t1.getRunningType() == ExpType.CHAR || 
			   t1.getRunningType() == ExpType.STRING ||
			   t1.getRunningType()== ExpType.BOOL)
			   return true;
			else	
				return compoundTypeEqual(t1,t2);
		}
		else
			return false;
	}

	private void checkNodeExpression(TreeNode pnode) {
		if(pnode.getKind()==ExpKind.ID){
			checkExpId(pnode);
		}else if(pnode.getKind()==ExpKind.OP){
			checkExpOp(pnode);
		}else if(pnode.getKind()==ExpKind.CONST){
			checkExpConst(pnode);
		}else if(pnode.getKind()==ExpKind.CASE){
			checkExpCase(pnode);
		}else if(pnode.getKind()==ExpKind.FUNC_ID){
			checkExpFunc(pnode);
		}
	
	}
	private void checkExpId(TreeNode pnode) {
		VariableDef ssvar=SymbolTable.lookupVar((String)pnode.getAttribute());
		ExpType checkType;
		LookupRet ret;

		if (ssvar==null){
	 		System.out.println("Line "+pnode.getLineNumber()+", Error: The variable "+(String)pnode.getAttribute()+" is not declared.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
	 	}
	 	checkType = ssvar.type;
	 	switch(checkType){
	 		case ARRAY:
	 			//�����index�Ƿ����
	 			if(pnode.getChildren().get(0)==null){
	 				//ֻ������
	 				pnode.setRunningType(checkType);
	 			}
	 			else{
	 				//������Ԫ��
	 				pnode.setRunningType(((ArrayDef)(ssvar.pAttr)).arrayType);
	 			}
	 			break;
	 		case RECORD:
	 			if(pnode.getChildren().get(0)==null){
	 				//ֻ��record
	 				pnode.setRunningType(checkType); 
	 			}
	 			else{
	 				ret = SymbolTable.lookupRecord((String)pnode.getAttribute(),(pnode.getChildren().get(0)).getAttribute().toString());
	 				if(ret.type == ExpType.VOID){
	 					System.out.println("Line "+pnode.getLineNumber()+", Error: The variable "+(String)pnode.getAttribute()+"."+(pnode.getChildren().get(0)).getAttribute().toString()+" is not declare.\n");
	 					//fflush(stdout);
	 					typeError();
	 					// error = True;
	 				}
	 				pnode.setRunningType(ret.type);
	 			}
	 			break;
	 		case BOOL:
	 			pnode.setRunningType(ExpType.INT);
	 		default:
	 			pnode.setRunningType(checkType);
	 			break;
	 	}
	}
	private void checkExpOp(TreeNode pnode) {
		if ((pnode.getChildren().get(0))!=null && (pnode.getChildren().get(1))!=null){
			//����﷨���󣬲�ΪRunningType��ֵ
			checkNode((pnode.getChildren().get(0)));
			checkNode((pnode.getChildren().get(1)));


			if ((pnode.getChildren().get(0)).getRunningType() != ((pnode.getChildren().get(1))).getRunningType()){
	 			System.out.println("Line "+pnode.getLineNumber()+", Error: Different type can not be calculated.\n");
	 			//fflush(stdout);	
	 			typeError();
	 			// error = True;			
	 		}
	 		
			if ((pnode.getChildren().get(0)).getRunningType()==ExpType.REAL && (pnode.getChildren().get(1)).getRunningType()==ExpType.REAL)
				pnode.setRunningType(ExpType.REAL);
			else if((pnode.getChildren().get(0)).getRunningType()==ExpType.INT &&(pnode.getChildren().get(1)).getRunningType()==ExpType.INT)
				pnode.setRunningType(ExpType.INT); 
			else if((pnode.getChildren().get(0)).getRunningType()==ExpType.CHAR && (pnode.getChildren().get(1)).getRunningType()==ExpType.CHAR){
				System.out.println("Line "+pnode.getLineNumber()+", Error: Char can not be calculated.\n");
	 			//fflush(stdout);
	 			typeError();
	 			// error = True;
			}
			else{
				System.out.println("Line "+pnode.getLineNumber()+", Error: compound type can not be calculated.\n");
	 			//fflush(stdout);
	 			typeError();
	 			// error = True;
			}

		}
		else {
			checkNode(pnode.getChildren().get(0));
			pnode.setRunningType((pnode.getChildren().get(0)).getRunningType());
		}
	}

	private static void checkExpConst(TreeNode pnode) {
		if(pnode.getType() == ExpType.BOOL)
			pnode.setRunningType(ExpType.INT);
		else
			pnode.setRunningType(pnode.getType());
	}
	
	private void checkExpCase(TreeNode pnode) {
		checkNode(pnode.getChildren().get(0));	
		checkNode(pnode.getChildren().get(1));

		pnode.setRunningType(pnode.getChildren().get(0).getRunningType());
	}
	private void checkExpFunc(TreeNode pnode) {
		FuncDef judge_var=SymbolTable.lookupFunc((String)pnode.getAttribute());
	 	if(judge_var == null){
	 		System.out.println("Line "+pnode.getRunningType()+", Error: Function "+(String)pnode.getAttribute()+" dose not declare.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
	 	}
		TreeNode tmpChild = pnode.getChildren().get(0);
		LinkedList<SimpleType> tmpPara = judge_var.paraList;

		if(tmpChild != null)		//���ܲ����ڲ���
			checkNode(tmpChild);

		int i=0;

		while(tmpChild!=null && i < tmpPara.size()){
			//ֻ�Ǽ򵥵��ж��������Ƿ���ͬ
			if(tmpChild.getRunningType() != tmpPara.get(i).type){
				System.out.println("Line "+pnode.getLineNumber()+", Error: parameter type does not match.\n");
	 			//fflush(stdout);
	 			typeError();
	 			// error = True;
			}
			tmpChild = tmpChild.getSibling();
			i++;
		}
		if(tmpChild != null || i < tmpPara.size()){
			System.out.println("Line "+pnode.getLineNumber()+", Error: number of parameters does not match.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
		}

		//���践��ֵ��ȷ
		pnode.setRunningType(judge_var.retType);

		// ��ʱ�޷��ȽϷ���ֵ
		// checkNode(pnode.getChildren().get(1));
		// if(pnode.getChildren().get(1)->RunningType != judge_var->retType){
		// 	printf("Line %d, Error: return data type does not match.\n",pnode.getLineNumber());
	 // 		fflush(stdout);
	 // 		error = True;
		// }

	}

	private void checkStmtAssign(TreeNode pnode) {
		//����0��һ�����ʽ�������ȼ��ñ��ʽ�Ƿ����﷨����
		//Ȼ���ñ��ʽ������
		checkNode(pnode.getChildren().get(1));
		//����1��һ������������ͨ������Ѱ�ұ������������ֻ����һ���ڵ�
		VariableDef ssvar=SymbolTable.lookupVar((pnode.getChildren().get(0)).getAttribute().toString());
		if(ssvar == null){
			System.out.println("Line "+pnode.getLineNumber()+", Error: The variable "+(pnode.getChildren().get(0)).getAttribute().toString()+" is not declare.\n");
	 	//	fflush(stdout);
	 		typeError();
		}
		//���ȣ�����ڵ㲻�ܹ�Ϊ����
		if (ssvar.isConst){	
			System.out.println("Line "+pnode.getLineNumber()+", Error: The const variable "+(pnode.getChildren().get(0)).getAttribute().toString()+" can not be assigned.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
		}
		//�����һ��ExpNode,�ṹֻ�����������id,id[]��id.id
		//Ȼ�����Ǽ�����ExpNode�Ƿ����﷨���󣬲��һ�ȡ���ExpNode������
		checkNode(pnode.getChildren().get(0));

		//���ֵ����������ߵ����Ͳ���ͬ���򱨴�
		if(!nodeTypeEqual(pnode.getChildren().get(0),pnode.getChildren().get(1))){
			System.out.println("Line "+pnode.getLineNumber()+", Error: The type of "+pnode.getChildren().get(0).getAttribute().toString()+" is different from the right hand.");
		//	fflush(stdout);
			typeError();
	 		// error = True;
		}
	}
	private void checkStmtIf(TreeNode pnode) {
		ExpType checkType;
		//�﷨�����ᱣ֤if����������

		//panduan
		checkNode(pnode.getChildren().get(0));
		checkType = pnode.getChildren().get(0).getRunningType();
		if(checkType != ExpType.INT &&
		   checkType != ExpType.REAL){
			System.out.println("Line "+pnode.getLineNumber()+", Error: If condition is not illegal.\n");
	 	//	fflush(stdout);
	 		typeError();
	 		// error = True;
		}

		//if-part
		checkNode(pnode.getChildren().get(1));

		if (pnode.getChildren().get(2)!=null)
			checkNode(pnode.getChildren().get(2));
	}
	private void checkStmtFor(TreeNode pnode) {

		//��ʼֵ
		//�﷨������֤for�����Ķ�
		checkNode(pnode.getChildren().get(0));
		checkNode(pnode.getChildren().get(1));
		checkNode(pnode.getChildren().get(2));
		checkNode(pnode.getChildren().get(3));

		if(pnode.getChildren().get(0).getRunningType()!= ExpType.INT||
		   pnode.getChildren().get(1).getRunningType()!= ExpType.INT ||
		   pnode.getChildren().get(2).getRunningType()!= ExpType.INT){
			System.out.println("Line "+pnode.getLineNumber()+", Error: Index of for statement must be int.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
		}
	}
	private void checkStmtWhile(TreeNode pnode) {
		ExpType checkType;

		//�﷨������֤while������������
		checkNode(pnode.getChildren().get(0));
		checkNode(pnode.getChildren().get(1));

		checkType = pnode.getChildren().get(0).getRunningType();
		if(checkType != ExpType.INT &&
		   checkType != ExpType.REAL){
			System.out.println("Line "+pnode.getLineNumber()+" Error: While condition is not illegal.\n");
	 		//fflush(stdout);
	 		typeError();
	 		// error = True;
		}
	}
	private void checkStmtRepeat(TreeNode pnode) {
		ExpType checkType;

		checkNode(pnode.getChildren().get(0));
		checkNode(pnode.getChildren().get(1));	

		checkType = pnode.getChildren().get(1).getRunningType();
		if(checkType != ExpType.INT &&
		   checkType != ExpType.REAL){
			System.out.println("Line "+pnode.getLineNumber()+" Error: Util condition is not illegal.\n");
	 		typeError();
	 		// error = True;
		}
	}
	private void checkStmtCase(TreeNode pnode) {
		ExpType checkType;
		TreeNode tmp;

		//�﷨������֤2������
		checkNode(pnode.getChildren().get(0));
		checkNode(pnode.getChildren().get(1));

		checkType = pnode.getChildren().get(0).getRunningType();
		tmp = pnode.getChildren().get(1);
		while(tmp!=null){
			if(checkType != tmp.getRunningType()){
				System.out.println("Line:"+ pnode.getLineNumber()+", Error: data type dose not match case type.\n");
	 			typeError();
	 			// error = True;
			}
			tmp = tmp.getSibling();
		}
	}
	private void checkStmtGoto(TreeNode pnode) {
		int i=0;
	}
	private void checkStmtLabel(TreeNode pnode) {
		checkNode(pnode.getChildren().get(0));
	}
	private void checkStmtProc(TreeNode pnode) {
		if((pnode.getChildren().get(0)) != null)
			checkNode(pnode.getChildren().get(0));
	}

	private void checkNode(TreeNode pnode) {

		if(pnode.getKind()==StmtKind.ASSIGN){
			checkStmtAssign(pnode);
		}else if(pnode.getKind()==StmtKind.IF){
			checkStmtIf(pnode);
		}else if(pnode.getKind()==StmtKind.FOR){
			checkStmtFor(pnode);
		}else if(pnode.getKind()==StmtKind.WHILE){
			checkStmtWhile(pnode);
		}else if(pnode.getKind()==StmtKind.REPEAT){
			checkStmtRepeat(pnode);
		}else if(pnode.getKind()==StmtKind.CASE){
			checkStmtCase(pnode);
		}else if(pnode.getKind()==StmtKind.GOTO){
			checkStmtGoto(pnode);
		}else if(pnode.getKind()==StmtKind.LABEL){
			checkStmtLabel(pnode);
		}else if(pnode.getKind()==StmtKind.PROC_ID){
			checkStmtProc(pnode);
		}
		
		if((pnode.getKind()==ExpKind.CASE)
		 |(pnode.getKind()==ExpKind.CONST)
		 |(pnode.getKind()==ExpKind.FUNC_ID)
		 |(pnode.getKind()==ExpKind.FUNC_SYS)
		 |(pnode.getKind()==ExpKind.ID)
		 |(pnode.getKind()==ExpKind.OP)){
			checkNodeExpression(pnode);
		}
		if(pnode.getKind()==DeclKind.ROUTINEHEAD){

			SymbolTable.enterNewScope(pnode);
			if (pnode.getChildren().get(3)!=null)
					checkNode(pnode.getChildren().get(3));

		}else if(pnode.getKind()==DeclKind.FUNCTION){

			(pnode.getChildren().get(1)).setAttribute((pnode.getChildren().get(0)).getAttribute().toString());
			SymbolTable.addProcOrFunc(pnode.getChildren().get(0));
			checkNode(pnode.getChildren().get(1)); //routine_head
			SymbolTable.leaveScope();

		}else if(pnode.getKind()==DeclKind.PROCEDURE){

			(pnode.getChildren().get(1)).setAttribute((pnode.getChildren().get(0)).getAttribute().toString());
			SymbolTable.addProcOrFunc(pnode.getChildren().get(0));
			checkNode(pnode.getChildren().get(1)); //routine_head
			SymbolTable.leaveScope();
		}
        

		if(pnode.getSibling()!=null){
			checkNode(pnode.getSibling());	
		}
	}
	private void typeError() {
		System.out.println("Exit with error\n");
		System.exit(1);
	}
}
