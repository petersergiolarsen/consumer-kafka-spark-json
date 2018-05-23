package test_app;

import app.RunApp;
import org.junit.Test;

import java.io.IOException;

public class TestMain {

    @Test
    public void test_main() throws IOException {
        String[] args = new String[1];
        args[0] = "/home/datascience/GovCloudApplications/consumer/digst/datastorageconsumer/src/test/resources/config/application.properties";
        RunApp.main(args);

    }
}
