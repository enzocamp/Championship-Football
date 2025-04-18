package application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import model.entities.Club;
import model.entities.Match;

public class FootballMatch {

	public static void main(String[] args) {
		
		String clubsNamesPath = ConfigManager.get("clubs.path");
		
		List<Club> clubs = new ArrayList<>();
		
		try (BufferedReader bw = new BufferedReader(new FileReader(clubsNamesPath))){
			String line = bw.readLine();
			
			while(line != null) {
				String[] fields = line.split(",");
				String name = fields[0];
				
				clubs.add(new Club(name));
				
				line = bw.readLine();
			}
		}
		catch (IOException e) {
			System.out.println("Error reading clubs " + e.getMessage());
		}
		
		List<Match> matches = Match.generateRandomMatches(clubs);

		for (Match match : matches) {
			String winner = match.getWinner(clubs);
			System.out.println(String.format("%s \nWinner: %s\n", match.getResult(), winner));
		    match.writeGoalsToPlayer();
		    match.printClubGoals();
		    System.out.println(String.format("Total Gols Partida: %d \n", match.getGoalsA() + match.getGoalsB()));
		}
		
		System.out.println();
		
		System.out.println("Resultado do Campeonato: ");
		
		clubs.stream()
	    .sorted(Comparator.comparing(Club::getScore).reversed())
	    .forEach(club -> System.out.printf("%s - Pontos: %d%n", club.getName(), club.getScore()));

	}
}
