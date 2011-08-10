package com.ning.jdbi.metrics;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;


public class SqlJdbiGroupStrategy extends BaseJdbiGroupStrategy
{
    public SqlJdbiGroupStrategy()
    {
        super(JdbiGroupStrategies.CHECK_EMPTY,
              JdbiGroupStrategies.SQL_OBJECT);
    }
}
