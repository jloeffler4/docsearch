import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileHelpers {
    static List<File> getFiles(Path start) throws IOException {
        File f = start.toFile();
        List<File> result = new ArrayList<>();
        if (f.isDirectory()) {
            System.out.println("It's a folder");
            File[] paths = f.listFiles();
            for (File subFile : paths) {
                result.addAll(getFiles(subFile.toPath()));
            }
        } else {
            result.add(start.toFile());
        }
        return result;
    }

    static String readFile(File f) throws IOException {
        //System.out.println(f.toString());
        return new String(Files.readAllBytes(f.toPath()));
    }
}

class Handler implements URLHandler {
    /**
     * @see https://github.com/jloeffler4/wavelet/blob/master/SearchEngine.java#L9
     */
    private String getQueryParameter(URI url, String key) {
        //Get the URL query
        String query = url.getQuery();
        if (query == null) return null;

        //Check each key-value pair
        String[] kvPairs = query.split("&");
        for (String kvPair : kvPairs) {
            String[] splitPair = kvPair.split("=");

            //Split pair into its components
            String pairKey = splitPair[0];
            String pairValue = splitPair[1];

            //Return the value for the matching key
            if (pairKey.equals(key)) return pairValue;
        }

        //Can return null if the key wasn't found (or no query component)
        return null;
    }

    List<File> files;

    Handler(String directory) throws IOException {
        this.files = FileHelpers.getFiles(Paths.get(directory));
    }

    public String handleRequest(URI url) throws IOException {
        switch (url.getPath()) {
            case "/": {
                return "There are " + this.files.size() + " files to search";
            }
            case "/search": {
                String query = getQueryParameter(url, "q");
                if (query == null) return "400 Bad Request";

                int matches = 0;
                String paths = "";
                for (File f : this.files) {
                    String txt = FileHelpers.readFile(f);
                    if (txt.contains(query)) {
                        matches++;
                        paths += f.getPath() + "\n";
                    }
                }

                return "There were " + matches + " files found:\n" + paths;
            }
            default: {
                return "Don't know how to handle that path!";
            }
        }
    }
}

class DocSearchServer {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler("./technical/"));
    }
}
