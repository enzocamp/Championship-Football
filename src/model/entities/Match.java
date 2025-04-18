package model.entities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import application.ConfigManager;

public class Match {

	private Club clubA;
	private Club clubB;
	private int goalsA;
	private int goalsB;
	private static String clubAScalation = ConfigManager.get("clubAScalation.path");
	private static String clubBScalation = ConfigManager.get("clubBScalation.path");

	public Match(Club clubA, Club clubB) {
		this.clubA = clubA;
		this.clubB = clubB;
		simulateMatch();

	}

	public Club getClubA() {
		return clubA;
	}

	public Club getClubB() {
		return clubB;
	}

	public int getGoalsA() {
		return goalsA;
	}

	public int getGoalsB() {
		return goalsB;
	}

	public int getTotalGoals() {
		return goalsA + goalsB;
	}

	public String getClubAScalation() {
		return clubAScalation;
	}

	public String getClubScalation() {
		return clubBScalation;
	}

	public String getWinner(List<Club> originalClubs) {

		int result = goalsA - goalsB;

		Club originalA = findOriginalClub(clubA.getName(), originalClubs);
		Club originalB = findOriginalClub(clubB.getName(), originalClubs);
		
		if (result > 0) {
			originalA.setScore(originalA.getScore() + 3);
			return clubA.getName();
		} else if (result < 0) {
			originalB.setScore(originalB.getScore() + 3);
			return clubB.getName();
		} else {
			originalA.setScore(originalA.getScore() + 1);
			originalB.setScore(originalB.getScore() + 1);
			return "empate";
		}
	}

	private Club findOriginalClub(String name, List<Club> clubs) {
		return clubs.stream().filter(c -> c.getName().equals(name)).findFirst()
				.orElseThrow(() -> new RuntimeException("Club not found: " + name));
	}

	public void simulateMatch() {

		this.goalsA = ThreadLocalRandom.current().nextInt(0, 6);
		this.goalsB = ThreadLocalRandom.current().nextInt(0, 6);
		clubA.setGoals(goalsA);
		clubB.setGoals(goalsB);

	}

	public String getResult() {
		return clubA.getName() + " " + goalsA + " x " + goalsB + " " + clubB.getName();

	}

	public static List<Match> generateRandomMatches(List<Club> clubs) {
		List<Match> matches = new ArrayList<>();
		Set<Set<String>> matchKeys = new HashSet<>();

		for (int i = 0; i < clubs.size(); i++) {
			for (int j = i + 1; j <= clubs.size() - 1; j++) {

				Set<String> matchKey = Set.of(clubs.get(i).getName(), clubs.get(j).getName());

				if (matchKeys.contains(matchKey))
					continue;

				matchKeys.add(matchKey);
				
	            Club teamA = clubs.get(i).cloneWithSamePlayers();
	            Club teamB = clubs.get(j).cloneWithSamePlayers();


				// Cada instância carrega seus próprios jogadores

				if (clubAScalation == null) {
					throw new RuntimeException("Caminho da configuração 'clubAScalation.path' não encontrado!");
				}
				if (clubBScalation == null) {
					throw new RuntimeException("Caminho da configuração 'clubBScalation.path' não encontrado!");
				}

				teamA.loadPlayersFromCsv(clubAScalation);

				teamB.loadPlayersFromCsv(clubBScalation);

				Match match = new Match(teamA, teamB);

				matches.add(match);
			}
		}

		return matches;

	}

	public void writeGoalsToPlayer() {
		List<Player> playersSortedA = clubA.getPlayers();
		List<Player> playersSortedB = clubB.getPlayers();

		int goalsAdded = 0;

		while (goalsAdded < goalsA) {
			Player player = playersSortedA.get(ThreadLocalRandom.current().nextInt(playersSortedA.size()));
			if (goalsAdded == goalsA) {
				return;
			}
			// else if(player.getGoals())
			int goalsToAdd = (ThreadLocalRandom.current().nextInt(1, (goalsA - goalsAdded) + 1));
			player.setGoals(player.getGoals() + goalsToAdd);
			goalsAdded += player.getGoals();
		}

		goalsAdded = 0;

		while (goalsAdded < goalsB) {
			Player player = playersSortedB.get(ThreadLocalRandom.current().nextInt(playersSortedB.size()));
			if (goalsAdded == goalsB) {
				return;
			}
			int goalsToAdd = (ThreadLocalRandom.current().nextInt(1, (goalsB - goalsAdded) + 1));
			player.setGoals(player.getGoals() + goalsToAdd);
			goalsAdded += player.getGoals();
		}

	}

	public void printClubGoals() {
		System.out.println("Gols dos jogadores: ");

		getClubA().getPlayers().forEach((p) -> {
			if (p.getGoals() > 0)
				System.out.println(
						String.format("%s - %s \nGols: %d \n", p.getName(), getClubA().getName(), p.getGoals()));
		});

		getClubB().getPlayers().forEach((p) -> {
			if (p.getGoals() > 0)
				System.out.println(
						String.format("%s - %s \nGols: %d \n", p.getName(), getClubB().getName(), p.getGoals()));
		});
	}
}
