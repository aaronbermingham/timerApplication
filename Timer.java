
import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import net.miginfocom.swing.MigLayout;

public class Timer extends JFrame implements ActionListener {
	int numClick = 0;
	int pauseCount = 0;
	TimerDialog tD;
	Thread thrd;
	int numClicks = 0;
	JFileChooser jfc = new JFileChooser();
	// Interface components

	// Fonts to be used
	Font countdownFont = new Font("Arial", Font.BOLD, 20);
	Font elapsedFont = new Font("Arial", Font.PLAIN, 14);

	// Labels and text fields
	JLabel countdownLabel = new JLabel("Seconds remaining:");
	JTextField countdownField = new JTextField(15);
	JLabel elapsedLabel = new JLabel("Time running:");
	JTextField elapsedField = new JTextField(15);
	JButton startButton = new JButton("START");
	JButton pauseButton = new JButton("PAUSE");
	JButton stopButton = new JButton("STOP");
	
	// The text area and the scroll pane in which it resides
	JTextArea display;

	JScrollPane myPane;

	// These represent the menus
	JMenuItem saveData = new JMenuItem("Save data", KeyEvent.VK_S);
	JMenuItem displayData = new JMenuItem("Display data", KeyEvent.VK_D);

	JMenu options = new JMenu("Options");

	JMenuBar menuBar = new JMenuBar();

	// These booleans are used to indicate whether the START button has been clicked
	boolean started;

	// and the state of the timer (paused or running)
	boolean paused;

	// Number of seconds
	long totalSeconds = 0;
	long secondsToRun = 0;
	long secondsSinceStart = 0;

	// This is the thread that performs the countdown and can be started, paused and
	// stopped
	TimerThread countdownThread;

