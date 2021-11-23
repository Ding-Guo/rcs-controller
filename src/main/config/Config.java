//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.SeedGenerator;
import main.log.Logger;

public class Config {
    private static final String ARRAY_REGEX = " |,";
    private Map<String, String> data;
    private Set<String> noCache;
    private Map<String, Integer> intData;
    private Map<String, Double> floatData;
    private Map<String, Boolean> booleanData;
    private Map<String, List<String>> arrayData;
    private static Random seedGenerator;
    private Random random;

    public Config() {
        this.data = new HashMap();
        this.noCache = new HashSet();
        this.intData = new HashMap();
        this.floatData = new HashMap();
        this.booleanData = new HashMap();
        this.arrayData = new HashMap();
    }

    public Config(File file) throws ConfigException {
        this();
        this.read(file);
    }

    public Config(Config other) {
        this();
        this.data.putAll(other.data);
    }

    public void read(String resource) throws ConfigException {
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null");
        } else {
            (new Config.ResourceContext(resource)).process(this);
//            this.checkAllConstraints();
        }
    }

    public void read(File file) throws ConfigException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        } else {
            (new Config.FileContext(file)).process(this);
//            this.checkAllConstraints();
        }
    }

    public void read(Reader reader, String name) throws ConfigException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        } else {
            (new Config.ReaderContext(reader, name)).process(this);
//            this.checkAllConstraints();
        }
    }

    private void readWithContext(BufferedReader reader, Config.Context context) throws ConfigException {
        String line = "";
        int lineNumber = 0;
        String name = context.getName();

        try {
            while(line != null) {
                try {
                    line = reader.readLine();
                } catch (IOException var14) {
                    throw new ConfigException(name, var14);
                }

                ++lineNumber;
                if (line != null) {
                    int hashIndex = line.indexOf("#");
                    if (hashIndex != -1) {
                        line = line.substring(0, hashIndex).trim();
                    }

                    line = line.trim();
                    if (!"".equals(line)) {
                        Config.LineType.process(line, context, this, lineNumber);
                    }
                }
            }
        } finally {
            try {
                reader.close();
            } catch (IOException var13) {
                Logger.error("Error reading config", var13);
            }

        }

    }

    public void write(PrintWriter out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output cannot be null");
        } else {
            Iterator var2 = this.data.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<String, String> next = (Entry)var2.next();
                out.print((String)next.getKey());
                out.print(" : ");
                out.println((String)next.getValue());
            }

        }
    }

    public void merge(Config other) {
        this.clearCache();
        this.data.putAll(other.data);
//        this.checkAllConstraints();
    }


    public Set<String> getAllKeys() {
        return Collections.unmodifiableSet(this.data.keySet());
    }

    public boolean isDefined(String key) {
        return this.data.containsKey(key);
    }

    public String getValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (!this.data.containsKey(key)) {
            throw new NoSuchConfigOptionException(key);
        } else {
            return this.processDollarNotation(key, (String)this.data.get(key));
        }
    }

    public String getValue(String key, String defaultValue) {
        try {
            return this.getValue(key);
        } catch (NoSuchConfigOptionException var4) {
            return defaultValue;
        }
    }

    public int getIntValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (!this.noCache.contains(key) && this.intData.containsKey(key)) {
            return (Integer)this.intData.get(key);
        } else {
            int result = Integer.parseInt(this.getValue(key));
            this.intData.put(key, result);
            return result;
        }
    }

    public int getIntValue(String key, int defaultValue) {
        try {
            return this.getIntValue(key);
        } catch (NoSuchConfigOptionException var4) {
            return defaultValue;
        }
    }

    public double getFloatValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (!this.noCache.contains(key) && this.floatData.containsKey(key)) {
            return (Double)this.floatData.get(key);
        } else {
            double result = Double.parseDouble(this.getValue(key));
            this.floatData.put(key, result);
            return result;
        }
    }

    public double getFloatValue(String key, double defaultValue) {
        try {
            return this.getFloatValue(key);
        } catch (NoSuchConfigOptionException var5) {
            return defaultValue;
        }
    }

    public boolean getBooleanValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (!this.noCache.contains(key) && this.booleanData.containsKey(key)) {
            return (Boolean)this.booleanData.get(key);
        } else {
            boolean result = false;
            String value = this.getValue(key);
            if ("true".equalsIgnoreCase(value) || "t".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
                result = true;
            }

            this.booleanData.put(key, result);
            return result;
        }
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        try {
            return this.getBooleanValue(key);
        } catch (NoSuchConfigOptionException var4) {
            return defaultValue;
        }
    }

    public List<String> getArrayValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else {
            List entry;
            if (!this.noCache.contains(key) && this.arrayData.containsKey(key)) {
                entry = (List)this.arrayData.get(key);
                if (entry != null) {
                    return entry;
                }
            }

            entry = this.splitArrayValue(this.getValue(key));
            this.arrayData.put(key, entry);
            return entry;
        }
    }

    public List<String> getArrayValue(String key, String defaultValue) {
        try {
            return this.getArrayValue(key);
        } catch (NoSuchConfigOptionException var4) {
            return defaultValue == null ? null : this.splitArrayValue(defaultValue);
        }
    }

    private List<String> splitArrayValue(String value) {
        List<String> result = new ArrayList();
        String[] s = value.split(" |,");
        String[] var4 = s;
        int var5 = s.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String next = var4[var6];
            if (!"".equals(next)) {
                result.add(next);
            }
        }

        return result;
    }

    public void setValue(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (value == null) {
            this.removeKey(key);
        } else {
            this.clearCache(key);
            this.noCache.remove(key);
            this.data.put(key, value);
//            this.checkAllConstraints();
        }
    }

    public void appendValue(String key, String value) {
        this.appendValue(key, value, " ");
    }

    public void appendValue(String key, String value, String separator) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        } else {
            this.clearCache(key);
            if (this.data.containsKey(key)) {
                String old = (String)this.data.get(key);
                this.data.put(key, old + separator + value);
            } else {
                this.data.put(key, value);
            }

//            this.checkAllConstraints();
        }
    }

    public void setIntValue(String key, int value) {
        this.setValue(key, Integer.valueOf(value).toString());
        this.intData.put(key, value);
    }

    public void setFloatValue(String key, double value) {
        this.setValue(key, Double.valueOf(value).toString());
        this.floatData.put(key, value);
    }

    public void setBooleanValue(String key, boolean value) {
        this.setValue(key, value ? "true" : "false");
        this.booleanData.put(key, value);
    }

    public void removeKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        } else {
            this.clearCache(key);
            this.noCache.remove(key);
            this.data.remove(key);
        }
    }

    public void removeAllKeys() {
        this.data.clear();
        this.noCache.clear();
        this.intData.clear();
        this.floatData.clear();
        this.booleanData.clear();
        this.arrayData.clear();
    }

    public void removeExcept(String... exceptions) {
        this.removeExcept((Collection)Arrays.asList(exceptions));
    }

    public void removeExcept(Collection<String> exceptions) {
        this.data.keySet().retainAll(exceptions);
        this.noCache.retainAll(exceptions);
    }

    public void removeExceptRegex(Collection<String> exceptions) {
        Set<String> toRemove = new HashSet(this.data.keySet());
        Logger.debug("Removing all except " + exceptions);
        Iterator var3 = exceptions.iterator();

        String exception;
        while(var3.hasNext()) {
            exception = (String)var3.next();
            Pattern p = Pattern.compile(exception);
            Iterator var6 = this.data.keySet().iterator();

            while(var6.hasNext()) {
                String key = (String)var6.next();
                if (p.matcher(key).matches()) {
                    Logger.debug(key + " matches " + exception);
                    toRemove.remove(key);
                }
            }
        }

        Logger.debug("Removing " + toRemove);
        var3 = toRemove.iterator();

        while(var3.hasNext()) {
            exception = (String)var3.next();
            this.data.remove(exception);
            this.noCache.remove(exception);
        }

    }

    public Random getRandom() {
        synchronized(this) {
            if (this.random == null) {
                String className = this.getValue("random.class", "org.uncommons.maths.random.MersenneTwisterRNG");
                String seed = this.getValue("random.seed", "");

                try {
                    Class<? extends Random> clazz = Class.forName(className).asSubclass(Random.class);
                    Logger.debug("Instantiating random number generator: " + className);
                    if ("".equals(seed)) {
                        long mtime = (new Date()).getTime();
                        seed = String.valueOf(mtime);
                    }

                    Logger.debug("Using seed " + seed);
                    BigInteger bi = new BigInteger(seed, 16);

                    Constructor constructor;
                    try {
                        Logger.trace("Trying to find a SeedGenerator constructor");
                        constructor = clazz.getConstructor(SeedGenerator.class);
                        this.random = (Random)constructor.newInstance(new Config.StaticSeedGenerator(bi.toByteArray()));
                        Logger.trace("Success");
                    } catch (IllegalAccessException var14) {
                        Logger.trace("SeedGenerator constructor for " + className, var14);
                    } catch (InstantiationException var15) {
                        Logger.trace("SeedGenerator constructor for " + className, var15);
                    } catch (NoSuchMethodException var16) {
                        Logger.trace("SeedGenerator constructor for " + className, var16);
                    } catch (InvocationTargetException var17) {
                        Logger.trace("SeedGenerator constructor for " + className, var17);
                    }

                    if (this.random == null) {
                        Logger.trace("Trying to find a long constructor");

                        try {
                            constructor = clazz.getConstructor(Long.TYPE);
                            this.random = (Random)constructor.newInstance(bi.longValue());
                            Logger.trace("Success");
                        } catch (IllegalAccessException var10) {
                            Logger.trace("Long constructor for " + className, var10);
                        } catch (InstantiationException var11) {
                            Logger.trace("Long constructor for " + className, var11);
                        } catch (NoSuchMethodException var12) {
                            Logger.trace("Long constructor for " + className, var12);
                        } catch (InvocationTargetException var13) {
                            Logger.trace("Long constructor for " + className, var13);
                        }
                    }

                    if (this.random == null) {
                        try {
                            Logger.trace("Trying to find no-arg constructor");
                            this.random = (Random)clazz.newInstance();
                            Logger.trace("Success");
                        } catch (IllegalAccessException var8) {
                            Logger.trace("No-arg constructor for " + className, var8);
                        } catch (InstantiationException var9) {
                            Logger.trace("No-arg constructor for " + className, var9);
                        }
                    }
                } catch (ClassNotFoundException var18) {
                    Logger.debug("Class not found: " + className);
                }

                if (this.random == null) {
                    Logger.debug("Using fallback RNG");
                    this.random = new MersenneTwisterRNG();
                }
            }

            return this.random;
        }
    }

    private void clearCache() {
        this.intData.clear();
        this.floatData.clear();
        this.booleanData.clear();
        this.arrayData.clear();
    }

    private void clearCache(String key) {
        this.intData.remove(key);
        this.floatData.remove(key);
        this.booleanData.remove(key);
        this.arrayData.remove(key);
    }

    private String processDollarNotation(String key, String value) {
        int index = value.indexOf("${");
        if (index == -1) {
            return value;
        } else {
            this.noCache.add(key);
            int end = value.indexOf("}", index);
            int colon = value.indexOf(":", index);
            String reference = value.substring(index + 2, end);
            String defaultValue = null;
            if (colon > index && colon < end) {
                reference = value.substring(index + 2, colon);
                defaultValue = value.substring(colon + 1, end);
            }

            StringBuilder result = new StringBuilder();
            result.append(value.substring(0, index));
            result.append(this.resolveReferences(reference, defaultValue));
            result.append(this.processDollarNotation(key, value.substring(end + 1)));
            return result.toString();
        }
    }

    private String resolveReferences(String s, String defaultValue) {
        try {
            return this.getValue(s);
        } catch (NoSuchConfigOptionException var4) {
            if (defaultValue != null) {
                return defaultValue;
            } else {
                throw var4;
            }
        }
    }

