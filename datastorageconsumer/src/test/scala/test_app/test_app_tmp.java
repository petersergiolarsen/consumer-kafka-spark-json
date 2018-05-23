package test_app;

import app.test.RunAppTmp;
import org.junit.Test;

public class test_app_tmp
{

    @Test
    public void test()
    {
        String[] args = new String[1];
        args[0] = "/home/datascience/GovCloudApplications/consumer/digst/datastorageconsumer/src/test/resources/config/application.properties";
        RunAppTmp.main(args);
    }

}
