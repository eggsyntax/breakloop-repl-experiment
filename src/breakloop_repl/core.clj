(ns breakloop-repl.core)

(defmacro break []
  `(clojure.main/repl
    :prompt #(print "debug=> ")
    :read readr
    :eval (partial contextual-eval (local-context))))

(defn blrep
  "Start a REPL whose behavior on error is to open an internal REPL preloaded
  with the context of the error."
  []
  )

;; (defn f [x]
;;   ;; (println "/" (type (var (symbol 'x))))
;;   #dbg
;;   (if (> x 4)
;;     (println "big x" x)
;;     (do
;;       (if (even? x)
;;        (println "small even x" x)
;;        (println "small odd x" x)))))

;; (f 3)
