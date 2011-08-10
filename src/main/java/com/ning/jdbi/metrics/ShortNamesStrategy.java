package com.ning.jdbi.metrics;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.skife.jdbi.v2.StatementContext;

import com.ning.jdbi.metrics.JdbiGroupStrategies.BaseJdbiGroupStrategy;

/**
 * Assembles all JDBI stats under a common prefix (passed in at c'tor time). Stats are grouped by class name and method; a shortening strategy is applied
 * to make the JMX output nicer.
 *
 */
public final class ShortNamesStrategy extends BaseJdbiGroupStrategy
{
    private final ConcurrentMap<String, String> shortClassNames = new ConcurrentHashMap<String, String>();

    private final String baseJmxName;

    public ShortNamesStrategy(final String baseJmxName)
    {
        this.baseJmxName = baseJmxName;

        // Java does not allow super (..., new ShortContextClassStrategy(), new ShortSqlObjectStrategy(), ...);
        // ==> No enclosing instance of type <xxx> is available due to some intermediate constructor invocation. Lame.
        registerStrategies(JdbiGroupStrategies.CHECK_EMPTY,
                           new ShortContextClassStrategy(),
                           new ShortSqlObjectStrategy(),
                           JdbiGroupStrategies.CHECK_RAW,
                           JdbiGroupStrategies.NAIVE_NAME);
    }

    private final class ShortContextClassStrategy implements JdbiGroupStrategy
    {

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final Object classObj = statementContext.getAttribute(JdbiGroupStrategies.STATEMENT_CLASS);
            final Object nameObj = statementContext.getAttribute(JdbiGroupStrategies.STATEMENT_NAME);

            if (classObj == null || nameObj == null) {
                return null;
            }

            final String className = (String) classObj;
            final String statementName = (String) nameObj;

            final int dotPos = className.lastIndexOf('.');
            if (dotPos == -1) {
                return null;
            }

            final String shortName = className.substring(dotPos+1);

            final String oldClassName = shortClassNames.putIfAbsent(shortName, className);
            if (oldClassName == null || oldClassName.equals(className)) {
                return new StatementName(baseJmxName, shortName, statementName);
            }
            else {
                return new StatementName(baseJmxName, className, statementName);
            }
        }
    }

    private final class ShortSqlObjectStrategy implements JdbiGroupStrategy
    {
        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final Class<?> clazz = statementContext.getSqlObjectType();
            final Method method = statementContext.getSqlObjectMethod();
            if (clazz != null && method != null) {
                final String className = clazz.getName();
                final String statementName = method.getName();

                final int dotPos = className.lastIndexOf('.');
                if (dotPos == -1) {
                    return null;
                }

                final String shortName = className.substring(dotPos+1);

                final String oldClassName = shortClassNames.putIfAbsent(shortName, className);
                if (oldClassName == null || oldClassName.equals(className)) {
                    return new StatementName(baseJmxName, shortName, statementName);
                }
                else {
                    return new StatementName(baseJmxName, className, statementName);
                }
            }
            return null;
        }
    }
}
