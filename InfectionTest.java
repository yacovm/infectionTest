package disseminationTest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InfectionTest {

	public static void main(String[] args) {
		for (int n=4; n<7000; n *= 2) {
			int notInfectedAttempts = 0;
			int k = (int) (Math.log(n) + 3);
			for (int i=0; i<1000; i++) {
				int infectedCount = calculateInfected(n, k);
				if (infectedCount < n) {
					notInfectedAttempts++;
				}
			}
			System.out.println(n + ":" + ((1000 - notInfectedAttempts) / 10) + "%, k: " + k);
		}
	}
	
	public static List<Integer> selectRandomPeers(int n, int k) {
		List<Integer> l = new LinkedList<Integer>();
		for (int i=0; i<k; i++) {
			int p = (int) (Math.random() * n);
			// select another peer that wasn't selected so far
			while (true) {
				if (! l.contains(p)) {
					break;
				}
				p = (int) (Math.random() * n);
			}
			l.add(p);
		}
		return l;
	}
	
	public static int calculateInfected(int n, int k) {
		Boolean[] infectedNodes = new Boolean[n];
		for (int i=0; i<infectedNodes.length; i++) {
			infectedNodes[i] = false;
		}
		Queue<Integer> nodes2act = new LinkedList<Integer>();
		nodes2act.add(0);
		while (! nodes2act.isEmpty()) {
			// get next id of infected node 
			int p = nodes2act.poll();
			infectedNodes[p] = true;
			// disseminate to k random nodes
			selectRandomPeers(n, k).stream().forEach(q -> {
				if (! infectedNodes[q]) {
					nodes2act.add(q); // infect node p
				}
			});
		}
		return (int) Arrays.stream(infectedNodes).filter(b -> true == b).count();
	}

}
