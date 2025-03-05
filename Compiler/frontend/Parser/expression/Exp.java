package frontend.Parser.expression;

import frontend.Parser.expression.arithmeticalexp.AddExp;
import frontend.lexer.Tokenlist;

public class Exp
{
    private String type = "<Exp>";
    private AddExp exp;
    private String valuetype;

    public Exp(AddExp exp)
    {
        this.exp = exp;
    }

    public Exp ParseExp(Tokenlist tokenlist, boolean flag)
    {
        AddExp addExp = new AddExp(null, null);
        AddExp exp1 = addExp.ParseAddExp(tokenlist, flag);
        Exp exp2 = new Exp(exp1);
        exp2.setValuetype(exp1.getValuetype());
        return exp2;
    }

    public String myToString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(exp.toString());
        sb.append(type + "\n");
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

    public AddExp getExp()
    {
        return exp;
    }
}
