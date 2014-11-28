package Assignment4;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MinesweeperGUI extends JFrame implements Serializable{

	private JFrame gameGUI, signIN;
	private JPanel boardPanel;
	private JButton[][] boardButtons = new JButton[10][10];
	private Game gameBoard = new Game();
	private Block[][] blockBoard = gameBoard.createNewBoard(); 


	private String userName;


	public MinesweeperGUI() {

		// Sign In Window/Prompt
		signIN = new JFrame("Enhanced Minesweeper");
		signIN.setSize(350, 200);
		signIN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // This will
		// close the
		// frame when
		// you press X.
		// IMPORTANT


		JLabel enterName = new JLabel("Enter your name:");
		final JTextField nameField = new JTextField(20);
		JPanel infoPanel = new JPanel();
		JButton submitName = new JButton("Enter");
		submitName.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent event) {
				userName = nameField.getText();
				signIN.dispose();
				gameGUI.setVisible(true);
			}
		});

		//JMenuBar menuBar = new JMenuBar();
		//	JMenu gameMenu = new JMenu("Game");

		infoPanel.add(enterName);
		infoPanel.add(nameField);
		infoPanel.add(submitName);
		signIN.add(infoPanel);
		signIN.setVisible(true);




		gameGUI = new JFrame("Enhanced Minesweeper"); // Title of the Window
		gameGUI.setSize(335, 450); // 400 550
		gameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // This will close the frame when you press X.
		// ^ IMPORTANT
		gameGUI.setResizable(false); //Disables Resize
		gameGUI.getContentPane().setBackground(Color.CYAN);
		gameGUI.setLayout(null);


		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");

		gameMenu.setMnemonic(KeyEvent.VK_G);

		JMenuItem gameNew = new JMenuItem("New Game(N)");
		gameNew.setMnemonic(KeyEvent.VK_N);
		gameNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gameGUI.remove(boardPanel);
				blockBoard = gameBoard.createNewBoard();


			}
		});

		JMenuItem gameLoad = new JMenuItem("Load Game(L)");
		gameLoad.setMnemonic(KeyEvent.VK_L);
		gameLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				blockBoard = readFromSerializedFile(new File("Board.dat"));

				for(int x = 0; x<blockBoard.length;x++){
					for(int y = 0; y<blockBoard[x].length;y++){
						if(blockBoard[x][y].isAlreadyChecked()==true){

							boardButtons[x][y].doClick();

						}
					}

				}

			}
		});

		JMenuItem gameSave = new JMenuItem("Save Game(S)");
		gameSave.setMnemonic(KeyEvent.VK_S);
		gameLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				writeToSerializedFile(new File("Board.dat"), blockBoard);
				System.out.println("saved");
			}
		});

		JMenuItem gameExit = new JMenuItem("Exit Game(E)");
		gameExit.setMnemonic(KeyEvent.VK_E);
		gameExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		JMenuItem gameHighScores = new JMenuItem("High Scores(H)");
		gameHighScores.setMnemonic(KeyEvent.VK_H);

		gameMenu.add(gameNew);
		gameMenu.add(gameLoad);
		gameMenu.add(gameSave);
		gameMenu.addSeparator();
		gameMenu.add(gameHighScores);
		gameMenu.addSeparator();
		gameMenu.add(gameExit);

		menuBar.add(gameMenu);
		gameGUI.setJMenuBar(menuBar);

		// Panel for Minesweeper board
		boardPanel = new JPanel();
		GridLayout experimentLayout = new GridLayout(10, 10);
		boardPanel.setLayout(experimentLayout);
		boardPanel.setLayout(new GridLayout(10,10));
		boardPanel.setBounds(10, 10, 300, 300);


		// Create GUI board
		if(blockBoard != null){
			for (int x = 0; x < boardButtons.length; x++) {
				for (int y = 0; y < boardButtons[x].length; y++) {

					boardButtons[x][y] = new JButton();


					// final because they will not change values
					final JButton clickedButton = boardButtons[x][y];
					final Block b = blockBoard[x][y];
					//final JTextArea c = new JTextArea();
					final int rows = x;
					final int column = y;

					boardButtons[x][y].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {


							// case of blank
							if (b instanceof Blank) {

								if (b.getNumOfMinesAround() == 0) {
									clickedButton.setVisible(false);
									checkForWhite(rows, column);

								}
								else
								{
									displayNumberBlock(clickedButton ,b.getNumOfMinesAround());
									gameBoard.step(b);
								}

							}

							// case of mines
							if (b instanceof Mines) {
								gameBoard.step(b);
								ImageIcon mineIMG = new ImageIcon(this.getClass().getResource("img/mine.png"));
								clickedButton.setIcon(mineIMG);
								clickedButton.setDisabledIcon(mineIMG);
								clickedButton.setEnabled(false); 

								if(!gameBoard.isHasLost()){
									if(gameBoard.getLives()==0){

										System.out.println("Last life");

									}
									if(gameBoard.getLives()<0 && gameBoard.getLives()==-1){
										
										revealBoard();
										gameBoard.gameOver();

									}
									if(gameBoard.getLives()>0){

										System.out.println(gameBoard.getLives()+" lives left");

									}
								} 

							}

							// case of treasure
							if (b instanceof Treasure) {
								gameBoard.step(b);
								clickedButton.setBackground(Color.GREEN);
								clickedButton.setEnabled(false);

							}

						}


					});

					// Add buttons to panel
					boardPanel.add(boardButtons[x][y]);

				}

			}//end of buttons



			gameGUI.add(boardPanel);
		}



	}

	protected void displayNumberBlock(JButton clickedButton, int numOfMinesAround) {
		StringBuilder filePath = new StringBuilder();
		filePath.append("img/").append(String.valueOf(numOfMinesAround)).append(".png");
		ImageIcon numIMG = new ImageIcon(this.getClass().getResource(filePath.toString()));
		clickedButton.setIcon(numIMG);
		clickedButton.setDisabledIcon(numIMG);
		clickedButton.setEnabled(false); 

	}


	// THE MAJESTIC RECURSION METHOD
	private void checkForWhite(int i, int j) {

		gameBoard.step(blockBoard[i][j]);
		// Checks that block is blank & 0
		if (blockBoard[i][j] instanceof Blank) {
			boardButtons[i][j].setVisible(false);
			blockBoard[i][j].setAlreadyChecked(true);
			for (int a = i - 1; a <= i + 1; a++) {
				for (int b = j - 1; b <= j + 1; b++) {

					try {

						if (!blockBoard[a][b].isAlreadyChecked()) {


							if (blockBoard[a][b] instanceof Blank) {
								if (blockBoard[a][b].getNumOfMinesAround() == 0) {
									checkForWhite(a, b);

								} else {	
									boardButtons[a][b].doClick();
									blockBoard[a][b].setAlreadyChecked(true);

								}
							}
							if(blockBoard[a][b] instanceof Treasure){
								boardButtons[a][b].doClick();
							}

						}
						
					} catch (Exception ArrayIndexOutOfBoundsException) {

					}

				}
			}
		}

	}


	public void revealBoard(){

		for (int x = 0; x <= 9;x++){
			for (int y = 0; y<= 9; y++){
				boardButtons[x][y].doClick();
			}

		}


	}

	/*
	 * For developing purpose, this will display answer
	 */

	public void gameboard() {
		for (int x = 0; x <= 9; x++) {
			for (int y = 0; y <= 9; y++) {
				if (blockBoard[x][y] instanceof Blank) {

					int ss = blockBoard[x][y].getNumOfMinesAround();
					System.out.print(ss);

				}
				if (blockBoard[x][y] instanceof Mines) {
					System.out.print("*");
				}
				if (blockBoard[x][y] instanceof Treasure) {
					System.out.print("T");
				}

			}
			System.out.println("");
		}
	}


	private static void writeToSerializedFile(File file, Block[][] blockBoard) {
		try {
			ObjectOutputStream output = new ObjectOutputStream(
					new FileOutputStream(file));
			output.writeObject(blockBoard);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Block[][] readFromSerializedFile(File file) {
		Block[][] blockBoard = null;
		try {
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream(file));
			blockBoard = (Block[][]) input.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return blockBoard;
	}

	public static void main(String[] args) {

		MinesweeperGUI mainInterface = new MinesweeperGUI();

		mainInterface.gameboard();



	}

}
