package app.deadmc.devnetworktool;

import org.junit.Test;

import app.deadmc.devnetworktool.helpers.SystemHelper;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        SystemHelper.INSTANCE.pingRequest();
    }
}