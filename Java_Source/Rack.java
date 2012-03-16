
import java.util.ArrayList;

public class Rack {
    
    public static Course[][] distribute(Object[][] values) {
        
        int fields = values.length;    
        int[] counts = new int[fields];    
        int totalRows = 1;
        
        for(int i=0; i<fields; i++) {    
            counts[i] = values[i].length;
            totalRows *= counts[i];            
        }
        
        Course[][] mainArray = new Course[totalRows][];
        int index = 0;
    
        for( int t = 0; t < totalRows; t++ ) {
        
            Course[] list = new Course[fields];
            
            for (int x = 0; x < fields; x++) {    
                int pos = t;
    
                for (int y = 0; y < x; y++) {
                    pos /= counts[y];
                }
    
                int offset = pos % counts[x];    
                Object[] members = values[x];
                Course member = (Course) members[offset];
                
                list[x] = member;                
            }            
            mainArray[index] = list;
            index++;
        }
        return mainArray;
    }
    
    public static TimeSlot[] spreadOut(Object[] values) { //NEED TO CHANGE TO LIST
        
        ArrayList<TimeSlot> courseList = new ArrayList();        
            
        for(Object value : values) {
            Course course = (Course) value;

            for(int i=0; i<3; i++) {
                if(course.getTimeSlot(i)!=null)
                    courseList.add(course.getTimeSlot(i));
            }
        }
        TimeSlot[] returnValueArray = (TimeSlot[]) courseList.toArray(new TimeSlot[courseList.size()]);   
        return returnValueArray;
    }
    
    public static boolean checkConflict(Object[] values, NoTime[] noTimeArray) {
        
        TimeSlot[] timeSlotArray = (TimeSlot[]) values;
        
        //ITERATE THROUGH THE NOTIME-ARRAY
        for (NoTime noTime : noTimeArray) {
            //ITERATE THROUGH THE TIMESLOTS
            for (TimeSlot timeSlot : timeSlotArray) {
                //CHECK IF A NOTIME CONFLICTS WITH ANY OF THE TIMESLOTS
                if (conflict(timeSlot, noTime))
                    //IF THEY CONFLICT RETURN TRUE
                    return true;
            }
        }
        //IF THEY DON'T CONFLICT RETURN FALSE
        return false;
    }
    
    public static boolean checkConflict(Object[] values) {
        
        ArrayList timeSlotList = new ArrayList();
        
        for (Object valueObj : values) {
            
            TimeSlot tempSlot = (TimeSlot) valueObj;
            timeSlotList.add(tempSlot);
        }
        
        char[] longestDay;     
        
        while (timeSlotList.size() > 0) {
            //FIND THE LONGEST DAY ARRAY
            longestDay = getLongestDay(timeSlotList);
            
            ArrayList<TimeSlot> commonDaysList = new ArrayList();
            //ITERATE THROUGH LIST OF VALUES AND FIND ALL TIMESLOTS WITH DAYS IN COMMON
            
            int size = timeSlotList.size();
            int counter = 0;
        
            for (int i=0; i<size; i++) {
                TimeSlot currentSlot = (TimeSlot) timeSlotList.get(counter);
                //IF THEY HAVE DAYS IN COMMON
                if (haveCommonDays(longestDay, currentSlot.getDaysArray())) {
                    //ADD THEM TO THE COMMON DAYS LIST
                    commonDaysList.add(currentSlot);
                    timeSlotList.remove(counter);
                    //RESET COUNTER BACK ONE
                    counter--;
                }
                counter++;
            }
            
            //ITERATE THROUGH THE TIMESLOT LIST AND COMPARE THE TIMESLOTS TO EACH OTHER
            int listSize = commonDaysList.size();
            
            for (int i=0; i<listSize; i++) {
                for (int index=0; index<listSize; index++) {
                    
                    TimeSlot timeSlot1 = commonDaysList.get(i);
                    TimeSlot timeSlot2 = commonDaysList.get(index);
                    
                    //IF THEY ARE NOT THE SAME TIMESLOTS CHECK IF THEY CONFLICT
                    if (!timeSlot1.equals(timeSlot2)) {
                        //CHECK IF THE TWO TIMESLOTS CONFLICT
                        boolean conflict = conflict(timeSlot1, timeSlot2);
                        //IF THEY CONFLICT RETURN TRUE
                        if (conflict)
                            return true;
//                        //CHECK IF THE TIMESLOT_1 IS IN BETWEEN TIMESLOT_2 OR IS EQUAL
//                        if (timeSlot1.startTime >= timeSlot2.startTime &
//                                timeSlot1.startTime <= timeSlot2.endTime) {      
//                            //IF THE STARTTIME CONFLICTS
//                            return true;                   
//                        }
//                        //CHECK IF THE TIMESLOT_1 IS IN BETWEEN TIMESLOT_2 OR IS EQUAL
//                        if (timeSlot1.endTime >= timeSlot2.startTime &
//                                timeSlot1.endTime <= timeSlot2.endTime) {
//                            //IF THE ENDTIME CONFLICTS
//                            return true;
//                        }
                    }
                }
            }
            
        }
        //IF NOTHING CONFLICTS
        return false;
    }
    
