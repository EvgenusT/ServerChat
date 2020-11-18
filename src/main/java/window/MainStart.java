package window;

public class MainStart {
    public static void main(String[] args) {
        /**
         особенности Maven. Нужен класс с  main для запуска Main, который наследует Application.
         Без этого запуск с jar не возможен.
         */
        Main.main(args);
    }
}
