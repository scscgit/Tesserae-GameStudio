package sk.tuke.sorm;

import sk.tuke.gamestudio.entity.Score;

import javax.persistence.Temporal;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SORM2 implements ISORM
{
	private String url = "jdbc:postgresql://localhost/gamestudio";
	private String login = "postgres";
	private String password = "postgres";

	public SORM2(String url, String login, String password)
	{
		this.url = url;
		this.login = login;
		this.password = password;
	}

	//Default postgresql constructor
	public SORM2()
	{
	}

	public String getDropTableString(Class<?> clazz)
	{
		return String.format("DROP TABLE %s", clazz.getSimpleName());
	}

	public String getCreateTableString(Class<?> clazz)
	{
		return String.format("CREATE TABLE %s (%s)",
		                     clazz.getSimpleName(),
		                     formatFieldsToString(clazz.getDeclaredFields(),
		                                          (Field f) -> f.getName() + " " + getSQLType(f.getType())
		                                                       +
		                                                       (f.isAnnotationPresent(Id.class) ? " PRIMARY KEY" : ""),
		                                          ", ")
		);
	}

	public String getInsertString(Class<?> clazz)
	{
		Field[] fields = clazz.getDeclaredFields();

		return String.format("INSERT INTO %s (%s) VALUES (%s)",
		                     clazz.getSimpleName(),
		                     formatFieldsToString(fields),
		                     formatFieldsToString(fields, (Field f) -> "?", ", ")
		);
	}

	public String getUpdateString(Class<?> clazz)
	{
		return String.format("UPDATE %s SET %s WHERE %s",
		                     clazz.getSimpleName(),
		                     formatFieldsToString(clazz.getDeclaredFields(),
		                                          f -> !isId(f),
		                                          f -> f.getName() + " = ?",
		                                          ", "),
		                     formatFieldsToString(clazz.getDeclaredFields(),
		                                          f -> isId(f),
		                                          f -> f.getName() + " = ?",
		                                          " AND ")
		);
	}

	public String getDeleteString(Class<?> clazz)
	{
		return String.format("DELETE FROM %s WHERE %s",
		                     clazz.getSimpleName(),
		                     formatFieldsToString(clazz.getDeclaredFields(),
		                                          f -> !isId(f),
		                                          f -> f.getName() + " = ?",
		                                          " AND ")
		);
	}

	public String getSelectString(Class<?> clazz, String condition)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("SELECT %s FROM %s",
		                        formatFieldsToString(clazz.getDeclaredFields()),
		                        clazz.getSimpleName()));

		if (condition != null)
		{
			sb.append(" WHERE ");
			sb.append(condition);
		}

		return sb.toString();
	}

	//Central point of decision whether a field can be considered an ID. Original version did not work.
	private static boolean isId(Field field)
	{
		return field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(javax.persistence.Id.class);
	}

	public void insert(Object object) throws Exception
	{
		Class<?> clazz = object.getClass();
		String query = getInsertString(clazz);

		System.out.println(query);

		try (Connection connection = DriverManager.getConnection(url, login, password);
		     PreparedStatement ps = connection.prepareStatement(query))
		{
			int index = 1;
			for (Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				//For some reason, it is actually the second one and not the first one
				if (isId(field))
				{
					ps.setBigDecimal(index, createIndex(object.getClass()));
				}
				else
				{
					ps.setObject(index, getValue(field, object));
				}
				index++;
			}

			ps.executeUpdate();
		}
	}

	/**
	 * Returns a new index for the entity using, (obviously) a reflection.
	 *
	 * @param clazz
	 * 	class for which we want the new index
	 * @return new index larger than the largest current index
	 *
	 * @throws Exception
	 */
	private BigDecimal createIndex(Class<?> clazz) throws Exception
	{
		List<?> select = select(clazz);
		BigDecimal entityIndex = new BigDecimal(0);
		//Iterates over all entities to find the largest ID
		for (Object o : select)
		{
			//Finds ID field
			for (Field field : o.getClass().getDeclaredFields())
			{
				field.setAccessible(true);
				if (isId(field))
				{
					//Finds max index
					//There was problem with Integer vs int when getInt() was used
					int currentIndex = (int) field.get(o);

					if (BigDecimal.valueOf(currentIndex).compareTo(entityIndex) > 0)
					{
						entityIndex = new BigDecimal(currentIndex);
					}
					break;
				}
			}
		}

		return entityIndex.add(new BigDecimal(1));
	}

	public void update(Object object) throws Exception
	{
		Class<?> clazz = object.getClass();
		String query = getUpdateString(clazz);

		System.out.println(query);

		try (Connection connection = DriverManager.getConnection(url, login, password);
		     PreparedStatement ps = connection.prepareStatement(query))
		{

			int index = 1;
			for (Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				if (!isId(field))
				{
					ps.setObject(index, getValue(field, object));
					index++;
				}
			}

			for (Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				if (isId(field))
				{
					ps.setObject(index, getValue(field, object));
					index++;
				}
			}

			ps.executeUpdate();
		}
	}

	public void delete(Object object) throws Exception
	{
		Class<?> clazz = object.getClass();
		String query = getDeleteString(clazz);

		System.out.println(query);

		try (Connection connection = DriverManager.getConnection(url, login, password);
		     PreparedStatement ps = connection.prepareStatement(query))
		{

			int index = 1;
			for (Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				//Only the non-id fields get used in the delete query
				if (!isId(field))
				{
					if (field.isAnnotationPresent(Temporal.class))
					{
						ps.setDate(index, (java.sql.Date) getValue(field, object));
					}
					else
					{
						ps.setObject(index, getValue(field, object));
					}
					index++;
				}
			}

			ps.executeUpdate();
		}
	}

	public List<?> select(Class<?> clazz) throws Exception
	{
		return select(clazz, null);
	}

	public List<?> select(Class<?> clazz, String condition) throws Exception
	{
		List<Object> results = new ArrayList<>();
		String query = getSelectString(clazz, condition);

		System.out.println(query);

		try (Connection connection = DriverManager.getConnection(url, login, password);
		     PreparedStatement ps = connection.prepareStatement(query))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					Object object = clazz.newInstance();

					int index = 1;
					for (Field field : clazz.getDeclaredFields())
					{
						field.setAccessible(true);
						Object value = rs.getObject(index);
						if (isId(field))
						{
							field.set(object, ((BigDecimal) value).intValueExact());
						}
						//If the field is date, we do this conversion (disclaimer: it is not universal solution I guess)
						else if (field.isAnnotationPresent(Temporal.class))
						{
							//timestampValue() is used to get both time AND date before conversion to Date format
							field.set(object,
							          new java.util.Date(((oracle.sql.TIMESTAMP) value).timestampValue().getTime()));
						}
						else if (Number.class.isAssignableFrom(field.getType()) ||
						         int.class.isAssignableFrom(field.getType()))
						{
							field.set(object, ((BigDecimal) value).intValueExact());
						}
						else
						{
							field.set(object, value);
						}
						index++;
					}

					results.add(object);
				}
			}
		}

		return results;
	}

	private String getSQLType(Class<?> clazz)
	{
		switch (clazz.getName())
		{
			case "java.lang.String":
				return "VARCHAR(32)";
			case "int":
			case "java.lang.Integer":
				return "INTEGER";
			case "java.util.Date":
				return "DATE";
			default:
				throw new IllegalArgumentException("Date type " + clazz.getName() + "not supported!");
		}
	}

	private Object getValue(Field field, Object object) throws IllegalAccessException
	{
		field.setAccessible(true);
		Object value = field.get(object);
		if (field.getType().getName().equals("java.util.Date"))
		{
			return new java.sql.Date(((Date) value).getTime());
		}
		else
		{
			return value;
		}
	}

	private String formatFieldsToString(Field[] fields, Predicate<Field> filter, Function<Field, String> transformer,
	                                    String separator)
	{
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (Field field : fields)
		{
			if (filter.test(field))
			{
				if (!first)
				{
					sb.append(separator);
				}
				sb.append(transformer.apply(field));
				first = false;
			}
		}

		return sb.toString();
	}

	private String formatFieldsToString(Field[] fields, Predicate<Field> filter, String separator)
	{
		return formatFieldsToString(fields, filter, f -> f.getName(), separator);
	}

	private String formatFieldsToString(Field[] fields, Function<Field, String> transformer, String separator)
	{
		return formatFieldsToString(fields, f -> true, transformer, separator);
	}

	private String formatFieldsToString(Field[] fields, String separator)
	{
		return formatFieldsToString(fields, f -> true, f -> f.getName(), separator);
	}

	private String formatFieldsToString(Field[] fields)
	{
		return formatFieldsToString(fields, ", ");
	}

	public static void main(String[] args) throws Exception
	{
		Score score = new Score(1, "jaro", "mines", 120, new Date());

		String url = "jdbc:oracle:oci:@localhost:1521:xe";
		String login = "gamestudio";
		String password = "gamestudio";
		SORM2 sorm = new SORM2(url, login, password);

		System.out.println(sorm.getCreateTableString(Score.class));
		System.out.println(sorm.getDropTableString(Score.class));
		System.out.println(sorm.getInsertString(Score.class));
		System.out.println(sorm.getUpdateString(Score.class));
		System.out.println(sorm.getDeleteString(Score.class));
		System.out.println(sorm.getSelectString(Score.class, null));
		System.out.println(sorm.getSelectString(Score.class, "game = 'mines'"));

		sorm.insert(score);
		//        System.out.println(sorm.select(Score.class));
		//
		//        score.setPlayer("fero");
		//        score.setGame("nukes");
		//        sorm.update(score);
		//        System.out.println(sorm.select(Score.class));
		//
		//        sorm.delete(score);
		//        System.out.println(sorm.select(Score.class));
	}
}
