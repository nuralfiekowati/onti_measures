package ontologybasedinconsistencymeasures;

class FactorialCount {

	public static long factorial(int number) {
		int i;
		long fact = 1;
		for (i = 1; i <= number; i++) {
			fact = fact * i;
		}
		return fact;
	}

}
