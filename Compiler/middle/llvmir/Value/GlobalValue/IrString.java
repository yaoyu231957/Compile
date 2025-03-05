package middle.llvmir.Value.GlobalValue;

import middle.llvmir.Value.IrType;

import java.util.ArrayList;

public class IrString extends IrGlobalVar
{

    //<name> = private unnamed_addr constant [<num> x i8] c"<value>", align 1
    private String name;
    private String value;
    private int length;

    private static int count = -1;
    private static boolean has_n = false;
    private static IrString irString;

    public IrString(String value)
    {
        super(IrType.n_i8, null, true, value);
        this.name = getAName();
        this.length = value.length() + 1;
        value = value + "\\00";
        //todo
        if (value.equals("\\n\\00"))
        {
            has_n = true;
        }
        String sub1 = "\\0A";  // 处理 "\0"
        String sub2 = "\\n";   // 处理 "\n"
        String sub3 = "\\\\";  // 处理 "\\"
        String sub4 = "\\09";   // 处理 "\t"
        String sub5 = "\\07";   // 处理 "\a"
        String sub6 = "\\08";   // 处理 "\b"
        String sub7 = "\\11";   // 处理 "\v"
        String sub8 = "\\12";   // 处理 "\f"
        String sub9 = "\\34";  // 处理 "\""
        String sub10 = "\\39";  // 处理 "\'"

        int count = 0;
        int index = 0;


        int i;

        // 处理 "\\\\" (反斜杠)
        while ((i = value.indexOf(sub3, index)) != -1)
        {
            count++;  // 每次找到匹配的字符就计数
            index = i + sub3.length(); // 从上次找到的位置继续查找
        }

        index = 0;
        // 处理 "\\n"
        while ((i = value.indexOf(sub2, index)) != -1)
        {
            count++;
            index = i + sub2.length();
        }

        index = 0;
        // 处理 "\\0A"
        while ((i = value.indexOf(sub1, index)) != -1)
        {
            count += 2;  // 特殊情况："\0A"对应2个字符
            index = i + sub1.length();
        }

        index = 0;
        // 处理 "\\t"
        while ((i = value.indexOf(sub4, index)) != -1)
        {
            count += 2;
            index = i + sub4.length();
        }

        index = 0;
        // 处理 "\\a"
        while ((i = value.indexOf(sub5, index)) != -1)
        {
            count += 2;
            index = i + sub5.length();
        }

        index = 0;
        // 处理 "\\b"
        while ((i = value.indexOf(sub6, index)) != -1)
        {
            count += 2;
            index = i + sub6.length();
        }

        index = 0;
        // 处理 "\\v"
        while ((i = value.indexOf(sub7, index)) != -1)
        {
            count += 2;
            index = i + sub7.length();
        }

        index = 0;
        // 处理 "\\f"
        while ((i = value.indexOf(sub8, index)) != -1)
        {
            count += 2;
            index = i + sub8.length();
        }

        index = 0;
        // 处理 "\\\""
        while ((i = value.indexOf(sub9, index)) != -1)
        {
            count += 2;
            index = i + sub9.length();
        }

        index = 0;
        // 处理 "\\'"
        while ((i = value.indexOf(sub10, index)) != -1)
        {
            count += 2;
            index = i + sub10.length();
        }


        length = length - count;

        this.value = value;
    }

    public ArrayList<String> irOutput()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("@" + name);
        sb.append(" = private unnamed_addr constant [");
        sb.append(length);
        sb.append(" x i8] c\"");
        //sb.append(value + "\\00");
        sb.append(value);
        sb.append("\", align 1\n");
        ArrayList<String> ret = new ArrayList<>();
        ret.add(sb.toString());
        return ret;
    }

    public static String getAName()
    {
        count++;
        if (count == 0)
        {
            return ".str";
        }
        else
        {
            return ".str." + count;
        }
    }

    public String getParam()
    {
        //i8* getelementptr inbounds ([7 x i8], [7 x i8]* @.str, i64 0, i64 0)
        return "i8* getelementptr inbounds ([" + length + " x i8], [" + length + " x i8]* @" + name + ", i64 0,i64 0)";
    }

    public static boolean getHas_n()
    {
        return has_n;
    }

    public static void setIrString(IrString irString)
    {
        IrString.irString = irString;
    }

    public static IrString getIrString()
    {
        return irString;
    }
}
