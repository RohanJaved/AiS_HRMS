/*
 *
 *   Created By Adnan Bashir Manak on 11/30/21, 12:43 PM
 *   Last modified: 10/20/21, 6:40 PM
 * /
 */

package com.appinsnap.aishrm.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.print.PrintHelper;

import com.appinsnap.aishrm.BuildConfig;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*******************************************************************************
 *
 *   Created By Adnan Bashir Manak on 11/30/21, 12:44 PM
 *   Last modified: 08/03/2022, 12:43 PM
 * /
 ******************************************************************************/
@Keep
public class MyHelperClass {
    public MyHelperClass() {
    }
    static MyHelperClass myHelperClass;
    public static MyHelperClass getInstance(){
        if (myHelperClass==null)
        {
            myHelperClass=new MyHelperClass();
            return myHelperClass;
        }else
        {
            return myHelperClass;
        }
    }




    public String getYYYYMMDD(String date) {
        String d1 = date.replace("-", "");
        String d2 = d1.replaceAll("/", "");

        return d2.trim();
    }
    public static String formatDate(int selectedDay, int selectedMonth, int selectedYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
    public static String formatTimeIn24Format(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


    public String removeSpecialCharectersAndTrim(String str) {
        String d1 = str.replaceAll("[^a-zA-Z0-9]", "");

        return d1.trim();
    }

    public String removeSpecialCharectersAndAddSpace(String str) {
        String d1 = str.replaceAll("[^a-zA-Z0-9]", " ");
        return d1.trim();
    }

    public boolean checkSpecialChar(String str) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(str);

        boolean b = m.find();
        if (b)
            return true;
        else {
            return false;
        }
    }

    public static String getCurrentDateHHMMSS_24() {
        String currentDateAndTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return currentDateAndTime;


    }



    public  boolean compareDates(String psDate1, String psDate2) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy/dd/MM");
        Date date1 = dateFormat.parse(psDate1);
        Date date2 = dateFormat.parse(psDate2);
        if(date2.after(date1)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentDateHHMMSS_12() {
        String currentDateAndTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String[] str1 = currentDateAndTime.split(":");
        String newdate = "";
        String hours = "";
        if (Integer.valueOf(str1[0]) <= 12) {
            hours = str1[0];
            newdate = hours + ":" + str1[1] + ":" + str1[2] + " " + "am";
        } else {
            hours = String.valueOf(Integer.valueOf(str1[0]) - 12);
            newdate = hours + ":" + str1[1] + ":" + str1[2] + " " + "pm";
        }

        return newdate;


    }
    public static String convertDateFormatyyyymmdd(String inputDateString) {
        // Specify the input format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            // Parse the input string to a Date object
            Date date = inputDateFormat.parse(inputDateString);

            // Specify the output format
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Format the Date object to a string in the desired format
            return outputDateFormat.format(date);

        } catch (ParseException e) {
            // Handle parsing exception, if any
            e.printStackTrace();
            return null; // Or throw an exception or return an error message based on your use case
        }
    }

    public static String getCurrentDateTimeInUTC() {
        // Get the current date and time in UTC
        Date currentDate = new Date();

        // Format the Date as a string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        return sdf.format(currentDate);
    }
    public static String convertToAMPM_24to12(String currentDateAndTime) {
        String[] str1 = currentDateAndTime.split(":");
        String newdate = "";
        String hours = "";
        if (Integer.valueOf(str1[0]) < 12) {
            hours = str1[0];
            newdate = hours + ":" + str1[1] +  " " + "AM";
        } else {
            hours = String.valueOf(Integer.valueOf(str1[0])-12);
            if(hours.equals("0"))
            {
                hours = "12";
                newdate = hours + ":" + str1[1] +  " " + "PM";
            }
            else{
                newdate = hours + ":" + str1[1] +  " " + "PM";
            }

        }

        return newdate;


    }

    public static String convertDateFormatYYYYMMDD(String inputDate) {
        // Define the input and output date formats
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the input date and format it to the desired output format
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        String outputDate = date.format(outputFormatter);

        return outputDate;
    }
    public static String convertDateFormatYYYYMMDDDashboard(String inputDate) {
        // Define the input and output date formats
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the input date and format it to the desired output format
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        String outputDate = date.format(outputFormatter);
        return outputDate;
    }
    public static String getCurrentTimeInISO8601Format() {
        // Get the current time in UTC
        ZonedDateTime currentTime = ZonedDateTime.now();

        // Define the desired time format using ThreeTen Backport
        org.threeten.bp.format.DateTimeFormatter formatter =
                org.threeten.bp.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSX");

        // Format the time using the formatter
        return currentTime.format(formatter);
    }
    public static String convertDateFormatYYYYMMDD3(String inputDate) {
        // Define the input and output date formats
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the input date and format it to the desired output format
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        String outputDate = date.format(outputFormatter);

        return outputDate;
    }
    public static String convertDateFormatYYYYMMDD2(String inputDate) {
        // Define the input and output date formats
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the input date and format it to the desired output format
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        String outputDate = date.format(outputFormatter);

        return outputDate;
    }
    public static String getCurrentTimeAMPM()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date currentTime = new Date();
        String formattedTime = dateFormat.format(currentTime);
        return formattedTime;
    }
    public static String formatTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(cal.getTime());
    }

   public static String formatTimeHHmmssSSS(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static String getCurrentDateYYYYMMDD() {
        String currentDateAndTime = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        return currentDateAndTime;
    }


    public static String convertTimeFormatCurrent(String inputTime) {
        try {
            // Parse input time
            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(inputTime);

            // Format output time
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid time format";
        }
    }

    public String convertCurrDateToString()
    {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Define a custom date formatter
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        // Format LocalDate to String
        String dateString = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = currentDate.format(formatter);
        }
        return dateString;
    }
    public static String getCurrentDateMMDDYYYY() {
        String currentDateAndTime = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        return currentDateAndTime;
    }
    public static String getCurrentDateYYYYMMDDFormat() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Format the LocalDate as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
    public static String getCurrentDateDDMMYYYY() {
        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Format the current date
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

    public static String getDateTimeDDMMYYYY(String originalDateTime) {
        // Define the desired date format
        DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(originalDateTime, originalFormatter);

        // Format the date and time in the desired output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        String formattedDateTime = dateTime.format(outputFormatter);
        return formattedDateTime;
    }


    public static String arrangeMonthSecond(String inputDate) {
        String[] dateParts = inputDate.split("/");

        if (dateParts.length == 3) {
            // Check if "12" is already in the second position
            if (dateParts[1].equals("12")) {
                return String.join("/", dateParts);
            } else if (dateParts[0].equals("12")) {
                // Swap day and month positions
                String day = dateParts[0];
                dateParts[0] = dateParts[1];
                dateParts[1] = day;

                // Join the parts back together
                return String.join("/", dateParts);
            }
        }

        // Return an error message for invalid input
        return "Invalid date format";
    }
    public static String convertDateFormatDDMMYYYY(String inputDate) {
        try {
            // Parse the input date with the original format
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = originalFormat.parse(inputDate);

            // Format the date with the new format
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Invalid date format";
        }
    }




    public static String convertDateFormatddMMyyyyWithDashes(String inputDate) {
        try {
            // Parse the input date with the original format
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = originalFormat.parse(inputDate);

            // Format the date with the new format
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Invalid date format";
        }
    }


    public static String getCurrentDateYYYYMMDD_Dashes() {
        String currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return currentDateAndTime;

    }

    public static String convertToYYYYMMDD(String inputDate) {
        try {
            // Parse the input date with the original format
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = originalFormat.parse(inputDate);

            // Format the date with the new format
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Invalid date format";
        }
    }
    public static String convertToTargetFormatZoneOffset(String inputTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        LocalDateTime localDateTime = LocalDateTime.parse(inputTime, inputFormatter);
        String formattedTime = localDateTime.format(outputFormatter);

        // Convert to UTC and append Z to indicate Zulu time (UTC)
        return formattedTime + ZoneOffset.UTC.getId();
    }
    public static String convertTimeFormatToAMPM(String inputTime) {
        try {
            // Parse the input time with the original format
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm:ss");
            Date time = originalFormat.parse(inputTime);

            // Format the time with the new format
            SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm a");
            return newFormat.format(time);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Invalid time format";
        }
    }
    public static String convertTimeFormatFromAmPm(String inputTime) {
        // Specify the input format
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("hh:mm a");

        // Specify the output format
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            // Parse the input time string to a Date object
            Date date = inputTimeFormat.parse(inputTime);

            // Format the Date object to a string in the desired output format
            return outputTimeFormat.format(date);

        } catch (ParseException e) {
            // Handle parsing exception, if any
            e.printStackTrace();
            return null; // Or throw an exception or return an error message based on your use case
        }
    }
