# Instrumental Java Agent

A native Java agent for the [Instrumental](https://instrumentalapp.com/) service.

## Setup & Usage

Add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>com.instrumentalapp</groupId>
  <artifactId>instrumental_agent</artifactId>
  <version>0.0.1</version>
</dependency>
```

Visit [instrumentalapp.com](https://instrumentalapp.com) and create an account, then initialize the agent with your API key, found in the Docs section.

```Java
import com.instrumentalapp.*;

Agent agent = new Agent(new AgentOptions().setApiKey("YOUR_API_KEY").setEnabled(isProduction));
```

You'll probably want something like the above, only enabling the agent in production mode so you don't have development and production data writing to the same value. Or you can setup two projects, so that you can verify stats in one, and release them to production in another.

Now you can begin to use Instrumental to track your application.

```Java
agent.gauge("load", 1.23);     // value at a point in time

agent.increment("signups");    // increasing value, think "events"

agent.time("query_time", new Runnable() {  // time execution
  public void run() {
    // Do something....
  });
agent.timeMs("query_time", new Runnable() {  // prefer milliseconds?
  public void run() {
    // Do something....
  });
```

**Note**: For your app's safety, the agent is meant to isolate your app from any problems our service might suffer. If it is unable to connect to the service, it will discard data after reaching a low memory threshold.

Want to track an event (like an application deploy, or downtime)? You can capture events that are instantaneous, or events that happen over a period of time.

```Java
agent.notice('Jeffy deployed rev ef3d6a')  // instantaneous event
agent.notice('Testing socket buffer increase', System.currentTimeMillis() / 1000 - 60 * 10, 60*10) // an event with a duration
```


## Server Stats

Want some general server stats (load, memory, etc.)? Check out the [instrumental_tools](https://github.com/expectedbehavior/instrumental_tools) gem.

```sh
gem install instrumental_tools
instrument_server
```


## Troubleshooting & Help

We are here to help. Email us at [support@instrumentalapp.com](mailto:support@instrumentalapp.com).
