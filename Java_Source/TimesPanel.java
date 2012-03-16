
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TimesPanel extends JPanel {
    
    public static int HEIGHT = 25;
    public static int WIDTH = 90;

    public JLabel classNameLabel;
    public JTextField classNameUI;
    public JLabel sectionLabel;
    public JSpinner sectionUI;
    public JLabel isOptionalLabel;
    public JCheckBox isOptionalUI;
    public JLabel lectureLabel;
    public CustomSpinner lectureStartUI;
    public CustomSpinner lectureEndUI;
    public JLabel labLabel;
    public CustomSpinner labStartUI;
    public CustomSpinner labEndUI;
    public JLabel recitationLabel;
    public CustomSpinner recitationStartUI;
    public CustomSpinner recitationEndUI;
    private JButton clear;
    private JTable daysTable;
    
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JScrollPane scrollPane;
    private Calendar c;
    public String lastClassName;
    
  //CREATE PANEL
    public TimesPanel() {
        init();
        setPanelLayout();
        addComponents();
        run();
    }
    
    public void init() {
        lastClassName = "";
        
        classNameLabel = new JLabel("Class Name:");
        classNameUI = new JTextField();
        
        sectionLabel = new JLabel("Section:");
        sectionUI = new JSpinner();
        
        isOptionalLabel = new JLabel("Optional:");
        isOptionalUI = new JCheckBox();
        
        lectureLabel = new JLabel("Lecture Time:");
        lectureStartUI = new CustomSpinner();
        lectureEndUI = new CustomSpinner();
        
        labLabel = new JLabel("Lab Time:");
        labStartUI = new CustomSpinner();
        labEndUI = new CustomSpinner();
        
        recitationLabel = new JLabel("Recitation Time:");
        recitationStartUI = new CustomSpinner();
        recitationEndUI = new CustomSpinner();
        
        clear = new JButton("Clear");
        
        //CREATE TABLE OF DAYS
        Object[] columnNames = {"", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"};
        Object[][] defaultRow = {{"Lecture:", false, false, false, false, false, false},
                            {"Lab:", false, false, false, false, false, false},
                            {"Recitation:", false, false, false, false, false, false}};

        DefaultTableModel model = new DefaultTableModel(defaultRow, columnNames);
        
        daysTable = new JTable(model) {            
            @Override //MAKE THE CELL NOT EDITABLE
            public boolean isCellEditable(int row, int col) {
                if (col==0)
                    return false;
                else
                    return true;
            }           
            @Override //MAKE THE CELLS RENDERABLE
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }            
        };   
        //MAKE THE COLUMNS OF THE TABLE NOT MOVEABLE
        daysTable.getTableHeader().setReorderingAllowed(false);
        //SET THE EDITOR FOR THE COLUMNS
        for(int i=1; i<7; i++) {
            daysTable.getColumn(columnNames[i]).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        }     
        
        //SET THE SIZE AND SCROLL PANE OF THE TABLE
        TableColumn column = null;
        column = daysTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(70);
        for(int i=1; i<7; i++) {
            column = daysTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(40);
        }
//        daysTable.setPreferredScrollableViewportSize(
//                new Dimension(daysTable.getPreferredScrollableViewportSize().width, 20));
        scrollPane = new JScrollPane(daysTable);
        
        //SET THE DIMENSIONS OF THE COMPONENTS
        classNameUI.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        sectionUI.setPreferredSize(new Dimension(WIDTH-30, HEIGHT));
    }
    
    public void setPanelLayout() {        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        //--PANEL1 WILL BE FOR TESTING
        panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        
        panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.setBorder(BorderFactory.createEmptyBorder(5, 19, 5, 0));
        
        panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.setBorder(BorderFactory.createEmptyBorder(5, 39, 5, 0));
        
        panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        
        JPanel panelsHolder = new JPanel();
        panelsHolder.setLayout(new GridLayout(0, 1));
        //panelsHolder.setLayout(new BoxLayout(panelsHolder, BoxLayout.Y_AXIS)); //TESTING
        panelsHolder.add(panel1);
        panelsHolder.add(panel2);
        panelsHolder.add(panel3);
        panelsHolder.add(panel4);
        
        panelsHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setSize((daysTable.getPreferredSize()));
        
        add(panelsHolder);
        add(scrollPane);
    }

    public void addComponents() {
        
        Separator sep1 = new Separator();
        sep1.setPreferredSize(new Dimension(20, 5));
        
        Separator sep2 = new Separator();
        sep2.setPreferredSize(new Dimension(20, 5));
        
        Separator sep3 = new Separator();
        sep3.setPreferredSize(new Dimension(20, 5));
        
        panel1.add(classNameLabel);
        panel1.add(Box.createRigidArea(new Dimension(5, 0)));
        panel1.add(classNameUI);
        panel1.add(Box.createRigidArea(new Dimension(15, 0)));
        panel1.add(sectionLabel);
        panel1.add(Box.createRigidArea(new Dimension(5, 0)));
        panel1.add(sectionUI);
        panel1.add(Box.createRigidArea(new Dimension(15, 0)));
        panel1.add(isOptionalLabel);
        panel1.add(Box.createRigidArea(new Dimension(5, 0)));
        panel1.add(isOptionalUI);
        panel1.add(Box.createHorizontalGlue());
        
        panel2.add(lectureLabel);
        panel2.add(Box.createRigidArea(new Dimension(5, 0)));
        panel2.add(lectureStartUI); 
        panel2.add(Box.createRigidArea(new Dimension(5, 0)));
        panel2.add(sep1);
        panel2.add(Box.createRigidArea(new Dimension(5, 0)));
        panel2.add(lectureEndUI);
        panel2.add(Box.createRigidArea(new Dimension(79, 0)));
        panel2.add(Box.createHorizontalGlue());
        
        panel3.add(labLabel);
        panel3.add(Box.createRigidArea(new Dimension(5, 0)));
        panel3.add(labStartUI);
        panel3.add(Box.createRigidArea(new Dimension(5, 0)));
        panel3.add(sep2);
        panel3.add(Box.createRigidArea(new Dimension(5, 0)));
        panel3.add(labEndUI);
        panel3.add(Box.createRigidArea(new Dimension(79, 0)));
        panel3.add(Box.createHorizontalGlue());
        
        panel4.add(recitationLabel);
        panel4.add(Box.createRigidArea(new Dimension(5, 0)));
        panel4.add(recitationStartUI);
        panel4.add(Box.createRigidArea(new Dimension(5, 0)));
        panel4.add(sep3);
        panel4.add(Box.createRigidArea(new Dimension(5, 0)));
        panel4.add(recitationEndUI);
        panel4.add(Box.createRigidArea(new Dimension(20, 0)));
        panel4.add(clear);
        panel4.add(Box.createHorizontalGlue());
    }
    
    public String getClassName() {
        return classNameUI.getText().toString();
    }
    
    public int getSection() {
        Object value = sectionUI.getValue();
        return Integer.parseInt(value.toString());
    }
    
    public boolean isOptional() {
        return isOptionalUI.isSelected();
    }
    

    public TimeSlot createLectureTime() {        
        //CREATE AN ARRAY TO HOLD THE DAYS
        boolean[] daysArray = new boolean[daysTable.getColumnCount()-1];
        Object value;  //WILL HOLD THE VALUE IN A CELL
        for (int i=1; i<daysTable.getColumnCount(); i++) {
            value = daysTable.getValueAt(0, i);
            daysArray[i-1] = Boolean.parseBoolean(value.toString());
        }
        //--CREATE THE TIMESLOT AND RETURN IT
        TimeSlot timeSlot = new TimeSlot(lectureStartUI.getValue(), lectureEndUI.getValue());
        timeSlot.setDays(daysArray);
        return timeSlot;
    }

    public TimeSlot createLabTime() {
        //CREATE AN ARRAY TO HOLD THE DAYS
        boolean[] daysArray = new boolean[daysTable.getColumnCount()-1];
        Object value;  //WILL HOLD THE VALUE IN A CELL
        for (int i=1; i<daysTable.getColumnCount(); i++) {
            value = daysTable.getValueAt(1, i);
            daysArray[i-1] = Boolean.parseBoolean(value.toString());
        }
        //--CREATE THE TIMESLOT AND RETURN IT
        TimeSlot timeSlot = new TimeSlot(labStartUI.getValue(), labEndUI.getValue());
        timeSlot.setDays(daysArray);
        return timeSlot;
    }

    public TimeSlot createRecitationTime() {
        //CREATE AN ARRAY TO HOLD THE DAYS
        boolean[] daysArray = new boolean[daysTable.getColumnCount()-1];
        Object value;  //WILL HOLD THE VALUE IN A CELL
        for (int i=1; i<daysTable.getColumnCount(); i++) {
            value = daysTable.getValueAt(2, i);
            daysArray[i-1] = Boolean.parseBoolean(value.toString());
        }
        //--CREATE THE TIMESLOT AND RETURN IT
        TimeSlot timeSlot = new TimeSlot(recitationStartUI.getValue(), recitationEndUI.getValue());
        timeSlot.setDays(daysArray);
        return timeSlot;
    }
    //////////////////////////////////////////////////////////////////
    public void loadCourse(Course courseToLoad) {
        Calendar calendar = new GregorianCalendar();
        
        sectionUI.setValue(courseToLoad.section);
        isOptionalUI.setSelected(courseToLoad.isOptional);
        
        JSpinner[] spinnerArray = {lectureStartUI, lectureEndUI,
                                    labStartUI, labEndUI,
                                    recitationStartUI, recitationEndUI};
        //RELOAD THE JSPINNERS WITH THE TIMES
        ////////////////////////////////////---NEEDS WORK------/////////////////////////////////////////////////
        for (int index=0; index<3; index++) {
            try {
                calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 
                    courseToLoad.getTimeSlot(index).startHour, courseToLoad.getTimeSlot(index).startMin);
                spinnerArray[index*2].setValue(calendar.getTime());

                calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 
                    courseToLoad.getTimeSlot(index).endHour, courseToLoad.getTimeSlot(index).endMin);
                spinnerArray[index*2+1].setValue(calendar.getTime());
            } catch(Exception e) {}
        }
        //////////////////////////////////////////----NEEDS WORK----///////////////////////////////////////
        //RELOAD THE DAYS IN THE DAYS TABLE
        for (int row=0; row<daysTable.getRowCount(); row++) {
            for (int col=1; col<daysTable.getColumnCount(); col++) {
                try {
                    boolean dayCell = courseToLoad.getTimeSlot(row).getTableRow()[col-1];
                    daysTable.getModel().setValueAt(dayCell, row, col);
                } catch(Exception e) {}
            }
        }
    }

    public Course createCourse() {
        //CREATE A COURSE OBJECT
      Course course = new Course();
      
      course.name = getClassName();
      course.section = getSection();
      course.isOptional = isOptional();
      
      //CHECK CLASSES THAT DAYS
      Object value;
      boolean[] hasClass = new boolean[3];
      int hasClassIndex = 0;
      
      for (int row=0; row<daysTable.getRowCount(); row++) {
          for (int col=1; col<daysTable.getColumnCount(); col++) {
              value = daysTable.getModel().getValueAt(row, col);
              if (value.equals(true))
                  hasClass[hasClassIndex] = true;
          }
          hasClassIndex++;
      }
      
      if (hasClass[0] == true)
          course.setLecture(createLectureTime());
      if (hasClass[1] == true)
          course.setLab(createLabTime());
      if (hasClass[2] == true)
          course.setRecitation(createRecitationTime());       
      
      return course;
    }
    
    public int canCreateCourse() {
    //*@RETURNS: 1-blank, 2-not the same, 3-no days checked, 0-creatable
        
        //IF COURSE NAME IS BLANK
        if (classNameUI.getText().equals(""))
            return 1;
        //IF COURSE NAME DOES NOT EQUAL THE ONE BEFORE IT
        else if (!classNameUI.getText().equals(lastClassName))
            if (!lastClassName.equals(""))
                return 2;
        
        //CHECK CLASSES THAT DAYS
        Object value;      
        for (int row=0; row<daysTable.getRowCount(); row++) {
          for (int col=1; col<daysTable.getColumnCount(); col++) {
              value = daysTable.getModel().getValueAt(row, col);
              if (value.equals(true))
                  //IF THE COURSE PASSES EVERYTHING AND HAS ATLEAST ONE DAY CHECKED
                  return 0;
          }
        }
        //IF COURSE DOES NOT HAVE ANY DAYS CHECKED
        return 3;
    }
    
    
    
    public void run() {
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearButtonClicked(true);
            }
        });
    }
    
    public void clearButtonClicked(boolean clearClassName) {
        //RESET THE CALENDAR
        if (clearClassName)
            classNameUI.setText("");
        sectionUI.setValue(0);
        isOptionalUI.setSelected(false);
        lectureStartUI.resetValue();
        lectureEndUI.resetValue();
        labStartUI.resetValue();
        labEndUI.resetValue();
        recitationStartUI.resetValue();
        recitationEndUI.resetValue();
        
        Object bool = false;
        for (int col=1; col<daysTable.getColumnCount(); col++) {
            for (int row=0; row<daysTable.getRowCount(); row++) {
                daysTable.getModel().setValueAt(bool, row, col);
            }
        }        
    }
    
}