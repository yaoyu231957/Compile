package frontend.Parser.function;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class FuncType
{
    private String type = "<FuncType>";
    private Token ftype;

    public FuncType(Token ftype)
    {
        this.ftype = ftype;
    }

    public FuncType ParseFunctype(Tokenlist tokenlist)
    {
        Token token1 = tokenlist.getToken();
        if (token1.getType() != Token.Type.INTTK && token1.getType() != Token.Type.CHARTK && token1.getType() != Token.Type.VOIDTK)
        {
            System.out.println("FuncType + 函数类型错误");
        }
        tokenlist.ReadNext();
        return new FuncType(token1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ftype.myToString());
        sb.append(type + "\n");
        return sb.toString();
    }

    public Token getFtype()
    {
        return ftype;
    }
}
