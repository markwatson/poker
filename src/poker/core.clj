(ns poker.core
  (:gen-class))
(use '[clojure.string :as s :only (trim lower-case split)])

(defn card-type
  "Given a string such as 'Ah' return the card type such as ('A', 'h')"
  [t]
  (let [m (re-find #"^([akqj]|2|3|4|5|6|7|8|9|10)([chds])$"
                   (s/lower-case (s/trim t)))]
    (cond (nil? m) nil
          :else (rest m))))

(defn rank-hand
  "Given a set of cards ranks the hand."
  [cards]
  cards)

(def invalid-cards-message "
There were one or more invalid cards. Valid cards are:
- King of Hearts = Kh
- Ace of Spades = As
- 7 of diamonds = 7d
...
")

(defn parse-hand
  "Parses a hand string and returns the set of cards."
  [hand]
  ;; Get a list of the cards
  (let [cards (map card-type (s/split hand #"\s+"))]
    (cond
     ;; If any cards failed to parse we have a problem.
     (every? #(not (nil? %)) cards) (println invalid-cards-message)

     ;; Otherwise return the rank
     :else (rank-hand cards))))

(defn read-hands
  "Loops over STDIN and reads commands and poker hands."
  []
  (let [rank (parse-hand (read-line))]
    (println rank))
  (read-hands))

(defn -main
  "Poker hand loop."
  [& args]
  (println "Enter a poker hand followed by [RET] to evaluate:")
  (read-hands))
