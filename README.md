# poker

I'm a program that evaluates poker hands given a set of cards.

## Usage

To use this first get [Leiningen](https://github.com/technomancy/leiningen), and then execute:

     $ lein run

Pass in any number of poker hands as STDIN into the program seperated by newlines, and the rank will be written to STDOUT. For example:

     Ah As 10c 7d 6s
     Pair of Aces

     Kh Kc 3s 3h 2d
     2 Pair

     Kh Qh 6h 2h 9h
     Flush

## License

Copyright © 2012 Mark Watson

Distributed under the Eclipse Public License, the same as Clojure.
