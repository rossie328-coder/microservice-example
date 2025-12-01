import org.example.controller.init.Initialization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MyTest {
    private Initialization init;

    @BeforeEach
    public void setUp() {
        init = new Initialization();
        init.start();
    }

    @Test
    public void testInit() {
        Set<String> topics = init.getSubscribedTopic();
        Assertions.assertTrue(topics.contains("system/entrance/manual/intern/button"));
    }

    @Test
    public void firstMessageReceived() throws InterruptedException {
        Thread.sleep(10000);
        Set<String> topics = init.getAllSubscribedTopics();
        Assertions.assertTrue(topics.contains("system/entrance/manual/intern/button"));
    }

    @Test
    public void cameraMessageReceived() throws InterruptedException {
        Thread.sleep(10000);
        Set<String> topics = init.getAllSubscribedTopics();
        Assertions.assertTrue(topics.contains("system/entrance/manual/123456/intern/camera/response"));
    }

    @Test
    public void ServerMessageReceived() throws InterruptedException {
        Thread.sleep(10000);
        Set<String> topics = init.getAllSubscribedTopics();
        Assertions.assertTrue(topics.contains("system/entrance/manual/123456/server/data"));
    }


}
