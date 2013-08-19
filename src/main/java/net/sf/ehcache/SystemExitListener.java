package net.sf.ehcache;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import java.io.*;

public class SystemExitListener extends RunListener {

	public static String TARGET_FOLDER_LOCATION;
	static {
		String currentFolderPath = new File(".").getAbsolutePath();
		int targetLastIndex = currentFolderPath.lastIndexOf("target");
		if (targetLastIndex != -1) {
			TARGET_FOLDER_LOCATION = currentFolderPath.substring(0,
					targetLastIndex)
					+ File.separator
					+ "target"
					+ File.separator;
		} else {
			TARGET_FOLDER_LOCATION = new File("target").getAbsolutePath()
					+ File.separator;
		}
	}

	public static final String SYSTEM_EXIT_LISTENER_RESULT = TARGET_FOLDER_LOCATION
			+ "surefire-reports" + File.separator;

	@Override
	public void testStarted(Description description) throws Exception {
		StringBuilder text = new StringBuilder();
		String className = description.getClassName();
		String descriptionDisplayName = description.getDisplayName();
		String methodName = description.getMethodName();
		text.append(
				"-------------------------------------------------------------------------------\n")
				.append("Test set: " + className + "\n")
				.append("-------------------------------------------------------------------------------\n")
				.append("Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0 sec <<< FAILURE! - in "
						+ className + "\n")
				.append(descriptionDisplayName
						+ "  Time elapsed: 0.019 sec  <<< FAILURE!\n")
				.append("This test has crashed or timeout. Check logs for details.\n\n");
		String fileNameTxt = SYSTEM_EXIT_LISTENER_RESULT + className + ".txt";
		writeReport(text, fileNameTxt);

		String fileNameXml = SYSTEM_EXIT_LISTENER_RESULT + "TEST-" + className
				+ ".xml";
		StringBuilder xml = new StringBuilder()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
				.append("<testsuite name=\""
						+ className
						+ "\" time=\"0\" tests=\"1\" errors=\"1\" skipped=\"0\" failures=\"0\">\n")
				.append("  <testcase name=\"" + methodName + "\" classname=\""
						+ className + "\" time=\"0.012\">\n")
				.append("    <error message=\""
						+ descriptionDisplayName
						+ "\" type=\"System.exit\"><![CDATA[This test crashed the VM running it, probably because of a System.exit()]]></error>\n")
				.append("    <system-err>This test crashed has crashed or timeout. Check logs for details.</system-err>\n")
				.append("  </testcase>\n").append("</testsuite>");
		writeReport(xml, fileNameXml);
	}

	private void writeReport(StringBuilder sb, String fileName) {
		try {

			File file = new File(SYSTEM_EXIT_LISTENER_RESULT);
			if (!file.exists()) {
				file.mkdirs();
			}
			// we make sure we write to an empty file
			File reportFile = new File(fileName);
			if (reportFile.exists()) {
				reportFile.delete();
			}
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, true)));
			out.println(sb.toString());
			out.close();
		} catch (FileNotFoundException e) {
			sb.append(e.getMessage());
		} catch (IOException e) {
			sb.append(e.getMessage());
		}
	}
}
