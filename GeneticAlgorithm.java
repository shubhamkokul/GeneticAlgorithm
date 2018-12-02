import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm {

	static final double BIAS = 1;
	static HashMap<Integer, ArrayList<Double>> weightList = new HashMap<>();
	static HashMap<Integer, Double> population = new HashMap<>();
	static double input[][] = { { 0, 1, -1 }, { 0, 0, -1 }, { 1, 0, -1 }, { 1, 1, -1 } };
	static double expectedValue[] = { 0, 1, 1, 0 };
	static int firstBest = 0;
	static int secondBest = 1;
	static double y[] = new double[4];
	private static Scanner scanner;

	public static void main(String args[]) {
		GeneticAlgorithm geneAlgo = new GeneticAlgorithm();
		scanner = new Scanner(System.in);
		System.out.println("Please enter initial population");
		int numberPopulation = Integer.parseInt(scanner.nextLine());
		geneAlgo.initialWeight(numberPopulation);
		geneAlgo.selectionPopulation();
		geneAlgo.crossOverPopulation();
		int answerKey = geneAlgo.geneticAlgorithm();
		System.out.println(weightList.get(answerKey));

	}

	public void populationGeneration(int numberPopulation) {
		population.put(numberPopulation, costCalculation(numberPopulation));
	}

	public int selectionPopulation() {
		ArrayList<Double> tempSelectArray = new ArrayList<>();
		for (int i = 1; i <= population.size(); i++) {
			tempSelectArray.add(population.get(i));
		}
		Collections.sort(tempSelectArray); // Ranking of population
		firstBest = getKey(population, tempSelectArray.get(0));
		secondBest = getKey(population, tempSelectArray.get(1));
		// System.out.println("BestSolution " + population.get(firstBest));
		return firstBest;
	}

	public void crossOverPopulation() {
		ArrayList<Double> firstBestArrayList = weightList.get(firstBest);
		ArrayList<Double> secondBestArrayList = weightList.get(secondBest);
		int randomSelection = randomProbability(1, 2);
		int randomProbability = randomProbability(0, 9);
		for (int i = 0; i < randomProbability; i++) {
			double temp = firstBestArrayList.get(i);
			firstBestArrayList.set(i, secondBestArrayList.get(i));
			secondBestArrayList.set(i, temp);
		}
		if (randomProbability == 0) {
			mutationPopulation(firstBestArrayList);
		} else {
			if (randomSelection == 1) {
				mutationPopulation(firstBestArrayList);
			} else {
				mutationPopulation(secondBestArrayList);
			}
		}

	}

	// Will be called internally in crossover
	public void mutationPopulation(ArrayList<Double> tempNewOffSpring) {
		int randomSetLimit = randomProbability(0, 8);
		int randomTempLimit = 8 - randomSetLimit;
		int randomSetStart = randomProbability(0, randomTempLimit);
		int randomSetEnd = randomSetStart + randomSetLimit;
		for (int i = randomSetStart; i < randomSetEnd; i++) {
			tempNewOffSpring.set(i, (tempNewOffSpring.get(i)) * -1);
		}
		weightList.put(weightList.size() + 1, tempNewOffSpring);
		populationGeneration(weightList.size()); // Accepting New Offspring in population
	}

	public double costCalculation(int keyWeight) {
		// System.out.println("COST of " + keyWeight);
		double cost = 0;
		ArrayList<Double> tempArrayList = weightList.get(keyWeight);
		double yy1[] = new double[4];
		double yy2[] = new double[4];
		double yy[][] = new double[4][3];
		yy1[0] = input[0][0] * tempArrayList.get(0) + input[0][1] * tempArrayList.get(1)
				+ input[0][2] * tempArrayList.get(2);
		yy1[1] = input[1][0] * tempArrayList.get(0) + input[1][1] * tempArrayList.get(1)
				+ input[1][2] * tempArrayList.get(2);
		yy1[2] = input[2][0] * tempArrayList.get(0) + input[2][1] * tempArrayList.get(1)
				+ input[2][2] * tempArrayList.get(2);
		yy1[3] = input[3][0] * tempArrayList.get(0) + input[3][1] * tempArrayList.get(1)
				+ input[3][2] * tempArrayList.get(2);

		yy2[0] = input[0][0] * tempArrayList.get(3) + input[0][1] * tempArrayList.get(4)
				+ input[0][2] * tempArrayList.get(5);
		yy2[1] = input[1][0] * tempArrayList.get(3) + input[1][1] * tempArrayList.get(4)
				+ input[1][2] * tempArrayList.get(5);
		yy2[2] = input[2][0] * tempArrayList.get(3) + input[2][1] * tempArrayList.get(4)
				+ input[2][2] * tempArrayList.get(5);
		yy2[3] = input[3][0] * tempArrayList.get(3) + input[3][1] * tempArrayList.get(4)
				+ input[3][2] * tempArrayList.get(5);

		/*
		 * System.out.println("YY1 SET " + yy1[0] + " " + yy1[1] + " " + yy1[2] + " " +
		 * yy1[3]);
		 * 
		 * System.out.println("YY2 SET " + yy2[0] + " " + yy2[1] + " " + yy2[2] + " " +
		 * yy2[3]);
		 */

		yy[0][0] = yy1[0] > 0 ? 1 : 0;
		yy[0][1] = yy2[0] > 0 ? 1 : 0;
		yy[0][2] = BIAS;
		yy[1][0] = yy1[1] > 0 ? 1 : 0;
		yy[1][1] = yy2[1] > 0 ? 1 : 0;
		yy[1][2] = BIAS;
		yy[2][0] = yy1[2] > 0 ? 1 : 0;
		yy[2][1] = yy2[2] > 0 ? 1 : 0;
		yy[2][2] = BIAS;
		yy[3][0] = yy1[3] > 0 ? 1 : 0;
		yy[3][1] = yy2[3] > 0 ? 1 : 0;
		yy[3][2] = BIAS;

		y[0] = (yy[0][0] * tempArrayList.get(6) + yy[0][1] * tempArrayList.get(7) * yy[0][2] * tempArrayList.get(8)) > 0
				? 1 : 0;
		y[1] = (yy[1][0] * tempArrayList.get(6) + yy[1][1] * tempArrayList.get(7) * yy[1][2] * tempArrayList.get(8)) > 0
				? 1 : 0;
		y[2] = (yy[2][0] * tempArrayList.get(6) + yy[2][1] * tempArrayList.get(7) * yy[2][2] * tempArrayList.get(8)) > 0
				? 1 : 0;
		y[3] = (yy[3][0] * tempArrayList.get(6) + yy[3][1] * tempArrayList.get(7) * yy[3][2] * tempArrayList.get(8)) > 0
				? 1 : 0;

		System.out.println("Y SET " + y[0] + " " + y[1] + " " + y[2] + " " + y[3]);
		if (y[0] == 0.0 && y[1] == 1.0 && y[2] == 1.0 && y[3] == 0.0) {
			System.out.println(keyWeight);
			System.out.println("Y SET " + y[0] + " " + y[1] + " " + y[2] + " " + y[3]);
			System.out.println("Existing");
			System.out.println("Weights to take " + tempArrayList);
			System.exit(1);
		}

		cost = Math.pow(expectedValue[0] - y[0], 2) + Math.pow(expectedValue[1] - y[1], 2)
				+ Math.pow(expectedValue[2] - y[2], 2) + Math.pow(expectedValue[3] - y[3], 2);
		return Double.parseDouble(new DecimalFormat("#.##").format(cost));

	}

	public ArrayList<Double> weightGeneration() {

		ArrayList<Double> tempWeight = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			tempWeight.add(generateRandomWeight());
		}
		return tempWeight;
	}

	public void initialWeight(int numberPopulation) {
		for (int i = 1; i <= numberPopulation; i++) {
			weightList.put(i, weightGeneration());
			populationGeneration(i);
		}
	}

	public static double generateRandomWeight() {
		double randomWeight = 0;
		double low = -3;
		double high = 3;
		randomWeight = ThreadLocalRandom.current().nextDouble(low, high);

		return Double.parseDouble(new DecimalFormat("#.##").format(randomWeight));
	}

	public static double sigmf(double initialCalculate) {
		double result = 0;
		if (initialCalculate < -10)
			result = 0;
		else if (initialCalculate > 10)
			result = 1;
		else
			result = 1 / (1 + Math.exp(-initialCalculate));
		return result;
	}

	public static int getKey(HashMap<Integer, Double> map, Double value) {
		return map.entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey)
				.findFirst().get();
	}

	public int geneticAlgorithm() {
		int weightNumber = 0;
		int i = 0;
		while (i < 60000) {
			int answerKey = this.selectionPopulation();
			
			double tempCheckDouble = Double.parseDouble(new DecimalFormat("#.##").format(population.get(answerKey)));
			if (tempCheckDouble < 0.1) {
				System.out.println("breaking");
				weightNumber = answerKey;
				System.out.println("Y SET " + y[0] + " " + y[1] + " " + y[2] + " " + y[3]);
				System.out.println(population.get(weightNumber));
				System.out.println("BestAnswer " + weightNumber);
				break;
			}
			this.crossOverPopulation();
			i++;
			System.out.println(i);
			// System.out.println("Cost"+population);
			weightNumber = answerKey;
		}
		return weightNumber;
	}

	public int randomProbability(int low, int high) {
		int randomSet = 0;
		Random random = new Random();
		randomSet = random.nextInt(high - low) + low;
		return randomSet;
	}

}
