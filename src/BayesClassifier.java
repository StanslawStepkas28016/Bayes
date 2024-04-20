import java.util.*;

public class BayesClassifier {
    private final HashMap<Integer, HashSet<String>> differentAttributes;
    private List<String> trainFileLines;

    public BayesClassifier() {
        this.differentAttributes = new HashMap<>();
    }

    public void transformTrainSet(String trainSetPath) {
        // Przeczytanie zawartości pliku testowego oraz przypisanie
        // danych do pola, dane te będą wykorzystywane przy testowaniu.
        trainFileLines = IOUtility.readFromPath(trainSetPath);

        // Ilość potrzebnych do przechowywania różnych atrybutów hasz setów,
        // razem z inicjalizacją pustymi hasz setami.
        final int attributesCount = trainFileLines.getFirst().split(",").length;
        for (int i = 0; i < attributesCount; i++) {
            differentAttributes.put(i, new HashSet<>());
        }

        // Iterowanie przez dane w zbiorze testowym.
        for (String line : trainFileLines) {
            final String[] attributes = line.split(",");

            // Umieszczenie różnych atrybutów w mapie.
            for (int i = 0; i < attributes.length; i++) {
                differentAttributes.get(i).add(attributes[i]);
            }
        }
    }

    /*
     * LOGIKA DZIAŁANIA:
     * 1. Wczytaj jakoś dane z train-set.
     * 2. Wprowadź wektor testowy np. X=(overcast,hot,high,true), (nie ma piątego paramteru — tego decyzyjnego).
     * 3. Oblicz p. dla parametru decyzyjnego A i dla parametru decyzyjnego B, zwróć parametr, dla którego p. jest większe.
     *
     * SZCZEGÓŁY IMPLEMENTACYJNE:
     * - Obliczenie p. dla wektora X=(overcast,hot, high,true) dla paramteru decyzyjnego A, odbywa się w sposób następujący,
     * P(A|X) = (ilość wystąpień A w train-set / ilość wszystkich zdarzeń w train-set) * (ilość wystąpień "overcast" razem z A / ilość wystąpień overcast).
     * Analogicznie dzieje się to dla każdego kolejnego atrybutu, a następnie dla paramteru decyzyjnego B.
     * - Porównanie, które p. jest większe p(A) ? p(B), jeżeli p(A) > p(B), to zwracamy A, analogicznie, jeżeli jest na odwrót.
     *
     * WYJĄTKI:
     * - Jeżeli jakieś p. cząstkowe (pojedyncze), wychodzi 0, zastosuj wygładzenie (korzystamy wtedy z differentAttributes,
     * żeby pozyskać ilość różnych atrybutów, dla konkretnych parametrów, którą dodamy do mianownika),
     * */

    public void testUsingTestSet(String testSetPath) {
        // Przeczytanie zawartości pliku testowego.
        final List<String> testFileLines = IOUtility.readFromPath(testSetPath);

        // Iterowanie przez dane testowe.
        for (String testLine : testFileLines) {
            final String[] split = testLine.split(",");
            // to implement....
        }

    }
}
