package com.bupt.wangfu.mgr.wsnPolicy;

import com.bupt.wangfu.info.msg.PolicyDB;
import com.bupt.wangfu.ldap.Ldap;
import com.bupt.wangfu.ldap.TopicEntry;
import com.bupt.wangfu.mgr.admin.AdminMgr;
import com.bupt.wangfu.mgr.wsnPolicy.msgs.WsnPolicyMsg;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.naming.NamingException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShorenUtils {

	private static final String POLICYMSG = "./policyMsg.xml";
	public static Ldap ldap = null;

	public static String getXml(Node node) {
		try {
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			aTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			Source src = new DOMSource(node);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Result dest = new StreamResult(stream);
			aTransformer.transform(src, dest);

			return stream.toString("UTF-8");//原来UTF-8
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void writeFile(String contents, String filename) throws IOException {
		OutputStreamWriter o = new OutputStreamWriter(new FileOutputStream( new File(filename)), "UTF-8");
		o.write(contents);
		o.flush();
		o.close();
	}

	public static Document createDocument() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();

			return parser.newDocument();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	//编码的时候查看是否有此策略的信息，若有则删除，再添加，若没有，则直接添加。写入ldap
	public static void encodePolicyMsg(TopicEntry topic) {
		List<TopicEntry> topicList = ldap.getAllChildrens(topic);
		ldap.deleteWithAllChildrens(topic);
		ldap.create(topic);
		for (TopicEntry tpentry : topicList) {
			ldap.create(tpentry);
		}

		// send this policy message to groups
		PolicyDB policys = new PolicyDB();
		policys.clearAll = false;
		policys.time = System.currentTimeMillis();
		topic.getWsnpolicymsg().setTargetTopic(getFullName(topic));
		policys.pdb.add(topic.getWsnpolicymsg());
		encodePolicyMsg(topic.getWsnpolicymsg());//写入xml文件

		spreadPolicyMsg(policys);
	}

	//encode WsnPolicyMsg写入xml文件
	public static void encodePolicyMsg(WsnPolicyMsg msg) {
		Document doc = null;
		Node root;
		String fileName = POLICYMSG;
		try {
			doc = parse(readFile(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			//若没有文件，则生成一个。
			doc = ShorenUtils.createDocument();
			assert doc != null;
			root = doc.createElement("WsnPolicyMsgs");
			doc.appendChild(root);
		}

		//获取root节点
		WsnPolicyMsgCodec codec = new WsnPolicyMsgCodec();
		codec.setDocument(doc);
		root = doc.getElementsByTagName("WsnPolicyMsgs").item(0);

		//判断有没有当前策略的信息，有则删除
		Node node = getPolicyMsg(root, msg.getTargetTopic());
		if (node != null)
			root.removeChild(node);

		//写入当前策略信息
		root.appendChild(codec.encodePolicyMsg(msg));
		try {
			writeFile(getXml(doc.getDocumentElement()), fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void spreadPolicyMsg(Object obj) {
		AdminMgr.bloodMsg(obj);
	}

	//从文件中查找topic策略信息，并返回node.
	protected static Node getPolicyMsg(Node root, String topic) {
		NodeList nodes = root.getChildNodes();
		if (nodes != null) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String name = node.getNodeName();
				//需要判断topic！！！！！！！！！！！！！！！！！！！！
				if (name.equals("policyMsg") && node.hasAttributes()) {
					NamedNodeMap attrs = node.getAttributes();
					for (int j = 0; j < attrs.getLength(); j++) {
						Node attr = attrs.item(j);
						if (attr.getNodeName().equals("targetTopic")) {
							String value = attr.getNodeValue();
							if (value.equals(topic)) {
								return node;
							} else
								break;
						}
					}
				}
			}
		}
		return null;
	}

	public static WsnPolicyMsg decodePolicyMsg(TopicEntry topic) {
		TopicEntry tempentry = null;
		try {
			tempentry = ldap.getByDN(topic.getTopicPath());
		} catch (NamingException e) {

			e.printStackTrace();
		}
		assert tempentry != null;
		return tempentry.getWsnpolicymsg();
	}

	/**
	 * Returns a new document for the given XML string.
	 *
	 * @param xml String that represents the XML data.
	 * @return Returns a new XML document.
	 */
	public static Document parse(String xml) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			return docBuilder.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Reads the given filename into a string.
	 *
	 * @param filename Name of the file to be read.
	 * @return Returns a string representing the file contents.
	 * @throws IOException
	 */
	public static String readFile(String filename) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), "UTF-8"));
		StringBuffer result = new StringBuffer();
		String tmp = reader.readLine();

		while (tmp != null) {
			result.append(tmp + "\n");
			tmp = reader.readLine();
		}
		reader.close();
		return result.toString();
	}

	public static ArrayList<WsnPolicyMsg> getAllPolicy() {
		WsnPolicyMsg policy;
		Document doc = null;
		String fileName = POLICYMSG;
		try {
			doc = parse(readFile(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			JOptionPane.showMessageDialog(null, "读取" + fileName + "文件出错");
			return null;
		}
		Node root = doc.getDocumentElement();
		ArrayList<WsnPolicyMsg> all = new ArrayList<>();
		WsnPolicyMsgCodec codec = new WsnPolicyMsgCodec(doc);

		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			Node node = root.getChildNodes().item(i);
			policy = new WsnPolicyMsg();
			codec.decodePolicyMsg(node, policy);
			if (policy.getTargetTopic() != null && policy.getAllGroups() != null && policy.getTargetGroups().size() > 0) {
				all.add(policy);
			}
		}
		return all;
	}


	public static String getFullName(TopicEntry t) {
		String[] prefix = t.getTopicPath().split(",");
		StringBuffer topic = new StringBuffer();
		for (int i = prefix.length - 1; i >= 0; i--) {
			if (prefix[i].startsWith("ou=")) {
				if (topic.length() > 0) {
					topic.append(":");
				}
				if (prefix[i].split("=")[1].equals("all_test")) {
					topic.append("all");
				} else {
					topic.append(prefix[i].split("=")[1]);
				}
			}
		}
		return topic.toString();
	}
}
