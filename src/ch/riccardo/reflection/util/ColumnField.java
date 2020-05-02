package ch.riccardo.reflection.util;

import ch.riccardo.reflection.annotations.Column;

import java.lang.reflect.Field;

public class ColumnField {
    private final Field field;
    private final Column column;

    public ColumnField(Field field) {
        this.field = field;
        this.column = this.field.getAnnotation(Column.class);
    }

    public String getName() {
        return column.name();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }
}
