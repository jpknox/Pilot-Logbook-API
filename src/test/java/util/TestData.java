package util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestData {

    public static String getText(String path) throws URISyntaxException, IOException {
        URI u = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
        File f = new File(u);
        Path p = f.toPath();
        byte[] bytes = Files.readAllBytes(p);
        String s = new String(bytes);
        return s;
    }
}
