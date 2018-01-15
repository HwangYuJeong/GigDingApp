package org.androidtown.gigdingapp.common;

import java.util.Calendar;

/**
 * 공통 Util Class
 */

public class ComUtil {

    /**
     *
     * @return YYYMMDD
     */
    public String com_toDate() {

        Calendar cal = Calendar.getInstance();
        String sTmpDateYear = String.valueOf(cal.get(Calendar.YEAR));
        String sTmpDateMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String sTmpDateDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        sTmpDateMonth = sTmpDateMonth.length() == 2 ? sTmpDateMonth : "0" + sTmpDateMonth;
        sTmpDateDay = sTmpDateDay.length() == 2 ? sTmpDateDay : "0" + sTmpDateDay;

        return sTmpDateYear + sTmpDateMonth + sTmpDateDay;
    }

}
