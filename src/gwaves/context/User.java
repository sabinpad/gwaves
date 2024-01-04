package gwaves.context;

import lombok.Getter;

import fileio.input.UserInput;

@Getter
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

    public abstract void clearAll();

    /**
     * @return result message of the last executed command
     */
    public String getLastCommandMessage() {
        return this.commandMessage;
    }
}
