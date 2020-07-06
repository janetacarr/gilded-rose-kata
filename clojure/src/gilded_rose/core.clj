(ns gilded-rose.core
  (:require [clojure.string :as string]))

(defn update-quality [items]
  (let [item-quality (fn[item]
                       (let [{:keys [name sell-in quality]} item
                             rate (cond-> 1
                                    (or (string/includes? name "conjured")
                                        (string/includes? name "Conjured")) (* 2)
                                    (< sell-in 0) (* 2))
                             quality-decay (fn [quality delta mult]
                                             (let [new-quality (+ quality (* delta mult))]
                                               (if (< new-quality 0)
                                                 0
                                                 new-quality)))
                             aged-brie? (fn [name] (string/includes? name "Aged Brie"))
                             concert? (fn [name] (string/includes? name "Backstage passes to a TAFKAL80ETC concert"))
                             vest? (fn [name] (string/includes? name "+5 Dexterity Vest"))
                             elixir? (fn [name] (string/includes? name "Elixir of the Mongoose"))]
                         (cond
                           (and (< sell-in 0)
                                (concert? name))
                           (merge item {:quality 0})

                           (or (aged-brie? name)
                               (concert? name))
                           (cond
                             (and (concert? name)
                                  (>= sell-in 5)
                                  (< sell-in 10))
                             (update item :quality quality-decay 2 rate)

                             (and (concert? name)
                                  (>= sell-in 0)
                                  (< sell-in 5))
                             (update item :quality quality-decay 3 rate)

                             (< quality 50)
                             (update item :quality quality-decay 1 rate)

                             :else item)

                           (or (vest? name)
                               (elixir? name))
                           (update item :quality quality-decay -1 rate)

                           :else item)))
        decrease-sell-in (fn [item]
                           (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
                             (merge item {:sell-in (dec (:sell-in item))})
                             item))]
    (map item-quality
         (map decrease-sell-in items))))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory []
  (let [inventory
        [
         (item "+5 Dexterity Vest" 10 20)
         (item "Aged Brie" 2 0)
         (item "Elixir of the Mongoose" 5 7)
         (item "Sulfuras, Hand Of Ragnaros" 0 80)
         (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
         ]]
    (update-quality inventory)
    ))
