package com.ning.jdbi.metrics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.skife.jdbi.v2.ClasspathStatementLocator;
import org.skife.jdbi.v2.StatementContext;

public final class JdbiGroupStrategies
{
    public static final JdbiGroupStrategy CHECK_EMPTY = new CheckEmptyStrategy();
    public static final JdbiGroupStrategy CHECK_RAW = new CheckRawStrategy();
    public static final JdbiGroupStrategy SQL_OBJECT = new SqlObjectStrategy();
    public static final JdbiGroupStrategy NAIVE_NAME  = new NaiveNameStrategy();
    public static final JdbiGroupStrategy CONTEXT_CLASS  = new ContextClassStrategy();
    public static final JdbiGroupStrategy CONTEXT_NAME  = new ContextNameStrategy();

    private JdbiGroupStrategies()
    {
    }

    /** An empty SQL statement. */
    private static final StatementName EMPTY_SQL = new StatementName("sql", "empty", "");

    /** Unknown SQL. */
    private static final StatementName UNKNOWN_SQL = new StatementName("sql", "unknown", "");

    /** Context element for JMX class. */
    public static final String STATEMENT_CLASS = "_jmx_class";

    /** Context element for JMX group. */
    public static final String STATEMENT_GROUP = "_jmx_group";

    /** Context element for JMX name. */
    public static final String STATEMENT_NAME = "_jmx_name";

    private static StatementName forRawSql(final String rawSql)
    {
        return new StatementName("sql", "raw", rawSql);
    }

    public abstract static class BaseJdbiGroupStrategy implements JdbiGroupStrategy
    {
        private final List<JdbiGroupStrategy> strategies = new ArrayList<JdbiGroupStrategy>();

        protected BaseJdbiGroupStrategy(final JdbiGroupStrategy ... strategies)
        {
            registerStrategies(strategies);
        }

        protected void registerStrategies(final JdbiGroupStrategy ... strategies)
        {
            this.strategies.addAll(Arrays.asList(strategies));
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            if (strategies != null) {
                for (JdbiGroupStrategy strategy : strategies)
                {
                    StatementName statementName = strategy.getStatementName(statementContext);
                    if (statementName != null) {
                        return statementName;
                    }
                }
            }

            return UNKNOWN_SQL;
        }
    }

    static final class CheckEmptyStrategy implements JdbiGroupStrategy
    {
        private CheckEmptyStrategy()
        {
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final String rawSql = statementContext.getRawSql();

            if (rawSql == null || rawSql.length() == 0) {
                return EMPTY_SQL;
            }
            return null;
        }
    }

    static final class CheckRawStrategy implements JdbiGroupStrategy
    {
        private CheckRawStrategy()
        {
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final String rawSql = statementContext.getRawSql();

            if (ClasspathStatementLocator.looksLikeSql(rawSql)) {
                return forRawSql(rawSql);
            }
            return null;
        }
    }

    static final class NaiveNameStrategy implements JdbiGroupStrategy
    {
        private NaiveNameStrategy()
        {
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final String rawSql = statementContext.getRawSql();

            // Is it using the template loader?
            int colon = rawSql.indexOf(':');

            if (colon == -1) {
                // No package? Just return the name, JDBI figured out somehow on how to find the raw sql for this statement.
                return forRawSql(rawSql);
            }

            final String group = rawSql.substring(0, colon);
            final String name = rawSql.substring(colon + 1);
            return new StatementName(group, name, "");
        }
    }

    static final class SqlObjectStrategy implements JdbiGroupStrategy
    {
        private SqlObjectStrategy()
        {
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final Class<?> clazz = statementContext.getSqlObjectType();
            final Method method = statementContext.getSqlObjectMethod();
            if (clazz != null) {
                final String rawSql = statementContext.getRawSql();

                final String group = clazz.getPackage().getName();
                final String name = clazz.getSimpleName();
                final String type = method == null ? rawSql : method.getName();
                return new StatementName(group, name, type);
            }
            return null;
        }
    }

    static final class ContextClassStrategy implements JdbiGroupStrategy
    {
        private ContextClassStrategy()
        {
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final Object classObj = statementContext.getAttribute(STATEMENT_CLASS);
            final Object nameObj = statementContext.getAttribute(STATEMENT_NAME);

            if (classObj == null || nameObj == null) {
                return null;
            }

            final String className = (String) classObj;
            final String statementName = (String) nameObj;

            final int dotPos = className.lastIndexOf('.');
            if (dotPos == -1) {
                return null;
            }

            return new StatementName(className.substring(0, dotPos), className.substring(dotPos+1), statementName);
        }
    }

    static final class ContextNameStrategy implements JdbiGroupStrategy
    {
        /** File pattern to shorten the group name. */
        private static final Pattern SHORT_PATTERN = Pattern.compile("^(.*?)/(.*?)-sql\\.st$");

        private ContextNameStrategy()
        {
        }

        @Override
        public StatementName getStatementName(final StatementContext statementContext)
        {
            final Object groupObj = statementContext.getAttribute(STATEMENT_GROUP);
            final Object nameObj = statementContext.getAttribute(STATEMENT_NAME);

            if (groupObj == null || nameObj == null) {
                return null;
            }

            final String group = (String) groupObj;
            final String statementName = (String) nameObj;

            final Matcher matcher = SHORT_PATTERN.matcher(group);
            if (matcher.matches()) {
                String groupName = matcher.group(1);
                String typeName = matcher.group(2);
                return new StatementName(groupName, typeName, statementName);
            }

            return new StatementName(group, statementName, "");
        }
    }
}
