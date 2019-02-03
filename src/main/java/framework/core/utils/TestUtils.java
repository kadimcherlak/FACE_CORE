package framework.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Calendar;

public class TestUtils {

	private static final Logger logger = LogManager.getLogger(TestUtils.class);

	public TestUtils() {
	}

	public static Calendar getTimeAsCalendar() {
		return Calendar.getInstance();
	}

	public static long getTime() {
		return System.currentTimeMillis();
	}

	public static String calcTime(long startTime) {
		return "Time Taken:" + (System.currentTimeMillis() - startTime) / 1000;
	}

	public static void wait(int closeCoolTime) {
		try {
			Thread.sleep(closeCoolTime);
		} catch (InterruptedException e) {
			logger.debug("Interrupted Exception: " + e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public static Object getPrivateField(Object subject, String fieldName)
		throws NoSuchFieldException, IllegalAccessException {
		Field f = subject.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);
		return f.get(subject);
	}
}
