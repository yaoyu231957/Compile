package middle.syntax;

import java.util.ArrayList;

public class SymbolVar extends Symbol
{
    private String var;
    private ArrayList<String> array;

    public SymbolVar(int line, int level, String name, Type type, int dimension)
    {
        super(line, level, name, type, dimension);
    }

    public void setVar(String var)
    {
        this.var = var;
    }

    public void setArray(ArrayList<String> array)
    {
        this.array = array;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getLevel() + " " + getName() + " " + getType());
        return sb.toString();
    }
}