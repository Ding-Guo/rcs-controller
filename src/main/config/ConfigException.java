package main.config;
public class ConfigException extends Exception {
    public ConfigException(String filename) {
        this(filename, -1, "Unknown error", (Throwable)null);
    }

    public ConfigException(String filename, String msg) {
        this(filename, -1, msg, (Throwable)null);
    }

    public ConfigException(String filename, Throwable cause) {
        this(filename, -1, cause.toString(), cause);
    }

    public ConfigException(String filename, int linenumber) {
        this(filename, linenumber, "Unknown error", (Throwable)null);
    }

    public ConfigException(String filename, int linenumber, String msg) {
        this(filename, linenumber, msg, (Throwable)null);
    }

    public ConfigException(String filename, int linenumber, Throwable cause) {
        this(filename, linenumber, cause.toString(), cause);
    }

    public ConfigException(String filename, String msg, Throwable cause) {
        this(filename, -1, msg, cause);
    }

    public ConfigException(String filename, int lineNumber, String msg, Throwable cause) {
        super((filename == null ? "" : filename + ": ") + (lineNumber > 0 ? "Line " + lineNumber + ": " : "") + (msg == null ? "" : msg), cause);
    }
}
