package middle.llvmir.Value.IrInstruction;

import middle.llvmir.Value.IrType;
import middle.llvmir.Value.IrValue;

import java.util.ArrayList;

public class IrBlockInfo extends IrInstruction
{
    //1:               ; preds = %0
    //<name>:   ; preds = {predslist}
    IrValue name;
    ArrayList<IrValue> preds;

    public IrBlockInfo(IrType type, IrValue name)
    {
        super(type);
        this.name = name;
        this.preds = new ArrayList<>();
    }

    public void addPreds(IrValue pred)
    {
        preds.add(pred);
    }

    public ArrayList<String> irOutput()
    {
        ArrayList<String> ret = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String s = name.getName();
        s = s.substring(1);
        sb.append("\n");
        sb.append(s);
        sb.append(":");
        if (!preds.isEmpty())
        {
            sb.append("                                               ;pred = ");
            sb.append(preds.get(0).getName());
            for (int i = 1; i < preds.size(); i++)
            {
                sb.append(", ");
                sb.append(preds.get(i).getName());
            }
        }
        sb.append("\n");
        ret.add(sb.toString());
        return ret;
    }

}
