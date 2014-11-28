package Assignment4;

import java.io.Serializable;
import java.util.Random;

public class Mines extends Block implements Serializable{
	
	Random r = new Random();
	int start = 1;
	int end = 3;
	final int num = r.nextInt((end-start)+1)+start;


	public int getNumOfMines() {
		
		if(num==1)
			return 1;
	 
		if(num==2)
			return 2;
		else 
		{// num =3
			return 3;
		}
			
	
	}

	public int mineDamage(int lives){
		return --lives;
	}



}
