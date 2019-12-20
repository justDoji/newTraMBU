package be.doji.productivity.trambu.messages;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public final class XmlFormatter {

  public static String prettyPrint(String input) {
    return prettyPrint(input, 2);
  }

  public static String prettyPrint(String input, int indent) {
    try {
      // Turn xml string into a document
      Document document = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder()
          .parse(new InputSource(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));

      // Remove whitespaces outside tags
      document.normalize();
      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
          document,
          XPathConstants.NODESET);

      for (int i = 0; i < nodeList.getLength(); ++i) {
        Node node = nodeList.item(i);
        node.getParentNode().removeChild(node);
      }

      // Setup pretty print options
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer
          .setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));

      // Return pretty print xml string
      StringWriter stringWriter = new StringWriter();
      transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
      return stringWriter.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
