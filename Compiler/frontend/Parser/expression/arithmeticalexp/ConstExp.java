package frontend.Parser.expression.arithmeticalexp;

import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class ConstExp
{
    private String type = "<ConstExp>";
    private AddExp exp;

    public ConstExp(AddExp exp)
    {
        this.exp = exp;
    }

    public ConstExp ParseConstExp(Tokenlist tokenlist, boolean flag)
    {
        AddExp addExp = new AddExp(null, null);
        AddExp exp1 = addExp.ParseAddExp(tokenlist, flag);
        return new ConstExp(exp1);
    }

    public String myToString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exp.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public int calcExp(SymbolTable symbolTable)
    {
        return exp.calcExp(symbolTable);
    }

    public AddExp getExp()
    {
        return exp;
    }
}
