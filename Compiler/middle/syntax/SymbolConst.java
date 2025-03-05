package middle.syntax;

import java.util.ArrayList;

public class SymbolConst extends Symbol
{
    private String constvar;
    private ArrayList<String> constarray;

    public SymbolConst(int line, int level, String name, Type type, int dimension)
    {
        super(line, level, name, type, dimension);
    }

    public void setConstvar(String constvar)
    {
        this.constvar = constvar;
    }

    public void setConstarray(ArrayList<String> constarray)
    {
        this.constarray = constarray;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getLevel() + " " + getName() + " " + getType());
        return sb.toString();
    }
}
