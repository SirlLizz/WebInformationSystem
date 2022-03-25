package com.example.webinformationsystem.bean;

import com.example.webinformationsystem.connection.HibernateConnection;
import com.example.webinformationsystem.model.Customer;
import com.example.webinformationsystem.model.Order;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
import java.util.List;

@Stateless
@LocalBean
public class XSLOrderBean {

    private File XML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\xmlData\\orders.xml");
    private File HTML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\webapp\\xlst-order.html");
    private File XSLT_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xslt\\order.xslt");
    private File XSD_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xsd\\order.xsd");

    public int createXSLOrder() {
        try (Session session = HibernateConnection.getSession()) {

            Query query = session.createQuery("FROM Order");
            List<Order> orders = (List<Order>) query.list();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.newDocument();

            Element rootElement = document.createElement("orders");
            for (int i = 0; i < orders.size(); i++){
                Element order = document.createElement("order");
                order.appendChild(getElement(document, order, "id", orders.get(i).getOrderID()));
                order.appendChild(getCustomerElement(document, order, orders.get(i).getCustomer()));
                order.appendChild(getElement(document, order, "date", orders.get(i).getOrderDate().toString()));
                order.appendChild(getElement(document, order, "price", Double.toString(orders.get(i).getOrderPrice())));
                rootElement.appendChild(order);
            }
            document.appendChild(rootElement);

            validateOrderFile(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            DOMSource source = new DOMSource(document);
            transformerFactory.newTransformer().transform(source, new StreamResult(XML_FILE));
            transformerFactory.newTransformer(new StreamSource(XSLT_FILE)).transform(source, new StreamResult(HTML_FILE));
            return 1;

        } catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public int replaceXMLDataOrder(String file) {
        try (Session session = HibernateConnection.getSession()) {

            session.beginTransaction();
            session.createQuery("DELETE FROM Customer ").executeUpdate();
            session.createQuery("DELETE FROM Order ").executeUpdate();

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
                    Customer customer = getCustomer(customerElement);
                    session.save(customer);
                    addOrder(orderElement, customer, session);
                }
            }
            session.getTransaction().commit();
            return 1;
        } catch (SQLException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int addXMLDataOrder(String file) {
        try(Session session = HibernateConnection.getSession()){

            session.beginTransaction();
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
                    if(!checkOrderByID(getTagValue("id", orderElement), session)){
                        if(!checkCustomerByID(getTagValue("id", customerElement), session)){
                            Customer customer = getCustomer(customerElement);
                            session.save(customer);
                            addOrder(orderElement, customer, session);
                        }else{
                            Customer customer = getCustomer(customerElement);
                            addOrder(orderElement, customer, session);
                        }
                    }
                }
            }
            session.getTransaction().commit();
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

    private void addOrder(Element element, Customer customer, Session session) throws SQLException {
        Order order = new Order();
        order.setOrderID(getTagValue("id", element));
        order.setCustomer(customer);
        order.setOrderDate(Date.valueOf(getTagValue("date", element)));
        order.setOrderPrice(Double.parseDouble(getTagValue("price", element)));
        session.save(order);
    }

    private Customer getCustomer(Element element) {
        Customer customer = new Customer();
        customer.setCustomerID(getTagValue("id", element));
        customer.setName(getTagValue("name", element));
        customer.setPhoneNumber(getTagValue("phoneNumber", element));
        customer.setAddress(getTagValue("address", element));
        return customer;
    }

    private Boolean checkOrderByID(String orderID, Session session){
        Query query = session.createQuery("select id from Order where id = :orderID");
        query.setParameter("orderID", orderID);
        if (query.list().isEmpty()){
            return true;
        }
        return false;
    }

    private Boolean checkCustomerByID(String customerID, Session session){
        Query query = session.createQuery("select id from Customer where id = :customerID");
        query.setParameter("customerID", customerID);
        if (query.list().isEmpty()){
            return true;
        }
        return false;
    }
}
