package frontend.Parser.declaration.constant.consinitval;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class ConstInitVal
{
    private String type = "<ConstInitVal>";

    private ConstAllInitVal constInitVal;

    public ConstInitVal(ConstAllInitVal constInitVal)
    {
        this.constInitVal = constInitVal;
    }

    public ConstInitVal ParseConstInitVal(Tokenlist tokenlist, boolean flag)
    {
        ConstAllInitVal constAllInitVal;
        if (tokenlist.getToken().getType() == Token.Type.LBRACE)
        {
            ConIVArray conIVArray = new ConIVArray(null, null, null, null);
            constAllInitVal = conIVArray.ParseConIVArray(tokenlist, flag);
        }
        else if (tokenlist.getToken().getType() == Token.Type.STRCON)
        {
            ConIVStrConst conIVStrConst = new ConIVStrConst(null);
            constAllInitVal = conIVStrConst.ParseConIVStrConst(tokenlist);
        }
        else
        {
            ConIVConstExp conIVConstExp = new ConIVConstExp(null);
            constAllInitVal = conIVConstExp.ParseConIVConstExp(tokenlist, flag);
        }
        return new ConstInitVal(constAllInitVal);
    }

    public String myToString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(constInitVal.toString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public ConstAllInitVal getConstInitVal()
    {
        return constInitVal;
    }
}
