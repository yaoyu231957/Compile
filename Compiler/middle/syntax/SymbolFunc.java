package middle.syntax;

import java.util.ArrayList;

public class SymbolFunc extends Symbol
{
    private ArrayList<Symbol> symbols = new ArrayList<>();

    public SymbolFunc(int line, int level, String name, Type type, int dimension)
    {
        super(line, level, name, type, dimension);
    }

    public void addSymbol(Symbol symbol)
    {
        this.symbols.add(symbol);
    }

    public ArrayList<Symbol> getSymbols()
    {
        return symbols;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getLevel() + " " + getName() + " " + getType());
        return sb.toString();
    }
}