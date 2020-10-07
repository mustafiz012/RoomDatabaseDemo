package com.musta.roomdemo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.musta.roomdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This is a thread safe singleton class
 * */
public class Singling {

    private static final String TAG = "Singling";
    private static volatile Singling instance;
    private static AlertDialog alertDialog;
    private static ProgressDialog progressDialog;
    private boolean doubleBackToExitPressedOnce = false;

    private Singling() {
        if (instance != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class");
    }

    public static Singling getInstance() {
        if (instance == null) {
            synchronized (Singling.class) {
                if (instance == null) {
                    instance = new Singling();
                }
            }
        }
        return instance;
    }

    public static void hideKeyboard(Activity activity) {
//        Timber.i( "hideKeyboard: ");
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void hideDrawerKeyboard(View view, Activity activity) {
//        Timber.i( "hideDrawerKeyboard: ");
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setWarningDialog(Context context, String title, String message) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok),
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public static boolean isInvalidMobile(String phone) {
        String regexStr = "^(?:\\+?88)?01[3-9]\\d{8}$";
        if (Pattern.matches(regexStr, phone)) {
            return phone.length() != 11;
        }
        return true;
    }

    public static boolean isInvalidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    /**
     * Password validation using regex
     *
     * @param forceSpecialChar
     * @param forceCapitalLetter
     * @param forceNumber
     * @param minLength
     * @param maxLength
     * @param password
     * @return
     */
    public static boolean isValidPassword(boolean forceSpecialChar,
                                          boolean forceCapitalLetter,
                                          boolean forceNumber,
                                          int minLength,
                                          int maxLength,
                                          String password) {

        StringBuilder patternBuilder = new StringBuilder("((?=.*[a-z])");

        if (forceSpecialChar) {
            patternBuilder.append("(?=.*[@#$%])");
        }

        if (forceCapitalLetter) {
            patternBuilder.append("(?=.*[A-Z])");
        }

        if (forceNumber) {
            patternBuilder.append("(?=.*[0-9])");
        }

        patternBuilder.append(".{").append(minLength).append(",").append(maxLength).append("})");
        String PASSWORD_PATTERN = patternBuilder.toString();
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        return !TextUtils.isEmpty(password) && pattern.matcher(password).matches();
    }

    public static void setWarningDialogWithAutoDismissal(Context context, String title, String message) {
        if (alertDialog != null && alertDialog.isShowing()) {
            return;
        }
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok),
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public static boolean isWarningDialogShowing() {
        return alertDialog != null && alertDialog.isShowing();
    }

    public static void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public static List<String> getSingleLines(String urlsString, String regex) {
        List<String> resultantList = new ArrayList<>();
        for (String url : urlsString.split(regex)) {
            resultantList.add(url.trim());
        }
//        Collections.addAll(resultantList, urlsString.split(regex));
        return resultantList;
    }

    /**
     * Generates list of strings from string
     *
     * @param urlsString master string
     * @param regex      regex
     * @param mediaDir   a prefix to identify some directory
     * @return list of strings
     */
    public static List<String> getSingleLines(String urlsString, String regex, String mediaDir) {
        List<String> resultantList = new ArrayList<>();
        for (String url : urlsString.split(regex)) {
            resultantList.add(mediaDir + url.trim());
        }
//        Collections.addAll(resultantList, urlsString.split(regex));
        return resultantList;
    }

    public static int getPercentageValue(double total, int percentage) {
        float valueFromPercentage = (float) ((total * percentage) / 100);
        return Math.round(valueFromPercentage);
    }

    public static int getPercentageToValue(double total, int percentage) {
        float valueFromPercentage = (float) ((total * percentage) / 100);
        return Math.round(valueFromPercentage);
    }

    public static Date getDateOfNDaysAgo(int numberOfDaysAgo) {
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDaysAgo);
        return calendar.getTime();
    }

    public static int getDayNumber(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDateTime(String transactionTime, String pattern) {
        String dateOfCartSaving = "";
        String timeStamp = transactionTime;
        transactionTime = timeStamp.substring(0, timeStamp.indexOf(' ')) + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            Date date = sdf.parse(transactionTime);
            SimpleDateFormat formatter = new SimpleDateFormat("dd", Locale.getDefault());
            String newFormatDay = formatter.format(date);
            formatter = new SimpleDateFormat("MMM", Locale.getDefault());
            String newFormatMonths = formatter.format(date);
            formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
            String newFormatYear = formatter.format(date);
//            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String time = timeStamp.substring(timeStamp.indexOf(' ') + 1);
            dateOfCartSaving = time + ", " + newFormatMonths.toUpperCase() + " " + newFormatDay + ", " + newFormatYear;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dateOfCartSaving;
    }

    public static String getFormattedDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }

    public static Date getDateAt00(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        try {
            return sdf.parse(sdf.format(now.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param low
     * @param high
     * @return This gives you a random number in between @param low (inclusive) and @param high (exclusive)
     */
    public static int getRandomNumberBetween(int low, int high) {
        return new Random().nextInt(high - low) + low;
    }

    public static Date getDateFromString(String stringDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static void setCursorColor(EditText editText, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(editText);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(editText);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(editText.getContext(), drawableResId);
            Objects.requireNonNull(drawable).setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    public static Drawable getAttributeDrawable(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        int drawableRes = typedValue.resourceId;
        Drawable drawable = null;
        try {
            drawable = context.getResources().getDrawable(drawableRes);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static int getAttributeColor(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        int colorRes = typedValue.resourceId;
        int color = -1;
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Not found color resource by id: " + colorRes);
        }
        return color;
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    public static void showSettingsDialog(Context context, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(context, activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private static void openSettings(Context context, Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Find visible fragment
     */
    public static Fragment getVisibleFragment(FragmentActivity mActivity) {
        Fragment fragment = null;
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        for (Fragment frag : fragmentManager.getFragments()) {
            if (frag.isVisible()) {
                fragment = frag;
                break;
            }
        }
        return fragment;
    }

    public static void showProgressDialog(Context context, String message) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
            }

            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } catch (Exception ignored) {
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static boolean copyTextToClipboard(Context context, String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", textToCopy);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            return true;
        } else {
            return false;
        }
    }

    public static String getTwoDigitsValue(int input) {
        String output;
        if (input < 10) {
            output = "0" + input;
        } else {
            output = "" + input;
        }
        return output;
    }

    /**
     * SR or Distributor phone number save in phone contacts.
     *
     * @param srName      SR name for saving contact
     * @param phoneNumber SR phone number
     */
    public void addToContact(FragmentActivity mActivity, String srName, String phoneNumber) {

        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        operationList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // first and last names
        operationList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "" + srName)
                //.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "First Name")
                .build());

        operationList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "" + phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        operationList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                //.withValue(ContactsContract.CommonDataKinds.Email.DATA, "abc@xyz.com")
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        try {
            ContentProviderResult[] results = mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getQuestionNumber(String questionNumber) {
        int qNumber;
        qNumber = Integer.parseInt(questionNumber.split(Pattern.quote("."))[1]);
        return qNumber;
    }

    public TextView setFreeTextView(Context context, LinearLayout parentLayout) {
        TextView textView = new TextView(context);
        parentLayout.addView(textView);
        return textView;
    }

    public void showSnackBar(Activity activity, String message) {
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent));
        snackbar.show();
    }

    public void showAppExitToast(Activity activity, Context context) {
        if (doubleBackToExitPressedOnce) {
            activity.moveTaskToBack(true);
        }
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
