package ru;

import ru.parser.IParser;
import ru.parser.Selecting;
import ru.preprocessor.IPreprocessor;
import ru.preprocessor.Preprocessor_v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class Test_2 {
    public static void main(String[] args) throws IOException {
        IPreprocessor preprocessor = new Preprocessor_v1();
        File file_cmc = preprocessor.process(new File("test_2.cm"));
        IParser parser = new Selecting().select(new FileInputStream(file_cmc));
        parser.process(new FileInputStream(file_cmc), Collections.singletonList("hatch_a_b_c"));
    }
}