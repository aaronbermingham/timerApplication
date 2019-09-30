
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TimerThread implements Runnable {

	// booleans indicating whether the timer is running, and whether the pause
	// button has been pressed.
	boolean running;
	boolean paused;

	// text fields to be updated.
	JTextField countdownField;
	JTextField elapsedField;

	// counters of seconds
	long elapsedSeconds;
	long countdownSeconds;

	// The constructor:
	// This should set up initial values for the text fields, booleans and start
	// time.
	// It initialises the text fields to be updated along with countdownSeconds and
	// elapsedSeconds.
	// It can then start the thread.
	TimerThread(JTextField countdownField, JTextField elapsedField, long secondsToRun, long secondsSinceStart) {
		this.countdownField = countdownField;
		this.elapsedField = elapsedField;
		this.countdownSeconds = secondsToRun;
		this.elapsedSeconds = secondsSinceStart;

		running = true;
		paused = false;

	}

	// This needs to update the text fields for countdown and elapsed seconds every
	// second.
	// The thread should run until countdownSeconds is 0. If the pause button is
	// activated, the number of elapsed
	// seconds will increase by 1 every second, whereas if pause is not activated,
	// elapsed seconds will be increased
	// by 1 each second, while the countdown seconds should similarly decrease by 1.
	// The text fields should be updated
	// each second. When countdownSeconds reaches 0, a message should be displayed
	// saying that time is up.

	// Used this link as a reference for this method
	// https://stackoverflow.com/questions/16758346/how-pause-and-then-resume-a-thread
	public synchronized void run() {
		// while the thread is running & not paused & the countdown is greater than 0
		while (isRunning() && !(isPaused()) && countdownSeconds > 0) {
			this.countdownSeconds = this.countdownSeconds - 1; // decrement the countdown time by one
			this.elapsedSeconds = this.elapsedSeconds + 1; // increment the elapsed seconds by one
			setText(); // call to setText method
			try {
				Thread.sleep(1000); // slows the counter by 1000 ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (this.countdownSeconds == 0) {
				JOptionPane.showMessageDialog(null, "TIME UP"); // If the counter reaches zero, display a dialog informing user that the time is up
			}

		 //Check if the thread has been paused
					if (paused) {
							while (paused) {
								//Continue to increment elapsed seconds while the thread is paused
								this.elapsedSeconds = this.elapsedSeconds + 1;
								String elpsdScnds = convertToHMSString(this.elapsedSeconds);
								this.elapsedField.setText(elpsdScnds);
								try {
									Thread.sleep(1000); // slows the counter by 1000 ms
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
						}

					}	
		}
		this.notifyAll(); // Notify all
	}

	// Method that sets the text boxes with the countdown and elapsed seconds
	public void setText() {
		String cntdwnScnds = convertToHMSString(this.countdownSeconds); // Convert seconds to hours,mins,secs
		String elpsdScnds = convertToHMSString(this.elapsedSeconds); // Convert seconds to hours,mins,secs
		this.countdownField.setText(cntdwnScnds); // Set countdown field to the string cntdwnScnds
		this.elapsedField.setText(elpsdScnds); // Set elapsed Field to the string elpsdScnds
	}

	// This should pause or resume the application depending on the current value of
	// paused.
	public void pause() {
		paused = !paused; // toggle boolean paused from paused to not paused
	}
	

	// This should stop the application.
	public void stop() {
		running = false; // sets running to false, the countdown is stopped
		paused = false; // sets paused to false, the elapsed time count is stopped
	}

	// These methods can be used by the Timer class to get the values of elapsed and
	// countdown seconds.
	public long getElapsedSeconds() {
		return elapsedSeconds;
	}

	public long getCountdownSeconds() {
		return countdownSeconds;
	}

	// This method is to convert a long integer representing the number of seconds
	// for which a thread has been running
	// into a display.
	// You could also use DateFormat.
	public String convertToHMSString(long seconds) {
		long secs, mins, hrs;
		// String to be displayed
		String returnString = "";

		// Split time into its components
		secs = seconds % 60;
		mins = (seconds / 60) % 60;
		hrs = (seconds / 60) / 60;

		// Insert 0 to ensure each component has two digits
		if (hrs < 10) {
			returnString = returnString + "0" + hrs;
		} else
			returnString = returnString + hrs;
		returnString = returnString + ":";

		if (mins < 10) {
			returnString = returnString + "0" + mins;
		} else
			returnString = returnString + mins;
		returnString = returnString + ":";

		if (secs < 10) {
			returnString = returnString + "0" + secs;
		} else
			returnString = returnString + secs;

		return returnString;

	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public JTextField getCountdownField() {
		return countdownField;
	}

	public void setCountdownField(JTextField countdownField) {
		this.countdownField = countdownField;
	}

	public JTextField getElapsedField() {
		return elapsedField;
	}

	public void setElapsedField(JTextField elapsedField) {
		this.elapsedField = elapsedField;
	}

	public void setElapsedSeconds(long elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}

	public void setCountdownSeconds(long countdownSeconds) {
		this.countdownSeconds = countdownSeconds;
	}

}
