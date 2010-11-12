package client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import common.util.Log;

public class OSUtil {
	public static String getJarDir() {
        String dir = "";
        try {
            dir = new File(OSUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            Log.p.out("Got dir: "+dir);
        } catch (URISyntaxException uriEx) {
            try {
                // Try to resort to current dir. May still fail later due to bad start dir.
                uriEx.printStackTrace();
                dir = new File(".").getCanonicalPath();
            } catch (IOException ioEx) {
                // Completely failed
                System.out.println("Unable to find jar directory, try launching in another way!");
                ioEx.printStackTrace();
                return null;
            }
        }
        return dir;
	}
	
	public static String getOS() {
		String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return "windows";
        } else if (osName.startsWith("Linux") || osName.startsWith("FreeBSD")) {
            return "linux";
        } else if (osName.startsWith("Mac OS X")) {
            return "macosx";
        } else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
             return "solaris";
        } else {
            System.out.println("Unsupported OS: " + osName + ". Exiting.");
            return null;
        }
	}

	public static void setLibraryPath() throws Exception {
		String appdir = OSUtil.getJarDir();
		String os = OSUtil.getOS();
		if (os == null || appdir == null) throw new Exception("Failed to set library path");
		String libraryPath = appdir + File.separator + "lib" + File.separator + "native" + File.separator + os;
		System.setProperty("org.lwjgl.librarypath", libraryPath);
	}
}
