package com.ning.jdbi.metrics;

import java.util.regex.Pattern;

import org.skife.jdbi.v2.ClasspathStatementLocator;
import org.skife.jdbi.v2.StatementContext;

/**
 * Very simple strategy, can be used with any JDBI loader to build basic statistics.
 */
public class NaiveJdbiGroupStrategy implements JdbiGroupStrategy
{
    private static final StatementName EMPTY_SQL = new StatementName("sql", "empty", "");
    private static final StatementName RAW_SQL = new StatementName("sql", "raw", "");
    private static final Pattern SAFE_CHARS = Pattern.compile("[^a-zA-Z0-9_\\.-]");

    @Override
    public StatementName getStatementName(final StatementContext statementContext)
    {
        String rawSql = statementContext.getRawSql();

        if (rawSql == null || rawSql.length() == 0) {
            return EMPTY_SQL;
        }
        else if(ClasspathStatementLocator.looksLikeSql(rawSql)) {
            return RAW_SQL;
        }

        // Is it using the template loader?
        int colon = rawSql.indexOf(':');

        if (colon == -1) {
            // No package? Just return the name, JDBI figured out somehow on how to find the raw sql for this statement.
            return new StatementName("sql", getJmxSafeName(rawSql), "");
        }

        String group = getJmxSafeName(rawSql.substring(0, colon));
        String name = getJmxSafeName(rawSql.substring(colon + 1));

        return new StatementName(group, name, "");
    }

    public static final String getJmxSafeName(final String name)
    {
        final String result = SAFE_CHARS.matcher(name).replaceAll("_");

        if (result == null || result.length() == 0) {
            return "_";
        }

        return (Character.isDigit(result.charAt(0))) ? "_" + result : result;
    }

}
