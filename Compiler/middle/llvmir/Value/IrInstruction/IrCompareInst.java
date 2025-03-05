package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrCompareInst extends IrInstruction
{
    public enum Type
    {
        Lt,//<
        Le,//<=
        Gt,//>
        Ge,//>=
        Eq,//==
        Neq,//!=
    }

    //<result> = icmp <cond> <ty> <op1>, <op2>
    private String result;
    private Type cond;
    private IrType type;
    private IrValue op1;
    private IrValue op2;

    public IrCompareInst(String result, Type cond, IrType type, IrValue op1, IrValue op2)
    {
        super(IrType.i1);
        this.result = result;
        this.cond = cond;
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
    }

    public String getName()
    {
        return result;
    }

    public void setName(String name)
    {
        this.result = name;
    }

    public void setOp1(IrValue op1)
    {
        this.op1 = op1;
    }

    @Override
    public ArrayList<String> irOutput()
    {

        StringBuilder sb = new StringBuilder();
        sb.append(result + " = icmp ");
        switch (cond)
        {
            case Lt:
                sb.append("slt ");
                break;
            case Le:
                sb.append("sle ");
                break;
            case Gt:
                sb.append("sgt ");
                break;
            case Ge:
                sb.append("sge ");
                break;
            case Eq:
                sb.append("eq ");
                break;
            case Neq:
                sb.append("ne ");
                break;
            default:
                System.out.println("ERROR in IrBinaryInst : should not reach here");
                break;
        }
        if (type == IrType.i32)
        {
            sb.append("i32 ");
        }
        else if (type == IrType.i8)
        {
            sb.append("i8 ");
        }

        sb.append(op1.getName());
        sb.append(", ");
        sb.append(op2.getName());
        sb.append("\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
