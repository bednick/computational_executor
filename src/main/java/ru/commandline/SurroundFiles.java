package ru.commandline;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;


/**
 *
 */
public class SurroundFiles implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        // TODO check file exist
    }
}
