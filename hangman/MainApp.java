package hangman;

import java.awt.Color;

import javax.swing.JFrame;

public class MainApp {
	public static void main(String[] args) {

		Game frame = new Game();
		frame.setTitle("****** THE HANGMAN GAME ******");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color (40,40,40));
		frame.setVisible(true);
		
	}
}