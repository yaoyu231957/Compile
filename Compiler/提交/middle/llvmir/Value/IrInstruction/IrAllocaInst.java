package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrAllocaInst extends IrInstruction
{
    //<name> = alloca <num> * <type>
    private String name;
    private IrType type;
    private int num;
    private boolean isParam;

    public IrAllocaInst(String name, IrType type, int num)
    {
        super(type);
        this.name = name;
        this.type = type;
        this.num = num;
    }

    public IrAllocaInst(String name, IrType type, int num, boolean isParam)
    {
        super(type);
        this.name = name;
        this.type = type;
        this.num = num;
        this.isParam = isParam;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getNum()
    {
        return num;
    }

    @Override
    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        if (isParam)
        {
            if (type == IrType.n_i32 || type == IrType.n_i8)
            {
                name = name.replaceAll("func", "array_param");
            }
            else
            {
                name = name.replaceAll("func", "param");
            }
        }
        sb.append(name + " = alloca");
        if (type == IrType.i32 || (type == IrType.n_i32 && !isParam))
        {
            if (num == 1 && type == IrType.i32)
            {
                sb.append(" i32");
            }
            else
            {
                sb.append(" [" + num + " x i32]");
            }
        }
        else if (type == IrType.i8 || (type == IrType.n_i8 && !isParam))
        {
            if (num == 1 && type == IrType.i8)
            {
                sb.append(" i8");
            }
            else
            {
                sb.append(" [" + num + " x i8] , align 1");
            }
        }
        else if (type == IrType.n_i8)
        {
            sb.append(" i8*");
        }
        else if (type == IrType.n_i32)
        {
            sb.append(" i32*");
        }
        sb.append("\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
