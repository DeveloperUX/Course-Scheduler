
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResultsTextPanel extends JPanel {
    
    private JButton previousButton;
    private JButton nextButton;
    
    private JPanel mainPanel;
    private Course[][] schedulesArray;
    
    public int displayNum;
    
    public ResultsTextPanel() {
        init();
        run();
    }
    
    public void init() {
        previousButton = new JButton("Previous Schedule");
        nextButton = new JButton("Next Schedule");
        displayNum = 0;
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));        
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        //LOOK AND FEEL
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        topPanel.add(previousButton);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(nextButton);
        
        setLayout(new BorderLayout());
        
        
        add(topPanel, BorderLayout.PAGE_START);
    }
    
    public void setSchedules(Course[][] schedules) {
        //SET THE SCHEDULES ARRAY
        if (schedules.length > 0) {
            this.schedulesArray = schedules;
            //DISPLAY THE FIRST SCHEDULE
            displaySchedule(schedules[0]);
            //DISABLE THE PREVIOUS BUTTON
            previousButton.setEnabled(false);
        }
        
        else {
            mainPanel.setLayout(new FlowLayout());
            JLabel text = new JLabel("THERE ARE NO WORKING SCHEDULES!");
            text.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            text.setForeground(Color.RED);
            mainPanel.add(text);
            System.out.println("SCHEDULES LIST IS EMPTY");
        }
        
        //ENABLE OR DISABLE THE BUTTONS
        setButtons();        
        //ADD THE NEW PANEL
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(mainPanel, BorderLayout.CENTER);          
    }
    
    public void displaySchedule(Course[] schedule) {
        /** Displays the course list to the user */ 
        mainPanel.removeAll();        
        int length = schedule.length;
        
        JLabel[][] labels = new JLabel[length][14];        
        
        for (int x=0; x<length; x++) {
            
            labels[x][0] = new JLabel("Lecture:");
            labels[x][2] = new JLabel("Days:");
            try {
                labels[x][1] = new JLabel(schedule[x].getTimeSlot(0).stringTime);                
                labels[x][3] = new JLabel(schedule[x].getTimeSlot(0).getDays());
            } catch (Exception e) {
                labels[x][1] = new JLabel("NONE");
                labels[x][3] = new JLabel("NONE");                
            }
            
            labels[x][4] = new JLabel("Lab:");
            labels[x][6] = new JLabel("Days:");
            try {
                labels[x][5] = new JLabel(schedule[x].getTimeSlot(1).stringStartTime +
                        "-" + schedule[x].getTimeSlot(1).stringEndTime);                
                labels[x][7] = new JLabel(schedule[x].getTimeSlot(1).getDays());
            } catch(Exception e) {
                labels[x][5] = new JLabel("NONE");
                labels[x][7] = new JLabel("NONE");
            }
            
            labels[x][8] = new JLabel("Recitation:");
            labels[x][10] = new JLabel("Days:");
            try {
                labels[x][9] = new JLabel(schedule[x].getTimeSlot(2).stringStartTime +
                        "-" + schedule[x].getTimeSlot(2).stringEndTime);                
                labels[x][11] = new JLabel(schedule[x].getTimeSlot(2).getDays());
            } catch (Exception e) {
                labels[x][9] = new JLabel("NONE");
                labels[x][11] = new JLabel("NONE");                
            }
            
            labels[x][12] = new JLabel("IS OPTIONAL:");
            labels[x][13] = new JLabel(String.valueOf(schedule[x].isOptional));
            
        }
        
        JPanel[] mainPanels = new JPanel[length];
        
        for (int panelNum=0; panelNum<length; panelNum++) {
            
            mainPanels[panelNum] = new JPanel();
            mainPanels[panelNum].setBorder(BorderFactory.createTitledBorder(
                    schedule[panelNum].name + " - " + schedule[panelNum].section));
            mainPanels[panelNum].setLayout(new BoxLayout(mainPanels[panelNum], BoxLayout.Y_AXIS));
            
            mainPanel.add(mainPanels[panelNum]);
        }
        
        //CREATE THE JPANELS
        JPanel[][] panels = new JPanel[length][4];
                
        for (int num=0; num<length; num++) {
            
            int counter = 0;
            
            for (int insideNum=0; insideNum<4; insideNum++) {
                
                panels[num][insideNum] = new JPanel();
                panels[num][insideNum].setLayout(new FlowLayout(FlowLayout.LEADING));
                panels[num][insideNum].add(labels[num][counter++]);
                panels[num][insideNum].add(labels[num][counter++]);
                
                if (insideNum != 3) {
                    panels[num][insideNum].add(labels[num][counter++]);
                    panels[num][insideNum].add(labels[num][counter++]);                    
                }
                
                mainPanels[num].add(panels[num][insideNum]);
            }                    
        }
              
    }
    
    public void run() {        
        
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //SET THE CURRENT PAGE NUMBER
                displayNum++;
                //ENABLE OR DISABLE THE BUTTONS
                setButtons();          
                //DISPLAY THE NEXT SCHEDULE
                displaySchedule(schedulesArray[displayNum]);
                validate();
            }
        });
        
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //SET THE CURRENT PAGE NUMBER
                displayNum--;
                //ENABLE OR DISABLE THE BUTTONS
                setButtons();                
                //DISPLAY THE PREVIOUS SCHEDULE
                displaySchedule(schedulesArray[displayNum]);
                validate();
            }
        });
        
    }
    
    public void clearSchedules() {
        mainPanel.removeAll();
    }
    
    private void setButtons() {
        //IF LIST IS ZERO OR HAS ONLY 1 SCHEDULE
        System.out.println("LENGTH: " + schedulesArray.length);
        if (schedulesArray.length < 2) {
            nextButton.setEnabled(false);
            previousButton.setEnabled(false);
        }
        //IF AT THE END OF THE LIST DISABLE THE NEXTBUTTON
        else if (displayNum == schedulesArray.length-1) {
            nextButton.setEnabled(false);
            previousButton.setEnabled(true);
        }
        //IF AT THE BEGGINING DISABLE THE PREVIOUSBUTTON
        else if (displayNum == 0) {
            previousButton.setEnabled(false);
            nextButton.setEnabled(true);
        }
        //IF IN BETWEEN ENABLE BOTH BUTTONS
        else {
            previousButton.setEnabled(true);
            nextButton.setEnabled(true);
        }
    }
    
}
