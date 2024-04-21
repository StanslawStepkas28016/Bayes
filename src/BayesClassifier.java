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

    public void test(List<String> testFileLines) {
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
