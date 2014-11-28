package Assignment4;

import java.io.Serializable;
import java.util.Random;

public class Game implements Serializable{

	private static int size=10;
	private int numOfProbs = 0;
	private int lives=3;
	private int Score = 0;
	private static Block utility;
	private boolean hasLost;




	public int getScore() {
		return Score;
	}



	public void setScore(int score) {
		this.Score += score;
	}



	public int getNumOfProbs() {
		return numOfProbs;
	}

	public void setNumOfProbs(int numOfProbs) {
		this.numOfProbs = numOfProbs;
	}

	public void setLives(int lives){
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



	//WILL CREATE NEW BOARD(NEW GAME)
	public Block[][] createNewBoard(){

		Block[][] board = new Block[size][size];

		Random r = new Random();
		int start = 1;
		int end = 18;
		int numberOfMines = 10;
		int numberOfTreasure = 3;

		//while(numberOfMines> 0 && numberOfTreasure> 0){


		for(int a = 0; a < size; a++){
			for(int b = 0; b<size;b++){

				int num = r.nextInt((end-start)+1)+start;

				//	if(board[a][b] == null){

				if(num >= 1 && num < 3 && numberOfMines > 0){

					board[a][b] = new Mines();
					numberOfMines--;
					continue;


				}
				if(num ==6  && numberOfTreasure > 0){

					board[a][b] = new Treasure();
					numberOfTreasure--;
					continue;

				}
				else{
					board[a][b] = new Blank();
					continue;

				} 
				//}


			}
			//	}

		}

		//add numbers around mines
		addNumbers(board);

		return board;
	}

	/*
	 * Method adds sets number around mines
	 * 
	 */
	private void addNumbers(Block[][] block){

		for(int i =0;i<block.length;i++){
			for(int j = 0;j<block.length;j++){
				if(block[i][j] instanceof Mines){

					//Numbers of mines on that Mines Block
					int index = ((Mines) block[i][j]).getNumOfMines();

					for(int a=i-1;a<=i+1;a++){
						for(int b=j-1;b<=j+1;b++){
							try{

								block[a][b].setNumOfMinesAround(index);

							} catch (Exception ArrayIndexOutOfBoundsException){

							}
						}
					}

				}
			}
		}
	}


	public void step(Block b){

		if(isHasLost() == false && getLives()>=0){
			b.setAlreadyChecked(true);
			
			if(b instanceof Mines)
				stepOnMines();
			if(b instanceof Treasure)
				stepOnTreasure();
			if(b instanceof Blank)
				stepOnBlank();
		} 
	}

	//Create method land on mine(What game will do when player lands on mine)
	private void stepOnMines(){
		utility = new Mines();

		
		setLives(((Mines) utility).mineDamage(getLives()));
		

	}

	//Create method land on treasure(What game will do if lands on treasure)
	private void stepOnTreasure(){

		utility = new Treasure();
		Random r = new Random();
		int start = 1;
		int end = 30;

		int num = r.nextInt((end-start)+1)+start;

		
			if(num%2==0 && num<=10){

				((Treasure) utility).getProbe(getNumOfProbs());//(5/30)
				return;

			} else{
				if(num == 2){
					setLives(((Treasure) utility).immortal(getLives()));//(1/30)
					return;

				} else if(num%2!=0 && num>=16 ){
					setLives(((Treasure) utility).getLives(getLives()));//(8/30)
					return;
				} else {
					setScore(500);//(16/30)
					System.out.println("+500 points");
					return;
				}
			}
		


	}

	/*
	 * If user step on neither mine nor treasure.
	 * This method will call method checkAround to determine whether it is blank or has a number associated to it
	 */
	private void stepOnBlank(){
		
			setScore(100);

	}

	public void gameOver(){
		setHasLost(true);
	
		System.out.println("lose " + isHasLost());
		System.out.println("Total Score is: "+getScore());
	}





}