//    public static int convertStringToMinutes(String hoursString) {
//        try {
//            // Parse the string to a BigDecimal
//            BigDecimal hours = new BigDecimal(hoursString);
//
//            // Convert hours to minutes with BigDecimal for precision
//            BigDecimal minutesDecimal = hours.multiply(BigDecimal.valueOf(60));
//
//            // Round off the minutes to the nearest integer using BigDecimal
//            BigDecimal roundedMinutes = minutesDecimal.setScale(0, RoundingMode.HALF_UP);
//
//            // Return the rounded minutes as an integer
//            return roundedMinutes.intValue();
//        } catch (NumberFormatException e) {
//            // Handle the case where the input string is not a valid number
//            e.printStackTrace();
//            return -1; // or throw an exception or handle the error based on your use case
//        }
//    }
public static int convertStringToMinutes(String input) {
    int totalMinutes = 0;

    // Use regular expression to extract hours and minutes
    Pattern pattern = Pattern.compile("(\\d+)\\s*hour");
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
        totalMinutes += Integer.parseInt(matcher.group(1)) * 60;
    }

    pattern = Pattern.compile("(\\d+)\\s*min");
    matcher = pattern.matcher(input);

    if (matcher.find()) {
        totalMinutes += Integer.parseInt(matcher.group(1));
    }
    return totalMinutes;
}

    public static String getCurrentTime() {
        // Define the desired time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Format the current time
        String formattedTime = currentTime.format(formatter);

        return formattedTime;
    }
    public static String getCurrentTimeHHmmssSSS() {
        // Define the desired time format
        LocalTime currentTime = LocalTime.now();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        // Format the current time
        String formattedTime = currentTime.format(formatter);

        return formattedTime;
    }

    public static String convertToHoursAndMinutes(double inputHours) {
        if (inputHours < 1) {
            // Convert less than 1 hour to minutes
            int totalMinutes = (int) Math.round(inputHours * 60);
            return totalMinutes + " minutes";
        } else {
            // Convert 1 hour or more to hours and minutes
            int hours = (int) inputHours;
            int minutes = (int) Math.round((inputHours - hours) * 60);

            if (minutes == 0) {
                return hours + " hour";
            } else {
                return hours + " hours " + minutes + " minutes";
            }
        }
    }
    public static String getTimeString(int hours, int minutes) {
        if (hours == 0 && minutes < 60) {
            return minutes + " mins";
        } else {
            return hours + " hour" + (hours > 1 ? "s" : "") + " " + minutes + " mins";
        }
    }




    public String getRandom15() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Random rnd = new Random();
        int number = rnd.nextInt(999999999);
        String ninedigitrandom = String.format("%09d", number);

        String finalKey = ninedigitrandom + formattedDate.replaceAll(":", "");
        return finalKey;
    }

    public static String getCurrentDateYYYYMMDDhhmmsss() {
        String currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return currentDateAndTime;
    }
    public static  String getCurrentTimeIn24Format() {
        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Define the desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        // Format the current time
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }

    public String removeSpecialCharectersAndAmPmFromTime(String time) {
        String t1 = time.replaceAll(":", "");
        String t2 = t1;
        if (t2.contains("am") || t2.contains("AM")) {
            t2 = time.toLowerCase().replaceAll("am", "");
        }
        if (t2.contains("pm") || t2.contains("PM")) {
            t2 = time.toLowerCase().replaceAll("pm", "");
        }
        return t2.trim();
    }

    public String getHtmlFormatedText(String message) {

        String htmltextstart = "<HTML>";
        String htmltextclose = "</HTML>";
        String bodytextstart = "<body>";
        String bodytextclose = "</body>";

        return htmltextstart + bodytextstart + message + bodytextclose + htmltextclose;

    }

    public String toBase64(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 3508, 2480, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public String getTravelModedriving() {


        return ",13z/data=!3m1!4b1!4m2!4m1!3e0";
    }

    public String getTravelModewalking() {


        return ",14z/data=!3m1!4b1!4m2!4m1!3e2";
    }

    public String getTravelModebicycling() {


        return ",14z/data=!3m1!4b1!4m2!4m1!3e2";
    }

    public String getTravelModetransit() {


        return ",12z/data=!3m1!4b1!4m2!4m1!3e3";
    }

    public long timeToMillis_yyyymmddhhmmss(String yyyymmddhhmmss) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(yyyymmddhhmmss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    public boolean cnicValidate(String txtNic) {
        if (!(txtNic.matches("^[0-9]{9}[vVxX]$"))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean emailValidate(String email) {
        if (!(email.matches("^(.+)@(.+)$"))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getDeviceInfo() {
        // tested in android 10
        String uniquePseudoID = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        String serial = Build.getRadioVersion();
        String uuid = new UUID(uniquePseudoID.hashCode(), serial.hashCode()).toString();
        String brand = Build.BRAND;
        String modelno = Build.MODEL;
        String version = Build.VERSION.RELEASE;
        return "UUID:" + uuid + "\n" +
                "Brand:" + brand + "\n" +
                "Model:" + modelno + "\n" +
                "version:" + version + "\n" +
                "uniquePseudoId:" + uniquePseudoID;


    }

//    public  boolean isRooted() {
//
//        // get from build info
//        String buildTags = android.os.Build.TAGS;
//        if (buildTags != null && buildTags.contains("test-keys")) {
//            return true;
//        }
//
//        // check if /system/app/Superuser.apk is present
//        try {
//            File file = new File("/system/app/Superuser.apk");
//            if (file.exists()) {
//                return true;
//            }
//        } catch (Exception e1) {
//            // ignore
//        }
//
//        // try executing commands
//        return canExecuteCommand("/system/xbin/which su")
//                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
//    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }


    public   boolean isRooted() {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = { "/sbin/", "/system/bin/", "/system/xbin/",
                    "/data/local/xbin/", "/data/local/bin/",
                    "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/" };
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;

                    break;
                }
            }
        }
        return found;
    }



    public void shareOnWhatsapp(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        context.startActivity(sendIntent);
    }

    public void copyText(Context context, String text, String tag) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(tag, text);
        clipboard.setPrimaryClip(clip);
    }

    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void log(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.println(Log.DEBUG,tag, ".\n#########################################\n" +
                    "##\n" +
                    "##         TAG     : " + tag + "" +
                    "\n##\n" +
                    "##         Time    : " + getCurrentDateHHMMSS_12() + "" +
                    "\n##\n" +
                    "##         Message :" + message + "" +
                    "\n##\n" +
                    "##\n" +
                    "##\n" +
                    "############################################")
            ;
        }
    }

    public void toast(Context context, int duration, String message) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setText(message);
        toast.show();

    }

    public void customToast(Context context, View layout) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    public boolean isPrime(int n) {
        boolean result = false;
        if (n == 2) {
            result = true;
        }
        int squareRoot = (int) Math.sqrt(n);
        for (int i = 1; i <= squareRoot; i++) {
            if (n % i == 0 && i != 1) {
                result = false;
            }
            result = true;
        }
        return result;
    }

    public boolean validateEditText(List<EditText> editTextList) {
        boolean result = false;
        for (int i = 0; i < editTextList.size(); i++) {
            if (editTextList.get(i).getText().toString().trim().equals("")) {
                result = true;
//                kjhkjlkj(context, kjhkjlkj.LENGTH_SHORT, " Required fields are not empty !");
                i = editTextList.size();
            }
        }

        return result;

    }

    private void printPhoto(Context context, Bitmap image) {
        PrintHelper photoPrinter = new PrintHelper(context);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("droids.jpg - photo print", image);
        log("PhotoPrint", "Application work is done !");
    }

    public String removeEndoflineandnextline(String str) {

        str = str.replaceAll("([\\r\\n])", "");
        return str;
    }
    // Context context , Activity activity e.g MainActivity.this , Permission -> Manifest.permission.WRITE_EXTERNAL_STORAGE , requestCode int=12 etc
    public void askForPermission(Context c, Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(c, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                LoggerGenratter.getInstance().showToast(activity,"Please grant the requested permission to get your task done!");


                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        }
    }
    public void setImageTint(ImageView imageView,Context context,int color){
        imageView.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
    public void setVectorDrawableTint(ImageView imageView,Context context,int color){
        imageView.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
    }
    // don't forget to get permission
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            kjhkjlkj(context,kjhkjlkj.LENGTH_SHORT,"Message Send");
        } catch (Exception ex) {

//            kjhkjlkj(context,kjhkjlkj.LENGTH_SHORT,ex.getMessage());
            ex.printStackTrace();
        }
    }


    public boolean deleteRecursive(Context context) {
        String filePath = context.getFilesDir().getPath();
        boolean result=false;

        //Create androiddeft folder if it does not exist
        File fileOrDirectory = new File(filePath);

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {

                child.delete();
            }
        }else {
            log("Delete pdf","no directory found");
            return false;
        }

        if (fileOrDirectory.listFiles().length<2)
        {
            result=true;
        }else {
            result=false;
        }


        return  result;

    }
    private String getInstalledVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionCode = info.versionCode;
        return info.versionName;
    }
    public void installAPK(String PATH,Context context) {
        File file = new File(PATH);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(context, new File(PATH)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                LoggerGenratter.getInstance().printLog("Download File", "Error in opening the file!");

            }
        } else {
            LoggerGenratter.getInstance().showToast(context,"Installing");

        }
    }
    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static boolean isValidPhone(String value) {
        //TODO: Replace this with your own logic
        return !value.startsWith("03");
    }


    public static boolean isValidPhonemax(String value) {
        //TODO: Replace this with your own logic
        return value.length() >= 11 && value.length() <= 11;
    }
    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-","");
        return uuid;
    }
    public static String encodeTextBase64(String value){
        if (value == null)
            value = "";
        return Base64.encodeToString(value.trim().getBytes(), Base64.DEFAULT);
    }
    public static String decodeTextBase64(String encodedString) {
        byte[] bytes = Base64.decode(encodedString,Base64.DEFAULT);
        return new String(bytes);
    }
    public static String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(m);
    }
    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap!=null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded;
        }else
        {
            return "";
        }
    }


    public Bitmap createBitmapFromLayout(View tv) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(spec, spec);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(tv.getMeasuredWidth(), tv.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate((-tv.getScrollX()), (-tv.getScrollY()));
        tv.draw(c);
        return b;
    }
    public  Bitmap getBitmapFromView(View view, int totalHeight,
                                     int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,
                totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        return returnedBitmap;
    }
    public void shareMedia(Context context,Bitmap bitmap) {
        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"", null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        context.startActivity(Intent.createChooser(intent, ""));
    }
    public  String getMsgID(String text){
        String transactionDateTime,systemsTraceAuditNumber;
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String strDate = dateFormat.format(date);
        Date currentTime = Calendar.getInstance().getTime();
        transactionDateTime=strDate;
        String TransactionId= getRandomNumberString();

        if (TransactionId.length() > 6)
        {
            systemsTraceAuditNumber = TransactionId.substring(0, 6);
        }
        else
        {
            systemsTraceAuditNumber = TransactionId;
        }
        return text+transactionDateTime+systemsTraceAuditNumber;
    }
    public  String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }
    static public int countSpecialChar(String aldisp_str)
    {
        char ch;
        int spl=0;
        for(int i = 0; i < aldisp_str.length(); i++)
        {
            ch = aldisp_str.charAt(i);
            if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' ) {

            }
            else if(ch >= '0' && ch <= '9') {

            }
            else {
                spl++;
            }
        }

        return spl;
    }

    public boolean isEmulator() {
        String model = Build.MODEL;
        return model.contains("google_sdk") || model.contains("Emulator") || (new File("/sys/qemu_trace")).exists() || (new File("/system/lib/libc_malloc_debug_qemu.so")).exists();
    }

   /* public static boolean isMockLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Marshmallow and above, check if mock location is enabled via developer settings
            return Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        } else {
            // For pre-Marshmallow, check if mock location providers are enabled
            // This check is less reliable and may not catch all cases
            String locationProviders = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return locationProviders.contains("mock");
        }
    }*/

    public static String convertToDecimalHours(String totalHours) {
        Pattern pattern = Pattern.compile("(\\d+)\\s*hours?(?:\\s*(\\d+)\\s*mins?)?");
        Matcher matcher = pattern.matcher(totalHours);

        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;

            double decimalHours = hours + (double) minutes / 60;

            return String.format("%.5f", decimalHours);  // Adjusted to display more decimal places
        } else {
            pattern = Pattern.compile("(\\d+)\\s*mins?");
            matcher = pattern.matcher(totalHours);

            if (matcher.find()) {
                int minutes = Integer.parseInt(matcher.group(1));
                double decimalHours = (double) minutes / 60;
                return String.format("%.5f", decimalHours);  // Adjusted to display more decimal places
            } else {
                return "Invalid time format";
            }
        }
    }


    private static long parseLongOrDefault(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }



    public static void disableMockLocation(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public  List<String> convertHashMapToList(HashMap<Integer, Integer> hashMap) {
        List<String> stringList = new ArrayList<>();
        for (int value : hashMap.values()) {
            stringList.add(Integer.toString(value));
        }
        return stringList;
    }


}
