import java.util.Calendar;
import java.util.TimeZone;

public class test {
    public static void main(String[] args) {
        /**
         *今天0点的时间戳
         */
        Long  time = System.currentTimeMillis();
        long zero = time/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        System.out.println(zero);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        long tt = calendar.getTime().getTime();
        System.out.println(tt);

    }


}
