(ns poker.core
  (:gen-class))
(use '[clojure.string :as s :only (trim lower-case split)])

;; Strings
(def invalid-cards-message "
Error: There were one or more invalid cards. Valid cards are:
- King of Hearts = Kh
- Ace of Spades = As
- 7 of diamonds = 7d
...
")

(def hand-length-message "
Error: A hand must be 5 cards.")

(def intro-message "Enter poker hands followed by [RET] to evaluate.")


;; Possible hands



;; Helper functions
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

(defn parse-hand
  "Parses a hand string and returns the set of cards."
  [hand]
  ;; Get a list of the cards
  (let [cards (map card-type (s/split (s/trim hand) #"\s+"))]
    (cond
     ;; If any cards failed to parse we have a problem.
     (some nil? cards) invalid-cards-message

     ;; If we don't have a hand
     (not= (count cards) 5) hand-length-message

     ;; Otherwise return the rank
     :else (rank-hand cards))))

(defn read-hands
  "Loops over STDIN and reads commands and poker hands."
  []
  (let [rank (parse-hand (read-line))]
    (println rank)
    (println))
  (read-hands))


;; main
(defn -main
  "Poker hand loop."
  [& args]
  (println intro-message)
  (read-hands))
