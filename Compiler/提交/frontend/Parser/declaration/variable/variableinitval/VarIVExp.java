package frontend.Parser.declaration.variable.variableinitval;

import frontend.Parser.expression.Exp;
import frontend.lexer.Tokenlist;

public class VarIVExp implements VarAllInitVal
{
    private Exp exp;

    public VarIVExp(Exp exp)
    {
        this.exp = exp;
    }

    public VarIVExp ParseVarIVExp(Tokenlist tokenlist, boolean flag)
    {
        Exp exp1 = new Exp(null);
        Exp exp2 = exp1.ParseExp(tokenlist, flag);
        return new VarIVExp(exp2);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exp.myToString());
        return sb.toString();
    }

    public Exp getExp()
    {
        return exp;
    }
}
