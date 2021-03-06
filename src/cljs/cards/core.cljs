(ns cards.core
  (:require [cljsjs.material-ui]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-frisk.core :refer [enable-re-frisk!]]
            [cards.events]
            [cards.subs]
            [cards.routes :as routes]
            [cards.views.index :as views]
            [cards.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (routes/app-routes)
  (dev-setup)
  (mount-root))
