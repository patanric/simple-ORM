package ch.riccardo.reflection;

import ch.riccardo.reflection.model.Person;
import ch.riccardo.reflection.util.ColumnField;
import ch.riccardo.reflection.util.Metamodel;
import ch.riccardo.reflection.util.PrimaryKeyField;

import java.util.List;

public class Reflection {

    public static void main(String[] args) {
        Metamodel metamodel = Metamodel.of(Person.class);

        PrimaryKeyField primaryKeyField = metamodel.getPrimaryKey();
        List<ColumnField> columnFields = metamodel.getColumnFields();

        System.out.println("primaryKeyField: name=" + primaryKeyField.getName()
                + ", type=" + primaryKeyField.getType().getSimpleName());

        for (ColumnField columnField : columnFields) {
            System.out.println("columnField: name=" + columnField.getName()
                    + ", type=" + columnField.getType().getSimpleName());
        }


    }
}
