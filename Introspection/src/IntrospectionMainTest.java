import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

// Test Driven Development 
public class IntrospectionMainTest {

	// System Under Test = sut
	IntrospectionMain sut = new IntrospectionMain();

	@Test
	public void testDumpNullToJson() throws Exception {
		// given
		// when 
		String jsonText = sut.dumpObjectToJson(null);
		// then
		Assert.assertEquals("null", jsonText);
	}

	@Test
	public void testDumpStringToJson() throws Exception {
		// given
		// when 
		String jsonText = sut.dumpObjectToJson("text");
		// then
		Assert.assertEquals("\"text\"", jsonText);
	}


	@Test
	public void testDumpIntToJson() throws Exception {
		// given
		// when 
		String jsonText = sut.dumpObjectToJson(50);
		// then
		Assert.assertEquals("50", jsonText);
	}

	@Test
	public void testDumpObjectToJson() throws Exception {
		// given
		User u = new User();
		u.firstName = "Jean";
		u.lastName = "Dupont";
		
		// when 
		String jsonText = sut.dumpObjectToJson((Object) u);

		// then
		Assert.assertEquals("{\"firstName\":\"Jean\", \"lastName\":\"Dupont\", \"cars\":null}", jsonText);
	}
	
	public static class User {
		public String firstName;
		private String lastName;
		public List<Car> cars;

		public String getLastName() {
			return lastName;
		}
	}
	
	@Test
	public void testDumpObjectToJson_Car() throws Exception {
		// given
		Car c = new Car();
		c.model = "Renaut 5";
		c.horsePower = 50;
		
		// when 
		String jsonText = sut.dumpObjectToJson((Object) c);

		// then
		Assert.assertEquals("{\"model\":\"Renaut 5\", \"horsePower\":50}", jsonText);
	}

	@Test
	public void testDumpObjectToJson_Car_nullField() throws Exception {
		// given
		Car c = new Car();
		c.model = null;
		c.horsePower = 50;
		
		// when 
		String jsonText = sut.dumpObjectToJson((Object) c);

		// then
		Assert.assertEquals("{\"model\":null, \"horsePower\":50}", jsonText);
	}

	public static class Car {
		public String model;
		public int horsePower;
	}

	
	@Test
	public void testDumpObjectToJson_fieldList() throws Exception {
		// given
		User u = new User();
		u.firstName = "Jean";
		u.lastName = "Dupont";
		u.cars = new ArrayList<>();
		Car c = new Car();
		c.model = "Renaut 5";
		c.horsePower = 50;
		u.cars.add(c);
		
		// when 
		String jsonText = sut.dumpObjectToJson((Object) u);

		// then
		Assert.assertEquals("{"
				+ "\"firstName\":\"Jean\", \"lastName\":\"Dupont\"" 
				+ ", \"cars\":["
				+ "{\"model\":\"Renaut 5\", \"horsePower\":50}"
				+ "]"
				+ "}", jsonText);
	}
	
	@Test
	public void testDumpListObjectToJson() throws Exception {
		// given
		List<Car> cars = new ArrayList<>();
		Car c = new Car();
		c.model = "Renaut 5";
		c.horsePower = 50;
		cars.add(c);
		
		// when 
		String jsonText = sut.dumpObjectToJson((Object) cars);

		// then
		Assert.assertEquals(
				"["
				+ "{\"model\":\"Renaut 5\", \"horsePower\":50}"
				+ "]", jsonText);
	}
	
	@Test
	public void testDumpJsontToObject() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = "{\"model\":\"Renaut 5\", \"horsePower\":\"50\"}";
		
		Car car = objectMapper.readValue(json, Car.class);
		
	}
	
	
	
}
