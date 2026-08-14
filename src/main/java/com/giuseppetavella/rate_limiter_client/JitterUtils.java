package com.giuseppetavella.rate_limiter_client;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility methods for applying statistical noise/jitter (sigma variations) 
 * to numerical values (e.g., backoff intervals, rate limits, retry delays).
 */
public final class JitterUtils {

    private JitterUtils() {
        // Private constructor to prevent instantiation of static utility class
    }

    /**
     * Applies a percentage-based jitter (sigma) variation to an integer amount.
     * The variation can be both positive or negative within [-jitterPrc, +jitterPrc].
     *
     * @param amount    the base value
     * @param jitterPrc the maximum percentage variation (e.g., 0.15 for +/- 15%)
     * @return the jittered amount as a rounded integer
     */
    public static int addJitter(int amount, double jitterPrc) {
        if (amount == 0 || jitterPrc <= 0.0) {
            return amount;
        }

        // Generate a random factor in range [-jitterPrc, +jitterPrc]
        double factor = ThreadLocalRandom.current().nextDouble(-jitterPrc, jitterPrc);
        double delta = amount * factor;

        return (int) Math.round(amount + delta);
    }

    /**
     * Applies a percentage-based jitter to a double amount.
     *
     * @param amount    the base value
     * @param jitterPrc the maximum percentage variation (e.g., 0.10 for +/- 10%)
     * @return the jittered amount as a double
     */
    public static double addJitter(double amount, double jitterPrc) {
        if (amount == 0.0 || jitterPrc <= 0.0) {
            return amount;
        }

        double factor = ThreadLocalRandom.current().nextDouble(-jitterPrc, jitterPrc);
        return amount + (amount * factor);
    }

    /**
     * Applies an absolute range variation (+/- absoluteAmount) rather than a percentage.
     *
     * @param amount     the base value
     * @param maxAbsoluteDelta the max absolute variation (e.g., +/- 50 ms)
     * @return the jittered amount as an integer
     */
    public static int addAbsoluteJitter(int amount, int maxAbsoluteDelta) {
        if (maxAbsoluteDelta <= 0) {
            return amount;
        }

        int delta = ThreadLocalRandom.current().nextInt(-maxAbsoluteDelta, maxAbsoluteDelta + 1);
        return amount + delta;
    }
}