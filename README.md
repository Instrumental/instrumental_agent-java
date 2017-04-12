# Instrumental Java Agent

Instrumental is a [application monitoring platform](https://instrumentalapp.com) built for developers who want a better understanding of their production software. Powerful tools, like the [Instrumental Query Language](https://instrumentalapp.com/docs/query-language), combined with an exploration-focused interface allow you to get real answers to complex questions, in real-time.

This agent supports custom metric monitoring for Java applications. It provides high-data reliability at high scale, without ever blocking your process or causing an exception.

## Setup & Usage

Add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>com.instrumentalapp</groupId>
  <artifactId>instrumental_agent</artifactId>
  <version>1.0.0</version>
</dependency>
```

Visit [instrumentalapp.com](https://instrumentalapp.com) and create an account, then initialize the agent with your [project API token](https://instrumentalapp.com/docs/tokens).

```Java
import com.instrumentalapp.*;

Agent agent = new Agent(new AgentOptions().setApiKey("PROJECT_API_TOKEN").setEnabled(isProduction));
```

You'll probably want something like the above, only enabling the agent in production mode so you don't have development and production data writing to the same value. Or you can setup two projects, so that you can verify stats in one, and release them to production in another.

Now you can begin to use Instrumental to track your application.

```Java
agent.gauge("load", 1.23);     // value at a point in time

agent.increment("signups");    // increasing value, think "events"

agent.time("query_time", new Runnable() { // time execution
  public void run() {
    // Do something
  }});

agent.time("query_time", new Runnable() { // prefer milliseconds?
  public void run() {
    // Do something
  }});
```

**Note**: For your app's safety, the agent is meant to isolate your app from any problems our service might suffer. If it is unable to connect to the service, it will discard data after reaching a low memory threshold.

Want to track an event (like an application deploy, or downtime)? You can capture events that are instantaneous, or events that happen over a period of time.

```Java
agent.notice('Jeffy deployed rev ef3d6a');  // instantaneous event
agent.notice('Testing socket buffer increase', System.currentTimeMillis() / 1000 - 60 * 10, 60*10); // an event with a duration
```


## Server Metrics

Want server stats like load, memory, etc.? Check out [InstrumentalD](https://github.com/instrumental/instrumentald).


## Troubleshooting & Help

We are here to help. Email us at [support@instrumentalapp.com](mailto:support@instrumentalapp.com).


## Release Process

1. Pull latest master
2. Merge feature branch(es) into master
3. `script/test`
4. Increment version in code:
  - `src/main/java/com/instrumentalapp/Agent.java`
  - `pom.xml`
  - `README.md`
5. Update [CHANGELOG.md](CHANGELOG.md)
6. Commit "Release vX.Y.Z"
7. Push to GitHub
8. Tag version: `git tag 'vX.Y.Z' && git push --tags`
9. `eval $(gpg-agent --daemon)`
10. `gpg --use-agent --armor --detach-sign` and press ^C after authenticating
11. `mvn clean deploy`
12. Use the git tag and make a new release with `target/instrumental_agent-*` attached, https://github.com/instrumental/instrumental_agent-java/tags
13. Refresh documentation on instrumentalapp.com


## Version Policy

This library follows [Semantic Versioning 2.0.0](http://semver.org).
