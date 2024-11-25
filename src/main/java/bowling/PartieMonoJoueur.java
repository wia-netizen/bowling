package bowling;

/**
 * Cette classe a pour but d'enregistrer le nombre de quilles abattues lors des
 * lancers successifs d'<b>un seul et même</b> joueur, et de calculer le score
 * final de ce joueur
 */
import java.util.ArrayList;
import java.util.List;

public class PartieMonoJoueur {
	private static final int MAX_TOURS = 10;
	private static final int QUILLES_PAR_TOUR = 10;

	private final List<Integer> lancers;
	private int tourCourant;
	private int lancerCourant;

	/**
	 * Constructeur
	 */
	public PartieMonoJoueur() {
		this.lancers = new ArrayList<>();
		this.tourCourant = 1;
		this.lancerCourant = 1;
	}

	/**
	 * Cette méthode doit être appelée à chaque lancer de boule
	 *
	 * @param nombreDeQuillesAbattues le nombre de quilles abattues lors de ce lancer
	 * @throws IllegalStateException si la partie est terminée
	 * @return vrai si le joueur doit lancer à nouveau pour continuer son tour, faux sinon	
	 */
	public boolean enregistreLancer(int nombreDeQuillesAbattues) {
		if (estTerminee()) {
			throw new IllegalStateException("La partie est terminée.");
		}

		// Enregistrer le lancer
		lancers.add(nombreDeQuillesAbattues);

		if (tourCourant < MAX_TOURS) {
			if (lancerCourant == 1 && nombreDeQuillesAbattues == QUILLES_PAR_TOUR) { // Strike
				tourCourant++;
				lancerCourant = 1;
				return false;
			} else if (lancerCourant == 2) { // Fin de tour
				tourCourant++;
				lancerCourant = 1;
				return false;
			} else {
				lancerCourant++;
				return true;
			}
		} else { // Dernier tour
			if (lancerCourant == 1 && nombreDeQuillesAbattues == QUILLES_PAR_TOUR) { // Strike
				lancerCourant++;
				return true;
			} else if (lancerCourant == 2 && cumulDernierTour() == QUILLES_PAR_TOUR) { // Spare
				lancerCourant++;
				return true;
			} else if (lancerCourant == 3 || (lancerCourant == 2 && cumulDernierTour() < QUILLES_PAR_TOUR)) { // Fin de partie
				tourCourant = 0;
				lancerCourant = 0;
				return false;
			} else {
				lancerCourant++;
				return true;
			}
		}
	}

	/**
	 * Cette méthode donne le score du joueur.
	 * Si la partie n'est pas terminée, on considère que les lancers restants
	 * abattent 0 quille.
	 * @return Le score du joueur
	 */
	public int score() {
		int score = 0;
		int indexLancer = 0;
		int tour = 1;

		while (tour <= MAX_TOURS && indexLancer < lancers.size()) {
			System.out.println("Tour " + tour + " - Index Lancer : " + indexLancer);

			if (estStrike(indexLancer)) {
				System.out.println("Strike détecté au lancer " + indexLancer);
				score += 10 + bonusStrike(indexLancer);
				indexLancer++;  
			} else if (estSpare(indexLancer)) {
				System.out.println("Spare détecté au lancer " + indexLancer);
				score += 10 + bonusSpare(indexLancer);
				indexLancer += 2;
			} else {
				System.out.println("Tour normal détecté au lancer " + indexLancer);
				score += lancers.get(indexLancer) + lancers.get(indexLancer + 1);
				indexLancer += 2; 
			}
			tour++;
		}

		System.out.println("Score final : " + score);
		return score;
	}

	
	/**
	 * @return vrai si la partie est terminée pour ce joueur, faux sinon
	 */
	public boolean estTerminee() {
		return tourCourant == 0;
	}


	/**
	 * @return Le numéro du tour courant [1..10], ou 0 si le jeu est fini
	 */
	public int numeroTourCourant() {
		return tourCourant;
	}

	/**
	 * @return Le numéro du prochain lancer pour tour courant [1..3], ou 0 si le jeu
	 *         est fini
	 */
	public int numeroProchainLancer() {
		return lancerCourant;
	}
	private int bonusStrike(int index) {
		int bonus = 0;
		if (index + 1 < lancers.size()) {
			bonus += lancers.get(index + 1); 
		}
		if (index + 2 < lancers.size()) {
			bonus += lancers.get(index + 2); 
		}
		return bonus;
	}

	private int bonusSpare(int index) {
		if (index + 1 < lancers.size()) {
			return lancers.get(index + 1); 
		}
		return 0;
	}
	
	public boolean estStrike(int index) {
		return lancers.get(index) == 10; 
	}

	public boolean estSpare(int index) {
		return (lancers.get(index) + lancers.get(index + 1)) == 10; 
	}


	private int cumulDernierTour() {
		int cumul = 0;
		int lancersDernierTour = Math.min(lancerCourant, lancers.size());
		for (int i = lancers.size() - lancersDernierTour; i < lancers.size(); i++) {
			cumul += lancers.get(i);
		}
		return cumul;
	}

}
