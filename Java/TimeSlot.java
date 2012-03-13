
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeSlot {
    
    private Course reference;
    public String stringStartTime;
    public String stringEndTime;
    public String stringTime;
    public int startTime;
    public int endTime;
    private char[] days;
    private boolean[] tableRow;
    
    public int startHour;
    public int startMin;
    public int endHour;
    public int endMin;
    
    //DEFAULT CONSTRUCTOR
    public TimeSlot() {
        startTime = 0;
        endTime = 0;
        days = null;
    }
    
    //CONSTRUCTOR USING TWO TIMES
    public TimeSlot(int startTime, int endTime, char[] days) {
        setStringTimes(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }    
    //CONSTRUCTOR USING ONLY A STRING TIME
    public TimeSlot(Object startDate, Object endDate) {   
        convertToMilitary(startDate, endDate);
        setStringTimes(startDate, endDate);
    }
    //CONSTRUCTOR USING A STRING TIME AND AN ARRAY OF BOOLEANS
    public TimeSlot(Object startDate, Object endDate, boolean[] daysArray) {
        setDays(daysArray);
        convertToMilitary(startDate, endDate);
        setStringTimes(startDate, endDate);       
    }
    
    //SETTERS
    public void setDays(boolean[] daysArray) {
        tableRow = daysArray; //HOLDS A BOOLEAN ARRAY: [false, true...]
        char[] charArray = {'M', 'T', 'W', 'R', 'F', 'S'};
        int index = 0;
        int numOfDays = 0;        
        //FIND OUT NUMBER OF DAYS
        for (int i=0; i<6; i++) {
            if (daysArray[i] == true)
                numOfDays++;              
        }
        //MAKE THE DAYS ARRAY AS BIG AS THERE ARE DAYS
        days = new char[numOfDays];
        //GO THROUGH AND ASSIGN DAYS
        for (int j=0; j<6; j++) {
            if (daysArray[j] == true) {
                days[index] = charArray[j];
                index++;    
            } 
        }
    }
    
    //GETTERS
    public char[] getDaysArray() {
        char[] daysCopy = new char[days.length];
        int index = 0;
        for (char i : days) {
            daysCopy[index] = i;
            index++;
        }
        return daysCopy;
    }
    
    public String getDays() {
        String daysString = new String(days);
        if (daysString == null)
            return "NONE";
        else 
            return daysString;
    }
    
    public boolean[] getTableRow() {
        return tableRow;
    }
    
    //PUBLIC USER INTERFACES
    public String toString() {
        String mainString = "";        
        mainString += "Time (Binary Format): " + startTime + "-" + endTime;
        return mainString;
    }
    
    public void setStringTimes(Object startDate, Object endDate) {
        Date startTime = (Date) startDate;
        Date endTime = (Date) endDate;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma");
        stringStartTime = dateFormat.format(startTime);
        stringEndTime = dateFormat.format(endTime);
        
        if (stringStartTime.equals(stringEndTime))
            stringTime = "NONE";
        else 
            stringTime = stringStartTime + "-" + stringEndTime;
    }
    
    public void convertToMilitary(Object startDate, Object endDate) {
        Date startTime = (Date) startDate;
        Date endTime = (Date) endDate;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        this.startTime = Integer.parseInt(dateFormat.format(startTime));
        this.endTime = Integer.parseInt(dateFormat.format(endTime));
        
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        startHour = Integer.parseInt(hourFormat.format(startTime));
        endHour = Integer.parseInt(hourFormat.format(endTime));
        
        SimpleDateFormat minFormat = new SimpleDateFormat("mm");
        startMin = Integer.parseInt(minFormat.format(startTime));
        endMin = Integer.parseInt(minFormat.format(endTime));
    }
    
    public void setReference(Course course) {
        reference = course;
    }
    
    public Course getReference() {
        return reference;
    }
    
    public boolean equals(Object timeSlotObj) {
        
        boolean equal = true;
        
        TimeSlot timeSlot = (TimeSlot) timeSlotObj;
        //CHECK IF THEY ARE FROM THE SAME COURSE
        if (!reference.equals(timeSlot.getReference()))
            equal = false;
        //CHECK IF THEY HAVE THE SAME START TIME AND END TIMES
        else if (startTime != timeSlot.startTime)
            equal = false;
        else if (endTime != timeSlot.endTime)
            equal = false;        
        //CHECK IF THEY HAVE THE SAME NUMBER OF DAYS
        else if (days.length == timeSlot.days.length)
            //ITERATE THROUGH EACH INDEX
            for (int i=0; i<days.length; i++) {
                //IF THEY DONT HAVE THE SAME DAY THEN THEY'RE NOT EQUAL
                if (days[i] != timeSlot.days[i])
                    equal = false;
            }
        return equal;
    }
}