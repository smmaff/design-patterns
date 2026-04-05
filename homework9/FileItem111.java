package homework9;

import java.util.ArrayList;
import java.util.List;

public class FileItem111 {
    static class TV {
        private boolean on;

        public void on() {
            if (!on) {
                on = true;
                System.out.println("TV включен");
            } else {
                System.out.println("TV уже включен");
            }
        }

        public void off() {
            if (on) {
                on = false;
                System.out.println("TV выключен");
            } else {
                System.out.println("TV уже выключен");
            }
        }

        public void setChannel(int channel) {
            if (on) {
                System.out.println("TV переключен на канал " + channel);
            } else {
                System.out.println("Сначала включите TV");
            }
        }

        public void setInput(String input) {
            if (on) {
                System.out.println("TV установлен на вход: " + input);
            } else {
                System.out.println("Сначала включите TV");
            }
        }
    }

    static class AudioSystem {
        private boolean on;
        private int volume = 10;

        public void on() {
            if (!on) {
                on = true;
                System.out.println("Аудиосистема включена");
            } else {
                System.out.println("Аудиосистема уже включена");
            }
        }

        public void off() {
            if (on) {
                on = false;
                System.out.println("Аудиосистема выключена");
            } else {
                System.out.println("Аудиосистема уже выключена");
            }
        }

        public void setVolume(int volume) {
            if (on) {
                this.volume = volume;
                System.out.println("Громкость установлена на " + this.volume);
            } else {
                System.out.println("Сначала включите аудиосистему");
            }
        }
    }

    static class DVDPlayer {
        public void play() {
            System.out.println("DVD воспроизводится");
        }

        public void pause() {
            System.out.println("DVD на паузе");
        }

        public void stop() {
            System.out.println("DVD остановлен");
        }
    }

    static class GameConsole {
        private boolean on;

        public void on() {
            if (!on) {
                on = true;
                System.out.println("Игровая консоль включена");
            } else {
                System.out.println("Игровая консоль уже включена");
            }
        }

        public void startGame(String game) {
            if (on) {
                System.out.println("Запущена игра: " + game);
            } else {
                System.out.println("Сначала включите игровую консоль");
            }
        }
    }

    static class HomeTheaterFacade {
        private TV tv;
        private AudioSystem audioSystem;
        private DVDPlayer dvdPlayer;
        private GameConsole gameConsole;

        public HomeTheaterFacade(TV tv, AudioSystem audioSystem, DVDPlayer dvdPlayer, GameConsole gameConsole) {
            this.tv = tv;
            this.audioSystem = audioSystem;
            this.dvdPlayer = dvdPlayer;
            this.gameConsole = gameConsole;
        }

        public void watchMovie() {
            System.out.println("\nЗапуск режима просмотра фильма");
            tv.on();
            tv.setChannel(1);
            audioSystem.on();
            audioSystem.setVolume(15);
            dvdPlayer.play();
        }

        public void endSession() {
            System.out.println("\nВыключение всей системы");
            dvdPlayer.stop();
            audioSystem.off();
            tv.off();
        }

        public void playGame(String game) {
            System.out.println("\nЗапуск режима игры");
            tv.on();
            tv.setInput("HDMI");
            audioSystem.on();
            audioSystem.setVolume(20);
            gameConsole.on();
            gameConsole.startGame(game);
        }

        public void listenMusic() {
            System.out.println("\nЗапуск режима прослушивания музыки");
            tv.on();
            tv.setInput("AUDIO");
            audioSystem.on();
            audioSystem.setVolume(12);
        }

        public void setVolume(int volume) {
            audioSystem.setVolume(volume);
        }
    }

    static abstract class FileSystemComponent {
        protected String name;

        public FileSystemComponent(String name) {
            this.name = name;
        }

        public abstract void display(String indent);
        public abstract int getSize();
    }

    static class FileItem extends FileSystemComponent {
        private int size;

        public FileItem(String name, int size) {
            super(name);
            this.size = size;
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "Файл: " + name + " (" + size + " KB)");
        }

        @Override
        public int getSize() {
            return size;
        }
    }

    static class Directory extends FileSystemComponent {
        private List<FileSystemComponent> components = new ArrayList<>();

        public Directory(String name) {
            super(name);
        }

        public void add(FileSystemComponent component) {
            if (component == null) {
                System.out.println("Нельзя добавить null в папку " + name);
                return;
            }
            if (components.contains(component)) {
                System.out.println("Компонент " + component.name + " уже существует в папке " + name);
                return;
            }
            components.add(component);
            System.out.println("Компонент " + component.name + " добавлен в папку " + name);
        }

        public void remove(FileSystemComponent component) {
            if (component == null) {
                System.out.println("Нельзя удалить null из папки " + name);
                return;
            }
            if (!components.contains(component)) {
                System.out.println("Компонент " + component.name + " не найден в папке " + name);
                return;
            }
            components.remove(component);
            System.out.println("Компонент " + component.name + " удален из папки " + name);
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "Папка: " + name + " (общий размер: " + getSize() + " KB)");
            for (FileSystemComponent component : components) {
                component.display(indent + "  ");
            }
        }

        @Override
        public int getSize() {
            int totalSize = 0;
            for (FileSystemComponent component : components) {
                totalSize += component.getSize();
            }
            return totalSize;
        }
    }

    public static void main(String[] args) {
        TV tv = new TV();
        AudioSystem audioSystem = new AudioSystem();
        DVDPlayer dvdPlayer = new DVDPlayer();
        GameConsole gameConsole = new GameConsole();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(tv, audioSystem, dvdPlayer, gameConsole);

        homeTheater.watchMovie();
        homeTheater.setVolume(18);
        homeTheater.endSession();

        homeTheater.playGame("FIFA 25");
        homeTheater.endSession();

        homeTheater.listenMusic();
        homeTheater.setVolume(14);
        homeTheater.endSession();

        System.out.println("\n====================\n");

        Directory root = new Directory("Root");
        Directory documents = new Directory("Documents");
        Directory images = new Directory("Images");
        Directory projects = new Directory("Projects");

        FileItem file1 = new FileItem("resume.docx", 120);
        FileItem file2 = new FileItem("photo.jpg", 850);
        FileItem file3 = new FileItem("project.zip", 1500);
        FileItem file4 = new FileItem("notes.txt", 50);

        documents.add(file1);
        documents.add(file4);
        images.add(file2);
        projects.add(file3);

        root.add(documents);
        root.add(images);
        root.add(projects);

        root.display("");

        System.out.println("\nУдаление файла notes.txt из Documents");
        documents.remove(file4);

        System.out.println("\nПовторный вывод структуры:");
        root.display("");
    }
}
