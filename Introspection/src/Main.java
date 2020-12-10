import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	
	
	public static void main(String[] args) throws IllegalAccessException, Exception {

		IntrospectionMain sut = new IntrospectionMain();

		String carjson="{\"model\":\"BMW\",\"horsepower\":50}";
		
		ObjectMapper om = new ObjectMapper();
		JsonNode tree = om.readTree(carjson);
	
		Car s2 = sut.createAndFill(tree, Car.class);
		
		System.out.println(s2.getModel());
		System.out.println(s2.getHorsepower());
		
/******************************************************************************/		
		String userjson="{"
		+ "\"firstName\":\"Jean\", \"lastName\":\"Dupont\"" 
		+ ", \"cars\":["
		+ "{\"model\":\"Renaut 5\", \"horsePower\":50},"
		+ "{\"model\":\"Renaut 6\", \"horsePower\":51},"
		+ "{\"model\":\"Renaut 7\", \"horsePower\":52},"
		+ "{\"model\":\"Renaut 8\", \"horsePower\":53}"
		+ "]"
		+ "}";

		
		ObjectMapper om2 = new ObjectMapper();
		JsonNode tree2 = om2.readTree(userjson);
		
		User u1 = sut.createAndFill(tree2, User.class);
		System.out.println(u1.toString());
		
//		System.out.println(tree2.get("cars").get(1));
		
/******************************************************************************/
		
		ObjectMapper objectMapper = new ObjectMapper();
		Car c = objectMapper.readValue(carjson, Car.class);
//		System.out.println(c.getModel());

		
	}

}
