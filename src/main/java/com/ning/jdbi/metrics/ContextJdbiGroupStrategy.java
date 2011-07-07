package com.ning.jdbi.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.skife.jdbi.v2.StatementContext;

/**
 * Shortens names provided by the AnnotatedDBI Providers to look nice in JMX.
 */
public class ContextJdbiGroupStrategy extends NaiveJdbiGroupStrategy
{
    public static final String STATEMENT_GROUP = "_jmx_group";
    public static final String STATEMENT_NAME = "_jmx_name";


    private static final Pattern SHORT_PATTERN = Pattern.compile("^.*?/(.*?)-sql\\.st$");

    private final ConcurrentMap<String, String> shortGroups = new ConcurrentHashMap<String, String>();

    @Override
    public StatementName getStatementName(final StatementContext statementContext)
    {
        final Object groupObj = statementContext.getAttribute(STATEMENT_GROUP);
        final Object nameObj = statementContext.getAttribute(STATEMENT_NAME);

        if (groupObj == null || nameObj == null) {
            return super.getStatementName(statementContext);
        }

        final String group = (String) groupObj;
        final String name = (String) nameObj;


        final Matcher matcher = SHORT_PATTERN.matcher(group);
        if (matcher.matches()) {
            final String newShortGroup = matcher.group(1);

            // So we have a short name. Figure out whether this might actually be already taken.
            final String oldGroup = shortGroups.putIfAbsent(newShortGroup, group);
            if (oldGroup == null || oldGroup.equals(group)) {
                return new StatementName(newShortGroup, name, "");
            }
        }

        return new StatementName(group, name, "");
    }
}
