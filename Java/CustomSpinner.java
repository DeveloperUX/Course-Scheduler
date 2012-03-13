
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JSpinner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.SpinnerDateModel;

/**
 *
 * @author Yasso
 */
public class CustomSpinner extends JSpinner {
    
    Calendar c;
    
    public CustomSpinner() {
        c = new GregorianCalendar();
        c.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 0, 0);
        setModel(new SpinnerDateModel());
        getModel().setValue(c.getTime());
        setEditor(new JSpinner.DateEditor(this, "h:mm a"));
    }
    
    public void resetValue() {
        /**Resets the value of the JSpinner to: 12:00AM*/
        //SET THE CALENDAR TIME IN THE SPINNER
        getModel().setValue(c.getTime());
    }
    
    public void setValue(TimeSlot timeSlot, int startOrEnd) {
        /**Loads a TimeSlot object's current time into the JSpinner depending on whether the user
         * wants to load the start time or the end time of the TimeSlot object. */
        Calendar cal = new GregorianCalendar();
        
        switch (startOrEnd) { 
            
            case 0:
                cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, timeSlot.startHour, timeSlot.startMin);
                
            case 1:
                cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, timeSlot.endHour, timeSlot.endMin);
        }
        //SET THE VALUE OF THE SPINNER TO THE NEW CALENDAR OBJECT'S TIME
        setValue(cal.getTime());        
    }
    
    public void setValue(int hour, int min) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, hour, min);
        //SET THE SPINNER VALUE
        setValue(cal.getTime());
    }

}
