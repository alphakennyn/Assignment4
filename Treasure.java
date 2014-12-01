package Assignment4;

import java.io.Serializable;

public class Treasure extends Block implements Serializable{
	
	
	//If player gets lives are reward
	public int getLives(int lives){
		
		return lives+=3;
	}
	
	//Player gets prob
	public int getProbe(int numOfProb){
		
		return ++numOfProb;
		
	}
	
	//Player becomes immortal
	public int immortal(int lives){

		return lives+=99;
	}
	
}
