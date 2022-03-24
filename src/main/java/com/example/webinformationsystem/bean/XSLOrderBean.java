package com.example.webinformationsystem.bean;

import com.example.webinformationsystem.connection.JDBCConnection;
import com.example.webinformationsystem.connection.JDBCUtils;
import com.example.webinformationsystem.model.Customer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
import java.util.UUID;

@Stateless
@LocalBean
public class XSLOrderBean {

    private static JDBCUtils jdbcUtils = JDBCConnection.getJDBCUtils();
    private File XML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\xmlData\\orders.xml");
    private File HTML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\webapp\\xlst-order.html");
    private File XSLT_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xslt\\order.xslt");
    private File XSD_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xsd\\order.xsd");

    public int createXSLOrder() {
        try (Connection connection = jdbcUtils.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ORDERS");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.newDocument();

            Element rootElement = document.createElement("orders");
            while (resultSet.next()) {
                Element order = document.createElement("order");
                order.appendChild(getElement(document, order, "id", resultSet.getString("ID")));
                order.appendChild(getCustomerElement(document, order, getCustomerByID(resultSet.getString("CUSTOMER"))));
                order.appendChild(getElement(document, order, "date", resultSet.getDate("DATA").toString()));
                order.appendChild(getElement(document, order, "price", resultSet.getString("PRICE")));
                rootElement.appendChild(order);
            }
            document.appendChild(rootElement);

            validateOrderFile(document);

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

    public int replaceXMLDataOrder(String file) {
        try (Connection connection = jdbcUtils.getConnection()) {

            connection.prepareStatement("TRUNCATE TABLE ORDERS").executeUpdate();
            connection.prepareStatement("TRUNCATE TABLE CUSTOMERS").executeUpdate();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(file.trim().replaceFirst("^([\\W]+)<","<"))));

            validateOrderFile(document);

            NodeList nodeList = document.getElementsByTagName("order");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node order = nodeList.item(i);
                if (order.getNodeType() == Node.ELEMENT_NODE) {
                    Element orderElement = (Element) order;
                    Element customerElement = (Element) ((Element) order).getElementsByTagName("customer").item(0);
                    addCustomer(customerElement);
                    addOrder(orderElement, getTagValue("id", customerElement));
                }
            }
            return 1;
        } catch (SQLException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int addXMLDataOrder(String file) {
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(file.trim().replaceFirst("^([\\W]+)<","<"))));

            validateOrderFile(document);

            NodeList nodeList = document.getElementsByTagName("order");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node order = nodeList.item(i);
                if (order.getNodeType() == Node.ELEMENT_NODE) {
                    Element orderElement = (Element) order;
                    Element customerElement = (Element) ((Element) order).getElementsByTagName("customer").item(0);
                    if(!checkOrderByID(getTagValue("id", orderElement))){
                        if(!checkCustomerByID(getTagValue("id", customerElement))){
                            addCustomer(customerElement);
                            addOrder(orderElement, getTagValue("id", customerElement));
                        }else{
                            addOrder(orderElement, getTagValue("id", customerElement));
                        }
                    }
                }
            }
            return 1;
        } catch (SQLException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void validateOrderFile(Document document) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(XSD_FILE));
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
    }

    private Node getCustomerElement(Document document, Element order, Customer customer) {
        Element customerElement = document.createElement("customer");
        customerElement.appendChild(getElement(document, customerElement, "id", customer.getCustomerID()));
        customerElement.appendChild(getElement(document, customerElement, "name", customer.getName()));
        customerElement.appendChild(getElement(document, customerElement, "phoneNumber", customer.getPhoneNumber()));
        customerElement.appendChild(getElement(document, customerElement, "address", customer.getAddress()));
        order.appendChild(customerElement);
        return customerElement;
    }

    private Customer getCustomerByID(String customerID){
        try (Connection connection = jdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMERS where ID = '" + customerID + "'");
            resultSet.next();
            Customer customer = new Customer();
            customer.setCustomerID(UUID.fromString(resultSet.getString("ID")));
            customer.setName(resultSet.getString("NAME"));
            customer.setPhoneNumber(resultSet.getString("TELEPHONE"));
            customer.setAddress(resultSet.getString("ADDRESS"));
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    private void addOrder(Element element, String customerId) throws SQLException {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ORDERS VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, getTagValue("id", element));
        preparedStatement.setString(2, customerId);
        preparedStatement.setObject(3, getTagValue("date", element), Types.DATE);
        preparedStatement.setDouble(4, Double.parseDouble(getTagValue("price", element)));
        preparedStatement.executeUpdate();
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

    private Boolean checkOrderByID(String orderID){
        try (Connection connection = jdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ORDERS where ID = '" + orderID + "'");
            List<String> ordersID = new ArrayList<>();
            while (resultSet.next()) {
                ordersID.add(resultSet.getString("ID"));
            }
            if(ordersID.size() != 0){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
