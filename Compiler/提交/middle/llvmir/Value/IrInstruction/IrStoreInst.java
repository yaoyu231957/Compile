package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrStoreInst extends IrInstruction
{
    //store <type> <value>,<type>* <name>
    private IrValue name;
    private IrType type;
    private IrValue value;

    private ArrayList<Character> characters;
    private boolean isParam = false;
    private boolean isSting = false;
    private int num;

    public IrStoreInst(IrValue name, IrType type, IrValue value)
    {
        super(type);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public IrStoreInst(IrValue name, IrType type, String value)
    {
        super(type);
        this.name = name;
        this.type = type;
        IrValue irValue = new IrValue(type, value);
        this.value = irValue;
    }

    public IrStoreInst(IrValue name, IrType type, ArrayList<Character> characters)
    {
        super(type);
        this.name = name;
        this.type = type;
        this.characters = characters;
        this.isSting = true;
    }

    public IrStoreInst(IrValue name, IrType type, IrValue value, boolean isParam)
    {
        super(type);
        this.name = name;
        this.type = type;
        this.value = value;
        this.isParam = isParam;
    }

    public String getName()
    {
        return name.getName();
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        String name = this.name.getName();
        String value = null;
        if (!isSting)
        {
            value = this.value.getName();
        }
        if (type == IrType.i32 || type == IrType.i8)
        {
            sb.append("store ");
            sb.append(type);
            sb.append(" ");
            sb.append(value);
            sb.append(", ");
            sb.append(type);
            sb.append("* ");
            sb.append(name);
            sb.append("\n");
        }
        else if (type == IrType.n_i8 && !isParam)
        {
            //  store [3 x i8] [i8 97, i8 98, i8 99], [3 x i8]* %a, align 1
            sb.append("store [");
            sb.append(num);
            sb.append(" x i8] [i8 ");
            if (!isSting)
            {
                if (!value.isEmpty())
                {
                    sb.append((int) value.charAt(0));
                }
                else
                {
                    sb.append(0);
                }
                for (int i = 1; i < value.length(); i++)
                {
                    sb.append(", i8 ");
                    sb.append((int) value.charAt(i));
                }
                int length = Math.max(value.length(), 1);
                for (int i = length; i < num; i++)
                {
                    sb.append(", i8 0");
                }
            }
            else
            {
                if (!characters.isEmpty())
                {
                    sb.append((int) characters.get(0));
                }
                else
                {
                    sb.append(0);
                }
                for (int i = 1; i < characters.size(); i++)
                {
                    sb.append(", i8 ");
                    sb.append((int) characters.get(i));
                }
                int length = Math.max(characters.size(), 1);
                for (int i = length; i < num; i++)
                {
                    sb.append(", i8 0");
                }
            }
            sb.append("], [");
            sb.append(num);
            sb.append(" x i8]* ");
            sb.append(name);
            sb.append(", align 1\n");
        }
        else if (type == IrType.n_i32 && !isParam)
        {
            //  store [3 x i8] [i8 97, i8 98, i8 99], [3 x i8]* %a, align 1
            sb.append("store [");
            sb.append(num);
            sb.append(" x i32] [i32 ");
            if (!value.isEmpty())
            {
                sb.append((int) value.charAt(0));
            }
            else
            {
                sb.append(0);
            }
            int length = Math.max(value.length(), 1);
            for (int i = length; i < num; i++)
            {
                sb.append(", i32 0");
            }
            sb.append("], [");
            sb.append(num);
            sb.append(" x i32]* ");
            sb.append(name);
            sb.append("\n");
        }
        else if (type == IrType.n_i8 && isParam)
        {
            sb.append("store i8* ");
            sb.append(value);
            sb.append(", i8** ");
            sb.append(name);
            sb.append("\n");
        }
        else if (type == IrType.n_i32 && isParam)
        {
            sb.append("store i32* ");
            sb.append(value);
            sb.append(", i32** ");
            sb.append(name);
            sb.append("\n");
        }
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }
}
