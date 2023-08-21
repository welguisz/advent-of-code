# Day 18: Medicine For Rudolph

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Medium

Input: List of Elements and their reactions.  Last String is the molefule for Rudolph's medicine

## Parsing
The reactions can be parsed into a Map<String, List<String>> that contains the reactions. The
last line can be just stored as a string.

## Part 1
This is pretty straightforward and what the initial Molecule can become next with the reaction.
Basically our code should look for an input value from our Map. When we find that value, create
the new molecule and store in a HashSet, since we are looking for the number of unique reactions.
