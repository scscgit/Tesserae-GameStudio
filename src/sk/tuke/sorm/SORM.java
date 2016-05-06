package sk.tuke.sorm;

import sk.tuke.gamestudio.entity.Score;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jaros_000 on 31.3.2016.
 */
public class SORM {
    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "postgres";

    public String getDropTableString(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(clazz.getSimpleName());
        return sb.toString();
    }

    public String getCreateTableString(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(clazz.getSimpleName());
        sb.append(" (");

        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {
            if (!first)
                sb.append(", ");
            first = false;
            sb.append(field.getName());
            sb.append(" ");
            sb.append(getSQLType(field.getType()));
            if(field.isAnnotationPresent(Id.class)) {
                sb.append(" PRIMARY KEY");
            }
        }

        sb.append(") ");
        return sb.toString();
    }

    public String getInsertString(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(clazz.getSimpleName());
        sb.append(" (");

        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {
            if (!first)
                sb.append(", ");
            first = false;
            sb.append(field.getName());
        }

        sb.append(") VALUES (");

        first = true;
        for (Field field : clazz.getDeclaredFields()) {
            if (!first)
                sb.append(", ");
            first = false;
            sb.append("?");
        }

        sb.append(")");

        return sb.toString();
    }

    public String getDeleteString(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(clazz.getSimpleName());
        sb.append(" WHERE ");

        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(Id.class)) {
                if (!first)
                    sb.append(" AND ");
                sb.append(field.getName());
                sb.append(" = ");
                sb.append("?");
                first = false;
            }
        }

        return sb.toString();
    }

    public String getSelectString(Class<?> clazz, String condition) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");

        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {
            if (!first)
                sb.append(", ");
            first = false;
            sb.append(field.getName());
        }

        sb.append(" FROM ");
        sb.append(clazz.getSimpleName());

        if(condition != null) {
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
                field.setAccessible(true);
                Object value = field.get(object);
                if (field.getType().getName().equals("java.util.Date")) {
                    ps.setDate(index, new java.sql.Date(((java.util.Date) value).getTime()));
                } else {
                    ps.setObject(index, value);
                }
                index++;
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
                if(field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (field.getType().getName().equals("java.util.Date")) {
                        ps.setDate(index, new java.sql.Date(((java.util.Date) value).getTime()));
                    } else {
                        ps.setObject(index, value);
                    }
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

        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(query)) {

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
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
                throw new IllegalArgumentException("Date type " + clazz.getName() + "not supported");
        }
    }

    public static void main(String[] args) throws Exception {
        Score score = new Score(1, "jaro", "mine", 120, new Date());

        SORM sorm = new SORM();
        //System.out.println(sorm.getDropTableString(Score.class));
        //System.out.println(sorm.getCreateTableString(Score.class));
        //sorm.insert(score);
        //System.out.println(sorm.getDeleteString(Score.class));
        sorm.delete(score);

        //System.out.println(sorm.getSelectString(Score.class));
        //System.out.println(sorm.getSelectString(Person.class));
        //System.out.println(sorm.select(Score.class));
    }
}
