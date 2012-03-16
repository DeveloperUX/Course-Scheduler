
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;  
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;

public class MainFrame extends JApplet {
    
    private int FRAMEWIDTH = 510;
    private int FRAMEHEIGHT = 400;
    
    private InputPanel inputPanel;
    private PreviewPanel previewPanel;
    private NotAvailable notAvailablePanel;
    private ResultsTextPanel resultsTextPanel;
    private JPanel resultsGraphTab;
    private JButton done;
    private JButton previous;
    
    private JTabbedPane mainTab;
    private ArrayList workingSchedules;
    private ArrayList mainCourseList;
    private ArrayList<NoTime> noTimeList;
    
    private int topPriority;
    
    private static int pageNum = 0;
  
    @Override
    public void init() {              
        try {
/*
// look and feel that mimics the native OS UI
UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
// cross platform Look and Feel (metal)
UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
// windows look and feel
UIManager.setLookAndFeel( new com.sun.java.swing.plaf.windows.WindowsLookAndFeel() );
// windows classic look and feel
UIManager.setLookAndFeel( new com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel() );
// motif look and feel
UIManager.setLookAndFeel( new com.sun.java.swing.plaf.motif.MotifLookAndFeel() );
// metal look and feel
UIManager.setLookAndFeel( new javax.swing.plaf.metal.MetalLookAndFeel() );
// MacIntosh Look and feel
UIManager.setLookAndFeel( new it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel() );
// Nimbus Look and Feel in JDK 1.6.0_10+
UIManager.setLookAndFeel( new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel() );
// Linux GTK Look and feel
UIManager.setLookAndFeel( new com.sun.java.swing.plaf.gtk.GTKLooktAndFeel() );
*/
	    //mac osx
            UIManager.setLookAndFeel( new com.sun.java.swing.plaf.windows.WindowsLookAndFeel()); 
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createGUI();
                    listen();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    public void createGUI() {
        //INSTANTIATE ALL SCHEDULES
        workingSchedules = new ArrayList();
        noTimeList = new ArrayList<NoTime>();
        mainCourseList = new ArrayList();
        
        //CREATE COMPONENTS
        done = new JButton("DONE");
        previous = new JButton("PREVIOUS");
        previous.setVisible(false);
        done.setPreferredSize(previous.getPreferredSize());
        
        //SET UP SIZE AND LAYOUT OF THE FRAME
        setSize(FRAMEWIDTH, FRAMEHEIGHT);
        Container pane = getContentPane();
        setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        //CREATE A PANEL THAT WILL HOLD THE QUIT BUTTON
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        //CREATE SOME SPACE BETWEEN THE TAB AND THE BUTTON
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //MAKE THE BUTTON AT THE END OF THE PANEL
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.setBorder(BorderFactory.createEtchedBorder()); //FOR TESTING
        
        buttonPane.add(previous);
        buttonPane.add(done);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        
        mainTab = new JTabbedPane();
        
        pane.add(mainTab, 0);
        pane.add(buttonPane, 1);  
        
        inputPanel = new InputPanel();
        previewPanel = new PreviewPanel();
        notAvailablePanel = new NotAvailable();
        resultsTextPanel = new ResultsTextPanel(); 
        
        updatePanels(0);     
        //MAKE IT BLACK
        pane.setBackground(Color.BLACK);
    }
    
    
    public void updatePanels(int num) {
        switch(num) {
            case 0:
                previous.setVisible(false);
                mainTab.removeAll();
                mainTab.addTab("Input", inputPanel);
                mainTab.addTab("Preview", previewPanel);
                break;
            case 1:
                previous.setVisible(true);
                mainTab.removeAll();
                mainTab.addTab("Availability", notAvailablePanel);
                break;
            case 2:
                previous.setVisible(true);
                mainTab.removeAll();
                mainTab.addTab("Results", resultsTextPanel);
                break;
        }
    }
    
    public void listen() {

        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                
                pageNum++;                    
                updatePanels(pageNum);                 
                
                if (pageNum == 2) {
                    //IF DONE IS CLICKED ON PAGE 2, CALCULATE THE SCHEDULES
                    //SET THE MAIN COURSE LIST AND THE NO-TIME LIST                   
                    setCourseLists();
                    setNoTimeList();
                    //GET A TWO-DIMENSIONAL ARRAY OF COURSES
                    Course[][] mainCourseArray = (Course[][]) mainCourseList.toArray(
                            new Course[mainCourseList.size()][]);                
                    
                    ArrayList<NoTime> _noTimeOptional = new ArrayList<NoTime>();
                    ArrayList<NoTime> _noTimeMust = new ArrayList<NoTime>();
                    
                    for (NoTime noTime : noTimeList) {
                        if (noTime.optional)
                            _noTimeOptional.add(noTime);
                        else
                            _noTimeMust.add(noTime);
                    }
                    
                    mainCourseArray = Rack.distribute(mainCourseArray);
                    
                    NoTime[] noTimeOptional = (NoTime[]) _noTimeOptional.toArray(new NoTime[_noTimeOptional.size()]);
                    NoTime[] noTimeMust = (NoTime[]) _noTimeMust.toArray(new NoTime[_noTimeMust.size()]);
                    
                    ArrayList highPriority = new ArrayList();
                    ArrayList middlePriority = new ArrayList();
                    ArrayList lowPriority = new ArrayList();
                    
                    //ITERATE THROUGH THE MAINCOURSE-ARRAY AND CHECK EACH SCHEDULE
                    int length = mainCourseArray.length;
                    for (int i=0; i<length; i++) {
                        Course[] courseArray = mainCourseArray[i];
                        TimeSlot[] timeSlotArray;
                    
                        boolean conflicts = false;                       
                        
                        //CHECK IF THE SCHEDULE CONFLICTS

                        //CREATE ALL TIMESLOTS FROM THE COURSE ARRAY
                        timeSlotArray = Rack.spreadOut(courseArray);
                        //WHILE THE TIMESLOT ARRAY HAS OPTIONAL TIMESLOTS
                         
                        //CHECK IF THE TIMESLOTS CONFLICT
                        timeSlotArray = Rack.trim(timeSlotArray);
                        conflicts = Rack.checkConflict(timeSlotArray);
                        //IF THERE ARE MORE OPTIONALS 
                        if (conflicts) {
                            //GET NUMBER OF OPTIONAL COURSES
                            int numOfOptionals = Rack.numOfOptionals(timeSlotArray);
                            //LOOP THE NUMBER OF OPTIONALS
                            for (int j=0; j<numOfOptionals; j++) {
                                //REMOVE AN OPTIONAL COURSE
                                timeSlotArray = Rack.removeOptional(timeSlotArray);
                                timeSlotArray = Rack.trim(timeSlotArray);
                                //CHECK IF IT CONFLICTS AGAIN
                                conflicts = Rack.checkConflict(timeSlotArray);
                                //IF IT DOES: BREAK THE LOOP
                                if (!conflicts)
                                    break;
                            }                                    
                        }
                        
                        //IF IT STILL CONFLICTS THEN THERE ARE NO MORE OPTIONALS
                        if (conflicts) {
                            //SO REMOVE THIS ARRAY FROM THE MAINCOURSE-ARRAY
                            mainCourseArray[i] = null;
                        }

                        //IF IT DOESN'T CONFLICT
                        else if (!conflicts) {
                            //CHECK IF IT CONFLICTS WITH THE (not optional) NOTIMES ARRAY
                            conflicts = Rack.checkConflict(timeSlotArray, noTimeMust);
                            //IF IT DOES REMOVE THIS ARRAY FROM THE MAINCOURSE-ARRAY
                            if (conflicts)
                                mainCourseArray[i] = null;
                            //IF IT DOESN'T CONFLICT
                            else {
                                //CHECK IF IT CONFLICTS WITH THE (optional) NOTIMES ARRAY 
                                conflicts = Rack.checkConflict(timeSlotArray, noTimeOptional);
                                //IF IT CONFLICTS
                                if (conflicts)
                                    //ADD THE ARRAY TO THE MIDDLE PRIORITY LIST
                                    middlePriority.add(timeSlotArray);
                                //IF IT DOESN'T CONFLICT
                                else {
                                    //IF IT HAS OPTIONAL TIMESLOTS
                                    if (Rack.numOfOptionals(timeSlotArray) > 0)
                                        //ADD THE ARRAY TO THE HIGH PRIORITY LIST
                                        highPriority.add(timeSlotArray);
                                    //ELSE IT HAS NO OPTIONALS
                                    else {
                                        //ADD THE ARRAY TO THE LOW PRIORITY LIST
                                        lowPriority.add(timeSlotArray);
                                    }
                                }
                            }
                        }                    
                    }
                    mainCourseArray = Rack.trim(mainCourseArray);
                    resultsTextPanel.setSchedules(mainCourseArray);
                }
            }
        });
        
        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (pageNum == 2)
                    resultsTextPanel.clearSchedules();
                    
