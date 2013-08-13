package net.sf.ehcache;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.io.*;

public class CacheManagerProperlyShutdownListener extends RunListener {

  // TODO : override this constant if outputDirectory is specified (xml config or vm argument)
  public static final String SYSTEM_EXIT_LISTENER_RESULT = "target" + File.separator + "surefire-reports" + File.separator;

  @Override
  public void testRunStarted(Description description) throws Exception {
    // description is null !
    StringBuilder sb =  new StringBuilder();
    sb.append("TestRunStarted");
    //appendToFile(sb);
    //System.out.println(sb.toString());
  }

  @Override
  public void testStarted(Description description) throws Exception {
    StringBuilder sb =  new StringBuilder();
    sb.append("TestStarted , description is : " + description.getDisplayName());
    //appendToFile(sb);
    //System.out.println(sb.toString());
  }

  private void appendToFile(StringBuilder sb) {
    try {
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SYSTEM_EXIT_LISTENER_RESULT + "CacheManagerProperlyShutdownListener-results.txt", true)));
      out.println(sb.toString());
      out.close();
    } catch (FileNotFoundException e) {
      sb.append(e.getMessage());
    } catch (IOException e) {
      sb.append(e.getMessage());
    }
  }

  @Override
  public void testFinished(Description description) {
    StringBuilder sb =  new StringBuilder();
    sb.append("TestFinished , description is : " + description.getDisplayName());
    //appendToFile(sb);
    //System.out.println(sb.toString());
  }


  /**
   * testRunFinished is called before the termination of the vm that launched the tests.
   */
  @Override
  public void testRunFinished(Result result) {
    StringBuilder sb =  new StringBuilder();
    sb.append("** TestRunFinished , result is : " + result.wasSuccessful()+ " **\n");
    boolean empty = CacheManager.ALL_CACHE_MANAGERS.isEmpty();
    if(!empty) {
      sb.append("--> Shame on you ! CacheManagers were not cleaned up, here are the cache managers remaining after the execution :\n");
      for (CacheManager allCacheManager : CacheManager.ALL_CACHE_MANAGERS) {
        sb.append("CM : " + allCacheManager.getName());
      }
    }
    System.out.println(sb.toString());
    appendToFile(sb);
  }

}
