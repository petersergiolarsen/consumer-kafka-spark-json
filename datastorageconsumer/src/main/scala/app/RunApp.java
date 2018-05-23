package app;

import app.kafka.ConsumeData;
import com.govcloud.digst.Organisation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RunApp {

    private String consumerConfig = null;
    private String topicConfig = null;


    public static void main(String...args) throws IOException {

        if (args.length<1)
        {
            System.exit(-1);
        }

        String confDir = args[0];
        RunApp app = new RunApp();
        app.setup(confDir);
        app.run();

    }

    public void run() throws IOException {

        Properties consumerProps = readProperties(consumerConfig);
        Properties topicProps = readProperties(topicConfig);
        ConsumeData<byte[]> consumer = new ConsumeData<byte[]>(consumerProps, topicProps);
        consumer.setupSerializer(Organisation.getClassSchema());
        consumer.run();


    }

    public void setup(String confDir) throws IOException {

        Properties applicationProps = readProperties(confDir);
        consumerConfig = applicationProps.getProperty("consumer.config");
        topicConfig = applicationProps.getProperty("topic.config");

    }

    public Properties readProperties(String path) throws IOException {

        Properties props = new Properties();
        InputStream inStream = new FileInputStream(path);
        props.load(inStream);
        return props;


    }


}
