package com.ning.jdbi.metrics;

import java.util.concurrent.TimeUnit;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.TimingCollector;

import com.yammer.metrics.MetricsRegistry;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.TimerMetric;

public class MetricsTimingCollector implements TimingCollector
{
    private final MetricsRegistry metricsRegistry;
    private final JdbiGroupStrategy jdbiGroupStrategy;
    private final TimeUnit durationUnit;
    private final TimeUnit rateUnit;

    public MetricsTimingCollector(final MetricsRegistry metricsRegistry,
                                    final JdbiGroupStrategy jdbiGroupStrategy,
                                    final TimeUnit durationUnit,
                                    final TimeUnit rateUnit)
    {
        this.metricsRegistry = metricsRegistry;
        this.jdbiGroupStrategy = jdbiGroupStrategy;
        this.durationUnit = durationUnit;
        this.rateUnit = rateUnit;
    }

    @Override
    public void collect(long elapsedTime, StatementContext ctx)
    {
        StatementName statementName = jdbiGroupStrategy.getStatementName(ctx);
        TimerMetric timer = metricsRegistry.newTimer(new MetricName(statementName.getGroupName(), statementName.getTypeName(), statementName.getStatementName()),
                                                     durationUnit,
                                                     rateUnit);

        timer.update(elapsedTime, TimeUnit.NANOSECONDS);
    }
}
