package com.ruoyi.common.utils.file;

import com.amazonaws.DefaultRequest;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.http.HttpMethodName;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ZosSignableRequestAdapter extends DefaultRequest<Void> {

    private final HttpRequestBase http;

    public ZosSignableRequestAdapter(HttpRequestBase http, String endpoint) {
        super(null);
        this.http = http;
        setEndpoint(URI.create(endpoint));
        setResourcePath(http.getURI().getPath());
        setHttpMethod(HttpMethodName.valueOf(http.getMethod()));

        // 把头拷进来
        Map<String, String> h = new HashMap<>();
        for (org.apache.http.Header ch : http.getAllHeaders()) {
            h.put(ch.getName(), ch.getValue());
        }
        setHeaders(h);
    }

    /* 后续签名前会把新头写回 http */
    public void writeBackHeaders() {
        getHeaders().forEach(http::setHeader);
    }
}