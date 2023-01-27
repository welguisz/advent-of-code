# Day 14: Reindeer Olympics

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy/Medium

Input: List of Reindeers with their flying time, speed, resting time

## Parsing
The parsing is straightforward. Will be storing the information in each line in a
class called `Reindeer`. Reindeer will contain the Reindeer's name, its speed, its
flying time, and its rest time.

In this class, I will add the following function `distanceTraveled(int time)` that will
calculate the distance travelled.

## Part 1
Since everything is being done in the class, we need to know the state of the
reindeer.  Since we need to go 2503 seconds, we fly for so many seconds, then rest
for another period of time. This will make the function a loop that counts down
the number of seconds and when we reach 0, stop.

```java
        public Integer distanceTraveled(int seconds) {
            Integer distance = 0;
            boolean flying = true;
            while (seconds > 0) {
                if (flying) {
                    Integer flyingTime = (seconds > flyTime) ? flyTime : seconds;
                    distance += speed * flyingTime;
                    seconds -= flyingTime;
                    flying = false;
                } else {
                    seconds -= restTime;
                    flying = true;
                }
            }
            return distance;
        }
```

From this, we can just write a simple streaming line to get the max:

```java
    public Integer findWinnerDistance(Integer seconds) {
        return reindeers.stream().mapToInt(r -> r.distanceTraveled(seconds)).max().getAsInt();
    }
```

## Part 2
In Part2, we can't just take any huge steps. We will have to step through each
second, see who is in the lead and go from there.  This adds four class variables:
* `distanceTravelledInSecondRace`: Total distance travelled in the second race
* `currentlyFlying`: Are we currently flying at this second?
* `timeLeftInPeriod`: Time left for flying or rest
* `currentPoints`: current points earned by the Reindeer

By adding these variables, we create the following functions:

```java
        public void stepOneSecond() {
            if (currentlyFlying) {
                distanceTraveledInSecondRace += speed;
            }
            timeLeftInPeriod--;
            if (timeLeftInPeriod == 0) {
                timeLeftInPeriod = currentlyFlying ? restTime : flyTime;
                currentlyFlying ^= true;
            }
        }

        public Integer getDistanceTraveledInSecondRance() {
            return distanceTraveledInSecondRace;
        }

        public void incPoints() {
            currentPoints++;
        }

        public Integer getCurrentPoints() {
            return currentPoints;
        }
```

The bulk of the work is done in `stepOneSecond` where distances are updated and
checks to see if we need to change from flying to rest and vice versa.

By using these functions, we can write part 2 as:

```java
    public Integer findNewWinnerPoints(Integer seconds) {
        for (int i = 0; i < seconds; i++) {
            reindeers.stream().forEach(r -> r.stepOneSecond());
            Integer currentMaxDistance = reindeers.stream().mapToInt(Reindeer::getDistanceTraveledInSecondRance).max().getAsInt();
            reindeers.stream()
                    .filter(r -> r.getDistanceTraveledInSecondRance().equals(currentMaxDistance)).
                    forEach(Reindeer::incPoints);
        }
        return reindeers.stream().mapToInt(Reindeer::getCurrentPoints).max().getAsInt();
    }
```

|[Previous (Day 13)](../day13/README.md)|[Next (Day 15)](../day15/README.md)|