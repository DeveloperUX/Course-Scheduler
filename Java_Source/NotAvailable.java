
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

public class NotAvailable extends JPanel {
    
    private JButton clear;
    private JButton addButton;
    private JButton edit;
    private JButton remove;
    
    private JLabel sentence;
    private JLabel labelLabel;
    private JTextField labelUI;
    private JCheckBox isOptionalUI;
    private JLabel timeLabel;
    private CustomSpinner timeStartUI;
    private CustomSpinner timeEndUI;
    private JLabel daysLabel;
    private JCheckBox[] daysListUI;
    private JTable previewTable;
    
    private ArrayList<NoTime> notAvailableList;
    
    public NotAvailable() {
        init();
        addComponents();
        run();
    }
    
    public void init() {
        Dimension dimension = new Dimension(80, 24);
        
        sentence = new JLabel("Enter the times at which you are not available to take courses:");
        notAvailableList = new ArrayList();
        
        labelLabel = new JLabel("Label:");
        labelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        labelUI = new JTextField();
        labelUI.setPreferredSize(new Dimension(90, 24));
        isOptionalUI = new JCheckBox("Optional");
        
        timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        timeStartUI = new CustomSpinner();
        timeEndUI = new CustomSpinner();
        
        //SET SPINNER SIZES
        timeStartUI.setPreferredSize(dimension);
        timeEndUI.setPreferredSize(dimension);
        
        daysLabel = new JLabel("Days:");
        daysLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        daysListUI = new JCheckBox[6];
        String[] columnNames = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
        int c = 0;
        for(String col : columnNames) {
            daysListUI[c] = new JCheckBox(col);
            c++;
        }
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Label");
        
        model.addColumn("Optional");
        model.addColumn("Time");
        model.addColumn("Days");
        previewTable = new JTable(model);
        
        clear = new JButton("Clear");
        addButton = new JButton("Add");
        edit = new JButton("Edit");
        remove = new JButton("Remove");
    }
    
    
    
    public void addComponents() {        
        //SET THE DIMENSIONS
        
        Dimension sepDimension = new Dimension(20, 5);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel timesPanel = new JPanel();
        timesPanel.setLayout(new GridLayout(3, 1));
        
        JPanel miniPanel_1 = new JPanel();
        miniPanel_1.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        miniPanel_1.add(labelLabel);
        miniPanel_1.add(labelUI);
        miniPanel_1.add(Box.createRigidArea(new Dimension(40, 0)));
        miniPanel_1.add(isOptionalUI);
        
        JPanel miniPanel_2 = new JPanel();
        miniPanel_2.setLayout(new FlowLayout(FlowLayout.LEADING));
        miniPanel_2.add(timeLabel);
        miniPanel_2.add(timeStartUI);
              
        JSeparator sep = new JSeparator();
        sep.setPreferredSize(sepDimension);       
        miniPanel_2.add(sep);
        
        miniPanel_2.add(timeEndUI);
        
        JPanel miniPanel_3 = new JPanel();
        miniPanel_3.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        miniPanel_3.add(daysLabel);
        for(JCheckBox checkBox : daysListUI)
            miniPanel_3.add(checkBox);
        
        timesPanel.setBorder(BorderFactory.createTitledBorder("Not Available"));
        
        miniPanel_1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        miniPanel_2.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 20));
        miniPanel_3.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        timesPanel.add(miniPanel_1);
        timesPanel.add(miniPanel_2);
        timesPanel.add(miniPanel_3);
        
        //CREATE PREVIEW PANEL AND BUTTONS
        JPanel previewPanel = new JPanel();
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        
        JScrollPane scrollPane = new JScrollPane(previewTable);
        previewTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
        previewPanel.add(scrollPane);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        buttonsPanel.add(clear);
        buttonsPanel.add(addButton);
        buttonsPanel.add(edit);
        buttonsPanel.add(remove);
        
        JPanel timesButtonsPanel = new JPanel();
        timesButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        timesButtonsPanel.add(timesPanel);
        timesButtonsPanel.add(buttonsPanel);
        
        JPanel sentencePanel = new JPanel();
        sentencePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        sentencePanel.add(sentence);
        
        add(sentencePanel);
        add(Box.createHorizontalGlue());
        add(timesButtonsPanel);
        add(previewPanel);
    }
    
    public void run() {
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearButtonClicked();
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addButtonClicked();
            }
        });
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int row = previewTable.getSelectedRow();
                editButtonClicked(row);
            }
        });
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeButtonClicked();
            }
        });
    }
        
    public void clearButtonClicked() {
        labelUI.setText("");
        isOptionalUI.setSelected(false);
        
        timeStartUI.resetValue();
        timeEndUI.resetValue();
        
        for(JCheckBox checkBox : daysListUI)
            checkBox.setSelected(false);
    }
    
    public void addButtonClicked() {
        NoTime noTime = null;
        //CREATE NO TIME OBJECT
        try { 
            String label = labelUI.getText();
            boolean optional = isOptionalUI.isSelected();

            Object startObject = timeStartUI.getValue();
            Object endObject = timeEndUI.getValue();

            boolean[] daysArray = new boolean[6];
            for(int index=0; index<daysListUI.length; index++)
                daysArray[index] = daysListUI[index].isSelected();

            TimeSlot timeslot = new TimeSlot(startObject, endObject, daysArray);

            noTime = new NoTime(label, optional, timeslot);
            notAvailableList.add(noTime);
        } catch(Exception e) {
            System.out.println("COULD NOT CREATE TIME");
        }
        
        //ADD IT TO THE TABLE
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        Object[] row = new Object[4];
        row[0] = noTime.label;
        row[1] = noTime.optional;
        row[2] = noTime.getTime().stringStartTime + "-" + noTime.getTime().stringEndTime;
        row[3] = noTime.getTime().getDays();
        
        model.addRow(row);
        
        clearButtonClicked();
    }
    
    public void editButtonClicked(int selectedRow) {
        if (selectedRow >= 0) {
            //GET THE NOTIME OBJECT WITH THE SELECTEDROW INDEX
            NoTime noTime = notAvailableList.get(selectedRow);
            //LOAD THE SELECTED ROW
            
            labelUI.setText(noTime.label);
            isOptionalUI.setSelected(noTime.optional);
            //SET THE CALENDAR FOR THE SPINNERS
            int START = 0;  int END = 1;
            timeStartUI.setValue(noTime.getTime(), START);
            timeEndUI.setValue(noTime.getTime().endHour, noTime.getTime().endMin);
            //ITERATE THROUGH THE DAYS AND SELECT THOSE THAT NEED TO BE SELECTED
            
            
            //DELETE IT FROM THE LIST
            DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
            model.removeRow(selectedRow);
        }
    }
    
    public void removeButtonClicked() {
        try {
            //REMOVE THE SELECTED ROW
            int selectedRow = previewTable.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
            model.removeRow(selectedRow);
            //REMOVE THE TIME OBJECT FROM THE LIST
            notAvailableList.remove(selectedRow);
        } catch(Exception e) {
            System.out.println("NO ROW SELECTED");
        }
    }
    
    public NoTime getNoTime(int index) {
        return notAvailableList.get(index);
    }
}
