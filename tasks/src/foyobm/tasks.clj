(ns jidash.tasks
  (:require [babashka.curl :as curl]
            [babashka.fs :as fs]
            [babashka.process :as process]
            [babashka.tasks :refer [shell clojure]]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [clojure.string :as str]
            [clojure.stacktrace :as st]))