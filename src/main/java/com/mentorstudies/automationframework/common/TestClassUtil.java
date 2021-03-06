package com.mentorstudies.automationframework.common;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.mentorstudies.automationframework.TestRunner;

public class TestClassUtil {

	@DataProvider(name = "defaultDP")
	public static Object[][] getTestData(Method method) {

		String methodName = method.getName();
		Object[][] allDataSet = TestRunner.testData.get(methodName);

		if (allDataSet == null) {
			return new Object[][] { {} };
		}

		Object[][] convertedDataSet = new Object[allDataSet.length][];

		int convertedSetIndex = 0;
		for (Object[] singleDataSet : allDataSet) {// Arya, 26
			int typeIndex = 0;
			Object[] finalSingleDataSet = new Object[singleDataSet.length];// [
																			// ,
																			// ]
			Class[] types = method.getParameterTypes();
			for (Class eachType : types) {// types : string, int
				String elementType = eachType.getName();
				switch (elementType) {
				case "boolean":
					boolean booleanData = Boolean.valueOf(singleDataSet[typeIndex].toString());
					finalSingleDataSet[typeIndex++] = booleanData;
					break;
				case "char":
					char charData = singleDataSet[typeIndex].toString().charAt(0);
					finalSingleDataSet[typeIndex++] = charData;
					break;
				case "double":

					break;
				case "float":

					break;
				case "int":
					int intData = Integer.parseInt(singleDataSet[typeIndex].toString());
					finalSingleDataSet[typeIndex++] = intData;
					break;
				case "java.lang.String":
					String strData = singleDataSet[typeIndex].toString();
					finalSingleDataSet[typeIndex++] = strData;
					break;
				case "long":

					break;
				default:

				}
			}
			convertedDataSet[convertedSetIndex++] = finalSingleDataSet;
		}
		return convertedDataSet;

	}

}
