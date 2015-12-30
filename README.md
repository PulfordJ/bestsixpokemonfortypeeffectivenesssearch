# poketype

Run program with  `lein run`.

Static copy of results from testing entire single-type and dual-type effectiveness from Gen VI (same-type-attack-bonus included):
```clojure
([(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :ice :dragon :fairy) 369.0]
 [(:fight :flying :ground :rock :bug :ghost :steel :fire :grass :ice :dragon :fairy) 369.0])
"Elapsed time: 14535.268226 msec
```

Static copy of results from testing entire single-type and dual-type effectiveness from Gen VI:

```clojure
([(:fight :flying :poison :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :ghost :fire :water :grass :ice :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :electric :ice :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :psychic :ice :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :steel :fire :grass :ice :dark :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :fire :water :grass :ice :dark :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :fire :grass :electric :ice :dark :fairy) 338]
 [(:fight :flying :poison :ground :rock :bug :fire :grass :psychic :ice :dark :fairy) 338]
 [(:fight :flying :ground :rock :bug :ghost :steel :fire :water :grass :ice :fairy) 338]
 [(:fight :flying :ground :rock :bug :ghost :steel :fire :grass :electric :ice :fairy) 338]
 [(:fight :flying :ground :rock :bug :ghost :steel :fire :grass :psychic :ice :fairy) 338]
 [(:fight :flying :ground :rock :bug :steel :fire :water :grass :ice :dark :fairy) 338]
 [(:fight :flying :ground :rock :bug :steel :fire :grass :electric :ice :dark :fairy) 338]
 [(:fight :flying :ground :rock :bug :steel :fire :grass :psychic :ice :dark :fairy) 338])
"Elapsed time: 13743.341593 msecs"
```
First 8 types preference for maximum coverage (In order of best n-types-of-coverage. For example if only one pokemon was acquired ice would provide the best type effectiveness.:

```clojure
[
:ice
:ground
:rock
:fight
:grass
:fire
:bug
:fairy
]
```

Static copy of results from testing entire single-type effectiveness space from Gen VI:

```clojure
([(:poison :ground :rock :grass :dark :fairy)
  [(1 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 35]]
 [(:poison :ground :rock :ghost :grass :fairy)
  [(1 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 35]]
 [(:fight :flying :ground :electric :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fight :flying :ground :grass :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fight :flying :ground :steel :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]]
 [(:fight :flying :ground :ghost :electric :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fight :flying :ground :ghost :grass :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fight :flying :ground :ghost :steel :ice)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]]
 [(:fight :flying :poison :ground :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]]
 [(:fight :flying :poison :ground :ghost :ice)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]])
```

Also results from Gen V (Before Fairy and minor effectiveness changes in table).
```clojure
([(:fight :flying :ground :electric :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]]
 [(:fight :flying :ground :grass :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]]
 [(:fight :flying :ground :ghost :electric :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]]
 [(:fight :flying :ground :ghost :grass :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]])
```
The project uses [Midje](https://github.com/marick/Midje/).

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
