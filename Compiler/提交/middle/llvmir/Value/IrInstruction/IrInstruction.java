package middle.llvmir.Value.IrInstruction;

import frontend.Parser.declaration.Decl;
import frontend.Parser.declaration.constant.ConstDecl;
import frontend.Parser.declaration.constant.ConstDef;
import frontend.Parser.declaration.constant.consinitval.*;
import frontend.Parser.declaration.variable.VarDecl;
import frontend.Parser.declaration.variable.VarDef;
import frontend.Parser.declaration.variable.variableinitval.*;
import frontend.Parser.expression.CondExp;
import frontend.Parser.expression.Exp;
import frontend.Parser.expression.FuncRParams;
import frontend.Parser.expression.arithmeticalexp.*;
import frontend.Parser.expression.primaryexp.Character;
import frontend.Parser.expression.primaryexp.ExpPrim;
import frontend.Parser.expression.primaryexp.LVal;
import frontend.Parser.expression.primaryexp.Number;
import frontend.Parser.expression.primaryexp.PrimaryAllExp;
import frontend.Parser.expression.primaryexp.PrimaryExp;
import frontend.Parser.expression.unaryexp.*;
import frontend.Parser.statement.*;
import frontend.Parser.statement.block.BlockDecl;
import frontend.Parser.statement.block.BlockItem;
import frontend.Parser.statement.block.BlockStmt;
import frontend.Parser.statement.block.StmtBlock;
import frontend.Parser.statement.stmtreturn.StmtReturn;
import frontend.lexer.Token;
import middle.llvmir.Value.GlobalValue.IrFunction;
import middle.llvmir.Value.GlobalValue.IrGlobalVar;
import middle.llvmir.Value.GlobalValue.IrString;
import middle.llvmir.Value.IrArgument;
import middle.llvmir.Value.IrBasicBlock;
import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;
import middle.semantic.*;

import java.util.ArrayList;

public class IrInstruction extends IrValue
{
    private ArrayList<IrInstruction> irinstructions;
    private IrBasicBlock irBasicBlock;
    private SymbolTable symbolTable;
    private BlockItem blockItem;
    private CondExp condExp;
    private Stmt stmt;
    private AddExp addExp;

    public IrInstruction(IrType type)
    {
        super(type);
    }

    public IrInstruction(IrType type, String name)
    {
        super(type, name);
    }

    public IrInstruction(IrBasicBlock irBasicBlock, SymbolTable symbolTable, BlockItem blockItem)
    {
        super(null);
        this.irBasicBlock = irBasicBlock;
        this.symbolTable = symbolTable;
        this.blockItem = blockItem;
        this.irinstructions = new ArrayList<>();
    }

    public IrInstruction(SymbolTable symbolTable, CondExp condExp)
    {
        super(null);
        this.symbolTable = symbolTable;
        this.condExp = condExp;
        this.irinstructions = new ArrayList<>();
    }

    public IrInstruction(IrBasicBlock irBasicBlock, SymbolTable symbolTable, Stmt stmt)
    {
        super(null);
        this.irBasicBlock = irBasicBlock;
        this.symbolTable = symbolTable;
        this.stmt = stmt;
        this.irinstructions = new ArrayList<>();
    }

    public IrInstruction(IrBasicBlock irBasicBlock, SymbolTable symbolTable, AddExp addExp)
    {
        super(null);
        this.irBasicBlock = irBasicBlock;
        this.symbolTable = symbolTable;
        this.addExp = addExp;
        this.irinstructions = new ArrayList<>();
    }

    public ArrayList<IrInstruction> buildIrInstruction()
    {
        if (blockItem != null)
        {
            if (blockItem instanceof BlockDecl)
            {
                Decl decl = ((BlockDecl) blockItem).getDecl();
                if (decl.getValuetype().equals("const"))
                {
                    handleIRConst(decl);
                }
                else if (decl.getValuetype().equals("var"))
                {
                    handleIRVar(decl);
                }
            }
            else if (blockItem instanceof BlockStmt)
            {
                Stmt stmt = ((BlockStmt) blockItem).getStmt();
                StmtAll stmtAll = stmt.getStmt();
                handleStmt(stmtAll);
            }
        }
        else if (stmt != null)
        {
            StmtAll stmtAll = stmt.getStmt();
            handleStmt(stmtAll);
        }
        return irinstructions;
    }

    public void handleStmt(StmtAll stmtAll)
    {
        if (stmtAll instanceof StmtAssign)
        {
            // 处理 StmtAssign 类型
            System.out.println("处理赋值语句");
            StmtAssign stmtAssign = (StmtAssign) stmtAll;
            handleIRAssign(stmtAssign);  // 处理赋值的中间代码生成函数
        }
        else if (stmtAll instanceof StmtBreak)
        {
            // 处理 StmtBreak 类型
            System.out.println("处理 break 语句");
            StmtBreak stmtBreak = (StmtBreak) stmtAll;
            // 这里可以添加针对 StmtBreak 类型的特定操作，比如中断循环的逻辑
            handleIRBreak(stmtBreak);  // 假设有中间代码生成的处理函数
        }
        else if (stmtAll instanceof StmtContinue)
        {
            // 处理 StmtContinue 类型
            System.out.println("处理 continue 语句");
            StmtContinue stmtContinue = (StmtContinue) stmtAll;
            // 这里可以添加针对 StmtContinue 类型的特定操作，比如跳过当前循环
            handleIRContinue(stmtContinue);  // 假设有处理 continue 的中间代码生成函数
        }
        else if (stmtAll instanceof StmtReturn)
        {
            // 处理 StmtReturn 类型
            System.out.println("处理 return 语句");
            StmtReturn stmtReturn = (StmtReturn) stmtAll;
            // 这里可以添加针对 StmtReturn 类型的操作，比如生成函数返回的中间代码
            handleIRReturn(stmtReturn);  // 假设有处理 return 的中间代码生成函数
        }
        else if (stmtAll instanceof StmtPrintf)
        {
            // 处理 StmtPrintf 类型
            System.out.println("处理 printf 语句");
            StmtPrintf stmtPrintf = (StmtPrintf) stmtAll;
            // 这里可以添加针对 StmtPrintf 类型的操作，例如生成 printf 的相关代码
            handleIRPrintf(stmtPrintf);  // 假设有处理 printf 的中间代码生成函数
        }
        else if (stmtAll instanceof StmtGet)
        {
            // 处理 StmtGet 类型
            System.out.println("处理 get 语句");
            StmtGet stmtGet = (StmtGet) stmtAll;
            // 假设 StmtGet 是用来获取输入的语句类型，可能会有输入相关的处理逻辑
            handleIRGet(stmtGet);  // 假设有处理 get 输入的中间代码生成函数
        }
        else if (stmtAll instanceof StmtExp)
        {
            // 处理 StmtExp 类型
            System.out.println("处理 expression 语句");
            StmtExp stmtExp = (StmtExp) stmtAll;
            if (stmtExp.getExp() != null)
            {
                handleIRStmtExp(stmtExp);  // 处理有表达式的中间代码生成函数
            }
        }
        else
        {
            // 默认处理
            System.out.println("未知的语句类型");
        }
    }

    public void handleIRConst(Decl decl)
    {
        ConstDecl constDecl = (ConstDecl) decl.getDecl();
        Token type = constDecl.getBtype().getToken();
        ArrayList<ConstDef> constDefs = constDecl.getConstDefs();

        for (ConstDef constDef : constDefs)
        {
            addConst(constDef, type);
        }

    }

