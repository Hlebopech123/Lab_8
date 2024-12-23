import java.util.List;
import java.util.stream.Collectors;

public class TransformProcessor {
    @DataProcessor
    public List<String> transformToUpperCase(List<String> data) {
        return data.stream()
                .map(String::toUpperCase) // Преобразование в верхний регистр
                .collect(Collectors.toList());
    }
}