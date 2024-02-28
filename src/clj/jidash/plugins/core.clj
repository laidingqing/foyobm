(ns jidash.plugins.core
  (:gen-class))

(defprotocol PointSource
  (load-source [this])
  (handler [this])
 )

