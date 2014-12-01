package Assignment4;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MinesweeperGUI extends JFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6572935174684904488L;

	private JFrame gameGUI, signIN, gameOverFrame, highScoreFrame,
	instructionFrame;
	private JPanel boardPanel, treasurePanel;
	private JButton autoWIN;
	private JButton[][] boardButtons = new JButton[10][10];
	private Game gameBoard = new Game();
	private Block[][] blockBoard = gameBoard.createNewBoard();
	private boolean winButton= false;
	private ImageIcon mineIMG = new ImageIcon(this.getClass().getResource(
			"img/mine.png"));
	private Set<Game> top10 = new TreeSet<Game>();

	public MinesweeperGUI() {

		if (new File("HighScores.dat").exists()) {
			top10 = readScoresFromSerializedFile(new File("HighScores.dat"));
		}
		// Sign In Window/Prompt
		signIN = new JFrame("Enhanced Minesweeper");
		signIN.setSize(350, 100);
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
				gameBoard.setUsername(nameField.getText());
				signIN.dispose();
				gameGUI.setVisible(true);
			}
		});

		infoPanel.add(enterName);
		infoPanel.add(nameField);
		infoPanel.add(submitName);
		signIN.add(infoPanel);
		signIN.setVisible(true);

		gameGUI = new JFrame("Enhanced Minesweeper"); // Title of the Window
		gameGUI.setSize(335, 500); // 400 550
		gameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // This will
		// close the
		// frame when
		// you press X.
		// ^ IMPORTANT
		gameGUI.setResizable(false); // Disables Resize
		gameGUI.getContentPane().setBackground(Color.CYAN);
		gameGUI.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu helpMenu = new JMenu("Help");

		gameMenu.setMnemonic(KeyEvent.VK_G);

		JMenuItem gameNew = new JMenuItem("New Game");
		gameNew.setMnemonic(KeyEvent.VK_N);
		gameNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				String name = gameBoard.getUsername();
				
				gameBoard = new Game(name);
				blockBoard = gameBoard.createNewBoard();
				
				
				
				boardPanel.setVisible(false);
				gameGUI.remove(boardPanel);
				

				boardButtons = new JButton[10][10];
				
				treasurePanel.setVisible(false);
				gameGUI.remove(treasurePanel);
				treasurePanel = new JPanel();
				
				guiBoard();
				
			}
		});

		JMenuItem gameLoad = new JMenuItem("Load Game");
		gameLoad.setMnemonic(KeyEvent.VK_L);
		gameLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				blockBoard = readFromSerializedFile(new File("Board.dat"));

				boardPanel.setVisible(false);
				gameGUI.remove(boardPanel);

				boardButtons = new JButton[10][10];
				
				treasurePanel.setVisible(false);
				gameGUI.remove(treasurePanel);
				treasurePanel = new JPanel();

				guiBoard();

				for (int x = 0; x < blockBoard.length; x++) {
					for (int y = 0; y < blockBoard[x].length; y++) {
						if (blockBoard[x][y].isAlreadyChecked() == true) {

							boardButtons[x][y].doClick();

						}
					}

				}

			}
		});

		JMenuItem gameSave = new JMenuItem("Save Game");
		gameSave.setMnemonic(KeyEvent.VK_S);
		gameSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("saved");
				writeToSerializedFile(new File("Board.dat"), blockBoard);

			}
		});

		JMenuItem gameExit = new JMenuItem("Exit Game");
		gameExit.setMnemonic(KeyEvent.VK_E);
		gameExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		JMenuItem gameHighScores = new JMenuItem("High Scores(H)");
		gameHighScores.setMnemonic(KeyEvent.VK_H);
		gameHighScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				highScoreFrame = new JFrame("High Scores");
				highScoreFrame.setSize(150, 250);
				highScoreFrame.setVisible(true);


				if (!top10.isEmpty()) {
					List<Game> top = new ArrayList<>();
					top.addAll(top10);
					Collections.sort(top, Collections.reverseOrder());
					JTextArea totalScores = new JTextArea();
					StringBuffer sb = new StringBuffer();
					for (Game g : top) {
						String hScores = g.getUsername() + "\t" + g.getScore() + "\n";
						sb.append(hScores);
					}
					totalScores.setText(sb.toString());
					highScoreFrame.add(totalScores);

				}
			}

		});

		gameMenu.add(gameNew);
		gameMenu.add(gameLoad);
		gameMenu.add(gameSave);
		gameMenu.addSeparator();
		gameMenu.add(gameHighScores);
		gameMenu.addSeparator();
		gameMenu.add(gameExit);

		/*
		 * Create help menu
		 */
		JMenuItem instructions = new JMenuItem("Instructions (I)");
		instructions.setMnemonic(KeyEvent.VK_I);
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				instructionFrame = new JFrame("Instructions");
				instructionFrame.setSize(600, 200);
				instructionFrame.setVisible(true);
				JTextArea howTo = new JTextArea();
				String Instructions = "\n	The purpose of the game is to open all the cells of the board which do not contain"
						+ " \n	a bomb. You lose if you set off a bomb cell.Every non-bomb cell you open will tell you "
						+ "\n	the total number of bombs in the eight neighboring cells. \n	Cells may also contain tresures!"
						+ "\n	 - Invicibility (You cannot lose any lives, though you reveal the board."
						+ ")\n	 - Probe (You can reveal a cell without losing any lives) [Right-click to use]\n "
						+ "	 - Bonus Points (Self Explanatory. YAY! :D)\n";
				howTo.setText(Instructions);
				instructionFrame.add(howTo);
			}
		});
		helpMenu.add(instructions);

		menuBar.add(gameMenu);
		menuBar.add(helpMenu);
		gameGUI.setJMenuBar(menuBar);

		guiBoard();

	}

	private void guiBoard() {
		// Panel for Minesweeper board
		boardPanel = new JPanel();
		GridLayout experimentLayout = new GridLayout(10, 10);
		boardPanel.setLayout(experimentLayout);
		boardPanel.setLayout(new GridLayout(10, 10));
		boardPanel.setBounds(10, 10, 300, 300);

		treasurePanel = new JPanel();
		treasurePanel.setBounds(10, 325, 150, 75);
		treasurePanel.setLayout(null);



		final JLabel scoreTitle = new JLabel("Score: ");
		scoreTitle.setBounds(12, 5, 40, 20);
		final JLabel scoreCurrent = new JLabel();
		refreshScore(scoreTitle, scoreCurrent); // updates score on UI

		final JLabel probeLeft = new JLabel("Probe Left:");
		probeLeft.setBounds(12, 30, 63, 19);
		final JLabel probeCount = new JLabel();
		refreshNumOfProbs(probeCount); // updates # of probs
		treasurePanel.add(probeLeft);

		final JLabel livesLeft = new JLabel("Lives Left:");
		livesLeft.setBounds(12, 50, 63, 19);
		final JLabel livesCount = new JLabel();
		refreshLives(livesLeft, livesCount); // updates lives on UI

		// Create 1st GUI button board
		if (blockBoard != null) {
			for (int x = 0; x < boardButtons.length; x++) {
				for (int y = 0; y < boardButtons[x].length; y++) {

					boardButtons[x][y] = new JButton();

					// final because they will not change values
					final JButton clickedButton = boardButtons[x][y];
					final Block b = blockBoard[x][y];
					final int rows = x;
					final int column = y;

					boardButtons[x][y].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {

							// case of blank
							if (b instanceof Blank) {

								if (b.getNumOfMinesAround() == 0) {
									clickedButton.setVisible(false);
									checkForWhite(rows, column);

								} else {
									displayNumberBlock(clickedButton,
											b.getNumOfMinesAround());
									gameBoard.step(b);
								}

							}

							// case of mines
							if (b instanceof Mines) {
								if (gameBoard.getLives() < 50) {
									final ImageIcon cageMine = new ImageIcon("img/cageMine.png");
									JOptionPane.showMessageDialog(null,"You have hit "+ ((Mines) b).getNumOfMines()
													+ " mine(s)!\n -1 Life","Ouch X_X",
													JOptionPane.INFORMATION_MESSAGE,
													cageMine);
								}
								gameBoard.step(b);
								ImageIcon mineIMG = new ImageIcon(this
										.getClass().getResource("img/mine.png"));
								clickedButton.setIcon(mineIMG);
								clickedButton.setDisabledIcon(mineIMG);
								clickedButton.setEnabled(false);

								if (!gameBoard.isHasLost()) {
									if (gameBoard.getLives() == 0) {

										System.out.println("Last life");

									}

									if (gameBoard.getLives() > 0) {

										System.out.println(gameBoard.getLives()
												+ " lives left");

									}
								}

							}

							// case of treasure
							if (b instanceof Treasure) {
								gameBoard.step(b);
								clickedButton.setBackground(Color.GREEN);
								clickedButton.setEnabled(false);

							}

							refreshLives(livesLeft, livesCount);
							refreshScore(scoreTitle, scoreCurrent);
							refreshNumOfProbs(probeCount);

							//Check if lost
							if (gameBoard.getLives() == -1 && !gameBoard.isHasLost()) {

								if (winButton == false) {
									JOptionPane.showMessageDialog(
											gameOverFrame,
											"Game Over!\nYou lose",
											"Inane error",
											JOptionPane.ERROR_MESSAGE);
									gameBoard.setHasLost(true);
								}
								revealBoard();
							}
							
							//Check if win
							if (gameBoard.checkWin() && !gameBoard.isHasLost()) {
								JOptionPane.showMessageDialog(null,"You win mister!\n");
								revealWin();
							}


						}

					});
					boardPanel.add(boardButtons[x][y]);



					probeFunction(probeCount, clickedButton, b);				

				}

			}

		}// end of buttons array



		gameGUI.getContentPane().add(boardPanel);
		gameGUI.getContentPane().add(treasurePanel);
	}



	private void probeFunction(final JLabel probeCount,
			final JButton clickedButton, final Block b) {
		// Probe action
		clickedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (gameBoard.getNumOfProbs() > 0) {
					if (e.getButton() == MouseEvent.BUTTON3) {

						if (b instanceof Mines) {
							ImageIcon mineIMG = new ImageIcon(this.getClass()
									.getResource("img/mine.png"));
							clickedButton.setIcon(mineIMG);
							clickedButton.setDisabledIcon(mineIMG);
							clickedButton.setEnabled(false);
							gameBoard.setNumOfProbs(gameBoard.getNumOfProbs() - 1);

						}
						
						else{
							clickedButton.doClick();
						}
						
						refreshNumOfProbs(probeCount);
					}
				}
			}
		});
	}

	/**
	 * @param probeLeft
	 * @param probeCount
	 */
	private void refreshNumOfProbs(JLabel probeCount) {
		probeCount.setText(Integer.toString(gameBoard.getNumOfProbs()));
		probeCount.setBounds(80, 30, 25, 19);
		treasurePanel.add(probeCount);
	}

	/**
	 * @param scoreTitle
	 * @param scoreCurrent
	 */
	private void refreshScore(JLabel scoreTitle, JLabel scoreCurrent) {
		scoreCurrent.setBounds(80, 5, 50, 20);
		int scoreTotal = gameBoard.getScore();
		scoreCurrent.setText((Integer.toString(scoreTotal)));
		treasurePanel.add(scoreTitle);
		treasurePanel.add(scoreCurrent);
	}

	/**
	 * @param livesLeft
	 * @param livesCount
	 */
	private void refreshLives(JLabel livesLeft, JLabel livesCount) {
		if (gameBoard.getLives() < 20) {
			livesCount.setText(Integer.toString(gameBoard.getLives()));
		} else {
			livesCount.setText("Immortal");
		}

		livesCount.setBounds(80, 50, 70, 19);
		treasurePanel.add(livesLeft);
		treasurePanel.add(livesCount);
	}

	protected void displayNumberBlock(JButton clickedButton,
			int numOfMinesAround) {
		StringBuilder filePath = new StringBuilder();
		filePath.append("img/").append(String.valueOf(numOfMinesAround))
		.append(".png");
		ImageIcon numIMG = new ImageIcon(this.getClass().getResource(
				filePath.toString()));
		clickedButton.setIcon(numIMG);
		clickedButton.setDisabledIcon(numIMG);
		clickedButton.setEnabled(false);

	}

	// THE MAJESTIC RECURSION METHOD
	private void checkForWhite(int i, int j) {

		gameBoard.step(blockBoard[i][j]);
		// Checks that block is blank & 0  n  
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
							if (blockBoard[a][b] instanceof Treasure) {
								boardButtons[a][b].doClick();
							}

						}

					} catch (Exception ArrayIndexOutOfBoundsException) {

					}

				}
			}
		}

	}

	//When user loses
	public void revealBoard() {

		for (int x = 0; x <= 9; x++) {
			for (int y = 0; y <= 9; y++) {

				if (blockBoard[x][y] instanceof Mines) {

					boardButtons[x][y].setIcon(mineIMG);
					boardButtons[x][y].setDisabledIcon(mineIMG);
					boardButtons[x][y].setEnabled(false);
				} else {

					boardButtons[x][y].doClick();

				}


			}

		}

			limitTen();
	}

	public void revealWin() {

		for (int x = 0; x <= 9; x++) {
			for (int y = 0; y <= 9; y++) {
				if ((blockBoard[x][y] instanceof Blank || blockBoard[x][y] instanceof Treasure)
						&& !blockBoard[x][y].isAlreadyChecked())

					boardButtons[x][y].doClick();

				else if (blockBoard[x][y] instanceof Mines) {

					boardButtons[x][y].setIcon(mineIMG);
					boardButtons[x][y].setDisabledIcon(mineIMG);
					boardButtons[x][y].setEnabled(false);
				}


			}

		}
			limitTen();
	}


	/**
	 * Will write to current Game object to Set top10
	 */
	private void limitTen() {
		if (top10.isEmpty() || (top10.size() < 10)) {

			try {
				top10.add(gameBoard);// problem here
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			TreeSet<Game> ss = new TreeSet<>();
			ss.addAll(top10);
			for (Game g : ss) {
				
				if (g.getScore() <= gameBoard.getScore()) {
					 	
					
						ss.remove((ss).first());
						ss.add(gameBoard);
						top10.removeAll(top10);
						top10.addAll(ss);
						break;
				}
			}

		}
		writeToSerializedFile(new File("HighScores.dat"), top10);
		System.out.println("New highscore updated");
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
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(
					new FileOutputStream(file));
			output.writeObject(blockBoard);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static Block[][] readFromSerializedFile(File file) {
		Block[][] blockBoard = null;
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(
					new FileInputStream(file));
			blockBoard = (Block[][]) input.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return blockBoard;
	}

	//saves highscore
	private static void writeToSerializedFile(File file, Set<Game> scores) {
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(
					new FileOutputStream(file));
			output.writeObject(scores);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


	}

	// Load highscore (if there are)
	@SuppressWarnings("unchecked")
	private static Set<Game> readScoresFromSerializedFile(File file) {
		Set<Game> scores = null;
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream( new FileInputStream(file));
			scores = (Set<Game>) input.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new HashSet<Game>(scores);
	}

	public static void main(String[] args) {

		MinesweeperGUI mainInterface = new MinesweeperGUI();

		mainInterface.gameboard(); //Show solution

	}

}
