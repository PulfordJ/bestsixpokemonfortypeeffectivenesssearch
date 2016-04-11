# poketype

Run program with  `lein run`.

Static copy of results from testing entire single-type and dual-type effectiveness from Gen VI with amount of pokemon types in loadout running from 1 to 12 (The max if all 6 pokemon are hybrids). The results are then sorted by the lowest type effectiveness score (Ice is at least 1/4 effective against every type, for example.) and then by total type effectiveness against all current single and hybrid type pokemon. This shows that with 9 pokemon types a trainer can be "super effective" (2x effective) or better against all other pokemon.:

```clojure
([(:ice) [1/4 339/2]] 
 [(:rock) [1/4 166N]] 
 [(:fire) [1/4 313/2]] 
 [(:flying) [1/4 305/2]])
([(:fight :ghost) [1N 210N]]
 [(:ground :ice) [1/2 481/2]]
 [(:fight :ice) [1/2 227N]]
 [(:ground :rock) [1/2 227N]])
([(:ground :rock :ice) [1 266N]]
 [(:ground :fire :ice) [1 264N]]
 [(:flying :ground :ice) [1 260N]]
 [(:ground :ice :fairy) [1 258N]])
([(:fight :ground :rock :ice) [1 282N]]
 [(:ground :rock :grass :ice) [1N 281N]]
 [(:ground :rock :ice :fairy) [1 281N]]
 [(:fight :ground :fire :ice) [1 280N]])
([(:fight :ground :rock :grass :ice) [1N 294N]]
 [(:ground :rock :grass :ice :fairy) [1 294N]]
 [(:fight :flying :ground :rock :ice) [1 293N]]
 [(:fight :ground :rock :bug :ice) [1 293N]])
([(:fight :ground :rock :fire :grass :ice) [1N 305N]]
 [(:ground :rock :fire :grass :ice :fairy) [1 305]]
 [(:fight :flying :ground :rock :grass :ice) [1N 304N]]
 [(:fight :ground :rock :bug :fire :ice) [1 304N]])
([(:fight :ground :rock :bug :fire :grass :ice) [1N 315N]]
 [(:fight :ground :rock :fire :grass :ice :fairy) [1 314]]
 [(:ground :rock :bug :fire :grass :ice :fairy) [1 314]]
 [(:fight :flying :ground :rock :bug :grass :ice) [1N 313N]])
([(:fight :ground :rock :bug :fire :grass :ice :fairy) [1 323]]
 [(:fight :flying :ground :rock :bug :fire :grass :ice) [1N 321N]]
 [(:fight :ground :rock :bug :ghost :fire :grass :ice) [1N 321N]]
 [(:ground :rock :bug :steel :fire :grass :ice :fairy) [1 321]])
([(:fight :poison :ground :rock :fire :grass :ice :dark :fairy) [2 326]]
 [(:fight :poison :ground :rock :fire :water :grass :dark :fairy) [2 316]]
 [(:fight :poison :ground :rock :bug :fire :grass :ice :fairy) [1 328]]
 [(:fight :ground :rock :bug :ghost :fire :grass :ice :fairy) [1 328]])
([(:fight :poison :ground :rock :bug :ghost :fire :grass :ice :fairy) [2 332]]
 [(:fight :poison :ground :rock :bug :fire :grass :ice :dark :fairy) [2 332]]
 [(:fight :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) [2 332]]
 [(:fight :ground :rock :bug :steel :fire :grass :ice :dark :fairy) [2 332]])
([(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :ice :fairy) [2 336]]
 [(:fight :flying :poison :ground :rock :bug :fire :grass :ice :dark :fairy) [2 336]]
 [(:fight :flying :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) [2 336]]
 [(:fight :flying :ground :rock :bug :steel :fire :grass :ice :dark :fairy) [2 336]])
([(:fight :flying :poison :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) [2 338]]
 [(:fight :flying :poison :ground :rock :bug :ghost :fire :water :grass :ice :fairy) [2 338]]
 [(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :electric :ice :fairy) [2 338]]
 [(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :psychic :ice :fairy) [2 338]])
 "Elapsed time: 96428.730988 msecs"
```

A near (and possibly entirely) optimal team of 5 pokemon would be: 
```
(Mega) Venasaur -> Poison Grass
(Mega) Heracross -> Bug Fighting
(Mega) Houndoom -> Fire Dark
(Mega) Diancie -> Rock, Fairy
Mamoswine -> Ground, Ice

Minimum type effectiveness against all existing types/hybrid types: 2x
Total type effectiveness against all existing types/hybrid types: 332
```
If a sixth pokemon is desired:
```
Mega Metagross -> Steel, Psychic Total Stats: 700
Lugia -> Psychic, flying Total stats: 680 

Total type effectiveness against all existing types/hybrid types: 338
```

Static copy of results from testing entire single-type and dual-type effectiveness from Gen VI with amount of pokemon types in loadout running from 1 to 12 (The max if all 6 pokemon are hybrids).:

```clojure
([(:ice) 339/2])
([(:ground :ice) 481/2])
([(:ground :rock :ice) 266N])
([(:fight :ground :rock :ice) 282N])
([(:fight :ground :rock :grass :ice) 294N] [(:ground :rock :grass :ice :fairy) 294N])
([(:fight :ground :rock :fire :grass :ice) 305N] [(:ground :rock :fire :grass :ice :fairy) 305])
([(:fight :ground :rock :bug :fire :grass :ice) 315N])
([(:fight :ground :rock :bug :fire :grass :ice :fairy) 323])
([(:fight :poison :ground :rock :bug :fire :grass :ice :fairy) 328]
 [(:fight :ground :rock :bug :ghost :fire :grass :ice :fairy) 328]
 [(:fight :ground :rock :bug :steel :fire :grass :ice :fairy) 328])
([(:fight :flying :poison :ground :rock :bug :fire :grass :ice :fairy) 332]
 [(:fight :flying :ground :rock :bug :ghost :fire :grass :ice :fairy) 332]
 [(:fight :flying :ground :rock :bug :steel :fire :grass :ice :fairy) 332]
 [(:fight :poison :ground :rock :bug :ghost :fire :grass :ice :fairy) 332]
 [(:fight :poison :ground :rock :bug :fire :grass :ice :dark :fairy) 332]
 [(:fight :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) 332]
 [(:fight :ground :rock :bug :steel :fire :grass :ice :dark :fairy) 332])
([(:fight :flying :poison :ground :rock :bug :ghost :fire :grass :ice :fairy) 336]
 [(:fight :flying :poison :ground :rock :bug :fire :grass :ice :dark :fairy) 336]
 [(:fight :flying :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) 336]
 [(:fight :flying :ground :rock :bug :steel :fire :grass :ice :dark :fairy) 336])
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
"Elapsed time: 308943.515934 msecs"
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
