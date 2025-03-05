package frontend.Parser.expression;

import frontend.Parser.expression.arithmeticalexp.AddExp;
import frontend.Parser.expression.arithmeticalexp.ConstExp;
import frontend.Parser.expression.arithmeticalexp.LOrExp;
import frontend.lexer.Tokenlist;

public class CondExp
{
    private String type = "<Cond>";
    private LOrExp exp;

    public CondExp(LOrExp exp)
    {
        this.exp = exp;
    }

    public CondExp ParseCondExp(Tokenlist tokenlist, boolean flag)
    {
        LOrExp lOrExp = new LOrExp(null, null);
        LOrExp exp1 = lOrExp.ParseLOrExp(tokenlist, flag);
        return new CondExp(exp1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exp.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public LOrExp getExp()
    {
        return exp;
    }
}
