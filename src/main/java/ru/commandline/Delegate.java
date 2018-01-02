package ru.commandline;

import com.beust.jcommander.Parameter;

import java.io.File;
import java.util.List;

/**
 *
 */
public class Delegate {

    @Parameter(names = {"--cm"}, converter = FileConverter.class, arity = 1, required = true,
            description = "File cm files") //variableArity = true
    private File file;

//    @Parameter(names = {"--debug"},
//            description = "Debug mode", hidden = true)
//    private boolean debug = true;

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help = false;

    public File getFile() {
        return file;
    }

//    public boolean isDebug() {
//        return debug;
//    }

    public boolean isHelp() {
        return help;
    }
}
