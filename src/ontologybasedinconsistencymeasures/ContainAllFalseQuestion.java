package ontologybasedinconsistencymeasures;

import java.util.ArrayList;

class ContainAllFalseQuestion {

	public static boolean doesListContainAllFalse(ArrayList<String> arrayList) {
		String falseValue = "false";
		for (String str : arrayList) {
			if (!str.equals(falseValue)) {
				return false;
			}
		}
		return true;
	}

}
