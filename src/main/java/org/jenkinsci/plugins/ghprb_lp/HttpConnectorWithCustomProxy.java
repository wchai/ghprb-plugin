package org.jenkinsci.plugins.ghprb_lp;

import hudson.ProxyConfiguration;
import hudson.URLConnectionDecorator;
import jenkins.util.JenkinsJVM;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.github.HttpConnector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpConnectorWithCustomProxy implements HttpConnector {

    private static final Logger LOGGER = Logger.getLogger(HttpConnectorWithCustomProxy.class.getName());

    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;

    private static final int DEFAULT_READ_TIMEOUT = 10000;

    private static final int DEFAULT_PROXY_PORT = 80;

    private String proxyHost = null;

    private int proxyPort = -1;

    public HttpConnectorWithCustomProxy(String proxySetting) {
        if (!StringUtils.isEmpty(proxySetting)) {
          // Format of proxySetting is "host:port"
          String[] tokens = proxySetting.split(":");
          if (tokens.length == 1) {
            proxyHost = proxySetting;
            proxyPort = DEFAULT_PROXY_PORT;
          } else {
              try {
                  proxyPort = Integer.parseInt(tokens[1]);
                  proxyHost = tokens[0];
              } catch (Exception e) {
                  LOGGER.log(Level.SEVERE, "Unable to parse proxy {0}, are you set it as \"host:port\"?", proxySetting);
              }
          }
        }
    }

    public HttpURLConnection connect(URL url) throws IOException {
        // Use our own version of open() instead of the static ProxyConfiguration.opne()
        HttpURLConnection con = (HttpURLConnection) open(url);

        // Set default timeouts in case there are none
        if (con.getConnectTimeout() == 0) {
            con.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        }
        if (con.getReadTimeout() == 0) {
            con.setReadTimeout(DEFAULT_READ_TIMEOUT);
        }
        return con;
    }

    public URLConnection open(URL url) throws IOException {
        // Use our own proxy settings instead of the jenkins system one
        final ProxyConfiguration p = (proxyHost != null && proxyPort != -1)
            ? new ProxyConfiguration(proxyHost, proxyPort)
            : null;

        URLConnection con;
        if (p == null) {
            con = url.openConnection();
        } else {
            con = url.openConnection(p.createProxy(url.getHost()));
        }

        if (JenkinsJVM.isJenkinsJVM()) { // this code may run on a slave
            decorate(con);
        }

        return con;
    }

    private void decorate(URLConnection con) throws IOException {
        for (URLConnectionDecorator d : URLConnectionDecorator.all()) {
            d.decorate(con);
        }
    }
}
