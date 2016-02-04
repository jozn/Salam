package org.jivesoftware.smack.util.dns.minidns;

import de.measite.minidns.Client;
import de.measite.minidns.DNSCache;
import de.measite.minidns.DNSMessage;
import de.measite.minidns.Question;
import de.measite.minidns.Record;
import de.measite.minidns.Record.CLASS;
import de.measite.minidns.Record.TYPE;
import de.measite.minidns.record.SRV;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smack.initializer.SmackAndOsgiInitializer;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.dns.DNSResolver;
import org.jivesoftware.smack.util.dns.SRVRecord;
import org.jxmpp.util.cache.ExpirationCache;

public class MiniDnsResolver extends SmackAndOsgiInitializer implements DNSResolver {
    private static final ExpirationCache<Question, DNSMessage> cache;
    private static final MiniDnsResolver instance;
    private final Client client;

    /* renamed from: org.jivesoftware.smack.util.dns.minidns.MiniDnsResolver.1 */
    class C12951 implements DNSCache {
        C12951() {
        }

        public final DNSMessage get(Question question) {
            return (DNSMessage) MiniDnsResolver.cache.get(question);
        }

        public final void put(Question question, DNSMessage message) {
            long expirationTime = 86400000;
            for (Record record : message.getAnswers()) {
                if (record.isAnswer(question)) {
                    expirationTime = record.getTtl();
                    break;
                }
            }
            MiniDnsResolver.cache.put(question, message, expirationTime);
        }
    }

    static {
        instance = new MiniDnsResolver();
        cache = new ExpirationCache(10, 86400000);
    }

    public MiniDnsResolver() {
        this.client = new Client(new C12951());
    }

    public final List<SRVRecord> lookupSRVRecords(String name) {
        List<SRVRecord> res = new LinkedList();
        for (Record payload : this.client.query(new Question(name, TYPE.SRV, CLASS.IN)).getAnswers()) {
            SRV srv = (SRV) payload.getPayload();
            res.add(new SRVRecord(srv.getName(), srv.getPort(), srv.getPriority(), srv.getWeight()));
        }
        return res;
    }

    public final List<Exception> initialize() {
        DNSUtil.setDNSResolver(instance);
        return null;
    }
}
