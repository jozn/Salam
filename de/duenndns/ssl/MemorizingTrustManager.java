package de.duenndns.ssl;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MemorizingTrustManager implements X509TrustManager {
    static String KEYSTORE_DIR;
    static String KEYSTORE_FILE;
    private static final Logger LOGGER;
    private static int decisionId;
    private static SparseArray<MTMDecision> openDecisions;
    private KeyStore appKeyStore;
    private X509TrustManager appTrustManager;
    private X509TrustManager defaultTrustManager;
    private File keyStoreFile;
    Context master;
    Handler masterHandler;
    NotificationManager notificationManager;

    class MemorizingHostnameVerifier implements HostnameVerifier {
        private HostnameVerifier defaultVerifier;

        public MemorizingHostnameVerifier(HostnameVerifier wrapped) {
            this.defaultVerifier = wrapped;
        }

        public final boolean verify(String hostname, SSLSession session) {
            MemorizingTrustManager.LOGGER.log(Level.FINE, "hostname verifier for " + hostname + ", trying default verifier first");
            if (this.defaultVerifier.verify(hostname, session)) {
                MemorizingTrustManager.LOGGER.log(Level.FINE, "default verifier accepted " + hostname);
                return true;
            }
            try {
                X509Certificate cert = session.getPeerCertificates()[0];
                if (cert.equals(MemorizingTrustManager.this.appKeyStore.getCertificate(hostname.toLowerCase(Locale.US)))) {
                    MemorizingTrustManager.LOGGER.log(Level.FINE, "certificate for " + hostname + " is in our keystore. accepting.");
                    return true;
                }
                MemorizingTrustManager.LOGGER.log(Level.FINE, "server " + hostname + " provided wrong certificate, asking user.");
                return MemorizingTrustManager.this.interactHostname(cert, hostname);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static {
        LOGGER = Logger.getLogger(MemorizingTrustManager.class.getName());
        KEYSTORE_DIR = "KeyStore";
        KEYSTORE_FILE = "KeyStore.bks";
        decisionId = 0;
        openDecisions = new SparseArray();
    }

    public MemorizingTrustManager(Context m) {
        Application m2;
        this.master = m;
        this.masterHandler = new Handler(m.getMainLooper());
        this.notificationManager = (NotificationManager) this.master.getSystemService("notification");
        if (m instanceof Application) {
            m2 = (Application) m;
        } else if (m instanceof Service) {
            m2 = ((Service) m).getApplication();
        } else if (m instanceof Activity) {
            m2 = ((Activity) m).getApplication();
        } else {
            throw new ClassCastException("MemorizingTrustManager context must be either Activity or Service!");
        }
        this.keyStoreFile = new File(m2.getDir(KEYSTORE_DIR, 0) + File.separator + KEYSTORE_FILE);
        this.appKeyStore = loadAppKeyStore();
        this.appTrustManager = getTrustManager(this.appKeyStore);
        this.defaultTrustManager = getTrustManager(null);
    }

    private static X509TrustManager getTrustManager(KeyStore ks) {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);
            for (TrustManager t : tmf.getTrustManagers()) {
                if (t instanceof X509TrustManager) {
                    return (X509TrustManager) t;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getTrustManager(" + ks + ")", e);
        }
        return null;
    }

    private KeyStore loadAppKeyStore() {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            try {
                ks.load(null, null);
                ks.load(new FileInputStream(this.keyStoreFile), "MTM".toCharArray());
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.INFO, "getAppKeyStore(" + this.keyStoreFile + ") - file does not exist");
            } catch (Exception e2) {
                LOGGER.log(Level.SEVERE, "getAppKeyStore(" + this.keyStoreFile + ")", e2);
            }
        } catch (KeyStoreException e3) {
            LOGGER.log(Level.SEVERE, "getAppKeyStore()", e3);
        }
        return ks;
    }

    private void storeCert(String alias, Certificate cert) {
        Throwable e;
        try {
            this.appKeyStore.setCertificateEntry(alias, cert);
            this.appTrustManager = getTrustManager(this.appKeyStore);
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(this.keyStoreFile);
                try {
                    this.appKeyStore.store(fileOutputStream, "MTM".toCharArray());
                    try {
                        fileOutputStream.close();
                    } catch (Throwable e2) {
                        LOGGER.log(Level.SEVERE, "storeCert(" + this.keyStoreFile + ")", e2);
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    try {
                        LOGGER.log(Level.SEVERE, "storeCert(" + this.keyStoreFile + ")", e2);
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable e22) {
                                LOGGER.log(Level.SEVERE, "storeCert(" + this.keyStoreFile + ")", e22);
                            }
                        }
                    } catch (Throwable th) {
                        e22 = th;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable e4) {
                                LOGGER.log(Level.SEVERE, "storeCert(" + this.keyStoreFile + ")", e4);
                            }
                        }
                        throw e22;
                    }
                }
            } catch (Exception e5) {
                e22 = e5;
                fileOutputStream = null;
                LOGGER.log(Level.SEVERE, "storeCert(" + this.keyStoreFile + ")", e22);
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Throwable th2) {
                e22 = th2;
                fileOutputStream = null;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw e22;
            }
        } catch (KeyStoreException e6) {
            LOGGER.log(Level.SEVERE, "storeCert(" + cert + ")", e6);
        }
    }

    private boolean isCertKnown(X509Certificate cert) {
        try {
            return this.appKeyStore.getCertificateAlias(cert) != null;
        } catch (KeyStoreException e) {
            return false;
        }
    }

    private static boolean isExpiredException(Throwable e) {
        while (!(e instanceof CertificateExpiredException)) {
            e = e.getCause();
            if (e == null) {
                return false;
            }
        }
        return true;
    }

    private void checkCertTrusted(X509Certificate[] chain, String authType, boolean isServer) throws CertificateException {
        LOGGER.log(Level.FINE, "checkCertTrusted(" + chain + ", " + authType + ", " + isServer + ")");
        try {
            LOGGER.log(Level.FINE, "checkCertTrusted: trying appTrustManager");
            if (isServer) {
                this.appTrustManager.checkServerTrusted(chain, authType);
            } else {
                this.appTrustManager.checkClientTrusted(chain, authType);
            }
        } catch (CertificateException ae) {
            LOGGER.log(Level.FINER, "checkCertTrusted: appTrustManager failed", ae);
            if (isExpiredException(ae)) {
                LOGGER.log(Level.INFO, "checkCertTrusted: accepting expired certificate from keystore");
            } else if (isCertKnown(chain[0])) {
                LOGGER.log(Level.INFO, "checkCertTrusted: accepting cert already stored in keystore");
            } else if (this.defaultTrustManager == null) {
                throw ae;
            } else {
                LOGGER.log(Level.FINE, "checkCertTrusted: trying defaultTrustManager");
                if (isServer) {
                    this.defaultTrustManager.checkServerTrusted(chain, authType);
                } else {
                    this.defaultTrustManager.checkClientTrusted(chain, authType);
                }
            }
        } catch (CertificateException e) {
            LOGGER.log(Level.FINER, "checkCertTrusted: defaultTrustManager failed", e);
            Certificate certificate = chain[0];
            storeCert(certificate.getSubjectDN().toString(), certificate);
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkCertTrusted(chain, authType, false);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkCertTrusted(chain, authType, true);
    }

    public X509Certificate[] getAcceptedIssuers() {
        LOGGER.log(Level.FINE, "getAcceptedIssuers()");
        return this.defaultTrustManager.getAcceptedIssuers();
    }

    final boolean interactHostname(X509Certificate cert, String hostname) {
        storeCert(hostname, cert);
        return true;
    }

    protected static void interactResult(int decisionId, int choice) {
        synchronized (openDecisions) {
            MTMDecision d = (MTMDecision) openDecisions.get(decisionId);
            openDecisions.remove(decisionId);
        }
        if (d == null) {
            LOGGER.log(Level.SEVERE, "interactResult: aborting due to stale decision reference!");
            return;
        }
        synchronized (d) {
            d.state = choice;
            d.notify();
        }
    }
}
