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

;; Helpers
(defn in-list
  "Checks if an item it in a list"
  [list item]
  (if (nil? (some #{item} list))
    false true))

(defn rank-counts
  "Rolls up a hand how many of each rank of card."
  [hand]
  (frequencies (map #(first %) hand)))

(defn has-card?
  "Does the hand have a specific card?"
  [hand card]
  (if (true? (some #(= card (first %)) hand))
    true false))

(defn card-value
  "Given card return an integer value for the card. Ace-high."
  [card & [ace-high?]]
  (cond
   (= card "a") (if (nil? ace-high?) 1 14)
   (= card "k") 13
   (= card "q") 12
   (= card "j") 11
   :else (. Integer parseInt card)))

;; Hand functions
(defn straight?
  "Is the hand a straight?"
  [hand]
  ;; Need both ace high and regular values.
  (let [vals-a (reverse (sort (map #(card-value (first %) :ace-high) hand)))
        vals (reverse (sort (map #(card-value (first %)) hand)))]

    (or
     (= (reduce #(if (= (- %1 %2) 1) %2 %1) vals) (apply min vals))
     (= (reduce #(if (= (- %1 %2) 1) %2 %1) vals-a) (apply min vals-a)))))

(defn flush?
  "Is the hand a Flush?"
  [hand]
  ;; If next expression is not nil then we have a flush
  (not (nil?
   ;; Reduce all the types, will reduce to "nil" if not all equal
        (reduce #(if (= %1 %2) %1 nil)
                ;; Get all the types
                (map #(second %) hand)))))

(defn royal-flush?
  "Is the hand a Royal Flush?"
  [hand]
  (and
   (flush? hand)
   (every? true? (map #(has-card? hand %) '("a" "k" "q" "j" "10")))))

(defn straight-flush?
  "Is the hand a Straight Flush?"
  [hand]
  (and (straight? hand) (flush? hand)))

(defn four-of-a-kind?
  "Does the hand have four of a kind?"
  [hand]
  (in-list (vals (rank-counts hand)) 4))

(defn full-house?
  "Is the hand a full house?"
  [hand]
  (and
   (in-list (vals (rank-counts hand)) 3)
   (in-list (vals (rank-counts hand)) 2)))

(defn three-of-a-kind?
  "Does the hand have three of a kind?"
  [hand]
  (in-list (vals (rank-counts hand)) 3))

(defn two-pair?
  "Does the hand have two-pair in it?"
  [hand]
  (>= (count (filter #(= % 2) (vals (rank-counts hand)))) 2))

(defn pair?
  "Does the hand have a pair in it?"
  [hand]
  (in-list (vals (rank-counts hand)) 2))

(defn high-card?
  "Returns true always, helps avoid a special case."
  [hand]
  true)

;; List of possible hands to check
(def possible-hands
  [["Royal Flush" royal-flush?]
   ["Straight Flush" straight-flush?]
   ["Four of a Kind" four-of-a-kind?]
   ["Full House" full-house?]
   ["Flush" flush?]
   ["Straight" straight?]
   ["Three of a Kind" three-of-a-kind?]
   ["Two Pair" two-pair?]
   ["One Pair" pair?]
   ["High Card" high-card?]])

;; Input parsing functions
(defn card-type
  "Given a string such as 'Ah' return the card type such as ('A', 'h')"
  [t]
  (let [m (re-find #"^([akqj]|2|3|4|5|6|7|8|9|10)([chds])$"
                   (s/lower-case (s/trim t)))]
    (cond (nil? m) nil
          :else (rest m))))

(defn rank-hand
  "Given a set of cards ranks the hand."
  [hand]
  (loop [x (first possible-hands) tail (rest possible-hands)]
    (if (or (empty? tail) (true? (apply (second x) [hand])))
      (first x)
      (recur (first tail) (rest tail)))))

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
