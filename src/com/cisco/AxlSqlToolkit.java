package com.cisco;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
//import javax.xml.messaging.URLEndpoint;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.parsers.DOMParser;
//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.InputSource;

/**
 */
public class AxlSqlToolkit {
    /** Defines the connection to AXL for the test driver */
    private SOAPConnection con;

    /** DOCUMENT ME! */
    private String port = "8443";

    /** DOCUMENT ME! */
    private String host = "localhost";

    private String username = null;

    private String password = null;
    
    /**  */
    private String outputFile = "sample.response";
    private String inputFile = "sample.xml";
    
    private String currentStatement = null;

    /**
     * This method provides the ability to initialize the SOAP connection
     */
    public void init() {
        try {
            X509TrustManager xtm = new MyTrustManager();
            TrustManager[] mytm = { xtm };
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, mytm, null);
            SSLSocketFactory sf = ctx.getSocketFactory();

            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            con = scf.createConnection();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method provides the ability to convert a SOAPMessage to a String
     *
     * @param inMessage SOAPMessage
     * @return String The string containing the XML of the SOAPMessage
     * @throws Exception Indicates a problem occured during processing of the SOAPMessage
     */
    public static String convertSOAPtoString(SOAPMessage inMessage) throws Exception {
        Source source = inMessage.getSOAPPart().getContent();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream myOutStr = new ByteArrayOutputStream();
        StreamResult res = new StreamResult();
        res.setOutputStream(myOutStr);
        transformer.transform(source, res);
        return myOutStr.toString().trim();
    }

    /**
     */
    private SOAPMessage createSqlMessage(String soapstring) throws Exception {
        // Add a soap body element to the soap body
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage soapMessage = mf.createMessage(new MimeHeaders(),  
                new ByteArrayInputStream(soapstring.getBytes(Charset.forName("UTF-8"))));  
        return soapMessage;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getUrlEndpoint() {
        return new String("https://" + username + ":" + password + "@" + host + ":" + port + "/axl/");
    }

    /**
     * This method provides the ability to send a specific SOAPMessage
     * 
     * @param requestMessage The message to send
     */
    public SOAPMessage sendMessage(String soapstring) throws Exception {
    	SOAPMessage requestMessage = createSqlMessage(soapstring);
    	SOAPMessage reply = null;
        try {
            System.out.println("*****************************************************************************");
            System.out.println("Sending message...");
            System.out.println("---------------------");
            requestMessage.writeTo(System.out);
            System.out.println("\n---------------------");

            reply = con.call(requestMessage, getUrlEndpoint());

            if (reply != null) {
                //Check if reply includes soap fault
                SOAPPart replySP = reply.getSOAPPart();
                SOAPEnvelope replySE = replySP.getEnvelope();
                SOAPBody replySB = replySE.getBody();

                if (replySB.hasFault()) {
                    System.out.println("ERROR: " + replySB.getFault().getFaultString());
                }
                else {
                    System.out.println("Positive response received.");
                }
                System.out.println("---------------------");
                reply.writeTo(System.out);
                FileWriter fw = new FileWriter(outputFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("---------------------------- " + currentStatement + " ----------------------------");
                bw.newLine();
                bw.write(convertSOAPtoString(reply));
                bw.newLine();
                bw.flush();
                bw.close();
                System.out.println("\n---------------------");
            }
            else {
                System.out.println("No reply was received!");
                System.out.println("---------------------");
            }

            System.out.println("");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return reply;
    }

    /*
    public class GenericNodeFilter implements NodeFilter {
        String theNodeName = null;

        public GenericNodeFilter(String _nodeName) {
            theNodeName = _nodeName;
        }

        public short acceptNode(Node _node) {
            if (_node.getNodeName().equals(theNodeName)) {
                return NodeFilter.FILTER_ACCEPT;
            }
            else {
                return NodeFilter.FILTER_REJECT;
            }
        }
    } */

    /**
     * This method provides the main method for the class
     * 
     * @param args Standard Java PSVM arguments
     */
    public static void main(String[] args) {
       
    }

    /**
     * DOCUMENT ME!
     * 
     * @param args DOCUMENT ME!
     */
    public void parseArgs(String loginuser,String loginpassword, String port, String host) {
        
    	this.username = loginuser;
    	this.password = loginpassword;
    	this.port = port;
    	this.host = host;
    	 
    }

    /* testing the SSL interface */
    public class MyTrustManager implements X509TrustManager {
        MyTrustManager() {}
        public void checkClientTrusted(X509Certificate chain[], String authType) throws CertificateException {}
        public void checkServerTrusted(X509Certificate chain[], String authType) throws CertificateException {}
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    
    /*
    public static String DeciTohex(int d) {
         int rem;
         String output = "";
         String digit;
         String backwards = "";

         do {
             rem = d % 16;
             digit = DtoHex(rem);
             d = d / 16;
             output += digit;
         }
         while (d / 16 != 0);

         rem = d % 16;

         digit = DtoHex(rem);

         output = output + digit;

         for (int i = output.length() - 1; i >= 0; i--) {
             backwards += output.charAt(i);
         }

         return backwards;
     }

     public static String DtoHex(int rem) {

         String str1 = String.valueOf(rem);

         if (str1.equals("10"))
             str1 = "A";

         else if (str1.equals("11"))
             str1 = "B";

         else if (str1.equals("12"))
             str1 = "C";

         else if (str1.equals("13"))
             str1 = "D";

         else if (str1.equals("14"))
             str1 = "E";

         else if (str1.equals("15"))
             str1 = "F";

         else
             str1 = str1;

         return str1;
     } */
    
    public String getSoapstring(String user,String password) {
		String soapstring = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/9.1\">";
		soapstring ="<soapenv:Header/><soapenv:Body><ns:updateUser sequence=\"?\">" +
					"<userid>user1</userid>" + 
					"<lastName>%s</lastName>" +
					"<password>%s</password>" +
					"</ns:updateUser>" +
					"</soapenv:Body>" +
					"</soapenv:Envelope>";
		soapstring = String.format(soapstring, user,password);
		System.out.println("soap string :" + soapstring);
		return soapstring;
		
	}
}
