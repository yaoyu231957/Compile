package middle.llvmir.Value.GlobalValue;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

public class IrParam
{
    private String name;
    private IrType type;
    private IrValue irValue;

    public IrParam(String name, IrType type)
    {
        //super(type);
        this.name = name;
        this.type = type;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setIrValue(IrValue irValue)
    {
        this.irValue = irValue;
    }

    public String irOutput()
    {
        if (irValue.getValueType() == IrType.i32)
        {
            return "i32 " + irValue.getName();
        }
        else if (irValue.getValueType() == IrType.i8)
        {
            return "i8 " + irValue.getName();
        }
        else if (irValue.getValueType() == IrType.n_i32)
        {
            return "i32* " + irValue.getName();
        }
        else if (irValue.getValueType() == IrType.n_i8)
        {
            return "i8* " + irValue.getName();
        }
        return "param error";
    }
}
