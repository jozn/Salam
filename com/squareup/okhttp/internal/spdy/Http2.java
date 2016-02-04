package com.squareup.okhttp.internal.spdy;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import com.kyleduo.switchbutton.C0473R;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.spdy.FrameReader.Handler;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public final class Http2 implements Variant {
    private static final ByteString CONNECTION_PREFACE;
    static final byte FLAG_ACK = (byte) 1;
    static final byte FLAG_COMPRESSED = (byte) 32;
    static final byte FLAG_END_HEADERS = (byte) 4;
    static final byte FLAG_END_PUSH_PROMISE = (byte) 4;
    static final byte FLAG_END_STREAM = (byte) 1;
    static final byte FLAG_NONE = (byte) 0;
    static final byte FLAG_PADDED = (byte) 8;
    static final byte FLAG_PRIORITY = (byte) 32;
    static final int INITIAL_MAX_FRAME_SIZE = 16384;
    static final byte TYPE_CONTINUATION = (byte) 9;
    static final byte TYPE_DATA = (byte) 0;
    static final byte TYPE_GOAWAY = (byte) 7;
    static final byte TYPE_HEADERS = (byte) 1;
    static final byte TYPE_PING = (byte) 6;
    static final byte TYPE_PRIORITY = (byte) 2;
    static final byte TYPE_PUSH_PROMISE = (byte) 5;
    static final byte TYPE_RST_STREAM = (byte) 3;
    static final byte TYPE_SETTINGS = (byte) 4;
    static final byte TYPE_WINDOW_UPDATE = (byte) 8;
    private static final Logger logger;

    static final class ContinuationSource implements Source {
        byte flags;
        int left;
        int length;
        short padding;
        private final BufferedSource source;
        int streamId;

        public ContinuationSource(BufferedSource source) {
            this.source = source;
        }

        public final long read(Buffer sink, long byteCount) throws IOException {
            while (this.left == 0) {
                this.source.skip((long) this.padding);
                this.padding = (short) 0;
                if ((this.flags & 4) != 0) {
                    return -1;
                }
                readContinuationHeader();
            }
            long read = this.source.read(sink, Math.min(byteCount, (long) this.left));
            if (read == -1) {
                return -1;
            }
            this.left = (int) (((long) this.left) - read);
            return read;
        }

        public final Timeout timeout() {
            return this.source.timeout();
        }

        public final void close() throws IOException {
        }

        private void readContinuationHeader() throws IOException {
            int previousStreamId = this.streamId;
            int access$300 = Http2.readMedium(this.source);
            this.left = access$300;
            this.length = access$300;
            byte type = (byte) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
            this.flags = (byte) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
            if (Http2.logger.isLoggable(Level.FINE)) {
                Http2.logger.fine(FrameLogger.formatHeader(true, this.streamId, this.length, type, this.flags));
            }
            this.streamId = this.source.readInt() & ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            if (type != 9) {
                throw Http2.ioException("%s != TYPE_CONTINUATION", Byte.valueOf(type));
            } else if (this.streamId != previousStreamId) {
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
        }
    }

    static final class FrameLogger {
        private static final String[] BINARY;
        private static final String[] FLAGS;
        private static final String[] TYPES;

        FrameLogger() {
        }

        static String formatHeader(boolean inbound, int streamId, int length, byte type, byte flags) {
            String formattedType = type < TYPES.length ? TYPES[type] : String.format("0x%02x", new Object[]{Byte.valueOf(type)});
            String formattedFlags = formatFlags(type, flags);
            String str = "%s 0x%08x %5d %-13s %s";
            Object[] objArr = new Object[5];
            objArr[0] = inbound ? "<<" : ">>";
            objArr[1] = Integer.valueOf(streamId);
            objArr[2] = Integer.valueOf(length);
            objArr[3] = formattedType;
            objArr[4] = formattedFlags;
            return String.format(str, objArr);
        }

        static String formatFlags(byte type, byte flags) {
            if (flags == null) {
                return BuildConfig.VERSION_NAME;
            }
            switch (type) {
                case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                case org.eclipse.paho.client.mqttv3.logging.Logger.FINEST /*7*/:
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    return BINARY[flags];
                case org.eclipse.paho.client.mqttv3.logging.Logger.CONFIG /*4*/:
                case org.eclipse.paho.client.mqttv3.logging.Logger.FINER /*6*/:
                    return flags == 1 ? "ACK" : BINARY[flags];
                default:
                    String result = flags < FLAGS.length ? FLAGS[flags] : BINARY[flags];
                    if (type == 5 && (flags & 4) != 0) {
                        return result.replace("HEADERS", "PUSH_PROMISE");
                    }
                    if (type != null || (flags & 32) == 0) {
                        return result;
                    }
                    return result.replace("PRIORITY", "COMPRESSED");
            }
        }

        static {
            int i;
            int i2;
            TYPES = new String[]{"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
            FLAGS = new String[64];
            BINARY = new String[AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY];
            for (i = 0; i < BINARY.length; i++) {
                BINARY[i] = String.format("%8s", new Object[]{Integer.toBinaryString(i)}).replace(' ', '0');
            }
            FLAGS[0] = BuildConfig.VERSION_NAME;
            FLAGS[1] = "END_STREAM";
            int[] prefixFlags = new int[]{1};
            FLAGS[8] = "PADDED";
            for (i2 = 0; i2 <= 0; i2++) {
                int prefixFlag = prefixFlags[i2];
                FLAGS[prefixFlag | 8] = FLAGS[prefixFlag] + "|PADDED";
            }
            FLAGS[4] = "END_HEADERS";
            FLAGS[32] = "PRIORITY";
            FLAGS[36] = "END_HEADERS|PRIORITY";
            int[] iArr = new int[]{4, 32, 36};
            for (int i3 = 0; i3 < 3; i3++) {
                int frameFlag = iArr[i3];
                for (i2 = 0; i2 <= 0; i2++) {
                    prefixFlag = prefixFlags[i2];
                    FLAGS[prefixFlag | frameFlag] = FLAGS[prefixFlag] + '|' + FLAGS[frameFlag];
                    FLAGS[(prefixFlag | frameFlag) | 8] = FLAGS[prefixFlag] + '|' + FLAGS[frameFlag] + "|PADDED";
                }
            }
            for (i = 0; i < FLAGS.length; i++) {
                if (FLAGS[i] == null) {
                    FLAGS[i] = BINARY[i];
                }
            }
        }
    }

    static final class Reader implements FrameReader {
        private final boolean client;
        private final ContinuationSource continuation;
        final Reader hpackReader;
        private final BufferedSource source;

        Reader(BufferedSource source, int headerTableSize, boolean client) {
            this.source = source;
            this.client = client;
            this.continuation = new ContinuationSource(this.source);
            this.hpackReader = new Reader(headerTableSize, this.continuation);
        }

        public final void readConnectionPreface() throws IOException {
            if (!this.client) {
                ByteString connectionPreface = this.source.readByteString((long) Http2.CONNECTION_PREFACE.data.length);
                if (Http2.logger.isLoggable(Level.FINE)) {
                    Http2.logger.fine(String.format("<< CONNECTION %s", new Object[]{connectionPreface.hex()}));
                }
                if (!Http2.CONNECTION_PREFACE.equals(connectionPreface)) {
                    throw Http2.ioException("Expected a connection header but was %s", connectionPreface.utf8());
                }
            }
        }

        public final boolean nextFrame(Handler handler) throws IOException {
            try {
                this.source.require(9);
                int length = Http2.readMedium(this.source);
                if (length < 0 || length > Http2.INITIAL_MAX_FRAME_SIZE) {
                    throw Http2.ioException("FRAME_SIZE_ERROR: %s", Integer.valueOf(length));
                }
                byte type = (byte) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
                byte flags = (byte) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
                int streamId = this.source.readInt() & ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                if (Http2.logger.isLoggable(Level.FINE)) {
                    Http2.logger.fine(FrameLogger.formatHeader(true, streamId, length, type, flags));
                }
                switch (type) {
                    case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                        readData(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                        readHeaders(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                        readPriority(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                        readRstStream(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.CONFIG /*4*/:
                        readSettings(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.FINE /*5*/:
                        readPushPromise(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.FINER /*6*/:
                        readPing(handler, length, flags, streamId);
                        return true;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.FINEST /*7*/:
                        readGoAway(handler, length, flags, streamId);
                        return true;
                    case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                        readWindowUpdate(handler, length, flags, streamId);
                        return true;
                    default:
                        this.source.skip((long) length);
                        return true;
                }
            } catch (IOException e) {
                return false;
            }
        }

        private void readHeaders(Handler handler, int length, byte flags, int streamId) throws IOException {
            if (streamId == 0) {
                throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
            }
            boolean endStream;
            short padding;
            if ((flags & 1) != 0) {
                endStream = true;
            } else {
                endStream = false;
            }
            if ((flags & 8) != 0) {
                padding = (short) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
            } else {
                padding = (short) 0;
            }
            if ((flags & 32) != 0) {
                readPriority(handler, streamId);
                length -= 5;
            }
            handler.headers(false, endStream, streamId, -1, readHeaderBlock(Http2.lengthWithoutPadding(length, flags, padding), padding, flags, streamId), HeadersMode.HTTP_20_HEADERS);
        }

        private List<Header> readHeaderBlock(int length, short padding, byte flags, int streamId) throws IOException {
            ContinuationSource continuationSource = this.continuation;
            this.continuation.left = length;
            continuationSource.length = length;
            this.continuation.padding = padding;
            this.continuation.flags = flags;
            this.continuation.streamId = streamId;
            this.hpackReader.readHeaders();
            return this.hpackReader.getAndResetHeaderList();
        }

        private void readData(Handler handler, int length, byte flags, int streamId) throws IOException {
            boolean inFinished;
            short s = (short) 1;
            short padding = (short) 0;
            if ((flags & 1) != 0) {
                inFinished = true;
            } else {
                inFinished = false;
            }
            if ((flags & 32) == 0) {
                s = (short) 0;
            }
            if (s != (short) 0) {
                throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
            }
            if ((flags & 8) != 0) {
                padding = (short) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
            }
            handler.data(inFinished, streamId, this.source, Http2.lengthWithoutPadding(length, flags, padding));
            this.source.skip((long) padding);
        }

        private void readPriority(Handler handler, int length, byte flags, int streamId) throws IOException {
            if (length != 5) {
                throw Http2.ioException("TYPE_PRIORITY length: %d != 5", Integer.valueOf(length));
            } else if (streamId == 0) {
                throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
            } else {
                readPriority(handler, streamId);
            }
        }

        private void readPriority(Handler handler, int streamId) throws IOException {
            int w1 = this.source.readInt();
            handler.priority(streamId, w1 & ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, (this.source.readByte() & MotionEventCompat.ACTION_MASK) + 1, (RtlSpacingHelper.UNDEFINED & w1) != 0);
        }

        private void readRstStream(Handler handler, int length, byte flags, int streamId) throws IOException {
            if (length != 4) {
                throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", Integer.valueOf(length));
            } else if (streamId == 0) {
                throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
            } else {
                ErrorCode errorCode = ErrorCode.fromHttp2(this.source.readInt());
                if (errorCode == null) {
                    throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", Integer.valueOf(errorCodeInt));
                } else {
                    handler.rstStream(streamId, errorCode);
                }
            }
        }

        private void readSettings(Handler handler, int length, byte flags, int streamId) throws IOException {
            if (streamId != 0) {
                throw Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]);
            } else if ((flags & 1) != 0) {
                if (length != 0) {
                    throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
                }
                handler.ackSettings();
            } else if (length % 6 != 0) {
                throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", Integer.valueOf(length));
            } else {
                Settings settings = new Settings();
                for (int i = 0; i < length; i += 6) {
                    short id = this.source.readShort();
                    int value = this.source.readInt();
                    switch (id) {
                        case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                        case org.eclipse.paho.client.mqttv3.logging.Logger.FINER /*6*/:
                            break;
                        case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                            if (!(value == 0 || value == 1)) {
                                throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                            }
                        case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                            id = (short) 4;
                            break;
                        case org.eclipse.paho.client.mqttv3.logging.Logger.CONFIG /*4*/:
                            id = (short) 7;
                            if (value >= 0) {
                                break;
                            }
                            throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                        case org.eclipse.paho.client.mqttv3.logging.Logger.FINE /*5*/:
                            if (value >= Http2.INITIAL_MAX_FRAME_SIZE && value <= ViewCompat.MEASURED_SIZE_MASK) {
                                break;
                            }
                            throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", Integer.valueOf(value));
                        default:
                            throw Http2.ioException("PROTOCOL_ERROR invalid settings id: %s", Short.valueOf(id));
                    }
                    settings.set(id, 0, value);
                }
                handler.settings(false, settings);
                if (settings.getHeaderTableSize() >= 0) {
                    this.hpackReader.headerTableSizeSetting(settings.getHeaderTableSize());
                }
            }
        }

        private void readPushPromise(Handler handler, int length, byte flags, int streamId) throws IOException {
            short padding = (short) 0;
            if (streamId == 0) {
                throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
            }
            if ((flags & 8) != 0) {
                padding = (short) (this.source.readByte() & MotionEventCompat.ACTION_MASK);
            }
            handler.pushPromise(streamId, this.source.readInt() & ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, readHeaderBlock(Http2.lengthWithoutPadding(length - 4, flags, padding), padding, flags, streamId));
        }

        private void readPing(Handler handler, int length, byte flags, int streamId) throws IOException {
            boolean ack = true;
            if (length != 8) {
                throw Http2.ioException("TYPE_PING length != 8: %s", Integer.valueOf(length));
            } else if (streamId != 0) {
                throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
            } else {
                int payload1 = this.source.readInt();
                int payload2 = this.source.readInt();
                if ((flags & 1) == 0) {
                    ack = false;
                }
                handler.ping(ack, payload1, payload2);
            }
        }

        private void readGoAway(Handler handler, int length, byte flags, int streamId) throws IOException {
            if (length < 8) {
                throw Http2.ioException("TYPE_GOAWAY length < 8: %s", Integer.valueOf(length));
            } else if (streamId != 0) {
                throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
            } else {
                int lastStreamId = this.source.readInt();
                int opaqueDataLength = length - 8;
                ErrorCode errorCode = ErrorCode.fromHttp2(this.source.readInt());
                if (errorCode == null) {
                    throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", Integer.valueOf(errorCodeInt));
                }
                ByteString debugData = ByteString.EMPTY;
                if (opaqueDataLength > 0) {
                    debugData = this.source.readByteString((long) opaqueDataLength);
                }
                handler.goAway(lastStreamId, errorCode, debugData);
            }
        }

        private void readWindowUpdate(Handler handler, int length, byte flags, int streamId) throws IOException {
            if (length != 4) {
                throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", Integer.valueOf(length));
            }
            long increment = ((long) this.source.readInt()) & 2147483647L;
            if (increment == 0) {
                throw Http2.ioException("windowSizeIncrement was 0", Long.valueOf(increment));
            } else {
                handler.windowUpdate(streamId, increment);
            }
        }

        public final void close() throws IOException {
            this.source.close();
        }
    }

    static final class Writer implements FrameWriter {
        private final boolean client;
        private boolean closed;
        private final Buffer hpackBuffer;
        private final Writer hpackWriter;
        private int maxFrameSize;
        private final BufferedSink sink;

        Writer(BufferedSink sink, boolean client) {
            this.sink = sink;
            this.client = client;
            this.hpackBuffer = new Buffer();
            this.hpackWriter = new Writer(this.hpackBuffer);
            this.maxFrameSize = Http2.INITIAL_MAX_FRAME_SIZE;
        }

        public final synchronized void flush() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            this.sink.flush();
        }

        public final synchronized void ackSettings(Settings peerSettings) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            this.maxFrameSize = peerSettings.getMaxFrameSize(this.maxFrameSize);
            frameHeader(0, 0, Http2.TYPE_SETTINGS, Http2.TYPE_HEADERS);
            this.sink.flush();
        }

        public final synchronized void connectionPreface() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            } else if (this.client) {
                if (Http2.logger.isLoggable(Level.FINE)) {
                    Http2.logger.fine(String.format(">> CONNECTION %s", new Object[]{Http2.CONNECTION_PREFACE.hex()}));
                }
                this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
                this.sink.flush();
            }
        }

        public final synchronized void synStream(boolean outFinished, boolean inFinished, int streamId, int associatedStreamId, List<Header> headerBlock) throws IOException {
            if (inFinished) {
                throw new UnsupportedOperationException();
            } else if (this.closed) {
                throw new IOException("closed");
            } else {
                headers(outFinished, streamId, headerBlock);
            }
        }

        public final synchronized void synReply(boolean outFinished, int streamId, List<Header> headerBlock) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            headers(outFinished, streamId, headerBlock);
        }

        public final synchronized void headers(int streamId, List<Header> headerBlock) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            headers(false, streamId, headerBlock);
        }

        public final synchronized void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            } else if (this.hpackBuffer.size != 0) {
                throw new IllegalStateException();
            } else {
                this.hpackWriter.writeHeaders(requestHeaders);
                long byteCount = this.hpackBuffer.size;
                int length = (int) Math.min((long) (this.maxFrameSize - 4), byteCount);
                frameHeader(streamId, length + 4, Http2.TYPE_PUSH_PROMISE, byteCount == ((long) length) ? Http2.TYPE_SETTINGS : Http2.TYPE_DATA);
                this.sink.writeInt(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & promisedStreamId);
                this.sink.write(this.hpackBuffer, (long) length);
                if (byteCount > ((long) length)) {
                    writeContinuationFrames(streamId, byteCount - ((long) length));
                }
            }
        }

        final void headers(boolean outFinished, int streamId, List<Header> headerBlock) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            } else if (this.hpackBuffer.size != 0) {
                throw new IllegalStateException();
            } else {
                this.hpackWriter.writeHeaders(headerBlock);
                long byteCount = this.hpackBuffer.size;
                int length = (int) Math.min((long) this.maxFrameSize, byteCount);
                byte flags = byteCount == ((long) length) ? Http2.TYPE_SETTINGS : Http2.TYPE_DATA;
                if (outFinished) {
                    flags = (byte) (flags | 1);
                }
                frameHeader(streamId, length, Http2.TYPE_HEADERS, flags);
                this.sink.write(this.hpackBuffer, (long) length);
                if (byteCount > ((long) length)) {
                    writeContinuationFrames(streamId, byteCount - ((long) length));
                }
            }
        }

        private void writeContinuationFrames(int streamId, long byteCount) throws IOException {
            while (byteCount > 0) {
                int length = (int) Math.min((long) this.maxFrameSize, byteCount);
                byteCount -= (long) length;
                frameHeader(streamId, length, Http2.TYPE_CONTINUATION, byteCount == 0 ? Http2.TYPE_SETTINGS : Http2.TYPE_DATA);
                this.sink.write(this.hpackBuffer, (long) length);
            }
        }

        public final synchronized void rstStream(int streamId, ErrorCode errorCode) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            } else if (errorCode.spdyRstCode == -1) {
                throw new IllegalArgumentException();
            } else {
                frameHeader(streamId, 4, Http2.TYPE_RST_STREAM, Http2.TYPE_DATA);
                this.sink.writeInt(errorCode.httpCode);
                this.sink.flush();
            }
        }

        public final int maxDataLength() {
            return this.maxFrameSize;
        }

        public final synchronized void data(boolean outFinished, int streamId, Buffer source, int byteCount) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            byte flags = Http2.TYPE_DATA;
            if (outFinished) {
                flags = Http2.TYPE_HEADERS;
            }
            dataFrame(streamId, flags, source, byteCount);
        }

        final void dataFrame(int streamId, byte flags, Buffer buffer, int byteCount) throws IOException {
            frameHeader(streamId, byteCount, Http2.TYPE_DATA, flags);
            if (byteCount > 0) {
                this.sink.write(buffer, (long) byteCount);
            }
        }

        public final synchronized void settings(Settings settings) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            frameHeader(0, settings.size() * 6, Http2.TYPE_SETTINGS, Http2.TYPE_DATA);
            for (int i = 0; i < 10; i++) {
                if (settings.isSet(i)) {
                    int id = i;
                    if (i == 4) {
                        id = 3;
                    } else if (id == 7) {
                        id = 4;
                    }
                    this.sink.writeShort(id);
                    this.sink.writeInt(settings.get(i));
                }
            }
            this.sink.flush();
        }

        public final synchronized void ping(boolean ack, int payload1, int payload2) throws IOException {
            byte flags = Http2.TYPE_DATA;
            synchronized (this) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (ack) {
                    flags = Http2.TYPE_HEADERS;
                }
                frameHeader(0, 8, Http2.TYPE_PING, flags);
                this.sink.writeInt(payload1);
                this.sink.writeInt(payload2);
                this.sink.flush();
            }
        }

        public final synchronized void goAway(int lastGoodStreamId, ErrorCode errorCode, byte[] debugData) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            } else if (errorCode.httpCode == -1) {
                throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
            } else {
                frameHeader(0, debugData.length + 8, Http2.TYPE_GOAWAY, Http2.TYPE_DATA);
                this.sink.writeInt(lastGoodStreamId);
                this.sink.writeInt(errorCode.httpCode);
                if (debugData.length > 0) {
                    this.sink.write(debugData);
                }
                this.sink.flush();
            }
        }

        public final synchronized void windowUpdate(int streamId, long windowSizeIncrement) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            } else if (windowSizeIncrement == 0 || windowSizeIncrement > 2147483647L) {
                throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", Long.valueOf(windowSizeIncrement));
            } else {
                frameHeader(streamId, 4, Http2.TYPE_WINDOW_UPDATE, Http2.TYPE_DATA);
                this.sink.writeInt((int) windowSizeIncrement);
                this.sink.flush();
            }
        }

        public final synchronized void close() throws IOException {
            this.closed = true;
            this.sink.close();
        }

        final void frameHeader(int streamId, int length, byte type, byte flags) throws IOException {
            if (Http2.logger.isLoggable(Level.FINE)) {
                Http2.logger.fine(FrameLogger.formatHeader(false, streamId, length, type, flags));
            }
            if (length > this.maxFrameSize) {
                throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", Integer.valueOf(this.maxFrameSize), Integer.valueOf(length));
            } else if ((RtlSpacingHelper.UNDEFINED & streamId) != 0) {
                throw Http2.illegalArgument("reserved bit set: %s", Integer.valueOf(streamId));
            } else {
                Http2.writeMedium(this.sink, length);
                this.sink.writeByte(type & MotionEventCompat.ACTION_MASK);
                this.sink.writeByte(flags & MotionEventCompat.ACTION_MASK);
                this.sink.writeInt(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & streamId);
            }
        }
    }

    static {
        logger = Logger.getLogger(FrameLogger.class.getName());
        CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
    }

    public final Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    public final FrameReader newReader(BufferedSource source, boolean client) {
        return new Reader(source, AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD, client);
    }

    public final FrameWriter newWriter(BufferedSink sink, boolean client) {
        return new Writer(sink, client);
    }

    private static IllegalArgumentException illegalArgument(String message, Object... args) {
        throw new IllegalArgumentException(String.format(message, args));
    }

    private static IOException ioException(String message, Object... args) throws IOException {
        throw new IOException(String.format(message, args));
    }

    private static int lengthWithoutPadding(int length, byte flags, short padding) throws IOException {
        if ((flags & 8) != 0) {
            short length2 = length - 1;
        }
        if (padding <= length2) {
            return (short) (length2 - padding);
        }
        throw ioException("PROTOCOL_ERROR padding %s > remaining length %s", Short.valueOf(padding), Integer.valueOf(length2));
    }

    private static int readMedium(BufferedSource source) throws IOException {
        return (((source.readByte() & MotionEventCompat.ACTION_MASK) << 16) | ((source.readByte() & MotionEventCompat.ACTION_MASK) << 8)) | (source.readByte() & MotionEventCompat.ACTION_MASK);
    }

    private static void writeMedium(BufferedSink sink, int i) throws IOException {
        sink.writeByte((i >>> 16) & MotionEventCompat.ACTION_MASK);
        sink.writeByte((i >>> 8) & MotionEventCompat.ACTION_MASK);
        sink.writeByte(i & MotionEventCompat.ACTION_MASK);
    }
}
