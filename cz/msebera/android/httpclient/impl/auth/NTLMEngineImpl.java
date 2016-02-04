package cz.msebera.android.httpclient.impl.auth;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.extras.Base64;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharsetUtils;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

final class NTLMEngineImpl implements NTLMEngine {
    private static final Charset DEFAULT_CHARSET;
    private static final SecureRandom RND_GEN;
    private static final byte[] SIGNATURE;
    private static final Type1Message TYPE_1_MESSAGE;
    private static final Charset UNICODE_LITTLE_UNMARKED;

    protected static class CipherGen {
        protected final byte[] challenge;
        protected byte[] clientChallenge;
        protected byte[] clientChallenge2;
        protected final String domain;
        protected byte[] lanManagerSessionKey;
        protected byte[] lm2SessionResponse;
        protected byte[] lmHash;
        protected byte[] lmResponse;
        protected byte[] lmUserSessionKey;
        protected byte[] lmv2Hash;
        protected byte[] lmv2Response;
        protected byte[] ntlm2SessionResponse;
        protected byte[] ntlm2SessionResponseUserSessionKey;
        protected byte[] ntlmHash;
        protected byte[] ntlmResponse;
        protected byte[] ntlmUserSessionKey;
        protected byte[] ntlmv2Blob;
        protected byte[] ntlmv2Hash;
        protected byte[] ntlmv2Response;
        protected byte[] ntlmv2UserSessionKey;
        protected final String password;
        protected byte[] secondaryKey;
        protected final String target;
        protected final byte[] targetInformation;
        protected byte[] timestamp;
        protected final String user;

        private CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation) {
            this.lmHash = null;
            this.lmResponse = null;
            this.ntlmHash = null;
            this.ntlmResponse = null;
            this.ntlmv2Hash = null;
            this.lmv2Hash = null;
            this.lmv2Response = null;
            this.ntlmv2Blob = null;
            this.ntlmv2Response = null;
            this.ntlm2SessionResponse = null;
            this.lm2SessionResponse = null;
            this.lmUserSessionKey = null;
            this.ntlmUserSessionKey = null;
            this.ntlmv2UserSessionKey = null;
            this.ntlm2SessionResponseUserSessionKey = null;
            this.lanManagerSessionKey = null;
            this.domain = domain;
            this.target = target;
            this.user = user;
            this.password = password;
            this.challenge = challenge;
            this.targetInformation = targetInformation;
            this.clientChallenge = null;
            this.clientChallenge2 = null;
            this.secondaryKey = null;
            this.timestamp = null;
        }

