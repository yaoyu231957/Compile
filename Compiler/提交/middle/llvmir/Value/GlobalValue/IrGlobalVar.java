package middle.llvmir.Value.GlobalValue;

import frontend.Parser.declaration.Decl;
import frontend.Parser.declaration.DeclAll;
import frontend.Parser.declaration.constant.ConstDecl;
import frontend.Parser.declaration.constant.ConstDef;
import frontend.Parser.declaration.constant.consinitval.ConIVArray;
import frontend.Parser.declaration.constant.consinitval.ConIVConstExp;
import frontend.Parser.declaration.constant.consinitval.ConIVStrConst;
import frontend.Parser.declaration.constant.consinitval.ConstInitVal;
import frontend.Parser.declaration.variable.VarDecl;
import frontend.Parser.declaration.variable.VarDef;
import frontend.Parser.declaration.variable.variableinitval.VarIVArray;
import frontend.Parser.declaration.variable.variableinitval.VarIVExp;
import frontend.Parser.declaration.variable.variableinitval.VarIVStrConst;
import frontend.Parser.declaration.variable.variableinitval.VarInitVal;
import frontend.Parser.expression.Exp;
import frontend.Parser.expression.arithmeticalexp.ConstExp;
import frontend.lexer.Token;
import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;
import middle.semantic.Symbol;
import middle.semantic.SymbolConst;
import middle.semantic.SymbolTable;
import middle.semantic.SymbolVar;

import java.util.ArrayList;

public class IrGlobalVar extends IrValue
{
    private SymbolTable symbolTable;
    private Decl decl;
    private String name;
    private boolean isConst;
    private String initvalue;
    private ArrayList<String> initvalues;
    private int num;
    private IrType type;
    private boolean isArray;

    public IrGlobalVar(SymbolTable symbolTable, Decl decl, IrType type)
    {
        super(type);
        this.symbolTable = symbolTable;
        this.decl = decl;
        this.type = type;
    }

    public IrGlobalVar(IrType type, String name, boolean isConst, String initvalue)
    {
        super(type);
        this.name = name;
        this.initvalue = initvalue;
        this.type = type;
        this.isConst = isConst;
    }

    public IrGlobalVar(IrType type, String name, boolean isConst, ArrayList<String> initvalues, int num)
    {
        super(type);
        this.name = name;
        this.initvalues = initvalues;
        this.type = type;
        this.isConst = isConst;
        this.num = num;
    }

    public IrGlobalVar(IrType type, String name, boolean isConst, ArrayList<String> initvalues, int num, boolean isArray)
    {
        super(type);
        this.name = name;
        this.initvalues = initvalues;
        this.type = type;
        this.isConst = isConst;
        this.num = num;
        this.isArray = isArray;
    }

    public void setInit(String initvalue)
    {
        this.initvalue = initvalue;
    }

    public String getInit()
    {
        return initvalue;
    }

    public String getName()
    {
        return "@" + name;
    }

    public int getNum()
    {
        return num;
    }

    public ArrayList<IrGlobalVar> buildGlobalVar()
    {
        ArrayList<IrGlobalVar> globalVars = new ArrayList<>();
        DeclAll declAll = decl.getDecl();
        if (declAll instanceof ConstDecl)
        {
            ConstDecl constDecl = (ConstDecl) declAll;
            for (ConstDef def : constDecl.getConstDefs())
            {
                globalVars.add(handleIRConstGlobalVar(def, constDecl.getBtype().getToken()));
            }
        }
        else if (declAll instanceof VarDecl)
        {
            VarDecl varDecl = (VarDecl) declAll;
            for (VarDef def : varDecl.getVarDefs())
            {
                globalVars.add(handleIRGlobalVar(def, varDecl.getBtype().getToken()));
            }
        }
        else
        {
            System.out.println("ERROR in IrBuilder.genIrGlobalVariable! Need Const or Var Decl");
        }
        return globalVars;
    }

