/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ql.common;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class Utils {
    public static int getHourFromTime(Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return hours;
    }
    
   public static String getYesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }
    
    public static String getNow() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }
    
    public static String formatCurrency(double value) {
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(value);
    }
    
    public static double roundUp(double value) {
        return Math.round(value * 2) / 2.0;
    }
    
    public static String formatDate(Timestamp date) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
    }

    public static String getTomorrow() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }

    public static String formatSalary(double salary) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
    
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.applyPattern("#,000,000");
        return df.format(salary);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
