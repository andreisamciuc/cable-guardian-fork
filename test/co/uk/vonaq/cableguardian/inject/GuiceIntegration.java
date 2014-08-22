package co.uk.vonaq.cableguardian.inject;

import co.uk.vonaq.cableguardian.inject.TestModule;
import org.junit.runners.model.InitializationError;

public class GuiceIntegration extends GuiceTestRunner {
    public GuiceIntegration(Class classToRun) throws InitializationError {
        super(classToRun, new TestModule());
    }
}
