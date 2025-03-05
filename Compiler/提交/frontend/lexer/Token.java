package frontend.lexer;

public class Token
{
    public enum Type
    {
        IDENFR, INTCON, STRCON, CHRCON, MAINTK, CONSTTK, INTTK, CHARTK, BREAKTK, CONTINUETK, IFTK, ELSETK, NOT, AND, OR, FORTK, GETINTTK, GETCHARTK, PRINTFTK, RETURNTK, PLUS, MINU, VOIDTK, MULT, DIV, MOD, LSS, LEQ, GRE, GEQ, EQL, NEQ, ASSIGN, SEMICN, COMMA, LPARENT, RPARENT, LBRACK, RBRACK, LBRACE, RBRACE
    }

    private final Type type;

    private final String content;

    private int line;
    private boolean error;

    public Type getType()
    {
        return type;
    }

    public String getContent()
    {
        return content;
    }

    public int getLine()
    {
        return line;
    }

    public boolean getError()
    {
        return error;
    }

    public Token(Type type, String content, int line, boolean error)
    {
        this.type = type;
        this.content = content;
        this.line = line;
        this.error = error;
    }

    public String myToString()
    {
        return type + " " + content + "\n";
    }


}
