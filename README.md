instrumental-agent-java
=======================

A minimal java agent library for the [Instrumental] logging service without using Statsd.
This is more or less a direct port of the [Instrumental.net](https://github.com/ralphrodkey/Instrumental.NET) library.

For a more full-featured solution, take a look at using [Dropwizard Metrics](http://dropwizard.io/metrics/) with the [metrics-instrumental](http://www.gihub.com/egineering-llc/metrics-instrumental) reporter.



Features
========
 - Doesn't require Statsd
 - Works well with Java. :^)

Example
=======
 ```Java
 import com.eg.instrumental.*;

 Agent agent = new Agent("your api key");

 agent.increment("myapp.login");
 agent.gauge("heap_free", 8675309);
 agent.time("some.longProcess", new Runnable() {
    public void run() {
        // Do something....
    });
 agent.notice("Maintenance Now.", 600);
```

 [Instrumental]:http://instrumentalapp.com
