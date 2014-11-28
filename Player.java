package Assignment4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Player {
	
	private MinesweeperGUI playerGame = new MinesweeperGUI();
	private String username;
	private int finalScore;
	
	
	public Player(){
		
	}

	

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}





	public MinesweeperGUI getPlayerGame() {
		return playerGame;
	}



	public void setPlayerGame(MinesweeperGUI playerGame) {
		this.playerGame = playerGame;
	}



	//Save board(Save game)[prints dat. file]
	private static void writeToSerializedFile(File file) {
	    try {
	        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
	        
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	//Load board(Load game)[reads dat. file]
	private static Player readFromSerializedFile(File file) {
	    Player player = null;
	    try {
	        ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
	        player = (Player) input.readObject();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	       e.printStackTrace();
	    }
	    return player;
	}

}
