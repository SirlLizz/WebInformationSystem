package com.example.webinformationsystem.bean;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.connection.JDBCUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
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
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
                customer.appendChild(getElement(document, customer, "id", resultSet.getString("ID")));
                customer.appendChild(getElement(document, customer, "name", resultSet.getString("NAME")));
                customer.appendChild(getElement(document, customer, "phoneNumber", resultSet.getString("TELEPHONE")));
                customer.appendChild(getElement(document, customer, "address", resultSet.getString("ADDRESS")));
                rootElement.appendChild(customer);
            }
            document.appendChild(rootElement);

            validateCustomerFile(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            DOMSource source = new DOMSource(document);
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformerFactory.newTransformer().transform(source, new StreamResult(XML_FILE));
            transformerFactory.newTransformer(new StreamSource(XSLT_FILE)).transform(source, new StreamResult(HTML_FILE));
            return 1;

        } catch (SQLException | ParserConfigurationException | TransformerException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int replaceXMLDataCustomer(String file) {
        try (Connection connection = jdbcUtils.getConnection()) {

            connection.prepareStatement("TRUNCATE TABLE ORDERS").executeUpdate();
            connection.prepareStatement("TRUNCATE TABLE CUSTOMERS").executeUpdate();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(file.trim().replaceFirst("^([\\W]+)<","<"))));

            validateCustomerFile(document);

            NodeList nodeList = document.getElementsByTagName("customer");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    addCustomer(element);
                }
            }
            return 1;
        } catch (SQLException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int addXMLDataCustomer(String file) {
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(file.trim().replaceFirst("^([\\W]+)<","<"))));

            validateCustomerFile(document);

            NodeList nodeList = document.getElementsByTagName("customer");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if(!checkCustomerByID(getTagValue("id", element))){
                        addCustomer(element);
                    }
                }
            }
            return 1;
        } catch (SQLException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void validateCustomerFile(Document document) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(XSD_FILE));
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
    }

    private Node getElement(Document document, Element element, String name, String value) {
        Element node = document.createElement(name);
        node.appendChild(document.createTextNode(value));
        return node;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    private void addCustomer(Element element) throws SQLException {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CUSTOMERS VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, getTagValue("id", element));
        preparedStatement.setString(2, getTagValue("name", element));
        preparedStatement.setString(3, getTagValue("phoneNumber", element));
        preparedStatement.setString(4, getTagValue("address", element));
        preparedStatement.executeUpdate();
    }

    private Boolean checkCustomerByID(String customerID){
        try (Connection connection = jdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMERS where ID = '" + customerID + "'");
            List<String> customersID = new ArrayList<>();
            while (resultSet.next()) {
                customersID.add(resultSet.getString("ID"));
            }
            if(customersID.size() != 0){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
