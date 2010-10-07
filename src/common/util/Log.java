package common.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {
	public static Log p; // primary log
	public static final Log CLIENT = new Log("server.log");
	public static final Log SERVER = new Log("client.log");

	public static void setPrimary(Log p) {
		Log.p = p;
	}

	private String logfile;
	public Log(String logfile) {
		this.logfile = logfile;
	}

	public void out(String s) {
		write(Util.stamp()+" "+s);
	}

	public void error(String s, Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		write(s+"\n"+sw.toString());
	}
	
	private BufferedWriter openLog() throws IOException {
		return new BufferedWriter(new FileWriter(logfile,true));
	}

	private void write(String s) {
		System.out.println(s);
		try {
			BufferedWriter bw = openLog();
			bw.write(s+"\n");
			bw.close();
		} catch (Exception e) {
			System.err.println("Error writing to log file: "+e.getMessage());
			e.printStackTrace();
		}
	}
}
