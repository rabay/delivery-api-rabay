package com.deliverytech.delivery_api.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class UserMetrics {

  private final Counter userRegisteredCounter;
  private final Counter userLoginCounter;
  private final Counter userLoginFailedCounter;
  private final Timer userLoginTimer;

  public UserMetrics(MeterRegistry meterRegistry) {
    // User counters
    userRegisteredCounter =
        Counter.builder("users.registered")
            .description("Number of users registered")
            .register(meterRegistry);

    userLoginCounter =
        Counter.builder("users.login.success")
            .description("Number of successful user logins")
            .register(meterRegistry);

    userLoginFailedCounter =
        Counter.builder("users.login.failed")
            .description("Number of failed user logins")
            .register(meterRegistry);

    // User login timer
    userLoginTimer =
        Timer.builder("user.login.time")
            .description("Time taken for user login")
            .register(meterRegistry);
  }

  public void incrementUserRegistered() {
    userRegisteredCounter.increment();
  }

  public void incrementUserLogin() {
    userLoginCounter.increment();
  }

  public void incrementUserLoginFailed() {
    userLoginFailedCounter.increment();
  }

  public Timer.Sample startUserLoginTimer() {
    return Timer.start();
  }

  public void recordUserLoginTime(Timer.Sample sample) {
    sample.stop(userLoginTimer);
  }
}
