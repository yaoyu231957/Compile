package frontend.Parser.declaration;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class Btype
{
    private String type = "<Btype>";
    private Token token;

    public Btype(Token token)
    {
        this.token = token;
    }

    public Btype ParseBtype(Tokenlist tokenlist)
    {
        Token token1 = tokenlist.getToken();
        tokenlist.ReadNext();
        return new Btype(token1);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(token.myToString());
        return sb.toString();
    }

    public Token getToken()
    {
        return token;
    }
}
