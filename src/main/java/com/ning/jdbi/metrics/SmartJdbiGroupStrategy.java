package com.ning.jdbi.metrics;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;


/**
 * Adds statistics for JDBI queries that set the
 * {@link JdbiGroupStrategies#STATEMENT_CLASS} and
 * {@link JdbiGroupStrategies#STATEMENT_NAME} for class based display or
 * {@link JdbiGroupStrategies#STATEMENT_GROUP} and
 * {@link JdbiGroupStrategies#STATEMENT_NAME} for group based display.
 *
 * Also knows how to deal with SQL Object statements.
 */
public class SmartJdbiGroupStrategy extends BaseJdbiGroupStrategy
{
    public SmartJdbiGroupStrategy()
    {
        super(JdbiGroupStrategies.CHECK_EMPTY,
              JdbiGroupStrategies.CONTEXT_CLASS,
              JdbiGroupStrategies.CONTEXT_NAME,
              JdbiGroupStrategies.SQL_OBJECT,
              JdbiGroupStrategies.CHECK_RAW,
              JdbiGroupStrategies.NAIVE_NAME);
    }
}
