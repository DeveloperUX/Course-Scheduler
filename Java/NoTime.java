
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoTime {
    
    public String label;
    public boolean optional;
    private TimeSlot time;
    
    public NoTime() {
        label = "";
        optional = false;
        time = new TimeSlot();
    }
    
    public NoTime(Object startDate, Object endDate, boolean[] days) {
        this.label = "";
        this.optional = false;
        
        Date startTime = (Date) startDate;
        Date endTime = (Date) endDate;
        this.time = new TimeSlot(startTime, endTime, days);
    }
    
    public NoTime(TimeSlot time) {
        label = "";
        optional = false;
        this.time = time;
    }
    
    public NoTime(String label, boolean optional, TimeSlot time) {
        this.label = label;
        this.optional = optional;
        this.time = time;
    }
    
    public TimeSlot getTime() {
        return time;
    }

}
