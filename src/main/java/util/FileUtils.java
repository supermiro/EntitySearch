package util;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {

    public static ArrayList<File> listFilesForFolder(File folder, String regex) {
        ArrayList<File> fA = new ArrayList<File>();
        if (!folder.isDirectory()) {
            fA.add(folder);
        } else {

            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    fA.addAll(listFilesForFolder(fileEntry, regex));
                } else if (fileEntry.getName().matches(regex)) {
                    fA.add(fileEntry);
                    //o.println(fileEntry.getName());
                }
            }
        }
        return fA;
    }
}
