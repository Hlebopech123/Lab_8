public class Application {
    public static void main(String[] args) {
        try {
            DataManager dataManager = new DataManager();

            // Регистрация обработчиков
            dataManager.registerDataProcessor(new FilterProcessor());
            dataManager.registerDataProcessor(new TransformProcessor());
            dataManager.registerDataProcessor(new AggregateProcessor());

            // Указание путей к файлам
            String source = "E:\\Lab_8\\input.txt";
            String destination = "E:\\Lab_8\\output.txt";

            // Загрузка данных
            dataManager.loadData(source);

            // Обработка данных
            dataManager.processData();

            // Сохранение данных
            dataManager.saveData(destination);

            System.out.println("Данные успешно обработаны и сохранены в " + destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}