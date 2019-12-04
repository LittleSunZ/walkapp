package com.zxc.walk.framework.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类.
 * 
 * 提供访问私有变量,获取泛型类型Class, 提取集合中元素的属性, 转换字符串到对象等Util函数.
 * 
 * @author xyu
 * @date 2015年8月6日
 */
public class ReflectionUtils {

//	/**
//	 * 调用Getter方法.
//	 */
//	public static Object invokeGetterMethod(Object target, String propertyName) {
//		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
//		return invokeMethod(target, getterMethodName, new Class[] {},
//				new Object[] {});
//	}
//
//	/**
//	 * 调用Setter方法.使用value的Class来查找Setter方法.
//	 */
//	public static void invokeSetterMethod(Object target, String propertyName,
//			Object value) {
//		invokeSetterMethod(target, propertyName, value, null);
//	}
//
//	/**
//	 * 调用Setter方法.
//	 *
//	 * @param propertyType
//	 *            用于查找Setter方法,为空时使用value的Class替代.
//	 */
//	public static void invokeSetterMethod(Object target, String propertyName,
//			Object value, Class<?> propertyType) {
//		Class<?> type = propertyType != null ? propertyType : value.getClass();
//		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
//		invokeMethod(target, setterMethodName, new Class[] { type },
//				new Object[] { value });
//	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object object,
                                       final String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			LogUtil.d("不可能抛出的异常{}" + e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
                                     final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			LogUtil.d("不可能抛出的异常{}" + e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object,
                                      final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method ["
					+ methodName + "] on target [" + object + "]");
		}

		method.setAccessible(true);

		try {
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Field getDeclaredField(final Object object,
                                            final String fieldName) {
		if (object == null || TextUtils.isEmpty(fieldName)) {
			LogUtil.d("object或者fieldname不能为null");
			return null;
		}
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 强行设置Field可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName,
                                              Class<?>[] parameterTypes) {
		// Assert.notNull(object, "object不能为空");
		if (object == null) {
			LogUtil.d("object不能为空");
		}

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {// NOSONAR
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
	 * extends HibernateDao<User>
	 *
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(final Class clazz,
                                                final int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			LogUtil.d(clazz.getSimpleName()
					+ "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			LogUtil.d("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			LogUtil.d(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Type mygetSuperClassGenricType(@SuppressWarnings("rawtypes") final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		return params[index];
	}


	/**
	 * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.最后生成对象
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T getSuperClassGenricInstance(Class clazz) {
		try {
			return (T) getNewInstance(getSuperClassGenricType(clazz));
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取反射的实例对象
	 * 
	 * @param cls
	 * @return
	 */
	public static final <T> T getNewInstance(Class<T> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(
			Exception e) {
		return convertReflectionExceptionToUnchecked(null, e);
	}

	public static RuntimeException convertReflectionExceptionToUnchecked(
            String desc, Exception e) {
		desc = (desc == null) ? "Unexpected Checked Exception." : desc;
		if (e instanceof IllegalAccessException
				|| e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(desc, e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(desc,
					((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException(desc, e);
	}
}
