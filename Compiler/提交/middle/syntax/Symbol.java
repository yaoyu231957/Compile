package middle.syntax;

public class Symbol
{
    public enum Type
    {
        ConstChar, ConstInt, ConstCharArray, ConstIntArray, Char, Int, CharArray, IntArray, VoidFunc, CharFunc, IntFunc
    }

    private int line;
    private int level;// 从1开始
    private String name;
    private Type type = null;
    private int dimension; // 维数

    public Symbol(int line, int level, String name, Type type, int dimension)
    {
        this.line = line;
        this.level = level;
        this.name = name;
        this.type = type;
        this.dimension = dimension;
    }

    public String getName()
    {
        return name;
    }

    public int getLine()
    {
        return line;
    }

    public Type getType()
    {
        return type;
    }

    public int getLevel()
    {
        return level;
    }

    public void setdimension(int dimension)
    {
        this.dimension = dimension;
    }

    public int getdimension()
    {
        return dimension;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(level + name + type);
        return sb.toString();
    }

}
