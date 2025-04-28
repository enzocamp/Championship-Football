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
				
			
				String cleanName = name.toLowerCase().replace(" ", "");
				clubs.add(new Club(name,ConfigManager.get(String.format("club%sscalation", cleanName))));
				
				line = bw.readLine();
			}
			
			
		}
		catch (IOException e) {
			System.out.println("Error reading clubs " + e.getMessage());
		}
		
		List<Match> matches = Match.generateRandomMatches(clubs);

		matches.forEach(Match::simulateMatch);
		
		//clubs.stream().sorted((clubA,clubB) -> clubA.getScore().compareTo(clubB.getScore())).forEach(System.out::println);
				
		System.out.println(String.format("%-20s | %-7s | %s","Team","Score","Goals"));
		
		clubs.stream()
	    .sorted(Comparator.comparing(Club::getScore).reversed())
	    .forEach(club -> System.out.printf("%-20s | %-7d | %d %n", club.getName(), club.getScore(), club.getGoals()));

	}
}
