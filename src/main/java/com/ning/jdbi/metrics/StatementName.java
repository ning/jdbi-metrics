package com.ning.jdbi.metrics;

import java.util.regex.Pattern;

public class StatementName
{
    /** Characters safe to be used in JMX names. */
    private static final Pattern JMX_SAFE_CHARS = Pattern.compile("[^a-zA-Z0-9_\\.-]");

    private final String groupName;
    private final String typeName;
    private final String statementName;

    public StatementName(String groupName, String typeName, String statementName)
    {
        this.groupName = getJmxSafeName(groupName);
        this.typeName = getJmxSafeName(typeName);
        this.statementName = getJmxSafeName(statementName);
    }

    public String getGroupName()
    {
        return groupName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public String getStatementName()
    {
        return statementName;
    }

    public String toString()
    {
        return groupName + "." + typeName + (statementName == null ? "" : "#" + statementName);
    }

    /**
     * Turns an arbitrary string into a JMX safe name.
     */
    static final String getJmxSafeName(final String name)
    {
        final String result = JMX_SAFE_CHARS.matcher(name).replaceAll("_");

        if (result == null || result.length() == 0) {
            return "_";
        }

        return (Character.isDigit(result.charAt(0))) ? "_" + result : result;
    }


}
