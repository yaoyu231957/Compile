package frontend.Parser.declaration.constant.consinitval;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class ConIVStrConst implements ConstAllInitVal
{
    private Token stringconst;

    public ConIVStrConst(Token stringconst)
    {
        this.stringconst = stringconst;
    }

    public ConIVStrConst ParseConIVStrConst(Tokenlist tokenlist)
    {
        Token stringconst1 = tokenlist.getToken();
        tokenlist.ReadNext();
        return new ConIVStrConst(stringconst1);
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