    public IrGlobalVar handleIRConstGlobalVar(ConstDef constDef, Token type)
    {
        Token ident = constDef.getIdent();
        ConstExp constExp = constDef.getConstExp();
        SymbolConst symbolConst = null;
        int num;
        if (constExp == null)
        {
            if (type.getType() == Token.Type.INTTK)
            {
                symbolConst = new SymbolConst(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.ConstInt);
                //symbolConst.setIrValue(new IrValue(IrType.i32, "@" + ident.getContent()));
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                symbolConst = new SymbolConst(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.ConstChar);
                //symbolConst.setIrValue(new IrValue(IrType.i8, "@" + ident.getContent()));
            }
            num = 1;
        }
        else
        {
            if (type.getType() == Token.Type.INTTK)
            {
                symbolConst = new SymbolConst(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.ConstIntArray);
                //symbolConst.setIrValue(new IrValue(IrType.n_i32, "@" + ident.getContent()));
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                symbolConst = new SymbolConst(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.ConstCharArray);
                //symbolConst.setIrValue(new IrValue(IrType.n_i8, "@" + ident.getContent()));
            }
            num = constExp.calcExp(symbolTable);
        }
        boolean isArray = setConInitVal(symbolConst, constDef.getConstInitVal(), num);
        symbolTable.addSymbol(symbolConst);
        IrGlobalVar irGlobalVar = null;
        boolean isConst = true;
        if (constExp == null)
        {
            if (type.getType() == Token.Type.INTTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.i32, ident.getContent(), isConst, symbolConst.getValue());
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.i8, ident.getContent(), isConst, symbolConst.getValue());
            }

