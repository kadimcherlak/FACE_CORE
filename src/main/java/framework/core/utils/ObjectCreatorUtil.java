package framework.core.utils;

import com.google.common.base.Optional;
import framework.core.exceptions.FrameworkException;
import org.openqa.selenium.NoSuchElementException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectCreatorUtil {

	/**
	 * Searches the given class for the constructors matching parameters and
	 * invokes the right one if found and returns a optional instance.
	 *
	 * @param cls
	 * @param parameters
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> selectConstructorAndInvoke(Class<T> cls, Object... parameters) {
		try {
			T pageObj = null;
			for (Constructor<T> constructor : (Constructor<T>[]) cls.getConstructors()) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				Object[] args = new Object[parameters.length];
				int count = 0;
				for (Class<?> param : parameterTypes) {
					for (Object inputParam : parameters) {
						if (param.isAssignableFrom(inputParam.getClass())) {
							args[count++] = inputParam;
							break;
						}
					}
				}
				if (parameterTypes.length == 0 || args.length > 0) {
					pageObj = constructor.newInstance(args);
					break;
				}
			}
			return Optional.fromNullable(pageObj);
		} catch (NoSuchElementException nexcp) {
			throw nexcp;
		} catch (Exception excp) {
			throw new FrameworkException("Error while creating the object " + cls, excp);
		}
	}

}
