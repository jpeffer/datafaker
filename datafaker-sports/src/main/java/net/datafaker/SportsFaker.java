package net.datafaker;

public class SportsFaker extends InternalFaker {

    public EnglandFootBall englandfootball() {
        return getProvider(EnglandFootBall.class, () -> new EnglandFootBall(this));
    }


}
