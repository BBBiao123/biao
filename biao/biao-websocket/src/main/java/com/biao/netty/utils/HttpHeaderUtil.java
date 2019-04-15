package com.biao.netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

/**
 * HttpHeaderUtil.
 *
 *  ""(Myth)
 */
public class HttpHeaderUtil {
    public static boolean isKeepAlive(HttpMessage message) {
        CharSequence connection = message.headers().get(HttpHeaderNames.CONNECTION);
        if (connection != null && HttpHeaderValues.CLOSE.equals(connection)) {
            return false;
        } else if (message.protocolVersion().isKeepAliveDefault()) {
            return !HttpHeaderValues.CLOSE.equals(connection);
        } else {
            return HttpHeaderValues.KEEP_ALIVE.equals(connection);
        }
    }

    public static void setKeepAlive(HttpMessage message, boolean keepAlive) {
        HttpHeaders h = message.headers();
        if (message.protocolVersion().isKeepAliveDefault()) {
            if (keepAlive) {
                h.remove(HttpHeaderNames.CONNECTION);
            } else {
                h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            }
        } else if (keepAlive) {
            h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        } else {
            h.remove(HttpHeaderNames.CONNECTION);
        }

    }

    private static int getWebSocketContentLength(HttpMessage message) {
        HttpHeaders h = message.headers();
        if (message instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) message;
            if (HttpMethod.GET.equals(req.method()) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                return 8;
            }
        } else if (message instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) message;
            if (res.status().code() == 101 && h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                return 16;
            }
        }

        return -1;
    }

    public static boolean isContentLengthSet(HttpMessage m) {
        return m.headers().contains(HttpHeaderNames.CONTENT_LENGTH);
    }

    public static boolean is100ContinueExpected(HttpMessage message) {
        if (!(message instanceof HttpRequest)) {
            return false;
        } else if (message.protocolVersion().compareTo(HttpVersion.HTTP_1_1) < 0) {
            return false;
        } else {
            CharSequence value = (CharSequence) message.headers().get(HttpHeaderNames.EXPECT);
            if (value == null) {
                return false;
            } else {
                return HttpHeaderValues.CONTINUE.equals(value) ? true : message.headers().contains(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE, true);
            }
        }
    }

    public static void set100ContinueExpected(HttpMessage message, boolean expected) {
        if (expected) {
            message.headers().set(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
        } else {
            message.headers().remove(HttpHeaderNames.EXPECT);
        }

    }

    public static boolean isTransferEncodingChunked(HttpMessage message) {
        return message.headers().contains(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
    }


    static void encodeAscii0(CharSequence seq, ByteBuf buf) {
        int length = seq.length();

        for (int i = 0; i < length; ++i) {
            buf.writeByte((byte) seq.charAt(i));
        }

    }

    private HttpHeaderUtil() {
    }
}
