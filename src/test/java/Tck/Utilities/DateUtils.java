package Tck.Utilities;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	
	 public static String getCalendarDate() {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        return sdf.format(new Date());
	    }

	    public static String getFutureDate(int days) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, days);
	        return sdf.format(cal.getTime());
	    }

	    public static String getCurrentTime() {
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	        return sdf.format(new Date());
	    }

	    public static String getFutureTime(int minutes) {
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.MINUTE, minutes);
	        return sdf.format(cal.getTime());
	    }
	
	
	

}
