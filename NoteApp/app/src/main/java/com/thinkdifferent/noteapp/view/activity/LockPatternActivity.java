package com.thinkdifferent.noteapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.thinkdifferent.noteapp.R;
import com.thinkdifferent.noteapp.util.Utility;

/**
 * activity to show pattern lock to user.
 */
public class LockPatternActivity extends AppCompatActivity {

    // variables.
    private boolean needChangePattern, isOldPatternCorrect;
    private String securityPattern;
    private PinLockView mPinLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_pattern);
        securityPattern = Utility.getSavedPasswordPattern(this);
        initializeViews();
    }

    /**
     * initialize views from layout and set data to views.
     */
    private void initializeViews() {
        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        TextView infoTextView = (TextView) findViewById(R.id.tv_pattern_info);
        final Button changePatternButton = (Button) findViewById(R.id.btn_change_code);
        // set custom font for views.
        Utility.setViewsCustomFont(this, infoTextView, changePatternButton);

        if (securityPattern != null) {
            // user saved his pattern before.
            changePatternButton.setClickable(true);
            changePatternButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    needChangePattern = true;
                    setInfoTextViewText(R.string.add_old_pattern);
                }
            });
        } else {
            // user not has security pattern before.
            findViewById(R.id.btn_change_code).setVisibility(View.INVISIBLE);
            setInfoTextViewText(R.string.add_pattern);
        }
    }


    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            if (needChangePattern) {
                if (securityPattern.equals(pin)) {
                    // correct pattern.
                    isOldPatternCorrect = true;
                    setInfoTextViewText(R.string.add_new_pattern);
                    mPinLockView.resetPinLockView();
                } else if (isOldPatternCorrect) {
                    // change pattern , save new pattern.
                    Utility.showToastMessage(LockPatternActivity.this, R.string.pattern_changed);
                    savePattern(pin);
                    startNoteActivity();
                } else {
                    // wrong password.
                    showSnackBar(R.string.wrong_pattern);
                    mPinLockView.resetPinLockView();
                }
            } else {
                // user enter pattern.
                if (securityPattern == null) {
                    // set password for first time.
                    Utility.showToastMessage(LockPatternActivity.this, R.string.pattern_saved);
                    savePattern(pin);
                    startNoteActivity();
                } else {
                    // check if pattern is true of false.
                    if (securityPattern.equals(pin)) {
                        // true pattern.
                        startNoteActivity();
                    } else {
                        // wrong pattern.
                        showSnackBar(R.string.wrong_pattern);
                        mPinLockView.resetPinLockView();
                    }
                }
            }
        }

        @Override
        public void onEmpty() {
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };


    /**
     * save security pattern in shared preferences.
     *
     * @param pin pattern that user entered.
     */
    private void savePattern(String pin) {
        Utility.saveSecurityPattern(LockPatternActivity.this, pin);
    }

    /**
     * start note activity and finish current activity.
     */
    private void startNoteActivity() {
        startActivity(new Intent(LockPatternActivity.this, NotesActivity.class));
        finish();
    }


    /**
     * set text to infoTextView.
     *
     * @param text text to show inside textView.
     */
    private void setInfoTextViewText(int text) {
        ((TextView) findViewById(R.id.tv_pattern_info)).setText(text);
    }


    /**
     * show snack bar
     *
     * @param textInfo text message info.
     */
    private void showSnackBar(int textInfo) {
        Snackbar.make(findViewById(R.id.layout_pattern), getString(textInfo), Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
            }
        }).show();
    }

}
