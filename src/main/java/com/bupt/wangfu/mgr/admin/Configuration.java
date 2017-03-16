package com.bupt.wangfu.mgr.admin;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

public class Configuration {

	public void configure() {

		Properties props = new Properties();
		File file = new File("./Aconfigure.txt");
		BufferedReader reader;
		try {
			System.out.println(System.getProperty("user.dir"));
			reader = new BufferedReader(new FileReader(file));
			String l;
			String[] s;
			while ((l = reader.readLine()) != null) {
				s = l.split(":");
				s[0] = s[0].trim();
				switch (s[0]) {
					case "localAddr":
						AdminMgr.localAddr = s[1].trim();
						break;
					case "port":
						AdminMgr.port = Integer.parseInt(s[1].trim());
						break;
					case "ldapAddr":
						AdminMgr.ldapAddr = s[1].trim();
						break;
					case "sdnIP":
						AdminMgr.globalControllerAddr = s[1].trim();
						break;
				}
			}

			InputStream in = new BufferedInputStream (new FileInputStream("./SdnConfig.properties"));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = props.getProperty (key);
				if (key.equals("集群规模")) {
					AdminMgr.groupCount = Integer.parseInt(property);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
