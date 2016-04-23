# Instrumental Java Agent

A minimal native Java agent for the [Instrumental](https://instrumentalapp.com/) service.

## Usage

```Java
import com.eg.instrumental.*;

Agent agent = new Agent(new AgentOptions().setApiKey("YOUR_API_KEY"));

agent.increment("myapp.login");
agent.gauge("heap_free", 8675309);
agent.time("some.longProcess", new Runnable() {
  public void run() {
    // Do something....
  });
agent.notice("Maintenance Now.");
```
