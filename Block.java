package Assignment4;

import java.io.Serializable;

public class Block  implements Serializable{
	
	private  int numOfMinesAround=0;
	private boolean alreadyChecked=false;
	
	public Block(){
		this.alreadyChecked = isAlreadyChecked();
		
	}
	


	public int getNumOfMinesAround() {
		
		return numOfMinesAround;
	}
	
	public void setNumOfMinesAround(int num) {
		
		numOfMinesAround = numOfMinesAround + num;
		
	}


	public boolean isAlreadyChecked() {
		
		return alreadyChecked;
		
	}



	public void setAlreadyChecked(boolean alreadyChecked) {
		
		this.alreadyChecked = alreadyChecked;
		
	}
	
}
