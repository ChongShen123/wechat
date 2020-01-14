import java.util.Calendar;
import java.util.TimeZone;

public class test {
    public static void main(String[] args) {
        /**
         *今天0点的时间戳
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);

    }


}
