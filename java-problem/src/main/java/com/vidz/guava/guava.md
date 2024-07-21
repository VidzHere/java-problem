Guava is a set of core Java libraries from Google that provides a variety of APIs for developers. 
These APIs include utilities for:
1.Collections: Common operations like multimaps and multisets, and immutable collections
2.Concurrency: Tools for concurrency
3.Data types: Extensions to primitive data types like unsigned bytes, integers, and longs, plus boxed wrappers and basic functions
4.Hashing: Sophisticated hashes like Bloom filters, and related structures
5.I/O: Working with input and output streams, readers, writers, and files
6.Math: Optimized math utilities not provided by the JDK
7.Net addresses: Working with numeric IP addresses and domain names
8.Primitives: Static utilities for working with the eight primitive types and void
9.Reflection: Working with Java reflection
10.Strings: Utilities for splitting, joining, and padding
------------------------------------------------------
Caffeine versus Guava cache
------------------------------------------------------
The main difference is because Caffeine uses ring buffers to record & replay events, whereas Guava uses ConcurrentLinkedQueue. 
The intent was always to migrate Guava over and it made sense to start simpler, but unfortunately there was never interest in accepting those changes. 
The ring buffer approach avoids allocation, is bounded (lossy), and cheaper to operate against.
The remaining costs are due to a design mismatch. The original author of MapMaker was enthusiastic about soft references as the solution to caching problems
by deferring it to the GC. Unfortunately while that can seem fast in microbenchmarks, it has horrible performance in practice due to causing stop-the-world GC 
thrashing. The size-based solution had to be adapted into this work and that is not ideal. Caffeine optimizes for size-based and also gains an improved hash table, 
whereas Guava handles reference caching more elegantly.
Caffeine doesn't create its own threads for maintenance or expiration. It does defer the cost to the commonPool, which slightly improves user-facing latencies
but not throughput. A future version might leverage CompletableFuture.delayedExecutor to schedule the next expiration event without directly creating threads 
(for users who have business logic depending on prompt removal notifications).

ConcurrentLinkedHashMap and MapMaker were written at the same time and CLHM has similar performance to Caffeine. I believe the difference is due to what 
scenarios the designers favored and optimized for, which impacted how other features would be implemented. There is low hanging fruit to allow Guava to have 
similar performance profile, but there isn't an internal champion to drive that (and even less so with Caffeine as a favored alternative).
------------------------------------------------------
Guava vs Apache
------------------------------------------------------
1. Bi-directional Maps
Maps that can be accessed by their keys, as well as values, are known as bi-directional maps. JCF does not have this feature.

Let’s see how our two technologies offer them. In both cases, we’ll take an example of days of the week to get the name of the day given its number and vice-versa.
Guava

BiMap<Integer, String> daysOfWeek = HashBiMap.create();
daysOfWeek.put(7, "Sunday");
@Test
public void givenBiMap_whenValue_thenKeyReturned() {
assertEquals(Integer.valueOf(7), daysOfWeek.inverse().get("Sunday"));
}

@Test
public void givenBiMap_whenKey_thenValueReturned() {
assertEquals("Tuesday", daysOfWeek.get(2));
}
Apache
BidiMap<Integer, String> daysOfWeek = new TreeBidiMap<Integer, String>();
@Test
public void givenBidiMap_whenValue_thenKeyReturned() {
assertEquals(Integer.valueOf(7), daysOfWeek.inverseBidiMap().get("Sunday"));
}

@Test
public void givenBidiMap_whenKey_thenValueReturned() {
assertEquals("Tuesday", daysOfWeek.get(2));
}
-------------------
2.MultiValuedMap: