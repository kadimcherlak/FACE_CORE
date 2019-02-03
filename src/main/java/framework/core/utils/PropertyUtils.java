package framework.core.utils;

import framework.core.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyUtils {

	private static final Logger logger = LogManager.getLogger(PropertyUtils.class);
	private static final String privateKey = "bb";
	private final Properties props = new Properties();

	private PropertyUtils(String propFile) {
		InputStream inputResource = ClassLoader.getSystemResourceAsStream(propFile);
		try {
			props.load(inputResource);
			closeResource(inputResource);
		} catch (IOException ioe) {
			throw new FrameworkException("IO Exception", ioe);
		} finally {
			closeResource(inputResource);
		}
	}

	public static Properties getPropertiesByPrefix(Properties props, String[] prefix) {
		Properties returnProps = new Properties();
		Enumeration<String> en = (Enumeration<String>) props.propertyNames();
		while (en.hasMoreElements()) {
			String propName = en.nextElement();
			String propValue = props.getProperty(propName);

			for (String pre : prefix) {
				if (propName.startsWith(pre)) {
					String key = propName.substring(pre.length() + 1);
					returnProps.setProperty(key, propValue);
				}
			}
		}
		return returnProps;
	}

	public static Properties getPropertiesFromMap(Map map) {
		Properties props = new Properties();
		props.putAll(map);
		return props;
	}

	public static Properties combineProperties(Properties props, Properties... moreProps) {
		Properties returnProps = props;
		for (Properties p : moreProps) {
			returnProps.putAll(p);
		}
		return returnProps;
	}

	public static Properties loadPropertiesFromFile(String path) {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			props.load(in);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return props;
	}

	public static void writePropertiesToFile(Properties props, String path) {
		// properties are not alpha sorted, so putting in hashmap to allow the
		// sort before print.
		TreeMap<String, String> propsMap = new TreeMap();
		propsMap.putAll(props.entrySet().stream()
			.collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));
		try (Writer writer = Files.newBufferedWriter(Paths.get(path))) {
			propsMap.forEach((key, value) -> {
				try {
					writer.write(key + "=" + value + System.lineSeparator());
				} catch (IOException ex) {
					throw new UncheckedIOException(ex);
				}
			});
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void encryptPropertyValue(String propertyFileName, String propertyToEncrypt) {
		Properties props = loadPropertiesFromFile(propertyFileName);
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(privateKey);
		String encryptedPropertyValue = encryptor.encrypt(props.getProperty(propertyToEncrypt));
		props.setProperty(propertyToEncrypt, encryptedPropertyValue);
		writePropertiesToFile(props, propertyFileName);
	}

	public static String getEncryptedPassword(String valueToEncrypt) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(privateKey);
		return encryptor.encrypt(valueToEncrypt);
	}

	public static String decryptValue(String encryptedPropertyValue) {
		StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
		decryptor.setPassword(privateKey);
		String decryptedPropertyValue = decryptor.decrypt(encryptedPropertyValue);
		return decryptedPropertyValue;
	}

	public static String decryptPropertyValue(String encryptedPropertyValue) {
		StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
		decryptor.setPassword(privateKey);
		String decryptedPropertyValue = decryptor.decrypt(encryptedPropertyValue);
		return decryptedPropertyValue;
	}

	public static void closeResource(InputStream inputResource) {
		try {
			inputResource.close();
		} catch (IOException ioe) {
			logger.error("IO Exception {}", ioe.getMessage());
		}
	}

	public static PropertyUtils getInstance(final String propFile) {
		return new PropertyUtils(propFile);
	}

	public String getProperty(final String key) {
		return props.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return props.stringPropertyNames();
	}

	public boolean containsKey(final String key) {
		return props.containsKey(key);
	}

}
