import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    public static void main(String[] args) {
        final String trainSetPath = args[0]; // Ścieżka train-set.
        //final String testSetPath = args[1]; // Ścieżka test-set.
        final String testSetPath = "files/my_test-set.csv"; // Ścieżka test-set.

        final BayesClassifier bayesClassifier = new BayesClassifier(); // Obiekt klasy BayesClassifier.
        bayesClassifier.transformTrainSet(trainSetPath); // Procesowanie danych train-set.

        final Scanner optionScanner = new Scanner(System.in);
        final Scanner vectorScanner = new Scanner(System.in);
        while (true) {
            System.out.println("Dostępne opcje: ");
            System.out.println("1. Klasyfikacja na podstawie test-set.");
            System.out.println("2. Klasyfikacja na podstawie wprowadzonego wektora.");
            System.out.println("3. Wyjście z programu.");

            System.out.print("Wprowadź opcję : ");
            final int i = optionScanner.nextInt();

            if (i == 1) {
                final List<String> testFileLines = IOUtility.readFromPath(testSetPath);
                bayesClassifier.test(testFileLines); // Testowanie danych test-set
            } else if (i == 2) {
                System.out.print("Wprowadź wektor : ");
                final String vector = vectorScanner.nextLine();
                final List<String> vectorList = new ArrayList<>();
                vectorList.add(vector);
                bayesClassifier.test(vectorList);
            } else if (i == 3) {
                System.exit(0);
            }
            else {
                System.out.println("\nWprowadzono niepoprawną opcję!");
            }

            System.out.println();
        }

    }
}