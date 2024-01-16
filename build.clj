(ns build
  (:require
   [clojure.java.io :as io]
   [clojure.tools.build.api :as b]))


(def lib 'jidash)
(def version (format "0.1.%s" (b/git-count-revs nil)))
(def src-dirs ["src/clj" "resources"])
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))


(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs src-dirs
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs src-dirs
                  :class-dir class-dir})
  (spit (io/file "resources" (str (name lib) ".version")) version)
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main 'jidash.core
           :manifest {"Build-Number" version}}))