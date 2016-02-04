package de.measite.minidns;

import de.measite.minidns.DNSMessage.RESPONSE_CODE;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER;
    protected int bufferSize;
    protected DNSCache cache;
    protected Random random;
    protected int timeout;

    static {
        LOGGER = Logger.getLogger(Client.class.getName());
    }

    public Client(DNSCache cache) {
        this.bufferSize = 1500;
        this.timeout = 5000;
        try {
            this.random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            this.random = new SecureRandom();
        }
        this.cache = cache;
    }

    public Client() {
        this(null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private de.measite.minidns.DNSMessage query$3d14a83a(de.measite.minidns.Question r12, java.lang.String r13) throws java.io.IOException {
        /*
        r11 = this;
        r6 = 0;
        r5 = 0;
        r7 = r11.cache;
        if (r7 != 0) goto L_0x000b;
    L_0x0006:
        r1 = r5;
    L_0x0007:
        if (r1 == 0) goto L_0x0012;
    L_0x0009:
        r5 = r1;
    L_0x000a:
        return r5;
    L_0x000b:
        r7 = r11.cache;
        r1 = r7.get(r12);
        goto L_0x0007;
    L_0x0012:
        r2 = new de.measite.minidns.DNSMessage;
        r2.<init>();
        r7 = 1;
        r7 = new de.measite.minidns.Question[r7];
        r7[r6] = r12;
        r2.setQuestions(r7);
        r2.setRecursionDesired$1385ff();
        r7 = r11.random;
        r7 = r7.nextInt();
        r2.setId(r7);
        r0 = r2.toArray();
        r4 = new java.net.DatagramSocket;
        r4.<init>();
        r3 = new java.net.DatagramPacket;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r7 = r0.length;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r8 = java.net.InetAddress.getByName(r13);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r9 = 53;
        r3.<init>(r0, r7, r8, r9);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r7 = r11.timeout;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r4.setSoTimeout(r7);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r4.send(r3);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r3 = new java.net.DatagramPacket;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r7 = r11.bufferSize;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r7 = new byte[r7];	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r8 = r11.bufferSize;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r3.<init>(r7, r8);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r4.receive(r3);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r7 = r3.getData();	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r1 = de.measite.minidns.DNSMessage.parse(r7);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r7 = r1.getId();	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r8 = r2.getId();	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        if (r7 == r8) goto L_0x006c;
    L_0x0068:
        r4.close();
        goto L_0x000a;
    L_0x006c:
        r7 = r1.getAnswers();	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r8 = r7.length;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
    L_0x0071:
        if (r6 >= r8) goto L_0x0084;
    L_0x0073:
        r9 = r7[r6];	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r9 = r9.isAnswer(r12);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        if (r9 == 0) goto L_0x0089;
    L_0x007b:
        r6 = r11.cache;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        if (r6 == 0) goto L_0x0084;
    L_0x007f:
        r6 = r11.cache;	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
        r6.put(r12, r1);	 Catch:{ Throwable -> 0x008c, all -> 0x00a1 }
    L_0x0084:
        r4.close();
        r5 = r1;
        goto L_0x000a;
    L_0x0089:
        r6 = r6 + 1;
        goto L_0x0071;
    L_0x008c:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x008e }
    L_0x008e:
        r6 = move-exception;
        r10 = r6;
        r6 = r5;
        r5 = r10;
    L_0x0092:
        if (r6 == 0) goto L_0x009d;
    L_0x0094:
        r4.close();	 Catch:{ Throwable -> 0x0098 }
    L_0x0097:
        throw r5;
    L_0x0098:
        r7 = move-exception;
        r6.addSuppressed(r7);
        goto L_0x0097;
    L_0x009d:
        r4.close();
        goto L_0x0097;
    L_0x00a1:
        r6 = move-exception;
        r10 = r6;
        r6 = r5;
        r5 = r10;
        goto L_0x0092;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.measite.minidns.Client.query$3d14a83a(de.measite.minidns.Question, java.lang.String):de.measite.minidns.DNSMessage");
    }

    public final DNSMessage query(Question q) {
        DNSMessage message = this.cache.get(q);
        if (message != null) {
            return message;
        }
        String[] findDNSByReflection = findDNSByReflection();
        if (findDNSByReflection != null) {
            LOGGER.fine("Got DNS servers via reflection: " + Arrays.toString(findDNSByReflection));
        } else {
            findDNSByReflection = findDNSByExec();
            if (findDNSByReflection != null) {
                LOGGER.fine("Got DNS servers via exec: " + Arrays.toString(findDNSByReflection));
            } else {
                LOGGER.fine("No DNS found? Using fallback [8.8.8.8, [2001:4860:4860::8888]]");
                findDNSByReflection = new String[]{"8.8.8.8", "[2001:4860:4860::8888]"};
            }
        }
        for (String dns : r3) {
            try {
                message = query$3d14a83a(q, dns);
                if (message == null) {
                    continue;
                } else if (message.getResponseCode() == RESPONSE_CODE.NO_ERROR) {
                    for (Record isAnswer : message.getAnswers()) {
                        if (isAnswer.isAnswer(q)) {
                            return message;
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            } catch (IOException ioe) {
                LOGGER.log(Level.FINE, "IOException in query", ioe);
            }
        }
        return null;
    }

    private static String[] findDNSByExec() {
        try {
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream()));
            HashSet<String> server = new HashSet(6);
            while (true) {
                String line = lnr.readLine();
                if (line == null) {
                    break;
                }
                int split = line.indexOf("]: [");
                if (split != -1) {
                    String property = line.substring(1, split);
                    String value = line.substring(split + 4, line.length() - 1);
                    if (property.endsWith(".dns") || property.endsWith(".dns1") || property.endsWith(".dns2") || property.endsWith(".dns3") || property.endsWith(".dns4")) {
                        InetAddress ip = InetAddress.getByName(value);
                        if (ip != null) {
                            value = ip.getHostAddress();
                            if (!(value == null || value.length() == 0)) {
                                server.add(value);
                            }
                        }
                    }
                }
            }
            if (server.size() > 0) {
                return (String[]) server.toArray(new String[server.size()]);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Exception in findDNSByExec", e);
        }
        return null;
    }

    private static String[] findDNSByReflection() {
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            ArrayList<String> servers = new ArrayList(5);
            String[] strArr = new String[]{"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
            for (int i = 0; i < 4; i++) {
                String value = (String) method.invoke(null, new Object[]{strArr[i]});
                if (!(value == null || value.length() == 0 || servers.contains(value))) {
                    InetAddress ip = InetAddress.getByName(value);
                    if (ip != null) {
                        value = ip.getHostAddress();
                        if (!(value == null || value.length() == 0 || servers.contains(value))) {
                            servers.add(value);
                        }
                    }
                }
            }
            if (servers.size() > 0) {
                return (String[]) servers.toArray(new String[servers.size()]);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception in findDNSByReflection", e);
        }
        return null;
    }
}
