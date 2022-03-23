package com.example.webinformationsystem.bean;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.connection.JDBCUtils;
import com.example.webinformationsystem.model.Customer;
import com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.sql.query.OracleXMLQuery;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.ejb.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


@Stateless
@LocalBean
public class XSLCustomerBean {

    private static JDBCUtils jdbcUtils = JDBCConnection.getJDBCUtils();
    private File XML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\xmlData\\customers.xml");
    private File HTML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\webapp\\xlst-customer.html");
    private File XSLT_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xslt\\customer.xslt");
    private File XSD_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xsd\\customer.xsd");

    public int createXSLCustomer(){
        try (Connection connection = jdbcUtils.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMERS");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.newDocument();

            Element rootElement = document.createElement("customers");
            while (resultSet.next()) {
                Element customer = document.createElement("customer");
                customer.appendChild(getElements(document, customer, "id", resultSet.getString("ID")));
                customer.appendChild(getElements(document, customer, "name", resultSet.getString("NAME")));
                customer.appendChild(getElements(document, customer, "phoneNumber", resultSet.getString("TELEPHONE")));
                customer.appendChild(getElements(document, customer, "address", resultSet.getString("ADDRESS")));
                rootElement.appendChild(customer);
            }
            document.appendChild(rootElement);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(XSD_FILE));
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            DOMSource source = new DOMSource(document);
            transformerFactory.newTransformer().transform(source, new StreamResult(XML_FILE));
            transformerFactory.newTransformer(new StreamSource(XSLT_FILE)).transform(source, new StreamResult(HTML_FILE));
            return 1;

        } catch (SQLException | ParserConfigurationException | TransformerException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Node getElements(Document document, Element element, String name, String value) {
        Element node = document.createElement(name);
        node.appendChild(document.createTextNode(value));
        return node;
    }

}
