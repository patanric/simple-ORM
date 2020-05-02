package ch.riccardo.reflection;

import ch.riccardo.reflection.model.Person;
import ch.riccardo.reflection.orm.EntityManager;

import java.sql.SQLException;

public class WritingObjects {

    public static void main(String[] args) throws SQLException, IllegalAccessException {

        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person riccardo = new Person("Riccardo", 39);
        Person adriano = new Person("Adriano", 27);
        Person sandra = new Person("Sandra", 26);
        Person emanuela = new Person("Emanuela", 25);

        System.out.println(riccardo);
        System.out.println(adriano);
        System.out.println(sandra);
        System.out.println(emanuela);

        System.out.println("write DB");

        entityManager.persist(riccardo);
        entityManager.persist(adriano);
        entityManager.persist(sandra);
        entityManager.persist(emanuela);

        System.out.println(riccardo);
        System.out.println(adriano);
        System.out.println(sandra);
        System.out.println(emanuela);

    }

}
