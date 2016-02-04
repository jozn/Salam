package org.jivesoftware.smack.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.util.dns.DNSResolver;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jivesoftware.smack.util.dns.SRVRecord;

public class DNSUtil {
    private static final Logger LOGGER;
    private static DNSResolver dnsResolver;
    private static StringTransformer idnaTransformer;

    /* renamed from: org.jivesoftware.smack.util.DNSUtil.1 */
    static class C12911 implements StringTransformer {
        C12911() {
        }

        public final String transform(String string) {
            return string;
        }
    }

    /* renamed from: org.jivesoftware.smack.util.DNSUtil.2 */
    static /* synthetic */ class C12922 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$util$DNSUtil$DomainType;

        static {
            $SwitchMap$org$jivesoftware$smack$util$DNSUtil$DomainType = new int[DomainType.values$4c1493ac().length];
            try {
                $SwitchMap$org$jivesoftware$smack$util$DNSUtil$DomainType[DomainType.Server$63e69326 - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$util$DNSUtil$DomainType[DomainType.Client$63e69326 - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private enum DomainType {
        ;

        public static int[] values$4c1493ac() {
            return (int[]) $VALUES$72032b35.clone();
        }

        static {
            Server$63e69326 = 1;
            Client$63e69326 = 2;
            $VALUES$72032b35 = new int[]{Server$63e69326, Client$63e69326};
        }
    }

    static {
        LOGGER = Logger.getLogger(DNSUtil.class.getName());
        dnsResolver = null;
        idnaTransformer = new C12911();
    }

    public static void setDNSResolver(DNSResolver resolver) {
        dnsResolver = resolver;
    }

    public static void setIdnaTransformer(StringTransformer idnaTransformer) {
        idnaTransformer = idnaTransformer;
    }

    public static List<HostAddress> resolveXMPPDomain(String domain) throws Exception {
        domain = idnaTransformer.transform(domain);
        if (dnsResolver != null) {
            return resolveDomain$303d10ce(domain, DomainType.Client$63e69326);
        }
        LOGGER.warning("No DNS Resolver active in Smack, will be unable to perform DNS SRV lookups");
        List<HostAddress> addresses = new ArrayList(1);
        addresses.add(new HostAddress(domain, 5222));
        return addresses;
    }

    private static List<HostAddress> resolveDomain$303d10ce(String domain, int domainType) throws Exception {
        String srvDomain;
        List<HostAddress> addresses = new ArrayList();
        switch (C12922.$SwitchMap$org$jivesoftware$smack$util$DNSUtil$DomainType[domainType - 1]) {
            case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                srvDomain = "_xmpp-server._tcp." + domain;
                break;
            case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                srvDomain = "_xmpp-client._tcp." + domain;
                break;
            default:
                throw new AssertionError();
        }
        try {
            List<SRVRecord> srvRecords = dnsResolver.lookupSRVRecords(srvDomain);
            if (LOGGER.isLoggable(Level.FINE)) {
                String logMessage = "Resolved SRV RR for " + srvDomain + ":";
                for (SRVRecord r : srvRecords) {
                    logMessage = logMessage + " " + r;
                }
                LOGGER.fine(logMessage);
            }
            addresses.addAll(sortSRVRecords(srvRecords));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception while resolving SRV records for " + domain, e);
        }
        addresses.add(new HostAddress(domain));
        return addresses;
    }

    private static List<HostAddress> sortSRVRecords(List<SRVRecord> records) {
        if (records.size() == 1) {
            if (((SRVRecord) records.get(0)).fqdn.equals(".")) {
                return Collections.emptyList();
            }
        }
        Collections.sort(records);
        SortedMap<Integer, List<SRVRecord>> buckets = new TreeMap();
        for (SRVRecord r : records) {
            Integer priority = Integer.valueOf(r.priority);
            List<SRVRecord> bucket = (List) buckets.get(priority);
            if (bucket == null) {
                bucket = new LinkedList();
                buckets.put(priority, bucket);
            }
            bucket.add(r);
        }
        List<HostAddress> res = new ArrayList(records.size());
        for (Integer priority2 : buckets.keySet()) {
            bucket = (List) buckets.get(priority2);
            while (true) {
                int bucketSize = bucket.size();
                if (bucketSize > 0) {
                    int selectedPos;
                    int[] totals = new int[bucket.size()];
                    int running_total = 0;
                    int count = 0;
                    int zeroWeight = 1;
                    for (SRVRecord sRVRecord : bucket) {
                        if (sRVRecord.weight > 0) {
                            zeroWeight = 0;
                        }
                    }
                    for (SRVRecord r2 : bucket) {
                        running_total += r2.weight + zeroWeight;
                        totals[count] = running_total;
                        count++;
                    }
                    if (running_total == 0) {
                        selectedPos = (int) (Math.random() * ((double) bucketSize));
                    } else {
                        int[] iArr = totals;
                        selectedPos = bisect(iArr, Math.random() * ((double) running_total));
                    }
                    res.add((SRVRecord) bucket.remove(selectedPos));
                }
            }
        }
        return res;
    }

    private static int bisect(int[] array, double value) {
        int pos = 0;
        int[] arr$ = array;
        int len$ = array.length;
        int i$ = 0;
        while (i$ < len$ && value >= ((double) arr$[i$])) {
            pos++;
            i$++;
        }
        return pos;
    }
}
