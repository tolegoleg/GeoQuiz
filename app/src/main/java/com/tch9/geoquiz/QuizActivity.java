package com.tch9.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.tch9.geoquiz.CheatActivity.newIntent;

public class QuizActivity extends AppCompatActivity
{
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT_CHEATS = "count_cheats";
    private static final String KEY_ANSWER_ARR = "answer_arr";
    private static final String KEY_SOLUTION_ARR = "solution_arr";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Button mCheatButton;
    private Button mExitButton;
    private ImageButton mPrevImButton;
    private ImageButton mNextImButton;
    private TextView mQuestionTextView;
    private TextView mCountCheatsTextView;

    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_01, false),
                    new Question(R.string.question_02, true),
                    new Question(R.string.question_03, false),
                    new Question(R.string.question_04, false),
                    new Question(R.string.question_05, false),
                    new Question(R.string.question_06, true),
                    new Question(R.string.question_07, false),
                    new Question(R.string.question_08, true),
                    new Question(R.string.question_09, false),
                    new Question(R.string.question_10, false)
            };
    private boolean Answer[] = {false, false, false, false, false, false, false, false, false, false};
    private boolean Solution[] = {false, false, false, false, false, false, false, false, false, false};

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private int mCountCheats = 3;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT)
        {
            if (data == null)
            {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShonw(data);
            if (mCountCheats > 0)
            {
                mCountCheats--;
                mCountCheatsTextView.setText("Осталось " + Integer.toString(mCountCheats));
                if (mCountCheats == 0)
                {
                    mCheatButton.setEnabled(false);
                }
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "OnStart() called!");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop() called!");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() called!");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause() called!");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called!");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState !!!");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_ANSWER_ARR, Answer);
        savedInstanceState.putBooleanArray(KEY_SOLUTION_ARR, Solution);
        savedInstanceState.putInt(KEY_COUNT_CHEATS, mCountCheats);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate(Bundle) called!");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            Answer = savedInstanceState.getBooleanArray(KEY_ANSWER_ARR);
            Solution = savedInstanceState.getBooleanArray(KEY_SOLUTION_ARR);
            mCountCheats = savedInstanceState.getInt(KEY_COUNT_CHEATS, 3);
        }

        mCountCheatsTextView = (TextView) findViewById(R.id.count_cheats_textview);
        mCountCheatsTextView.setText("Осталось " + Integer.toString(mCountCheats));

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mCheatButton = findViewById(R.id.cheat_button);
        if (mCountCheats == 0)
        {
            mCheatButton.setEnabled(false);
        }
        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Start CHEAT activity
                boolean answer_is_true = mQuestionBank[mCurrentIndex].IsAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answer_is_true);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast T = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
                //T.setGravity(Gravity.BOTTOM, 0, 0);
                //T.show();
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ////Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
                //Toast T = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
                //T.setGravity(Gravity.BOTTOM, 0, 0);
                //T.show();
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mCurrentIndex > 0)
                {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }
                else
                {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevImButton = (ImageButton) findViewById(R.id.prev_imbutton);
        mPrevImButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mCurrentIndex > 0)
                {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }
                else
                {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                mIsCheater = false;
                updateQuestion();
            }
        });

        mNextImButton = (ImageButton) findViewById(R.id.next_imbutton);
        mNextImButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    private void updateQuestion()
    {
        int question = mQuestionBank[mCurrentIndex].GetTextResId();
        mQuestionTextView.setText(question);
        if (Answer[mCurrentIndex])
        {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            if (Solution[mCurrentIndex])
                mQuestionTextView.setBackgroundResource(R.color.colorMint);
            else
                mQuestionTextView.setBackgroundResource(R.color.colorMalinov);
        }
        else
        {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
            mQuestionTextView.setBackgroundResource(R.color.colorOrange);
        }

        checkExam();
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        Answer[mCurrentIndex] = true;
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        String toast;

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].IsAnswerTrue();

        if (userPressedTrue == answerIsTrue)
        {
            toast = getString(R.string.correct_toast);
            Solution[mCurrentIndex] = true;
            mQuestionTextView.setBackgroundResource(R.color.colorMint);
        }
        else
        {
            toast = getString(R.string.incorrect_toast);
            Solution[mCurrentIndex] = false;
            mQuestionTextView.setBackgroundResource(R.color.colorMalinov);
        }

        if (mIsCheater)
        {
            toast += "\n" + getResources().getString(R.string.jugment_toast);
        }

        Toast T = Toast.makeText(QuizActivity.this, toast, Toast.LENGTH_SHORT);
        T.setGravity(Gravity.TOP, 0, 0);
        T.show();

        checkExam();
    }

    private void checkExam()
    {
        for (int c = 0; c < mQuestionBank.length; c++)
        {
            if (!Answer[c])
                return;
        }
        int r = 0;
        for (int c = 0; c < mQuestionBank.length; c++)
        {
            if (Solution[c])
                r++;
        }
        r = (int)(((float)r / (float)mQuestionBank.length) * 100);
        String s = "Экзамен окончен!\n" + r + "% успеха!";
        if (r <= 80)
            s = s + "\nНу ты и ДВОЕЧНИК!!!";
        Toast T = Toast.makeText(QuizActivity.this,  s, Toast.LENGTH_LONG);
        T.setGravity(Gravity.BOTTOM, 0, 0);
        T.show();
    }

    public void OnExitButtonClick (View view)
    {
        this.finish();
    }
}
