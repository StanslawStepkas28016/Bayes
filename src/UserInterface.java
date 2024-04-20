public class UserInterface {
    public static void main(String[] args) {
        final String trainSetPath = args[0]; // Ścieżka train-set.
        final String testSetPath = args[1]; // Ścieżka test-set.

        final BayesClassifier bayesClassifier = new BayesClassifier(); // Obiekt klasy BayesClassifier.
        bayesClassifier.transformTrainSet(trainSetPath); // Procesowanie danych train-set.
        bayesClassifier.testUsingTestSet(testSetPath); // Testowanie danych test-set
    }
}