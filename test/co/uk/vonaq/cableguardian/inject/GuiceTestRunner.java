package co.uk.vonaq.cableguardian.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class GuiceTestRunner extends BlockJUnit4ClassRunner {
    private final Injector injector;

    public GuiceTestRunner(final Class<?> classToRun, Module... modules) throws InitializationError {
        super(classToRun);
        this.injector = Guice.createInjector(modules);
    }

    @Override
    public Object createTest() {
        return injector.getInstance(getTestClass().getJavaClass());
    }

    @Override
    protected void validateZeroArgConstructor(List<Throwable> errors) {
        // Guice can inject constructors with parameters so we don't want this method to trigger an error
    }

    protected Injector getInjector() {
        return injector;
    }
}