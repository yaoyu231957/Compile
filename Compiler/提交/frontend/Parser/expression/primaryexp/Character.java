package frontend.Parser.expression.primaryexp;

import frontend.lexer.Token;
import frontend.lexer.Tokenlist;

public class Character implements PrimaryAllExp
{
    private String type = "<Character>";
    private Token charconst;

    private String valuetype;

    public Character(Token charconst)
    {
        this.charconst = charconst;
    }

    public Character ParseCharConst(Tokenlist tokenlist)
    {
        Token s = tokenlist.getToken();
        Character character = new Character(s);
        character.setValuetype("char");
        return character;
        /*
        String s = tokenlist.getToken().getContent();
        ////System.out.println(s.length());
        if (s.length() == 1)
        {
            return new Character(s.charAt(0));
        }
        else
        {
            return new Character(s.charAt(1));
        }

         */
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(charconst.myToString());
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

    public Token getCharconst()
    {
        return charconst;
    }
}
