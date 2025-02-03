# Advent of Code

## Download Script
From the [wiki about Automation](https://old.reddit.com/r/adventofcode/wiki/faqs/automation), the download script does the following:
* Download logic is located at [AoCClient.java](src/com/dwelguisz/base/AoCClient.java)
* Outbound calls are throttled to every 15 minutes. Next allowed time is stored locally. Done with `allowedTime`
* When input has been gathered, cached locally in a `.gitignore` folder under `resources`.
* The `User-Agent` header is set to me since I maintain this tool.

## Overview
After completing all puzzles, I am going through my repo and trying to clean it 
up to make it more readable. As I go through it, I will be updating my thoughts
and put the general solution in this repository. During the actual event, I will
not update the current year's solution thoughts until January of the next year 
(e.g. Advent of Code 2022 README's will not be available until January 2023 at 
the earliest.)

## Year 2015
* [Day 1, Not Quite Lisp](docs/year2015/day01/README.md)
* [Day 2, I Was Told There Would Be No Math](docs/year2015/day02/README.md)
* [Day 3, Perfectly Spherical Houses In a Vacuum](docs/year2015/day03/README.md)
* [Day 4, The Ideal Stocking Stuffer](docs/year2015/day04/README.md)
* [Day 5, Doesn't He Have Intern-Elves For This?](docs/year2015/day05/README.md)
* [Day 6, Probably a Fire Hazard](docs/year2015/day06/README.md)
* [Day 7, Some Assembly Required](docs/year2015/day07/README.md)
* [Day 8, Matchsticks](docs/year2015/day08/README.md)
* [Day 9, All In A Single Night](docs/year2015/day09/README.md)
* [Day 10, Elves Look, Elves Say](docs/year2015/day10/README.md)
* [Day 11, Corporate Policy](docs/year2015/day11/README.md)
* [Day 12, JSAbacusFramework.io](docs/year2015/day12/README.md)
* [Day 13, Knights of the Dinner Table](docs/year2015/day13/README.md)
* [Day 14, Reindeer Olympics](docs/year2015/day14/README.md)
* [Day 15, Science for Hungry People](docs/year2015/day15/README.md)
* [Day 16, Aunt Sue](docs/year2015/day16/README.md)
* [Day 17, No Such Thing as Too Much](docs/year2015/day17/README.md)
* [Day 18, Like A GIF For Your Yard](docs/year2015/day18/README.md)

## Year 2022
* [Day 1, Calorie Counting](docs/year2022/day01/README.md)
* [Day 2, Rock Paper Scissors](docs/year2022/day02/README.md)
* [Day 3, Rucksack Reorganization](docs/year2022/day03/README.md)
* [Day 4, Camp Cleanup](docs/year2022/day04/README.md)
* [Day 5, Supply Stacks](docs/year2022/day05/README.md)
* [Day 6, Tuning Trouble](docs/year2022/day06/README.md)
* [Day 16, Proboscidea Volcanium](docs/year2022/day16/README.md)

## Puzzles to come back to

### Year 2015
* WizardSimulator20XX, Day 22

### Year 2016
* RadioisotepeThermoelectricGenerators, Day 11
* Safe Cracking, Day 23

### Year 2018
* Mine Cart Madness, Day 13
* Mode Maze, Day 22

### Year 2019
* May Worlds Interpreation, Day 18, Part 2. My runs, but takes forever. Good one to come back to optimize this one.

### Year 2022
* Monkey Map, Day 22