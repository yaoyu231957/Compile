package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrLoadInst extends IrInstruction
{
    //load : <result> = load <ty>, <ty>* <pointer>
    private String result;
    private IrType type;
    private IrValue pointer;
    private boolean isLeft = false;

    public IrLoadInst(String result, IrType type, IrValue pointer)
    {
        super(type);
        this.result = result;
        this.type = type;
        this.pointer = pointer;
    }

    public IrLoadInst(String result, IrType type, IrValue pointer, boolean isLeft)
    {
        super(type);
        this.result = result;
        this.type = type;
        this.pointer = pointer;
        this.isLeft = isLeft;
    }

    public IrValue getPointer()
    {
        return pointer;
    }

    @Override
    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String type_change = "";
        if (type == IrType.n_i32 || (type == IrType.i32 && isLeft))
        {
            type_change = "i32*";
        }
        else if (type == IrType.n_i8 || (type == IrType.i8 && isLeft))
        {
            type_change = "i8*";
        }
        else if (type == IrType.i32)
        {
            type_change = "i32";
        }
        else if (type == IrType.i8)
        {
            type_change = "i8";
        }
        sb.append(result);
        sb.append(" = load ");
        sb.append(type_change);
        sb.append(", ");
        sb.append(type_change + "* ");
        sb.append(pointer.getName());
        sb.append("\n");
        ret.add(sb.toString());
        return ret;
    }

    public String getName()
    {
        return result;
    }

    public void setName(String name)
    {
        this.result = name;
    }
}