            symbolConst.setIrValue(irGlobalVar);
        }
        else
        {
            ArrayList<String> values = symbolConst.getConstarray();
            if (type.getType() == Token.Type.INTTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.n_i32, ident.getContent(), isConst, values, num);
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.n_i8, ident.getContent(), isConst, values, num, isArray);
            }
            symbolConst.setIrValue(irGlobalVar);
        }
        return irGlobalVar;
    }

    public IrGlobalVar handleIRGlobalVar(VarDef varDef, Token type)
    {
        Token ident = varDef.getIdent();
        ConstExp constExp = varDef.getConstExp();
        SymbolVar symbolVar = null;
        int num;
        if (constExp == null)
        {
            if (type.getType() == Token.Type.INTTK)
            {
                symbolVar = new SymbolVar(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.Int);
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                symbolVar = new SymbolVar(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.Char);
            }
            num = 0;
        }
        else
        {
            if (type.getType() == Token.Type.INTTK)
            {
                symbolVar = new SymbolVar(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.IntArray);
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                symbolVar = new SymbolVar(ident.getLine(), symbolTable.getLevel(), ident.getContent(), Symbol.Type.CharArray);
            }
            num = constExp.calcExp(symbolTable);
        }
        boolean isArray = setVarInitVal(symbolVar, varDef.getVarInitVal(), num);
        symbolTable.addSymbol(symbolVar);
        IrGlobalVar irGlobalVar = null;
        boolean isConst = false;
        if (constExp == null)
        {
            if (type.getType() == Token.Type.INTTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.i32, ident.getContent(), isConst, symbolVar.getValue());
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.i8, ident.getContent(), isConst, symbolVar.getValue());
            }

            symbolVar.setIrValue(irGlobalVar);
        }
        else
        {
            ArrayList<String> values = symbolVar.getArray();
            if (type.getType() == Token.Type.INTTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.n_i32, ident.getContent(), isConst, values, num);
            }
            else if (type.getType() == Token.Type.CHARTK)
            {
                irGlobalVar = new IrGlobalVar(IrType.n_i8, ident.getContent(), isConst, values, num, isArray);
            }
            symbolVar.setIrValue(irGlobalVar);
        }
        return irGlobalVar;
    }

    public boolean setConInitVal(SymbolConst symbol, ConstInitVal initVal, int num)
    {
        boolean isArray = false;
        if (symbol.getType() == Symbol.Type.ConstInt || symbol.getType() == Symbol.Type.ConstChar)
        { // 常量初值
            ConIVConstExp conIVConstExp = (ConIVConstExp) initVal.getConstInitVal();
            ConstExp exp = conIVConstExp.getConstExp();
            symbol.setValue(String.valueOf(exp.calcExp(symbolTable)));
            symbol.setConstvar(String.valueOf(exp.calcExp(symbolTable)));
        }
        else
        { // 一维数组初值
            ArrayList<String> values = new ArrayList<>();
            if (symbol.getType() == Symbol.Type.ConstIntArray)
            {
                ConIVArray conIVArray = (ConIVArray) initVal.getConstInitVal();
                for (ConstExp constExp : conIVArray.getConstExps())
                {
                    values.add(String.valueOf(constExp.calcExp(symbolTable)));
                }
                symbol.setConstarray(values);
                isArray = true;
            }
            else if (symbol.getType() == Symbol.Type.ConstCharArray)
            {
                if (initVal.getConstInitVal() instanceof ConIVStrConst)
                {
                    ConIVStrConst conIVStrConst = (ConIVStrConst) initVal.getConstInitVal();
                    Token string = conIVStrConst.getStringconst();
                    StringBuilder sb = new StringBuilder();
                    String s = string.getContent();
                    s = s.substring(0, s.length() - 1);
                    sb.append(s);
                    for (int i = s.length(); i < num + 1; i++)
                    {
                        sb.append("\\00");
                    }
                    sb.append("\"");
                    values.add(sb.toString());
                    symbol.setConstarray(values);
                }
                else if (initVal.getConstInitVal() instanceof ConIVArray)
                {
                    isArray = true;
                    ConIVArray conIVArray = (ConIVArray) initVal.getConstInitVal();
                    for (ConstExp constExp : conIVArray.getConstExps())
                    {
                        //values.add(String.valueOf((char) constExp.calcExp(symbolTable)));
                        values.add(String.valueOf(constExp.calcExp(symbolTable)));
                    }
                    symbol.setConstarray(values);
                }
            }
        }
        return isArray;
    }

    public boolean setVarInitVal(SymbolVar symbol, VarInitVal initVal, int num)
    {
        boolean isArray = false;
        if (initVal != null)
        {
            // 有初始化的全局变量

            if (symbol.getType() == Symbol.Type.Int || symbol.getType() == Symbol.Type.Char)
            {
                VarIVExp varIVExp = (VarIVExp) initVal.getVarInitVal();
                symbol.setValue(String.valueOf(varIVExp.getExp().getExp().calcExp(symbolTable)));
                symbol.setVar(String.valueOf(varIVExp.getExp().getExp().calcExp(symbolTable)));
            }
            else
            {
                ArrayList<String> values = new ArrayList<>();
                if (symbol.getType() == Symbol.Type.IntArray)
                {
                    VarIVArray varIVArray = (VarIVArray) initVal.getVarInitVal();
                    for (Exp exp : varIVArray.getExps())
                    {
                        values.add(String.valueOf(exp.getExp().calcExp(symbolTable)));
                    }
                    symbol.setArray(values);
                }
                else if (symbol.getType() == Symbol.Type.CharArray)
                {
                    if (initVal.getVarInitVal() instanceof VarIVStrConst)
                    {
                        VarIVStrConst varIVStrConst = (VarIVStrConst) initVal.getVarInitVal();
                        Token string = varIVStrConst.getStringconst();
                        StringBuilder sb = new StringBuilder();
                        String s = string.getContent();
                        s = s.substring(0, s.length() - 1);
                        sb.append(s);
                        for (int i = s.length(); i < num + 1; i++)
                        {
                            sb.append("\\00");
                        }
                        sb.append("\"");
                        values.add(sb.toString());
                        symbol.setArray(values);
                    }
                    else if (initVal.getVarInitVal() instanceof VarIVArray)
                    {
                        isArray = true;
                        VarIVArray varIVArray = (VarIVArray) initVal.getVarInitVal();
                        for (Exp exp : varIVArray.getExps())
                        {
                            //values.add(String.valueOf((char) exp.getExp().calcExp(symbolTable)));
                            values.add(String.valueOf(exp.getExp().calcExp(symbolTable)));
                        }
                        symbol.setArray(values);
                    }
                }
            }
        }
        else
        {
            // 无初始化的全局变量
            if (symbol.getType() == Symbol.Type.Int || symbol.getType() == Symbol.Type.Char)
            {
                symbol.setValue("0"); // 为初始化的全局变量的初值为0
                symbol.setVar("0");
            }
            else
            {
                symbol.setVar("0");
            }
        }
        return isArray;
    }

    //todo
    //有初始值的数组仍然输出zeroinitial
    @Override
    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        if (this.isConst)
        {
            // 全局常量，一定被赋初值
            if (type == IrType.i32)
            {
                String s = "@" + name + " = dso_local global i32 "
                        + initvalue + "\n";
                ret.add(s);
            }
            else if (type == IrType.i8)
            {
                String s = "@" + name + " = dso_local global i8 "
                        + initvalue + "\n";
                ret.add(s);
            }
            //数组
            else if (type == IrType.n_i32)
            {
                StringBuilder sb = new StringBuilder();
                if (initvalues == null || initvalues.isEmpty())
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i32] zeroinitializer, align 4\n");
                }
                else
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i32] [");
                    sb.append("i32 " + initvalues.get(0));
                    for (int i = 1; i < initvalues.size(); i++)
                    {
                        sb.append(", i32 " + initvalues.get(i));
                    }
                    for (int i = initvalues.size(); i < num; i++)
                    {
                        sb.append(", i32 0");
                    }
                    sb.append("]\n");
                }
                ret.add(sb.toString());
            }
            else if (type == IrType.n_i8)
            {
                StringBuilder sb = new StringBuilder();
                if (initvalues == null || initvalues.isEmpty())
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i8] zeroinitializer, align 1\n");
                }
                else if (!isArray)
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i8] c");
                    String s = initvalues.get(0);
                    //s = s.substring(0, s.length() - 1);
                    sb.append(s);
                    /*
                    for (int i = s.length(); i < num + 1; i++)
                    {
                        sb.append("\\00");
                    }
                    sb.append("\", align 1\n");
                     */
                    sb.append(", align 1\n");
                }
                else if (isArray)
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i8] [");
                    sb.append("i8 " + initvalues.get(0));
                    for (int i = 1; i < initvalues.size(); i++)
                    {
                        sb.append(", i8 " + initvalues.get(i));
                    }
                    for (int i = initvalues.size(); i < num; i++)
                    {
                        sb.append(", i8 0");
                    }
                    sb.append("]\n");
                }
                ret.add(sb.toString());
            }
            //
            else
            {
                System.out.println("ERROR in IrGlobalVariable : should not reach here");
            }
        }
        else
        {
            // 全局变量，可能被赋初值，对为赋值的赋0
            if (type == IrType.i32)
            {
                String s = "@" + name + " = dso_local global i32 "
                        + initvalue + "\n";
                ret.add(s);
            }
            else if (type == IrType.i8)
            {
                String s = "@" + name + " = dso_local global i8 "
                        + initvalue + "\n";
                ret.add(s);
            }
            else if (type == IrType.n_i32)
            {
                StringBuilder sb = new StringBuilder();
                if (initvalues == null || initvalues.isEmpty())
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i32] zeroinitializer \n");
                }
                else
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i32] [");
                    sb.append("i32 " + initvalues.get(0));
                    for (int i = 1; i < initvalues.size(); i++)
                    {
                        sb.append(", i32 " + initvalues.get(i));
                    }
                    for (int i = initvalues.size(); i < num; i++)
                    {
                        sb.append(", i32 0");
                    }
                    sb.append("]\n");
                }
                ret.add(sb.toString());
            }
            else if (type == IrType.n_i8)
            {
                StringBuilder sb = new StringBuilder();
                if (initvalues == null || initvalues.isEmpty())
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i8] zeroinitializer, align 1\n");
                }
                else if (!isArray)
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i8] c");
                    String s = initvalues.get(0);
                    //s = s.substring(0, s.length() - 1);
                    sb.append(s);
                    /*
                    for (int i = s.length(); i < num + 1; i++)
                    {
                        sb.append("\\00");
                    }
                    sb.append("\", align 1\n");
                     */
                    sb.append(", align 1\n");
                }
                else if (isArray)
                {
                    sb.append("@" + name + " = dso_local global [" + num + " x i8] [");
                    sb.append("i8 " + initvalues.get(0));
                    for (int i = 1; i < initvalues.size(); i++)
                    {
                        sb.append(", i8 " + initvalues.get(i));
                    }
                    for (int i = initvalues.size(); i < num; i++)
                    {
                        sb.append(", i8 0");
                    }
                    sb.append("]\n");
                }
                ret.add(sb.toString());
            }
            else
            {
                System.out.println("ERROR in IrGlobalVariable : should not reach here");
            }
        }
        return ret;
    }
}
