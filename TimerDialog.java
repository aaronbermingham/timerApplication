

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

//import net.miginfocom.swing.MigLayout;


public class TimerDialog extends JDialog implements ActionListener  {
	//private Timer timer;
	// Represents the number of seconds that the countdown will be performed for.
	private long seconds;
	
	// Menu components.
	JTextField hourField, minField, secField;
	JLabel hourLabel, minLabel, secLabel;
	JButton startButton = new JButton("START");

	public TimerDialog(Frame owner, long seconds, boolean modality) {
		super(owner, modality);
		this.seconds = seconds;
		initComponents();
		
	}
	
	// Sets up display.
	private void initComponents() {
		setTitle("Initialise Timer");	
		setLayout(new BorderLayout());
		
		Font displayFont = new Font("Arial", Font.BOLD, 16);
		Font labelFont = new Font("Arial", Font.BOLD, 12);
		
		JPanel displayPanel = new JPanel(new GridLayout(1,3));
				
		JPanel hourPanel = new JPanel(new BorderLayout());
		hourField = new JTextField(5);
		hourField.setHorizontalAlignment(JTextField.CENTER);
		hourField.setFont(displayFont);
		hourField.setText("00");
		hourLabel = new JLabel("Hours");
		hourLabel.setHorizontalAlignment(JTextField.CENTER);
		hourLabel.setFont(labelFont);
		hourPanel.add(hourField, BorderLayout.CENTER);
		hourPanel.add(hourLabel, BorderLayout.SOUTH);
		
		displayPanel.add(hourPanel);
		
		JPanel minPanel = new JPanel(new BorderLayout());
		minField = new JTextField(5);
		minField.setHorizontalAlignment(JTextField.CENTER);
		minField.setFont(displayFont);
		minField.setText("00");
		minLabel = new JLabel("Minutes");
		minLabel.setHorizontalAlignment(JTextField.CENTER);
		minLabel.setFont(labelFont);
		minPanel.add(minField, BorderLayout.CENTER);
		minPanel.add(minLabel, BorderLayout.SOUTH);
		
		displayPanel.add(minPanel);
		
		JPanel secPanel = new JPanel(new BorderLayout());
		secField = new JTextField(5);
		secField.setHorizontalAlignment(JTextField.CENTER);
		secField.setFont(displayFont);
		secField.setText("00");
		secLabel = new JLabel("Seconds");
		secLabel.setHorizontalAlignment(JTextField.CENTER);
		secLabel.setFont(labelFont);
		secPanel.add(secField, BorderLayout.CENTER);
		secPanel.add(secLabel, BorderLayout.SOUTH);
		
		displayPanel.add(secPanel);
		
		add(displayPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.add(startButton);
		startButton.addActionListener(this);
		
		// TODO: This start action listener will be invoked when the start button is clicked.
		// It should take the values from the three text fields and try to convert them into integer values, and then check for NumberFormatExceptions 
		// and for the minute and second values between 0 and 59.
		
		
		add(buttonPanel, BorderLayout.SOUTH);
		/* Used information from these links:
		 * https://docs.oracle.com/javase/7/docs/api/java/awt/Window.html#setLocationRelativeTo(java.awt.Component)
		 * https://stackoverflow.com/questions/41027724/using-set-location-relative-method-to-set-the-jframe-to-center-still-appearing
		 * */
		this.pack(); // Makes the TimerDialog appear over the frame owner
		setLocationRelativeTo(this.getOwner()); // Makes the TimerDialog appear over the frame owner
		setSize(300, 150);
		setVisible(true);
		
	}
	
	public void start() {
		boolean valid = true;
		Long hours,mins,secs;
		try {
			hours = Long.parseLong(hourField.getText()); 
			mins = Long.parseLong(minField.getText()); 
			if(mins < 0 || mins > 59) {
				JOptionPane.showConfirmDialog(null,"Minute value must be between 0 and 59","Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				valid = false;
				minField.setText("00");
			}
			secs = Long.parseLong(secField.getText());
			if(secs < 0 || secs > 59) {
				JOptionPane.showConfirmDialog(null,"Second value must be between 0 and 59","Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				valid = false;
				secField.setText("00");
			}
			if(secs==0 && mins == 0 && hours ==0 ){
				JOptionPane.showConfirmDialog(null,"Please input a countdown time!\n Hours, minutes & seconds cannot all be zero","Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
			if (valid == false) {
				JOptionPane.showConfirmDialog(null,"Please input valid minutes or seconds","Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			}
			else {
			this.dispose();
			this.seconds = convertToSec(hours,mins, secs);
			}
			//System.out.println(convertToSec(hours,mins, secs));
			
		}
		 catch(NumberFormatException ne)
        {
			 JOptionPane.showConfirmDialog(null,"Error, you must input a valid time","Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
		
	}
	
	// Converts user input to seconds
	public long convertToSec(Long hours,Long mins ,Long secs) {
		Long conSec = (hours*3600)+(mins*60)+secs;
		return conSec;
	}
	
	
	
	public long getSeconds() {
		return (long)seconds;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == startButton) {
			start();
			
		}
		
	}

}



