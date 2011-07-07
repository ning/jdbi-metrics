package com.ning.jdbi.metrics;

public class StatementName
{
    private final String groupName;
    private final String typeName;
    private final String statementName;

    public StatementName(String groupName, String typeName, String statementName)
    {
        this.groupName = groupName;
        this.typeName = typeName;
        this.statementName = statementName;
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
}
