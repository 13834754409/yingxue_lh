package com.lih.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/27 17:31
 */
public class MonthUtil {

    public static List<Integer> queryMonths(){
        ArrayList<Integer> months = new ArrayList<>();
        months.add(Integer.valueOf(getLast12Months(5)));
        months.add(Integer.valueOf(getLast12Months(4)));
        months.add(Integer.valueOf(getLast12Months(3)));
        months.add(Integer.valueOf(getLast12Months(2)));
        months.add(Integer.valueOf(getLast12Months(1)));
        String mm = new SimpleDateFormat("MM").format(new Date());
        months.add(Integer.valueOf(mm));
        return months;
    }

    private static String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }
}
