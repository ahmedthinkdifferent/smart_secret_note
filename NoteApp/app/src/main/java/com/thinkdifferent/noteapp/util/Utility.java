package com.thinkdifferent.noteapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdifferent.noteapp.R;

/**
 * class for application utility methods.
 */

public class Utility {


    /**
     * save user security patten.
     *
     * @param context  caller context.
     * @param password user password.
     */
    public static void saveSecurityPattern(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("password", password).apply();
    }

    /**
     * get saved pattern from shared preferences.
     *
     * @param context activity context.
     * @return saved security pattern or null if not found.
     */
    public static String getSavedPasswordPattern(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("password", null);
    }

    /**
     * show toast message to user.
     *
     * @param context caller context.
     * @param message toast message.
     */
    public static void showToastMessage(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * save integer value in shared prefs.
     *
     * @param context caller context.
     * @param key     key to save value with.
     * @param value   value to save.
     */
    public static void saveIntValueInPrefs(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * get int value from shared prefs.
     *
     * @param context      caller context.
     * @param key          key to get value with
     * @param defaultValue default value if not found value.
     * @return value or default if not found.
     */
    public static int getIntValueFromPrefs(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * set custom font fot views.
     *
     * @param context activity context.
     * @param views   views to set font for it.
     */
    public static void setViewsCustomFont(Context context, TextView... views) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "font.ttf");
        for (TextView view : views) {
            view.setTypeface(font);
        }
    }


    /**
     * open link in browser.
     *
     * @param context caller context.
     * @param link    link to open in browser.
     */
    public static void openLink(Context context, String link) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(link));
        try {
            context.startActivity(newIntent);
        } catch (Exception e) {
            e.printStackTrace();
            showToastMessage(context, R.string.cannot_open_link);
        }
    }

    /**
     * send mail .
     *
     * @param context caller context.
     * @param mail    mail receiver.
     */
    public static void sendMail(Context context, String mail) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.send_mail_chooser)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            showToastMessage(context, R.string.cannot_send_mail);
        }
    }


    /**
     * make a phone call.
     *
     * @param context caller context.
     * @param phone   phone number to call.
     */
    public static void makeCall(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showToastMessage(context, R.string.cannot_make_call);
        }
    }
}
