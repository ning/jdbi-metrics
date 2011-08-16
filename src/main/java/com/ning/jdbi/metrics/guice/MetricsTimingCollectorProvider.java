package com.ning.jdbi.metrics.guice;

import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.ning.jdbi.metrics.JdbiGroupStrategy;
import com.ning.jdbi.metrics.MetricsTimingCollector;
import com.yammer.metrics.core.MetricsRegistry;

public class MetricsTimingCollectorProvider implements Provider<MetricsTimingCollector>
{
    private final MetricsRegistry metricsRegistry;
    private final JdbiGroupStrategy jdbiGroupStrategy;
    private TimeUnit durationUnit;
    private TimeUnit rateUnit;

    @Inject
    public MetricsTimingCollectorProvider(final MetricsRegistry metricsRegistry, final JdbiGroupStrategy jdbiGroupStrategy)
    {
        this.metricsRegistry = metricsRegistry;
        this.jdbiGroupStrategy = jdbiGroupStrategy;
    }

    @Inject(optional = true)
    public void setDurationUnit(@Named("MetricsTimingCollector.durationUnit") TimeUnit durationUnit)
    {
        this.durationUnit = durationUnit;
    }
    
    @Inject(optional = true)
    public void setRateUnit(@Named("MetricsTimingCollector.rateUnit") TimeUnit rateUnit)
    {
        this.rateUnit = rateUnit;
    }

    @Override
    public MetricsTimingCollector get()
    {
        return new MetricsTimingCollector(metricsRegistry,
                                          jdbiGroupStrategy,
                                          durationUnit == null ? TimeUnit.MILLISECONDS : durationUnit,
                                          rateUnit == null ? TimeUnit.SECONDS : rateUnit);
    }
}
