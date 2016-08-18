package disseminationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InfectionTest {

	private static List<Integer> byzantinePeers = new ArrayList<Integer>();
	private static double p = 0.15;
	private static double h = 1.0;
	
	public static void main(String[] args) {
		
		for (int n=128; n<7000; n *= 2) {
			populateByzantine(n);
			int notInfectedAttempts = 0;
			int k = (int) (((int) (Math.log(n) + 3)) * h);
			
			for (int i=0; i<1000; i++) {
				int infectedCount = calculateInfected(n, k);
				if (infectedCount < n - byzantinePeers.size()) {
					notInfectedAttempts++;
				}
			}
			System.out.println(n + ":" + ((double)(1000 - notInfectedAttempts) / 1000) * 100 + "% success, k: " + k + ", h: " + h);
		}
	}
	
	private static void populateByzantine(int n) {
		for (int i=0; i<n; i++) {
			if (Math.random() < p) {
				byzantinePeers.add(i);
			}
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
			if (byzantinePeers.contains(p)) {
				continue;
			}
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
