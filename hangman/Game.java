package hangman;
import javax.swing.*;
import java.io.PrintWriter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//An ArrayList that will hold the words on the hangman.txt file:
	ArrayList<String> wordsArrayList = new ArrayList<>();

	private String currentWord;
	private char[] wordState;
	private int misses;
	//Declaring an ArrayList to store the entered characters of each randomly picked word:
	private ArrayList<Character> enteredLetters;
	
	//Declaring the Gui components:
	private JLabel welcomeLabel;
	private JLabel instructionsLabel;
	
	private JLabel logoLabel;
	private JLabel wordLabel;
	private JLabel missesLabel;
	private JLabel picturePngLabel;
	private JTextField inputField;
	JButton enterBtn;
	private JLabel authorsLabel;
	
	JPanel upperPanel;
	JPanel centralPanel;
	JPanel centralComponentsPanel;
	JPanel imagePanel;
	JPanel bottomPanel;
	ImageIcon logo;
	ImageIcon icon;
	Color darkGray1 = new Color (40,40,40);


	//Constructor:
	public Game() {

		scanWordsFromTxt();
		enteredLetters = new ArrayList<>();
		misses = 0;
		
		createGui();
		activateBtn();
		newGame();
	}
	
	//Method with the Try button Action Listener:
	private void activateBtn() {		
		enterBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guessLogic();
			}
		});
	}
	
	//Method to create the Graphical User interface (needs the class to extend JFrame):
	public void createGui() {
		setLayout(new BorderLayout(20, 40));
		
		//setBackground(darkGray1);
		//setForeground(new Color(255,255,255));//set color
		//To set a default font size for all text in the UIManager
        int fontSize = 14; // Change this to your desired font size
        Font newFont = UIManager.getFont("Label.font").deriveFont((float) fontSize);
        UIManager.put("Label.font", newFont);
        UIManager.put("TextField.font", newFont);
        UIManager.put("TextArea.font", newFont);
        UIManager.put("Button.font", newFont);
        
		logo = new ImageIcon("src\\hangman\\images\\hangmanlogo.png");
		logoLabel = new JLabel();
		logoLabel.setIcon(logo);
		welcomeLabel = new JLabel(" Are you daring enough to play?");
		welcomeLabel.setForeground(Color.gray);
		welcomeLabel.setBackground(darkGray1);
		instructionsLabel = new JLabel(" > You have 6 shots missed per word. Good Luck!");
		instructionsLabel.setForeground(Color.gray);
		wordLabel = new JLabel("?");
		wordLabel.setForeground(Color.white);
		missesLabel = new JLabel();
		missesLabel.setForeground(Color.white);
		picturePngLabel = new JLabel();
		inputField = new JTextField(2);
		enterBtn = new JButton("Try");
		upperPanel = new JPanel();
		centralPanel = new JPanel();
		centralComponentsPanel = new JPanel();
		centralComponentsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 5));
		imagePanel = new JPanel();
		imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 40));
		bottomPanel = new JPanel();
		authorsLabel = new JLabel(" Developed by Roger Paredes - OCT 2023 ");
		authorsLabel.setForeground(Color.gray);
		
		
		//upperPanel with a set height
		upperPanel.setPreferredSize(new Dimension(0, 200));
		upperPanel.add(logoLabel);
		upperPanel.add(welcomeLabel);
		upperPanel.add(instructionsLabel);
		upperPanel.setBackground(darkGray1);
		
		
		
		centralComponentsPanel.add(wordLabel);
		centralComponentsPanel.add(inputField);
		centralComponentsPanel.add(enterBtn);
		centralComponentsPanel.add(missesLabel);
		centralComponentsPanel.setBackground(darkGray1);
		centralPanel.add(centralComponentsPanel);
		imagePanel.add(picturePngLabel);
		imagePanel.setBackground(darkGray1);
		centralPanel.add(imagePanel);
		centralPanel.setBackground(darkGray1);
		bottomPanel.add(authorsLabel);
		bottomPanel.setBackground(darkGray1);
		
		add(upperPanel, BorderLayout.NORTH);
		add(centralPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		JButton addWordBtn = new JButton("Add Word");
		
		addWordBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        addWord();
		    }
		});
		
		
	}
	
	//Method reads the .txt file and updates the wordsArrayList:
	private void scanWordsFromTxt() {	
		try {
		File wordsFile = new File("src\\hangman\\hangman.txt");
		Scanner textScanner = new Scanner(wordsFile);
			
			//Loop to add each word as long as there is another line:
			while(textScanner.hasNextLine()) {
				wordsArrayList.add(textScanner.nextLine());
			}	
			textScanner.close();
		}	
		catch (java.io.FileNotFoundException ex ) {
			JOptionPane.showMessageDialog(null, "The hangman.txt source file with the words for the game was not found. Either Group1 or you messed up the files. :(", "Oh, snap! - ERROR", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
	}
	
	//The word randomizer:
	private String getRandomWord() {		
		String randomWord = wordsArrayList.get((int)(Math.random() * wordsArrayList.size()));
		return randomWord.toLowerCase();
	}
	
	//Method to start the game, invoke the random word and display "*" for its lenght
	private void newGame() {
		currentWord = getRandomWord();
		wordState = new char[currentWord.length()];
		for (int i = 0; i < currentWord.length(); i++) {
			wordState[i] = '*';
		}
		if (wordLabel != null) {
			wordLabel.setText(new String(wordState));
			misses = 0;
			enteredLetters.clear();
			missesLabel.setText("Misses: " + misses);
			
			// This is so there's a placeholder empty png in place and components alignment wont reorder after first miss:
			ImageIcon nullIcon = new ImageIcon("src\\hangman\\images\\hangman0.png");
			picturePngLabel.setIcon(nullIcon);
			inputField.setText("");
		}	
	}

	//Game's logic:
	private void guessLogic() {
		String input = inputField.getText().toLowerCase();
		if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
			JOptionPane.showMessageDialog(this, "Please enter a single letter from a to z.");
			return;
		}
		char guess = input.charAt(0);
		if (enteredLetters.contains(guess)) {
			JOptionPane.showMessageDialog(this, "You already tried this letter. PLease try another.");
			return;
		}
		enteredLetters.add(guess);

		boolean found = false;
		for (int i = 0; i < currentWord.length(); i++) {
			if (currentWord.charAt(i) == guess) {
				wordState[i] = guess;
				found = true;
			}
		}

		if (!found) {
			misses = misses + 1;
			updateHangmanPng();
		}

		wordLabel.setText(new String(wordState));
		missesLabel.setText("Misses: " + misses);

		if (new String(wordState).equals(currentWord)) {
			int option = JOptionPane.showConfirmDialog(this, "You won!!!!!!" + "\n" + "Play again?", "Congrats!",
					JOptionPane.YES_NO_OPTION);
			addWord();
			if (option == JOptionPane.YES_OPTION) {
				
				newGame();
				
			} else {
				System.exit(0);
			}
		} else if (misses >= 6) {
			JOptionPane.showMessageDialog(this, "You are Dead!!!!!!" + "\n" + "The word was: " + currentWord);
			int option = JOptionPane.showConfirmDialog(this, "Want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
			addWord();
			if (option == JOptionPane.YES_OPTION) {
				newGame();
				
			} else {
				System.exit(0);
			}
		}
	}
	//Method to change the image of the hangman according to misses:
	private void updateHangmanPng() {
		if (misses == 6) {
			icon = new ImageIcon("src\\hangman\\images\\hangman6.png");		
		}
		else if (misses == 5){
			icon = new ImageIcon("src\\hangman\\images\\hangman5.png");
		}
		else if (misses == 4){
			icon = new ImageIcon("src\\hangman\\images\\hangman4.png");	
		}
		else if (misses == 3){
			icon = new ImageIcon("src\\hangman\\images\\hangman3.png");	
		}
		else if (misses == 2){
			icon = new ImageIcon("src\\hangman\\images\\hangman2.png");	
		}			
		else if (misses == 1){
			icon = new ImageIcon("src\\hangman\\images\\hangman1.png");	
		}
		picturePngLabel.setIcon(icon);
	}
	
	private void addWord() {
	    String newWord = JOptionPane.showInputDialog(this, "Enter a new word to add to the list:");
	    
	    if (newWord != null && !newWord.isEmpty()) {
	        try {
	            // Open the text file for appending the new word
	            PrintWriter writer = new PrintWriter(new FileWriter("src\\hangman\\hangman.txt", true));
	            
	            // Add the new word to the file
	            writer.println(newWord);
	            
	            // Close the writer
	            writer.close();
	            
	            // Inform the user that the word has been added
	            JOptionPane.showMessageDialog(this, "Word added successfully.");
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "An error occurred while adding the word.");
	        }
	    }
	}

	

}
