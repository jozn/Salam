package org.jivesoftware.smack.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public final class FileUtils {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(FileUtils.class.getName());
    }

    public static InputStream getStreamForUrl(String url, ClassLoader loader) throws MalformedURLException, IOException {
        URI fileUri = URI.create(url);
        if (fileUri.getScheme() == null) {
            throw new MalformedURLException("No protocol found in file URL: " + url);
        } else if (!fileUri.getScheme().equals("classpath")) {
            return fileUri.toURL().openStream();
        } else {
            List<ClassLoader> classLoaders = getClassLoaders();
            if (loader != null) {
                classLoaders.add(0, loader);
            }
            for (ClassLoader resourceAsStream : classLoaders) {
                InputStream is = resourceAsStream.getResourceAsStream(fileUri.getSchemeSpecificPart());
                if (is != null) {
                    return is;
                }
            }
            return null;
        }
    }

    private static List<ClassLoader> getClassLoaders() {
        ClassLoader[] classLoaders = new ClassLoader[]{FileUtils.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        List<ClassLoader> loaders = new ArrayList(2);
        ClassLoader[] arr$ = classLoaders;
        for (int i$ = 0; i$ < 2; i$++) {
            ClassLoader classLoader = arr$[i$];
            if (classLoader != null) {
                loaders.add(classLoader);
            }
        }
        return loaders;
    }

    public static boolean addLines(String url, Set<String> set) throws MalformedURLException, IOException {
        InputStream is = getStreamForUrl(url, null);
        if (is == null) {
            return false;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true) {
            String line = br.readLine();
            if (line == null) {
                return true;
            }
            set.add(line);
        }
    }
}
