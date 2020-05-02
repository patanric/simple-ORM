package ch.riccardo.reflection;

import ch.riccardo.reflection.model.Person;
import ch.riccardo.reflection.orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ReadingObjects {

    public static void main(String[] args) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person riccardo = entityManager.findById(Person.class, 0L);
        Person adriano = entityManager.findById(Person.class, 1L);
        Person sandra = entityManager.findById(Person.class, 2L);
        Person emanuela = entityManager.findById(Person.class, 3L);

        System.out.println(riccardo);
        System.out.println(adriano);
        System.out.println(sandra);
        System.out.println(emanuela);

    }

}
