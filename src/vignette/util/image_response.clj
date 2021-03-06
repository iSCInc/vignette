(ns vignette.util.image-response
  (:require [clojure.java.io :refer [file]]
            [compojure.route :refer [not-found]]
            [ring.util.response :refer [response status header]]
            [digest :as digest]
            [vignette.media-types :refer :all]
            [vignette.util.query-options :refer :all]
            [vignette.storage.local :refer [create-stored-object]]
            [vignette.storage.protocols :refer :all]
            [vignette.util.thumbnail :refer :all]))

(declare create-image-response
         add-content-disposition-header
         add-surrogate-header
         surrogate-key)

(def error-image-file (file "public/brokenImage.jpg"))

(defmulti error-image (fn [map]
                        (:request-type map)))

(defmethod error-image :thumbnail [map]
  (when-let [thumb (original->thumbnail error-image-file map)]
    (create-stored-object thumb)))

(defmethod error-image :default [_]
  (create-stored-object error-image-file))

(defn error-response
  ([code map]
   (let [resp (if-let [image (error-image map)]
                (status (create-image-response image map) code)
                (not-found "Unable to fetch or generate image"))]
     (add-surrogate-header resp map)))
  ([code]
   (error-response code nil)))

(defn create-image-response
  ([image image-map]
   (-> (response (->response-object image))
       (header "Content-Type" (content-type image))
       (header "Content-Length" (content-length image))
       (header "X-Thumbnailer" "Vignette")
       (add-content-disposition-header image-map)
       (add-surrogate-header image-map)))
  ([image]
    (create-image-response image nil)))

(defn create-head-response
  [image-map]
  (-> (response "")
       ;(header "Content-Type" (content-type image))
       (header "X-Thumbnailer" "Vignette")
       (add-content-disposition-header image-map)
       (add-surrogate-header image-map)))

(defn add-content-disposition-header
  [response-map image-map]
  (if (original image-map)
    (let [requested-format (query-opt image-map :format)
          filename (cond-> (original image-map)
                     requested-format (str "." requested-format))]
      (header response-map "Content-Disposition" (format "inline; filename=\"%s\"" filename)))
    response-map))

(defn add-surrogate-header
  [response-map image-map]
  (if (and (wikia image-map)
           (original image-map)
           (image-type image-map))
    (let [sk (surrogate-key image-map)]
      (-> response-map
          (header "Surrogate-Key" sk)
          (header "X-Surrogate-Key" sk)))
    response-map))

(defn surrogate-key
  [image-map]
  (try
    (digest/sha1 (fully-qualified-original-path image-map))
    (catch Exception e
      (str "vignette-"(:original image-map)))))
