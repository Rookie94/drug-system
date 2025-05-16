package com.ruoyi.framework.security.handle;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 自定义响应增强器
 */
public class EncryptionResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private boolean hasFlushed = false;

    public EncryptionResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {}

            @Override
            public void write(int b) throws IOException {
                byteArrayOutputStream.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8));
    }

    @Override
    public void flushBuffer() throws IOException {
        super.flushBuffer();
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.flush();
        }
        hasFlushed = true;
    }

    public byte[] getContentAsByteArray() throws IOException {
        if (!hasFlushed) {
            flushBuffer();
        }
        return byteArrayOutputStream.toByteArray();
    }
}
