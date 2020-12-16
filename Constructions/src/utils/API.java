package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;

/**
 * Class cung cap cac phuong thuc giup gui request len server va nhan du lieu tra ve
 * @author TTMT
 * Date: 16/12/2020
 * @version 1.0
 */
public class API {

	/**
	 * thuoc tinh giup format ngay thang theo dinh dang
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/**
	 * thuoc tinh giup log thong tin ra console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * phuong thuc giup goi cac api dang GET
	 * @param url: duong dan toi server can request
	 * @param token: doan ma bam can cung cap de xac thuc nguoi dung
	 * @return response: phan hoi tu sever
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		
		// setup connection
		HttpURLConnection conn = setupConnection(line_api_url, token, "GET"); 	
		
		// read response
		return readResponse(conn);
	}

	int var;

	/**
	 * phuong thuc giup goi cac api dang POST
	 * @param url: duong dan toi server
	 * @param data: du lieu dua len server de xu ly
	 * @return: phan hoi tu server (string)
	 * @throws IOException
	 */
	public static String post(String url, String data
//			, String token
	) throws IOException {
		allowMethods("PATCH");
		URL line_api_url = new URL(url);
		String payload = data;
		LOGGER.info("Request Info:\nRequest URL: " + url + "\n" + "Payload Data: " + payload + "\n");
		
		// setup connection
		HttpURLConnection conn = setupConnection(line_api_url, null, "POST"); 
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(payload);
		writer.close();
		
		return readResponse(conn);
	}
	
	/**
	 * phuong thuc giup doc dw lieu tra ve tu server
	 * @param conn: connection to server
	 * @return response: phan hoi tu server dang string
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder respone = new StringBuilder(); // ising StringBuilder for the sake of memory and performance
		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		respone.append(inputLine + "\n");
		in.close();
		LOGGER.info("Respone Info: " + respone.substring(0, respone.length() - 1).toString());
		return respone.substring(0, respone.length() - 1).toString();
	}

	/**
	 * phuong thuc giup thiet lap ket noi toi server
	 * @param line_api_url: duong dan toi server
	 * @param token: doan ma bam can cung cap de xac thuc nguoi dung
	 * @param method
	 * @return connection
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static HttpURLConnection setupConnection(URL line_api_url, String token, String method)
			throws IOException, ProtocolException {
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		return conn;
	}

	/**
	 * phuong thuc cho phep goi cac loai giao thuc API khac nhau 
	 * @param methods: giao thuc can cho phep
	 * @deprecated chi hoat dong voi java <= 11
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
