import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.lang.reflect.Method;

public class DataManager {
    private final List<Method> processors = new ArrayList<>();
    private final List<Object> processorObjects = new ArrayList<>();
    private List<String> data = new ArrayList<>();

    // Регистрация обработчиков с аннотацией @DataProcessor
    public void registerDataProcessor(Object processor) {
        Method[] methods = processor.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(DataProcessor.class)) {
                processors.add(method);
                processorObjects.add(processor);
            }
        }
    }

    // Загрузка данных из файла
    public void loadData(String source) throws IOException {
        data = Files.readAllLines(Paths.get(source)).stream()
                .flatMap(line -> List.of(line.split(",")).stream())
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }

    // Многопоточная обработка данных
    public void processData() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CopyOnWriteArrayList<String> processedData = new CopyOnWriteArrayList<>(data);

        for (int i = 0; i < processors.size(); i++) {
            Method processor = processors.get(i);
            Object processorObject = processorObjects.get(i);

            executor.submit(() -> {
                try {
                    List<String> result = (List<String>) processor.invoke(processorObject, new ArrayList<>(processedData));
                    synchronized (processedData) {
                        processedData.clear();
                        processedData.addAll(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Ожидание завершения всех задач
        }

        data = new ArrayList<>(processedData);
    }

    // Сохранение обработанных данных в файл
    public void saveData(String destination) throws IOException {
        String result = data.stream()
                .filter(word -> word.length() == 4) // Фильтруем слова длиной ровно 4 символа
                .map(String::toUpperCase)          // Преобразуем слова в верхний регистр
                .collect(Collectors.joining(", ")); // Объединяем слова через ", "

        Files.writeString(Paths.get(destination), result); // Записываем строку в файл
    }
}