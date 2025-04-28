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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import model.entities.Club;
import model.entities.Match;
import model.entities.Player;

public class FootballMatch {

	public static void main(String[] args) {

		String clubsNamesPath = ConfigManager.get("clubs.path");

		List<Club> clubs = new ArrayList<>();

		try (BufferedReader bw = new BufferedReader(new FileReader(clubsNamesPath))) {
			String line = bw.readLine();

			while (line != null) {
				String[] fields = line.split(",");
				String name = fields[0];

				String cleanName = name.toLowerCase().replace(" ", "");
				clubs.add(new Club(name, ConfigManager.get(String.format("club%sscalation", cleanName))));

				line = bw.readLine();
			}

		} catch (IOException e) {
			System.out.println("Error reading clubs " + e.getMessage());
		}

		List<Match> matches = Match.generateRandomMatches(clubs);

		matches.forEach(Match::simulateMatch);

		// clubs.stream().sorted((clubA,clubB) ->
		// clubA.getScore().compareTo(clubB.getScore())).forEach(System.out::println);

		System.out.println(String.format("%-20s | %-7s | %s", "Club", "Score", "Goals"));

		clubs.stream().sorted(Comparator.comparing(Club::getScore).thenComparing(Club::getGoals).reversed()).forEach(
				club -> System.out.printf("%-20s | %-7d | %d %n", club.getName(), club.getScore(), club.getGoals()));

		Map<String, Integer> playersGoals = clubs.stream()
	    .flatMap(club -> club.getPlayers().stream()) // pega todos os jogadores de todos os clubes
	    .sorted((p1, p2) -> p2.getGoals().compareTo(p1.getGoals())) // ordena decrescente
	    .limit(10)
	    .collect(Collectors.toMap(
	    		Player::getName,
	            Player::getGoals,
	            (e1, e2) -> e1,
	            LinkedHashMap:: new // Mantém a ordem de inserção!
	        ));
		

		System.out.println();
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println();
		
		System.out.println(String.format("%-20s | %-20s | %-7s", "Player", "Club" ,"Goals"));

		playersGoals.forEach((name, goals) -> System.out.printf(String.format("%-20s | %-20s | %d%n",
				name.split(" - ")[0],
				name.split(" - ")[1],
				goals)));

	}
}
