package model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.corba.se.impl.orbutil.graph.Node;

public class Brain
{
	public static final double BITS_PER_DIGIT = 3.0;
	public static final Random RNG = new Random();	
	public static final String TCP_SERVER = "red.eecs.yorku.ca";
	public static final int TCP_PORT = 12345;
	public static final String DB_URL = "jdbc:derby://red.eecs.yorku.ca:64413/EECS;user=student;password=secret";
	public static final String HTTP_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/World.cgi";
	public static final String ROSTER_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/Roster.cgi";

	public Brain()
	{
		
	}
	
	public String doTime()
	{
		return new Date().toString();
	}
	
	public String doPrime(String digit) {
		int bitLength = Integer.parseInt(digit);
		BigInteger bigInteger = BigInteger.probablePrime(bitLength * 3, RNG);
		return bigInteger.toString();
		
	}
	
	public String doTcp(String digit) throws UnknownHostException, IOException {
		String prime = "";
		Socket client = new Socket(TCP_SERVER, TCP_PORT);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		
		//issue: prime digits on the socket's output stream
		out.write("PRIME " + digit);
		out.write("\r\n");
		out.flush();
		
		// read: the returned prime from the socket's input stream
		prime = in.readLine();
		
		
		// issue: bye on the socket's output stream
		out.write("bye");
		out.write("\r\n");
		out.flush();
		
		in.close();
		out.close();
		client.close();
		
		return prime;
	}
	
	
	public String doDb(String itemNo) throws Exception {
		  Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		  Connection con = DriverManager.getConnection(DB_URL);
		  Statement s = con.createStatement();
		  s.executeUpdate("set schema roumani");
		  String query = "SELECT NAME ,PRICE FROM ITEM WHERE number = '" + itemNo + "'";
		  ResultSet r = s.executeQuery(query);
		  String result = "";
		  if (r.next())
		  {
		  	result = "$" + r.getDouble("PRICE") + " - " + r.getString("NAME");
		  }
		  else
		  {
		  	throw new Exception(itemNo + " not found!");
		  }
		  r.close(); s.close(); con.close();
		  return result;
	}
	
	public String doHttp(String country, String query) throws IOException {
		
		String content = this.getHttpResponseContent(HTTP_URL + "?country=" + country + "&query=" + query );
		
		return content;
	}
	
	
	public String doRoster(String course) throws IOException, SAXException, ParserConfigurationException {
		String result = "<h1>Course Roster via HTTP</h1>\r\n" + 
				"<table border=\"1\" cellpadding=\"4\">\r\n" + 
				"<tbody><tr>\r\n" + 
				"<th>ID</th>\r\n" + 
				"<th>Last Name</th>\r\n" + 
				"<th>First Name</th>\r\n" + 
				"<th>City</th>\r\n" + 
				"<th>Program</th>\r\n" + 
				"<th>Hours</th>\r\n" + 
				"<th>GPA</th>\r\n" + 
				"</tr>";
		String xmlString = this.getHttpResponseContent(ROSTER_URL + "?course=" + course);
		
		Document xml = this.StringToXml(xmlString);
		NodeList students = xml.getElementsByTagName("students");
		if (students.getLength()>0) {
			for(int i = 0 ; i < students.getLength(); i ++) {
				org.w3c.dom.Node current_student = students.item(i);
				NodeList student_info = current_student.getChildNodes();
				result += "<tr>";
				result += "<td>" + student_info.item(0).getTextContent() + "</td>";
				result += "<td>" + student_info.item(1).getTextContent() + "</td>";
				result += "<td>" + student_info.item(2).getTextContent() + "</td>";
				result += "<td>" + student_info.item(3).getTextContent() + "</td>";
				result += "<td>" + student_info.item(4).getTextContent() + "</td>";
				result += "<td>" + student_info.item(5).getTextContent() + "</td>";
				result += "<td>" + student_info.item(6).getTextContent() + "</td>";
				result += "</tr>";
			}
		}
		
		
		result += "</tbody></table>";
		return result;
	}
	

	
	private String getHttpResponseContent(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		/*
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		*/

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();
	}
	
	
	private Document StringToXml(String string) throws SAXException, IOException, ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(string)));
		return doc;
	}
 
	
	
	
	/*
	 private static Brain instance = null;
		
		private Brain()
		{
			
		}
		
		public synchronized static Brain getInstance()
		{
			if (instance == null) instance = new Brain();
			return instance;
	} 
	  
	
	 */
}