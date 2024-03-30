package weatherPack;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class WeatherApp
 */
public class WeatherApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherApp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String apiKey = "d942f99943dc800d938e98c155724c4b";
		String city = request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
		try {
		URL url = new URL(apiUrl);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		InputStream input = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(input);
		Scanner sc =new Scanner(reader);
		StringBuilder responseContent = new StringBuilder();
		while(sc.hasNext()) {
			responseContent.append(sc.next());
			
			}
		sc.close();
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		System.out.println(jsonObject);
		
		long dateTimeStamp =jsonObject.get("dt").getAsLong()*1000;
		String date = new Date(dateTimeStamp).toString();
		
		double tempKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempCelcius = (int)(tempKelvin-273.15);
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		double WindSpeed =jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
		JsonObject firstWeatherObject = weatherArray.get(0).getAsJsonObject();
		String weatherCondition = firstWeatherObject.get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", tempCelcius);
		request.setAttribute("weatherCondition",weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("WindSpeed", WindSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
	}catch(IOException e) {
	e.printStackTrace();	
	}
		request.getRequestDispatcher("index.jsp").forward(request,response);

	}

}