package net.datafaker;

import net.datafaker.service.FakeValuesService;
import net.datafaker.service.RandomService;

import java.nio.file.Path;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Provides utility methods for generating fake strings, such as names, phone
 * numbers, addresses. generate random strings with given patterns
 *
 * @author ren
 */
public class InternalFaker {
    private final RandomService randomService;
    private final FakeValuesService fakeValuesService;
    private final Map<Class<?>, Object> providersMap = new IdentityHashMap<>();

    public InternalFaker() {
        this(Locale.ENGLISH);
    }

    public InternalFaker(Locale locale) {
        this(locale, (Random) null);
    }

    public InternalFaker(Random random) {
        this(Locale.ENGLISH, random);
    }

    public InternalFaker(Locale locale, Random random) {
        this(locale, new RandomService(random));
    }

    public InternalFaker(Locale locale, RandomService randomService) {
        this(new FakeValuesService(locale, randomService), randomService);
    }

    public InternalFaker(FakeValuesService fakeValuesService, RandomService random) {
        this.randomService = random;
        this.fakeValuesService = fakeValuesService;
    }

    /**
     * Constructs Faker instance with default argument.
     *
     * @return {@link InternalFaker#InternalFaker()}
     */
    public static InternalFaker instance() {
        return new InternalFaker();
    }

    /**
     * Constructs Faker instance with provided {@link Locale}.
     *
     * @param locale - {@link Locale}
     * @return {@link InternalFaker#InternalFaker(Locale)}
     */
    public static InternalFaker instance(Locale locale) {
        return new InternalFaker(locale);
    }

    /**
     * Constructs Faker instance with provided {@link Random}.
     *
     * @param random - {@link Random}
     * @return {@link InternalFaker#InternalFaker(Random)}
     */
    public static InternalFaker instance(Random random) {
        return new InternalFaker(random);
    }

    /**
     * Constructs Faker instance with provided {@link Locale} and {@link Random}.
     *
     * @param locale - {@link Locale}
     * @param random - {@link Random}
     * @return {@link InternalFaker#InternalFaker(Locale, Random)}
     */
    public static InternalFaker instance(Locale locale, Random random) {
        return new InternalFaker(locale, random);
    }

    Locale getLocale() {
        return fakeValuesService.getLocalesChain().isEmpty() || fakeValuesService.getLocalesChain().get(0) == null
            ? Locale.ROOT : fakeValuesService.getLocalesChain().get(0);
    }

    /**
     * Returns a string with the '#' characters in the parameter replaced with random digits between 0-9 inclusive.
     * <p>
     * For example, the string "ABC##EFG" could be replaced with a string like "ABC99EFG".
     *
     * @param numberString Template for string generation
     * @return Generated string
     */
    public String numerify(String numberString) {
        return fakeValuesService.numerify(numberString);
    }

    /**
     * Returns a string with the '?' characters in the parameter replaced with random alphabetic
     * characters.
     * <p>
     * For example, the string "12??34" could be replaced with a string like "12AB34".
     *
     * @param letterString Template for string generation
     * @return Generated string.
     */
    public String letterify(String letterString) {
        return fakeValuesService.letterify(letterString);
    }

    /**
     * Returns a string with the '?' characters in the parameter replaced with random alphabetic
     * characters.
     * <p>
     * For example, the string "12??34" could be replaced with a string like "12AB34".
     */
    public String letterify(String letterString, boolean isUpper) {
        return fakeValuesService.letterify(letterString, isUpper);
    }

    /**
     * Applies both a {@link #numerify(String)} and a {@link #letterify(String)}
     * over the incoming string.
     */
    public String bothify(String string) {
        return fakeValuesService.bothify(string);
    }

    /**
     * Applies both a {@link #numerify(String)} and a {@link #letterify(String)}
     * over the incoming string.
     */
    public String bothify(String string, boolean isUpper) {
        return fakeValuesService.bothify(string, isUpper);
    }

    /**
     * Generates a String that matches the given regular expression.
     */
    public String regexify(String regex) {
        return fakeValuesService.regexify(regex);
    }

    /**
     * Generates a String by example. The output string will have the same pattern as the input string.
     * <p>
     * For example:
     * "AAA" becomes "KRA"
     * "abc" becomes "uio"
     * "948" becomes "345"
     * "A19c" becomes "Z20d"
     *
     * @param example The input string
     * @return The output string based on the input pattern
     */
    public String examplify(String example) {
        return fakeValuesService.examplify(example);
    }

    /**
     * Returns a string with the char2replace characters in the parameter replaced with random option from available options.
     * <p>
     * For example, the string "ABC??EFG" could be replaced with a string like "ABCtestтестEFG"
     * if passed options are new String[]{"test", "тест"}.
     *
     * @param string       Template for string generation
     * @param char2replace Char to replace
     * @param options      Options to use while filling the template
     * @return Generated string
     */
    public String templatify(String string, char char2replace, String... options) {
        return fakeValuesService().templatify(string, char2replace, options);
    }

    /**
     * Returns a string with the characters in the keys of optionsMap parameter replaced with random option from values.
     *
     * <p>
     * For example, the string "ABC$$EFG" could be replaced with a string like "ABCtestтестEFG"
     * if passed for key '$' there is value new String[]{"test", "тест"} in optionsMap
     *
     * @param string     Template for string generation
     * @param optionsMap Map with replacement rules
     * @return Generated string
     */
    public String templatify(String string, Map<Character, String[]> optionsMap) {
        return fakeValuesService().templatify(string, optionsMap);
    }


    public RandomService random() {
        return this.randomService;
    }

    FakeValuesService fakeValuesService() {
        return this.fakeValuesService;
    }

    /**
     * Allows to add paths to files with custom data. Data should be in YAML format.
     *
     * @param locale        the locale for which a path is going to be added.
     * @param path          path to a file with YAML structure
     * @throws IllegalArgumentException in case of invalid path
     */
    public void addPath(Locale locale, Path path) {
        fakeValuesService().addPath(locale, path);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getProvider(Class<T> clazz, Supplier<T> valueSupplier) {
        T result = (T) providersMap.get(clazz);
        if (result == null) {
            providersMap.putIfAbsent(clazz, valueSupplier.get());
            result = (T) providersMap.get(clazz);
        }
        return result;
    }

    public String resolve(String key) {
        return this.fakeValuesService.resolve(key, this, this);
    }

    /**
     * Allows the evaluation of native YML expressions to allow you to build your
     * own.
     * <p>
     * The following are valid expressions:
     * <ul>
     * <li>#{regexify '(a|b){2,3}'}</li>
     * <li>#{regexify '\\.\\*\\?\\+'}</li>
     * <li>#{bothify '????','false'}</li>
     * <li>#{Name.first_name} #{Name.first_name} #{Name.last_name}</li>
     * <li>#{number.number_between '1','10'}</li>
     * </ul>
     *
     * @param expression (see examples above)
     * @return the evaluated string expression
     * @throws RuntimeException if unable to evaluate the expression
     */
    public String expression(String expression) {
        return this.fakeValuesService.expression(expression, this);
    }

}
