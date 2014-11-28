package Assignment4;

import java.io.Serializable;

public class Treasure extends Block implements Serializable{
	
	
	//If player gets lives are reward
	public int getLives(int lives){
		System.out.println("+3 Lives");
		return lives+=3;
	}
	
	//Player gets prob
	public int getProbe(int numOfProb){
		System.out.println("Probes");
		return numOfProb++;
		
	}
	
	//Player becomes immortal
	public int immortal(int lives){
		System.out.println("Immortal activated");
		return lives+=99;
	}
	
}
