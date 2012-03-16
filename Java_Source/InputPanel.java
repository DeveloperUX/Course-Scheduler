
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InputPanel extends JPanel {

    private TimesPanel timesPanel;
    public JTable previewTable;
    private JButton addButton;
    private JButton edit;
    private JButton remove;
    public JButton nextClass;
    private JScrollPane scrollPane;
    private ArrayList<Course> courseList;
    public boolean isPreviewPanelCreated;

    public InputPanel() {
        init();
        addComponents();
        run();
    }

    public void init() {
        timesPanel = new TimesPanel();
        timesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TIMES"));

        DefaultTableModel miniTableModel = new DefaultTableModel();
        miniTableModel.addColumn("Name");
        miniTableModel.addColumn("Section");
        miniTableModel.addColumn("Optional");
        miniTableModel.addColumn("Lecture");
        miniTableModel.addColumn("Lab");
        miniTableModel.addColumn("Recitation");

        previewTable = new JTable(miniTableModel) {

            @Override //MAKE THE CELLS NOT EDITABLE
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        //SET THE TABLES COLUMN SIZES
        previewTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        previewTable.getColumnModel().getColumn(1).setPreferredWidth(25);
        previewTable.getColumnModel().getColumn(2).setPreferredWidth(25);

        scrollPane = new JScrollPane(previewTable);
        previewTable.setPreferredScrollableViewportSize(new Dimension(400, 70));

        addButton = new JButton("Add");
        edit = new JButton("Edit");
        remove = new JButton("Remove");
        nextClass = new JButton("Next Class");

        addButton.isDefaultButton();
        courseList = new ArrayList<Course>();
        isPreviewPanelCreated = false;
    }

    public void addComponents() {
        //SET THE LAYOUT SO THAT BOTH PANEL ARE THE SAME SIZE
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //THIS PANEL WILL HOLD ALL THE BUTTONS AND MAKE THEM THE SAME SIZE
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 1));
        buttonsPanel.add(addButton);
        buttonsPanel.add(edit);
        buttonsPanel.add(remove);
        buttonsPanel.add(nextClass);

        JPanel tableButtonPanel = new JPanel();  //THIS WILL HOLD THE TABLE AND THE BUTTONS
        tableButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        //ADD THE TABLE AND THE BUTTONS PANEL TO THE "tableButtonPanel"
        tableButtonPanel.add(scrollPane);
        tableButtonPanel.add(buttonsPanel);

        timesPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        add(timesPanel);
        add(tableButtonPanel);
        
        //updateButtons(null);
    }

    public void run() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addButtonClicked();
            }
        });
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                int selectedRow = previewTable.getSelectedRow();
                //IF THE EDIT BUTTON SAYS: edit, LOAD THE COURSE
                if (edit.getText().equals("Edit")) {
                    if (selectedRow>=0) { //MAKE SURE A ROW IS SELECTED
                        //LOAD THE COURSE AND DISABLE ALL OTHER BUTTONS
                        timesPanel.loadCourse(courseList.get(selectedRow));
                        setEditText("Done", false);
                    }
                } //IF IT DOES NOT ADD THE NEW COURSE AND REMOVE THE OLD ONE
                else {                    
                    addButtonClicked();
                    remove(selectedRow);
                    setEditText("Edit", true); //RESET THE BUTTONS TO ENABLED
                    
                }
            }
        });
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //GET THE SELECTED ROW INDEX AND REMOVE BOTH ROW AND COURSE OBJECT
                int selectedRow = previewTable.getSelectedRow();
                if (selectedRow >= 0)
                    remove(selectedRow);
            }
        });
        
    }

    public void addButtonClicked() {
        //CHECK IF ANY DAYS ARE CHECKED TO CREATE A COURSE
        int selection = timesPanel.canCreateCourse();
        switch (selection) {
            case 0:  //--CREATE COURSE
                Course course = timesPanel.createCourse();

                DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
                Object[] rowData = new Object[6];

                rowData[0] = course.name;
                rowData[1] = course.section;
                rowData[2] = course.isOptional;

                for (int i = 0; i < 3; i++) {
                    try {
                        rowData[i + 3] = course.getTimeSlot(i).stringStartTime + "-" + course.getTimeSlot(i).getDays();
                    } catch (Exception e) {
                        rowData[i + 3] = "NONE";
                    }
                }

                model.addRow(rowData);
                courseList.add(course);
                timesPanel.lastClassName = course.name;

                //CLEAR THE INFORMATION AFTER ADDING IT.
                timesPanel.clearButtonClicked(false);
                break;

            case 1: //--THE CLASS NAME IS BLANK
                JOptionPane.showMessageDialog(null, "You need to enter the name of the class",
                        "Error", JOptionPane.DEFAULT_OPTION);
                break;

            case 2: //--THE CLASS NAME IS DIFFERENT FROM THE PREVIOUS NAME
                JOptionPane.showMessageDialog(null, "The class name must be the same as the one before",
                        "Error", JOptionPane.DEFAULT_OPTION);
                break;

            case 3: //--THERE ARE NO DAYS SELECTED
                JOptionPane.showMessageDialog(null,
                        "You must check some days to create a valid course", "Error",
                        JOptionPane.DEFAULT_OPTION);
                break;
        }
    }

    public void edit(int selection) {
        try {
            timesPanel.loadCourse(courseList.get(selection));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select the class you would like to edit",
                    "Error", JOptionPane.DEFAULT_OPTION);
        }
        setEditText("Done", false);
        
    }
    
    public void setEditText(String text, boolean shouldEnable) {
        edit.setText(text);
        addButton.setEnabled(shouldEnable);
        remove.setEnabled(shouldEnable);
        nextClass.setEnabled(shouldEnable);
    }

    @Override
    public void remove(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.removeRow(selectedRow);
        courseList.remove(selectedRow);        
    }

    public void clearAll() {
        //REMOVE ALL ROWS FROM THE TABLE AND COURSELIST
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            model.removeRow(0);
        }
        //CLEAR ALL THE FIELDS OF THE TIMES PANEL
        timesPanel.clearButtonClicked(true);
        //CLEAR THE LAST CLASS NAME
        timesPanel.lastClassName = "";
    }
    
    public ArrayList<Course> getCourseList() {
        return courseList;
    }
}