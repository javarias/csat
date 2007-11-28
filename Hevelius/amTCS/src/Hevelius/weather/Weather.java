package Hevelius.weather;

import java.io.*;
import java.util.regex.*;
import java.net.*;
import java.util.*;
public class Weather {

	public static Vector<WeatherCityId> vector_city;
	public static Vector<WeatherCityCondition> vector_condition;
	public static void main(String[] args) {
		BufferedReader lugar;
		BufferedReader id;
		String cadena;
		String tmp;
		Pattern pat;  
		Matcher mat;
		int i;
		try
		{
			System.out.println("Ingrese la ciudad que desea revisar");
			lugar=new BufferedReader(new InputStreamReader(System.in));
			tmp=lugar.readLine();
			WeatherCity weather=  new WeatherCity(tmp);
			vector_city = new Vector<WeatherCityId>();
			vector_city=weather.ListCity();
			int n= vector_city.size();
			for(i=1;i<n;i++)
			{
			System.out.println(vector_city.get(i).getNameCity()+" "+vector_city.get(i).getId());
			}
			System.out.println("Ingrese el ID de la ciudad que desea");
			id=new BufferedReader(new InputStreamReader(System.in));
			tmp=id.readLine();
			tmp=tmp.trim();
			WeatherCondition condition = new WeatherCondition(tmp);
			vector_condition = new Vector<WeatherCityCondition>();
			vector_condition = condition.ListCityCondition();
			int k= vector_condition.size();
			System.out.println(k);
				System.out.println("lalaalal");
				System.out.println(vector_condition.get(0).getLat()+" "+vector_condition.get(0).getLon());



		}
		 catch (UnknownHostException e )
	        {
        	        System.out.println("false");
        	}
		catch (Exception e) 
		{
			e.printStackTrace(); 
		}
		}


	}
