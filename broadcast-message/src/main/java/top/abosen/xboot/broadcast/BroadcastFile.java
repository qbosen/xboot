package top.abosen.xboot.broadcast;

import java.io.*;
import java.util.Properties;

import static com.google.common.base.Charsets.UTF_8;

/**
 * A helper class for reading and writing Services files.
 */
final class BroadcastFile {
    public static final String FILE_PATH = "META-INF/broadcast.properties";

    private BroadcastFile() {
    }

    /**
     * Reads the set of service classes from a service file.
     *
     * @param input not {@code null}. Closed after use.
     * @return a not {@code null Set} of service class names.
     * @throws IOException
     */
    static Properties readConfigFile(InputStream input) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(input, UTF_8));
        return properties;
    }

    /**
     * Writes the set of service class names to a service file.
     *
     * @param output     not {@code null}. Not closed after use.
     * @param properties a not {@code null Collection} of service class names.
     * @throws IOException
     */
    static void writeConfigFile(Properties properties, OutputStream output)
            throws IOException {
        properties.store(new OutputStreamWriter(output, UTF_8), "message name and implementation");
    }
}
