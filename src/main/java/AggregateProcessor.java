import java.util.List;
import java.util.stream.Collectors;

public class AggregateProcessor {
    @DataProcessor
    public List<String> removeDuplicates(List<String> data) {
        return data.stream().distinct().collect(Collectors.toList());
    }
}