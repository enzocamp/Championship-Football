package model.entities;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

public class Match {

	private Club clubA;
	private Club clubB;
	private int goalsA;
	private int goalsB;

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

	public Club getWinner(){
		return goalsA > goalsB ? clubA : goalsA < goalsB ? clubB : null;
		
	}

	public void simulateMatch() {

		this.goalsA = ThreadLocalRandom.current().nextInt(0, 6);
		this.goalsB = ThreadLocalRandom.current().nextInt(0, 6);
		clubA.setGoals(clubA.getGoals() + goalsA);
		clubB.setGoals(clubB.getGoals() + goalsB);
		
		writeGoalsToPlayer();
		
		int result = goalsA - goalsB;
		
		if (result > 0) {
			clubA.setScore(clubA.getScore() + 3);
		} else if (result < 0) {
			clubB.setScore(clubB.getScore() + 3);
		} else {
			clubA.setScore(clubA.getScore() + 1);
			clubB.setScore(clubB.getScore() + 1);
		}
	}

	public String getResult() {
		return clubA.getName() + " " + goalsA + " x " + goalsB + " " + clubB.getName();

	}

	public static List<Match> generateRandomMatches(List<Club> clubs) {
		List<Match> matches = new ArrayList<>();

		for (int i = 0; i < clubs.size(); i++) {
			for (int j = i + 1; j <= clubs.size() - 1; j++) {
				
	            Club teamA = clubs.get(i);
	            Club teamB = clubs.get(j);

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
		System.out.println("Players Goals: ");

		getClubA().getPlayers().forEach((p) -> {
			if (p.getGoals() > 0)
				System.out.println(
						String.format("%s \nGoals: %d \n", p.getName(), p.getGoals()));
		});

		getClubB().getPlayers().forEach((p) -> {
			if (p.getGoals() > 0)
				System.out.println(
						String.format("%s \nGols: %d \n", p.getName(), p.getGoals()));
		});
	}
}
