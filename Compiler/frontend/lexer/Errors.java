package frontend.lexer;

import java.util.ArrayList;

public class Errors
{
    private int line;
    private char type;

    public Errors(int line, char type)
    {
        this.line = line;
        this.type = type;
    }

    public int getLine()
    {
        return line;
    }

    public char getType()
    {
        return type;
    }
}
