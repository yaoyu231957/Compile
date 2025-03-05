package middle.llvmir.Value;

import frontend.Parser.CompUnit;
import frontend.Parser.declaration.Decl;
import frontend.Parser.function.FuncDef;
import middle.llvmir.Value.GlobalValue.IrFunction;
import middle.llvmir.Value.GlobalValue.IrGlobalVar;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class IrBuilder
{
    private CompUnit compUnit; // 作为构造器参数，为语法树的顶层节点，其蕴含了语法树的全部信息
    private IrModule module; // 顶层Module单元
    private SymbolTable symbolTable; // 当前指向的符号表

    public IrBuilder(CompUnit compUnit)
    {
        this.compUnit = compUnit;
        this.module = new IrModule(); // 生成新的IrModule
        this.symbolTable = new SymbolTable(null);
    }

    public IrModule buildIrModule()
    {
        /* 生成全局变量 */
        for (Decl decl : compUnit.getDecls())
        {
            IrGlobalVar irGlobalVar = new IrGlobalVar(symbolTable, decl, IrType.i8);
            ArrayList<IrGlobalVar> globalVars = irGlobalVar.buildGlobalVar();
            for (IrGlobalVar var : globalVars)
            {
                if (var == null)
                {
                    continue;
                }
                module.addIrGlobalVar(var);
            }
        }
        for (FuncDef funcDef : compUnit.getFuncDefs())
        {
            SymbolTable symbolTable1 = new SymbolTable(symbolTable); // 进入新的函数，进入新的子表
            IrFunction irFunction = new IrFunction(IrType.i32, symbolTable1, funcDef, module);
            IrFunction irFunction1 = irFunction.buildIrFunction();
            module.addIrFunction(irFunction1);
        }
        SymbolTable symbolTable2 = new SymbolTable(symbolTable);
        IrFunction irFunction = new IrFunction(IrType.i32, symbolTable2, compUnit.getMainFuncDef(), module);
        module.addIrFunction(irFunction.buildIrFunction());
        return module;
    }
}
