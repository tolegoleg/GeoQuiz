package com.tch9.geoquiz;

public class Question
{
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue)
    {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int GetTextResId()
    {
        return mTextResId;
    }

    public boolean IsAnswerTrue()
    {
        return mAnswerTrue;
    }

}
