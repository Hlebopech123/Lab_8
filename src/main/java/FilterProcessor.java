import java.util.List;
import java.util.stream.Collectors;

public class FilterProcessor {
    @DataProcessor
    public List<String> filterWordsWithLengthFour(List<String> data) {
        return data.stream()
                .filter(word -> word.length() == 4) // Фильтрация слов длиной ровно 4
                .collect(Collectors.toList());
    }
}