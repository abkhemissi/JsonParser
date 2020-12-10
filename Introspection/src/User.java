import java.util.ArrayList;
import java.util.List;

public class User {
	
	public String firstName;
	private String lastName;
	private ArrayList<Car> cars;
	
	public User() {
		
	}
	public User(String fn, String ln,ArrayList<Car> cars ) {
		firstName=fn;
		lastName= ln;
		cars=cars;
	}
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public ArrayList<Car> getCars() {
		return cars;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(firstName + " " + lastName + " possède ces voitures : "+"\n");
		for (Car c: cars) {
			sb.append(c.toString()+ "\n");
		}
		return sb.toString();
	}
	
	

}
