package middle.llvmir.Value.GlobalValue;

import frontend.Parser.declaration.Btype;
import frontend.Parser.function.FuncDef;
import frontend.Parser.function.FuncFParam;
import frontend.Parser.function.MainFuncDef;
import frontend.Parser.statement.block.BlockStmt;
import frontend.Parser.statement.block.StmtBlock;
import frontend.lexer.Token;
import middle.llvmir.Value.IrBasicBlock;
import middle.llvmir.Value.IrInstruction.*;
import middle.llvmir.Value.IrModule;
import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;
import middle.semantic.Symbol;
import middle.semantic.SymbolFunc;
import middle.semantic.SymbolTable;
import middle.semantic.SymbolVar;

import java.util.ArrayList;
import java.util.Iterator;

public class IrFunction extends IrValue
{
    private String name;
    private IrType type;
    private ArrayList<IrParam> params;
    private ArrayList<IrBasicBlock> basicBlocks;
    private FuncDef funcDef;
    private MainFuncDef mainFuncDef;
    private IrModule irModule;//父Module
    private SymbolTable symbolTable;
    private SymbolFunc symbolFunc;

    private int count = -1;

    public IrFunction(IrType type, String name)
    {
        super(type);
        this.type = type;
        this.name = name;
    }

    public IrFunction(IrType type, SymbolTable symbolTable, FuncDef funcDef, IrModule module)
    {
        super(type);
        this.type = type;
        this.symbolTable = symbolTable;
        this.funcDef = funcDef;
        this.irModule = module;
        this.basicBlocks = new ArrayList<>();
    }

    public IrFunction(IrType type, SymbolTable symbolTable, MainFuncDef mainFuncDef, IrModule module)
    {
        super(type);
        this.type = type;
        this.symbolTable = symbolTable;
        this.mainFuncDef = mainFuncDef;
        this.irModule = module;
        this.basicBlocks = new ArrayList<>();
    }

    public IrFunction(IrType type, String name, ArrayList<IrParam> params, ArrayList<IrBasicBlock> basicBlocks, IrModule irModule, FuncDef funcDef)
    {
        super(type);
        this.type = type;
        this.name = name;
        this.params = params;
        if (basicBlocks != null)
        {
            this.basicBlocks = basicBlocks;
        }
        else
        {
            this.basicBlocks = new ArrayList<>();
        }
        this.irModule = irModule;
        this.funcDef = funcDef;
    }

