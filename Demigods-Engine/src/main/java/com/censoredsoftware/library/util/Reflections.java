package com.censoredsoftware.library.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Reflections
{
	/**
	 * Set a static value.
	 * 
	 * @param field The field to manipulate.
	 * @param value The value being set.
	 */
	public static void setStaticValue(Field field, Object value)
	{
		try
		{
			Field modifiers = Field.class.getDeclaredField("modifiers");

			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, value);
		}
		catch(Exception ignored)
		{}
	}

	/**
	 * Set a private value.
	 * 
	 * @param instance The object being manipulated.
	 * @param name The name of the field being manipulated.
	 * @param value The value being set.
	 */
	public static void setPrivateValue(Object instance, String name, Object value)
	{
		try
		{
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		}
		catch(Exception ignored)
		{}
	}

	/**
	 * Get a private value.
	 * 
	 * @param instance The object being manipulated.
	 * @param name The name of the field being manipulated.
	 * @return The value from the manipulated field.
	 */
	public static Object getPrivateValue(Object instance, String name)
	{
		try
		{
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(instance);
		}
		catch(Exception ignored)
		{}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T, V> V getPrivateMethod(Class<T> clazz, String methodName, T instance, Object... args)
	{
		V value = null;
		try
		{
			Method method = clazz.getDeclaredMethod(methodName);
			method.setAccessible(true);
			value = (V) method.invoke(instance, args);
		}
		catch(Exception ignored)
		{}
		return value;
	}
}
