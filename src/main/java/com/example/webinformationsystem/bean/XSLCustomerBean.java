package com.example.webinformationsystem.bean;

import com.example.webinformationsystem.connection.HibernateConnection;
import com.example.webinformationsystem.model.Customer;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
import java.util.List;


@Stateless
@LocalBean
public class XSLCustomerBean {

    private File XML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\xmlData\\customers.xml");
    private File HTML_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\webapp\\xlst-customer.html");
    private File XSLT_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xslt\\customer.xslt");
    private File XSD_FILE = new File("C:\\Users\\Ilya\\Desktop\\Kracker\\WebInformationSystem\\src\\main\\java\\com\\example\\webinformationsystem\\reference\\xsd\\customer.xsd");

    public int createXSLCustomer(){
        try (Session session = HibernateConnection.getSession()) {

            Query query = session.createQuery("FROM Customer");
            List<Customer> customers = (List<Customer>) query.list();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.newDocument();

            Element rootElement = document.createElement("customers");
            for (int i = 0; i < customers.size(); i++){
                Element customer = document.createElement("customer");
                customer.appendChild(getElement(document, customer, "id", customers.get(i).getCustomerID()));
                customer.appendChild(getElement(document, customer, "name", customers.get(i).getName()));
                customer.appendChild(getElement(document, customer, "phoneNumber", customers.get(i).getPhoneNumber()));
                customer.appendChild(getElement(document, customer, "address", customers.get(i).getAddress()));
                rootElement.appendChild(customer);
            }
            document.appendChild(rootElement);

            validateCustomerFile(document);

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

    public int replaceXMLDataCustomer(String file) {
        try (Session session = HibernateConnection.getSession()) {

            session.beginTransaction();

            session.createQuery("DELETE FROM Customer ").executeUpdate();
            session.createQuery("DELETE FROM Order ").executeUpdate();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(file.trim().replaceFirst("^([\\W]+)<","<"))));

            validateCustomerFile(document);

            NodeList nodeList = document.getElementsByTagName("customer");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Customer customer = getCustomer(element);
                    session.save(customer);
                }
            }
            session.getTransaction().commit();
            return 1;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int addXMLDataCustomer(String file) {
        try(Session session = HibernateConnection.getSession()){

            session.beginTransaction();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(file.trim().replaceFirst("^([\\W]+)<","<"))));

            validateCustomerFile(document);

            NodeList nodeList = document.getElementsByTagName("customer");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if(!checkCustomerByID(getTagValue("id", element), session)){
                        Customer customer = getCustomer(element);
                        session.save(customer);
                    }
                }
            }
            return 1;
        } catch (ParserConfigurationException | IOException | SAXException e) {
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

    private Customer getCustomer(Element element) {
        Customer customer = new Customer();
        customer.setCustomerID(getTagValue("id", element));
        customer.setName(getTagValue("name", element));
        customer.setPhoneNumber(getTagValue("phoneNumber", element));
        customer.setAddress(getTagValue("address", element));
        return customer;
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
