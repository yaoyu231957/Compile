package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrTrunc extends IrInstruction
{
    //<result> = trunc <ty> <value> to <ty2>
    private String result;
    private IrType type1;
    private IrValue value;
    private IrType type2;

    public IrTrunc(IrType type1, String result, IrValue value, IrType type2)
    {
        super(type2);
        this.result = result;
        this.type1 = type1;
        this.value = value;
        this.type2 = type2;
    }

    public String getName()
    {
        return result;
    }

    public void setName(String name)
    {
        this.result = name;
    }

    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(result);
        sb.append(" = trunc ");
        sb.append(type1);
        sb.append(" ");
        sb.append(value.getName());
        sb.append(" to ");
        sb.append(type2);
        sb.append("\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