        public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation, byte b) {
            this(domain, user, password, challenge, target, targetInformation);
        }

        private byte[] getClientChallenge() throws NTLMEngineException {
            if (this.clientChallenge == null) {
                this.clientChallenge = NTLMEngineImpl.makeRandomChallenge();
            }
            return this.clientChallenge;
        }

        public final byte[] getSecondaryKey() throws NTLMEngineException {
            if (this.secondaryKey == null) {
                this.secondaryKey = NTLMEngineImpl.makeSecondaryKey();
            }
            return this.secondaryKey;
        }

        private byte[] getLMHash() throws NTLMEngineException {
            if (this.lmHash == null) {
                this.lmHash = NTLMEngineImpl.lmHash(this.password);
            }
            return this.lmHash;
        }

        public final byte[] getLMResponse() throws NTLMEngineException {
            if (this.lmResponse == null) {
                this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge);
            }
            return this.lmResponse;
        }

        private byte[] getNTLMHash() throws NTLMEngineException {
            if (this.ntlmHash == null) {
                this.ntlmHash = NTLMEngineImpl.access$400(this.password);
            }
            return this.ntlmHash;
        }

        public final byte[] getNTLMResponse() throws NTLMEngineException {
            if (this.ntlmResponse == null) {
                this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge);
            }
            return this.ntlmResponse;
        }

        private byte[] getNTLMv2Hash() throws NTLMEngineException {
            if (this.ntlmv2Hash == null) {
                this.ntlmv2Hash = NTLMEngineImpl.access$600(this.domain, this.user, getNTLMHash());
            }
            return this.ntlmv2Hash;
        }

        public final byte[] getNTLMv2Response() throws NTLMEngineException {
            if (this.ntlmv2Response == null) {
                byte[] nTLMv2Hash = getNTLMv2Hash();
                byte[] bArr = this.challenge;
                if (this.ntlmv2Blob == null) {
                    if (this.clientChallenge2 == null) {
                        this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge();
                    }
                    byte[] bArr2 = this.clientChallenge2;
                    byte[] bArr3 = this.targetInformation;
                    if (this.timestamp == null) {
                        long currentTimeMillis = (System.currentTimeMillis() + 11644473600000L) * 10000;
                        this.timestamp = new byte[8];
                        for (int i = 0; i < 8; i++) {
                            this.timestamp[i] = (byte) ((int) currentTimeMillis);
                            currentTimeMillis >>>= 8;
                        }
                    }
                    this.ntlmv2Blob = NTLMEngineImpl.access$700(bArr2, bArr3, this.timestamp);
                }
                this.ntlmv2Response = NTLMEngineImpl.access$800(nTLMv2Hash, bArr, this.ntlmv2Blob);
            }
            return this.ntlmv2Response;
        }

        public final byte[] getLMv2Response() throws NTLMEngineException {
            if (this.lmv2Response == null) {
                if (this.lmv2Hash == null) {
                    this.lmv2Hash = NTLMEngineImpl.access$500(this.domain, this.user, getNTLMHash());
                }
                this.lmv2Response = NTLMEngineImpl.access$800(this.lmv2Hash, this.challenge, getClientChallenge());
            }
            return this.lmv2Response;
        }

        public final byte[] getNTLM2SessionResponse() throws NTLMEngineException {
            if (this.ntlm2SessionResponse == null) {
                this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge());
            }
            return this.ntlm2SessionResponse;
        }

        public final byte[] getLM2SessionResponse() throws NTLMEngineException {
            if (this.lm2SessionResponse == null) {
                byte[] clntChallenge = getClientChallenge();
                this.lm2SessionResponse = new byte[24];
                System.arraycopy(clntChallenge, 0, this.lm2SessionResponse, 0, clntChallenge.length);
                Arrays.fill(this.lm2SessionResponse, clntChallenge.length, this.lm2SessionResponse.length, (byte) 0);
            }
            return this.lm2SessionResponse;
        }

        public final byte[] getLMUserSessionKey() throws NTLMEngineException {
            if (this.lmUserSessionKey == null) {
                this.lmUserSessionKey = new byte[16];
                System.arraycopy(getLMHash(), 0, this.lmUserSessionKey, 0, 8);
                Arrays.fill(this.lmUserSessionKey, 8, 16, (byte) 0);
            }
            return this.lmUserSessionKey;
        }

        public final byte[] getNTLMUserSessionKey() throws NTLMEngineException {
            if (this.ntlmUserSessionKey == null) {
                MD4 md4 = new MD4();
                md4.update(getNTLMHash());
                this.ntlmUserSessionKey = md4.getOutput();
            }
            return this.ntlmUserSessionKey;
        }

        public final byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
            if (this.ntlmv2UserSessionKey == null) {
                byte[] ntlmv2hash = getNTLMv2Hash();
                byte[] truncatedResponse = new byte[16];
                System.arraycopy(getNTLMv2Response(), 0, truncatedResponse, 0, 16);
                this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(truncatedResponse, ntlmv2hash);
            }
            return this.ntlmv2UserSessionKey;
        }

        public final byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
            if (this.ntlm2SessionResponseUserSessionKey == null) {
                byte[] ntlm2SessionResponseNonce = getLM2SessionResponse();
                byte[] sessionNonce = new byte[(this.challenge.length + ntlm2SessionResponseNonce.length)];
                System.arraycopy(this.challenge, 0, sessionNonce, 0, this.challenge.length);
                System.arraycopy(ntlm2SessionResponseNonce, 0, sessionNonce, this.challenge.length, ntlm2SessionResponseNonce.length);
                this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(sessionNonce, getNTLMUserSessionKey());
            }
            return this.ntlm2SessionResponseUserSessionKey;
        }

        public final byte[] getLanManagerSessionKey() throws NTLMEngineException {
            if (this.lanManagerSessionKey == null) {
                try {
                    byte[] keyBytes = new byte[14];
                    System.arraycopy(getLMHash(), 0, keyBytes, 0, 8);
                    Arrays.fill(keyBytes, 8, 14, (byte) -67);
                    Key lowKey = NTLMEngineImpl.createDESKey(keyBytes, 0);
                    Key highKey = NTLMEngineImpl.createDESKey(keyBytes, 7);
                    byte[] truncatedResponse = new byte[8];
                    System.arraycopy(getLMResponse(), 0, truncatedResponse, 0, 8);
                    Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
                    des.init(1, lowKey);
                    byte[] lowPart = des.doFinal(truncatedResponse);
                    des = Cipher.getInstance("DES/ECB/NoPadding");
                    des.init(1, highKey);
                    byte[] highPart = des.doFinal(truncatedResponse);
                    this.lanManagerSessionKey = new byte[16];
                    System.arraycopy(lowPart, 0, this.lanManagerSessionKey, 0, lowPart.length);
                    System.arraycopy(highPart, 0, this.lanManagerSessionKey, lowPart.length, highPart.length);
                } catch (Exception e) {
                    throw new NTLMEngineException(e.getMessage(), e);
                }
            }
            return this.lanManagerSessionKey;
        }
    }

    static class HMACMD5 {
        protected byte[] ipad;
        protected MessageDigest md5;
        protected byte[] opad;

        HMACMD5(byte[] input) throws NTLMEngineException {
            byte[] key = input;
            try {
                this.md5 = MessageDigest.getInstance("MD5");
                this.ipad = new byte[64];
                this.opad = new byte[64];
                int keyLength = key.length;
                if (keyLength > 64) {
                    this.md5.update(key);
                    key = this.md5.digest();
                    keyLength = key.length;
                }
                int i = 0;
                while (i < keyLength) {
                    this.ipad[i] = (byte) (key[i] ^ 54);
                    this.opad[i] = (byte) (key[i] ^ 92);
                    i++;
                }
                while (i < 64) {
                    this.ipad[i] = (byte) 54;
                    this.opad[i] = (byte) 92;
                    i++;
                }
                this.md5.reset();
                this.md5.update(this.ipad);
            } catch (Exception ex) {
                throw new NTLMEngineException("Error getting md5 message digest implementation: " + ex.getMessage(), ex);
            }
        }

        final byte[] getOutput() {
            byte[] digest = this.md5.digest();
            this.md5.update(this.opad);
            return this.md5.digest(digest);
        }

        final void update(byte[] input) {
            this.md5.update(input);
        }
    }

    static class MD4 {
        protected int f29A;
        protected int f30B;
        protected int f31C;
        protected int f32D;
        protected long count;
        protected byte[] dataBuffer;

        MD4() {
            this.f29A = 1732584193;
            this.f30B = -271733879;
            this.f31C = -1732584194;
            this.f32D = 271733878;
            this.count = 0;
            this.dataBuffer = new byte[64];
        }

        final void update(byte[] input) {
            int curBufferPos = (int) (this.count & 63);
            int inputIndex = 0;
            while ((input.length - inputIndex) + curBufferPos >= this.dataBuffer.length) {
                int i;
                int transferAmt = this.dataBuffer.length - curBufferPos;
                System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
                this.count += (long) transferAmt;
                curBufferPos = 0;
                inputIndex += transferAmt;
                int[] iArr = new int[16];
                for (i = 0; i < 16; i++) {
                    iArr[i] = (((this.dataBuffer[i * 4] & MotionEventCompat.ACTION_MASK) + ((this.dataBuffer[(i * 4) + 1] & MotionEventCompat.ACTION_MASK) << 8)) + ((this.dataBuffer[(i * 4) + 2] & MotionEventCompat.ACTION_MASK) << 16)) + ((this.dataBuffer[(i * 4) + 3] & MotionEventCompat.ACTION_MASK) << 24);
                }
                i = this.f29A;
                int i2 = this.f30B;
                int i3 = this.f31C;
                int i4 = this.f32D;
                this.f29A = NTLMEngineImpl.rotintlft((this.f29A + NTLMEngineImpl.m44F(this.f30B, this.f31C, this.f32D)) + iArr[0], 3);
                this.f32D = NTLMEngineImpl.rotintlft((this.f32D + NTLMEngineImpl.m44F(this.f29A, this.f30B, this.f31C)) + iArr[1], 7);
                this.f31C = NTLMEngineImpl.rotintlft((this.f31C + NTLMEngineImpl.m44F(this.f32D, this.f29A, this.f30B)) + iArr[2], 11);
                this.f30B = NTLMEngineImpl.rotintlft((this.f30B + NTLMEngineImpl.m44F(this.f31C, this.f32D, this.f29A)) + iArr[3], 19);
                this.f29A = NTLMEngineImpl.rotintlft((this.f29A + NTLMEngineImpl.m44F(this.f30B, this.f31C, this.f32D)) + iArr[4], 3);
                this.f32D = NTLMEngineImpl.rotintlft((this.f32D + NTLMEngineImpl.m44F(this.f29A, this.f30B, this.f31C)) + iArr[5], 7);
                this.f31C = NTLMEngineImpl.rotintlft((this.f31C + NTLMEngineImpl.m44F(this.f32D, this.f29A, this.f30B)) + iArr[6], 11);
                this.f30B = NTLMEngineImpl.rotintlft((this.f30B + NTLMEngineImpl.m44F(this.f31C, this.f32D, this.f29A)) + iArr[7], 19);
                this.f29A = NTLMEngineImpl.rotintlft((this.f29A + NTLMEngineImpl.m44F(this.f30B, this.f31C, this.f32D)) + iArr[8], 3);
                this.f32D = NTLMEngineImpl.rotintlft((this.f32D + NTLMEngineImpl.m44F(this.f29A, this.f30B, this.f31C)) + iArr[9], 7);
                this.f31C = NTLMEngineImpl.rotintlft((this.f31C + NTLMEngineImpl.m44F(this.f32D, this.f29A, this.f30B)) + iArr[10], 11);
                this.f30B = NTLMEngineImpl.rotintlft((this.f30B + NTLMEngineImpl.m44F(this.f31C, this.f32D, this.f29A)) + iArr[11], 19);
                this.f29A = NTLMEngineImpl.rotintlft((this.f29A + NTLMEngineImpl.m44F(this.f30B, this.f31C, this.f32D)) + iArr[12], 3);
                this.f32D = NTLMEngineImpl.rotintlft((this.f32D + NTLMEngineImpl.m44F(this.f29A, this.f30B, this.f31C)) + iArr[13], 7);
                this.f31C = NTLMEngineImpl.rotintlft((this.f31C + NTLMEngineImpl.m44F(this.f32D, this.f29A, this.f30B)) + iArr[14], 11);
                this.f30B = NTLMEngineImpl.rotintlft((this.f30B + NTLMEngineImpl.m44F(this.f31C, this.f32D, this.f29A)) + iArr[15], 19);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m45G(this.f30B, this.f31C, this.f32D)) + iArr[0]) + 1518500249, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m45G(this.f29A, this.f30B, this.f31C)) + iArr[4]) + 1518500249, 5);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m45G(this.f32D, this.f29A, this.f30B)) + iArr[8]) + 1518500249, 9);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m45G(this.f31C, this.f32D, this.f29A)) + iArr[12]) + 1518500249, 13);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m45G(this.f30B, this.f31C, this.f32D)) + iArr[1]) + 1518500249, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m45G(this.f29A, this.f30B, this.f31C)) + iArr[5]) + 1518500249, 5);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m45G(this.f32D, this.f29A, this.f30B)) + iArr[9]) + 1518500249, 9);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m45G(this.f31C, this.f32D, this.f29A)) + iArr[13]) + 1518500249, 13);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m45G(this.f30B, this.f31C, this.f32D)) + iArr[2]) + 1518500249, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m45G(this.f29A, this.f30B, this.f31C)) + iArr[6]) + 1518500249, 5);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m45G(this.f32D, this.f29A, this.f30B)) + iArr[10]) + 1518500249, 9);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m45G(this.f31C, this.f32D, this.f29A)) + iArr[14]) + 1518500249, 13);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m45G(this.f30B, this.f31C, this.f32D)) + iArr[3]) + 1518500249, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m45G(this.f29A, this.f30B, this.f31C)) + iArr[7]) + 1518500249, 5);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m45G(this.f32D, this.f29A, this.f30B)) + iArr[11]) + 1518500249, 9);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m45G(this.f31C, this.f32D, this.f29A)) + iArr[15]) + 1518500249, 13);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m46H(this.f30B, this.f31C, this.f32D)) + iArr[0]) + 1859775393, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m46H(this.f29A, this.f30B, this.f31C)) + iArr[8]) + 1859775393, 9);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m46H(this.f32D, this.f29A, this.f30B)) + iArr[4]) + 1859775393, 11);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m46H(this.f31C, this.f32D, this.f29A)) + iArr[12]) + 1859775393, 15);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m46H(this.f30B, this.f31C, this.f32D)) + iArr[2]) + 1859775393, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m46H(this.f29A, this.f30B, this.f31C)) + iArr[10]) + 1859775393, 9);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m46H(this.f32D, this.f29A, this.f30B)) + iArr[6]) + 1859775393, 11);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m46H(this.f31C, this.f32D, this.f29A)) + iArr[14]) + 1859775393, 15);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m46H(this.f30B, this.f31C, this.f32D)) + iArr[1]) + 1859775393, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m46H(this.f29A, this.f30B, this.f31C)) + iArr[9]) + 1859775393, 9);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m46H(this.f32D, this.f29A, this.f30B)) + iArr[5]) + 1859775393, 11);
                this.f30B = NTLMEngineImpl.rotintlft(((this.f30B + NTLMEngineImpl.m46H(this.f31C, this.f32D, this.f29A)) + iArr[13]) + 1859775393, 15);
                this.f29A = NTLMEngineImpl.rotintlft(((this.f29A + NTLMEngineImpl.m46H(this.f30B, this.f31C, this.f32D)) + iArr[3]) + 1859775393, 3);
                this.f32D = NTLMEngineImpl.rotintlft(((this.f32D + NTLMEngineImpl.m46H(this.f29A, this.f30B, this.f31C)) + iArr[11]) + 1859775393, 9);
                this.f31C = NTLMEngineImpl.rotintlft(((this.f31C + NTLMEngineImpl.m46H(this.f32D, this.f29A, this.f30B)) + iArr[7]) + 1859775393, 11);
                this.f30B = NTLMEngineImpl.rotintlft((iArr[15] + (this.f30B + NTLMEngineImpl.m46H(this.f31C, this.f32D, this.f29A))) + 1859775393, 15);
                this.f29A = i + this.f29A;
                this.f30B += i2;
                this.f31C += i3;
                this.f32D += i4;
            }
            if (inputIndex < input.length) {
                transferAmt = input.length - inputIndex;
                System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
                this.count += (long) transferAmt;
            }
        }

        final byte[] getOutput() {
            int bufferIndex = (int) (this.count & 63);
            int padLen = bufferIndex < 56 ? 56 - bufferIndex : 120 - bufferIndex;
            byte[] postBytes = new byte[(padLen + 8)];
            postBytes[0] = Byte.MIN_VALUE;
            for (int i = 0; i < 8; i++) {
                postBytes[padLen + i] = (byte) ((int) ((this.count * 8) >>> (i * 8)));
            }
            update(postBytes);
            byte[] result = new byte[16];
            NTLMEngineImpl.writeULong(result, this.f29A, 0);
            NTLMEngineImpl.writeULong(result, this.f30B, 4);
            NTLMEngineImpl.writeULong(result, this.f31C, 8);
            NTLMEngineImpl.writeULong(result, this.f32D, 12);
            return result;
        }
    }

    static class NTLMMessage {
        int currentOutputPosition;
        byte[] messageContents;

        NTLMMessage() {
            this.messageContents = null;
            this.currentOutputPosition = 0;
        }

        NTLMMessage(String messageBody) throws NTLMEngineException {
            this.messageContents = null;
            this.currentOutputPosition = 0;
            this.messageContents = Base64.decode$1cf9d9ca(messageBody.getBytes(NTLMEngineImpl.DEFAULT_CHARSET));
            if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length) {
                throw new NTLMEngineException("NTLM message decoding error - packet too short");
            }
            for (int i = 0; i < NTLMEngineImpl.SIGNATURE.length; i++) {
                if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
                    throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
                }
            }
            int type = readULong(NTLMEngineImpl.SIGNATURE.length);
            if (type != 2) {
                throw new NTLMEngineException("NTLM type " + Integer.toString(2) + " message expected - instead got type " + Integer.toString(type));
            }
            this.currentOutputPosition = this.messageContents.length;
        }

        protected final int readULong(int position) throws NTLMEngineException {
            return NTLMEngineImpl.readULong(this.messageContents, position);
        }

        protected final byte[] readSecurityBuffer(int position) throws NTLMEngineException {
            return NTLMEngineImpl.access$1400(this.messageContents, position);
        }

        protected final void prepareResponse(int maxlength, int messageType) {
            this.messageContents = new byte[maxlength];
            this.currentOutputPosition = 0;
            addBytes(NTLMEngineImpl.SIGNATURE);
            addULong(messageType);
        }

        private void addByte(byte b) {
            this.messageContents[this.currentOutputPosition] = b;
            this.currentOutputPosition++;
        }

        protected final void addBytes(byte[] bytes) {
            if (bytes != null) {
                for (byte b : bytes) {
                    this.messageContents[this.currentOutputPosition] = b;
                    this.currentOutputPosition++;
                }
            }
        }

        protected final void addUShort(int value) {
            addByte((byte) (value & MotionEventCompat.ACTION_MASK));
            addByte((byte) ((value >> 8) & MotionEventCompat.ACTION_MASK));
        }

        protected final void addULong(int value) {
            addByte((byte) (value & MotionEventCompat.ACTION_MASK));
            addByte((byte) ((value >> 8) & MotionEventCompat.ACTION_MASK));
            addByte((byte) ((value >> 16) & MotionEventCompat.ACTION_MASK));
            addByte((byte) ((value >> 24) & MotionEventCompat.ACTION_MASK));
        }

        String getResponse() {
            byte[] resp;
            if (this.messageContents.length > this.currentOutputPosition) {
                byte[] tmp = new byte[this.currentOutputPosition];
                System.arraycopy(this.messageContents, 0, tmp, 0, this.currentOutputPosition);
                resp = tmp;
            } else {
                resp = this.messageContents;
            }
            Object encode$1cf9d9ca = Base64.encode$1cf9d9ca(resp);
            Args.notNull(encode$1cf9d9ca, "Input");
            int length = encode$1cf9d9ca.length;
            Args.notNull(encode$1cf9d9ca, "Input");
            return new String(encode$1cf9d9ca, 0, length, Consts.ASCII);
        }
    }

    static class Type1Message extends NTLMMessage {
        private final byte[] domainBytes;
        private final byte[] hostBytes;

        Type1Message() {
            this.hostBytes = null;
            this.domainBytes = null;
        }

        final String getResponse() {
            prepareResponse(40, 1);
            addULong(-1576500735);
            addUShort(0);
            addUShort(0);
            addULong(40);
            addUShort(0);
            addUShort(0);
            addULong(40);
            addUShort(261);
            addULong(2600);
            addUShort(3840);
            if (this.hostBytes != null) {
                addBytes(this.hostBytes);
            }
            if (this.domainBytes != null) {
                addBytes(this.domainBytes);
            }
            return super.getResponse();
        }
    }

    static class Type2Message extends NTLMMessage {
        protected byte[] challenge;
        protected int flags;
        protected String target;
        protected byte[] targetInfo;

        Type2Message(String message) throws NTLMEngineException {
            super(message);
            this.challenge = new byte[8];
            Object obj = this.challenge;
            if (this.messageContents.length < obj.length + 24) {
                throw new NTLMEngineException("NTLM: Message too short");
            }
            System.arraycopy(this.messageContents, 24, obj, 0, obj.length);
            this.flags = readULong(20);
            if ((this.flags & 1) == 0) {
                throw new NTLMEngineException("NTLM type 2 message indicates no support for Unicode. Flags are: " + Integer.toString(this.flags));
            }
            byte[] bytes;
            this.target = null;
            if (this.currentOutputPosition >= 20) {
                bytes = readSecurityBuffer(12);
                if (bytes.length != 0) {
                    try {
                        this.target = new String(bytes, "UnicodeLittleUnmarked");
                    } catch (UnsupportedEncodingException e) {
                        throw new NTLMEngineException(e.getMessage(), e);
                    }
                }
            }
            this.targetInfo = null;
            if (this.currentOutputPosition >= 48) {
                bytes = readSecurityBuffer(40);
                if (bytes.length != 0) {
                    this.targetInfo = bytes;
                }
            }
        }

        final byte[] getChallenge() {
            return this.challenge;
        }

        final String getTarget() {
            return this.target;
        }

        final byte[] getTargetInfo() {
            return this.targetInfo;
        }

        final int getFlags() {
            return this.flags;
        }
    }

    static class Type3Message extends NTLMMessage {
        protected byte[] domainBytes;
        protected byte[] hostBytes;
        protected byte[] lmResp;
        protected byte[] ntResp;
        protected byte[] sessionKey;
        protected int type2Flags;
        protected byte[] userBytes;

        Type3Message(String domain, String host, String user, String password, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
            byte[] userSessionKey;
            this.type2Flags = type2Flags;
            String unqualifiedHost = NTLMEngineImpl.stripDotSuffix(host);
            String unqualifiedDomain = NTLMEngineImpl.stripDotSuffix(domain);
            CipherGen gen = new CipherGen(unqualifiedDomain, user, password, nonce, target, targetInformation, (byte) 0);
            if ((GravityCompat.RELATIVE_LAYOUT_DIRECTION & type2Flags) != 0 && targetInformation != null && target != null) {
                try {
                    this.ntResp = gen.getNTLMv2Response();
                    this.lmResp = gen.getLMv2Response();
                    if ((type2Flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0) {
                        userSessionKey = gen.getLanManagerSessionKey();
                    } else {
                        userSessionKey = gen.getNTLMv2UserSessionKey();
                    }
                } catch (NTLMEngineException e) {
                    this.ntResp = new byte[0];
                    this.lmResp = gen.getLMResponse();
                    if ((type2Flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0) {
                        userSessionKey = gen.getLanManagerSessionKey();
                    } else {
                        userSessionKey = gen.getLMUserSessionKey();
                    }
                }
            } else if ((AccessibilityNodeInfoCompat.ACTION_COLLAPSE & type2Flags) != 0) {
                this.ntResp = gen.getNTLM2SessionResponse();
                this.lmResp = gen.getLM2SessionResponse();
                if ((type2Flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0) {
                    userSessionKey = gen.getLanManagerSessionKey();
                } else {
                    userSessionKey = gen.getNTLM2SessionResponseUserSessionKey();
                }
            } else {
                this.ntResp = gen.getNTLMResponse();
                this.lmResp = gen.getLMResponse();
                if ((type2Flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0) {
                    userSessionKey = gen.getLanManagerSessionKey();
                } else {
                    userSessionKey = gen.getNTLMUserSessionKey();
                }
            }
            if ((type2Flags & 16) == 0) {
                this.sessionKey = null;
            } else if ((1073741824 & type2Flags) != 0) {
                this.sessionKey = NTLMEngineImpl.RC4(gen.getSecondaryKey(), userSessionKey);
            } else {
                this.sessionKey = userSessionKey;
            }
            if (NTLMEngineImpl.UNICODE_LITTLE_UNMARKED == null) {
                throw new NTLMEngineException("Unicode not supported");
            }
            this.hostBytes = unqualifiedHost != null ? unqualifiedHost.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
            this.domainBytes = unqualifiedDomain != null ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
            this.userBytes = user.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED);
        }

        final String getResponse() {
            int domainLen;
            int hostLen;
            int sessionKeyLen;
            int ntRespLen = this.ntResp.length;
            int lmRespLen = this.lmResp.length;
            if (this.domainBytes != null) {
                domainLen = this.domainBytes.length;
            } else {
                domainLen = 0;
            }
            if (this.hostBytes != null) {
                hostLen = this.hostBytes.length;
            } else {
                hostLen = 0;
            }
            int userLen = this.userBytes.length;
            if (this.sessionKey != null) {
                sessionKeyLen = this.sessionKey.length;
            } else {
                sessionKeyLen = 0;
            }
            int ntRespOffset = lmRespLen + 72;
            int domainOffset = ntRespOffset + ntRespLen;
            int userOffset = domainOffset + domainLen;
            int hostOffset = userOffset + userLen;
            int sessionKeyOffset = hostOffset + hostLen;
            prepareResponse(sessionKeyOffset + sessionKeyLen, 3);
            addUShort(lmRespLen);
            addUShort(lmRespLen);
            addULong(72);
            addUShort(ntRespLen);
            addUShort(ntRespLen);
            addULong(ntRespOffset);
            addUShort(domainLen);
            addUShort(domainLen);
            addULong(domainOffset);
            addUShort(userLen);
            addUShort(userLen);
            addULong(userOffset);
            addUShort(hostLen);
            addUShort(hostLen);
            addULong(hostOffset);
            addUShort(sessionKeyLen);
            addUShort(sessionKeyLen);
            addULong(sessionKeyOffset);
            addULong(((((((((((((this.type2Flags & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) | (this.type2Flags & AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY)) | (this.type2Flags & AccessibilityNodeInfoCompat.ACTION_COLLAPSE)) | 33554432) | (this.type2Flags & AccessibilityNodeInfoCompat.ACTION_PASTE)) | (this.type2Flags & 32)) | (this.type2Flags & 16)) | (this.type2Flags & 536870912)) | (this.type2Flags & RtlSpacingHelper.UNDEFINED)) | (this.type2Flags & 1073741824)) | (this.type2Flags & GravityCompat.RELATIVE_LAYOUT_DIRECTION)) | (this.type2Flags & 1)) | (this.type2Flags & 4));
            addUShort(261);
            addULong(2600);
            addUShort(3840);
            addBytes(this.lmResp);
            addBytes(this.ntResp);
            addBytes(this.domainBytes);
            addBytes(this.userBytes);
            addBytes(this.hostBytes);
            if (this.sessionKey != null) {
                addBytes(this.sessionKey);
            }
            return super.getResponse();
        }
    }

    NTLMEngineImpl() {
    }

    static /* synthetic */ byte[] access$1400(byte[] x0, int x1) throws NTLMEngineException {
        if (x0.length < x1 + 2) {
            throw new NTLMEngineException("NTLM authentication - buffer too small for WORD");
        }
        int i = (x0[x1] & MotionEventCompat.ACTION_MASK) | ((x0[x1 + 1] & MotionEventCompat.ACTION_MASK) << 8);
        int readULong = readULong(x0, x1 + 4);
        if (x0.length < readULong + i) {
            throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
        }
        Object obj = new byte[i];
        System.arraycopy(x0, readULong, obj, 0, i);
        return obj;
    }

    static /* synthetic */ byte[] access$400(String x0) throws NTLMEngineException {
        if (UNICODE_LITTLE_UNMARKED == null) {
            throw new NTLMEngineException("Unicode not supported");
        }
        byte[] bytes = x0.getBytes(UNICODE_LITTLE_UNMARKED);
        MD4 md4 = new MD4();
        md4.update(bytes);
        return md4.getOutput();
    }

    static /* synthetic */ byte[] access$500(String x0, String x1, byte[] x2) throws NTLMEngineException {
        if (UNICODE_LITTLE_UNMARKED == null) {
            throw new NTLMEngineException("Unicode not supported");
        }
        HMACMD5 hmacmd5 = new HMACMD5(x2);
        hmacmd5.update(x1.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
        if (x0 != null) {
            hmacmd5.update(x0.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
        }
        return hmacmd5.getOutput();
    }

    static /* synthetic */ byte[] access$600(String x0, String x1, byte[] x2) throws NTLMEngineException {
        if (UNICODE_LITTLE_UNMARKED == null) {
            throw new NTLMEngineException("Unicode not supported");
        }
        HMACMD5 hmacmd5 = new HMACMD5(x2);
        hmacmd5.update(x1.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
        if (x0 != null) {
            hmacmd5.update(x0.getBytes(UNICODE_LITTLE_UNMARKED));
        }
        return hmacmd5.getOutput();
    }

    static /* synthetic */ byte[] access$700(byte[] x0, byte[] x1, byte[] x2) {
        Object obj = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
        Object obj2 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
        Object obj3 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
        Object obj4 = new byte[(((((x2.length + 8) + 8) + 4) + x1.length) + 4)];
        System.arraycopy(new byte[]{(byte) 1, (byte) 1, (byte) 0, (byte) 0}, 0, obj4, 0, 4);
        System.arraycopy(obj, 0, obj4, 4, 4);
        System.arraycopy(x2, 0, obj4, 8, x2.length);
        int length = x2.length + 8;
        System.arraycopy(x0, 0, obj4, length, 8);
        length += 8;
        System.arraycopy(obj2, 0, obj4, length, 4);
        length += 4;
        System.arraycopy(x1, 0, obj4, length, x1.length);
        System.arraycopy(obj3, 0, obj4, length + x1.length, 4);
        return obj4;
    }

    static /* synthetic */ byte[] access$800(byte[] x0, byte[] x1, byte[] x2) throws NTLMEngineException {
        HMACMD5 hmacmd5 = new HMACMD5(x0);
        hmacmd5.update(x1);
        hmacmd5.update(x2);
        Object output = hmacmd5.getOutput();
        Object obj = new byte[(output.length + x2.length)];
        System.arraycopy(output, 0, obj, 0, output.length);
        System.arraycopy(x2, 0, obj, output.length, x2.length);
        return obj;
    }

    static {
        UNICODE_LITTLE_UNMARKED = CharsetUtils.lookup("UnicodeLittleUnmarked");
        DEFAULT_CHARSET = Consts.ASCII;
        SecureRandom rnd = null;
        try {
            rnd = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
        }
        RND_GEN = rnd;
        byte[] bytesWithoutNull = "NTLMSSP".getBytes(Consts.ASCII);
        SIGNATURE = new byte[(bytesWithoutNull.length + 1)];
        System.arraycopy(bytesWithoutNull, 0, SIGNATURE, 0, bytesWithoutNull.length);
        SIGNATURE[bytesWithoutNull.length] = (byte) 0;
        TYPE_1_MESSAGE = new Type1Message();
    }

    private static String stripDotSuffix(String value) {
        if (value == null) {
            return null;
        }
        int index = value.indexOf(".");
        if (index != -1) {
            return value.substring(0, index);
        }
        return value;
    }

    private static int readULong(byte[] src, int index) throws NTLMEngineException {
        if (src.length >= index + 4) {
            return (((src[index] & MotionEventCompat.ACTION_MASK) | ((src[index + 1] & MotionEventCompat.ACTION_MASK) << 8)) | ((src[index + 2] & MotionEventCompat.ACTION_MASK) << 16)) | ((src[index + 3] & MotionEventCompat.ACTION_MASK) << 24);
        }
        throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD");
    }

    private static byte[] makeRandomChallenge() throws NTLMEngineException {
        if (RND_GEN == null) {
            throw new NTLMEngineException("Random generator not available");
        }
        byte[] rval = new byte[8];
        synchronized (RND_GEN) {
            RND_GEN.nextBytes(rval);
        }
        return rval;
    }

    private static byte[] makeSecondaryKey() throws NTLMEngineException {
        if (RND_GEN == null) {
            throw new NTLMEngineException("Random generator not available");
        }
        byte[] rval = new byte[16];
        synchronized (RND_GEN) {
            RND_GEN.nextBytes(rval);
        }
        return rval;
    }

    static byte[] hmacMD5(byte[] value, byte[] key) throws NTLMEngineException {
        HMACMD5 hmacMD5 = new HMACMD5(key);
        hmacMD5.update(value);
        return hmacMD5.getOutput();
    }

    static byte[] RC4(byte[] value, byte[] key) throws NTLMEngineException {
        try {
            Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(1, new SecretKeySpec(key, "RC4"));
            return rc4.doFinal(value);
        } catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    static byte[] ntlm2SessionResponse(byte[] ntlmHash, byte[] challenge, byte[] clientChallenge) throws NTLMEngineException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(challenge);
            md5.update(clientChallenge);
            byte[] sessionHash = new byte[8];
            System.arraycopy(md5.digest(), 0, sessionHash, 0, 8);
            return lmResponse(ntlmHash, sessionHash);
        } catch (Exception e) {
            if (e instanceof NTLMEngineException) {
                throw ((NTLMEngineException) e);
            }
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    private static byte[] lmHash(String password) throws NTLMEngineException {
        try {
            byte[] oemPassword = password.toUpperCase(Locale.ROOT).getBytes(Consts.ASCII);
            byte[] keyBytes = new byte[14];
            System.arraycopy(oemPassword, 0, keyBytes, 0, Math.min(oemPassword.length, 14));
            Key lowKey = createDESKey(keyBytes, 0);
            Key highKey = createDESKey(keyBytes, 7);
            byte[] magicConstant = "KGS!@#$%".getBytes(Consts.ASCII);
            Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
            des.init(1, lowKey);
            byte[] lowHash = des.doFinal(magicConstant);
            des.init(1, highKey);
            byte[] highHash = des.doFinal(magicConstant);
            byte[] lmHash = new byte[16];
            System.arraycopy(lowHash, 0, lmHash, 0, 8);
            System.arraycopy(highHash, 0, lmHash, 8, 8);
            return lmHash;
        } catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    private static byte[] lmResponse(byte[] hash, byte[] challenge) throws NTLMEngineException {
        try {
            byte[] keyBytes = new byte[21];
            System.arraycopy(hash, 0, keyBytes, 0, 16);
            Key lowKey = createDESKey(keyBytes, 0);
            Key middleKey = createDESKey(keyBytes, 7);
            Key highKey = createDESKey(keyBytes, 14);
            Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
            des.init(1, lowKey);
            byte[] lowResponse = des.doFinal(challenge);
            des.init(1, middleKey);
            byte[] middleResponse = des.doFinal(challenge);
            des.init(1, highKey);
            byte[] highResponse = des.doFinal(challenge);
            byte[] lmResponse = new byte[24];
            System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
            System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
            System.arraycopy(highResponse, 0, lmResponse, 16, 8);
            return lmResponse;
        } catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    private static Key createDESKey(byte[] bytes, int offset) {
        keyBytes = new byte[7];
        System.arraycopy(bytes, offset, keyBytes, 0, 7);
        byte[] material = new byte[]{keyBytes[0], (byte) ((keyBytes[0] << 7) | ((keyBytes[1] & MotionEventCompat.ACTION_MASK) >>> 1)), (byte) ((keyBytes[1] << 6) | ((keyBytes[2] & MotionEventCompat.ACTION_MASK) >>> 2)), (byte) ((keyBytes[2] << 5) | ((keyBytes[3] & MotionEventCompat.ACTION_MASK) >>> 3)), (byte) ((keyBytes[3] << 4) | ((keyBytes[4] & MotionEventCompat.ACTION_MASK) >>> 4)), (byte) ((keyBytes[4] << 3) | ((keyBytes[5] & MotionEventCompat.ACTION_MASK) >>> 5)), (byte) ((keyBytes[5] << 2) | ((keyBytes[6] & MotionEventCompat.ACTION_MASK) >>> 6)), (byte) (keyBytes[6] << 1)};
        for (int i = 0; i < 8; i++) {
            byte b = material[i];
            if (((((b >>> 1) ^ ((((((b >>> 7) ^ (b >>> 6)) ^ (b >>> 5)) ^ (b >>> 4)) ^ (b >>> 3)) ^ (b >>> 2))) & 1) == 0 ? 1 : 0) != 0) {
                material[i] = (byte) (material[i] | 1);
            } else {
                material[i] = (byte) (material[i] & -2);
            }
        }
        return new SecretKeySpec(material, "DES");
    }

    static void writeULong(byte[] buffer, int value, int offset) {
        buffer[offset] = (byte) (value & MotionEventCompat.ACTION_MASK);
        buffer[offset + 1] = (byte) ((value >> 8) & MotionEventCompat.ACTION_MASK);
        buffer[offset + 2] = (byte) ((value >> 16) & MotionEventCompat.ACTION_MASK);
        buffer[offset + 3] = (byte) ((value >> 24) & MotionEventCompat.ACTION_MASK);
    }

    static int m44F(int x, int y, int z) {
        return (x & y) | ((x ^ -1) & z);
    }

    static int m45G(int x, int y, int z) {
        return ((x & y) | (x & z)) | (y & z);
    }

    static int m46H(int x, int y, int z) {
        return (x ^ y) ^ z;
    }

    static int rotintlft(int val, int numbits) {
        return (val << numbits) | (val >>> (32 - numbits));
    }

    public final String generateType1Msg$7157d249() throws NTLMEngineException {
        return TYPE_1_MESSAGE.getResponse();
    }

    public final String generateType3Msg(String username, String password, String domain, String workstation, String challenge) throws NTLMEngineException {
        Type2Message t2m = new Type2Message(challenge);
        return new Type3Message(domain, workstation, username, password, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo()).getResponse();
    }
}