    public void addConst(ConstDef constDef, Token type)
    {
        SymbolConst symbol = new SymbolConst(0, 0, constDef.getIdent().getContent(), null);
        if (constDef.getConstExp() == null && type.getType() == Token.Type.INTTK)
        {
            symbol.setType(Symbol.Type.ConstInt);
            String value = getAValue();
            symbolTable.addSymbol(symbol);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.i32, 1);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (constDef.getConstInitVal() != null)
            {
                ConstVarStore(constDef, symbol);
            }
        }
        else if (constDef.getConstExp() == null && type.getType() == Token.Type.CHARTK)
        {
            symbol.setType(Symbol.Type.ConstChar);
            String value = getAValue();
            symbolTable.addSymbol(symbol);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.i8, 1);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (constDef.getConstInitVal() != null)
            {
                ConstVarStore(constDef, symbol);
            }
        }
        else if (type.getType() == Token.Type.INTTK)
        {
            symbol.setType(Symbol.Type.ConstIntArray);
            String value = getAValue();
            symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            int num = constDef.getConstExp().calcExp(symbolTable);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.n_i32, num);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (constDef.getConstInitVal() != null)
            {
                ConstArrayStore(num, constDef, symbol);
            }
        }
        else if (type.getType() == Token.Type.CHARTK)
        {
            symbol.setType(Symbol.Type.ConstCharArray);
            String value = getAValue();
            symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            int num = constDef.getConstExp().calcExp(symbolTable);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.n_i8, num);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (constDef.getConstInitVal() != null)
            {
                ConstArrayStore(num, constDef, symbol);
            }
        }
        //setValue(constDef, symbol);
        symbolTable.addSymbol(symbol);
    }

    public void handleIRVar(Decl decl)
    {
        VarDecl varDecl = (VarDecl) decl.getDecl();
        Token type = varDecl.getBtype().getToken();
        ArrayList<VarDef> varDefs = varDecl.getVarDefs();
        for (VarDef varDef : varDefs)
        {
            addVar(varDef, type);
        }
    }

    public void addVar(VarDef varDef, Token type)
    {
        Symbol symbol = new Symbol(0, 0, varDef.getIdent().getContent(), null);
        if (varDef.getConstExp() == null && type.getType() == Token.Type.INTTK)
        {
            symbol.setType(Symbol.Type.Int);
            String value = getAValue();
            symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.i32, 1);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (varDef.getVarInitVal() != null)
            {
                VarStore(varDef, symbol);
            }
            else
            {
                IrStoreInst irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i32, "0");
                //irStoreInst.setNeedInit(true);
                irinstructions.add(irStoreInst);
            }
        }
        else if (varDef.getConstExp() == null && type.getType() == Token.Type.CHARTK)
        {
            symbol.setType(Symbol.Type.Char);
            String value = getAValue();
            symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.i8, 1);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (varDef.getVarInitVal() != null)
            {
                VarStore(varDef, symbol);
            }
            else
            {
                IrStoreInst irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i8, "0");
                //irStoreInst.setNeedInit(true);
                irinstructions.add(irStoreInst);
            }
        }
        else if (type.getType() == Token.Type.INTTK)
        {
            //SymbolVar symbolVar = new SymbolVar(0, 0, varDef.getIdent().getContent(), Symbol.Type.IntArray);
            //symbol = symbolVar;
            symbol.setType(Symbol.Type.IntArray);
            String value = getAValue();
            //symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            int num = varDef.getConstExp().calcExp(symbolTable);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.n_i32, num);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (varDef.getVarInitVal() != null)
            {
                ArrayStore(num, varDef, symbol);
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                IrStoreInst irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.n_i32, "");
                irStoreInst.setNum(num);
                irinstructions.add(irStoreInst);
            }
        }
        else if (type.getType() == Token.Type.CHARTK)
        {
            //SymbolVar symbolVar = new SymbolVar(0, 0, varDef.getIdent().getContent(), Symbol.Type.CharArray);
            //symbol = symbolVar;
            symbol.setType(Symbol.Type.CharArray);
            String value = getAValue();
            //symbol.setValue(value);
            symbolTable.addSymbol(symbol);
            int num = varDef.getConstExp().calcExp(symbolTable);
            IrAllocaInst irAllocaInst = new IrAllocaInst(value, IrType.n_i8, num);
            irinstructions.add(irAllocaInst);
            symbol.setIrValue(irAllocaInst);
            if (varDef.getVarInitVal() != null)
            {
                ArrayStore(num, varDef, symbol);
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                IrStoreInst irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.n_i8, "");
                irStoreInst.setNum(num);
                irinstructions.add(irStoreInst);
            }
        }
        symbolTable.addSymbol(symbol);
    }

    public void ConstVarStore(ConstDef constDef, SymbolConst symbol)
    {
        ConstAllInitVal constAllInitVal = constDef.getConstInitVal().getConstInitVal();
        ConIVConstExp conIVConstExp = (ConIVConstExp) constAllInitVal;
        IrValue irValue = handleIRConstExp(conIVConstExp.getConstExp(), false);
        symbol.setValue(irValue.getName());
        IrStoreInst irStoreInst = null;
        if (symbol.getType() == Symbol.Type.ConstInt)
        {
            if (irValue.getValueType() != IrType.i32)
            {
                String value_zext = getAValue();
                IrZext irZext = new IrZext(IrType.i8, value_zext, irValue, IrType.i32);
                irValue = irZext;
                irinstructions.add(irZext);
            }
            //修改
            //irStoreInst = new IrStoreInst(symbol.getValue(), IrType.i32, irValue);
            irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i32, irValue);
        }
        else if (symbol.getType() == Symbol.Type.ConstChar)
        {
            if (irValue.getValueType() != IrType.i8)
            {
                String value_trunc = getAValue();
                IrTrunc irTrunc = new IrTrunc(IrType.i32, value_trunc, irValue, IrType.i8);
                irValue = irTrunc;
                irinstructions.add(irTrunc);
            }
            //修改
            irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i8, irValue);
            //irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i8, irValue);
        }
        symbol.setIrValue(irStoreInst);
        irinstructions.add(irStoreInst);
    }

    public void VarStore(VarDef varDef, Symbol symbol)
    {
        VarAllInitVal varAllInitVal = varDef.getVarInitVal().getVarInitVal();
        VarIVExp varIVExp = (VarIVExp) varAllInitVal;
        IrValue irValue = handleIRExp(varIVExp.getExp(), false);
        symbol.setValue(irValue.getName());
        IrStoreInst irStoreInst = null;
        if (symbol.getType() == Symbol.Type.Int)
        {
            if (irValue.getValueType() != IrType.i32)
            {
                String value_zext = getAValue();
                IrZext irZext = new IrZext(IrType.i8, value_zext, irValue, IrType.i32);
                irValue = irZext;
                irinstructions.add(irZext);
            }
            //修改
            //irStoreInst = new IrStoreInst(symbol.getValue(), IrType.i32, irValue);
            irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i32, irValue);
        }
        else if (symbol.getType() == Symbol.Type.Char)
        {
            if (irValue.getValueType() != IrType.i8)
            {
                String value_trunc = getAValue();
                IrTrunc irTrunc = new IrTrunc(IrType.i32, value_trunc, irValue, IrType.i8);
                irValue = irTrunc;
                irinstructions.add(irTrunc);
            }
            //修改
            irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i8, irValue);
            //irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.i8, irValue);
        }

        symbol.setIrValue(irStoreInst);
        irinstructions.add(irStoreInst);
    }

    public void ConstArrayStore(int num, ConstDef constDef, SymbolConst symbol)
    {
        ConstInitVal constInitVal = constDef.getConstInitVal();
        if (symbol.getType() == Symbol.Type.ConstIntArray)
        {
            ConIVArray conIVArray = (ConIVArray) constInitVal.getConstInitVal();
            ArrayList<ConstExp> constExps = conIVArray.getConstExps();
            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < num; i++)
            {
                IrValue irValue = null;
                if (i < constExps.size())
                {
                    irValue = handleIRConstExp(constExps.get(i), false);
                }
                else
                {
                    irValue = new IrValue(IrType.i32, "0");
                }
                String value = getAValue();
                values.add(irValue.getName());
                IrGetelement irGetelement = new IrGetelement(value, IrType.n_i32, symbol.getIrValue(), String.valueOf(i));
                irinstructions.add(irGetelement);
                if (irValue.getValueType() != IrType.i32)
                {
                    String value_zext = getAValue();
                    IrZext irZext = new IrZext(IrType.i8, value_zext, irValue, IrType.i32);
                    irValue = irZext;
                    irinstructions.add(irZext);
                }
                //修改
                //IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i32, irValue);
                IrStoreInst irStoreInst = new IrStoreInst(irGetelement, IrType.i32, irValue);
                irinstructions.add(irStoreInst);
            }
            symbol.setConstarray(values);
        }
        else if (symbol.getType() == Symbol.Type.ConstCharArray)
        {
            ArrayList<String> values = new ArrayList<>();
            if (constInitVal.getConstInitVal() instanceof ConIVStrConst)
            {
                ConIVStrConst conIVStrConst = (ConIVStrConst) constInitVal.getConstInitVal();
                Token string = conIVStrConst.getStringconst();
                String s = string.getContent();
                s = s.substring(1, s.length() - 1);
                ArrayList<java.lang.Character> charvalues = new ArrayList<>();
                for (int i = 0; i < s.length(); i++)
                {
                    if (s.charAt(i) == '\\' && i + 1 < s.length())
                    {
                        // 处理 \\ (反斜杠)
                        if (s.charAt(i + 1) == '\\')
                        {
                            charvalues.add('\\');
                            i++;
                            continue;
                        }
                        // 处理 \0 (null字符)
                        if (s.charAt(i + 1) == '0')
                        {
                            charvalues.add('\0');
                            i++;
                            continue;
                        }
                        // 处理 \t (制表符)
                        if (s.charAt(i + 1) == 't')
                        {
                            charvalues.add('\t');  // 在原代码中，\t被转换成了'9'
                            i++;
                            continue;
                        }
                        // 处理 \a (响铃)
                        if (s.charAt(i + 1) == 'a')
                        {
                            charvalues.add((char) 7);  // \a被转换成了'7'
                            i++;
                            continue;
                        }
                        // 处理 \b (退格符)
                        if (s.charAt(i + 1) == 'b')
                        {
                            charvalues.add('\b');  // \b被转换成了'8'
                            i++;
                            continue;
                        }
                        // 处理 \n (换行符)
                        if (s.charAt(i + 1) == 'n')
                        {
                            charvalues.add('\n');  // \n被添加为换行符
                            i++;
                            continue;
                        }
                        // 处理 \v (垂直制表符)
                        if (s.charAt(i + 1) == 'v')
                        {
                            charvalues.add('\u000B');  // Unicode for vertical tab
                            i++;
                            continue;
                        }
                        // 处理 \f (换页符)
                        if (s.charAt(i + 1) == 'f')
                        {
                            charvalues.add('\f');  // \f被添加为换页符
                            i++;
                            continue;
                        }
                        // 处理 \" (双引号)
                        if (s.charAt(i + 1) == '\"')
                        {
                            charvalues.add('\"');  // 双引号被添加
                            i++;
                            continue;
                        }
                        // 处理 \' (单引号)
                        if (s.charAt(i + 1) == '\'')
                        {
                            charvalues.add('\'');  // 单引号被添加
                            i++;
                            continue;
                        }
                    }
                    else
                    {
                        // 处理非转义字符，直接添加到 charvalues
                        charvalues.add(s.charAt(i));
                    }
                }
                IrStoreInst irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.n_i8, charvalues);
                irStoreInst.setNum(num);
                irinstructions.add(irStoreInst);
            }
            else if (constInitVal.getConstInitVal() instanceof ConIVArray)
            {
                ConIVArray conIVArray = (ConIVArray) constInitVal.getConstInitVal();
                ArrayList<ConstExp> constExps = conIVArray.getConstExps();
                for (int i = 0; i < num; i++)
                {
                    IrValue irValue = null;
                    if (i < constExps.size())
                    {
                        irValue = handleIRConstExp(constExps.get(i), false);
                    }
                    else
                    {
                        irValue = new IrValue(IrType.i8, "0");
                    }
                    values.add(irValue.getName());
                    String value = getAValue();
                    IrGetelement irGetelement = new IrGetelement(value, IrType.n_i8, symbol.getIrValue(), String.valueOf(i));
                    irinstructions.add(irGetelement);
                    if (irValue.getValueType() != IrType.i8)
                    {
                        String value_trunc = getAValue();
                        IrTrunc irTrunc = new IrTrunc(IrType.i32, value_trunc, irValue, IrType.i8);
                        irValue = irTrunc;
                        irinstructions.add(irTrunc);
                    }
                    //修改
                    //IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i8, irValue);
                    IrStoreInst irStoreInst = new IrStoreInst(irGetelement, IrType.i8, irValue);
                    irinstructions.add(irStoreInst);
                }
            }
            symbol.setConstarray(values);
        }
    }

    public void ArrayStore(int num, VarDef varDef, Symbol symbol)
    {
        VarInitVal varInitVal = varDef.getVarInitVal();
        if (symbol.getType() == Symbol.Type.IntArray)
        {
            VarIVArray varIVArray = (VarIVArray) varInitVal.getVarInitVal();
            ArrayList<Exp> exps = varIVArray.getExps();
            for (int i = 0; i < num; i++)
            {
                IrValue irValue = null;
                if (i < exps.size())
                {
                    irValue = handleIRExp(exps.get(i), false);
                }
                else
                {
                    irValue = new IrValue(IrType.i32, "0");
                }
                String value = getAValue();
                IrGetelement irGetelement = new IrGetelement(value, IrType.n_i32, symbol.getIrValue(), String.valueOf(i));
                irinstructions.add(irGetelement);
                if (irValue.getValueType() != IrType.i32)
                {
                    String value_zext = getAValue();
                    IrZext irZext = new IrZext(IrType.i8, value_zext, irValue, IrType.i32);
                    irValue = irZext;
                    irinstructions.add(irZext);
                }
                //修改
                //IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i32, irValue);
                IrStoreInst irStoreInst = new IrStoreInst(irGetelement, IrType.i32, irValue);
                irinstructions.add(irStoreInst);
            }
        }
        else if (symbol.getType() == Symbol.Type.CharArray)
        {
            if (varInitVal.getVarInitVal() instanceof VarIVStrConst)
            {
                VarIVStrConst varIVStrConst = (VarIVStrConst) varInitVal.getVarInitVal();
                Token string = varIVStrConst.getStringconst();
                String s = string.getContent();
                s = s.substring(1, s.length() - 1);
                ArrayList<java.lang.Character> values = new ArrayList<>();

                for (int i = 0; i < s.length(); i++)
                {
                    if (s.charAt(i) == '\\' && i + 1 < s.length())
                    {
                        // 处理 \\ (反斜杠)
                        if (s.charAt(i + 1) == '\\')
                        {
                            values.add('\\');
                            i++;
                            continue;
                        }
                        // 处理 \0 (null字符)
                        if (s.charAt(i + 1) == '0')
                        {
                            values.add('\0');
                            i++;
                            continue;
                        }
                        // 处理 \t (制表符)
                        if (s.charAt(i + 1) == 't')
                        {
                            values.add('\t');  // 在原代码中，\t被转换成了'9'
                            i++;
                            continue;
                        }
                        // 处理 \a (响铃)
                        if (s.charAt(i + 1) == 'a')
                        {
                            values.add((char) 7);  // \a被转换成了'7'
                            i++;
                            continue;
                        }
                        // 处理 \b (退格符)
                        if (s.charAt(i + 1) == 'b')
                        {
                            values.add('\b');  // \b被转换成了'8'
                            i++;
                            continue;
                        }
                        // 处理 \n (换行符)
                        if (s.charAt(i + 1) == 'n')
                        {
                            values.add('\n');  // \n被添加为换行符
                            i++;
                            continue;
                        }
                        // 处理 \v (垂直制表符)
                        if (s.charAt(i + 1) == 'v')
                        {
                            values.add('\u000B');  // Unicode for vertical tab
                            i++;
                            continue;
                        }
                        // 处理 \f (换页符)
                        if (s.charAt(i + 1) == 'f')
                        {
                            values.add('\f');  // \f被添加为换页符
                            i++;
                            continue;
                        }
                        // 处理 \" (双引号)
                        if (s.charAt(i + 1) == '\"')
                        {
                            values.add('\"');  // 双引号被添加
                            i++;
                            continue;
                        }
                        // 处理 \' (单引号)
                        if (s.charAt(i + 1) == '\'')
                        {
                            values.add('\'');  // 单引号被添加
                            i++;
                            continue;
                        }
                    }
                    else
                    {
                        // 处理非转义字符，直接添加到 values
                        values.add(s.charAt(i));
                    }
                }
                IrStoreInst irStoreInst = new IrStoreInst(symbol.getIrValue(), IrType.n_i8, values);
                irStoreInst.setNum(num);
                irinstructions.add(irStoreInst);
            }
            else if (varInitVal.getVarInitVal() instanceof VarIVArray)
            {
                VarIVArray varIVArray = (VarIVArray) varInitVal.getVarInitVal();
                ArrayList<Exp> exps = varIVArray.getExps();
                for (int i = 0; i < num; i++)
                {
                    IrValue irValue = null;
                    if (i < exps.size())
                    {
                        irValue = handleIRExp(exps.get(i), false);
                    }
                    else
                    {
                        irValue = new IrValue(IrType.i8, "0");
                    }
                    String value = getAValue();
                    IrGetelement irGetelement = new IrGetelement(value, IrType.n_i8, symbol.getIrValue(), String.valueOf(i));
                    irinstructions.add(irGetelement);
                    if (irValue.getValueType() != IrType.i8)
                    {
                        String value_trunc = getAValue();
                        IrTrunc irTrunc = new IrTrunc(IrType.i32, value_trunc, irValue, IrType.i8);
                        irValue = irTrunc;
                        irinstructions.add(irTrunc);
                    }
                    //修改
                    //IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i8, irValue);
                    IrStoreInst irStoreInst = new IrStoreInst(irGetelement, IrType.i8, irValue);
                    irinstructions.add(irStoreInst);
                }
            }

        }
    }

    public IrValue handleIRExp(Exp exp, boolean isLeft)
    {
        return handleIRAddExp(exp.getExp(), isLeft);
    }

    public IrValue handleIRConstExp(ConstExp constExp, boolean isLeft)
    {
        return handleIRAddExp(constExp.getExp(), isLeft);
    }

    public IrValue handleIRAddExp(AddExp addExp, boolean isLeft)
    {
        ArrayList<MulExp> mulExps = addExp.getExps();
        ArrayList<Token> ops = addExp.getOps();
        IrValue first = handleIRMulExp(mulExps.get(0), isLeft);
        if (ops.isEmpty())
        {
            return first;
        }
        else
        {
            IrValue exp1 = first;
            IrValue exp2 = null;
            for (int i = 0; i < ops.size(); i++)
            {
                exp2 = handleIRMulExp(mulExps.get(i + 1), isLeft);
                if (exp1.getValueType() == IrType.i8)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(IrType.i8, value, exp1, IrType.i32);
                    irinstructions.add(irZext);
                    exp1 = irZext;
                }
                if (exp2.getValueType() == IrType.i8)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(IrType.i8, value, exp2, IrType.i32);
                    irinstructions.add(irZext);
                    exp2 = irZext;
                }
                IrBinaryOperator irBinaryOperator = null;
                if (ops.get(i).getType() == Token.Type.PLUS)
                {
                    irBinaryOperator = new IrBinaryOperator(getAValue(), IrBinaryOperator.Type.Add, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.MINU)
                {
                    irBinaryOperator = new IrBinaryOperator(getAValue(), IrBinaryOperator.Type.Sub, IrType.i32, exp1, exp2);
                }
                irinstructions.add(irBinaryOperator);
                exp1 = irBinaryOperator;
                first = irBinaryOperator;
            }
            return first;
        }
    }

    public IrValue handleIRMulExp(MulExp mulExp, boolean isLeft)
    {
        ArrayList<UnaryExp> unaryExps = mulExp.getExps();
        ArrayList<Token> ops = mulExp.getOps();
        IrValue first = handleIRUnaryExp(unaryExps.get(0), isLeft);
        if (ops.isEmpty())
        {
            return first;
        }
        else
        {
            IrValue exp1 = first;
            IrValue exp2 = null;
            for (int i = 0; i < ops.size(); i++)
            {
                exp2 = handleIRUnaryExp(unaryExps.get(i + 1), isLeft);
                if (exp1.getValueType() == IrType.i8)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(IrType.i8, value, exp1, IrType.i32);
                    irinstructions.add(irZext);
                    exp1 = irZext;
                }
                if (exp2.getValueType() == IrType.i8)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(IrType.i8, value, exp2, IrType.i32);
                    irinstructions.add(irZext);
                    exp2 = irZext;
                }
                IrBinaryOperator irBinaryOperator = null;
                if (ops.get(i).getType() == Token.Type.MULT)
                {
                    irBinaryOperator = new IrBinaryOperator(getAValue(), IrBinaryOperator.Type.Mul, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.DIV)
                {
                    irBinaryOperator = new IrBinaryOperator(getAValue(), IrBinaryOperator.Type.Div, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.MOD)
                {
                    irBinaryOperator = new IrBinaryOperator(getAValue(), IrBinaryOperator.Type.Mod, IrType.i32, exp1, exp2);
                }
                irinstructions.add(irBinaryOperator);
                first = irBinaryOperator;
                exp1 = irBinaryOperator;
            }
            return first;
        }
    }

    public IrValue handleIRUnaryExp(UnaryExp unaryExp, boolean isLeft)
    {
        UnaryAll unaryexp = unaryExp.getUnaryexp();
        IrValue irValue = null;
        if (unaryexp instanceof UnaryPriExp)
        {
            UnaryPriExp unaryPriExp = (UnaryPriExp) unaryexp;
            irValue = handleUnaryPrim(unaryPriExp, isLeft);
        }
        else if (unaryexp instanceof UnaryFunc)
        {
            UnaryFunc unaryFunc = (UnaryFunc) unaryexp;
            irValue = handleUnaryFunc(unaryFunc, isLeft);
        }
        else if (unaryexp instanceof UnaryOp)
        {
            UnaryOp unaryOp = (UnaryOp) unaryexp;
            irValue = handleUnaryOp(unaryOp, isLeft);
        }
        else
        {
            System.out.println("UnaryExp error");
        }
        return irValue;
    }


    public IrValue handleUnaryPrim(UnaryPriExp unaryPriExp, boolean isLeft)
    {
        PrimaryAllExp primaryExp = unaryPriExp.getExp().getExp();
        IrValue irValue = null;
        if (primaryExp instanceof ExpPrim)
        {
            ExpPrim expPrim = (ExpPrim) primaryExp;
            irValue = handleIRExpPrim(expPrim, isLeft);
        }
        else if (primaryExp instanceof LVal)
        {
            LVal lVal = (LVal) primaryExp;
            irValue = handleIRLVal(lVal, false);
        }
        else if (primaryExp instanceof Number)
        {
            Number number = (Number) primaryExp;
            irValue = handleNumber(number);
        }
        else if (primaryExp instanceof Character)
        {
            Character character = (Character) primaryExp;
            irValue = handleCharacter(character);
        }
        else
        {
            System.out.println("UnaryPrim Error");
        }
        return irValue;
    }

    public IrValue handleIRLVal(LVal lVal, boolean isLeft)
    {
        IrValue irValue = null;
        String name = lVal.getIdent().getContent();
        Exp exp = lVal.getExp();
        if (exp == null)
        {
            Symbol symbol = symbolTable.getSymbol(name);
            if (isLeft)
            {
                irValue = symbol.getIrValue();
                if (irValue.isParam())
                {
                    IrValue left = irValue;
                    String value = null;
                    IrAllocaInst irAllocaInst = null;
                    if (symbol.getType() == Symbol.Type.Int)
                    {
                        if (irValue instanceof IrAllocaInst)
                        {
                            irAllocaInst = (IrAllocaInst) irValue;
                        }
                        else
                        {
                            left = new IrValue(IrType.i32);
                            value = getAValue();
                            irAllocaInst = new IrAllocaInst(value, IrType.i32, 1, true);
                            left.setName(name);
                            irinstructions.add(irAllocaInst);
                        }
                        //修改
                        //IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i32, irValue.getName());
                        IrStoreInst irStoreInst = new IrStoreInst(irAllocaInst, IrType.i32, irValue, true);
                        irinstructions.add(irStoreInst);
                        irValue = irStoreInst;
                    }
                    else if (symbol.getType() == Symbol.Type.Char)
                    {
                        if (irValue instanceof IrAllocaInst)
                        {
                            irAllocaInst = (IrAllocaInst) irValue;
                        }
                        else
                        {
                            left = new IrValue(IrType.i8);
                            value = getAValue();
                            irAllocaInst = new IrAllocaInst(value, IrType.i8, 1);
                            left.setName(name);
                            irinstructions.add(irAllocaInst);
                        }
                        //修改
                        //IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i8, irValue.getName());
                        IrStoreInst irStoreInst = new IrStoreInst(irAllocaInst, IrType.i8, irValue);
                        irStoreInst.setName(name);
                        irinstructions.add(irStoreInst);
                        irValue = irStoreInst;
                    }

                    //修改
                    symbol.setIrValue(irAllocaInst);
                }
                return irValue;
            }
            else
            {
                IrValue ptr = symbol.getIrValue();
                String name1 = ptr.getName();
                if (!(ptr.getName().contains("%") || ptr.getName().contains("@")))
                {
                    return ptr;
                }

                if (ptr.isParam())
                {
                    IrAllocaInst irAllocaInst = null;
                    if (ptr instanceof IrAllocaInst)
                    {
                        irAllocaInst = (IrAllocaInst) irValue;
                    }
                    else
                    {
                        String value_alloca = irBasicBlock.getFunction().getAValue();
                        irAllocaInst = new IrAllocaInst(value_alloca, ptr.getValueType(), 1, true);
                    }
                    //修改
                    //IrStoreInst irStoreInst = new IrStoreInst(value_alloca, ptr.getValueType(), name1, true);
                    IrStoreInst irStoreInst = new IrStoreInst(irAllocaInst, ptr.getValueType(), ptr, true);
                    String value_load = irBasicBlock.getFunction().getAValue();
                    IrLoadInst irLoadInst = new IrLoadInst(value_load, ptr.getValueType(), irAllocaInst);
                    irinstructions.add(irAllocaInst);
                    irinstructions.add(irStoreInst);
                    irinstructions.add(irLoadInst);

                    if (symbol.getType() == Symbol.Type.Int || symbol.getType() == Symbol.Type.ConstInt || symbol.getType() == Symbol.Type.Char || symbol.getType() == Symbol.Type.ConstChar)
                    {
                        symbol.setIrValue(irAllocaInst);
                    }
                    else if (symbol.getType() == Symbol.Type.IntArray || symbol.getType() == Symbol.Type.ConstIntArray || symbol.getType() == Symbol.Type.CharArray || symbol.getType() == Symbol.Type.ConstCharArray)
                    {
                        symbol.setIrValue(irLoadInst);
                    }
                    //修改
                    //symbol.setIrValue(irAllocaInst);
                    //symbol.setIrValue(irLoadInst);

                    return irLoadInst;
                }

                // 全局/局部变量，需要从内存中读取
                String value = getAValue();
                IrValue irValue_ret = null;
                IrLoadInst irLoadInst = null;
                if (symbol.getType() == Symbol.Type.Int || symbol.getType() == Symbol.Type.ConstInt)
                {
                    if (ptr instanceof IrLoadInst)
                    {
                        irLoadInst = (IrLoadInst) ptr;
                    }
                    else
                    {
                        irLoadInst = new IrLoadInst(value, IrType.i32, ptr);
                        irinstructions.add(irLoadInst);
                    }
                    irValue_ret = irLoadInst;
                }
                else if (symbol.getType() == Symbol.Type.Char || symbol.getType() == Symbol.Type.ConstChar)
                {
                    if (ptr instanceof IrLoadInst)
                    {
                        irLoadInst = (IrLoadInst) ptr;
                    }
                    else
                    {
                        irLoadInst = new IrLoadInst(value, IrType.i8, ptr);
                        irinstructions.add(irLoadInst);
                    }
                    irValue_ret = irLoadInst;
                }
                else if (symbol.getType() == Symbol.Type.IntArray || symbol.getType() == Symbol.Type.ConstIntArray)
                {
                    //todo
                    //直接返回数组对象
                    IrGetelement irGetelement = new IrGetelement(value, IrType.n_i32, ptr, "0");
                    irinstructions.add(irGetelement);
                    irValue_ret = irGetelement;
                }
                else if (symbol.getType() == Symbol.Type.CharArray || symbol.getType() == Symbol.Type.ConstCharArray)
                {
                    //todo
                    //直接返回数组对象
                    IrGetelement irGetelement = new IrGetelement(value, IrType.n_i8, ptr, "0");
                    irinstructions.add(irGetelement);
                    irValue_ret = irGetelement;
                }
                return irValue_ret;
            }
        }
        else
        {
            Symbol symbol = symbolTable.getSymbol(name);
            if (isLeft)
            {
                //todo
                //数组变量在左边
                irValue = symbol.getIrValue();
                if (irValue.isParam())
                {
                    IrAllocaInst irAllocaInst = null;
                    if (irValue instanceof IrAllocaInst)
                    {
                        irAllocaInst = (IrAllocaInst) irValue;
                    }
                    else
                    {
                        IrValue left = null;
                        String value = null;
                        left = new IrValue(irValue.getValueType());
                        value = getAValue();
                        irAllocaInst = new IrAllocaInst(value, irValue.getValueType(), 1, true);
                        left.setName(name);
                        irinstructions.add(irAllocaInst);
                    }
                    IrStoreInst irStoreInst = new IrStoreInst(irAllocaInst, irValue.getValueType(), irValue, true);
                    irStoreInst.setName(name);
                    irinstructions.add(irStoreInst);
                    IrLoadInst irLoadInst = new IrLoadInst(getAValue(), irValue.getValueType(), irAllocaInst);
                    irinstructions.add(irLoadInst);
                    //irValue = irStoreInst;
                    irValue = irLoadInst;

                    //修改
                    symbol.setIrValue(irLoadInst);
                }
                IrGetelement irGetelement = null;
                IrLoadInst irLoadInst = null;
                IrValue offset = handleIRExp(exp, isLeft);
                if (offset.getValueType() != IrType.i32)
                {
                    IrZext irZext = new IrZext(offset.getValueType(), getAValue(), offset, IrType.i32);
                    irinstructions.add(irZext);
                    offset = irZext;
                }
                String value = getAValue();
                if (symbol.getType() == Symbol.Type.IntArray || symbol.getType() == Symbol.Type.ConstIntArray)
                {
                    irGetelement = new IrGetelement(value, IrType.n_i32, irValue, offset.getName());
                    //String value1 = getAValue();
                    //irLoadInst = new IrLoadInst(value1, IrType.i32, irGetelement, true);
                    //irValue = irLoadInst;
                    irValue = irGetelement;
                }
                else if (symbol.getType() == Symbol.Type.CharArray || symbol.getType() == Symbol.Type.ConstCharArray)
                {
                    irGetelement = new IrGetelement(value, IrType.n_i8, irValue, offset.getName());
                    //String value1 = getAValue();
                    //irLoadInst = new IrLoadInst(value1, IrType.i8, irGetelement, true);
                    //irValue = irLoadInst;
                    irValue = irGetelement;
                }

                irinstructions.add(irGetelement);
                //irinstructions.add(irLoadInst);
                return irValue;
            }
            else
            {
                IrValue ptr = symbol.getIrValue();
                String name1 = ptr.getName();
                if (ptr.isParam())
                {
                    IrAllocaInst irAllocaInst = null;
                    if (ptr instanceof IrAllocaInst)
                    {
                        irAllocaInst = (IrAllocaInst) irValue;
                    }
                    else
                    {
                        String value_alloca = irBasicBlock.getFunction().getAValue();
                        irAllocaInst = new IrAllocaInst(value_alloca, ptr.getValueType(), 0, true);
                    }
                    //修改
                    //IrStoreInst irStoreInst = new IrStoreInst(value_alloca, ptr.getValueType(), name1, true);
                    IrStoreInst irStoreInst = new IrStoreInst(irAllocaInst, ptr.getValueType(), ptr, true);
                    String value_load = irBasicBlock.getFunction().getAValue();
                    IrLoadInst irLoadInst = new IrLoadInst(value_load, ptr.getValueType(), irAllocaInst);
                    irinstructions.add(irAllocaInst);
                    irinstructions.add(irStoreInst);
                    irinstructions.add(irLoadInst);
                    //symbol.setIrValue(irLoadInst);
                    ptr = irLoadInst;

                    //修改
                    symbol.setIrValue(irLoadInst);
                }
                IrGetelement irGetelement = null;
                IrLoadInst irLoadInst = null;
                IrValue offset = handleIRExp(exp, isLeft);
                //todo
                //不知道全局数组变量是不是一样
                if (offset.getValueType() != IrType.i32)
                {
                    IrZext irZext = new IrZext(offset.getValueType(), getAValue(), offset, IrType.i32);
                    irinstructions.add(irZext);
                    offset = irZext;
                }
                String value = getAValue();
                if (symbol.getType() == Symbol.Type.IntArray || symbol.getType() == Symbol.Type.ConstIntArray)
                {
                    irGetelement = new IrGetelement(value, IrType.n_i32, ptr, offset.getName());
                    String value1 = getAValue();
                    irLoadInst = new IrLoadInst(value1, IrType.i32, irGetelement);
                }
                else if (symbol.getType() == Symbol.Type.CharArray || symbol.getType() == Symbol.Type.ConstCharArray)
                {
                    irGetelement = new IrGetelement(value, IrType.n_i8, ptr, offset.getName());
                    String value1 = getAValue();
                    irLoadInst = new IrLoadInst(value1, IrType.i8, irGetelement);
                }
                irinstructions.add(irGetelement);
                irinstructions.add(irLoadInst);
                return irLoadInst;
            }

        }
    }

    public IrValue handleIRExpPrim(ExpPrim expPrim, boolean isLeft)
    {
        Exp exp = expPrim.getExp();
        return handleIRExp(exp, isLeft);
    }

    public IrValue handleNumber(Number number)
    {
        String num = number.getIntconst().getContent();
        IrValue irValue = new IrValue(IrType.i32, num);
        return irValue;
    }

    public IrValue handleCharacter(Character character)
    {
        String cha = character.getCharconst().getContent();
        IrValue irValue = null;
        if (cha.length() == 3)
        {
            int c = cha.charAt(1);
            irValue = new IrValue(IrType.i8, String.valueOf(c));
        }
        else
        {
            cha = cha.substring(1, cha.length() - 1);
            int a = '\t';
            switch (cha)
            {
                case "\\t":
                    irValue = new IrValue(IrType.i8, "9");
                    break;
                case "\\a":
                    irValue = new IrValue(IrType.i8, "7");
                    break;
                case "\\b":
                    irValue = new IrValue(IrType.i8, "8");
                    break;
                case "\\n":
                    irValue = new IrValue(IrType.i8, "10");
                    break;
                case "\\v":
                    irValue = new IrValue(IrType.i8, "11");
                    break;
                case "\\f":
                    irValue = new IrValue(IrType.i8, "12");
                    break;
                case "\\\"":
                    irValue = new IrValue(IrType.i8, "34");
                    break;
                case "\\'":
                    irValue = new IrValue(IrType.i8, "39");
                    break;
                case "\\\\":
                    irValue = new IrValue(IrType.i8, "92");
                    break;
                case "\\0":
                    irValue = new IrValue(IrType.i8, "0");
                    break;
            }

        }
        return irValue;
    }

    public IrValue handleUnaryFunc(UnaryFunc unaryFunc, boolean isLeft)
    {
        IrCallInst irCallInst = null;
        String name = unaryFunc.getName().getContent();
        FuncRParams funcRParams = unaryFunc.getFuncRParams();
        ArrayList<IrValue> args = new ArrayList<>();
        Symbol symbol = symbolTable.getSymbol(name);
        IrFunction irFunction = (IrFunction) symbol.getIrValue();
        if (funcRParams.getExps().isEmpty())
        {
            if (symbol.getType() == Symbol.Type.IntFunc)
            {
                irCallInst = new IrCallInst(IrType.i32, irFunction, args);
            }
            else if (symbol.getType() == Symbol.Type.CharFunc)
            {
                irCallInst = new IrCallInst(IrType.i8, irFunction, args);
            }
            else if (symbol.getType() == Symbol.Type.VoidFunc)
            {
                irCallInst = new IrCallInst(IrType.Void, irFunction, args);
            }
        }
        else
        {
            SymbolFunc symbolFunc = (SymbolFunc) symbol;
            ArrayList<Symbol> symbols = symbolFunc.getSymbols();
            ArrayList<Exp> exps = funcRParams.getExps();
            IrValue arg = null;
            int i = 0;
            for (Exp exp : exps)
            {
                Symbol symbol1 = symbols.get(i);
                if (symbol1.getType() != Symbol.Type.Int && symbol1.getType() != Symbol.Type.Char)
                {
                    /* 对待参数中的数组部分 */
                    arg = handleIRExp(exp, true);
                    if (symbol1.getType() == Symbol.Type.Int && arg.getValueType() != IrType.i32)
                    {
                        String value = getAValue();
                        IrZext irZext = new IrZext(IrType.i8, value, arg, IrType.i32);
                        irinstructions.add(irZext);
                        arg = irZext;
                    }
                    else if (symbol1.getType() == Symbol.Type.Char && arg.getValueType() != IrType.i8)
                    {
                        String value = getAValue();
                        IrTrunc irTrunc = new IrTrunc(IrType.i32, value, arg, IrType.i8);
                        irinstructions.add(irTrunc);
                        arg = irTrunc;
                    }
                    args.add(arg);
                }
                else
                {
                    /* 对待参数中的非数组部分 */
                    arg = handleIRExp(exp, false);
                    if (symbol1.getType() == Symbol.Type.Int && arg.getValueType() != IrType.i32)
                    {
                        String value = getAValue();
                        IrZext irZext = new IrZext(IrType.i8, value, arg, IrType.i32);
                        irinstructions.add(irZext);
                        arg = irZext;
                    }
                    else if (symbol1.getType() == Symbol.Type.Char && arg.getValueType() != IrType.i8)
                    {
                        String value = getAValue();
                        IrTrunc irTrunc = new IrTrunc(IrType.i32, value, arg, IrType.i8);
                        irinstructions.add(irTrunc);
                        arg = irTrunc;
                    }
                    args.add(arg);
                    /*
                    IrValue left = null;
                    String value = null;
                    IrAllocaInst irAllocaInst = null;
                    if (symbol1.getType() == Symbol.Type.Int)
                    {
                        left = new IrValue(IrType.i32);
                        value = getAValue();
                        irAllocaInst = new IrAllocaInst(value, IrType.i32, 1);
                        left.setName(name);
                        irAllocaInst.setName(name);
                        irinstructions.add(irAllocaInst);
                        IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i32, arg.getName());
                        irStoreInst.setName(name);
                        irinstructions.add(irStoreInst);
                        args.add(irStoreInst);
                    }
                    else if (symbol1.getType() == Symbol.Type.Char)
                    {
                        left = new IrValue(IrType.i8);
                        value = getAValue();
                        irAllocaInst = new IrAllocaInst(value, IrType.i8, 1);
                        left.setName(name);
                        irAllocaInst.setName(name);
                        irinstructions.add(irAllocaInst);
                        IrStoreInst irStoreInst = new IrStoreInst(value, IrType.i8, arg.getName());
                        irStoreInst.setName(name);
                        irinstructions.add(irStoreInst);
                        args.add(irStoreInst);
                    }

                     */
                }
                i++;
            }
            if (symbol.getType() == Symbol.Type.IntFunc)
            {
                irCallInst = new IrCallInst(IrType.i32, irFunction, args);
            }
            else if (symbol.getType() == Symbol.Type.CharFunc)
            {
                irCallInst = new IrCallInst(IrType.i8, irFunction, args);
            }
            else if (symbol.getType() == Symbol.Type.VoidFunc)
            {
                irCallInst = new IrCallInst(IrType.Void, irFunction, args);
            }
        }
        if (symbol.getType() != Symbol.Type.VoidFunc)
        {
            String value = getAValue();
            irCallInst.setResult(value);
        }
        irinstructions.add(irCallInst);
        return irCallInst;
    }

    public IrValue handleUnaryOp(UnaryOp unaryOp, boolean isLeft)
    {
        UnaryExp unaryExp = unaryOp.getExp();
        Token op = unaryOp.getOp();
        IrValue irValue = handleIRUnaryExp(unaryExp, isLeft);
        if (op.getType() == Token.Type.PLUS)
        {

        }
        else if (op.getType() == Token.Type.MINU)
        {
            IrValue left = new IrValue(IrType.i32, "0");
            if (irValue.getValueType() != IrType.i32)
            {
                IrZext irZext = new IrZext(irValue.getValueType(), getAValue(), irValue, IrType.i32);
                irValue = irZext;
                irinstructions.add(irZext);
            }
            IrBinaryOperator irBinaryOperator = new IrBinaryOperator(null, IrBinaryOperator.Type.Sub, IrType.i32, left, irValue);
            String value = getAValue();
            irBinaryOperator.setResult(value);
            irValue = irBinaryOperator;
            irinstructions.add(irBinaryOperator);
        }
        else if (op.getType() == Token.Type.NOT)
        {
            IrValue value_0 = new IrValue(IrType.i32, "0");
            IrValue value_1 = new IrValue(IrType.i32, "1");
            if (irValue.getValueType() != IrType.i32)
            {
                IrZext irZext = new IrZext(irValue.getValueType(), getAValue(), irValue, IrType.i32);
                irValue = irZext;
                irinstructions.add(irZext);
            }
            IrCompareInst irCompareInst = new IrCompareInst(null, IrCompareInst.Type.Neq, IrType.i32, irValue, value_0);
            irCompareInst.setName(getAValue());
            irinstructions.add(irCompareInst);
            IrBinaryOperator irBinaryOperator = new IrBinaryOperator(getAValue(), IrBinaryOperator.Type.Not, IrType.i1, irCompareInst, value_1);
            IrZext irZext = new IrZext(IrType.i1, getAValue(), irBinaryOperator, IrType.i32);
            irValue = irZext;
            irinstructions.add(irBinaryOperator);
            irinstructions.add(irZext);
        }
        return irValue;
    }

    public void handleIRAssign(StmtAssign stmtAssign)
    {
        LVal lVal = stmtAssign.getlVal();
        Exp exp = stmtAssign.getExp();
        IrValue left = handleIRLVal(lVal, true);
        IrValue right = handleIRExp(exp, false);
        IrStoreInst irStoreInst = null;
        Symbol symbol = symbolTable.getSymbol(lVal.getIdent().getContent());
        if ((left.getValueType() == IrType.i8 || left.getValueType() == IrType.n_i8) && (right.getValueType() == IrType.i32 || right.getValueType() == IrType.n_i32))
        {
            String value = getAValue();
            IrTrunc irTrunc = new IrTrunc(IrType.i32, value, right, IrType.i8);
            irinstructions.add(irTrunc);
            right = irTrunc;
        }
        if ((left.getValueType() == IrType.i32 || left.getValueType() == IrType.n_i32) && (right.getValueType() == IrType.i8 || right.getValueType() == IrType.n_i8))
        {
            String value = getAValue();
            IrZext irZext = new IrZext(IrType.i8, value, right, IrType.i32);
            irinstructions.add(irZext);
            right = irZext;
        }
        if (lVal.getExp() == null)
        {
            if (lVal.getValuetype().equals("int"))
            {
                irStoreInst = new IrStoreInst(left, IrType.i32, right);
            }
            else
            {
                irStoreInst = new IrStoreInst(left, IrType.i8, right);
            }
            symbol.setIrValue(irStoreInst);
        }
        else
        {
            Exp exp1 = lVal.getExp();
            IrValue offset = handleIRExp(exp1, false);
            //String value = getAValue();
            IrGetelement irGetelement = null;
            if (lVal.getValuetype().equals("int"))
            {
                irStoreInst = new IrStoreInst(left, IrType.i32, right);
                /*
                irGetelement = new IrGetelement(value, IrType.i32, left.getName(), offset.getName());
                irStoreInst = new IrStoreInst(right.getName(), IrType.i32, value);

                 */
            }
            else
            {
                irStoreInst = new IrStoreInst(left, IrType.i8, right);
                /*
                irGetelement = new IrGetelement(value, IrType.i8, left.getName(), offset.getName());
                irStoreInst = new IrStoreInst(right.getName(), IrType.i8, value);

                 */
            }
            //irinstructions.add(irGetelement);
        }
        irinstructions.add(irStoreInst);
    }

    public void handleIRBreak(StmtBreak stmtBreak)
    {
        ArrayList<IrBasicBlock> basicBlocks_for = irBasicBlock.getBasicBlocks_for();
        IrBranchInst irBranchInst = new IrBranchInst(IrType.i32, basicBlocks_for.get(basicBlocks_for.size() - 1));
        irinstructions.add(irBranchInst);
    }

    public void handleIRContinue(StmtContinue stmtContinue)
    {
        ArrayList<IrBasicBlock> basicBlocks_for = irBasicBlock.getBasicBlocks_for();
        IrBranchInst irBranchInst = new IrBranchInst(IrType.i32, basicBlocks_for.get(basicBlocks_for.size() - 3));
        irinstructions.add(irBranchInst);
    }

    public void handleIRReturn(StmtReturn stmtReturn)
    {
        Exp exp = stmtReturn.getExp();
        IrValue irValue = new IrValue(IrType.Void);
        if (exp != null)
        {
            irValue = handleIRExp(exp, false);
        }
        String value = getAValue();
        if (irValue.getValueType() != irBasicBlock.getFunction().getValueType())
        {
            if (irValue.getValueType() == IrType.i8)
            {
                IrZext irZext = new IrZext(IrType.i8, value, irValue, IrType.i32);
                irinstructions.add(irZext);
                irValue = irZext;
            }
            else if (irValue.getValueType() == IrType.i32)
            {
                IrTrunc irTrunc = new IrTrunc(IrType.i32, value, irValue, IrType.i8);
                irinstructions.add(irTrunc);
                irValue = irTrunc;
            }
        }
        IrReturnInst irReturnInst = new IrReturnInst(irValue);
        irinstructions.add(irReturnInst);
    }

    public void handleIRGet(StmtGet stmtGet)
    {
        LVal lVal = stmtGet.getlVal();
        Token gettk = stmtGet.getGettk();
        IrValue left = handleIRLVal(lVal, true);
        String value = getAValue();
        IrCallInst irCallInst = null;
        if (gettk.getType() == Token.Type.GETINTTK)
        {
            irCallInst = new IrCallInst("getint");
        }
        else if (gettk.getType() == Token.Type.GETCHARTK)
        {
            irCallInst = new IrCallInst("getchar");
        }
        irCallInst.setResult(value);
        irinstructions.add(irCallInst);
        IrStoreInst irStoreInst = null;
        IrValue value_store = irCallInst;
        if (left.getValueType() != IrType.i32 && left.getValueType() != IrType.n_i32)
        {
            String value_trunc = getAValue();
            IrTrunc irTrunc = new IrTrunc(IrType.i32, value_trunc, irCallInst, IrType.i8);
            irinstructions.add(irTrunc);
            value_store = irTrunc;
        }
        if (left.getValueType() == IrType.i32 || left.getValueType() == IrType.i8)
        {
            //
            irStoreInst = new IrStoreInst(left, left.getValueType(), value_store);
        }
        else
        {
            Exp exp = lVal.getExp();
            IrValue offset = handleIRExp(exp, false);
            if (offset.getValueType() != IrType.i32)
            {
                IrZext irZext = new IrZext(offset.getValueType(), getAValue(), offset, IrType.i32);
                irinstructions.add(irZext);
                offset = irZext;
            }
            //String result = getAValue();
            //修改
            //IrGetelement irGetelement = new IrGetelement(result, left.getValueType(), left, offset.getName());
            //IrGetelement irGetelement = new IrGetelement(result, left.getValueType(), lVal.getIdent().getContent(), offset.getName());
            //irinstructions.add(irGetelement);
            if (left.getValueType() == IrType.n_i8)
            {
                irStoreInst = new IrStoreInst(left, IrType.i8, value_store);
            }
            else
            {
                irStoreInst = new IrStoreInst(left, IrType.i32, value_store);
            }

        }
        irinstructions.add(irStoreInst);
    }

    public void handleIRPrintf(StmtPrintf stmtPrintf)
    {
        Token stringconst = stmtPrintf.getStringconst();
        ArrayList<Exp> exps = stmtPrintf.getExps();
        String string = stringconst.getContent();
        char[] chars = string.substring(1, string.length() - 1).toCharArray();
        int cnt = 0;

        ArrayList<IrValue> values = new ArrayList<>();
        /* 首先将exps处理出来 */
        for (int i = 0; i < exps.size(); i++)
        {
            Exp exp = exps.get(i);
            IrValue irexp = handleIRExp(exp, false);
            values.add(irexp);
        }
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            IrCallInst irCallInst = null;
            if (c == '%' && (chars[i + 1] == 'd' || chars[i + 1] == 'c'))
            {
                if (chars[i + 1] == 'd')
                {
                    IrValue value = values.get(cnt);
                    if (value.getValueType() != IrType.i32)
                    {
                        String value1 = getAValue();
                        IrZext irZext = new IrZext(IrType.i8, value1, value, IrType.i32);
                        irinstructions.add(irZext);
                        value = irZext;
                    }
                    irCallInst = new IrCallInst("putint", value);
                    cnt++;
                    i++;
                }
                else if (chars[i + 1] == 'c')
                {
                    IrValue value = values.get(cnt);
                    if (value.getValueType() != IrType.i32)
                    {
                        String value1 = getAValue();
                        IrZext irZext = new IrZext(IrType.i8, value1, value, IrType.i32);
                        irinstructions.add(irZext);
                        value = irZext;
                    }
                    irCallInst = new IrCallInst("putch", value);
                    cnt++;
                    i++;
                }
            }
            else if (c == '\\' && chars[i + 1] == 'n')
            {
                if (!IrString.getHas_n())
                {
                    IrString irString = new IrString("\\0A");
                    irBasicBlock.getFunction().getIrModule().addIrGlobalVar(irString);
                    irCallInst = new IrCallInst("putstr", irString.getParam());
                    IrString.setIrString(irString);
                }
                else
                {
                    IrString irString = IrString.getIrString();
                    irCallInst = new IrCallInst("putstr", irString.getParam());
                }
                i += 1;
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                while (!(c == '%' && (chars[i + 1] == 'd' || chars[i + 1] == 'c')))
                {
                    if (c == '\\' && chars[i + 1] == 'n')
                    {
                        sb.append("\\0A");
                        i++;
                    }
                    else if (c == '\\' && chars[i + 1] == '\\')
                    {
                        sb.append("\\\\");
                        i++;
                    }
                    else if (c == '\\' && chars[i + 1] == 't')
                    {
                        sb.append("\\09");  // "\t" 对应 ASCII 9
                        i++;
                    }
                    // 处理 "\a"
                    else if (c == '\\' && chars[i + 1] == 'a')
                    {
                        sb.append("\\07");  // "\a" 对应 ASCII 7
                        i++;
                    }
                    // 处理 "\b"
                    else if (c == '\\' && chars[i + 1] == 'b')
                    {
                        sb.append("\\08");  // "\b" 对应 ASCII 8
                        i++;
                    }
                    // 处理 "\v"
                    else if (c == '\\' && chars[i + 1] == 'v')
                    {
                        sb.append("\\11");  // "\v" 对应 ASCII 11
                        i++;
                    }
                    // 处理 "\f"
                    else if (c == '\\' && chars[i + 1] == 'f')
                    {
                        sb.append("\\12");  // "\f" 对应 ASCII 12
                        i++;
                    }
                    // 处理 "\""
                    else if (c == '\\' && chars[i + 1] == '"')
                    {
                        sb.append("\\34");  // "\"" 对应 ASCII 34
                        i++;
                    }
                    // 处理 "\'"
                    else if (c == '\\' && chars[i + 1] == '\'')
                    {
                        sb.append("\\39");  // "\'" 对应 ASCII 39
                        i++;
                    }
                    // 处理 "\0"
                    else if (c == '\\' && chars[i + 1] == '0')
                    {
                        sb.append("\\00");  // "\0" 对应 ASCII 0
                        i++;
                    }
                    else
                    {
                        sb.append(c);
                    }
                    i++;
                    if (i >= chars.length)
                    {
                        break;
                    }
                    c = chars[i];
                }
                i--;
                String s = sb.toString();
                //s.replaceAll("\n", "\0A");
                IrString irString = new IrString(s);
                irBasicBlock.getFunction().getIrModule().addIrGlobalVar(irString);
                irCallInst = new IrCallInst("putstr", irString.getParam());
            }
            irinstructions.add(irCallInst);
        }
    }

    public void handleIRStmtExp(StmtExp stmtExp)
    {
        Exp exp = stmtExp.getExp();
        handleIRExp(exp, false);
    }

    public void handleCondExp(ArrayList<IrBasicBlock> basicBlocks, boolean isIf)
    {
        LOrExp lOrExp = condExp.getExp();
        ArrayList<LAndExp> lAndExps = lOrExp.getExps();
        ArrayList<Token> ops = lOrExp.getOps();
        int num = 0;
        if (!isIf)
        {
            num = 1;
        }
        handleIRLAndExp(basicBlocks, lAndExps.get(0), num, isIf);
        if (!ops.isEmpty())
        {
            for (int i = 0; i < ops.size(); i++)
            {
                num += lAndExps.get(i).getExps().size();
                handleIRLAndExp(basicBlocks, lAndExps.get(i + 1), num, isIf);
            }
        }
    }

    public void handleIRLAndExp(ArrayList<IrBasicBlock> basicBlocks, LAndExp lAndExp, int num, boolean isIf)
    {
        ArrayList<EqExp> eqExps = lAndExp.getExps();
        ArrayList<Token> ops = lAndExp.getOps();

        /*
        //进行基本块的相关操作
        IrBasicBlock basicBlock = basicBlocks.get(num);
        IrBlockInfo irBlockInfo = basicBlock.getIrBlockInfo();
        basicBlock.addIrInstruction(irBlockInfo);
        setIrBasicBlock(basicBlock);
        basicBlock.setName(getAValue());
         */


        IrValue first = null;
        if (ops.isEmpty())
        {
            first = handleIREqExp(basicBlocks, eqExps.get(0), num);
            handleIRBranch(basicBlocks, first, num, true, eqExps.size(), isIf);
        }
        else
        {
            first = handleIREqExp(basicBlocks, eqExps.get(0), num);
            handleIRBranch(basicBlocks, first, num, false, eqExps.size(), isIf);
        }
        IrValue value_0 = new IrValue(IrType.i32, "0");
        //IrCompareInst irCompareInst = new IrCompareInst(getAValue(), IrCompareInst.Type.Neq, IrType.i32, first, value_0);
        /*
        //todo
        //跳转语句
         */

        if (!ops.isEmpty())
        {
            int num_new = num;
            for (int i = 0; i < ops.size(); i++)
            {
                num_new++;
                if (i + 1 == ops.size())
                {
                    IrValue exp = handleIREqExp(basicBlocks, eqExps.get(i + 1), num_new);
                    handleIRBranch(basicBlocks, exp, num_new, true, eqExps.size() - i, isIf);
                }
                else
                {
                    IrValue exp = handleIREqExp(basicBlocks, eqExps.get(i + 1), num_new);
                    handleIRBranch(basicBlocks, exp, num_new, false, eqExps.size() - i, isIf);
                }
            }
        }
    }

    public IrValue handleIREqExp(ArrayList<IrBasicBlock> basicBlocks, EqExp eqExp, int num)
    {
        ArrayList<RelExp> relExps = eqExp.getExps();
        ArrayList<Token> ops = eqExp.getOps();

        //进行基本块的相关操作
        IrBasicBlock basicBlock = basicBlocks.get(num);
        setIrBasicBlock(basicBlock);


        if (num != 0)
        {
            IrBlockInfo irBlockInfo = basicBlock.getIrBlockInfo();
            basicBlock.addIrInstruction(irBlockInfo);
            basicBlock.setName(getAValue());
        }

        //todo
        IrValue first = handleRelExp(relExps.get(0), basicBlock);

        if (ops.isEmpty())
        {
            return first;
        }
        else
        {
            IrValue exp1 = first;
            IrValue exp2 = null;
            for (int i = 0; i < ops.size(); i++)
            {
                exp2 = handleRelExp(relExps.get(i + 1), basicBlock);
                if (exp1.getValueType() != IrType.i32)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(exp1.getValueType(), value, exp1, IrType.i32);
                    basicBlock.addIrInstruction(irZext);
                    exp1 = irZext;
                }
                if (exp2.getValueType() != IrType.i32)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(exp2.getValueType(), value, exp2, IrType.i32);
                    basicBlock.addIrInstruction(irZext);
                    exp2 = irZext;
                }
                IrCompareInst irCompareInst1 = null;
                if (ops.get(i).getType() == Token.Type.EQL)
                {
                    irCompareInst1 = new IrCompareInst(getAValue(), IrCompareInst.Type.Eq, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.NEQ)
                {
                    irCompareInst1 = new IrCompareInst(getAValue(), IrCompareInst.Type.Neq, IrType.i32, exp1, exp2);
                }
                basicBlock.addIrInstruction(irCompareInst1);
                exp1 = irCompareInst1;
                first = irCompareInst1;
            }
            return first;
        }
    }

    public void handleIRBranch(ArrayList<IrBasicBlock> basicBlocks, IrValue exp, int num, boolean isOr, int nextLor, boolean isIf)
    {
        //todo
        //for语句的

        IrBranchInst irBranchInst = null;
        IrBasicBlock block_if = null;
        IrBasicBlock block_else = null;
        if (isIf)
        {
            if (basicBlocks.get(basicBlocks.size() - 2).getName().equals("else"))
            {
                block_if = basicBlocks.get(basicBlocks.size() - 3);
                block_else = basicBlocks.get(basicBlocks.size() - 2);
            }
            else
            {
                block_if = basicBlocks.get(basicBlocks.size() - 2);
                block_else = basicBlocks.get(basicBlocks.size() - 1);
            }
        }
        else
        {
            //num = num + 1;
            block_if = basicBlocks.get(basicBlocks.size() - 2);
            block_else = basicBlocks.get(basicBlocks.size() - 1);
        }

        IrBasicBlock basicBlock = basicBlocks.get(num);

        String str = exp.getName();
        boolean isNumber = false;
        if (str.matches("-?\\d+(\\.\\d+)?"))
        {
            isNumber = true;
        }

        if (exp.getName().equals("0") && isOr)
        {
            if (basicBlocks.get(num + 1).getName().equals("forstmt2") || basicBlocks.get(num + nextLor).getName().equals("if"))
            {
                irBranchInst = new IrBranchInst(IrType.i32, block_else);
            }
            else
            {
                irBranchInst = new IrBranchInst(IrType.i32, basicBlocks.get(num + 1));
            }
            basicBlocks.get(num + 1).addPreds(basicBlock);
        }
        else if (!exp.getName().equals("0") && isNumber && !isOr)
        {
            irBranchInst = new IrBranchInst(IrType.i32, basicBlocks.get(num + 1));
            basicBlocks.get(num + 1).addPreds(basicBlock);
        }
        else if (exp.getName().equals("0") && !isOr)
        {
            if (basicBlocks.get(num + nextLor).getName().equals("if") || basicBlocks.get(num + nextLor).getName().equals("forstmt2"))
            {
                irBranchInst = new IrBranchInst(IrType.i32, block_else);
            }
            else
            {
                irBranchInst = new IrBranchInst(IrType.i32, basicBlocks.get(num + nextLor));
            }
            block_else.addPreds(basicBlock);
        }
        else if (!exp.getName().equals("0") && isNumber && isOr)
        {
            irBranchInst = new IrBranchInst(IrType.i32, block_if);
            block_if.addPreds(basicBlock);
        }
        else
        {
            IrValue value_0 = new IrValue(IrType.i32, "0");
            IrCompareInst irCompareInst = null;
            if (exp instanceof IrCompareInst)
            {
                irCompareInst = (IrCompareInst) exp;
            }
            else
            {
                if (exp.getValueType() != IrType.i32)
                {
                    IrZext irZext = new IrZext(exp.getValueType(), getAValue(), exp, IrType.i32);
                    exp = irZext;
                    basicBlock.addIrInstruction(irZext);
                }
                irCompareInst = new IrCompareInst(getAValue(), IrCompareInst.Type.Neq, IrType.i32, exp, value_0);
                basicBlock.addIrInstruction(irCompareInst);
            }
            if (isOr)
            {
                //是0跳下一个or,不是跳if-true
                if (basicBlocks.get(num + 1).getName().equals("if") || basicBlocks.get(num + 1).getName().equals("forstmt2"))
                {
                    irBranchInst = new IrBranchInst(IrType.i1, irCompareInst, block_if, block_else);
                    block_if.addPreds(basicBlock);
                    block_else.addPreds(basicBlock);
                }
                else
                {
                    irBranchInst = new IrBranchInst(IrType.i1, irCompareInst, block_if, basicBlocks.get(num + 1));
                    block_if.addPreds(basicBlock);
                    basicBlocks.get(num + 1).addPreds(basicBlock);
                }
            }
            else
            {
                //是0跳下一个or,不是跳下一个and
                if (basicBlocks.get(num + nextLor).getName().equals("if") || basicBlocks.get(num + nextLor).getName().equals("forstmt2"))
                {
                    irBranchInst = new IrBranchInst(IrType.i1, irCompareInst, basicBlocks.get(num + 1), block_else);
                }
                else
                {
                    irBranchInst = new IrBranchInst(IrType.i1, irCompareInst, basicBlocks.get(num + 1), basicBlocks.get(num + nextLor));
                }


                basicBlocks.get(num + 1).addPreds(basicBlock);
                basicBlocks.get(num + nextLor).addPreds(basicBlock);
            }
        }


        basicBlock.addIrInstruction(irBranchInst);
    }

    public IrValue handleRelExp(RelExp relExp, IrBasicBlock basicBlock)
    {
        ArrayList<AddExp> addExps = relExp.getExps();
        ArrayList<Token> ops = relExp.getOps();
        IrInstruction irInstruction = new IrInstruction(basicBlock, symbolTable, addExps.get(0));
        IrValue first = irInstruction.buildIRAddExp();
        basicBlock.addAllIrInstruction(irInstruction.getIrinstructions());
        if (ops.isEmpty())
        {
            return first;
        }
        else
        {
            IrValue exp1 = first;
            IrValue exp2 = null;
            for (int i = 0; i < ops.size(); i++)
            {
                irInstruction = new IrInstruction(basicBlock, symbolTable, addExps.get(i + 1));
                exp2 = irInstruction.buildIRAddExp();
                basicBlock.addAllIrInstruction(irInstruction.getIrinstructions());
                if (exp1.getValueType() != IrType.i32)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(exp1.getValueType(), value, exp1, IrType.i32);
                    basicBlock.addIrInstruction(irZext);
                    exp1 = irZext;
                }
                if (exp2.getValueType() != IrType.i32)
                {
                    String value = getAValue();
                    IrZext irZext = new IrZext(exp2.getValueType(), value, exp2, IrType.i32);
                    basicBlock.addIrInstruction(irZext);
                    exp2 = irZext;
                }
                IrCompareInst irCompareInst1 = null;
                if (ops.get(i).getType() == Token.Type.GRE)
                {
                    irCompareInst1 = new IrCompareInst(getAValue(), IrCompareInst.Type.Gt, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.GEQ)
                {
                    irCompareInst1 = new IrCompareInst(getAValue(), IrCompareInst.Type.Ge, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.LSS)
                {
                    irCompareInst1 = new IrCompareInst(getAValue(), IrCompareInst.Type.Lt, IrType.i32, exp1, exp2);
                }
                else if (ops.get(i).getType() == Token.Type.LEQ)
                {
                    irCompareInst1 = new IrCompareInst(getAValue(), IrCompareInst.Type.Le, IrType.i32, exp1, exp2);
                }
                basicBlock.addIrInstruction(irCompareInst1);
                exp1 = irCompareInst1;
                first = irCompareInst1;
            }
            return first;
        }
    }

    public IrValue buildIRAddExp()
    {
        return handleIRAddExp(addExp, false);
    }

    public String getAValue()
    {
        if (irBasicBlock.getFunction().isMainFunc())
        {
            return IrArgument.getAValue();
        }
        else
        {
            return irBasicBlock.getFunction().getAValue();
        }
    }

    public void setIrBasicBlock(IrBasicBlock irBasicBlock)
    {
        this.irBasicBlock = irBasicBlock;
    }

    public ArrayList<IrInstruction> getIrinstructions()
    {
        return irinstructions;
    }

    @Override
    public ArrayList<String> irOutput()
    {
        return null;
    }
}
