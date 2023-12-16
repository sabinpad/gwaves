package gwaves.context;

import fileio.input.UserInput;

public abstract class User {
    private String username;
    private Integer age;
    private String city;

    protected String commandMessage;

    public User() {

    }

    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    public User(final UserInput userInput) {
        this.username = userInput.getUsername();
        this.age = userInput.getAge();
        this.city = userInput.getCity();
    }

    /**
     * @return name of the user
     */
    public String getUserName() {
        return this.username;
    }

    /**
     * @return age number
     */
    public Integer getAge() {
        return this.age;
    }

    /**
     * @return name of the user's city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @return result of the last executed command
     */
    public String getLastCommandMessage() {
        return this.commandMessage;
    }
}
