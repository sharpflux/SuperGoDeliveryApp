package com.sharpflux.supergodeliveryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PTBLR-1057 on 11/23/2015.
 */
public class CommonUtils {

    public static void showAlertDialog(final Activity act, String msg, String title) {
        if(act!=null) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);

            AlertDialog.Builder builder = alertDialogBuilder
                    .setTitle(Html.fromHtml("<font color='#635e5b'>" + title + "</font>"))
                    .setMessage(msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            {
                                dialog.cancel();
                            }
                        }
                    });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(false);

            alertDialog.show();
            Button positive_button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Typeface font = Typeface.createFromAsset(act.getAssets(), "fonts/SF-UI-Text-Regular_0.otf");
            positive_button.setTypeface(font, Typeface.BOLD);
        }

    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public final static boolean isValidPhone(String target) {

        if (target.length() == 10) {

            return true;

        } else {

            return false;
        }
    }



    public static void serverErrorDisplay(int statusCode, Activity activity) {

        if (statusCode == 404) {
            showAlertDialog(activity, "Requested resource not found", "Error");

        }
        // When Http response code is '500'
        else if (statusCode == 500) {
            showAlertDialog(activity, "Something went wrong at server end", "Error");

        }
        // When Http response code other than 404, 500
        else {

            showAlertDialog(activity, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running", "Error");

        }

    }

    public static void snackBarMsg(View v, String msg){
        Snackbar snackbar = Snackbar
                .make(v,msg, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }



    public static void displayToastMsg(Activity act, String msg) {

        if(act!=null) {
            Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
        }else{
            Log.e("ActivityError","activity coming null");
        }
    }


    public static GradientDrawable getDrawable(View view){
        GradientDrawable drawable = null;
        if(view != null){
            drawable = (GradientDrawable) view.getBackground().mutate();
            return drawable;
        }
        return drawable;
    }

    public static void requestFocus(View view, Activity activity) {
        if ( view != null && view.requestFocus() && activity != null) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public static void makeRequiredFeildValidation(String msg, EditText editText, Activity activity){
        if(activity != null && editText != null && msg != null && TextUtils.isEmpty(msg) || msg.isEmpty()){
            editText.setError(msg);
            requestFocus(editText,activity);
        }
    }

    public static String changeDateFormat(String date, String inputPattern, String outputPattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(inputPattern);
            Date dateObj;
            dateObj = sdf.parse(date);
            String resultantdate = new SimpleDateFormat(outputPattern).format(dateObj);
            return resultantdate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public  static  Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = null;
        try {

            output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);

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
        } catch (Exception e){
            e.printStackTrace();
        }

        return output;

    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


}
