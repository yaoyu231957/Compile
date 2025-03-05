package frontend.Parser.expression.unaryexp;

import frontend.Parser.expression.primaryexp.PrimaryExp;
import frontend.lexer.Tokenlist;
import middle.semantic.SymbolTable;

public class UnaryPriExp implements UnaryAll
{
    private PrimaryExp exp;

    private String valuetype;

    public UnaryPriExp(PrimaryExp exp)
    {
        this.exp = exp;
    }

    public UnaryPriExp ParseUnaryPriExp(Tokenlist tokenlist, boolean flag)
    {
        PrimaryExp exp1 = new PrimaryExp(null);
        PrimaryExp exp2 = exp1.ParsePrimaryExp(tokenlist, flag);
        UnaryPriExp unaryPriExp = new UnaryPriExp(exp2);
        unaryPriExp.setValuetype(exp2.getValuetype());
        return unaryPriExp;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exp.toString());
        return sb.toString();
    }

    public void setValuetype(String valuetype)
    {
        this.valuetype = valuetype;
    }

    public String getValuetype()
    {
        return valuetype;
    }

    public PrimaryExp getExp()
    {
        return exp;
    }

    public int calcExp(SymbolTable symbolTable)
    {
        return exp.calcExp(symbolTable);
    }
}
