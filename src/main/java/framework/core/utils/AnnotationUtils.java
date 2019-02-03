package framework.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class AnnotationUtils {

	public static final String ANNOTATION_DATA = "annotationData";
	private static final String ANNOTATIONS = "annotations";

	public static void alterAnnotationOn(Class clazzToLookFor, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) {
		try {
			Method method = Class.class.getDeclaredMethod(ANNOTATION_DATA, null);
			method.setAccessible(true);
			Object annotationData = method.invoke(clazzToLookFor);
			Field annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS);
			annotations.setAccessible(true);
			Map<Class<? extends Annotation>, Annotation> map =
				(Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
			map.put(annotationToAlter, annotationValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
