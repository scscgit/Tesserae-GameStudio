package sk.tuke.sorm;

import java.util.List;

/**
 * You can sorm any way you like.
 * <p>
 * Created by Steve on 18.04.2016.
 */
public interface ISORM
{
	String getDropTableString(Class<?> clazz);
	String getCreateTableString(Class<?> clazz);
	String getInsertString(Class<?> clazz);
	String getDeleteString(Class<?> clazz);
	String getSelectString(Class<?> clazz, String condition);
	void insert(Object object) throws Exception;
	void delete(Object object) throws Exception;
	List<?> select(Class<?> clazz) throws Exception;
	List<?> select(Class<?> clazz, String condition) throws Exception;
}
