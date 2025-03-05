package middle.syntax;

import java.util.ArrayList;

public class SymbolTable
{
    /* 符号名 -> 符号obj */
    //private HashMap<String, Symbol> symbols;

    private ArrayList<Symbol> symbols;
    private SymbolTable parent = null;

    private ArrayList<SymbolTable> childrens;
    private int level;
    private static int alllevel = 1;
    private int circle;
    private static SymbolTable allsymboltable = new SymbolTable(null);

    public SymbolTable(SymbolTable parent)
    {
        this.symbols = new ArrayList<>();
        this.parent = parent;
        this.childrens = new ArrayList<>();
        this.level = alllevel;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void GetInCircle()
    {
        circle++;
    }

    public void GetOutCircle()
    {
        circle--;
    }

    public int getCircle()
    {
        return circle;
    }

    public SymbolTable getParent()
    {
        return parent;
    }

    public boolean hasParent()
    {
        return this.parent != null;
    }

    public void addChildren(SymbolTable symbolTable)
    {
        childrens.add(symbolTable);
    }

    public void addSymbol(Symbol symbol)
    {
        int i = 0;
        while (i < symbols.size() && symbol.getLevel() >= symbols.get(i).getLevel())
        {
            i++;
        }
        symbols.add(i, symbol);
    }

    /* 检测出b类错误返回true，否则返回false */
    public boolean checkHasSymbol(Symbol symbol)
    {
        for (Symbol symbol1 : symbols)
        {
            if (symbol1.getName().equals(symbol.getName()))
            {
                return true;
            }
        }
        return false;
    }

    /* 检测出c类错误返回true，否则返回false */
    public boolean checkNotHasSymbol(String name)
    {
        for (Symbol symbol : symbols)
        {
            if (symbol.getName().equals(name))
            {
                return false;
            }
        }
        if (this.hasParent())
        {
            return this.getParent().checkNotHasSymbol(name);
        }
        else
        {
            return true;
        }
    }

    /* 获取指定名字对应的参数对象，如果没有则返回null */
    public Symbol getSymbol(String name)
    {
        for (Symbol symbol : symbols)
        {
            if (symbol.getName().equals(name))
            {
                return symbol;
            }
        }
        if (this.hasParent())
        {
            return this.parent.getSymbol(name);
        }
        return null;
    }

    public static void UpLevel()
    {
        alllevel++;
    }

    public static void DownLevel()
    {
        alllevel--;
    }

    public static SymbolTable getAllsymboltable()
    {
        return allsymboltable;
    }

    public static void changeAllSymbolTable(SymbolTable symbolTable)
    {
        //allsymboltable.setChildren(symbolTable);
        allsymboltable = symbolTable;
    }

    /*
    public static void addSymboltables(SymbolTable symbolTable)
    {
        int i = 0;
        while (i < symbolTables.size() && symbolTable.getLevel() >= symbolTables.get(i).getLevel())
        {
            i++;
        }
        symbolTables.add(i, symbolTable);
    }

     */

    public static int getAllLevel()
    {
        return alllevel;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Symbol symbol : symbols)
        {
            sb.append(symbol.toString());
            sb.append('\n');
        }
        for (SymbolTable symbolTable : childrens)
        {
            sb.append(symbolTable.toString());
        }
        return sb.toString();
    }
}
