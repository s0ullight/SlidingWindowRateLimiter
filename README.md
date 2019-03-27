SlidingWindowRateLimiter
------------------------

This class is a simple approach to sliding window rate limiters.
SlidingWindowRateLimiter is thread safe and doesn't use external libraries.


The constructor takes three parameters:
    - double maxPermits: The maximum number of permits that should be granted during the window
    - long window: The window in which the permits should be rate limited
    - TimeUnit timeUnit: The time unit in which the window is expressed


Usage
-----

1.
Create an instance using the constructor, e.g.:
    SlidingWindowRateLimiter sWRL = new SlidingWindowRateLimiter(5, 1, TimeUnit.MINUTES);

2.
Use the acquire() method to request a permit:
    sWRL.acquire();

In case your request should consume more permits, you can pass it as a parameter in the form of a double:
    sWRL.acquire(5);


Notes
-----

In order to keep this simple, accuracy was sacrificed. While the inaccuracy should be negligible,
with higher number of permits in smaller windows this inaccuracy may be problematic.