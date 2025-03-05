package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrBinaryOperator extends IrInstruction
{
    public enum Type
    {
        Add, Sub, Mul, Div, Mod,
        And,// &
        Or, // |
        Not,//!
    }

    //<result> = <op> nsw <ty> <op1>, <op2>
    private String result;
    private Type op;
    private IrType type;
    private IrValue op1;
    private IrValue op2;

    public IrBinaryOperator(String result, Type op, IrType type, IrValue op1, IrValue op2)
    {
        super(type);
        this.result = result;
        this.op = op;
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

    public void setResult(String result)
    {
        this.result = result;
    }

    @Override
    public ArrayList<String> irOutput()
    {

        StringBuilder sb = new StringBuilder();
        sb.append(result + " = ");
        switch (op)
        {
            case Add:
                sb.append("add nsw ");
                break;
            case Sub:
                sb.append("sub nsw ");
                break;
            case Mul:
                sb.append("mul nsw ");
                break;
            case Div:
                sb.append("sdiv ");
                break;
            case Mod:
                sb.append("srem ");
                break;
            case Not:
                sb.append("xor ");
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
        else if (type == IrType.i1)
        {
            sb.append("i1 ");
        }


        sb.append(op1.getName());

        sb.append(", ");

        sb.append(op2.getName());

        sb.append("\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }

    public String getResult()
    {
        return result;
    }
}