                pageNum--;
                updatePanels(pageNum);
            }
        });
        
        inputPanel.nextClass.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent evt) {
                //ONLY ADD THE CLASS IF IT HAS SOME TIMESLOTS
                if (inputPanel.previewTable.getModel().getRowCount() > 0) {
                                 
                    //CHECK IF THE CLASS NAME EXISTS (checks the name of one of the courses in the list)
                    int equalIndex = previewPanel.getEqualIndex(inputPanel.getCourseList().get(0).name);
                    //IF IT ALREADY EXISTS ONLY APPEND THE COURSES TO THE ALREADY EXISTING LIST
                    if (equalIndex >= 0) 
                        //APPEND THE LIST TO THE COURSELIST WITH INDEX: equalIndex
                        previewPanel.appendData(inputPanel.getCourseList(), equalIndex);                    
                    else 
                        //FILL THE PREVEWPANEL'S TABLE
                        previewPanel.addData(inputPanel.getCourseList());
                    
                    //CLEAR THE COURSELIST AND THE TABLE
                    inputPanel.getCourseList().clear();
                    //CLEAR THE DATA IN THE TABLE BUT LEAVE THE COURSE LIST
                    inputPanel.clearAll();
                }
            }
        });
        
    }
    
    public void setCourseLists() {        
        mainCourseList.clear();
        
        Course[] tempCourseArray;
        int counter = 0;
        
        while(true) {
            //ADD ALL THE COURSE-LISTS TO THE MAIN COURSE
            try {
                
                ArrayList<Course[]> _tempCourseList = previewPanel.getCourseList(counter);
                int listSize = _tempCourseList.size();
                
                tempCourseArray = _tempCourseList.toArray(new Course[listSize]);
                mainCourseList.add(tempCourseArray);
                
                counter++;
                
            } catch(IndexOutOfBoundsException e) { //END OF LIST
                break;
            }
        }
    }
    
    public void setNoTimeList() {
        //CLEAR THE NOT AVAILABLE LIST
        noTimeList.clear();
        //ITERATE THROUGH AND ADD THE NO TIME OBJECTS TO THE NOT AVAILABLE LIST
        int counter = 0;
        
        while(true) {
            
            try {
                
                noTimeList.add(notAvailablePanel.getNoTime(counter));
                counter++;
                
            } catch(IndexOutOfBoundsException e) { //REACHED THE END OF THE LIST
                break;
            }
        }
    }
    
    public boolean calculateSchedules() {
        return true;
    }
}
