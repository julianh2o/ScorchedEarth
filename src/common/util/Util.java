package common.util;

//this class provides some handy utilities that are used throughout for displaying information

import java.util.Calendar;

public class Util {
	public static String getHexString(String s) { //displays a string spaced out with hex for non-displayable characters this is used by logging
		String out1 = "";
		byte[] bytes = s.getBytes();
		for (int i=0; i<bytes.length; i++) {
			String c = Integer.toHexString((int)bytes[i]);
			if (c.length()<2) c = "0" + c;
			if (bytes[i]>=33 && bytes[i]<=126) {
				c = "" + (char)(bytes[i]);
			}
			while (c.length()<2) c = " " + c;
			out1 += c + " ";
		}
		return out1;
	}
	
	public static String stamp() { //formats a timestamp
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"."+c.get(Calendar.MILLISECOND);
	}

	public static double timeScale(double x, long ms) {
		return x*(ms/1000.0);
	}
	
//	public static Vector2D timeScale(Vector2D v, long ms) {
//		return new Vector2D(timeScale(v.getX(),ms),timeScale(v.getY(),ms));
//	}
}