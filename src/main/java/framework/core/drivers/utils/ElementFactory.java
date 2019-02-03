package framework.core.drivers.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class ElementFactory {

	private static final Logger logger = LogManager.getLogger(ElementFactory.class);

	public static Object createInstanceOfType(Field field, ElementLocatorWrapper wrapper, Object pageObj,
											  Class<?> pageType) {
		try {
			logger.debug("Type is {}", field.getType());
			return getType(field).getConstructor(ElementLocatorWrapper.class, pageType).newInstance(wrapper, pageObj);
		} catch (Exception excp) {
			logger.error("Error while creating instance of a base element for field {} in class {}.", field.getName(),
				field.getDeclaringClass().getName(), excp);
			throw new NoSuchElementException("Error while creating the wrapper type", excp);
		}
	}

	private static Class<?> getType(Field field) {
		Class<?> supportedType = null;
		if (isListType(field) || isMapType(field)) {
			if (field.getGenericType() != null) {
				ParameterizedType typeOfList = (ParameterizedType) field.getGenericType();
				supportedType = (Class<?>) typeOfList.getActualTypeArguments()[
					typeOfList.getActualTypeArguments().length - 1];
			}
		} else {
			supportedType = field.getType();
		}
		return supportedType;
	}

	private static boolean isListType(Field field) {
		return List.class.equals(field.getType());
	}

	private static boolean isMapType(Field field) {
		return Map.class.equals(field.getType());
	}
}