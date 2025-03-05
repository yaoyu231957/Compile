package frontend.lexer;

import java.util.ArrayList;

public class Tokenlist
{
    private ArrayList<Token> tokenlist = new ArrayList<>();
    private int cur_pos = 0;

    public ArrayList<Token> getTokenlist()
    {
        return tokenlist;
    }

    public int getCur_pos()
    {
        return cur_pos;
    }

    public Token getToken()
    {
        return tokenlist.get(cur_pos);
    }

    public Token getNextToken()
    {
        return tokenlist.get(cur_pos + 1);
    }

    public Token getForwardToken()
    {
        return tokenlist.get(cur_pos - 1);
    }

    public Token getThirdToken()
    {
        return tokenlist.get(cur_pos + 2);
    }

    public void ReadNext()
    {
        cur_pos++;
    }

    public void ReadForward()
    {
        cur_pos--;
    }

    public boolean IsOver()
    {
        if (cur_pos < tokenlist.size())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void ReadForwardNum(int i)
    {
        cur_pos -= i;
    }

    public void ReadBackwardNum(int i)
    {
        cur_pos += i;
    }

    public void ChangeCur_pos(int pos)
    {
        cur_pos = pos;
    }
}