	// Interface constructed
	Timer() {

		setTitle("Timer Application");

		MigLayout layout = new MigLayout("fillx");
		JPanel panel = new JPanel(layout);
		getContentPane().add(panel);

		options.add(saveData);
		options.add(displayData);
		menuBar.add(options);

		panel.add(menuBar, "spanx, north, wrap");

		MigLayout centralLayout = new MigLayout("fillx");

		JPanel centralPanel = new JPanel(centralLayout);

		GridLayout timeLayout = new GridLayout(2, 2);

		JPanel timePanel = new JPanel(timeLayout);

		countdownField.setEditable(false);
		countdownField.setHorizontalAlignment(JTextField.CENTER);
		countdownField.setFont(countdownFont);
		countdownField.setText("00:00:00");

		timePanel.add(countdownLabel);
		timePanel.add(countdownField);

		elapsedField.setEditable(false);
		elapsedField.setHorizontalAlignment(JTextField.CENTER);
		elapsedField.setFont(elapsedFont);
		elapsedField.setText("00:00:00");

		timePanel.add(elapsedLabel);
		timePanel.add(elapsedField);

		centralPanel.add(timePanel, "wrap");

		GridLayout buttonLayout = new GridLayout(1, 3);

		JPanel buttonPanel = new JPanel(buttonLayout);

		buttonPanel.add(startButton);
		buttonPanel.add(pauseButton, "");
		buttonPanel.add(stopButton, "");
		/* 
		 * Pause & stop initially set to false
		 * Stops user from using them before 
		 * a timer is set
		 * */
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		
		centralPanel.add(buttonPanel, "spanx, growx, wrap");

		panel.add(centralPanel, "wrap");

		display = new JTextArea(100, 150);
		display.setMargin(new Insets(5, 5, 5, 5));
		display.setEditable(false);

		JScrollPane myPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		panel.add(myPane, "alignybottom, h 100:320, wrap");
		// add action listener
		startButton.addActionListener(this);
		pauseButton.addActionListener(this);
		stopButton.addActionListener(this);
		saveData.addActionListener(this);
		displayData.addActionListener(this);

		// Initial state of system
		paused = false;
		started = false;

		// Allowing interface to be displayed
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	// TODO: SAVE: This method should allow the user to specify a file name to which
	// to save the contents of the text area using a
	// JFileChooser. You should check to see that the file does not already exist in
	// the system.
	// Use information from this links:
	// https://www.tutorialspoint.com/java/io/file_exists.htm
	// https://stackoverflow.com/questions/26569921/checking-if-file-exists-when-saving-file-in-java-swing
	// https://docs.oracle.com/javase/7/docs/api/java/io/File.html#exists()
	public void save() {
		String time = display.getText(); // get text from display text area
		File file = new File(System.getProperty("user.dir")); // Set current directory 
		jfc = new JFileChooser(file);
		jfc.setCurrentDirectory(file);
		int returnVal = jfc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().exists()) { // Check if file exists
				int exists = JOptionPane.showConfirmDialog(null, // if it does, ask user do they wish to overwrite the file
						"Do you want to overwrite the existing file?", //
						"Confirm", JOptionPane.YES_NO_OPTION, //
						JOptionPane.QUESTION_MESSAGE);

				if (exists != JOptionPane.YES_OPTION) { // if not, continue
					return;
				} else {
					file = jfc.getSelectedFile();
					//System.out.println("Saving: " + file.getName() + "." + "\n");
				}
			}
			try {
				FileWriter fw = new FileWriter(jfc.getSelectedFile()); // fw is set to the selected file
				fw.write(time); // write time to the file
				fw.close(); // close the file writer
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

	}

	// TODO: DISPLAY DATa: This method should retrieve the contents of a file
	// representing a previous report using a JFileChooser.
	// The result should be displayed as the contents of a dialog object.
	public void display() {
		JTextArea txtTime = new JTextArea(5, 20); // set text area size
		File file = new File(System.getProperty("user.dir")); // file set to current directory
		jfc = new JFileChooser(file);
		jfc.setCurrentDirectory(file); // file chooser set to current directory
		int returnVal = jfc.showOpenDialog(this); // opens file chooser dialog
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = jfc.getSelectedFile(); // file is equal to the selected file
			FileReader fr;
			/*
			 * Read in the chosen file and display it
			 * in a dialog
			 * */
			try {
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				txtTime.read(br, null); // Set this string equal to the file that is read in
				JOptionPane.showMessageDialog(null, new JScrollPane(txtTime), "Time", JOptionPane.PLAIN_MESSAGE);
				br.close(); // NB close the buffered reader
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// TODO: START: This method should check to see if the application is already
	// running, and if not, launch a TimerThread object.
	// If the application is running, you may wish to ask the user if the existing
	// thread should be stopped and a new thread started.
	// It should begin by launching a TimerDialog to get the number of seconds to
	// count down, and then pass that number of seconds along
	// with the seconds since the start (0) to the TimerThread constructor.
	// It can then display a message in the text area stating how long the countdown
	// is for.
	public void startTimer() {
		display.setText(""); // Clear display text area
		tD = new TimerDialog(this, secondsToRun, true); // New instance of timer dialog
		secondsToRun = tD.getSeconds(); // get seconds to run from the timer dialog
		tD.dispose(); // close the timer dialog
		countdownThread = new TimerThread(countdownField, elapsedField, secondsToRun, secondsSinceStart); // New instance of TimerThread
		thrd = new Thread(countdownThread); // create thread, pass countdownThread in as a parameter
		thrd.start(); // start the thread
		pauseButton.setEnabled(true); // set pause button to enabled, user can now use this functionality
		stopButton.setEnabled(true);	// set stop button to enabled, user can now use this functionality
		display.append("Countdown for " + tD.getSeconds() + " seconds"); // Append the total countdown seconds to the text area display

	}

	// TODO: PAUSE: This method should call the TimerThread object's pause method
	// and display a message in the text area
	// indicating whether this represents pausing or restarting the timer.
	public void pause() {
		pauseCount++; // variable to hold number of times pause has been clicked
		/*
		 * if pause count is 1, set text to resume,
		 * pause the thread and append the time it
		 * was paused at to the text area display
		 * */
		if (pauseCount == 1) {
			pauseButton.setText("RESUME");
			countdownThread.pause();
			display.append(
					"\nPaused at: " + countdownThread.getElapsedSeconds() + " seconds");
			/*
			 * if pause count is 2, call the pause method
			 * which toggles paused (ie the timer resumes)
			 * change text back to Pause and resets the pause counter
			 * */
		} else if (pauseCount == 2) {
			countdownThread.pause();
			display.append("\nRestarted at " + countdownThread.getElapsedSeconds()
					+ " seconds");
			pauseButton.setText("PAUSE");
			pauseCount = 0;
		}
		
	}


	// TODO: STOP: This method should stop the TimerThread object and use
	// appropriate methods to display the stop time
	// and the total amount of time remaining in the countdown (if any).
	public void stop() {
		countdownThread.stop(); // Stop the thread, uses method in TimerThread
		display.append("\nRemaining time: " + countdownThread.getCountdownSeconds()
		+ " seconds"); // Append the remaining time to the display text area
		pauseButton.setEnabled(false); // disable pause button
		stopButton.setEnabled(false);	// disable stop button

	}

	public static void main(String[] args) {

		Timer timer = new Timer();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == startButton) {
			numClick++; // variable to hold number of times start is clicked
			if (numClick == 1) 
				startTimer(); // call to start timer method
			/*
			 * If the button is clicked again, asks user
			 * if they want to start a new timer which will cancel
			 * timer that is running
			 * */
			else if (numClick == 2) {
				int option = JOptionPane.showConfirmDialog(null,
						"Do you want to start a new timer? Doing so will stop the current timer", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					/* 
					 * if the current timer was paused,reset pause count
					 * change the text back to pause
					 * */
					if(pauseButton.getText().equals("RESUME")) {
					pauseButton.setText("PAUSE");
					pauseCount = 0;
					}
					countdownThread.stop(); // stop the current timer
					startTimer(); // start a new timer
				} else {
					return;
				}
				numClick = 1; // set number of clicks to one, in case user wants to start another timer
			}
		} else if (e.getSource() == pauseButton) {
			
				pause(); // call to pause method
				
				
		}

		else if (e.getSource() == stopButton) {
			stop(); // call to stop method
			
		} else if (e.getSource() == saveData) {
			/*
			 * Stop user saving if there is no details to save
			 * */
			if(display.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "There is no timer information to save!", "Save warning",
						JOptionPane.WARNING_MESSAGE);
			}
			/*
			 * Stop using from saving while
			 * the countdown is still running,
			 * prompts them to stop the thread if
			 * they want to save
			 * */
			else if (countdownThread.isRunning() == false)
				save();
			else
				JOptionPane.showMessageDialog(this, "You must stop the timer if you wish to save!!", "Save warning",
						JOptionPane.WARNING_MESSAGE);
		} else if (e.getSource() == displayData) {
			display(); // call to display method
		}

	}

}
