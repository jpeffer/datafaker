package net.datafaker;

public class MediaFaker extends InternalFaker {

    public TheItCrowd theItCrowd() {
        return getProvider(TheItCrowd.class, () -> new TheItCrowd(this));
    }

}
