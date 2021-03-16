package com.panelManagement.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panelManagement.activity.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class Utility {

    private static boolean menuopen;

    /**
     * Checks the validation of email address. Takes pattern and checks the text
     * entered is valid email address or not.
     *
     * @param email : email in string format
     * @return True if email address is correct.
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else if (email.equals("")) {
            return true;
        }
        return false;
    }

    public static Typeface getTypeface(Context context, boolean isRegular) {
        Typeface mtTypefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Bold.otf");
        Typeface mtTypefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Regular.otf");
        if (isRegular)
            return mtTypefaceRegular;
        else
            return mtTypefaceBold;

    }

    public static boolean isConnectedToInternet(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static Long getTimeStamp() {
        Date res = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(res);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long data = calendar.getTimeInMillis();
        return data;
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static Bitmap getRoundedShape(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate.replace(" ", "T");
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void generateKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String value = String.format(Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static String getDeviceId(Context mContext) {
        String android_id = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
        return android_id;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        try {
            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }


    public static String formatDateTimeFromString(Context context, String inputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        try {
            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
            SimpleDateFormat df_output = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

            Date date = new SimpleDateFormat(inputFormat).parse(inputDate);
            String newString = new SimpleDateFormat(Constants.INPUT_DATE_HH_MM_FORMAT).format(date);

            outputDate = outputDate + " " + context.getString(R.string.at) + " " + newString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static void alertDialogCustom(Context mcontext, int textid, boolean isShow) {
        // Create custom dialog object
        try {
            final Dialog dialog = new Dialog(mcontext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // Include dialog.xml file
            dialog.setContentView(R.layout.dialog_custom_alert);

            // set values for custom dialog components - text, image and button
            TextView text = dialog.findViewById(R.id.dialog_txt);
            text.setText(mcontext.getString(textid));

            ImageView iv_close = dialog.findViewById(R.id.iv_close_forgot);
            iv_close.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            if (isShow) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    public static Dialog MandatoryalertDialog(Context context, String textid) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        try {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // Include dialog.xml file
            dialog.setContentView(R.layout.dialog_custom_alert);

            // set values for custom dialog components - text, image and button
            TextView text = dialog.findViewById(R.id.dialog_txt);
            text.setText(textid);

            ImageView iv_close = dialog.findViewById(R.id.iv_close_forgot);
            iv_close.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

        } catch (Exception e) {

        }
        return dialog;
    }

    public static void setMargins(Context context, FrameLayout frameLayout, int marginTop) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, marginTop, 0, getDp(context, 40));
        frameLayout.setLayoutParams(params);
    }

    public static void setLocaleLanguage(String localeCode, Activity mActivity) {
        try {
            if (localeCode.equalsIgnoreCase("")) {
                String value = Locale.getDefault().getLanguage();
                setLanguageCode(value, mActivity);
                return;
            }
            loginSetLanguage(localeCode, mActivity);
            setLanguageCode(localeCode, mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loginSetLanguage(String localeCode, Activity mActivity) {
        Locale myLocale = null;
        switch (localeCode) {
            case "ar-EG":
                myLocale = new Locale("ar");
                break;
            case "de-de":
                myLocale = new Locale("de", "DE");
                break;
            case "fr-FR":
                myLocale = new Locale("fr");
                break;
            case "id-ID":
                myLocale = new Locale("in");
                break;
            case "it-IT":
                myLocale = new Locale("it");
                break;
            case "ko-KR":
                myLocale = new Locale("ko");
                break;
            case "ms-MY":
                myLocale = new Locale("ms");
                break;
            case "pl-PL":
                myLocale = new Locale("pl");
                break;
            case "pt-BR":
                myLocale = new Locale("pt");
                break;
            case "ru-RU":
                myLocale = new Locale("ru");
                break;
            case "th-TH":
                myLocale = new Locale("th");
                break;
            case "fil-PH":
                myLocale = new Locale("Tl");
                break;
            case "tr-TR":
                myLocale = new Locale("TR");
                break;
            case "vi-VN":
                myLocale = new Locale("vi");
                break;
            case "zh-CHS":
                myLocale = new Locale("zh", "CN");
                break;
            case "zh-CN":
                myLocale = new Locale("zh", "CN");
                break;
            case "zh-TW":
                myLocale = new Locale("zh", "TW");
                break;
            case "es-MX":
                myLocale = new Locale("es", "MX");
                break;
            case "es-AR":
                myLocale = new Locale("es", "AR");
                break;
            case "es-CO":
                myLocale = new Locale("es", "CO");
                break;
            case "es-CL":
                myLocale = new Locale("es", "CL");
                break;
            case "zh-CHT":
                myLocale = new Locale("zh", "CN");
                break;
            case "es-ES":
                myLocale = new Locale("es", "MX");
                break;
            case "ar":
                myLocale = new Locale("ar");
                break;
            case "de":
                myLocale = new Locale("de");
                break;
            case "es":
                myLocale = new Locale("es");
                break;
            case "fr":
                myLocale = new Locale("fr");
                break;
            case "in":
                myLocale = new Locale("in");
                break;
            case "it":
                myLocale = new Locale("it");
                break;
            case "ko":
                myLocale = new Locale("ko");
                break;
            case "ms":
                myLocale = new Locale("ms");
                break;
            case "pl":
                myLocale = new Locale("pl");
                break;
            case "pt":
                myLocale = new Locale("pt");
                break;
            case "ru":
                myLocale = new Locale("ru");
                break;
            case "th":
                myLocale = new Locale("th");
                break;
            case "tl":
                myLocale = new Locale("tl");
                break;
            case "tr":
                myLocale = new Locale("tr");
                break;
            case "vi":
                myLocale = new Locale("vi");
                break;
            case "zh":
                myLocale = new Locale("zh");
                break;

            default:
                myLocale = new Locale("en");
        }
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        mActivity.getBaseContext().getResources().updateConfiguration(config, mActivity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static void setLanguageCode(String localeCode, Activity mActivity) {
        switch (localeCode) {
            case "en":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "hi-in");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "hi-in");
                break;
            case "ar":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ar-EG");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ar");
                break;
            case "de":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "de-de");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "de-de");
                break;
            case "es":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-CL");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "es-CL");
                break;
            case "fr":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fr-FR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "fr-FR");
                break;
            case "in":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "id-ID");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "id-ID");
                break;
            case "it":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "it-IT");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "it-IT");
                break;
            case "ko":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ko-KR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ko-KR");
                break;
            case "ms":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ms-MY");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ms-MY");
                break;
            case "pl":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pl-PL");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "pl-PL");
                break;
            case "pt":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pt-BR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "pt-BR");
                break;
            case "ru":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ru-RU");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ru-RU");
                break;
            case "th":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "th-TH");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "th-TH");
                break;
            case "fil":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fil-PH");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "fil-PH");
                break;
            case "tl":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fil-PH");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "fil-PH");
                break;
            case "tr":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "tr-TR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "tr-TR");
                break;
            case "vi":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "vi-VN");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "vi-VN");
                break;
            case "zh-CN":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "zh-CHS");
                break;
            case "zh":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "zh-CHS");
                break;
            case "zh-CHT":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHT");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "zh-CHT");
                break;
            case "ar-EG":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ar-EG");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ar-EG");
                break;
            case "de-de":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "de-de");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "de-de");
                break;
            case "es-CL":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-CL");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "es-CL");
                break;
            case "fr-FR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fr-FR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "fr-FR");
                break;
            case "id-ID":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "id-ID");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "id-ID");
                break;
            case "it-IT":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "it-IT");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "it-IT");
                break;
            case "ko-KR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ko-KR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ko-KR");
                break;
            case "ms-MY":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ms-MY");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ms-MY");
                break;
            case "pl-PL":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pl-PL");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "pl-PL");
                break;
            case "pt-BR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pt-BR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "pt-BR");
                break;
            case "ru-RU":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ru-RU");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "ru-RU");
                break;
            case "th-TH":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "th-TH");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "th-TH");
                break;
            case "fil-PH":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fil-PH");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "fil-PH");
                break;
            case "tr-TR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "tr-TR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "tr-TR");
                break;
            case "vi-VN":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "vi-VN");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "vi-VN");
                break;
            case "zh-CNS":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "zh-CHS");
                break;
            case "zh-TW":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHT");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "zh-CHT");
                break;
            case "es-MX":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-ES");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "es-ES");
                break;
            case "es-ES":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-ES");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "es-ES");
                break;
            case "es-AR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-AR");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "es-AR");
                break;
            case "es-CO":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-CO");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "es-CO");
                break;
            case "zh-CHS":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "zh-CHS");
                break;
            default:
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "hi-in");
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE_VALUE, "hi-in");
        }

    }


    public static String getURL(String url, String countryCode) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(countryCode)) {
            url += "?LanguageCulture=" + countryCode;
        }

        return url;
    }

    public static String getUrlForPuzzle(String url, String userId, String languageCode) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(userId)) {
            url += "?PID=" + userId+"&LanguageCulture=" + languageCode;
        }

        return url;
    }

    public static int getDp(Context context, int dpValue) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * d); // margin in pixels
    }

    public static void close(View lay, Dialog dialog, View v) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.1f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(300);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setEnabled(true);
                dialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rotateAnimation.setFillAfter(true);
        lay.startAnimation(rotateAnimation);
        menuopen = false;
    }

    public static void open(View lay) {
        RotateAnimation rotateAnimation = new RotateAnimation(180f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.1f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        lay.startAnimation(rotateAnimation);
        menuopen = true;
    }

    public static  String getStringFromList(ArrayList<Integer> selectedIDs) {

        String list = "";
        for (int i = 0; i < selectedIDs.size(); i++) {
            if (i == 0) {
                list = list + selectedIDs.get(i);
            } else {
                list = list + "," + selectedIDs.get(i);
            }
        }
        return list;
    }

    public static String getDateFromString(String openingDate, String inputDateType, String formattedString) {
        SimpleDateFormat format = new SimpleDateFormat(inputDateType);
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(openingDate);
            DateFormat targetFormat = new SimpleDateFormat(formattedString);
            //targetFormat.setTimeZone(TimeZone.getTimeZone(CJRParamConstants.INDIAN_TIME_ZONE));
            formattedString = targetFormat.format(date);


            return formattedString;
        } catch (ParseException e) {
            return formattedString;
        }
    }

}