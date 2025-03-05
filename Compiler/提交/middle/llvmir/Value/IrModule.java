package middle.llvmir.Value;

import middle.llvmir.Value.GlobalValue.IrFunction;
import middle.llvmir.Value.GlobalValue.IrGlobalVar;
import middle.semantic.SymbolTable;

import java.util.ArrayList;

public class IrModule
{
    private ArrayList<IrGlobalVar> globalVars;
    private ArrayList<IrFunction> functions;

    public IrModule()
    {
        this.functions = new ArrayList<>();
        this.globalVars = new ArrayList<>();
    }

    public void addIrFunction(IrFunction function)
    {
        this.functions.add(function);
    }

    public void addIrGlobalVar(IrGlobalVar irGlobalVar)
    {
        this.globalVars.add(irGlobalVar);
    }

    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        String s = "declare i32 @getint()\n" +
                "declare i32 @getchar()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n" +
                "declare void @putstr(i8*)\n\n";
        ret.add(s);
        for (IrGlobalVar irGlobalVar : globalVars)
        {
            ArrayList<String> string = irGlobalVar.irOutput();
            if (string != null && string.size() != 0)
            {
                ret.addAll(string);
            }
        }
        ret.add("\n");
        for (IrFunction function : functions)
        {
            System.out.println(function.getName());
            ArrayList<String> string = function.irOutput();
            if (string != null && string.size() != 0)
            {
                ret.addAll(string);
            }
            ret.add("\n");
        }
        return ret;
    }

    public ArrayList<IrFunction> getFunctions()
    {
        return functions;
    }

    public ArrayList<IrGlobalVar> getGlobalVars()
    {
        return globalVars;
    }
}
