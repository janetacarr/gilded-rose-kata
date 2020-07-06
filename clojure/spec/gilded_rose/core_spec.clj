(ns gilded-rose.core-spec
  (:require [clojure.test :refer :all]
            [gilded-rose.core :refer [update-quality item update-current-inventory]]))

(deftest gilded-rose-test

  (is (= "foo" (:name (first (update-quality [(item "foo" 0 0)]))))))

(deftest test-cases
  (testing "whether or not the original case is covered"
    (let [inventory
          [
           (item "+5 Dexterity Vest" 10 20)
           (item "Aged Brie" 2 0)
           (item "Elixir of the Mongoose" 5 7)
           (item "Sulfuras, Hand Of Ragnaros" 0 80)
           (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
           ]
          control '({:name "+5 Dexterity Vest", :sell-in 9, :quality 19}
                    {:name "Aged Brie", :sell-in 1, :quality 1}
                    {:name "Elixir of the Mongoose", :sell-in 4, :quality 6}
                    {:name "Sulfuras, Hand Of Ragnaros", :sell-in -1, :quality 80}
                    {:name "Backstage passes to a TAFKAL80ETC concert",
                     :sell-in 14,
                     :quality 21})]
      (is (= control (update-quality inventory)))))
  (testing "the conjured use case"
    (let [inventory [(item "+5 Dexterity Vest" 10 20)
                     (item "Aged Brie" 2 0)
                     (item "Elixir of the Mongoose" 5 7)
                     (item "Sulfuras, Hand Of Ragnaros" 0 80)
                     (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
                     (item "Conjured Elixir of the Mongoose" 5 7)
                     (item "Conjured +5 Dexterity Vest" 0 3)]
          expected  '({:name "+5 Dexterity Vest", :sell-in 9, :quality 19}
                      {:name "Aged Brie", :sell-in 1, :quality 1}
                      {:name "Elixir of the Mongoose", :sell-in 4, :quality 6}
                      {:name "Sulfuras, Hand Of Ragnaros", :sell-in -1, :quality 80}
                      {:name "Backstage passes to a TAFKAL80ETC concert",
                       :sell-in 14,
                       :quality 21}
                      {:name "Conjured Elixir of the Mongoose"
                       :sell-in 4
                       :quality 5}
                      {:name "Conjured +5 Dexterity Vest"
                       :sell-in -1
                       :quality 0})]
      (is (= (update-quality inventory) expected)))))

(run-tests)
