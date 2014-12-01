package Assignment4;

import java.io.Serializable;
import java.util.Random;

public class Game implements Serializable, Comparable{	

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = 4357356434872243842L;

	private static int size = 10;

	private int numOfProbs = 0;
	private int lives = 3;
	private int score = 0;
	private static Block utility;
	private boolean hasLost;
	private Block[][] board;
	private String userName;
	private int numberOfMines = 15;

	public Game(String name){
		this.userName=name;
	}

	public Game() {

	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score += score;
	}

	public int getNumOfProbs() {
		return numOfProbs;
	}

	public void setNumOfProbs(int numOfProbs) {
		this.numOfProbs = numOfProbs;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLives() {

		return lives;
	}

	public boolean isHasLost() {
		return hasLost;
	}

	public void setHasLost(boolean hasLost) {
		this.hasLost = hasLost;
	}

	// WILL CREATE NEW BOARD(NEW GAME)
	public Block[][] createNewBoard() {

		board = new Block[size][size];

		Random r = new Random();
		int start = 1;
		int end = 18;

		int numberOfTreasure = 3;

		while (numberOfMines != 0 && numberOfTreasure != 0) {

			for (int a = 0; a < size; a++) {
				for (int b = 0; b < size; b++) {

					int num = r.nextInt((end - start) + 1) + start;



					if (num >= 1 && num <= 3 && numberOfMines > 0) {

						board[a][b] = new Mines();
						--numberOfMines;
						continue;

					}
					if (num == 6 && numberOfTreasure > 0) {

						board[a][b] = new Treasure();
						--numberOfTreasure;
						continue;

					} else {
						board[a][b] = new Blank();
						continue;

					}
				}


			}

		}		
		// add numbers around mines
		addNumbers(board);

		return board;
	}

	/*
	 * Method adds sets number around mines
	 */
	private void addNumbers(Block[][] block) {

		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block.length; j++) {
				if (block[i][j] instanceof Mines) {

					// Numbers of mines on that Mines Block
					int index = ((Mines) block[i][j]).getNumOfMines();

					for (int a = i - 1; a <= i + 1; a++) {
						for (int b = j - 1; b <= j + 1; b++) {
							try {

								block[a][b].setNumOfMinesAround(index);

							} catch (Exception ArrayIndexOutOfBoundsException) {

							}
						}
					}

				}
			}
		}
	}

	public void step(Block b) {

		b.setAlreadyChecked(true);

		if (b instanceof Mines)
			stepOnMines();

		if (isHasLost() == false && getLives() >= 0) {

			if (b instanceof Treasure)
				stepOnTreasure();
			if (b instanceof Blank)
				stepOnBlank();
		}
	}

	// Create method land on mine(What game will do when player lands on mine)
	private void stepOnMines() {
		utility = new Mines();

		setLives(((Mines) utility).mineDamage(getLives()));

	}

	// Create method land on treasure(What game will do if lands on treasure)
	private void stepOnTreasure() {

		utility = new Treasure();
		Random r = new Random();
		int start = 1;
		int end = 40;

		int num = r.nextInt((end - start) + 1) + start;

		if (num < 15) {

			setNumOfProbs(((Treasure) utility).getProbe(getNumOfProbs()));// (5/30)
			// continue;

		}

		if (num >= 15 && num <= 30) {
			
			setScore(500); 
			return; 
			

		} if (num == 40) { 

			setLives(((Treasure) utility).immortal(getLives()));
			return;
			
		}

		else { 
			setLives(((Treasure) utility).getLives(getLives())); 
			return; 
			}

	}

	/*
	 * If user step on neither mine nor treasure. This method will call method
	 * checkAround to determine whether it is blank or has a number associated
	 * to it
	 */
	private void stepOnBlank() {

		setScore(100);

	}

	public void gameOver() {
		if (!isHasLost()) {
			setHasLost(true);
			System.out.println("lose " + isHasLost());
			System.out.println("Total Score is: " + getScore());
		}
	}

	/*
	 * when user wins
	 */
	public boolean checkWin() {
		int check = 0;
		int mines = 0;

		if (!isHasLost()) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if ((board[i][j] instanceof Blank || board[i][j] instanceof Treasure)
							&& board[i][j].isAlreadyChecked())
						check++;
					if (board[i][j] instanceof Mines)
						mines++;
				}
			}

			if (check == 100 - mines) {

				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int code = 0;

		if(null != userName)
			code += userName.hashCode();
		code += this.lives;
		code += this.numberOfMines;
		code += this.numOfProbs;
		code += this.score;

		return code;
	}


	@Override
	public int compareTo(Object obj) {
		int i = -1;

		if (null == obj) {
			return i;
		}
		if (obj instanceof Game) {
			i = Integer.valueOf(score).compareTo(((Game) obj).getScore());
			if (i != 0) {
				return i;
			}
			i = Integer.valueOf(lives).compareTo(((Game) obj).getLives());
			if (i != 0) {
				return i;
			}		
			i = userName.compareToIgnoreCase(((Game) obj).getUsername());

		}

		return i;
	}
}
