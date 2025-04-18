package model.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Club {

	private String name;
	private int score;
	private int goals;
	private List<Player> players = new ArrayList<>();
	
	public Club() {}
	
	public Club(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGoals() {
		return goals;
	}

	public void setGoals(Integer goals) {
		this.goals = goals;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public void loadPlayersFromCsv(String filePath) {
		
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
			String line = br.readLine();
			
			while(line != null) {
				String[] fields = line.split(",");
				String name = fields[0];
				Integer goals = Integer.parseInt(fields[1]);
				
 				players.add(new Player(name,goals));
				
				line = br.readLine();
			}
		}
		catch (IOException e) {
			System.out.println("Error reading players: " + e.getMessage());
		}
		
	}
	
	public Club cloneWithSamePlayers() {
	    Club cloned = new Club(this.name);
	    // copia os jogadores sem ler do CSV novamente
	    cloned.setPlayers(new ArrayList<>(this.players));
	    cloned.setScore(this.score);
	    return cloned;
	}
	
}
