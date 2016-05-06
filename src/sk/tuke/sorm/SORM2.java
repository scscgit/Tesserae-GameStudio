package sk.tuke.sorm;

import sk.tuke.gamestudio.entity.Score;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SORM2 {
    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "postgres";

    public String getDropTableString(Class<?> clazz) {
        return String.format("DROP TABLE %s", clazz.getSimpleName());
    }

    public String getCreateTableString(Class<?> clazz) {
        return String.format("CREATE TABLE %s (%s)",
                clazz.getSimpleName(),
                formatFieldsToString(clazz.getDeclaredFields(),
                        (Field f) -> f.getName() + " " + getSQLType(f.getType())
                                + (f.isAnnotationPresent(Id.class) ? " PRIMARY KEY" : ""), ", ")
        );
    }

    public String getInsertString(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                clazz.getSimpleName(),
                formatFieldsToString(fields),
                formatFieldsToString(fields, (Field f) -> "?", ", ")
        );
    }

    public String getUpdateString(Class<?> clazz) {
        return String.format("UPDATE %s SET %s WHERE %s",
                clazz.getSimpleName(),
                formatFieldsToString(clazz.getDeclaredFields(),
                        f -> !f.isAnnotationPresent(Id.class),
                        f -> f.getName() + " = ?" ,
                        ", "),
                formatFieldsToString(clazz.getDeclaredFields(),
                        f -> f.isAnnotationPresent(Id.class),
                        f -> f.getName() + " = ?" ,
                        " AND ")
        );
    }

    public String getDeleteString(Class<?> clazz) {
        return String.format("DELETE FROM %s WHERE %s",
                clazz.getSimpleName(),
                formatFieldsToString(clazz.getDeclaredFields(),
                        f -> f.isAnnotationPresent(Id.class),
                        f -> f.getName() + " = ?" ,
                        " AND ")
        );
    }

    public String getSelectString(Class<?> clazz, String condition) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT %s FROM %s",
                formatFieldsToString(clazz.getDeclaredFields()),
                clazz.getSimpleName()));

        if (condition != null) {
            sb.append(" WHERE ");
            sb.append(condition);
        }

        return sb.toString();
    }

    public void insert(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String query = getInsertString(clazz);

        System.out.println(query);

        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            int index = 1;
            for (Field field : clazz.getDeclaredFields()) {
                ps.setObject(index, getValue(field, object));
                index++;
            }

            ps.executeUpdate();
        }
    }

    public void update(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String query = getUpdateString(clazz);

        System.out.println(query);

        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            int index = 1;
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Id.class)) {
                    ps.setObject(index, getValue(field, object));
                    index++;
                }
            }

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    ps.setObject(index, getValue(field, object));
                    index++;
                }
            }

            ps.executeUpdate();
        }
    }

    public void delete(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String query = getDeleteString(clazz);

        System.out.println(query);

        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            int index = 1;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    ps.setObject(index, getValue(field, object));
                    index++;
                }
            }

            ps.executeUpdate();
        }
    }

    public List<?> select(Class<?> clazz) throws Exception {
        return select(clazz, null);
    }

    public List<?> select(Class<?> clazz, String condition) throws Exception {
        List<Object> results = new ArrayList<>();
        String query = getSelectString(clazz, condition);

        System.out.println(query);

        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object object = clazz.newInstance();

                    int index = 1;
                    for (Field field : clazz.getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = rs.getObject(index);
                        field.set(object, value);
                        index++;
                    }

                    results.add(object);
                }
            }
        }

        return results;
    }

    private String getSQLType(Class<?> clazz) {
        switch (clazz.getName()) {
            case "java.lang.String":
                return "VARCHAR(32)";
            case "int":
                return "INTEGER";
            case "java.util.Date":
                return "DATE";
            default:
                throw new IllegalArgumentException("Date type " + clazz.getName() + "not supported!");
        }
    }

    private Object getValue(Field field, Object object) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(object);
        if (field.getType().getName().equals("java.util.Date")) {
            return new java.sql.Date(((Date) value).getTime());
        } else {
            return  value;
        }
    }

    private String formatFieldsToString(Field[] fields, Predicate<Field> filter, Function<Field, String> transformer, String separator) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (Field field : fields) {
            if (filter.test(field)) {
                if (!first)
                    sb.append(separator);
                sb.append(transformer.apply(field));
                first = false;
            }
        }

        return sb.toString();
    }

    private String formatFieldsToString(Field[] fields, Predicate<Field> filter, String separator) {
        return formatFieldsToString(fields, filter, f -> f.getName(), separator);
    }

    private String formatFieldsToString(Field[] fields, Function<Field, String> transformer, String separator) {
        return formatFieldsToString(fields, f -> true, transformer, separator);
    }

    private String formatFieldsToString(Field[] fields, String separator) {
        return formatFieldsToString(fields, f -> true, f -> f.getName(), separator);
    }

    private String formatFieldsToString(Field[] fields) {
        return formatFieldsToString(fields, ", ");
    }

    public static void main(String[] args) throws Exception {
        Score score = new Score(1, "jaro", "mines", 120, new Date());

        SORM2 sorm = new SORM2();

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
