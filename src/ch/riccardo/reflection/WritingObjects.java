package ch.riccardo.reflection;

import ch.riccardo.reflection.beanmanager.BeanManager;
import ch.riccardo.reflection.model.Person;
import ch.riccardo.reflection.orm.EntityManager;
import ch.riccardo.reflection.orm.ManagedEntityManager;

import java.sql.SQLException;

public class WritingObjects {

    public static void main(String[] args) throws SQLException, IllegalAccessException {

        BeanManager beanManager = BeanManager.getInstance();
        EntityManager<Person> entityManager = beanManager.getInstance(ManagedEntityManager.class);

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
