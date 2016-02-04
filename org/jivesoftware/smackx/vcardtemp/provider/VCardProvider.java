package org.jivesoftware.smackx.vcardtemp.provider;

import android.support.v7.appcompat.BuildConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VCardProvider implements IQProvider {
    private static final Logger LOGGER;

    private static class VCardReader {
        private final Document document;
        final VCard vCard;

        VCardReader(VCard vCard, Document document) {
            this.vCard = vCard;
            this.document = document;
        }

        final void setupPhoto() {
            String binval = null;
            String mimetype = null;
            NodeList photo = this.document.getElementsByTagName("PHOTO");
            if (photo.getLength() == 1) {
                NodeList childNodes = photo.item(0).getChildNodes();
                int childNodeCount = childNodes.getLength();
                List<Node> nodes = new ArrayList(childNodeCount);
                for (int i = 0; i < childNodeCount; i++) {
                    nodes.add(childNodes.item(i));
                }
                for (Node n : nodes) {
                    String name = n.getNodeName();
                    String value = n.getTextContent();
                    if (name.equals("BINVAL")) {
                        binval = value;
                    } else if (name.equals("TYPE")) {
                        mimetype = value;
                    }
                }
                if (binval != null && mimetype != null) {
                    this.vCard.setAvatar(binval, mimetype);
                }
            }
        }

        final void setupEmails() {
            NodeList nodes = this.document.getElementsByTagName("USERID");
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    if ("WORK".equals(element.getParentNode().getFirstChild().getNodeName())) {
                        this.vCard.emailWork = getTextContent(element);
                    } else {
                        this.vCard.emailHome = getTextContent(element);
                    }
                }
            }
        }

        final void setupPhones() {
            NodeList allPhones = this.document.getElementsByTagName("TEL");
            if (allPhones != null) {
                for (int i = 0; i < allPhones.getLength(); i++) {
                    NodeList nodes = allPhones.item(i).getChildNodes();
                    String type = null;
                    String code = null;
                    String value = null;
                    for (int j = 0; j < nodes.getLength(); j++) {
                        Node node = nodes.item(j);
                        if (node.getNodeType() == (short) 1) {
                            String nodeName = node.getNodeName();
                            if ("NUMBER".equals(nodeName)) {
                                value = getTextContent(node);
                            } else if (isWorkHome(nodeName)) {
                                type = nodeName;
                            } else {
                                code = nodeName;
                            }
                        }
                    }
                    if (value != null) {
                        if (code == null) {
                            code = "VOICE";
                        }
                        if ("HOME".equals(type)) {
                            this.vCard.setPhoneHome(code, value);
                        } else {
                            this.vCard.workPhones.put(code, value);
                        }
                    }
                }
            }
        }

        private static boolean isWorkHome(String nodeName) {
            return "HOME".equals(nodeName) || "WORK".equals(nodeName);
        }

        final void setupAddresses() {
            NodeList allAddresses = this.document.getElementsByTagName("ADR");
            if (allAddresses != null) {
                for (int i = 0; i < allAddresses.getLength(); i++) {
                    int j;
                    Element addressNode = (Element) allAddresses.item(i);
                    String type = null;
                    List<String> code = new ArrayList();
                    List<String> value = new ArrayList();
                    NodeList childNodes = addressNode.getChildNodes();
                    for (j = 0; j < childNodes.getLength(); j++) {
                        Node node = childNodes.item(j);
                        if (node.getNodeType() == (short) 1) {
                            String nodeName = node.getNodeName();
                            if (isWorkHome(nodeName)) {
                                type = nodeName;
                            } else {
                                code.add(nodeName);
                                value.add(getTextContent(node));
                            }
                        }
                    }
                    for (j = 0; j < value.size(); j++) {
                        if ("HOME".equals(type)) {
                            this.vCard.setAddressFieldHome((String) code.get(j), (String) value.get(j));
                        } else {
                            String str = (String) code.get(j);
                            String str2 = (String) value.get(j);
                            this.vCard.workAddr.put(str, str2);
                        }
                    }
                }
            }
        }

        final String getTagContents(String tag) {
            NodeList nodes = this.document.getElementsByTagName(tag);
            if (nodes == null || nodes.getLength() != 1) {
                return null;
            }
            return getTextContent(nodes.item(0));
        }

        final void setupSimpleFields() {
            NodeList childNodes = this.document.getDocumentElement().getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    String field = element.getNodeName();
                    if (element.getChildNodes().getLength() == 0) {
                        this.vCard.setField(field, BuildConfig.VERSION_NAME);
                    } else if (element.getChildNodes().getLength() == 1 && (element.getChildNodes().item(0) instanceof Text)) {
                        this.vCard.setField(field, getTextContent(element));
                    }
                }
            }
        }

        private String getTextContent(Node node) {
            StringBuilder result = new StringBuilder();
            appendText(result, node);
            return result.toString();
        }

        private void appendText(StringBuilder result, Node node) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node nd = childNodes.item(i);
                String nodeValue = nd.getNodeValue();
                if (nodeValue != null) {
                    result.append(nodeValue);
                }
                appendText(result, nd);
            }
        }
    }

    static {
        LOGGER = Logger.getLogger(VCardProvider.class.getName());
    }

    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            int event = parser.getEventType();
            while (true) {
                switch (event) {
                    case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                        sb.append('<').append(parser.getName()).append('>');
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                        sb.append("</").append(parser.getName()).append('>');
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.CONFIG /*4*/:
                        sb.append(StringUtils.escapeForXML(parser.getText()));
                        break;
                }
                if (event == 3 && "vCard".equals(parser.getName())) {
                    String stringBuilder = sb.toString();
                    IQ vCard = new VCard();
                    VCardReader vCardReader = new VCardReader(vCard, DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringBuilder.getBytes("UTF-8"))));
                    vCardReader.vCard.setFirstName(vCardReader.getTagContents("GIVEN"));
                    VCard vCard2 = vCardReader.vCard;
                    vCard2.lastName = vCardReader.getTagContents("FAMILY");
                    vCard2.updateFN();
                    vCardReader.vCard.setMiddleName(vCardReader.getTagContents("MIDDLE"));
                    vCardReader.setupPhoto();
                    vCardReader.setupEmails();
                    vCardReader.vCard.organization = vCardReader.getTagContents("ORGNAME");
                    vCardReader.vCard.organizationUnit = vCardReader.getTagContents("ORGUNIT");
                    vCardReader.setupSimpleFields();
                    vCardReader.setupPhones();
                    vCardReader.setupAddresses();
                    return vCard;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            LOGGER.log(Level.SEVERE, "Exception parsing VCard", e);
        } catch (IOException e2) {
            LOGGER.log(Level.SEVERE, "Exception parsing VCard", e2);
        }
    }
}
