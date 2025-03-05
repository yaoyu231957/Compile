package middle.semantic;

import java.util.ArrayList;

public class SymbolConst extends Symbol
{
    private String constvar;
    private ArrayList<String> constarray;

    public SymbolConst(int line, int level, String name, Type type)
    {
        super(line, level, name, type);
    }

    public void setConstvar(String constvar)
    {
        this.constvar = constvar;
    }

    public void setConstarray(ArrayList<String> constarray)
    {
        this.constarray = constarray;
    }

    public ArrayList<String> getConstarray()
    {
        return constarray;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getLevel() + " " + getName() + " " + getType());
        return sb.toString();
    }
}
