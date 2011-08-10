package com.ning.jdbi.metrics;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;


/**
/**
 * Adds statistics for JDBI queries that set the
 * {@link JdbiGroupStrategies#STATEMENT_GROUP} and
 * {@link JdbiGroupStrategies#STATEMENT_NAME} for group based display.
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
