package ch.riccardo.reflection.util;

import ch.riccardo.reflection.annotations.Column;
import ch.riccardo.reflection.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Metamodel {

    private final Class<?> clss;

    public Metamodel(Class<?> clss) {
        this.clss = clss;
    }

    public static Metamodel of(Class<?> clss) {
        return new Metamodel(clss);
    }

    public PrimaryKeyField getPrimaryKey() {
        Field[] declaredFields = clss.getDeclaredFields();
        for (Field field : declaredFields) {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                return new PrimaryKeyField(field);
            }
        }
        throw new IllegalArgumentException("No PrimaryKey field available in class " + clss.getSimpleName());
    }

    public List<ColumnField> getColumnFields() {
        List<ColumnField> columnFields = new ArrayList<>();

        Field[] declaredFields = clss.getDeclaredFields();
        for (Field field : declaredFields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columnFields.add(new ColumnField(field));
            }
        }
        return columnFields;
    }

    @Override
    public String toString() {
        return "ch.riccardo.reflection.util.Metamodel{" +
                "clss=" + clss +
                '}';
    }

    public String buildInsertStatement() {
        // insert into Person (id, name, age) values (?, ?, ?)

        String columnElement = buildColumnElement();
        String questionMarkElement = buildQuestionMarksElement();

        return "insert into " + clss.getSimpleName() + " (" + columnElement + ") values (" + questionMarkElement + ")";
    }

    public String buildSelectStatement() {
        // select id, name, age from Person where id = ?
        String columnElement = buildColumnElement();
        return "select " + columnElement + " from " + clss.getSimpleName() + " where " + getPrimaryKey().getName() + " = ?";
    }

    private String buildQuestionMarksElement() {
        int numberOfQuestionMarks = getColumnFields().size() + 1;
        return IntStream.range(0, numberOfQuestionMarks).mapToObj(i -> "?").collect(Collectors.joining(", "));
    }

    private String buildColumnElement() {
        List<String> columns = getColumnFields().stream().map(ColumnField::getName).collect(Collectors.toList());
        columns.add(0, getPrimaryKey().getName());
        return String.join(", ", columns);
    }
}
