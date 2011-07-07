package com.ning.jdbi.metrics;

import org.skife.jdbi.v2.StatementContext;

/**
 * Allows shortening of a group name to make a nice view in jconsole.
 */
public interface JdbiGroupStrategy
{
    StatementName getStatementName(StatementContext statementContext);
}


