(ns breakloop-repl.core
  (:require [clojure.main :as main]))

;;;; Separate development thread #1 -- probably less valuable?

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

;;;; Separate development thread #2 -- clearly more developed

;; probably unneeded

;; TODO WHAT WAS I DOING HERE?
;; IIRC I was experimenting with giving clj the kind of breakloop-centric repl that CL
;;   reportedly had.
;; If I'm understanding right from the below what's going on -- can I solve by including
;;   the full local context in the exception?

;; TODO probably unneeded

(set! *default-data-reader-fn* tagged-literal)

(defn print-call-stack
  "Like print-stack-trace, but doesn't wait for an exception. Sometimes
      it's useful to know what called a function, and AFAIK this is the easiest way
      of doing that."
  []
  (try (throw (Exception. "")) (catch Exception e (.printStackTrace e *out*))))

(defmacro local-context []
  (let [symbols (keys &env)]
    (zipmap (map (fn [sym] `(quote ~sym))
                 symbols)
            symbols)))

#_(defmethod print-dup java.lang.Throwable [h stream]
  (println "printing throwable")
  (.write stream "#=(um whatevs? ")
  (.write stream (str h)))

(defn contextual-eval [ctx expr]
  (println "evaling")
  (println "ctx:" ctx)
  (println "expr:" expr)
  (println "type:" (type expr))
  (let [r (eval
           `(let [~@(mapcat (fn [[k v]] [k `'~v]) ctx)]
              ~expr))]
    (println "done evaling")
    r
    ))

(defn quittable-reader [prompt exit-code]
  ;; (println "debug-reader reading")
  (let [input (main/repl-read prompt exit-code)]
    (if (or (= input :repl/quit)
            (= input 'q))
      exit-code
      input)))

(defn start-debug-repl
  [^Throwable x]
  ;; TODO YOUAREHERE just added print-call-stack here and below so I can compare them and
  ;; see how they differ -- my hypothesis is that by the time we get here, we're
  ;; higher up the call stack (because [I hypothesize] the :caught hook only kicks in
  ;; after the exception has percolated up the stack without being caught).
  (println "call stack in start-debug-repl:")
  (print-call-stack)
  (println "An error occurred:" (.getMessage x))
  (println "For complete details, (pp/pprint *e)")
  (println "Entering debug repl")
  (main/repl :prompt #(print "debug=> ")
             :read quittable-reader
             ;; TODO I think the problem here is that
             ;; a) the local-context includes the *error* (as "x", oddly)
             :eval (partial contextual-eval (local-context))))

(defn breakloop-repl
  []
  ;; TODO what if I grab local-context on the next line?
  (main/repl :caught start-debug-repl
             :read quittable-reader
             :prompt #(print "bl=> ")))

;; faster to type
(def bl breakloop-repl)

;; function with error
(defn f [a b c]
  (println "call stack in f:")
  (print-call-stack)
  (let [x a
        y b
        z c
        r (+ x y)
        s (* r z)]
    (+ (* r s) (/ x y))))




(comment
  ;; evaluating nested syntax quotes
  (let [x 9, y '(- x)]
    (println y)
    (println `y)
    (println ``y)
    (println ``~y)
    (println ``~~y)
    (println (contextual-eval {'x 36} ``~~y)))

  )
