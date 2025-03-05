package middle.llvmir.Value;

import java.util.ArrayList;

public class IrValue
{
    private IrType valueType; // Value 类型
    private String name; //
    private boolean needName; // TODO
    //private LinkedList<IrUse> uses;
    private boolean isParam = false;
    

    public IrValue(IrType valueType)
    {
        this.valueType = valueType;
        this.name = "";
        this.needName = true;
        //this.uses = new LinkedList<>();
    }

    public IrValue(IrType valueType, String name)
    {
        this(valueType);
        this.name = name;
    }

    public IrValue(IrType valueType, String name, boolean isParam)
    {
        this(valueType, name);
        this.valueType = valueType;
        this.name = name;
        this.isParam = isParam;
    }

    public void setValueType(IrType valueType)
    {
        this.valueType = valueType;
    }

    /*
    public void addUse(IrUse use)
    {
        this.uses.add(use);
    }



    public void removeUse(IrUse use)
    {
        this.uses.removeIf(h -> h.equals(use));
    }

     */

    public IrType getValueType()
    {
        return valueType;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isParam()
    {
        return isParam;
    }

    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(this.name);
        return ret;
    }
}
