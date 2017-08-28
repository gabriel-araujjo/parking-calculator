package imd0412.parkinglot;

import java.time.Duration;
import java.util.*;

/**
 * Payment rule factory class
 */
public class PaymentRule {
    private TreeMap<Duration, TreeMap<Integer, Long>> priceScheme = new TreeMap<>();

    private PaymentRule() {
        this.priceScheme = new TreeMap<>();
    }

    /**
     * Calc the cost of a specific duration following this object rules
     * @param duration Period of parking
     * @return price in BRL
     */
    public Float calcCost(Duration duration) {
        long cost = 0;
        for (Map.Entry<Duration, TreeMap<Integer, Long>> periodicCost : priceScheme.entrySet()) {
            cost += calcPeriodicCost(periodicCost.getKey(), periodicCost.getValue(), duration);
        }
        return cost / 100.00f;
    }

    protected void addSchedule(Duration dur, Map<Integer, Long> periodPriceScheme) {
        TreeMap<Integer, Long> prices = new TreeMap<>(Collections.reverseOrder());
        prices.putAll(periodPriceScheme);
        priceScheme.put(dur, prices);
    }


    /**
     * Calculate the cost of duration, dividing the time in periods
     *
     * Ex.:
     * duration is 7d 20h
     * period is 1d
     * the price is {1-> 50, 7->30}
     *
     * 7d 20h / 1d = 7
     * cost = (7-7)x30 = 0
     * 7d 20h / 1d = 7
     * cost += (7-1)x50 = 300
     *
     * @param period The period division
     * @param price Price scheme for period division
     * @param duration Total duration
     * @return The cost in cents.
     */
    private long calcPeriodicCost(Duration period, TreeMap<Integer, Long> price, Duration duration) {
        long cost = 0;
        int until = (int) (duration.toMinutes() / period.toMinutes());

        // Fractional time is billed like an integer part
        if (duration.toMinutes() % period.toMinutes() > 0) {
            until++;
        }

        int notGreatTo = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Long> periodicPrice : price.entrySet()) {
            int from = periodicPrice.getKey();

            if (from >= until) continue;

            if (until > notGreatTo) {
                cost += periodicPrice.getValue() * (notGreatTo - from);
            } else {
                cost += periodicPrice.getValue() * (until - from);
            }

            notGreatTo = from;
        }
        return cost;
    }

    /**
     * Return the payment rules of a parking lot type
     * @param type The parking lot type
     * @return The payment rule relative to the parking lot type
     */
    public static PaymentRule of(ParkingLotType type) {
        if (type == null) return null;

        PaymentRule rules = getFromCache(type);
        if (rules != null) return rules;

        Map<Integer, Long> hourlyPrice =  new LinkedHashMap<>();
        Map<Integer, Long> dailyPrice =  new LinkedHashMap<>();
        Map<Integer, Long> weeklyPrice =  new LinkedHashMap<>();
        Map<Integer, Long> monthlyPrice =  new LinkedHashMap<>();

        rules = new PaymentRule();
        putOnCache(type, rules);

        switch (type) {
            case ShortTerm:
                hourlyPrice.put(0, 800L);
                hourlyPrice.put(1, 200L);

                dailyPrice.put(1, 5000L);
                dailyPrice.put(8, 3000L);

                rules.addSchedule(Duration.ofHours(1), hourlyPrice);
                rules.addSchedule(Duration.ofDays(1), dailyPrice);
                break;
            case LongTerm:
                dailyPrice.put(0, 7000L);
                dailyPrice.put(1, 5000L);
                dailyPrice.put(8, 3000L);

                monthlyPrice.put(1, 50000L);

                rules.addSchedule(Duration.ofDays(1), dailyPrice);
                rules.addSchedule(Duration.ofDays(30), monthlyPrice);
                break;
            case VIP:
                weeklyPrice.put(0, 50000L);
                weeklyPrice.put(1, 0L);

                dailyPrice.put(8, 10000L);
                dailyPrice.put(15, 8000L);

                rules.addSchedule(Duration.ofDays(1), dailyPrice);
                rules.addSchedule(Duration.ofDays(7), weeklyPrice);
                break;
        }

        return rules;
    }

    //Infinity cache. Replace this with something more sophisticated.
    private static final HashMap<ParkingLotType, PaymentRule> cache = new HashMap<>();
    private static PaymentRule getFromCache(ParkingLotType type) {
        return cache.get(type);
    }

    private static void putOnCache(ParkingLotType type, PaymentRule rules) {
        cache.put(type, rules);
    }
}
