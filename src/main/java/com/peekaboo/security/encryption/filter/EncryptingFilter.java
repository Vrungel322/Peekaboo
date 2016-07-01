package com.peekaboo.security.encryption.filter;

import com.peekaboo.security.encryption.Decryptor;
import com.peekaboo.security.encryption.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.util.UrlPathHelper;
import sun.misc.IOUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class EncryptingFilter implements Filter{

    private List<String> permittedAddressList;

    private Encryptor encryptor;

    @Autowired
    private Decryptor decryptor;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        permittedAddressList = new ArrayList<>();
        permittedAddressList.add("/");
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String address = new UrlPathHelper().getPathWithinApplication(req);

        //if address is in permitted list, no need to encrypt/decrypt data
        if (permittedAddressList.stream().allMatch(address::matches)) {
            chain.doFilter(request, response);
        } else {

//            String body = req.getReader().lines().reduce(String::concat).get();
//            String decoded = decryptor.decrypt(body);

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    private class DecryptedRequestWrapper extends HttpServletRequestWrapper {
        private byte[] rawData;
        private ServletInputStream servletStream;

        public DecryptedRequestWrapper(HttpServletRequest request, String data) {
            super(request);
            this.setRequest(request);
            this.servletStream = new ServletInputStream() {

                private ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());

                @Override
                public boolean isFinished() {
                    return stream.available() > -1;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return stream.read();
                }
            };
        }


        @Override
        public ServletInputStream getInputStream() throws IOException {
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(servletStream));
        }

    }
}
