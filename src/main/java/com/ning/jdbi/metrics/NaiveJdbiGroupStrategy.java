package com.ning.jdbi.metrics;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;


/**
 * Very simple strategy, can be used with any JDBI loader to build basic statistics.
 */
public class NaiveJdbiGroupStrategy extends BaseJdbiGroupStrategy
{
    public NaiveJdbiGroupStrategy()
    {
        super(JdbiGroupStrategies.CHECK_EMPTY,
              JdbiGroupStrategies.CHECK_RAW,
              JdbiGroupStrategies.NAIVE_NAME);
    }
}
