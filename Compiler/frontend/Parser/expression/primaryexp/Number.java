package frontend.Parser.expression.primaryexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class Number implements PrimaryAllExp
{
    private String type = "<Number>";
    private Token intconst;

    private String valuetype;

    public Number(Token intconst)
    {
        this.intconst = intconst;
    }

    public Number ParseIntConst(Tokenlist tokenlist)
    {
        Number number = new Number(tokenlist.getToken());
        number.setValuetype("int");
        return number;
//        return new Number(Integer.parseInt(tokenlist.getToken().getContent()));
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(intconst.myToString());
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

    public Token getIntconst()
    {
        return intconst;
    }
}