    public static int numOfOptionals(Object[] values) {
        
        TimeSlot[] timeArray = (TimeSlot[]) values;
        int numOfOptionals = 0;
        
        for (int i=0; i<timeArray.length; i++) {
            
            if (timeArray[i].getReference().isOptional)
                numOfOptionals++;
        }
        
        return numOfOptionals;
    }
    
    public static TimeSlot[] removeOptional(TimeSlot[] values) {
        
        for (int slot=0; slot<values.length; slot++) {
            if (values[slot].getReference().isOptional) 
                values[slot] = null;
        }
        return values;
    }
    
    public static TimeSlot[] trim(TimeSlot[] values) {
        
        TimeSlot[] trimmedArray;
        ArrayList<TimeSlot> trimmedList = new ArrayList<TimeSlot>();
        
        for (int i=0; i<values.length; i++) {
            if (values[i] != null)
                trimmedList.add(values[i]);
        }
        
        trimmedArray = (TimeSlot[]) trimmedList.toArray(new TimeSlot[trimmedList.size()]);
        return trimmedArray;
    }
    
    public static Course[][] trim(Course[][] values) {
        
        Course[][] trimmedArray;
        ArrayList<Course[]> trimmedList = new ArrayList<Course[]>();
        
        for (int i=0; i<values.length; i++) {
            if (values[i] != null) {
                trimmedList.add(values[i]);                
            }
        }
        
        trimmedArray = (Course[][]) trimmedList.toArray(new Course[trimmedList.size()][]);
        return trimmedArray;
    }
    
    
    private static char[] getLongestDay(ArrayList valuesList) {
        
        TimeSlot[] values = (TimeSlot[]) valuesList.toArray(new TimeSlot[valuesList.size()]);
        
        char[] currentDays = values[0].getDaysArray();
        for (int index=0; index<values.length; index++) {
            if (values[index].getDaysArray().length > currentDays.length)
                currentDays = values[index].getDaysArray();
        }          
        return currentDays; 
    }
    
    
    private static boolean haveCommonDays(char[] time1, char[] time2) {
        
        for (char char1 : time1)            
            for (char char2 : time2)                
                if (char1 == char2)                    
                    return true;
            
        return false;
    }
    
    private static boolean conflict(TimeSlot timeSlot_1, TimeSlot timeSlot_2) {
        boolean areCommon;
        areCommon = haveCommonDays(timeSlot_1.getDaysArray(), timeSlot_2.getDaysArray());
        
        if (areCommon) {
            
            if (timeSlot_1.startTime >= timeSlot_2.startTime &
                    timeSlot_1.startTime <= timeSlot_2.endTime) {      
                //IF THE STARTTIME CONFLICTS
                return true;                   
            }

            if (timeSlot_1.endTime >= timeSlot_2.startTime &
                    timeSlot_1.endTime <= timeSlot_2.endTime) {
                //IF THE ENDTIME CONFLICTS
                return true;
            }
        }
        return false;  
    }
    
    private static boolean conflict(TimeSlot timeSlot, NoTime noTime) {        
        boolean areCommon;
        areCommon = haveCommonDays(timeSlot.getDaysArray(), noTime.getTime().getDaysArray());
        
        if (areCommon) {
            
            if (timeSlot.startTime > noTime.getTime().startTime &
                    timeSlot.startTime < noTime.getTime().endTime) {      
                //IF THE STARTTIME CONFLICTS
                return true;                   
            }

            if (timeSlot.endTime > noTime.getTime().startTime &
                    timeSlot.endTime < noTime.getTime().endTime) {
                //IF THE ENDTIME CONFLICTS
                return true;
            }
        }
        return false;            
    }
    

}
