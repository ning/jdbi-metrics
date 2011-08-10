package com.ning.jdbi.metrics;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;


/**
 * Shortens names provided by the AnnotatedDBI Providers to look nice in JMX.
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
