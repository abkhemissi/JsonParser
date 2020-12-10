
public class Car {
		public String model;
		public int horsepower;
		
		public Car() {
			
		}
	
		public Car(String model, int horsepower) {
			this.model=model;
			this.horsepower=horsepower;
		}

		public String getModel() {
			return model;
		}

		public int getHorsepower() {
			return horsepower;
		}	
		
		public String toString() {
			return "horsepower " + horsepower + " model " + model;
		}

}
