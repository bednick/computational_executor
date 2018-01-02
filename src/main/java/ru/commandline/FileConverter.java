package ru.commandline;

import com.beust.jcommander.IStringConverter;

import java.io.File;

/**
 * Created by BODY on 02.01.2018.
 */
public class FileConverter implements IStringConverter<File> {
    @Override
    public File convert(String path) {
        return new File(path);
    }
}
