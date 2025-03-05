package frontend.Parser.declaration.variable.variableinitval;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class VarIVStrConst implements VarAllInitVal
{
    private Token stringconst;

    public VarIVStrConst(Token stringconst)
    {
        this.stringconst = stringconst;
    }

    public VarIVStrConst ParseVarIVStrConst(Tokenlist tokenlist)
    {
        Token stringconst1 = tokenlist.getToken();
        tokenlist.ReadNext();
        return new VarIVStrConst(stringconst1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(stringconst.myToString());
        return sb.toString();
    }

    public Token getStringconst()
    {
        return stringconst;
    }
}
