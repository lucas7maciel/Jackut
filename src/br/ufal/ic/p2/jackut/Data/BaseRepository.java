package br.ufal.ic.p2.jackut.Data;

import java.io.*;

public abstract class BaseRepository {
    protected final AppData appData;

    public BaseRepository(AppData appData) {
        this.appData = appData;
    }

    public static void saveToFile(AppData data, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    public static AppData loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (AppData) ois.readObject();
        }
    }
}
