import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class MoviewApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.addWebapp("", new File("src/main/").getAbsolutePath());
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }
}