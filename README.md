# poketype

Run program with  `lein run`.

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
