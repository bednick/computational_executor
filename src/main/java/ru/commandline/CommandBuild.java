package ru.commandline;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

/**
 *
 */
@Parameters(commandNames = {"build"}, commandDescription = "Preprocessing a file *.cm")
public class CommandBuild {
    public CommandBuild() {}

    public CommandBuild(Delegate delegate) {
        this.delegate = delegate;
    }

    @ParametersDelegate
    private Delegate delegate = new Delegate();

    public Delegate getDelegate() {
        return delegate;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }
}
