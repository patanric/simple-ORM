package ch.riccardo.reflection.model;

import ch.riccardo.reflection.annotations.Column;
import ch.riccardo.reflection.annotations.PrimaryKey;

public class Person {

    @PrimaryKey(name = "k_id")
    private long id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_age")
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "ch.riccardo.reflection.model.Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
