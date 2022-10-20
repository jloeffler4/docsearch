import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.*;

public class DocSearchTest {
    @Test
    public void testDocSearch() throws IOException, URISyntaxException {
        Handler handler = new Handler("./technical");
        String res = handler.handleRequest(new URI("http://localhost/"));
        assertEquals("There are 1391 files to search", res);
        String res2 = handler.handleRequest(new URI("http://localhost/search?q=Darwin"));
        assertEquals("There were 8 files found:\n./technical/biomed/1471-2105-3-2.txt\n./technical/plos/journal.pbio.0020046.txt\n./technical/plos/journal.pbio.0020071.txt\n./technical/plos/journal.pbio.0020302.txt\n./technical/plos/journal.pbio.0020311.txt\n./technical/plos/journal.pbio.0020346.txt\n./technical/plos/journal.pbio.0020347.txt\n./technical/plos/journal.pbio.0020439.txt\n", res2);

    }
}
