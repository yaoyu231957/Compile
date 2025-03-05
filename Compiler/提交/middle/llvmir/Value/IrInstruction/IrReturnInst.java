package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrReturnInst extends IrInstruction
{
    //ret <irvalue>
    private IrValue irValue;

    public IrReturnInst(IrValue irValue)
    {
        super(irValue.getValueType());
        this.irValue = irValue;
    }

    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ret ");
        if (irValue.getValueType() == IrType.Void)
        {
            sb.append("void\n");
        }
        else
        {
            sb.append(irValue.getValueType());
            sb.append(" ");
            sb.append(irValue.getName());
            sb.append("\n");
        }
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
