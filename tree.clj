(ns tree
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def L-branch "└── ")
(def T-branch "├── ")
(def I-branch "│   ")
(def SPACE    "    ")

(declare tree)

(defn children [path]
  (map #(tree %) (.listFiles path)))

(defn tree [dir-name]
  (let [path (io/file dir-name)
        dir? (.isDirectory path)]
    {:name (.getName path)
     :children (if dir? (children path))}))

(defn render-tree [{name :name children :children}]
  (cons name
        (mapcat (fn [child index]
                  (let [last? (= index (dec (count children)))
                        prefix-first (if last? L-branch T-branch)
                        prefix-rest (if last? SPACE I-branch)
                        sub-tree (render-tree child)]
                    (cons (str prefix-first (first sub-tree))
                          (map #(str prefix-rest %) (rest sub-tree)))))
                children
                (range))))

(defn -main [& args]
  (->> (tree (first args))
      (render-tree)
      (str/join "\n")
      (println)))
