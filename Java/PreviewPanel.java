
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class PreviewPanel extends JLayeredPane {
    
    private JLabel chooseLabel;
    private JComboBox classChooser;
    private JCheckBox isOptionalUI;
    private JTable previewTable;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    
    private ArrayList courseList;
    private DefaultTableModel tableModel;
    
    public PreviewPanel() {
        init();
        addComponents();
        run();
    }
    
    public PreviewPanel(ArrayList<Course> courseList) {
        init();
        addData(courseList);
        addComponents();
        run();
    }
    
    public void init() {        
        chooseLabel = new JLabel("Choose a course to preview:");
        chooseLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        
        classChooser = new JComboBox(); 
        classChooser.setPreferredSize(new Dimension(100, 25));
        
        isOptionalUI = new JCheckBox("Optional");
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");
        
        courseList = new ArrayList();
        
        tableModel = new DefaultTableModel();        
        String[] columns = {"Section", "Optional", "Lecture", "Lab", "Recitation"};
        for (String column : columns) {
            tableModel.addColumn(column);
        }        
        previewTable = new JTable(tableModel);
        
        //SET THE TABLE COLUMN SIZES
        previewTable.getColumnModel().getColumn(0).setPreferredWidth(15);
        previewTable.getColumnModel().getColumn(1).setPreferredWidth(15);
        //SET THE BUTTON SIZES
        addButton.setPreferredSize(removeButton.getPreferredSize());
        editButton.setPreferredSize(removeButton.getPreferredSize());
    }
    
    public void addData(ArrayList<Course> courseList) {

        int rowCount = tableModel.getRowCount();
        if (rowCount != 0)
            for (int index=0; index<rowCount; index++) 
                tableModel.removeRow(0); //REMOVE ROWS
        
        ArrayList<Course> tempCourseList = new ArrayList();
        
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        Object[] rowData = null;
        
        String name = "";
        
        for(Course course : courseList) {            
            rowData = new Object[5];
            rowData[0] = course.section;
            rowData[1] = course.isOptional;

            for (int i = 0; i < 3; i++) {
                try {
                    rowData[i + 2] = course.getTimeSlot(i).stringStartTime + "-" + 
                            course.getTimeSlot(i).stringEndTime + " " + 
                            course.getTimeSlot(i).getDays();
                } catch (Exception e) {
                    rowData[i + 2] = "NONE";
                }
            }
            model.addRow(rowData);
            tempCourseList.add(course);
            
            //FOR CLASS CHOOSER
            name = course.name;
        }
        
        this.courseList.add(tempCourseList);
        
        classChooser.addItem(name);  
        classChooser.setSelectedItem(name);
    }
    
    public void addComponents() {
        JScrollPane scrollPane = new JScrollPane(previewTable);
        previewTable.setPreferredScrollableViewportSize(new Dimension(480, 200));
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEADING)); 
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        topPanel.add(chooseLabel);
        topPanel.add(classChooser);
        topPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        topPanel.add(isOptionalUI);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);
        
        JPanel tableButtonPanel = new JPanel();
        tableButtonPanel.setLayout(new BorderLayout());
        tableButtonPanel.setBorder(
                BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Preview"));
        
        tableButtonPanel.add(scrollPane, BorderLayout.CENTER);
        tableButtonPanel.add(buttonsPanel, BorderLayout.PAGE_END);
        
        JPanel emptyPanel = new JPanel();
        emptyPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        setLayout(new BorderLayout());
        
        setLayer(topPanel, 0, 1);
        setLayer(tableButtonPanel, 0, 1);
        setLayer(emptyPanel, 0, 1);
        
        add(topPanel, BorderLayout.PAGE_START);
        add(tableButtonPanel, BorderLayout.CENTER);
        add(emptyPanel, BorderLayout.PAGE_END);
        
    }
    
    public void run() {
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //CREATE THE POPUP PANEL
                final JDialog popup = new JDialog();
                //GET THE CONTAINER PANEL AND SET LAYOUT
                popup.setLayout(new BorderLayout());
                //SET THE TIMESPANEL AND THE FINISHED BUTTON
                final TimesPanel app = new TimesPanel();
                app.setBorder(BorderFactory.createEtchedBorder());
                
                JButton finishButton = new JButton("Click Here When Finished");
                
                final JLabel warnLabel = new JLabel();
                warnLabel.setForeground(Color.RED);
                
                //ADD THE TIMESPANEL AND BUTTON
                popup.add(app, BorderLayout.CENTER);
                popup.add(warnLabel, BorderLayout.PAGE_START);
                popup.add(finishButton, BorderLayout.PAGE_END);                
                
                popup.setSize(400, 285);//400,270
                //SHOW THE POPUP PANEL
                popup.setVisible(true);
                
                //ADD THE NEW DATA TO THE LIST AND TABLE                
                finishButton.addActionListener(new ActionListener() {                    
                    public void actionPerformed(ActionEvent evt) {                        
                        //CHECK IF A COURSE CAN BE CREATED
                        if (app.canCreateCourse() == 0) {                
                            //CREATE A COURSE OBJECT
                            Course course = app.createCourse();                       
                            //ITERATE THROUGHT THE COURSELIST TO GET INDEX
                            int size = classChooser.getItemCount();
                            for (int num=0; num<size; num++) {
                                //IF THE CLASS NAME EQUALS ONE OF THE ITEM CHOOSERS:
                                if ( course.name.equals(classChooser.getItemAt(num)) ) {
                                    //ADD THE COURSE TO THE LIST
                                    addCourse(course, num);
                                    //UPDATE THE TABLE
                                    loadClass(num);
                                    //DESTROY THE POPUP WINDOW
                                    popup.dispose(); 
                                    break;
                                }                            
                            }
                            //IF A COURSE WAS NOT CREATED: MEANS THAT THERE ARE NO COURSES WITH THE SAME NAME
                            warnLabel.setText(" @ Can't create course, check that the course exists..");
                        }
                        else 
                            //IF A COURSE CANNOT BE CREATED:
                            warnLabel.setText(" @ Can't create course, check that all required fields are set..");                                           
                    }
                });
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //GET THE SELECTED CLASS FROM THE CLASS CHOOSER
                final int selectedList = classChooser.getSelectedIndex();
                //GET THE SELECTED ROW FROM THE TABLE
                final int selectedRow = previewTable.getSelectedRow();
                //IF A ROW IS SELECTED CONTINUE
                if (selectedRow >= 0) {
                    //GET THE COURSE OBJECT
                    ArrayList<Course> list = (ArrayList) courseList.get(selectedList);
                    final Course selectedCourse = list.get(selectedRow);
                    
                    //CREATE A DIALOG BOX AND ITS COMPONENTS
                    final JDialog popup = new JDialog();
                    final JLabel warnLabel = new JLabel();
                    final TimesPanel app = new TimesPanel();
                    JButton finishButton = new JButton("Click Here When Finished");
                    
                    //ADD COMPONENTS AND SET LAYOUT    
                    popup.setSize(400, 285);
                    popup.setLayout(new BorderLayout());
                    popup.add(app, BorderLayout.CENTER);
                    popup.add(finishButton, BorderLayout.PAGE_END);
                    popup.add(warnLabel, BorderLayout.PAGE_START);
                    //SHOW THE POPUP WINDOW
                    popup.setVisible(true);
                    //LOAD THE COURSE INTO THE TIMES PANEL
                    app.loadCourse(selectedCourse);   
                    //SET THE TEXT FIELD TO THE COURSE'S NAME AND MAKE IT NON-EDITABLE
                    app.classNameUI.setText(selectedCourse.name);
                    app.classNameUI.setEnabled(false);
                    
                    finishButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            //IF A COURSE CAN BE CREATED
                            if (app.canCreateCourse() == 0) {
                                //CREATE A COURSE OBJECT
                                Course course = app.createCourse();
                                
                                if (course.name.equals(selectedCourse.name)) {
                                    
                                    //REMOVE THE COURSE FROM THE COURSE LIST AND THE TABLE
                                    removeCourse(selectedList, selectedRow);
                                    //ADD THE COURSE OBJECT TO THE COURSE LIST
                                    addCourse(course, selectedList);
                                    //UPDATE THE TABLE
                                    loadClass(selectedList);
                                    //DESTROY THE POPUP WINDOW
                                    popup.dispose();
                                }
                                warnLabel.setText(" @ Could not create course, make sure that you don't change" +
                                        "the class name");
                            }
                            warnLabel.setText("@ Could not create course, check that all" +
                                    "required fields are set");
                        }
                    });
                    
                }
            }
        });
        
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int selectedRow = previewTable.getSelectedRow();
                if (selectedRow >= 0)
                    removeCourse(selectedRow);
            }
        });
        
        classChooser.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                //GET THE SELECTED ROW
                int index = classChooser.getSelectedIndex();
                //LOAD THE SELECTED COURSE-LIST USING THE INDEX
                loadClass(index);
            }
        });
    }
    
    
    public void loadClass(int index) {
        //LOAD A COURSE INTO THE TABLE
        try {
            int rowCount = tableModel.getRowCount();
            for (int i=0; i<rowCount; i++) {
                tableModel.removeRow(0); //REMOVE ROWS
            }
            
            Object[] rowData;            
            ArrayList objList = (ArrayList) courseList.get(index);
            Object[] objArray = objList.toArray(new Object[objList.size()]);
            
            for(Object courseObj : objArray) {
                
                Course course = (Course) courseObj;
                
                rowData = new Object[5];
                rowData[0] = course.section;
                rowData[1] = course.isOptional;

                for (int i = 0; i < 3; i++) {
                    try {
                        rowData[i + 2] = course.getTimeSlot(i).stringStartTime + "-" + 
                                course.getTimeSlot(i).stringEndTime + " " +
                                course.getTimeSlot(i).getDays();
                                //+ " " + course.getTimeSlot(i).getDays();
                    } catch (Exception e) {
                        rowData[i + 2] = "NONE";
                    }
                }
                tableModel.addRow(rowData);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        //UPDATE THE GUI
        validate();
    }
    
    public void appendData(ArrayList courseList, int index) {
        /**Appends the list to the appropriate courseList at the given index in the previewPanel*/
        //ITERATE THROUGH THE COURSE LIST AND ADD EACH COURSE
        for (Object courseObj : courseList) {
            
            Course course = (Course) courseObj;
            
            addCourse(course, index);
        }
        //UPDATE THE TABLE
        loadClass(index);
    }
    
    public void addCourse(Course course, int listIndex) {
        //GET THE LIST WITH THE DESIRED INDEX
        ArrayList<Course> list = (ArrayList) courseList.get(listIndex);
        //ADD THE COURSE TO THIS LIST
        list.add(course);
    }
    
    public void removeCourse(int listIndex, int courseIndex) {
        //REMOVE COURSE FROM LIST
        ArrayList list = (ArrayList) courseList.get(listIndex);
        list.remove(courseIndex);
        //UPDATE THE TABLE MODEL
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.removeRow(courseIndex);
        
        //IF THERE ARE NO MORE ROWS REMOVE THE WHOLE LIST AND REMOVE CLASS FROM CHOOSER
        if (model.getRowCount() == 0) {
            courseList.remove(listIndex);
            classChooser.removeItemAt(listIndex);
            classChooser.validate();
            loadClass(--listIndex);
        }
    }
    
    public void removeCourse(int courseIndex) {
        //GET THE APPROPRIATE LIST INDEX
        int listIndex = classChooser.getSelectedIndex();
        //GET THE LIST REFERENCE
        ArrayList list = (ArrayList) courseList.get(listIndex);
        //REMOVE THE COURSE FROM THE LIST
        list.remove(courseIndex);
        //UPDATE THE TABLE MODEL
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.removeRow(courseIndex);
        
        //IF THERE ARE NO MORE ROWS REMOVE THE WHOLE LIST AND REMOVE CLASS FROM CHOOSER
        if (model.getRowCount() == 0) {
            courseList.remove(listIndex);
            classChooser.removeItemAt(listIndex);
            classChooser.validate();
            loadClass(--listIndex);
        }
    }
    
    public int getEqualIndex(String courseName) {
        
        for (int i=0; i<classChooser.getItemCount(); i++) {
            
            Object item = classChooser.getItemAt(i);
            String chooserName = (String) item;
            
            if (chooserName.equalsIgnoreCase(courseName))
                return i;
        }
        
        return -1;
    }
    
    public ArrayList getCourseList(int index) {
        return (ArrayList) courseList.get(index);
    }
}
