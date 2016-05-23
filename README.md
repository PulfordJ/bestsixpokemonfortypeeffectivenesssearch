# poketype

Run program with  `lein run`.

Static copy of results from testing entire single-type and dual-type effectiveness from Gen VI with amount of pokemon types in loadout running from 1 to 12 (The max if all 6 pokemon are hybrids). The results are then sorted by the lowest type effectiveness score (Ice is at least 1/4 effective against every type, for example.) and then by total type effectiveness against all current single and hybrid type pokemon. This shows that with 9 pokemon types a trainer can be "super effective" (2x effective) or better against all other pokemon.:

```clojure
([(:ice) [1/4 339/2]] 
 [(:rock) [1/4 166N]] 
 [(:fire) [1/4 313/2]] 
 [(:flying) [1/4 305/2]])
([(:fighting :ghost) [1N 210N]]
 [(:ground :ice) [1/2 481/2]]
 [(:fighting :ice) [1/2 227N]]
 [(:ground :rock) [1/2 227N]])
([(:ground :rock :ice) [1 266N]]
 [(:ground :fire :ice) [1 264N]]
 [(:flying :ground :ice) [1 260N]]
 [(:ground :ice :fairy) [1 258N]])
([(:fighting :ground :rock :ice) [1 282N]]
 [(:ground :rock :grass :ice) [1N 281N]]
 [(:ground :rock :ice :fairy) [1 281N]]
 [(:fighting :ground :fire :ice) [1 280N]])
([(:fighting :ground :rock :grass :ice) [1N 294N]]
 [(:ground :rock :grass :ice :fairy) [1 294N]]
 [(:fighting :flying :ground :rock :ice) [1 293N]]
 [(:fighting :ground :rock :bug :ice) [1 293N]])
([(:fighting :ground :rock :fire :grass :ice) [1N 305N]]
 [(:ground :rock :fire :grass :ice :fairy) [1 305]]
 [(:fighting :flying :ground :rock :grass :ice) [1N 304N]]
 [(:fighting :ground :rock :bug :fire :ice) [1 304N]])
([(:fighting :ground :rock :bug :fire :grass :ice) [1N 315N]]
 [(:fighting :ground :rock :fire :grass :ice :fairy) [1 314]]
 [(:ground :rock :bug :fire :grass :ice :fairy) [1 314]]
 [(:fighting :flying :ground :rock :bug :grass :ice) [1N 313N]])
([(:fighting :ground :rock :bug :fire :grass :ice :fairy) [1 323]]
 [(:fighting :flying :ground :rock :bug :fire :grass :ice) [1N 321N]]
 [(:fighting :ground :rock :bug :ghost :fire :grass :ice) [1N 321N]]
 [(:ground :rock :bug :steel :fire :grass :ice :fairy) [1 321]])
([(:fighting :poison :ground :rock :fire :grass :ice :dark :fairy) [2 326]]
 [(:fighting :poison :ground :rock :fire :water :grass :dark :fairy) [2 316]]
 [(:fighting :poison :ground :rock :bug :fire :grass :ice :fairy) [1 328]]
 [(:fighting :ground :rock :bug :ghost :fire :grass :ice :fairy) [1 328]])
([(:fighting :poison :ground :rock :bug :ghost :fire :grass :ice :fairy) [2 332]]
 [(:fighting :poison :ground :rock :bug :fire :grass :ice :dark :fairy) [2 332]]
 [(:fighting :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) [2 332]]
 [(:fighting :ground :rock :bug :steel :fire :grass :ice :dark :fairy) [2 332]])
([(:fighting :flying :poison :ground :rock :bug :ghost :fire :grass :ice :fairy) [2 336]]
 [(:fighting :flying :poison :ground :rock :bug :fire :grass :ice :dark :fairy) [2 336]]
 [(:fighting :flying :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) [2 336]]
 [(:fighting :flying :ground :rock :bug :steel :fire :grass :ice :dark :fairy) [2 336]])
([(:fighting :flying :poison :ground :rock :bug :ghost :steel :fire :grass :ice :fairy) [2 338]]
 [(:fighting :flying :poison :ground :rock :bug :ghost :fire :water :grass :ice :fairy) [2 338]]
 [(:fighting :flying :poison :ground :rock :bug :ghost :fire :grass :electric :ice :fairy) [2 338]]
 [(:fighting :flying :poison :ground :rock :bug :ghost :fire :grass :psychic :ice :fairy) [2 338]])
 "Elapsed time: 96428.730988 msecs"
```

A near (and possibly entirely) optimal team of 5 pokemon would either of these: 
```
(print-best-combos '(:fighting :poison :ground :rock :bug :fire :grass
 :ice :dark :fairy))

([[10 3205]
  ("Mega Heracross"
   "Mega Venusaur"
   "Primal Groudon"
   "Mega Diancie"
   "Weavile")
  #{:fire :ground :bug :dark :fighting :grass :fairy :ice :rock
    :poison}]
 [[10 3166]
  ("Mega Heracross"
   "Drapion"
   "Primal Groudon"
   "Mega Diancie"
   "Mega Abomasnow")
  #{:fire :ground :bug :dark :fighting :grass :fairy :ice :rock
    :poison}])
```
If a sixth pokemon is desired:
```
Change: 
(Mega) Houndoom -> Chandelure (Fire Ghost)

Pick Either:
Mega Metagross -> Steel, Psychic Total Stats: 700
Lugia -> Psychic, flying Total stats: 680 

Total type effectiveness against all existing types/hybrid types: 338
```

Static copy of results from testing entire single-type effectiveness space from Gen VI:

```clojure
([(:poison :ground :rock :grass :dark :fairy)
  [(1 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 35]]
 [(:poison :ground :rock :ghost :grass :fairy)
  [(1 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 35]]
 [(:fighting :flying :ground :electric :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fighting :flying :ground :grass :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fighting :flying :ground :steel :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]]
 [(:fighting :flying :ground :ghost :electric :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fighting :flying :ground :ghost :grass :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1) 35]]
 [(:fighting :flying :ground :ghost :steel :ice)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]]
 [(:fighting :flying :poison :ground :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]]
 [(:fighting :flying :poison :ground :ghost :ice)
  [(2 2 2 2 2 2 2 2 2 2 1 2 2 2 2 2 2 2) 35]])
```

Also results from Gen V (Before Fairy and minor effectiveness changes in table).
```clojure
([(:fighting :flying :ground :electric :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]]
 [(:fighting :flying :ground :grass :ice :dark)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]]
 [(:fighting :flying :ground :ghost :electric :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]]
 [(:fighting :flying :ground :ghost :grass :ice)
  [(2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2) 34]])
```
The project uses [Midje](https://github.com/marick/Midje/).

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
