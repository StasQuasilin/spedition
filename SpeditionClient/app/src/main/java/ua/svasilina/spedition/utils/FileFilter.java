package ua.svasilina.spedition.utils;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {

    private final String mask;

    FileFilter(String mask) {
        this.mask = mask;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.contains(mask);
    }
}
