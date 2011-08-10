package com.ning.jdbi.metrics;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;


/**
 * Shortens names provided by the AnnotatedDBI Providers to look nice in JMX.
 */
public class ContextJdbiGroupStrategy extends BaseJdbiGroupStrategy
{
    public ContextJdbiGroupStrategy()
    {
        super(JdbiGroupStrategies.CHECK_EMPTY,
              JdbiGroupStrategies.CHECK_RAW,
              JdbiGroupStrategies.CONTEXT_NAME,
              JdbiGroupStrategies.NAIVE_NAME);
    }
}
