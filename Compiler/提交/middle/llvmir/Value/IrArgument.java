package middle.llvmir.Value;

public class IrArgument
{
    private static int count = 0;

    public static String getAValue()
    {
        count++;
        return "%main_" + count;
    }
}
