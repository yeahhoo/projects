package mock;

/** Example class with no default constructor for testing. */
public class MyEntityClass {

    private String name;
    private int age;

    public MyEntityClass(String name, int age) {
        throw new IllegalArgumentException("The world is rough so if you're going to make it you have to be tough");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyEntityClass)) {
            return false;
        }

        MyEntityClass that = (MyEntityClass) o;

        if (age != that.age) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }
}
