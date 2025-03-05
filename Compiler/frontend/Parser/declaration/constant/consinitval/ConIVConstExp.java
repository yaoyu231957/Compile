package frontend.Parser.declaration.constant.consinitval;

import frontend.Parser.expression.arithmeticalexp.ConstExp;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class ConIVConstExp implements ConstAllInitVal
{
    private ConstExp constExp;

    public ConIVConstExp(ConstExp constExp)
    {
        this.constExp = constExp;
    }

    public ConIVConstExp ParseConIVConstExp(Tokenlist tokenlist, boolean flag)
    {
        ConstExp constExp1 = new ConstExp(null);
        ConstExp constExp2 = constExp1.ParseConstExp(tokenlist, flag);
        return new ConIVConstExp(constExp2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(constExp.myToString());
        return sb.toString();
    }

    public int calcExp(SymbolTable symbolTable)
    {
        return constExp.calcExp(symbolTable);
    }

    public ConstExp getConstExp()
    {
        return constExp;
    }
}
