package ru.nikkollaii.preprocessor;

import java.io.File;
import java.io.IOException;

public interface IPreprocessor {

    File process(File file) throws IOException;
}
