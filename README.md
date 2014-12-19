instrumental-agent-java
=======================

A java agent library for the [Instrumental] logging service without using Statsd.

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
