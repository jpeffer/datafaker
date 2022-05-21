package net.datafaker;

/**
 * @since 0.9.0
 */
public class EnglandFootBall {

    private final SportsFaker faker;

    protected EnglandFootBall(final SportsFaker faker) {
        this.faker = faker;
    }

    public String league() {
        return faker.fakeValuesService().resolve("englandfootball.leagues", this, faker);
    }

    public String team() {
        return faker.fakeValuesService().resolve("englandfootball.teams", this, faker);
    }
}
