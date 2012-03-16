
public class Course {
    
    public String name;
    public int section;
    private TimeSlot lab;
    private TimeSlot lecture;
    private TimeSlot recitation;
    public boolean isOptional;
    
    //DEFAULT CONSTRUCTOR
    public Course() {
        this("unkown", 000);        
    }
    //HYBRID CONSTRUCTOR
    public Course(String name) {
        this(name, 000);
    }
    //COMPREHENSIVE CONSTRUCTOR
    public Course(String name, int section) {
        this.name = name;
        this.section = section;
    }
    
    //GETTERS
    public TimeSlot getTimeSlot(int type) {
        switch(type) {
            case 0:
                return lecture;
            case 1:
                return lab;
            case 2:
                return recitation;
        }
        return lecture;
    }
    //TIME GETTTERS FOR LABS AND OTHERS
    
    //SETTERS
    //TIME SETTERS FOR LABS AND OTHERS
    
    public void setLecture(TimeSlot timeSlot) {
        timeSlot.setReference(this);
        lecture = timeSlot;
    }
    public void setLab(TimeSlot timeSlot) {
        timeSlot.setReference(this);
        lab = timeSlot;
    }
    public void setRecitation(TimeSlot timeSlot) {
        timeSlot.setReference(this);
        recitation = timeSlot;
    }
    
    //PUBLIC INTERFACE BEHAVIORS
    @Override   
    public String toString() {
        String mainString;
        mainString = "Name: " + name + "\n" + 
                "Section: " + section + "\n" + 
                "TimeSlot: " + String.valueOf (lecture.startTime) + "-" + 
                String.valueOf (lecture.endTime) + " " + 
                "(" + lecture.stringStartTime + ")" +
                "\n" + "ALSO HAS: ";

        mainString += "[";
        if (lab != null)
            mainString += "lab ";
        if (lecture != null)
            mainString += "lecture ";
        if (recitation != null)
            mainString += "recitation"; 
        mainString += "]";
        
        return mainString;
    }
    
    public boolean equals(Object obj) {
        
        Course course = (Course) obj;
        boolean equal = true;
        
        if (!name.equals(course.name))
            equal = false;
        else if (section != course.section)
            equal = false;
        
        return equal;
    }
}
