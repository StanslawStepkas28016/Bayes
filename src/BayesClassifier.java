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
        for (String testFileLine : testFileLines) {
            final String[] testLine = testFileLine.split(",");

            // Atrybuty wyjściowe.
            final HashSet<String> outAttributes = differentAttributes.get(differentAttributes.size() - 1);

            // Mapa atrybut {wyjściowy : prawdopodobieństwo}.
            HashMap<String, Double> probabilities = new HashMap<>();

            // Wyliczenie prawdopodobieństw dla możliwych wyjść.
            for (String outAttribute : outAttributes) {
                // Zmienna przechowująca prawdopodobieństwo.
                double probability = 1.0;

                // Ilość atrybutów decyzyjnych w sumie.
                final int decisionAttributesCount = trainFileLines.size();

                // Ilość wystąpień dla danego atrybutu decyzyjnego.
                final int outDecisionAttributeCount = getAttributeCount(outAttribute, testLine.length);

                // Ilości wystąpień danych atrybutów pod danymi warunkami z wektora testowego.
                // Lista nie musi być typu double, ale dla większej przejrzystości będzie typu double.
                ArrayList<Double> attributeCountsUnderCondition = new ArrayList<>();
                for (int i = 0; i < testLine.length; i++) {
                    attributeCountsUnderCondition
                            .add((double) getAttributeCountUnderCondition(testLine[i], i, outAttribute));
                }

                // Wyliczenie prawdopodobieństwa ze wzoru Bayesa.
                probability *= ((double) outDecisionAttributeCount / (double) decisionAttributesCount);
                final int dataSize = testLine.length;
                for (int i = 0; i < dataSize; i++) {

                    double part = (attributeCountsUnderCondition.get(i) / outDecisionAttributeCount);

                    // Wygładzenie Laplace'a.
                    if (part == 0) {
                        part = ((attributeCountsUnderCondition.get(i) + 1)
                                / (outDecisionAttributeCount + differentAttributes.get(i).size()));
                    }

                    probability *= part;
                }

                // Dodanie do mapy prawdopodobieństw.
                probabilities.put(outAttribute, probability);
            }

            // Ustalenie najwyższego prawdopodobieństwa z mapy prawdopodobieństw.
            final String res = Collections.max(probabilities.entrySet(), Map.Entry.comparingByValue()).getKey();

            // Wypisanie wyniku.
            System.out.println(STR."For vector : \{testFileLine} : computed result : \{res}");
        }

    }

    private int getAttributeCount(String attribute, int index) {
        int count = 0;

        for (String trainFileLine : trainFileLines) {
            final String[] trainLine = trainFileLine.split(",");

            if (trainLine[index].contains(attribute)) {
                count += 1;
            }
        }

        return count;
    }

    private int getAttributeCountUnderCondition(String attribute, int attribIndex, String conditionAttribute) {
        int count = 0;

        for (String trainFileLine : trainFileLines) {
            final String[] trainLine = trainFileLine.split(",");

            if (trainLine[attribIndex].equals(attribute) && trainLine[trainLine.length - 1].equals(conditionAttribute)) {
                count += 1;
            }
        }

        return count;
    }
}
