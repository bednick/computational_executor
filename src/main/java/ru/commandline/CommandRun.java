package ru.commandline;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

import java.io.File;
import java.util.List;

/**
 *
 */
@Parameters(commandNames = {"run"}, commandDescription = "Build and obtaining a out files")
public class CommandRun {
//    @Parameter(converter = FileConverter.class,
//            description = "")
//    private File fileIn;
    @Parameter(names = {"-c", "--core"}, arity = 1,
            description = "Name core optimize")
    private String core = "time";

    @Parameter(names = {"-o", "--out"}, variableArity = true, required = true,
            description = "Name out file")
    private List<String> outStates;

    @ParametersDelegate
    private Delegate delegate = new Delegate();

    public String getCore() {
        return core;
    }

    public List<String> getOutStates() {
        return outStates;
    }

    public Delegate getDelegate() {
        return delegate;
    }
}
