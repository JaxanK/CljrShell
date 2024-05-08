(ns build
  (:require [clojure.string :as str])
  (:import  (System.IO Directory)
            (System Int32)))

;For our build file, we will have automatically generated version numbers. I
; rarely want to have to think about this, so the version of format
; Major.Minor.Patch will be used where the patch is automatically updated
; everytime we release a new build file to the release directory. The way this
; is done is by query of the release folder via get-latest-version
; function below

;The startup program runs a batch script that is designed to find the latest
;file of that format in release folder, so it works in parallel to this

;Finally, I want to be able to access and show the version number somewhere in
; the program and so easiest way I thought to do this is to simply have a version.clj
; file This build file will update the version.clj file via the
; update-version-file! fn. This will run prior to the build so it should show up
; properly in the build.


(def directory "...Fill this out...")
(def path-to-ver-file "C:\\Workspace\\CljrShell\\src\\CljrShell\\version.clj")

(defn get-latest-version "Chat-GPT generated function to find latest version in source directory" [directory]
  (let [jar-files (->> (Directory/GetDirectories directory)
                       (map #(re-find #"\d+\.\d+\.\d+" %))
                       (filter (complement nil?))
                       (map #(str/split % #"\."))
                       (sort-by #(. Int32 Parse  (last %)))
                       (sort-by #(. Int32 Parse (second %)))
                       (sort-by #(. Int32 Parse (first %)))
                       (last))]
    (if jar-files
      (let [[major minor patch] jar-files]
        (str major "." minor "." (inc (. Int32 Parse patch))))
      "0.0.0")))

(defn update-version-file! [new-version]
  (spit path-to-ver-file (str "(ns src.CljrShell.version \"Version Information for current build. Managed by build.clj\")\n;This File autoupdated and replaced by build.clj, so don't edit directly\n(def version \"" new-version "\")")))



(defn -main []
  ;Update Version Namespace to latest Version Number
  (def latest-version (get-latest-version directory))
  (update-version-file! latest-version)

  (def targetDir ".\\target")

  ;Delete Target Folder & Contents
  (if (Directory/Exists targetDir)
    (Directory/Delete targetDir true))

  ;Re-add Target Folder
  (Directory/CreateDirectory targetDir)

  ;Create Versioned Package Folder
  (def newPackageDir (str targetDir "\\CljrShell-" latest-version))
  (Directory/CreateDirectory newPackageDir)

  ;Copy Relevant files into package Folder is left for bat file xcopy
)


