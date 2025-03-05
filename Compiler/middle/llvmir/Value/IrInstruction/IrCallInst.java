package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.GlobalValue.IrFunction;
import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrCallInst extends IrInstruction
{
    //<result> = call [ret attrs] <ty> <name>(<...args>)
    private IrType type;
    private String name;
    private IrFunction function;
    private String functionName;
    private ArrayList<IrValue> args;

    private String result;
    private IrValue irValue;

    public IrCallInst(IrType type, IrFunction function, ArrayList<IrValue> args)
    {
        super(type);
        this.type = type;
        this.args = args;
        this.functionName = function.getName();
    }

    /* 处理get */
    public IrCallInst(String functionName)
    {
        super(IrType.i32);
        this.type = IrType.i32;
        this.functionName = functionName;
    }

    /* 处理putch*/
    public IrCallInst(String functionName, char c)
    {
        super(IrType.Void);
        this.type = IrType.Void;
        this.functionName = functionName;
        IrValue irValue = new IrValue(IrType.i8, String.valueOf((int) c));
        this.irValue = irValue;
        //this.setOperand(value, 1);
    }

    /* 处理putstr*/
    public IrCallInst(String functionName, String string)
    {
        super(IrType.Void);
        this.type = IrType.Void;
        this.functionName = functionName;
        IrValue irValue = new IrValue(IrType.i8, string);
        this.irValue = irValue;
        //this.setOperand(value, 1);
    }

    /* 处理putint(i32)*/
    public IrCallInst(String functionName, IrValue irValue)
    {
        super(IrType.Void);
        this.type = IrType.Void;
        this.functionName = functionName;
        this.irValue = irValue;
        //this.retVoid = true;
        //  value = new IrValue(IrIntegerType.get32(), String.valueOf(num));
        // value.setName("i32 " + value.getName());
        //this.setOperand(value, 1);
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }

    public String getName()
    {
        return result;
    }

    public void setName(String name)
    {
        this.result = name;
    }

    public IrType getType()
    {
        return type;
    }

    @Override
    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        if (type != IrType.Void)
        {
            sb.append(result + " = ");
        }
        sb.append("call ");
        if (type == IrType.Void)
        {
            sb.append("void ");
        }
        else if (type == IrType.i32)
        {
            sb.append("i32 ");
        }
        else if (type == IrType.i8)
        {
            sb.append("i8 ");
        }
        sb.append("@" + functionName);
        sb.append("(");
        if (functionName.equals("putstr"))
        {
            sb.append(irValue.getName());
        }
        if (functionName.equals("putint") || functionName.equals("putch"))
        {
            sb.append(irValue.getValueType() + " " + irValue.getName());
        }
        if (args != null && !args.isEmpty())
        {
            // 有参数
            for (int i = 0; i < args.size(); i++)
            {
                IrValue arg = args.get(i);
                if (arg.getValueType() == IrType.i32)
                {
                    sb.append("i32 ");
                }
                else if (arg.getValueType() == IrType.i8)
                {
                    sb.append("i8 ");
                }
                else if (arg.getValueType() == IrType.n_i32)
                {
                    sb.append("i32* ");
                }
                else if (arg.getValueType() == IrType.n_i8)
                {
                    sb.append("i8* ");
                }
                sb.append(arg.getName());
                if (i != args.size() - 1)
                {
                    sb.append(", ");
                }
            }
        }
        sb.append(")\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
