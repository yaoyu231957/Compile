package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrBranchInst extends IrInstruction
{
    //br i1 <cond>, label <iftrue>, label <iffalse> |
    //br label <dest>
    private IrValue cond;
    private IrValue iftrue;
    private IrValue iffalse;

    private IrValue dest;

    public IrBranchInst(IrType type, IrValue cond, IrValue iftrue, IrValue iffalse)
    {
        // br i1 <cond>, label <iftrue>, label <iffalse>
        super(type);
        this.cond = cond;
        this.iftrue = iftrue;
        this.iffalse = iffalse;
    }

    public IrBranchInst(IrType type, IrValue dest)
    {
        // br label <dest>
        super(type);
        this.dest = dest;
    }

    @Override
    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (dest == null)
        {
            sb.append("br i1 ");
            sb.append(cond.getName());
            sb.append(", label ");
            sb.append(iftrue.getName());
            sb.append(", label ");
            sb.append(iffalse.getName());
        }
        else
        {
            sb.append("br label ");
            sb.append(dest.getName());
        }
        sb.append("\n");
        ret.add(sb.toString());
        return ret;
    }
}
