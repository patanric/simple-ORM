package ch.riccardo.reflection.orm;

import ch.riccardo.reflection.annotations.Inject;
import ch.riccardo.reflection.util.ColumnField;
import ch.riccardo.reflection.util.Metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ManagedEntityManager<T> implements EntityManager<T> {

    final AtomicLong atomicLong = new AtomicLong(0L);

    @Inject
    private Connection connection;

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {
        Metamodel metamodel = Metamodel.of(t.getClass());
        String sql = metamodel.buildInsertStatement();
        try (PreparedStatement preparedStatement = prepareStatementWith(sql).addVariables(t)) {
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public T findById(Class<T> clss, long id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Metamodel metamodel = Metamodel.of(clss);
        String sql = metamodel.buildSelectStatement();
        try (PreparedStatement preparedStatement = prepareStatementWith(sql).addPrimaryKey(id);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return buildInstanceFrom(clss, resultSet);
        }
    }

    private T buildInstanceFrom(Class<T> clss, ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        Metamodel metamodel = Metamodel.of(clss);

        T instance = clss.getConstructor().newInstance();
        Field primaryKeyField = metamodel.getPrimaryKey().getField();
        String primaryKeyName = metamodel.getPrimaryKey().getName();
        Class<?> primaryKeyType = primaryKeyField.getType();

        if (resultSet.next()) {
            if (primaryKeyType == long.class) {
                long primaryKey = resultSet.getInt(primaryKeyName);
                primaryKeyField.setAccessible(true);
                primaryKeyField.setLong(instance, primaryKey);
            }

            List<ColumnField> columnFields = metamodel.getColumnFields();
            for (ColumnField columnField : columnFields) {
                Field field = columnField.getField();
                field.setAccessible(true);
                String columnName = columnField.getName();
                Class<?> columnType = field.getType();

                if (columnType == int.class) {
                    int value = resultSet.getInt(columnName);
                    field.setInt(instance, value);
                } else if (columnType == String.class) {
                    String value = resultSet.getString(columnName);
                    field.set(instance, value);
                }
            }
        }
        return instance;
    }

    private PreparedStatementWrapper prepareStatementWith(String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return new PreparedStatementWrapper(preparedStatement);
    }

    // inner class
    private class PreparedStatementWrapper {
        private final PreparedStatement preparedStatement;

        public PreparedStatementWrapper(PreparedStatement preparedStatement) {
            this.preparedStatement = preparedStatement;
        }

        public PreparedStatement addVariables(T t) throws SQLException, IllegalAccessException {

            Metamodel metamodel = Metamodel.of(t.getClass());
            Class<?> primaryKeyType = metamodel.getPrimaryKey().getType();
            long id = atomicLong.getAndIncrement();
            if (primaryKeyType == long.class) {
                preparedStatement.setLong(1, id);
            }
            Field primaryKeyField = metamodel.getPrimaryKey().getField();
            primaryKeyField.setAccessible(true);
            primaryKeyField.set(t, id);

            List<ColumnField> columnFields = metamodel.getColumnFields();
            for (int i = 0; i < columnFields.size(); i++) {
                Class<?> type = columnFields.get(i).getType();
                Field field = columnFields.get(i).getField();
                field.setAccessible(true);
                Object value = field.get(t);

                if (type == int.class) {
                    preparedStatement.setInt(i + 2, (int) value);
                } else if (type == String.class) {
                    preparedStatement.setString(i + 2, (String) value);
                } else if (type == long.class) {
                    preparedStatement.setLong(i + 2, (long) value);
                } else if (type == boolean.class) {
                    preparedStatement.setBoolean(i + 2, (boolean) value);
                }
            }
            return preparedStatement;
        }

        public PreparedStatement addPrimaryKey(Object primaryKey) throws SQLException {
            if (primaryKey.getClass() == Long.class) {
                preparedStatement.setLong(1, (Long) primaryKey);
            }
            return preparedStatement;
        }
    }
}
