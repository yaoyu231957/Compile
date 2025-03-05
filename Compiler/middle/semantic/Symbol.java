package middle.semantic;

import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

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
    private String value; // 值
    private IrValue irValue; //对应的IrValue

    public Symbol(int line, int level, String name, Type type)
    {
        this.line = line;
        this.level = level;
        this.name = name;
        this.type = type;
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

    public String getValue()
    {
        return value;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public void setIrValue(IrValue irValue)
    {
        this.irValue = irValue;
    }

    public IrValue getIrValue()
    {
        return irValue;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(level + name + type);
        return sb.toString();
    }

}
