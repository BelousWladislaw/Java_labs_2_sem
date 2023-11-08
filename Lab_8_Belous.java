import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class Currency {
    private String code;
    private String name;
    private double rate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }
}

class CurrencyHandler extends DefaultHandler {
    private List<Currency> currencies;
    private Currency currency;
    private StringBuilder data;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("Valute")) {
            currency = new Currency();
            currency.setCode(attributes.getValue("ID"));
        }
        data = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Name")) {
            currency.setName(data.toString());
        } else if (qName.equalsIgnoreCase("Value")) {
            currency.setRate(Double.parseDouble(data.toString().replace(",", ".")));
        } else if (qName.equalsIgnoreCase("Valute")) {
            currencies.add(currency);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        currencies = new ArrayList<>();
    }
}

public class Lab_8_Belous {
    public static void main(String[] args) {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";

        // DOM parsing
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(url).openStream());
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Valute");
            List<Currency> currencies = new ArrayList<>();

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Currency currency = new Currency();
                    currency.setCode(element.getAttribute("ID"));
                    currency.setName(getTagValue("Name", element));
                    currency.setRate(Double.parseDouble(getTagValue("Value", element).replace(",", ".")));
                    currencies.add(currency);
                }
            }

            System.out.println("Currencies (DOM):");
            for (Currency currency : currencies) {
                System.out.println(currency);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        // SAX parsing
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            CurrencyHandler handler = new CurrencyHandler();
            sp.parse(new InputSource(new URL(url).openStream()), handler);
            List<Currency> currencies = handler.getCurrencies();

            System.out.println("\nCurrencies (SAX):");
            for (Currency currency : currencies) {
                System.out.println(currency);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        // StAX parsing
        try {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader reader = xif.createXMLStreamReader(new URL(url).openStream());

            List<Currency> staxCurrencies = new ArrayList<>();
            Currency staxCurrency = null;
            String elementName = "";

// StAX parsing
         while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        elementName = reader.getLocalName();

                        if (elementName.equalsIgnoreCase("Valute")) {
                            staxCurrency = new Currency();
                            staxCurrency.setCode(reader.getAttributeValue(null, "ID"));
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        String data = reader.getText().trim();

                        if (!data.isEmpty()) {
                            switch (elementName) {
                                case "Name":
                                    staxCurrency.setName(data);
                                    break;
                                case "Value":
                                    staxCurrency.setRate(Double.parseDouble(data.replace(",", ".")));
                                    break;
                            }
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        elementName = reader.getLocalName();

                        if (elementName.equalsIgnoreCase("Valute")) {
                            staxCurrencies.add(staxCurrency);
                        }
                        break;
                }
            }

            System.out.println("\nCurrencies (StAX):");
            for (Currency currency : staxCurrencies) {
                System.out.println(currency);
            }
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
//Ошибка "class XMLParsingExample is public, should be declared in a file named XMLParsingExample.java" означает, что имя файла, в котором определен класс XMLParsingExample, не совпадает с именем класса. В Java требуется, чтобы публичный класс был определен в файле с соответствующим именем.

//В данном случае, чтобы исправить ошибку, убедитесь, что имя файла, содержащего код XMLParsingExample, совпадает с именем класса и имеет расширение .java. Например, файл должен быть назван XMLParsingExample.java, а содержимое класса XMLParsingExample должно быть помещено внутрь этого файла.

//После переименования файла в соответствии с именем класса, ошибка должна исчезнуть.