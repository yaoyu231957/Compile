package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.GlobalValue.IrGlobalVar;
import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrGetelement extends IrInstruction
{
    //<result> = getelementptr inbounds <tyoe>, <type>* <operator>, <type> <offset>
    private String result;
    private IrType type;
    private IrValue operator;
    private String offset;
    private boolean isParam = false;

    public IrGetelement(String result, IrType type, IrValue operator, String offset)
    {
        super(type);
        this.result = result;
        this.type = type;
        this.operator = operator;
        this.offset = offset;
    }

    public String getName()
    {
        return result;
    }

    public void setName(String name)
    {
        this.result = name;
    }

    public void setParam(boolean isParam)
    {
        this.isParam = isParam;
    }

    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        String type_change = "";
        //String type_offset = "";
        int num = 0;
        if (operator instanceof IrGlobalVar)
        {
            num = ((IrGlobalVar) operator).getNum();
        }
        else if (operator instanceof IrAllocaInst)
        {
            num = ((IrAllocaInst) operator).getNum();
        }
        if (type == IrType.i32)
        {
            type_change = "i32";
            //type_offset = "i32";
        }
        else if (type == IrType.i8)
        {
            type_change = "i8";
            //type_offset = "i8";
        }
        else if (type == IrType.n_i32)
        {
            if (num != 0)
            {
                type_change = "[" + num + " x i32]";
            }
            else
            {
                type_change = "i32";
            }
            //type_offset = "i32";
        }
        else if (type == IrType.n_i8)
        {
            if (num != 0)
            {
                type_change = "[" + num + " x i8]";
            }
            else
            {
                type_change = "i8";
            }
            //type_offset = "i8";
        }
        sb.append(result + " = getelementptr inbounds ");
        sb.append(type_change);
        sb.append(", ");
        sb.append(type_change + "* ");
        sb.append(operator.getName());
        if (num != 0)
        {
            //sb.append(", " + type_offset + " 0, ");
            sb.append(", i32 0 ,");
        }
        else
        {
            sb.append(", ");
        }

        //sb.append(type_offset + " " + offset);
        sb.append("i32 " + offset);
        sb.append("\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
