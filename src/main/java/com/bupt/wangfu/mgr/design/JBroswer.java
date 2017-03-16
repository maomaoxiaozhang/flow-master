package com.bupt.wangfu.mgr.design;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HanB on 2016/9/13 0013.
 * 浏览网页拓扑
 */
public class JBroswer extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JPanel jPanel;

    public JBroswer() {
        super(new BorderLayout());
    }
    public JPanel broswerPanle(final String url) {
        JPanel webBrowserPanel = new JPanel(new BorderLayout());
        JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setBarsVisible(false);
        webBrowser.navigate(url);
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
        add(webBrowserPanel, BorderLayout.CENTER);
        return this;
    }

    public JPanel getBroswer(final String url) {
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jPanel = new JPanel(new BorderLayout());
                jPanel.add(broswerPanle(url));
            }
        });
        return jPanel;
    }
}