    public IrFunction(IrType type, String name, ArrayList<IrParam> params, ArrayList<IrBasicBlock> basicBlocks, IrModule irModule, MainFuncDef mainFuncDef)
    {
        super(type);
        this.type = type;
        this.name = name;
        this.params = params;
        this.basicBlocks = basicBlocks;
        this.irModule = irModule;
        this.mainFuncDef = mainFuncDef;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setParams(ArrayList<IrParam> params)
    {
        this.params = params;
        for (int i = 0; i < params.size(); i++)
        {
            params.get(i).setName("%" + i);
        }
    }

    public ArrayList<IrParam> getParams()
    {
        return params;
    }

    public void addIrBasicBlock(IrBasicBlock block)
    {
        this.basicBlocks.add(block);
    }


    public void addAllIrBasicBlock(ArrayList<IrBasicBlock> basicBlocks)
    {
        for (IrBasicBlock basicBlock : basicBlocks)
        {
            addIrBasicBlock(basicBlock);
        }
    }

    public String getName()
    {
        return name;
    }

    public IrFunction buildIrFunction()
    {
        if (mainFuncDef != null)
        {
            addMainFuncSymbol(mainFuncDef);
            return handleMainFunc(mainFuncDef);
        }
        else
        {
            addFuncSymbol(funcDef);
            return handleFunc(funcDef);
        }
    }

    public IrFunction handleMainFunc(MainFuncDef mainFuncDef)
    {
        IrFunction irFunction = new IrFunction(IrType.i32, symbolTable, mainFuncDef, irModule);
        irFunction.setName("main");
        /* 将函数名加入符号表 */
        symbolFunc.setIrValue(irFunction);
        /* 解析Block */
        StmtBlock stmtBlock = mainFuncDef.getStmtBlock();
        IrBasicBlock irBasicBlock = new IrBasicBlock(symbolTable, stmtBlock);
        irBasicBlock.setFunction(irFunction);
        irFunction.addAllIrBasicBlock(irBasicBlock.buildIrBasicBlock());
        return irFunction;
    }

    public IrFunction handleFunc(FuncDef funcDef)
    {
        IrType type; // IrFunctionType的属性
        if (funcDef.getFuncType().getFtype().getContent().equals("int"))
        {
            /* 返回值为int */
            type = IrType.i32;
        }
        else if (funcDef.getFuncType().getFtype().getContent().equals("char"))
        {
            /* 返回值为void */
            type = IrType.i8;
        }
        else
        {
            type = IrType.Void;
        }

        ArrayList<IrParam> params = new ArrayList<>(); // IrFunctionType的属性
        ArrayList<FuncFParam> funcFParams = funcDef.getFuncFParams();
        if (funcFParams != null)
        {
            for (FuncFParam funcFParam : funcFParams)
            {
                IrParam param = getParam(funcFParam);
                params.add(param);
                addParamSymbol(funcFParam, param);
            }
        }

        IrFunction irFunction = new IrFunction(type, funcDef.getIdent().getContent(), params, null, irModule, funcDef);
        irFunction.addParamCount(params.size() + 1);
        /* 将函数名加入符号表 */
        symbolFunc.setIrValue(irFunction);

        //参数alloca
        for (int i = 0; i < params.size(); i++)
        {
            IrParam param = params.get(i);
        }

        /* 解析Block */
        StmtBlock stmtBlock = funcDef.getStmtBlock();
        IrBasicBlock irBasicBlock = new IrBasicBlock(symbolTable, stmtBlock);
        irBasicBlock.setFunction(irFunction);
        irFunction.addAllIrBasicBlock(irBasicBlock.buildIrBasicBlock());
        return irFunction;
    }

    private IrParam getParam(FuncFParam funcFParam)
    {
        Btype btype = funcFParam.getBtype();
        Token ident = funcFParam.getIdent();
        IrParam irParam = null;
        if (btype.getToken().getType() == Token.Type.INTTK)
        {
            irParam = new IrParam(ident.getContent(), IrType.i32);
        }
        else
        {
            irParam = new IrParam(ident.getContent(), IrType.i8);
        }
        return irParam;
    }

    private void addParamSymbol(FuncFParam funcFParam, IrParam irParam)
    {
        // 用于生成该变量在LLVM IR中的名字
        String value = getAValue();
        if (!funcFParam.isArray())
        {
            value = value.replaceAll("func", "param");
        }
        {
            value = value.replaceAll("func", "array_param");
        }
        // 获取当前参数的维度
        if (!funcFParam.isArray())
        {
            IrValue irvalue = null;
            SymbolVar symbolVar = null;
            if (funcFParam.getBtype().getToken().getType() == Token.Type.INTTK)
            {
                irvalue = new IrValue(IrType.i32, value, true);
                symbolVar = new SymbolVar(funcFParam.getIdent().getLine(), symbolTable.getLevel(), funcFParam.getIdent().getContent(), Symbol.Type.Int);
            }
            else
            {
                irvalue = new IrValue(IrType.i8, value, true);
                symbolVar = new SymbolVar(funcFParam.getIdent().getLine(), symbolTable.getLevel(), funcFParam.getIdent().getContent(), Symbol.Type.Char);
            }
            symbolVar.setIrValue(irvalue);
            irParam.setIrValue(irvalue);
            symbolTable.addSymbol(symbolVar);
            // 也要将形参符号加入函数符号中，以便函数调用
            symbolFunc.addSymbol(symbolVar);
        }
        else
        {
            IrValue irvalue = null;
            SymbolVar symbolVar = null;
            if (funcFParam.getBtype().getToken().getType() == Token.Type.INTTK)
            {
                irvalue = new IrValue(IrType.n_i32, value, true);
                symbolVar = new SymbolVar(funcFParam.getIdent().getLine(), symbolTable.getLevel(), funcFParam.getIdent().getContent(), Symbol.Type.IntArray);
            }
            else
            {
                irvalue = new IrValue(IrType.n_i8, value, true);
                symbolVar = new SymbolVar(funcFParam.getIdent().getLine(), symbolTable.getLevel(), funcFParam.getIdent().getContent(), Symbol.Type.CharArray);
            }
            symbolVar.setIrValue(irvalue);
            irParam.setIrValue(irvalue);
            symbolTable.addSymbol(symbolVar);
            // 也要将形参符号加入函数符号中，以便函数调用
            symbolFunc.addSymbol(symbolVar);
        }
    }

    private void addFuncSymbol(FuncDef funcDef)
    {
        SymbolFunc symbolFunc = null;
        if (funcDef.getFuncType().getFtype().getType() == Token.Type.INTTK)
        {
            symbolFunc = new SymbolFunc(0, symbolTable.getLevel(), funcDef.getIdent().getContent(), Symbol.Type.IntFunc);
        }
        else if (funcDef.getFuncType().getFtype().getType() == Token.Type.CHARTK)
        {
            symbolFunc = new SymbolFunc(0, symbolTable.getLevel(), funcDef.getIdent().getContent(), Symbol.Type.CharFunc);
        }
        else
        {
            symbolFunc = new SymbolFunc(0, symbolTable.getLevel(), funcDef.getIdent().getContent(), Symbol.Type.VoidFunc);
        }
        symbolTable.getParent().addSymbol(symbolFunc);
        this.symbolFunc = symbolFunc;
    }

    private void addMainFuncSymbol(MainFuncDef mainFuncDef)
    {
        SymbolFunc symbolFunc = new SymbolFunc(0, 0, "main", Symbol.Type.IntFunc);
        symbolTable.getParent().addSymbol(symbolFunc);
        this.symbolFunc = symbolFunc;
    }

    public void setSlot()
    {
        int num = 0;
        if (params != null && !params.isEmpty())
        {
            for (int i = 0; i < params.size(); i++)
            {
                params.get(i).setName("%" + num);
                num++;
            }
        }
        num++;
        for (int i = 0; i < basicBlocks.size(); i++)
        {
            IrBasicBlock basicBlock = basicBlocks.get(i);
            if (basicBlock.getNeedName())
            {
                basicBlock.setName("%" + num);
                num++;
            }
            ArrayList<IrInstruction> irInstructions = basicBlock.getInstructions();
            for (int j = 0; j < irInstructions.size(); j++)
            {
                IrInstruction instruction = irInstructions.get(j);
                if (!(instruction instanceof IrStoreInst) && !(instruction instanceof IrBranchInst) && !(instruction instanceof IrReturnInst))
                {
                    if (instruction instanceof IrCallInst)
                    {
                        IrType type1 = ((IrCallInst) instruction).getType();
                        if (type1 == IrType.Void)
                        {
                            continue;
                        }
                    }
                    instruction.setName("%" + num);
                    num++;
                }
            }
        }
    }

    @Override
    public ArrayList<String> irOutput()
    {
        /* 重排序号 */
        //setSlot();
        /* 函数签名 */
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ");
        if (type == IrType.i32)
        {
            sb.append("i32");
        }
        else if (type == IrType.i8)
        {
            sb.append("i8");
        }
        else if (type == IrType.Void)
        {
            sb.append("void");
        }
        sb.append(" ");
        sb.append("@" + name);
        sb.append("(");
        /* 形参 */
        ArrayList<String> allParam = new ArrayList<>();
        if (params != null)
        {
            for (IrParam param : params)
            {
                // 每个形参返回的字符串列表应该只有一个字符串
                String temp = param.irOutput();
                sb.append(temp + ", ");
                allParam.add(temp + ",");
            }
            /* 去掉最后可能多余的", " */
            int len = sb.length();
            if (sb.charAt(len - 2) == ',' && sb.charAt(len - 1) == ' ')
            {
                // 由于至少有define等元素在，因此不用考虑index out of的问题
                sb.replace(len - 2, len - 1, "");
            }
        }
        //sb.append(") #0 {\n");
        sb.append(") {\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        /* 基本块 */
        String string = "";
        for (IrBasicBlock basicBlock : basicBlocks)
        {
            ArrayList<String> temp = basicBlock.irOutput();
            if (temp != null && temp.size() != 0)
            {
                ret.addAll(temp);
            }
            if (temp != null && !temp.isEmpty())
            {
                string = temp.get(temp.size() - 1);
            }
        }
        //System.out.println(string);
        if (type == IrType.Void && !string.equals("ret void"))
        {
            ret.add("ret void\n");
        }
        /* 函数结尾大括号 */
        ret.add("}\n");


        //重新排序
        ArrayList<String> temp = new ArrayList<>();

        for (String s : ret)
        {
            if (s.contains("dso_local"))
            {
                temp.add(s);
            }
        }

        for (String s : ret)
        {
            if (s.contains("alloca") && !(s.contains("dso_local")))
            {
                temp.add(s);
            }
        }

        Iterator<String> iterator = ret.iterator();
        while (iterator.hasNext())
        {
            String s = iterator.next();
            for (String param : allParam)
            {
                if (s.contains("store") && s.contains(param))
                {
                    temp.add(s);
                    iterator.remove();  // 使用 Iterator 删除元素
                    break;
                }
            }
        }


        for (String s : ret)
        {
            if (s.contains("load") && (s.contains("array_param")))
            {
                temp.add(s);
            }
        }

        for (String s : ret)
        {
            if (!s.contains("alloca") && !(s.contains("dso_local")) && !(s.contains("load") && s.contains("array_param")))
            {
                temp.add(s);
            }
        }


        /*
        for (String s : ret)
        {
            if (!s.contains("alloca") && !(s.contains("dso_local")))
            {
                temp.add(s);
            }
        }

         */

        ret = temp;


        return ret;
    }

    public String getAValue()
    {
        count++;
        return "%func_" + count;
    }

    public void addParamCount(int num)
    {
        count += num;
    }


    public IrModule getIrModule()
    {
        return irModule;
    }

    public ArrayList<IrBasicBlock> getBasicBlocks()
    {
        return basicBlocks;
    }

    public boolean isMainFunc()
    {
        if (mainFuncDef != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
