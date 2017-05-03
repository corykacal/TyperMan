import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
public class TyperManGame extends JPanel implements KeyListener, ActionListener {

	
	JTextField currentString;
	JTextArea pointBox;
	ArrayList<String> bank;
	ArrayList<FallingWord> wordsOnBoard;
	private int points;
	private Timer time;
	private int currentTime;
	private int difficulty;
	
	public TyperManGame() throws FileNotFoundException {
		setSize(400,400);
		setLayout(null);
		bank = Dictionary.getWords("words.txt");
		setBackground(Color.WHITE);
		currentString = new JTextField("");
		currentString.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendString();
			}
			
		});
		currentString.setSize(400, 20);
		currentString.setLocation(0, 352);
		currentString.setBackground(Color.BLUE);
		currentString.setEditable(true);
		
		pointBox = new JTextArea("");
		pointBox.setEditable(false);
		pointBox.setSize(60,30);
		pointBox.setBackground(Color.GREEN);
		pointBox.setLocation(150, 10);
		
		add(pointBox);
		add(currentString);
		setVisible(true);
		time = new Timer(100, this);
		startNewGame();
	}
	
	public void startNewGame() {
		points = 0;
		currentTime = 0;
		wordsOnBoard = new ArrayList<FallingWord>();
		difficulty = 0;
		time.start();
	}
	
	public void sendString() {
		String entry = currentString.getText();
		currentString.setText("");
		if(wordIsOnBoard(entry)) {
			points = points + entry.length() + difficulty;
			pointBox.setText(""+points);
			removeWord(entry);
			updateUI();
		}
	}
	
	public boolean wordIsOnBoard(String entry) {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
		while(it.hasNext()) {
			FallingWord current = it.next();
			if(current.equals(entry)) {
				return true;
			}
		}
		return false;
	}

	private void removeWord(String entry) {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
		boolean found = false;
		while(it.hasNext() && !found) {
			FallingWord current = it.next();
			if(current.equals(entry)) {
				remove(current.box);
				it.remove();
				found = true;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		currentTime++;
		moveAllDown();
		if(collison()) {
			endGame();
		}
		if(difficulty < 6) {
			if(currentTime%40==0) {
				difficulty++;
				makeNewWord();
			}
		} else if(difficulty < 12) {
			if(currentTime%30==0) {
				difficulty++;
				makeNewWord();
			}
		} else if(difficulty < 19) {
			if(currentTime%20==0) {
				difficulty++;
				makeNewWord();
			}
		} else {
			if(currentTime%10==0) {
				makeNewWord();
			}
		}
	}
	
	private void makeNewWord() {
			String randomWord = getRandomWord();
			FallingWord newWord = new FallingWord(randomWord, 3);
			wordsOnBoard.add(newWord);
	}

	private void endGame() {
		time.stop();
	}

	public boolean collison() {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
		while(it.hasNext()) {
			FallingWord current = it.next();
			if(current.atBottom()) {
				return true;
			}
		}
		return false;
	}


	private void moveAllDown() {
		java.util.Iterator<FallingWord> it = wordsOnBoard.iterator();
		while(it.hasNext()) {
			FallingWord current = it.next();
			current.moveDown();
		}
		updateUI();
	}

	private String getRandomWord() {
		Random ran = new Random();
		int randomIndex = ran.nextInt(bank.size());
		return bank.get(randomIndex);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		;
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		;
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
		
	}
	
	private class FallingWord {
		
		private String word;
		private JTextField box;
		private int boxVel;
		private int xLoc;
		private int yLoc;
		
		
		public FallingWord(String word, int boxVel) {
			Random ran = new Random();
			xLoc = ran.nextInt(300);
			yLoc = 0;
			this.word = word;
			this.boxVel = boxVel;
			createBox();
		}
		
		public boolean atBottom() {
			if(yLoc >=340) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean equals(Object other) {
			if(other instanceof String) {
				String otherword = (String) other;
				return this.word.equals(otherword);
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return word.hashCode();
		}
		
		public void moveDown() {
			yLoc = yLoc + boxVel;
			box.setLocation(xLoc, yLoc);
		}
		
		public void createBox() {
			box = new JTextField(word);
			box.setLocation(xLoc, yLoc);
			box.setSize(9*word.length(), 30);
			add(box);
		}
	}
	
}
