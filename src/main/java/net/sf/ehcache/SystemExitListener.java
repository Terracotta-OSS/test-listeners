package net.sf.ehcache;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SystemExitListener extends RunListener {

	public static File TARGET_FOLDER_LOCATION;
	static {
		String currentFolderPath = new File(".").getAbsolutePath();
		int targetLastIndex = currentFolderPath.lastIndexOf("target");
		if (targetLastIndex != -1) {
			TARGET_FOLDER_LOCATION = new File(
							currentFolderPath.substring(0, targetLastIndex),
							"target");
		} else {
			TARGET_FOLDER_LOCATION = new File("target");
		}
	}

	public static final File SUREFIRE_SYSTEM_EXIT_LISTENER_RESULT = new File(TARGET_FOLDER_LOCATION, "surefire-reports");

	public static final File FAILSAFE_SYSTEM_EXIT_LISTENER_RESULT = new File(TARGET_FOLDER_LOCATION, "failsafe-reports");

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

		writeReport(text, new File(SUREFIRE_SYSTEM_EXIT_LISTENER_RESULT, className + ".txt"));
		writeReport(text, new File(FAILSAFE_SYSTEM_EXIT_LISTENER_RESULT, className + ".txt"));

		writeReport(xml, new File(SUREFIRE_SYSTEM_EXIT_LISTENER_RESULT, "TEST-" + className + ".xml"));
		writeReport(xml, new File(FAILSAFE_SYSTEM_EXIT_LISTENER_RESULT, "TEST-" + className + ".xml"));
	}

	@Override
	public void testFinished(Description description) {
		String className = description.getClassName();
		new File(SUREFIRE_SYSTEM_EXIT_LISTENER_RESULT, className + ".txt").delete();
		new File(FAILSAFE_SYSTEM_EXIT_LISTENER_RESULT, className + ".txt").delete();
		new File(SUREFIRE_SYSTEM_EXIT_LISTENER_RESULT, "TEST-" + className + ".xml").delete();
		new File(FAILSAFE_SYSTEM_EXIT_LISTENER_RESULT, "TEST-" + className + ".xml").delete();
	}

	private void writeReport(StringBuilder sb, File reportFile) {
		try {

			File parent = reportFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			// we make sure we write to an empty file
			if (reportFile.exists()) {
				reportFile.delete();
			}
			PrintWriter out = new PrintWriter(new BufferedWriter(
							new FileWriter(reportFile, true)));
			out.println(sb.toString());
			out.close();
		} catch (IOException e) {
			sb.append(e.getMessage());
		}
	}
}
