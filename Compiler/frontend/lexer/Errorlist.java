package frontend.lexer;

import java.util.ArrayList;

public class Errorlist
{
    // 创建一个静态实例
    private static final Errorlist errorlist = new Errorlist();
    private ArrayList<Errors> errors = new ArrayList<>();

    // 私有构造函数，防止外部实例化
    private Errorlist()
    {
    }

    // 提供一个公共的静态方法，返回该实例
    public static Errorlist getErrorlist()
    {
        return errorlist;
    }

    public ArrayList<Errors> getErrors()
    {
        return errors;
    }

    public void AddError(Errors error)
    {
        int i = 0;
        while (i < errorlist.getErrors().size() && error.getLine() >= errorlist.getErrors().get(i).getLine())
        {
            i++;
        }
        errorlist.getErrors().add(i, error);
    }
}
