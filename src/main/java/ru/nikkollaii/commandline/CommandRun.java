package ru.nikkollaii.commandline;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Parameters(commandNames = {"run"},
        commandDescription = "Computation a out states based on files *.cm")
public class CommandRun {
    @Parameter(names = {"-c", "--core"},
            //arity = 1,
            description = "Name core optimize")
    private String core = "time";

    @Parameter(names = {"-o", "--out"},
            variableArity = true,
            required = true,
            description = "Names out states")
    private List<String> outStates = new ArrayList<>();

    @Parameter(names = {"--cm", "--files"},
            converter = FileConverter.class,
            variableArity = true,
            required = true,
            description = "Names cm files")
    private List<File> files = new ArrayList<>();

    @Parameter(names = {"--help", "-h"},
            help = true)
    private boolean help = false;

    @Parameter(names = {"--not-rm"})
    private boolean notRm = false;

    @Parameter(names = {"--start-states"},
            //arity = 1,
            variableArity = true,
            description = "Name start states")
    private List<String> startStates = new ArrayList<>();

    @Parameter(names = {"--dir-environment"},
            converter = FileConverter.class,
            variableArity = true,
            description = "Directories containing start ")
    private List<File> dirEnvironment = new ArrayList<File>(){{add(new File("."));}};

    public String getCore() {
        return core;
    }

    public List<String> getOutStates() {
        return outStates;
    }

    public List<File> getFiles() {
        return files;
    }

    public boolean isHelp() {
        return help;
    }

    public List<File> getDirEnvironment() {
        return dirEnvironment;
    }

    public List<String> getStartStates() {
        return startStates;
    }

    public boolean isNotRm() {
        return notRm;
    }
}
