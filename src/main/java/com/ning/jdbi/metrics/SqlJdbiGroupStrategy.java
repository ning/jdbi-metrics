package com.ning.jdbi.metrics;

import java.lang.reflect.Method;

import org.skife.jdbi.v2.StatementContext;

public class SqlJdbiGroupStrategy implements JdbiGroupStrategy
{
    private static final StatementName EMPTY_SQL = new StatementName("sql", "empty", "");

    @Override
    public StatementName getStatementName(StatementContext statementContext)
    {
        String rawSql = statementContext.getRawSql();

        if (rawSql == null || rawSql.length() == 0) {
            return EMPTY_SQL;
        }
        else {
            Class<?> clazz = statementContext.getSqlObjectType();
            Method method = statementContext.getSqlObjectMethod();
            String group = clazz == null ? "sql" : clazz.getPackage().getName();
            String type = clazz == null ? "undefined" : ContextJdbiGroupStrategy.getJmxSafeName(clazz.getSimpleName());
            String name = ContextJdbiGroupStrategy.getJmxSafeName(method == null ? rawSql : method.getName());

            return new StatementName(group, type, name);
        }
    }
}