//    private void checkAllConstraints() {
//        this.violatedConstraints.clear();
//        Iterator var1 = this.constraints.iterator();
//
//        while(var1.hasNext()) {
//            ConfigConstraint next = (ConfigConstraint)var1.next();
//            this.checkConstraint(next);
//        }
//
//    }
//
//    private void checkConstraint(ConfigConstraint c) {
//        if (c != null) {
//            if (c.isViolated(this)) {
//                this.violatedConstraints.add(c);
//            } else {
//                this.violatedConstraints.remove(c);
//            }
//
//        }
//    }

    private interface Context {
        String getName();

        void process(Config var1) throws ConfigException;

        Config.Context include(String var1) throws ConfigException;
    }

    private static class ResourceContext implements Config.Context {
        private String name;

        public ResourceContext(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void process(Config config) throws ConfigException {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(this.name);
            if (in == null) {
                throw new ConfigException(this.name, "Resource not found");
            } else {
                config.readWithContext(new BufferedReader(new InputStreamReader(in)), this);
            }
        }

        public Config.Context include(String path) throws ConfigException {
            return new Config.ResourceContext(path);
        }
    }

    private static class FileContext implements Config.Context {
        private File file;

        public FileContext(File file) {
            this.file = file;
        }

        public String getName() {
            return this.file.getAbsolutePath();
        }

        public void process(Config config) throws ConfigException {
            if (!this.file.exists()) {
                throw new ConfigException(this.getName(), "File does not exist");
            } else if (!this.file.isDirectory()) {
                try {
                    config.readWithContext(new BufferedReader(new FileReader(this.file)), this);
                } catch (IOException var6) {
                    throw new ConfigException(this.getName(), var6);
                }
            } else {
                File[] var2 = this.file.listFiles();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File next = var2[var4];
                    (new Config.FileContext(next)).process(config);
                }

            }
        }

        public Config.Context include(String path) throws ConfigException {
            File newFile = new File(this.file.getParentFile(), path);
            return new Config.FileContext(newFile);
        }
    }

    private static class ReaderContext implements Config.Context {
        private BufferedReader reader;
        private String name;

        public ReaderContext(Reader r, String name) {
            this.name = name;
            if (r instanceof BufferedReader) {
                this.reader = (BufferedReader)r;
            } else {
                this.reader = new BufferedReader(r);
            }

        }

        public String getName() {
            return this.name;
        }

        public void process(Config config) throws ConfigException {
            config.readWithContext(this.reader, this);
        }

        public Config.Context include(String path) throws ConfigException {
            throw new ConfigException(this.name, "Cannot process include directives when reading raw data streams");
        }
    }

    private static enum LineType {
        INCLUDE {
            public void process(Matcher matcher, Config.Context context, Config config, int lineNumber) throws ConfigException {
                String includeName = matcher.group(1).trim();
                if ("".equals(includeName)) {
                    throw new ConfigException(context.getName(), lineNumber, "Empty include directive");
                } else {
                    Logger.trace("Reading included config '" + includeName + "'");
                    Config.Context newContext = context.include(includeName);
                    newContext.process(config);
                }
            }

            protected String getRegex() {
                return "^!include\\s*(.*)";
            }
        },
        ADDITIVE {
            public void process(Matcher matcher, Config.Context context, Config config, int lineNumber) throws ConfigException {
                String key = matcher.group(1).trim();
                String value = matcher.group(2).trim();
                if ("".equals(key)) {
                    throw new ConfigException(context.getName(), lineNumber, "Empty key");
                } else if ("".equals(value)) {
                    throw new ConfigException(context.getName(), lineNumber, "Empty value");
                } else {
                    String existing = config.getValue(key, (String)null);
                    if (existing != null && !"".equals(existing)) {
                        Logger.trace("Appending '" + value + "' to '" + key + "'");
                        value = existing + " " + value;
                    } else {
                        Logger.trace("Setting '" + key + "' to '" + value + "'");
                    }

                    config.setValue(key, value);
                }
            }

            protected String getRegex() {
                return "^([^+]*)(?:\\+:|=)(.*)";
            }
        },
        NORMAL {
            public void process(Matcher matcher, Config.Context context, Config config, int lineNumber) throws ConfigException {
                String key = matcher.group(1).trim();
                String value = matcher.group(2).trim();
                if ("".equals(key)) {
                    throw new ConfigException(context.getName(), lineNumber, "Empty key");
                } else if ("".equals(value)) {
                    throw new ConfigException(context.getName(), lineNumber, "Empty value");
                } else {
                    Logger.trace("Setting '" + key + "' to '" + value + "'");
                    if (config.isDefined(key) && !value.equals(config.getValue(key))) {
                        Logger.warn("Redefining config key '" + key + "' as '" + value + "'");
                    }

                    config.setValue(key, value);
                }
            }

            protected String getRegex() {
                return "^([^:=]*)(?::|=)(.*)";
            }
        };

        private Pattern pattern;

        private LineType() {
            this.pattern = Pattern.compile(this.getRegex());
        }

        public Pattern getPattern() {
            return this.pattern;
        }

        protected abstract String getRegex();

        protected abstract void process(Matcher var1, Config.Context var2, Config var3, int var4) throws ConfigException;

        public static void process(String line, Config.Context context, Config config, int lineNumber) throws ConfigException {
            Config.LineType[] var4 = values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Config.LineType next = var4[var6];
                Matcher matcher = next.getPattern().matcher(line);
                if (matcher.matches()) {
                    next.process(matcher, context, config, lineNumber);
                    return;
                }
            }

            throw new ConfigException(context.getName(), lineNumber, "Unrecognised config option: '" + line + "'");
        }
    }

    private static class StaticSeedGenerator implements SeedGenerator {
        private byte[] data;

        StaticSeedGenerator(byte[] data) {
            this.data = data;
        }

        public byte[] generateSeed(int length) {
            byte[] result = new byte[length];
            System.arraycopy(this.data, 0, result, 0, Math.min(this.data.length, result.length));
            return result;
        }
    }
}
